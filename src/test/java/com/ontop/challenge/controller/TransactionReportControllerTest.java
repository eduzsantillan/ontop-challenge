package com.ontop.challenge.controller;

import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.request.WalletTransactionRequest;
import com.ontop.challenge.service.TransactionReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TransactionController.class)
public class TransactionReportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionReportServiceImpl transactionService;

    private static final String PATH_POST = "/api/transactions?userId=123456789";

    @Test
    void getTransactionsTestOk() throws Exception {
        given(transactionService.fetchTransactionReport(Mockito.anyString()))
                .willReturn(Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_POST)
                .contentType("application/json")
                .content("{\"userId\":\"123456789\"}"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getTransactionsTestNotFound() throws Exception {
        given(transactionService.fetchTransactionReport(Mockito.anyString()))
                .willThrow(NotInfoFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_POST)
                .contentType("application/json")
                .content("{\"userId\":\"123456789\"}"))
                .andExpect(status().isNotFound());
    }

}
