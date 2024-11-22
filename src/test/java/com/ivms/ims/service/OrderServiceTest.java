package com.ivms.ims.service;

import com.ivms.ims.dto.OrderDTO;
import com.ivms.ims.exception.OrderNotFoundException;
import com.ivms.ims.model.Order;
import com.ivms.ims.model.OrderStatus;
import com.ivms.ims.model.User;
import com.ivms.ims.repository.OrderRepository;
import com.ivms.ims.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_ValidUserAndProducts_SuccessfulOrderCreation() {
        User mockUser = new User();
        mockUser.setId(1L);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Assuming convertToDTO is tested separately
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.valueOf(1000));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO createdOrder = orderService.createOrder(orderDTO);

        assertNotNull(createdOrder);
        assertEquals(1L, createdOrder.getId());
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrder_OrderExists_ReturnsOrder() {
        Order mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        OrderDTO orderDTO = orderService.getOrder(1L, OrderStatus.PENDING);

        assertNotNull(orderDTO);
        assertEquals(1L, orderDTO.getId());
        assertEquals(OrderStatus.PENDING, orderDTO.getStatus());
    }

    @Test
    void getOrder_OrderDoesNotExist_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(1L, OrderStatus.PENDING));
    }
}
