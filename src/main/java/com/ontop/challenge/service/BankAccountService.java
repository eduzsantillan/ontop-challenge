package com.ontop.challenge.service;

import com.ontop.challenge.model.entity.BankAccount;
import com.ontop.challenge.model.request.BankAccountRequest;

public interface BankAccountService {

    void submitBankAccount(BankAccountRequest request);
    BankAccount getBankAccountByBankAccountId(String bankAccountId);




}
