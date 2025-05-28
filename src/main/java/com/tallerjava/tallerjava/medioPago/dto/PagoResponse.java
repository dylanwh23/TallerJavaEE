// src/main/java/com/tallerjava/tallerjava/medioPago/dto/PagoResponse.java
package com.tallerjava.tallerjava.medioPago.dto;

public class PagoResponse {
    private boolean success;
    private String message;

    public PagoResponse() {}
    public PagoResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
