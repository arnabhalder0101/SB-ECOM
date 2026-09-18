package com.ecommerce.project.repository;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public Product findByProductName(String productName);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = ?1")
    public List<Product> findAllByCategoryId(Long categoryId);

    public  List<Product> findByCategory(Category category);

    public  List<Product> findByProductNameContainingIgnoreCase(String keyword);
}
