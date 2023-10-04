package com.ontop.challenge.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WalletTransactionRequest{



    private final String userId;
    private final double inputAmount;
    private double fee;

    @JsonIgnore
    private final double amount;


    public WalletTransactionRequest(String userId, double inputAmount, double fee) {
        this.userId = userId;
        this.inputAmount = inputAmount;
        this.fee = fee;
        this.amount = inputAmount - fee * inputAmount;
    }

    public String getUserId() {
        return userId;
    }
    public double getAmount() {
        return amount;
    }
    public double getFee() {
        return fee;
    }
    public double getInputAmount() {
        return inputAmount;
    }
}
