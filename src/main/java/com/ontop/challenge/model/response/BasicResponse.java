package com.ontop.challenge.model.response;


import lombok.Data;

import java.util.Optional;

@Data
public class BasicResponse<T> {

    private String status;
    private String message;
    private T data;

    public BasicResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public BasicResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

}
