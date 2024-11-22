package com.ivms.ims.controller;

import com.ivms.ims.dto.OrderDTO;
import com.ivms.ims.model.OrderStatus;
import com.ivms.ims.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "Endpoints for managing orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create Order", description = "Create a new order of products")
    public OrderDTO createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get User's Order", description = "Get the orders of a user by his/her Id")
    public List<OrderDTO> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrder(id, null); // You can choose to filter by status if needed
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public OrderDTO updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(id, status);
    }

    @GetMapping("/usr/{id}")
    public OrderDTO getUserOrder(@PathVariable Long id) {
        return orderService.getOrder(id, null);
    }
}
