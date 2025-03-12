package com.example.order.service;

import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImp orderService;

    @Test
    void findById() {
        UUID id = UUID.randomUUID();
        UUID listProduct = UUID.randomUUID();
        Order order = new Order();
        order.setId(id);
        order.setDate(LocalDate.parse("2023-12-03"));
        order.setProducts(Collections.singletonList(listProduct));

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Optional<Order> result = orderService.findById(id);
        assertThat(result).isPresent();
        assertThat(result.get().getDate()).isEqualTo("2023-12-03");
    }

    @Test
    void findAll() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setDate(LocalDate.parse("2023-12-03"));
        order.setProducts(Collections.singletonList(UUID.randomUUID()));

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        List<Order> result = orderService.findAll();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDate()).isEqualTo("2023-12-03");
    }

    @Test
    void save() {
        // Arrange
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setDate(LocalDate.parse("2023-12-03"));
        order.setProducts(Collections.singletonList(UUID.randomUUID()));

        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order savedOrder = orderService.save(order);

        // Assert
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getDate()).isEqualTo("2023-12-03");
    }

    @Test
    void deleteById() {
        // Arrange
        UUID id = UUID.randomUUID();


        // Act
        orderService.deleteById(id);

        // Assert
        Mockito.verify(orderRepository).deleteById(id);
    }

    @Test
    void updateOrder() {
            // Arrange
            UUID id = UUID.randomUUID();
            Order existingOrder = new Order();
            existingOrder.setId(UUID.randomUUID());
            existingOrder.setDate(LocalDate.parse("2023-12-03"));
            existingOrder.setProducts(Collections.singletonList(UUID.randomUUID()));
            existingOrder.setPayment(UUID.randomUUID());

            Order updatedOrder = new Order();
            updatedOrder.setId(UUID.randomUUID());
            updatedOrder.setDate(LocalDate.parse("2023-12-05"));
            updatedOrder.setProducts(Collections.singletonList(UUID.randomUUID()));
            updatedOrder.setPayment(UUID.randomUUID());



            when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));
            when(orderRepository.save(existingOrder)).thenReturn(updatedOrder);

            // Act
            Optional<Order> result = orderService.updateOrder(id, updatedOrder);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getDate()).isEqualTo("2023-12-05");
        }
}