package com.ontop.challenge.model.request;

import lombok.Data;

@Data
public class PaymentRequest {



    private static final String SOURCE_TYPE = "COMPANY";
    private static final String SOURCE_NAME = "ONTOP INC";
    private static final String DEFAULT_CURRENCY = "USD";



    PaymentSource source;
    PaymentDestination destination;
    Double amount;



    @Data
    public static class Account {
        private String accountNumber;
        private String routingNumber;
        private String currency=DEFAULT_CURRENCY;
    }
    @Data
    public static class SourceInfo {
        private String name = SOURCE_NAME;
    }
    @Data
    public static class PaymentSource {
        private Account account;
        private String type= SOURCE_TYPE;
        private SourceInfo sourceInformation;
    }

    @Data
    public static class PaymentDestination {
        private String name;
        private Account account;
    }




}
