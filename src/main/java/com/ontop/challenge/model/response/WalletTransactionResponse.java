package com.ontop.challenge.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WalletTransactionResponse  {

    @JsonProperty("wallet_transaction_id")
    private String walletTransactionId;
    private String amount;
    @JsonProperty("user_id")
    private String userId;

}
