package com.example.order.dto;

public record BaseResponse(String[] errorMessage) {
    public boolean hasError() {
        return errorMessage!=null && errorMessage.length > 0;
    }
}
