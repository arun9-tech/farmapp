package com.farmapp.backend.controller;

import com.farmapp.backend.dto.OrderRequest;
import com.farmapp.backend.dto.OrderResponse;
import com.farmapp.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest req) {
        return orderService.placeOrder(req);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponse> getOrdersForCustomer(@PathVariable Long customerId) {
        return orderService.getOrdersForCustomer(customerId);
    }

    @GetMapping("/farmer/{farmerId}")
    public List<OrderResponse> getOrdersForFarmer(@PathVariable Long farmerId) {
        return orderService.getOrdersForFarmer(farmerId);
    }
}
