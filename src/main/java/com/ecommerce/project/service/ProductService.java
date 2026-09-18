package com.ecommerce.project.service;


import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {
    public ProductResponse getAllProducts();
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
    public ProductResponse getProductByCategory(Long categoryId);
}
