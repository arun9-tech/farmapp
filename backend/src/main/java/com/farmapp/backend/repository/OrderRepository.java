package com.farmapp.backend.repository;

import com.farmapp.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByFarmerId(Long farmerId);

    List<Order> findByCustomerId(Long customerId);
}
