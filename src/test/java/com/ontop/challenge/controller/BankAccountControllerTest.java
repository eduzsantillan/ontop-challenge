package com.ontop.challenge.controller;

import com.ontop.challenge.exception.FieldsRequiredException;
import com.ontop.challenge.model.request.BankAccountRequest;
import com.ontop.challenge.service.BankAccountServiceImpl;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = BankAccountController.class)
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountServiceImpl bankAccountService;

    private static final String PATH_POST = "/api/bankaccount";

    @Test
    void submitBankAccountTest() throws Exception {
        Mockito.doNothing().when(bankAccountService).submitBankAccount(ArgumentMatchers.any(BankAccountRequest.class));
        mockMvc.perform(post(PATH_POST)
                .contentType("application/json")
                .content("{\"userId\":\"123456789\",\"accountNumber\":\"123456789\",\"routingNumber\":\"123456789\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"nationalId\":\"123456789\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void submitBankAccountTestBadRequest() throws Exception {
        Mockito.doThrow(FieldsRequiredException.class).when(bankAccountService).submitBankAccount(ArgumentMatchers.any(BankAccountRequest.class));
        mockMvc.perform(post(PATH_POST)
                .contentType("application/json")
                .content("{\"BadRequest\":\"123456789\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void submitBankAccountTestInternalServerError() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(bankAccountService).submitBankAccount(ArgumentMatchers.any(BankAccountRequest.class));
        mockMvc.perform(post(PATH_POST)
                        .contentType("application/json")
                        .content("{\"BadRequest\":\"123456789\"}"))
                .andExpect(status().is5xxServerError());
    }

}
