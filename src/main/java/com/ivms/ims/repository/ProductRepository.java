package com.ivms.ims.repository;

import com.ivms.ims.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.* FROM products p" +
            "INNER JOIN suppliers s ON p.supplier_id = s.id " +
            "WHERE p.quantity < :threshold " +
            "AND s.name LIKE %:supplierName%",
            nativeQuery = true )
    List<Product> findLowStockProductsBySupplier(
            @Param("threshold") int threshold,
            @Param("supplierName") String supplierName
    );

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(BigDecimal minPrice,BigDecimal maxPrice);
}
