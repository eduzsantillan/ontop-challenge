package com.ontop.challenge.infraestructure.endpoint;

import com.ontop.challenge.exception.ExternalServerErrorException;
import com.ontop.challenge.model.response.BalanceResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@AllArgsConstructor
@Component
public class GetBalanceEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(GetBalanceEndpoint.class);
    private static final String PATH_GET_BALANCE = "/wallets/balance?user_id={user_id}";
    @Qualifier("MockServiceRest")
    private final RestTemplate restTemplate;


    public double invoke(String userId) {
        try{
            ResponseEntity<BalanceResponse> response = restTemplate.getForEntity(PATH_GET_BALANCE, BalanceResponse.class, userId);
            return Objects.requireNonNull(response.getBody()).getBalance();
        }catch (Exception e){
            logger.error("Error consuming external api, path:{}", PATH_GET_BALANCE , e);
            throw new ExternalServerErrorException("Error getting balance, message obtained from external api: " + e);
        }

    }

}
