package com.ecommerce.project.controller;


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


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/public/product")
    public ProductResponse getAllProducts() {

        ProductResponse productResponse = productService.getAllProducts();

        return productResponse;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {

        ProductDTO savedProduct = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(modelMapper.map(savedProduct, ProductDTO.class), HttpStatus.CREATED);

    }

    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId) {
        ProductResponse productResponse = productService.getProductByCategory(categoryId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword")
    public ResponseEntity<ProductResponse> getProductByCategory(@RequestParam String keyword) {
        ProductResponse productResponse = productService.getProductByKeyword(keyword);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<APIResposne> updateProduct(@PathVariable Long productId,
                                                     @RequestBody ProductDTO
                                                             productDTO){

        ProductDTO updatedProduct = productService.updateProduct(productDTO, productId);
        APIResposne apiResposne = new APIResposne("Updated successfully!",
                updatedProduct.toString(), true);


        return  new ResponseEntity<>(apiResposne, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<APIResposne> deleteProduct(@PathVariable Long productId){

        ProductDTO deletedProduct = productService.deleteProduct(productId);
        APIResposne apiResposne = new APIResposne("Deleted Successfully!", deletedProduct.toString(), true);
        return new ResponseEntity<>(apiResposne, HttpStatus.OK);
    }

}
