package com.ontop.challenge.service;

import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.entity.Transaction;
import com.ontop.challenge.model.enums.ResponseCodes;
import com.ontop.challenge.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionReportServiceTest {

    @InjectMocks
    private TransactionReportServiceImpl transactionReportService;

    @Mock
    private TransactionRepository transactionRepository;


    @Test
    void testGetTransactionReportOK() {
        List<Transaction> transactions = List.of(mockTransaction());
        String accountId = "accountId";
        Double amount = 0.0;
        Date startDate = null;
        Date endDate = null;
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        when(transactionRepository.findAllByBankAccountId(accountId,pageable)).thenReturn(transactions);
        assertThat(transactionReportService.fetchTransactionReport(accountId,
                amount,
                startDate,
                endDate,
                page,
                size).getStatus()).isEqualTo(ResponseCodes.SUCCESS.getCode());
    }
    @Test
    void testGetTransactionReportFailed_NoInfoFound() {
        List<Transaction> transactions = new ArrayList<>();
        String accountId = "accountId";
        Double amount = 0.0;
        Date startDate = null;
        Date endDate = null;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        when(transactionRepository.findAllByBankAccountId(accountId,pageable)).thenReturn(transactions);
        assertThrows(NotInfoFoundException.class, () -> {
            transactionReportService.fetchTransactionReport(accountId,
                    amount,
                    startDate,
                    endDate,
                    page,
                    size);
        });
    }



    private static Transaction mockTransaction()
    {
        Transaction transaction = new Transaction();
        transaction.setProviderTransactionId("transactionId");
        transaction.setUserId("userId");
        transaction.setAmount(1000);
        transaction.setTypeTransaction("withdraw");
        transaction.setTypeAmount("outcome");
        transaction.setCreatedAt(new Date());
        return transaction;
    }



}
