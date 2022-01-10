package com.controller.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@AllArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ObjectMapper objectMapper;

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    protected ResponseEntity<Object> handleEntityNotFound() {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "This resource was not found on GitHub.");
        return buildResponseEntity(apiError);
    }

    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError(status, ex.getMessage());
        return buildResponseEntity(apiError);

    }

    @SneakyThrows
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(objectMapper.writeValueAsString(apiError), apiError.getStatus());
    }

}

