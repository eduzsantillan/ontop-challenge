package com.ontop.challenge.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WalletTransactionRequest{

    private final String bankAccountId;
    private final double inputAmount;
    private final double fee;

    @JsonIgnore
    private final double amount;


    public WalletTransactionRequest(String bankAccountId, double inputAmount, double fee) {
        this.bankAccountId = bankAccountId;
        this.inputAmount = inputAmount;
        this.fee = fee;
        this.amount = inputAmount - fee * inputAmount;
    }



    public String getBankAccountId() {
        return bankAccountId;
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
