package com.controller.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiError {

    private HttpStatus status;
    @JsonProperty("Message")
    private String message;
}
