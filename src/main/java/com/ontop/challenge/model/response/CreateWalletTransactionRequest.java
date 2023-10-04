package com.ontop.challenge.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class CreateWalletTransactionRequest {
    @JsonProperty("user_id")
    private String userId;
    private double amount;
}
