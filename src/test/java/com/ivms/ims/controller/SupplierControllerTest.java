package com.ivms.ims.controller;

import com.ivms.ims.model.Supplier;
import com.ivms.ims.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SupplierControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(supplierController).build();

        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test Supplier");
        supplier.setEmail("test@supplier.com");
        supplier.setPhone("123456789");
        supplier.setAddress("Test Address");
    }

    @Test
    void testGetAllSuppliers() throws Exception {
        List<Supplier> suppliers = Arrays.asList(supplier);

        when(supplierService.getAllSuppliers()).thenReturn(suppliers);

        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Supplier"));
    }

    @Test
    void testGetSupplierById() throws Exception {
        when(supplierService.getSupplierById(1L)).thenReturn(Optional.of(supplier));

        mockMvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Supplier"));
    }

    @Test
    void testCreateSupplier() throws Exception {
        when(supplierService.createSupplier(any(Supplier.class))).thenReturn(supplier);

        mockMvc.perform(post("/api/suppliers")
                        .contentType("application/json")
                        .content("{\"name\":\"Test Supplier\",\"email\":\"test@supplier.com\",\"phone\":\"123456789\",\"address\":\"Test Address\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Supplier"));
    }

    @Test
    void testUpdateSupplier() throws Exception {
        when(supplierService.updateSupplier(eq(1L), any(Supplier.class))).thenReturn(supplier);

        mockMvc.perform(put("/api/suppliers/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Updated Supplier\",\"email\":\"updated@supplier.com\",\"phone\":\"987654321\",\"address\":\"Updated Address\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Supplier"));
    }

    @Test
    void testDeleteSupplier() throws Exception {
        doNothing().when(supplierService).deleteSupplier(1L);

        mockMvc.perform(delete("/api/suppliers/1"))
                .andExpect(status().isNoContent());

        verify(supplierService, times(1)).deleteSupplier(1L);
    }

    @Test
    void testGetSuppliersWithProducts() throws Exception {
        List<Supplier> suppliers = Arrays.asList(supplier);

        when(supplierService.getSuppliersWithProducts()).thenReturn(suppliers);

        mockMvc.perform(get("/api/suppliers/with-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Supplier"));
    }
}
