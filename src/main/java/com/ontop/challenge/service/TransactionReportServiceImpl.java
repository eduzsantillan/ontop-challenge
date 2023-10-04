package com.ontop.challenge.service;

import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.entity.Transaction;
import com.ontop.challenge.model.enums.ResponseCodes;
import com.ontop.challenge.model.response.BasicResponse;
import com.ontop.challenge.model.response.TransactionReportResponse;
import com.ontop.challenge.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TransactionReportServiceImpl implements TransactionReportService{

    private final TransactionRepository transactionRepository;

    @Override
    public BasicResponse<List<TransactionReportResponse>> fetchTransactionReport(String userId) {

        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        if(transactions.isEmpty()) {
            throw new NotInfoFoundException("No transactions found for user id: " + userId);
        }

        List<TransactionReportResponse> response = transactions.stream().map(transaction -> {
            TransactionReportResponse transactionReportResponse = new TransactionReportResponse();
            transactionReportResponse.setAmount(transaction.getInputAmount());
            transactionReportResponse.setStatus(transaction.getStatus());
            transactionReportResponse.setTransactionDate(transaction.getUpdatedAt());
            transactionReportResponse.setTypeAmount(transaction.getTypeAmount());
            transactionReportResponse.setTypeTransaction(transaction.getTypeTransaction());
            return transactionReportResponse;
        }).toList();
        return new BasicResponse<>(ResponseCodes.SUCCESS.getCode(), ResponseCodes.SUCCESS.getMessage(), response);
    }
}
