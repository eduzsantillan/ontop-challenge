package com.ontop.challenge.infraestructure.endpoint;

import com.ontop.challenge.exception.ExternalServerErrorException;
import com.ontop.challenge.exception.PaymentProviderApiException;
import com.ontop.challenge.model.request.PaymentRequest;
import com.ontop.challenge.model.response.PaymentProviderResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Component
public class CreatePaymentEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(CreatePaymentEndpoint.class);
    private static final String PATH_CREATE_PAYMENT = "/api/v1/payments";
    @Qualifier("MockServiceRest")
    private final RestTemplate restTemplate;

    public PaymentProviderResponse invoke(PaymentRequest request) {
        try{
            return restTemplate.postForObject(PATH_CREATE_PAYMENT, request, PaymentProviderResponse.class);
        }catch (Exception e){
            logger.error("Error consuming external api, path:{}", PATH_CREATE_PAYMENT , e);
            throw new PaymentProviderApiException("Error creating payment, message obtained from external api: " + e);
        }
    }
}
