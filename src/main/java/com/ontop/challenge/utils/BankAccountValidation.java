package com.ontop.challenge.utils;

import com.ontop.challenge.exception.FieldsRequiredException;
import com.ontop.challenge.model.request.BankAccountRequest;

public class BankAccountValidation {

    public static boolean validateBankAccount(BankAccountRequest request) {

        if(request.getFirstName()==null ||
                request.getLastName()==null ||
                request.getRoutingNumber()==null ||
                request.getAccountNumber()==null ||
                request.getNationalId()==null){
            throw new FieldsRequiredException("Some of the fields are null");
        }

        if(request.getFirstName().isEmpty() ||
                request.getLastName().isEmpty() ||
                request.getRoutingNumber().isEmpty() ||
                request.getAccountNumber().isEmpty() ||
                request.getNationalId().isEmpty()){
            throw new FieldsRequiredException("All fields are required");
        }
        return true;
    }



}
