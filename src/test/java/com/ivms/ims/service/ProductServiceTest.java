package com.ivms.ims.service;

import com.ivms.ims.dto.ProductDTO;
import com.ivms.ims.exception.ProductNotFoundException;
import com.ivms.ims.model.Product;
import com.ivms.ims.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductById_ProductExists_ReturnsProduct() {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Product product = productService.getProductById(1L);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
    }

    @Test
    void getProductById_ProductDoesNotExist_ThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void updateStock_ValidStockUpdate_UpdatesSuccessfully() {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setQuantity(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        productService.updateStock(1L, -5);

        assertEquals(5, mockProduct.getQuantity());
        verify(productRepository, times(1)).save(mockProduct);
    }
}
