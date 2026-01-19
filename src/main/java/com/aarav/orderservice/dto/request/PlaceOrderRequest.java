package com.aarav.orderservice.dto.request;

import com.aarav.orderservice.entity.OrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlaceOrderRequest {

    @NotNull
    private List<OrderItemRequest> orderItems;

    public @NotNull List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public static class OrderItemRequest {

        private Long menuItemId;

        @Min(1)
        private int quantity;

        public Long getMenuItemId() {
            return menuItemId;
        }

        @Min(1)
        public int getQuantity() {
            return quantity;
        }
    }
}
