package com.example.jobhunter.controller;


import com.example.jobhunter.config.JwtConfig;
import com.example.jobhunter.domain.DTO.LoginDTO;
import com.example.jobhunter.domain.DTO.RegisterDTO;
import com.example.jobhunter.domain.DTO.Response.ResLogin;
import com.example.jobhunter.domain.DTO.Response.ResRegister;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.utils.JwtUtil;
import com.example.jobhunter.utils.SecurityUtils;
import com.example.jobhunter.utils.annotation.APIMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
@RequestMapping("${api.version}/auth")
public class AuthController {

    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private JwtUtil jwtUtil;
    private UserService userService;
    private JwtConfig jwtConfig;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          JwtUtil jwtUtil,
                          UserService userService,
                          JwtConfig jwtConfig) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/login")
    @APIMessage("Login")
    public ResponseEntity<ResLogin> login(@RequestBody LoginDTO login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                login.getUsername(),
                login.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);



        ResLogin resLogin = new ResLogin();
        User currentUser = userService.findByEmail(login.getUsername());
        ResLogin.UserLogin userLogin = new ResLogin.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName(),currentUser.getRole());

        resLogin.setUser(userLogin);
        String accessToken =  jwtUtil.createAccessToken(currentUser.getEmail(), resLogin);
        resLogin.setAccessToken(accessToken);



        String refreshToken = jwtUtil.createRefreshToken(login.getUsername(), resLogin);
        userService.setUserRefreshToken(refreshToken, currentUser);

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtConfig.getRefreshTokenExpirationSecond())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLogin);
    }

    @PostMapping("/register")
    @APIMessage("Register a new user")
    public ResponseEntity<ResRegister> register(@RequestBody RegisterDTO registerDTO) {
            User user = userService.register(registerDTO);
            ResRegister resRegister = new ResRegister();
            resRegister.setName(registerDTO.getName());
            resRegister.setEmail(registerDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    resRegister
            );
    }

    @GetMapping("/account")
    @APIMessage("Get user after F5")
    public ResponseEntity<ResLogin.UserGetAccount> getAccount() {
            String email = SecurityUtils.getCurrentUserLogin().isPresent()
                    ? SecurityUtils.getCurrentUserLogin().get() : null;

            User user = userService.findByEmail(email);
            ResLogin.UserLogin userLogin = new ResLogin.UserLogin(user.getId(), user.getEmail(), user.getName(), user.getRole());
            ResLogin.UserGetAccount userGetAccount = new ResLogin.UserGetAccount();
            userGetAccount.setUser(userLogin);
            return ResponseEntity.status(HttpStatus.OK).body(userGetAccount);
    }

    @GetMapping("/refresh")
    @APIMessage("Refresh token")
    public ResponseEntity<ResLogin> refreshToken(
            @CookieValue (name ="refreshToken", defaultValue = "abc") String refreshToken
    ) {

        Jwt jwt = jwtUtil.decodeRefreshToken(refreshToken);

        String email = jwt.getSubject();
        User user = userService.findByRefreshTokenAndEmail(refreshToken, email)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ResLogin resLogin = new ResLogin();

        ResLogin.UserLogin userLogin = new ResLogin.UserLogin(user.getId(), user.getEmail(), user.getName(), user.getRole());
        resLogin.setUser(userLogin);
        String accessToken =  jwtUtil.createAccessToken(email, resLogin);
        resLogin.setAccessToken(accessToken);


        String newRefreshToken = jwtUtil.createRefreshToken(email, resLogin);
        userService.setUserRefreshToken(refreshToken, user);

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtConfig.getRefreshTokenExpirationSecond())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLogin);
    }

    @PostMapping("/logout")
    @APIMessage("Logout")
    public ResponseEntity<Void> logout() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : null;
        User user = userService.findByEmail(email);
        userService.logout(user);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(null);
    }

}
