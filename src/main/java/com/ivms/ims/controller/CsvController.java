package com.ivms.ims.controller;

import com.ivms.ims.dto.ProductDTO;
import com.ivms.ims.dto.OrderDTO;
import com.ivms.ims.service.ProductService;
import com.ivms.ims.service.OrderService;
import com.ivms.ims.utils.CsvUtils;
import com.opencsv.exceptions.CsvException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/csv")
@Tag(name = "CSV API", description = "Endpoints for managing CSV files")
public class CsvController {

    private final ProductService productService;
    private final OrderService orderService;

    public CsvController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/products/export")
    @Operation(summary = "Export Products", description = "Exporting all the products in a CSV file")
    public byte[] exportProductsToCsv() throws IOException {
        List<ProductDTO> products = productService.getAllProducts();  // Fetch all products from the service
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            CsvUtils.exportProductsToCsv(products, outputStream);
            return outputStream.toByteArray();
        }
    }

    @PostMapping("/products/import")
    @Operation(summary = "Import Products", description = "Importing the products from a CSV file")
    public String importProductsFromCsv(@RequestParam("file") MultipartFile file) throws IOException, CsvException {
        List<ProductDTO> products = CsvUtils.importProductsFromCsv(file);
        productService.importProducts(products);  // Handle saving products in the service
        return "Products imported successfully!";
    }

    @GetMapping("/orders/export")
    @Operation(summary = "Export Orders", description = "Exporting all the orders in a CSV file")
    public byte[] exportOrdersToCsv() throws IOException {
        List<OrderDTO> orders = orderService.getAllOrders();  // Fetch all orders from the service
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            CsvUtils.exportOrdersToCsv(orders, outputStream);
            return outputStream.toByteArray();
        }
    }

    @PostMapping("/orders/import")
    @Operation(summary = "Import Orders", description = "Importing all the orders from a CSV file")
    public String importOrdersFromCsv(@RequestParam("file") MultipartFile file) throws IOException, CsvException {
        List<OrderDTO> orders = CsvUtils.importOrdersFromCsv(file);
        orderService.importOrders(orders);
        return "Orders imported successfully!";
    }
}
