package com.ivms.ims.controller;

import com.ivms.ims.dto.OrderDTO;
import com.ivms.ims.dto.ProductDTO;
import com.ivms.ims.service.OrderService;
import com.ivms.ims.service.ProductService;
import com.ivms.ims.utils.CsvUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CsvController.class)
class CsvControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CsvUtils csvUtil;

    private ProductDTO productDTO;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Laptop");
        productDTO.setPrice(BigDecimal.valueOf(1200.00));
        productDTO.setQuantity(10);

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setTotalAmount(BigDecimal.valueOf(2400.00));
        orderDTO.setItems(Collections.emptyList());
    }

    @Test
    void exportProductsToCsv_ShouldReturnCsvFile() throws Exception {
        List<ProductDTO> products = List.of(productDTO);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(productService.getAllProducts()).thenReturn(products);

        // Mock the CSV utility method
        doNothing().when(csvUtil).exportProductsToCsv(eq(products), eq(outputStream));

        mockMvc.perform(get("/api/csv/products/export")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void importProductsFromCsv_ShouldReturnSuccessMessage() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "products.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "id,name,price,quantity\n1,Laptop,1200.00,10".getBytes()
        );

        List<ProductDTO> products = List.of(productDTO);

        when(csvUtil.importProductsFromCsv(mockFile)).thenReturn(products);
        doNothing().when(productService).importProducts(products);

        mockMvc.perform(multipart("/api/csv/products/import")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("Products imported successfully!"));

        verify(productService, times(1)).importProducts(products);
    }

    @Test
    void exportOrdersToCsv_ShouldReturnCsvFile() throws Exception {
        List<OrderDTO> orders = List.of(orderDTO);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(orderService.getAllOrders()).thenReturn(orders);

        // Mock the CSV utility method
        doNothing().when(csvUtil).exportOrdersToCsv(eq(orders), eq(outputStream));

        mockMvc.perform(get("/api/csv/orders/export")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk());

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void importOrdersFromCsv_ShouldReturnSuccessMessage() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "orders.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "id,totalAmount\n1,2400.00".getBytes()
        );

        List<OrderDTO> orders = List.of(orderDTO);

        when(csvUtil.importOrdersFromCsv(mockFile)).thenReturn(orders);
        doNothing().when(orderService).importOrders(orders);

        mockMvc.perform(multipart("/api/csv/orders/import")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("Orders imported successfully!"));

        verify(orderService, times(1)).importOrders(orders);
    }
}
