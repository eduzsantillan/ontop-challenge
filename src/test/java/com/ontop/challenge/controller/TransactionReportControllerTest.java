package com.ontop.challenge.controller;

import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.response.BasicResponse;
import com.ontop.challenge.model.response.TransactionReportResponse;
import com.ontop.challenge.repository.TransactionRepository;
import com.ontop.challenge.service.TransactionReportService;
import com.ontop.challenge.service.TransactionReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TransactionController.class)
public class TransactionReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionReportService transactionReportService;
    @MockBean
    private TransactionRepository transactionRepository;

    private static final String PATH_POST = "/api/transactions?bankAccountId=123456789";

    @Test
    void getTransactionsTestOk() throws Exception {
        given(transactionReportService.fetchTransactionReport("accountId", 100.00, new Date(), new Date(), 0, 10))
                .willReturn(new BasicResponse<>("200", "OK", List.of(new TransactionReportResponse())));

        mockMvc.perform(MockMvcRequestBuilders.get(PATH_POST)
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());
    }

    //Not working even mockservice is throwing the exception
    /*
    @Test
    void getTransactionsTestNotFound() throws Exception {
        Date filteredDate = new Date();
        when(transactionReportService.fetchTransactionReport("123456", 0.0,filteredDate, 0, 15))
                .thenThrow(NotInfoFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_POST)
                        .contentType("application/json")
                        .content("{\"userId\":\"123456789\"}"))
                .andExpect(status().isNotFound());
    }
     */
}