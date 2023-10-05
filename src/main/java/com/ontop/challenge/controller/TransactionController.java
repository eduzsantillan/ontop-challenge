package com.ontop.challenge.controller;

import com.ontop.challenge.model.response.BasicResponse;
import com.ontop.challenge.model.response.TransactionReportResponse;
import com.ontop.challenge.service.TransactionReportService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionReportService transactionReportService;

    @GetMapping
    public ResponseEntity<BasicResponse<List<TransactionReportResponse>>> getTransactions(
            @RequestParam String bankAccountId,
            @RequestParam(required = false, defaultValue = "0") Double amount,
            @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,
            @RequestParam(required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyyMMdd") Date endDate,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size ) {
        return ResponseEntity.ok().body(transactionReportService.fetchTransactionReport( bankAccountId,
                amount,
                startDate,
                endDate,
                page,
                size));
    }
}
