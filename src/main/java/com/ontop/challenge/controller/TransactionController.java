package com.ontop.challenge.controller;

import com.ontop.challenge.model.response.BasicResponse;
import com.ontop.challenge.model.response.TransactionReportResponse;
import com.ontop.challenge.service.TransactionReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionReportService transactionReportService;

    @GetMapping
    public ResponseEntity<BasicResponse<List<TransactionReportResponse>>> getTransactions(@RequestParam String userId) {
        return ResponseEntity.ok().body(transactionReportService.fetchTransactionReport(userId));
    }
}
