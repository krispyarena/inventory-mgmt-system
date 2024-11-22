package com.ivms.ims.repository;

import com.ivms.ims.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(value = "SELECT p.name, SUM(oi.quantity) as total_quantity" +
            "FROM order_items oi" +
            "INNER JOIN products p ON oi.product_id = p.id" +
            "GROUP BY p.id" +
            "ORDER BY total_quantity DESC" +
            "LIMIT 10",
            nativeQuery = true)
    List<Object[]> findTopSellingProducts();
}
