package com.ontop.challenge.exception;

public class AmountLessOrEqualsThanZeroException extends RuntimeException{

    public AmountLessOrEqualsThanZeroException(String message) {
        super(message);
    }
}
