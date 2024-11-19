package com.bank.application.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)public class ExceptionResponse {
    private String message;
    private Map<String, Object> errors;
    private String exceptionClassName;

    public ExceptionResponse(String message, String exceptionClassName) {
        this.message = message;
        this.exceptionClassName = exceptionClassName;
    }

    public ExceptionResponse(String message, Map<String, Object> errors, String exceptionClassName) {
        this.message = message;
        this.errors = errors;
        this.exceptionClassName = exceptionClassName;
    }
}