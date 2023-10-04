package com.ontop.challenge.service;

import com.ontop.challenge.exception.FieldsRequiredException;
import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.entity.BankAccount;
import com.ontop.challenge.model.request.BankAccountRequest;
import com.ontop.challenge.repository.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Test
    void testSubmitBankAccountOK() {
        BankAccountRequest request = mockBankAccountRequest();
        bankAccountService.submitBankAccount(request);
        verify(bankAccountRepository,times(1)).save(request.toEntity());
    }

    @Test
    public void testSubmitBankAccountFailed_FieldsNull() {
        BankAccountRequest request = mockBankAccountRequest();
        request.setAccountNumber(null); ;
        assertThrows(FieldsRequiredException.class, () -> {
            bankAccountService.submitBankAccount(request);
        });
    }

    @Test
    public void testSubmitBankAccountFailed_FieldsEmpty() {
        BankAccountRequest request = mockBankAccountRequest();
        String empty_string = "";
        request.setAccountNumber(empty_string); ;
        assertThrows(FieldsRequiredException.class, () -> {
            bankAccountService.submitBankAccount(request);
        });
    }

    @Test
    public void testGetBankAccountByUserId() {
        String userId = "user123";
        BankAccount bankAccount = mockBankAccountRequest().toEntity();
        when(bankAccountRepository.findByUserId(userId)).thenReturn(Optional.of(bankAccount));
        BankAccount result = bankAccountService.getBankAccountByUserId(userId);

        assertThat(result).isNotNull();
    }

    @Test
    public void testGetBankAccountByUserIdNotFound() {
        String userId = "user123";
        when(bankAccountRepository.findByUserId(userId)).thenReturn(Optional.empty());
        assertThrows(NotInfoFoundException.class, () -> bankAccountService.getBankAccountByUserId(userId));
    }


    private static BankAccountRequest mockBankAccountRequest(){
        BankAccountRequest request= new BankAccountRequest();
        request.setFirstName("Rapahel");
        request.setLastName("Leao");
        request.setRoutingNumber("123456789");
        request.setAccountNumber("123456789");
        request.setUserId("123456789");
        request.setNationalId("123456789");
        return request;
    }

}
