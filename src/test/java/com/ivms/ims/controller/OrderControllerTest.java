package com.ivms.ims.controller;

import com.ivms.ims.dto.OrderDTO;
import com.ivms.ims.model.OrderStatus;
import com.ivms.ims.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setTotalAmount(BigDecimal.valueOf(2400.00));
        orderDTO.setItems(Collections.emptyList());
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"totalAmount\":2400.00}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"totalAmount\":2400.00}"));

        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void getUserOrders_ShouldReturnUserOrders() throws Exception {
        List<OrderDTO> orders = List.of(orderDTO);
        when(orderService.getUserOrders(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1, \"totalAmount\":2400.00}]"));

        verify(orderService, times(1)).getUserOrders(1L);
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        when(orderService.getOrder(1L, null)).thenReturn(orderDTO);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"totalAmount\":2400.00}"));

        verify(orderService, times(1)).getOrder(1L, null);
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        orderDTO.setStatus(OrderStatus.CONFIRMED);
        when(orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED)).thenReturn(orderDTO);

        mockMvc.perform(put("/api/orders/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1, \"totalAmount\":2400.00, \"status\":\"COMPLETED\"}"));

        verify(orderService, times(1)).updateOrderStatus(1L, OrderStatus.CONFIRMED);
    }
}
