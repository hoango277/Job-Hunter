package com.example.jobhunter.utils.errors;


import com.example.jobhunter.domain.DTO.Response.ResponseDTO;
import com.example.jobhunter.utils.annotation.APIMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatResponse implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = httpResponse.getStatus();

        if(status >=400) {
            return body;
        }
        else {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setStatusCode(status);
            APIMessage message = returnType.getMethodAnnotation(APIMessage.class);
            responseDTO.setMessage(message.value());
            responseDTO.setData(body);
            return responseDTO;
        }
    }
}
