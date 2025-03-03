package com.example.jobhunter.utils.errors;


import com.example.jobhunter.domain.DTO.Response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handle(MethodArgumentNotValidException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handle(ResponseStatusException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(ex.getStatusCode().value());
        errorResponse.setErrorMessage(ex.getReason());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ErrorResponse> handlePermissionException(PermissionException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorResponse.setErrorMessage("Forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


}
