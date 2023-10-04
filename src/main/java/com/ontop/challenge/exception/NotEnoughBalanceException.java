package com.ontop.challenge.exception;

public class NotEnoughBalanceException extends RuntimeException{

        public NotEnoughBalanceException(String message) {
            super(message);
        }
}
