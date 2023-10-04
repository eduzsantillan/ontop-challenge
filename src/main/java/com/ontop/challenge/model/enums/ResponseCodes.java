package com.ontop.challenge.model.enums;

import lombok.Getter;

@Getter
public enum ResponseCodes {
    SUCCESS("00", "Success"),
    AMOUNT_LESS_THAN_ZERO("01", "Amount must be positive"),
    INSUFFICIENT_BALANCE("02", "Insufficient Balance"),
    NO_INFO_FOUND("03", "No info found for user"),
    SERVER_INTERNAL_ERROR("04", "Something happened in the server"),
    NO_TRANSACTIONS_FOUND("05", "No transactions found for user"),
    ERROR_CONSUMING_EXTERNAL_API("07", "Error consuming external api"),
    ;

    private final String code;
    private final String message;

    ResponseCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
