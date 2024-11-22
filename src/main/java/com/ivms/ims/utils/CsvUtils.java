package com.ivms.ims.utils;

import com.ivms.ims.dto.ProductDTO;
import com.ivms.ims.dto.OrderDTO;
import com.ivms.ims.model.OrderStatus;
import com.ivms.ims.model.Product;
import com.ivms.ims.model.Order;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CsvUtils {

    public static void exportProductsToCsv(List<ProductDTO> products, OutputStream outputStream) throws IOException {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            String[] header = {"ID", "Name", "Price", "Quantity", "Supplier ID"};
            writer.writeNext(header);

            for (ProductDTO product : products) {
                String[] line = {
                        product.getId().toString(),
                        product.getName(),
                        product.getPrice().toString(),
                        product.getQuantity().toString(),
                        product.getSupplierId().toString()
                };
                writer.writeNext(line);
            }
        }
    }

    // Method to import products from CSV
    public static List<ProductDTO> importProductsFromCsv(MultipartFile file) throws IOException,CsvException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> lines = reader.readAll();
            return lines.stream().skip(1)  // Skip header row
                    .map(line -> {
                        ProductDTO productDTO = new ProductDTO();
                        productDTO.setId(Long.parseLong(line[0]));
                        productDTO.setName(line[1]);
                        productDTO.setPrice(new BigDecimal(line[2]));
                        productDTO.setQuantity(Integer.parseInt(line[3]));
                        productDTO.setSupplierId(Long.parseLong(line[4]));
                        return productDTO;
                    })
                    .collect(Collectors.toList());
        }
    }


    // Method to export orders to CSV
    public static void exportOrdersToCsv(List<OrderDTO> orders, OutputStream outputStream) throws IOException {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            String[] header = {"Order ID", "User ID", "Status", "Total Amount"};
            writer.writeNext(header);

            for (OrderDTO order : orders) {
                String[] line = {
                        order.getId().toString(),
                        order.getUserId().toString(),
                        order.getStatus().toString(),
                        order.getTotalAmount().toString()
                };
                writer.writeNext(line);
            }
        }
    }

    // Method to import orders from CSV
    public static List<OrderDTO> importOrdersFromCsv(MultipartFile file) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> lines = reader.readAll();
            return lines.stream().skip(1)  // Skip header row
                    .map(line -> {
                        OrderDTO orderDTO = new OrderDTO();
                        orderDTO.setId(Long.parseLong(line[0]));
                        orderDTO.setUserId(Long.parseLong(line[1]));
                        orderDTO.setStatus(OrderStatus.valueOf(line[2]));
                        orderDTO.setTotalAmount(new BigDecimal(line[3]));
                        return orderDTO;
                    })
                    .collect(Collectors.toList());
        }
    }

}
