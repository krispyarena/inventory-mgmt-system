package com.ivms.ims.dto;

import com.ivms.ims.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO{
        private Long productId;
        private Integer quantity;
    }
}
