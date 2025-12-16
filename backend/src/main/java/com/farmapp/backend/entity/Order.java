package com.farmapp.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 PRODUCT
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // 🔗 FARMER
    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    // 🔗 CUSTOMER
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private int quantity;

    private double totalPrice;

    private String status;

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
