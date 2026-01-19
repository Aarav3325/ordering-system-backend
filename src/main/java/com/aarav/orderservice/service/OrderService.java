package com.aarav.orderservice.service;

import com.aarav.orderservice.dto.request.PlaceOrderRequest;
import com.aarav.orderservice.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse placeOrder(PlaceOrderRequest request);

    Page<OrderResponse> getMyOrders(Pageable pageable);
}
