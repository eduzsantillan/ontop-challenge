package com.ontop.challenge.model.entity;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "wallet")
public class Wallet {
    @Id
    private ObjectId id;
    private String userId;
    private Double balance;
    private String currency;
}
