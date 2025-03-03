package com.example.jobhunter.utils;

import com.example.jobhunter.config.JwtConfig;
import com.example.jobhunter.domain.DTO.Response.ResLogin;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.example.jobhunter.config.JwtConfig.JWT_ALGORITHM;


@Component
public class JwtUtil {
    private JwtEncoder jwtEncoder;
    private JwtConfig jwtConfig;

    public JwtUtil(JwtEncoder jwtEncoder, JwtConfig jwtConfig) {
        this.jwtEncoder = jwtEncoder;
        this.jwtConfig = jwtConfig;
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtConfig.getKey()).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public Jwt decodeRefreshToken(String refreshToken) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(JWT_ALGORITHM)
                .build();
        try {
            Jwt decodedToken = jwtDecoder.decode(refreshToken);
            return decodedToken;
        } catch (Exception e) {
            System.out.println(">>> JWT error: " + e.getMessage());
            throw e;
        }
    }




    public String createAccessToken(String email, ResLogin resLogin) {
        ResLogin.UserInsideToken userInsideToken = new ResLogin.UserInsideToken();
        userInsideToken.setEmail(email);
        userInsideToken.setId(resLogin.getUser().getId());
        userInsideToken.setName(resLogin.getUser().getName());


        Instant now = Instant.now();
        Instant validity = now.plus(jwtConfig.getAccessTokenExpirationSecond(), ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userInsideToken)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }

    public String createRefreshToken(String email, ResLogin resLogin) {
        ResLogin.UserInsideToken userInsideToken = new ResLogin.UserInsideToken();
        userInsideToken.setEmail(email);
        userInsideToken.setId(resLogin.getUser().getId());
        userInsideToken.setName(resLogin.getUser().getName());

        Instant now = Instant.now();
        Instant validity = now.plus(jwtConfig.getRefreshTokenExpirationSecond(), ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userInsideToken)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }
}
