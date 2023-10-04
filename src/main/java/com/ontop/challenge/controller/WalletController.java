package com.ontop.challenge.controller;

import com.ontop.challenge.model.request.WalletTransactionRequest;
import com.ontop.challenge.model.response.BasicResponse;
import com.ontop.challenge.service.WalletService;
import com.ontop.challenge.service.WalletServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletServiceImpl walletService) {
        this.walletService = walletService;
    }



    @PostMapping("/transfer")
    public ResponseEntity<BasicResponse> processTransfer(@RequestBody WalletTransactionRequest request) {
        return ResponseEntity.ok().body(walletService.processTransfer(request));
    }

}
