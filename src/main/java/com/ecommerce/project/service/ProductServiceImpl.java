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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    FileService fileService;

    @Autowired
    ModelMapper modelMapper;

    @Value("${project.image}")
    String path = "images/";

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        // pagination and sorting impl
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<ProductDTO> productDTOS = productPage
                .stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());


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
    public ProductResponse getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        // pagination and sorting impl
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "ID", categoryId));

        Page<Product> productPage = productRepository.findByCategory(category, pageDetails);

        if (productPage == null) {
            throw new ResourceNotFoundException("Product", "category ID", categoryId);
        }

        List<ProductDTO> productDTOS = productPage.stream()
                .map(p -> modelMapper.map(p, ProductDTO.class)).toList();

        ProductResponse pr = new ProductResponse();

        pr.setContent(productDTOS);
        pr.setPageNumber(productPage.getNumber());
        pr.setPageSize(productPage.getSize());
        pr.setTotalPages(productPage.getTotalPages());
        pr.setTotalElements(productPage.getTotalElements());
        pr.setLastPage(productPage.isLast());

        return pr;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        // pagination and sorting impl
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(keyword, pageDetails);

        if (productPage == null) {
            throw new ResourceNotFoundException("Product", "keyword", keyword);
        }

        List<ProductDTO> productDTOS = productPage.stream()
                .map(p -> modelMapper.map(p, ProductDTO.class)).toList();

        ProductResponse pr = new ProductResponse();

        pr.setContent(productDTOS);
        pr.setPageNumber(productPage.getNumber());
        pr.setPageSize(productPage.getSize());
        pr.setTotalPages(productPage.getTotalPages());
        pr.setTotalElements(productPage.getTotalElements());
        pr.setLastPage(productPage.isLast());

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
    public ProductDTO updateProductCategory( Long productId, Long categoryId) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));

        Category newCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        existingProduct.setCategory(newCategory);

        Product updatedProduct = productRepository.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(existingProduct);

        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);

        existingProduct.setProductImage(fileName);

        Product updatedProduct = productRepository.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

}
