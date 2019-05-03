package com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BadRequestEXception extends RuntimeException {
    public BadRequestEXception(String s) {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
    public class BadRequestException extends RuntimeException{

        public BadRequestException(String message) {
            super(message);
        }

        public Integer getCode() {
            return 400;
        }

        public String getDescription() {
            return "Bad request exception";
        }
    }
}