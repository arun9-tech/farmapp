package com.farmapp.backend.service;

import com.farmapp.backend.dto.OrderRequest;
import com.farmapp.backend.dto.OrderResponse;
import com.farmapp.backend.entity.Customer;
import com.farmapp.backend.entity.Farmer;
import com.farmapp.backend.entity.Order;
import com.farmapp.backend.entity.Product;
import com.farmapp.backend.repository.CustomerRepository;
import com.farmapp.backend.repository.OrderRepository;
import com.farmapp.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    // ✅ PLACE ORDER (Customer)
    public OrderResponse placeOrder(OrderRequest req) {

        Product product = productRepository.findById(req.productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Customer customer = customerRepository.findById(req.customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (product.getQuantityAvailable() < req.quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        // 🔻 reduce stock
        product.setQuantityAvailable(
                product.getQuantityAvailable() - req.quantity
        );

        Order order = new Order();
        order.setProduct(product);
        order.setFarmer(product.getFarmer());
        order.setCustomer(customer);
        order.setQuantity(req.quantity);
        order.setTotalPrice(req.quantity * product.getPricePerUnit());
        order.setStatus("PENDING");

        Order saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    // ✅ GET ORDERS FOR CUSTOMER
    public List<OrderResponse> getOrdersForCustomer(Long customerId) {

        List<Order> orders = orderRepository.findByCustomerId(customerId);

        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ GET ORDERS FOR FARMER
    public List<OrderResponse> getOrdersForFarmer(Long farmerId) {

        List<Order> orders = orderRepository.findByFarmerId(farmerId);

        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔒 PRIVATE MAPPER (IMPORTANT)
    private OrderResponse mapToResponse(Order order) {

        Product product = order.getProduct();
        Farmer farmer = order.getFarmer();
        Customer customer = order.getCustomer();

        OrderResponse res = new OrderResponse();
        res.orderId = order.getId();

        // product
        res.productId = product.getId();
        res.productName = product.getName();
        res.pricePerUnit = product.getPricePerUnit();

        // farmer
        res.farmerId = farmer.getId();
        res.farmerName = farmer.getName();
        res.farmName = farmer.getFarmName();
        // res.location = farmer.getLocation();

        // customer
        res.customerId = customer.getId();
        res.customerName = customer.getName();
        res.customerMobile = customer.getMobile();

        // order
        res.quantity = order.getQuantity();
        res.totalPrice = order.getTotalPrice();
        res.status = order.getStatus();

        return res;
    }
}
