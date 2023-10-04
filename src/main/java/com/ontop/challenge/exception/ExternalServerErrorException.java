package com.ontop.challenge.exception;

public class ExternalServerErrorException extends RuntimeException{
    public ExternalServerErrorException(String message) {
        super(message);
    }
}
