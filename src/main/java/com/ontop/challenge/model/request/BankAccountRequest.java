package com.ontop.challenge.model.request;

import com.ontop.challenge.model.entity.BankAccount;
import lombok.Data;

import java.util.UUID;

@Data
public class BankAccountRequest {
    private String userId;
    private String bankAccountId;
    private String firstName;
    private String lastName;
    private String routingNumber;
    private String accountNumber;
    private String nationalId;


    public BankAccount toEntity() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserId(this.userId);
        bankAccount.setFirstName(this.firstName);
        bankAccount.setLastName(this.lastName);
        bankAccount.setRoutingNumber(this.routingNumber);
        bankAccount.setAccountNumber(this.accountNumber);
        bankAccount.setNationalId(this.nationalId);
        bankAccount.setBankAccountId(UUID.randomUUID().toString());
        return bankAccount;
    }

}
