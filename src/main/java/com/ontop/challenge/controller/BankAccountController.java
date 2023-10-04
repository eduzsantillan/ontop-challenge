package com.ontop.challenge.controller;


import com.ontop.challenge.exception.FieldsRequiredException;
import com.ontop.challenge.model.request.BankAccountRequest;
import com.ontop.challenge.service.BankAccountService;
import com.ontop.challenge.service.BankAccountServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bankaccount")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountServiceImpl walletService) {
        this.bankAccountService = walletService;
    }

    @PostMapping("")
    public ResponseEntity<String> submitBankAccount(
            @RequestBody BankAccountRequest request){
        try{
            bankAccountService.submitBankAccount(request);
            return ResponseEntity.status(201).body("Bank account submitted");
        }catch (FieldsRequiredException fre){
            return ResponseEntity.badRequest().body(fre.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(500).body("Internal server error");
        }



    }
}
