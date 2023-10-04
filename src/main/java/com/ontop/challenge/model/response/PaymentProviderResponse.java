package com.ontop.challenge.model.response;


import lombok.Data;

@Data
public class PaymentProviderResponse {


    private RequestInfo requestInfo;
    private PaymentInfo paymentInfo;

    @Data
    public static class RequestInfo {
        private String status;
    }

    @Data
    public static class PaymentInfo {
        private String id;
        private double amount;
    }


}


