package com.ontop.challenge.controller;

import com.ontop.challenge.exception.ExternalServerErrorException;
import com.ontop.challenge.exception.NotEnoughBalanceException;
import com.ontop.challenge.exception.NotInfoFoundException;
import com.ontop.challenge.model.request.WalletTransactionRequest;
import com.ontop.challenge.service.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
@WebMvcTest(controllers = WalletController.class)
public class WalletControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletServiceImpl walletService;

    private static final String PATH_POST = "/api/wallet/transfer";

    @Test
    void processTransferTestOk() throws Exception {
        given(walletService.processTransfer(ArgumentMatchers.any(WalletTransactionRequest.class)))
                .willReturn(Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_POST)
                .contentType("application/json")
                .content("{\"userId\":\"123456789\"}"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void processTransferTestBadRequest_NotEnoughBalance() throws Exception {
        given(walletService.processTransfer(ArgumentMatchers.any(WalletTransactionRequest.class)))
                .willThrow(NotEnoughBalanceException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_POST)
                .contentType("application/json")
                .content("{\"BadRequest\":\"123456789\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void processTransferTestBadRequest_NotInfoFound() throws Exception {
        given(walletService.processTransfer(ArgumentMatchers.any(WalletTransactionRequest.class)))
                .willThrow(NotInfoFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_POST)
                        .contentType("application/json")
                        .content("{\"BadRequest\":\"123456789\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void processTransferTest_InternalServerError() throws Exception {
        given(walletService.processTransfer(ArgumentMatchers.any(WalletTransactionRequest.class)))
                .willThrow(ExternalServerErrorException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_POST)
                        .contentType("application/json")
                        .content("{\"BadRequest\":\"123456789\"}"))
                .andExpect(status().is5xxServerError());
    }




}
