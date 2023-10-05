package com.ontop.challenge.service;

import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.entity.BankAccount;
import com.ontop.challenge.model.request.BankAccountRequest;
import com.ontop.challenge.repository.BankAccountRepository;
import com.ontop.challenge.utils.BankAccountValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service()
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Override
    public void submitBankAccount(BankAccountRequest request) {
       if(BankAccountValidation.validateBankAccount(request)) {
              bankAccountRepository.save(request.toEntity());
       }
    }

    @Override
    public BankAccount getBankAccountByBankAccountId(String bankAccountId) {
        return bankAccountRepository.findByBankAccountId(bankAccountId).orElseThrow(
                () -> new NotInfoFoundException("No bank account found for account id: " + bankAccountId));
    }


}
