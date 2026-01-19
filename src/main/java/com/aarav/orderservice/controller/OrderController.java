package com.aarav.orderservice.controller;

import com.aarav.orderservice.dto.request.PlaceOrderRequest;
import com.aarav.orderservice.dto.response.MenuItemResponse;
import com.aarav.orderservice.dto.response.OrderItemResponse;
import com.aarav.orderservice.dto.response.OrderResponse;
import com.aarav.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest placeOrderRequest) {
        return ResponseEntity.ok(orderService.placeOrder(placeOrderRequest));
    }

    @GetMapping("/my")
    public Page<OrderResponse> getMyOrders(Pageable pageable) {
        return orderService.getMyOrders(pageable);
    }
}
