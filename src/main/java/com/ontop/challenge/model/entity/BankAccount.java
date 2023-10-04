package com.ontop.challenge.model.entity;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "bank-account")
public class BankAccount {

    @Id
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String routingNumber;
    private String accountNumber;
    private String nationalId;
    private String currency;

}
