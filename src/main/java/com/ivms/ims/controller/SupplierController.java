package com.ivms.ims.controller;

import com.ivms.ims.model.Supplier;
import com.ivms.ims.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
@Tag(name = "Supplier API", description = "Endpoints for managing Suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    @Operation(summary = "Get All Suppliers", description = "Retrieving the list of all Suppliers")
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Supplier by Id", description = "Retrieving a Supplier by its Id")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        if (supplier.isPresent()) {
            return ResponseEntity.ok(supplier.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Adding Supplier", description = "Adding a Supplier into the system")
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierService.createSupplier(supplier);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updating a Supplier", description = "Updating a Supplier by its Id")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplierDetails) {
        Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDetails);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting a Supplier", description = "Removing a Supplier from the system")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    // Get suppliers with products (using custom query)
    @GetMapping("/with-products")
    @Operation(summary = "Get Suppliers with products", description = "Retrieving all Suppliers with their products")
    public List<Supplier> getSuppliersWithProducts() {
        return supplierService.getSuppliersWithProducts();
    }
}
