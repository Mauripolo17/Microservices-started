package com.example.order.service;

import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImp(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Optional<Order> updateOrder(UUID id, Order order) {
        return orderRepository.findById(id).map(
                orderInBD->{
                    orderInBD.setDate(order.getDate());
                    orderInBD.setPayment(order.getPayment());

                    return orderRepository.save(orderInBD);
                }
        );
    }
}
