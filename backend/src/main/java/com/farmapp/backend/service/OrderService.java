package com.farmapp.backend.service;

import com.farmapp.backend.dto.OrderRequest;
import com.farmapp.backend.entity.Customer;
import com.farmapp.backend.entity.Farmer;
import com.farmapp.backend.entity.Order;
import com.farmapp.backend.entity.Product;
import com.farmapp.backend.repository.CustomerRepository;
import com.farmapp.backend.repository.FarmerRepository;
import com.farmapp.backend.repository.OrderRepository;
import com.farmapp.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final FarmerRepository farmerRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        FarmerRepository farmerRepository,
                        CustomerRepository customerRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.farmerRepository = farmerRepository;
        this.customerRepository = customerRepository;
    }

    // ================= PLACE ORDER =================
    public Order placeOrder(OrderRequest req) {

        Product product = productRepository.findById(req.productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Customer customer = customerRepository.findById(req.customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Farmer farmer = product.getFarmer();

        if (product.getQuantityAvailable() < req.quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        product.setQuantityAvailable(
                product.getQuantityAvailable() - req.quantity
        );
        productRepository.save(product);

        Order order = new Order();
        order.setProduct(product);
        order.setFarmer(farmer);
        order.setCustomer(customer);
        order.setQuantity(req.quantity);
        order.setTotalPrice(req.quantity * product.getPricePerUnit());
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    // ================= FARMER ORDERS =================
    public List<Order> getOrdersForFarmer(Long farmerId) {
        // verify farmer exists
        farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
        return orderRepository.findByFarmerId(farmerId);
    }

    // ================= CUSTOMER ORDERS =================
    public List<Order> getOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // ================= UPDATE ORDER STATUS =================
    public Order updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!status.equals("ACCEPTED") && !status.equals("DELIVERED")) {
            throw new RuntimeException("Invalid order status");
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }
}
