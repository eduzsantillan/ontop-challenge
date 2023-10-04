package com.ontop.challenge.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionReportResponse {

    private String status;
    private Date transactionDate;
    private double amount;
    private String typeAmount;
    private String typeTransaction;

}
