package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/category")
    public ResponseEntity<List<Category>> getAllCategory() {
        return
                new ResponseEntity<>(this.categoryService.getAllCategory(), HttpStatus.OK);
    }

    @PostMapping("/public/category")
    public ResponseEntity<String> addCategory(@Valid @RequestBody Category newCategory) {
        categoryService.addCategory(newCategory);
        return new ResponseEntity<>("category added successfully! \n" + newCategory.toString(), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/category/{catId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long catId) {

        try {
            String status = categoryService.deleteCategory(catId);
            return new ResponseEntity<>(status, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/category/{catId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long catId, @Valid @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(catId, category);
            return new ResponseEntity<>("Updated: " + updatedCategory.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
