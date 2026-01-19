package com.aarav.orderservice.dto.response;

import java.math.BigDecimal;

public class OrderItemResponse {
    private String name;
    private int quantity;
    private BigDecimal price;

    public OrderItemResponse(String name, int quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
