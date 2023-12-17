package com.example.demo.entity;

public enum Status {
    IN_PROGRESS("Сборка заказа"),
    DONE("Готово");

    private String text;

    Status(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
