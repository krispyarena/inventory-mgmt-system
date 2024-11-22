package com.ivms.ims.repository;

import com.ivms.ims.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    @Query(value = "SELECT o.* FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE DATE(o.created_at) = CURDATE() " +
            "AND o.status = :status",
            nativeQuery = true)
    List<Order> findTodaysOrdersByStatus(@Param("status") String status);
}
