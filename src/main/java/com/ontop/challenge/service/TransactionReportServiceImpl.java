package com.ontop.challenge.service;

import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.entity.Transaction;
import com.ontop.challenge.model.enums.ResponseCodes;
import com.ontop.challenge.model.response.BasicResponse;
import com.ontop.challenge.model.response.TransactionReportResponse;
import com.ontop.challenge.repository.TransactionRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;

@Service
public class TransactionReportServiceImpl implements TransactionReportService{

    private final TransactionRepository transactionRepository;

    public TransactionReportServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public BasicResponse<List<TransactionReportResponse>> fetchTransactionReport(String bankAccountId,
                                                                                 Double amount,
                                                                                Date startDate,
                                                                                   Date endDate,
                                                                                 int page,
                                                                                 int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        List<Transaction> transactions;

        if(amount>0 && (startDate!=null && endDate!=null)){
            transactions = transactionRepository.findAllByBankAccountIdAndAmountIsGreaterThanEqualAndUpdatedAtIsBetween(
                bankAccountId, amount, startDate, endDate, pageable
            );
        }else if(amount>0){
            transactions = transactionRepository.findAllByBankAccountIdAndAmountIsGreaterThanEqual(
                bankAccountId, amount, pageable);
        } else if (startDate!=null && endDate!=null){

            transactions = transactionRepository.findAllByBankAccountIdAndUpdatedAtIsBetween(
                bankAccountId, startDate, endDate, pageable
            );

        } else  {
            transactions = transactionRepository.findAllByBankAccountId(bankAccountId, pageable);
        }

        if(transactions.isEmpty()) {
            throw new NotInfoFoundException("No transactions found for user account with id: " + bankAccountId);
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
