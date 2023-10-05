package com.ontop.challenge.model.entity;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document(collection = "transaction")
public class Transaction {


    @Id
    private ObjectId id;
    private String walletTransactionId;
    private String providerTransactionId;
    private String bankAccountId;
    private String userId;
    private double inputAmount;
    private double fee;
    private double amount;
    private String typeTransaction;
    private String typeAmount;
    private String currency;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public void buildTransaction(String transactionId, String userId,
                                 double amount, double fee, double inputAmount,
                                 String typeAmount, String currency, String typeTransaction, String bankAccountId) {
        this.walletTransactionId = transactionId;
        this.userId = userId;
        this.typeAmount= typeAmount;
        this.inputAmount = inputAmount;
        this.fee = fee;
        this.amount = amount;
        this.currency = currency;
        this.typeTransaction = typeTransaction;
        this.bankAccountId = bankAccountId;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public void updateStatusTransaction(String status) {
        this.status = status;
        this.updatedAt = new Date();
    }

}
