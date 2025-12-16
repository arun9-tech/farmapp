package com.farmapp.backend.controller;

import com.farmapp.backend.dto.OrderRequest;
import com.farmapp.backend.dto.OrderResponse;
import com.farmapp.backend.entity.Order;
import com.farmapp.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.farmapp.backend.dto.CustomerOrderResponse;
import com.farmapp.backend.dto.FarmerOrderResponse;
import com.farmapp.backend.dto.OrderStatusRequest;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;   // ✅ THIS WAS MISSING

    // ✅ CONSTRUCTOR INJECTION (VERY IMPORTANT)
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest req) {

        Order order = orderService.placeOrder(req); // ✅ NOW IT WORKS

        OrderResponse res = new OrderResponse();
        res.orderId = order.getId();
        res.status = order.getStatus();
        res.quantity = order.getQuantity();
        res.totalPrice = order.getTotalPrice();

        OrderResponse.Product p = new OrderResponse.Product();
        p.id = order.getProduct().getId();
        p.name = order.getProduct().getName();
        p.pricePerUnit = order.getProduct().getPricePerUnit();
        p.unit = order.getProduct().getUnit();
        p.imageUrl = order.getProduct().getImageUrl();
        res.product = p;

        OrderResponse.Farmer f = new OrderResponse.Farmer();
        f.id = order.getFarmer().getId();
        f.name = order.getFarmer().getName();
        f.farmName = order.getFarmer().getFarmName();
        f.location = order.getFarmer().getLocation();
        res.farmer = f;

        OrderResponse.Customer c = new OrderResponse.Customer();
        c.id = order.getCustomer().getId();
        c.name = order.getCustomer().getName();
        c.mobile = order.getCustomer().getMobile();
        res.customer = c;

        return res;
    }
    @GetMapping("/farmer/{farmerId}")
    public List<FarmerOrderResponse> getFarmerOrders(@PathVariable Long farmerId) {

        List<Order> orders = orderService.getOrdersForFarmer(farmerId);

        return orders.stream().map(order -> {
            FarmerOrderResponse res = new FarmerOrderResponse();
            res.orderId = order.getId();
            res.productName = order.getProduct().getName();
            res.quantity = order.getQuantity();
            res.totalPrice = order.getTotalPrice();
            res.status = order.getStatus();
            res.customerName = order.getCustomer().getName();
            res.customerMobile = order.getCustomer().getMobile();
            return res;
        }).toList();
    }
    @PutMapping("/{orderId}/status")
    public OrderResponse updateStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusRequest req) {

        Order order = orderService.updateOrderStatus(orderId, req.status);

        OrderResponse res = new OrderResponse();
        res.orderId = order.getId();
        res.status = order.getStatus();
        res.quantity = order.getQuantity();
        res.totalPrice = order.getTotalPrice();

        return res;
    }
    @GetMapping("/customer/{customerId}")
    public List<CustomerOrderResponse> getCustomerOrders(
            @PathVariable Long customerId) {

        List<Order> orders = orderService.getOrdersForCustomer(customerId);

        return orders.stream().map(order -> {
            CustomerOrderResponse res = new CustomerOrderResponse();
            res.orderId = order.getId();
            res.productName = order.getProduct().getName();
            res.quantity = order.getQuantity();
            res.totalPrice = order.getTotalPrice();
            res.status = order.getStatus();
            res.farmerName = order.getFarmer().getName();
            res.farmName = order.getFarmer().getFarmName();
            return res;
        }).toList();
    }



}
