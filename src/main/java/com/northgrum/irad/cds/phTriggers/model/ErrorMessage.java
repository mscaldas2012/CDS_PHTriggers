package com.northgrum.irad.cds.phTriggers.model;


import lombok.Data;

@Data
public class ErrorMessage {
    private String code;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String code, String message) {

        this.code = code;
        this.message = message;
    }
}
