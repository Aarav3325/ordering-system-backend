package com.aarav.orderservice.service.impl;

import com.aarav.orderservice.dto.request.PlaceOrderRequest;
import com.aarav.orderservice.dto.response.OrderItemResponse;
import com.aarav.orderservice.dto.response.OrderResponse;
import com.aarav.orderservice.dto.response.OrderStatusUpdatesResponse;
import com.aarav.orderservice.entity.*;
import com.aarav.orderservice.exception.BadRequestException;
import com.aarav.orderservice.exception.ResourceNotFoundException;
import com.aarav.orderservice.repository.MenuItemRepository;
import com.aarav.orderservice.repository.OrderRepository;
import com.aarav.orderservice.security.JwtUtil;
import com.aarav.orderservice.security.SecurityUtil;
import com.aarav.orderservice.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final SecurityUtil securityUtil;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            MenuItemRepository menuItemRepository,
            SecurityUtil securityUtil,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.securityUtil = securityUtil;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        User user = securityUtil.getCurrentUser();

        Order order = new Order(
                user
        );

        List<OrderItem> orderItemList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PlaceOrderRequest.OrderItemRequest itemRequest : request.getOrderItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Menu item not found")
                    );

            if (!menuItem.isAvailable()) {
                throw new BadRequestException("Menu item not available");
            }

            BigDecimal itemTotal =
                    menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);

            orderItemList.add(
                    new OrderItem(
                            order,
                            menuItem,
                            itemRequest.getQuantity(),
                            menuItem.getPrice()
                    )
            );


        }


        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItemList);

        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    @Override
    public Page<OrderResponse> getMyOrders(Pageable pageable) {

        User user = securityUtil.getCurrentUser();

        return orderRepository.findByUserOrderByCreatedAtDesc(user, pageable).map(this::mapToOrderResponse);
    }

    public OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> items =
                order.getOrderItems()
                        .stream()
                        .map(
                                o -> new OrderItemResponse(
                                        o.getMenuItem().getName(),
                                        o.getQuantity(),
                                        o.getPrice()
                                )
                        )
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);

        Order savedOrder = orderRepository.save(order);

        messagingTemplate.convertAndSend(
                "/topic/orders/" + savedOrder.getUser().getId(),
                new OrderStatusUpdatesResponse(
                        savedOrder.getId(),
                        savedOrder.getStatus()
                )
        );
    }

}
