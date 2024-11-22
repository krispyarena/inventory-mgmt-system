package com.ivms.ims.service;

import com.ivms.ims.dto.OrderDTO;
import com.ivms.ims.exception.OrderNotFoundException;
import com.ivms.ims.model.*;
import com.ivms.ims.repository.OrderRepository;
import com.ivms.ims.repository.ProductRepository;
import com.ivms.ims.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId()).
                orElseThrow(()-> new RuntimeException("User Not Found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for(OrderDTO.OrderItemDTO itemDTO : orderDTO.getItems()){
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(()-> new RuntimeException("Product Not Found"));

            productService.updateStock(product.getId(), itemDTO.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.getProduct().setQuantity(itemDTO.getQuantity());
            orderItem.getProduct().setPrice(product.getPrice());

            totalAmount = totalAmount.add(product.getPrice()
            .multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            orderItems.add(orderItem);

        }

        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    public OrderDTO getOrder(long id, OrderStatus status) {
        return orderRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(()-> new OrderNotFoundException("Order not found with id : " + id));

    }

    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id : " + id));


        if(status == OrderStatus.CANCELLED && order.getStatus() != OrderStatus.CANCELLED){
            for(OrderItem orderItem : order.getOrderItems()){
                productService.updateStock(
                        orderItem.getProduct().getId(),
                        orderItem.getProduct().getQuantity()
                );
            }
        }

        order.setStatus(status);
        return convertToDTO(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void importOrders(List<OrderDTO> orders) {
        for (OrderDTO orderDTO : orders) {
            User user = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User Not Found"));

            Order order = new Order();
            order.setUser(user);
            order.setStatus(orderDTO.getStatus());
            order.setTotalAmount(orderDTO.getTotalAmount());
            order.setOrderItems(orderDTO.getItems().stream().map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product Not Found")));
                return orderItem;
            }).collect(Collectors.toSet()));

            orderRepository.save(order);
        }
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

        List<OrderDTO.OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(item -> {
                    OrderDTO.OrderItemDTO itemDTO = new OrderDTO.OrderItemDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setQuantity(item.getProduct().getQuantity());
                    return itemDTO;
                }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

}
