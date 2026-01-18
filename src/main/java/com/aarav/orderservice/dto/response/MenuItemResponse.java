package com.aarav.orderservice.dto.response;


import java.math.BigDecimal;

public class MenuItemResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private boolean available;

    public MenuItemResponse(Long id, String name, BigDecimal price, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }
}
