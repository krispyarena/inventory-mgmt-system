package com.ivms.ims.repository;

import com.ivms.ims.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query(value = "SELECT s.* , COUNT(p.id) as product_count" +
            "FROM supplier s" +
            "LEFT JOIN products p ON s.id = p.supplier_id" +
            "GROUP BY s.id" +
            "HAVING product_count > 0",
            nativeQuery = true )
    List<Supplier> findSuppliersWithProducts();
}
