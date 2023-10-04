package com.ontop.challenge.service;

import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.entity.BankAccount;
import com.ontop.challenge.model.request.BankAccountRequest;
import com.ontop.challenge.repository.BankAccountRepository;
import com.ontop.challenge.utils.BankAccountValidation;
import org.springframework.stereotype.Service;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }


    @Override
    public void submitBankAccount(BankAccountRequest request) {
       if(BankAccountValidation.validateBankAccount(request)) {
              bankAccountRepository.save(request.toEntity());
       }
    }

    @Override
    public BankAccount getBankAccountByUserId(String userId) {
        return bankAccountRepository.findByUserId(userId).orElseThrow(
                () -> new NotInfoFoundException("No bank account found for user id: " + userId));
    }


}
