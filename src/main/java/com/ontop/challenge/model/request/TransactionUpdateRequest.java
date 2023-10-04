package com.ontop.challenge.model.request;

import lombok.Data;

@Data
public class TransactionUpdateRequest {

    private String providerTransactionId;
    private String status;

}
