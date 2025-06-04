package com.example.order.service;

import com.example.order.entity.Order;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    Optional<Order> findById(UUID id);

    List<Order> findAll();

    Order save(Order order) throws IllegalAccessException;

    void deleteById(UUID id);

    Optional<Order> updateOrder(UUID id, Order order);
}
