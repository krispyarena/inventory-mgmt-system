package com.ivms.ims.service;

import com.ivms.ims.model.Supplier;
import com.ivms.ims.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setEmail("test@supplier.com");
        supplier.setPhone("123456789");
        supplier.setAddress("Test Address");
    }

    @Test
    void testCreateSupplier() {
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        Supplier createdSupplier = supplierService.createSupplier(supplier);

        assertNotNull(createdSupplier);
        assertEquals("Test Supplier", createdSupplier.getName());
        verify(supplierRepository, times(1)).save(supplier);
    }

    @Test
    void testGetSupplierById() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        Optional<Supplier> foundSupplier = supplierService.getSupplierById(1L);

        assertTrue(foundSupplier.isPresent());
        assertEquals("Test Supplier", foundSupplier.get().getName());
    }

    @Test
    void testUpdateSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        Supplier updatedSupplier = new Supplier();
        updatedSupplier.setName("Updated Supplier");
        updatedSupplier.setEmail("updated@supplier.com");

        when(supplierRepository.save(any(Supplier.class))).thenReturn(updatedSupplier);

        Supplier result = supplierService.updateSupplier(1L, updatedSupplier);

        assertEquals("Updated Supplier", result.getName());
        assertEquals("updated@supplier.com", result.getEmail());
    }

    @Test
    void testDeleteSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(1L);

        verify(supplierRepository, times(1)).delete(supplier);
    }

    @Test
    void testGetSuppliersWithProducts() {
        // Mocking the custom query method
        List<Supplier> suppliersWithProducts = List.of(supplier);
        when(supplierRepository.findSuppliersWithProducts()).thenReturn(suppliersWithProducts);

        List<Supplier> result = supplierService.getSuppliersWithProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(supplierRepository, times(1)).findSuppliersWithProducts();
    }
}
