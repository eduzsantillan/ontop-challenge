package com.ontop.challenge.exception;

import com.ontop.challenge.model.enums.ResponseCodes;
import com.ontop.challenge.model.response.BasicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<BasicResponse> handleNotEnoughBalanceException(NotEnoughBalanceException e) {
        logger.error("The balance obtained from server is less than amount requested", e);
        return ResponseEntity.badRequest().body(new BasicResponse<>(ResponseCodes.INSUFFICIENT_BALANCE.getCode(),
                ResponseCodes.INSUFFICIENT_BALANCE.getMessage()));
    }

    @ExceptionHandler(AmountLessOrEqualsThanZeroException.class)
    public ResponseEntity<BasicResponse> handleAmountLessOrEqualsThanZeroException(AmountLessOrEqualsThanZeroException e) {
        logger.error("The amount requested is less or equals than zero", e);
        return ResponseEntity.badRequest().body(new BasicResponse<>(ResponseCodes.AMOUNT_LESS_THAN_ZERO.getCode(),
                ResponseCodes.AMOUNT_LESS_THAN_ZERO.getMessage()));
    }

    @ExceptionHandler(NotInfoFoundException.class)
    public ResponseEntity<BasicResponse> handleNotInfoFoundException(NotInfoFoundException e) {
        logger.error("The information requested was not found", e);
        return ResponseEntity.status(404).body(new BasicResponse<>(ResponseCodes.NO_INFO_FOUND.getCode(),
                ResponseCodes.NO_INFO_FOUND.getMessage()));
    }

    @ExceptionHandler(ExternalServerErrorException.class)
    public ResponseEntity<BasicResponse> handleExternalServerErrorException(ExternalServerErrorException e) {
        logger.error("Error consuming external api", e);
        return ResponseEntity.internalServerError().body(new BasicResponse<>(ResponseCodes.ERROR_CONSUMING_EXTERNAL_API.getCode(),
                e.toString()));
    }



}
