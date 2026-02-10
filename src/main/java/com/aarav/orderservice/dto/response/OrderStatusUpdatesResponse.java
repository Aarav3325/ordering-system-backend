package com.aarav.orderservice.dto.response;

import com.aarav.orderservice.entity.OrderStatus;

public class OrderStatusUpdatesResponse {
    private Long orderId;
    private OrderStatus status;

    public OrderStatusUpdatesResponse(Long orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
