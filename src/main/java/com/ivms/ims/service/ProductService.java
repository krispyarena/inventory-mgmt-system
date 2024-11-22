package com.ivms.ims.service;

import com.ivms.ims.dto.ProductDTO;
import com.ivms.ims.exception.InsufficientStockException;
import com.ivms.ims.exception.ProductNotFoundException;
import com.ivms.ims.model.Product;
import com.ivms.ims.model.Supplier;
import com.ivms.ims.repository.ProductRepository;
import com.ivms.ims.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

private final ProductRepository productRepository;
private final SupplierRepository supplierRepository;

public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setDescription(productDTO.getDescription());

        return productRepository.save(product);
}



public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)  // Convert Product to ProductDTO
                .collect(Collectors.toList());
}

public void importProducts(List<ProductDTO> products) {
        for (ProductDTO productDTO : products) {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            productRepository.save(product);
        }
}


public Product updateProduct(Long id, ProductDTO productDTO) {
    Product product = productRepository.findById(id)
            .orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + id));

    product.setName(productDTO.getName());
    product.setPrice(productDTO.getPrice());
    product.setQuantity(productDTO.getQuantity());
    product.setDescription(productDTO.getDescription());

    updateProductFromDTO(product, productDTO);
    return productRepository.save(product);
}

public Product getProductById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
}

public void deleteProduct(Long id) {
    if (!productRepository.existsById(id)) {
        throw new ProductNotFoundException("Product not found with id : " + id);
    }
    productRepository.deleteById(id);
}

@Transactional(readOnly = true)
    public List<Product> getLowStockProducts(int threshold){
    return productRepository.findAll().stream()
            .filter(product -> product.getQuantity() < threshold)
            .collect(Collectors.toList());
}

public void updateStock(Long productId, int quantity) {
    Product product = productRepository.findById(productId)
            .orElseThrow(()-> new ProductNotFoundException("Product not found with id : " + productId));

    if(product.getQuantity() + quantity <0){
        throw new InsufficientStockException("Insufficient Stock for Product: " + product.getName());
    }

    product.setQuantity(product.getQuantity() + quantity);
    productRepository.save(product);
}

private void updateProductFromDTO(Product product, ProductDTO productDTO) {
    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    product.setQuantity(productDTO.getQuantity());

    if(productDTO.getSupplierId() != null){
        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                .orElseThrow(()-> new RuntimeException("Supplier not found"));

        product.setSupplier(supplier);
    }
}

private ProductDTO convertToDTO(Product product) {
    ProductDTO dto = new ProductDTO();
    dto.setId(product.getId());
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    dto.setQuantity(product.getQuantity());

    if(product.getSupplier() != null){
        dto.setSupplierId(product.getSupplier().getId());
    }

    return dto;
}


}
