package com.ontop.challenge.infraestructure.endpoint;

import com.ontop.challenge.exception.ExternalServerErrorException;
import com.ontop.challenge.model.response.CreateWalletTransactionRequest;
import com.ontop.challenge.model.response.WalletTransactionResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Component
public class CreateWalletTransactionEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(CreateWalletTransactionEndpoint.class);
    private static final String PATH_CREATE_TRANSACTION = "/wallets/transactions";

    @Qualifier("MockServiceRest")
    private final RestTemplate restTemplate;



    public WalletTransactionResponse invoke(CreateWalletTransactionRequest request){
        try{
            ResponseEntity<WalletTransactionResponse> response = restTemplate.postForEntity(
                    PATH_CREATE_TRANSACTION, request, WalletTransactionResponse.class);
            return response.getBody();
        }catch (Exception e){
            logger.error("Error consuming external api, path:{}", PATH_CREATE_TRANSACTION , e);
            throw new ExternalServerErrorException("Error creating wallet transaction, message obtained: " + e);
        }
    }



}
