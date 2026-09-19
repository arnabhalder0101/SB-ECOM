package com.ecommerce.project.service;


import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    public ProductResponse getAllProducts();
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
    public ProductResponse getProductByCategory(Long categoryId);
    public ProductResponse getProductByKeyword(String keyword);
    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);
}
