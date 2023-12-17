package com.example.demo.entity;

public class OrderResponse {
    private String message;
    private String data;

    public OrderResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return this.data;
    }

    public void setOrder(String data) {
        this.data = data;
    }
}
