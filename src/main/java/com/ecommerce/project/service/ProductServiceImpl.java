package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ProductResponse getAllProducts() {
        List<ProductDTO> productDTOS = productRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);


        return productResponse;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Product product = modelMapper.map(productDTO, Product.class);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

        product.setCategory(category);
        if (product.getProductDiscount() != null) {
            double special_price = product.getProductPrice() - product.getProductPrice() * (product.getProductDiscount() * 0.01);
            product.setProductSpecialPrice(special_price);
        }
        Product sameNamedProduct = productRepository.findByProductName(product.getProductName());
        if (sameNamedProduct != null) {
            throw new APIException("Product with same name exists " + product.getProductName());
        }

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId) {

        //List<Product> products = productRepository.findAllByCategoryId(categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "ID", categoryId));
        List<Product> products = productRepository.findByCategory(category);

        if (products == null) {
            throw new ResourceNotFoundException("Product", "category ID", categoryId);
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(p -> modelMapper.map(p, ProductDTO.class)).toList();

        ProductResponse pr = new ProductResponse();
        pr.setContent(productDTOS);

        return pr;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword) {

        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(keyword);

        if (products == null) {
            throw new ResourceNotFoundException("Product", "keyword", keyword);
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(p -> modelMapper.map(p, ProductDTO.class)).toList();

        ProductResponse pr = new ProductResponse();
        pr.setContent(productDTOS);

        return pr;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        boolean calculateSpecialPrice = false;

        Product newProduct = modelMapper.map(productDTO, Product.class);

        Product sameNamedProduct = productRepository.findByProductName(newProduct.getProductName());

        if (sameNamedProduct != null &&
                sameNamedProduct.getProductId() != productId) {
            throw new APIException("Product with same name '" + sameNamedProduct.getProductName() + "' exists");
        }

        // check product existence with id
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (newProduct.getProductName() != null) {
            existingProduct.setProductName(newProduct.getProductName());
        }
        if (newProduct.getProductPrice() != null) {
            existingProduct.setProductPrice(newProduct.getProductPrice());
            calculateSpecialPrice = true;
        }
        if (newProduct.getProductDiscount() != null) {
            existingProduct.setProductDiscount(newProduct.getProductDiscount());
            calculateSpecialPrice = true;
        }
        if (newProduct.getProductDescription() != null) {
            existingProduct.setProductDescription(newProduct.getProductDescription());
        }
        if (newProduct.getProductImage() != null) {
            existingProduct.setProductImage(newProduct.getProductImage());
        }
        if (newProduct.getProductQuantity() != null) {
            existingProduct.setProductQuantity(newProduct.getProductQuantity());
        }

        if (calculateSpecialPrice) {

            double special_Price = existingProduct.getProductPrice() - (existingProduct.getProductPrice() * existingProduct.getProductDiscount() * 0.01);

            existingProduct.setProductSpecialPrice(special_Price);
        }

        Product savedProduct = productRepository.save(existingProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(existingProduct);

        return modelMapper.map(existingProduct, ProductDTO.class);
    }
}
