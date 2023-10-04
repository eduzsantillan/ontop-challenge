package com.ontop.challenge.model.enums;

public enum TransactionTypes {
    WITHDRAWAL("WITHDRAWAL"),
    REFUND("REFUND"),
    PROGRESS("PROGRESS")
    ;
    private final String type;

    TransactionTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }



}
