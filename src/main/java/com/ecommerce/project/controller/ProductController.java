package com.ecommerce.project.controller;


import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.APIResposne;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/public/product")
    public ProductResponse getAllProducts(@RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                          @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                          @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                          @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {

        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        return productResponse;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {

        ProductDTO savedProduct = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(modelMapper.map(savedProduct, ProductDTO.class), HttpStatus.CREATED);

    }

    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId,
                                                                @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                                                @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder) {

        ProductResponse productResponse = productService.getProductByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword")
    public ResponseEntity<ProductResponse> getProductByCategory(@RequestParam String keyword,
                                                                @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                                                @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        ProductResponse productResponse = productService.getProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<APIResposne> updateProduct(@PathVariable Long productId,
                                                     @RequestBody ProductDTO
                                                             productDTO) {

        ProductDTO updatedProduct = productService.updateProduct(productDTO, productId);
        APIResposne apiResposne = new APIResposne("Updated successfully!",
                updatedProduct.toString(), true);


        return new ResponseEntity<>(apiResposne, HttpStatus.OK);
    }

    @PutMapping("/admin/products/category/{productId}")
    public ResponseEntity<APIResposne> updateProductCategory(@PathVariable Long productId,
                                                             @RequestParam(name = "categoryId") Long categoryId) {

        ProductDTO updatedProduct = productService.updateProductCategory(productId, categoryId);
        APIResposne apiResposne = new APIResposne("Category Updated successfully!",
                updatedProduct.toString(), true);


        return new ResponseEntity<>(apiResposne, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<APIResposne> deleteProduct(@PathVariable Long productId) {

        ProductDTO deletedProduct = productService.deleteProduct(productId);
        APIResposne apiResposne = new APIResposne("Deleted Successfully!", deletedProduct.toString(), true);
        return new ResponseEntity<>(apiResposne, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam(name = "image") MultipartFile image) throws IOException {

        ProductDTO updatedProduct = productService.updateProductImage(productId, image);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }


}
