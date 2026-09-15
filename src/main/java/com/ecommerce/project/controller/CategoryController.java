package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.APIResposne;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
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
    public ResponseEntity<CategoryResponse> getAllCategory() {
        return
                new ResponseEntity<>(this.categoryService.getAllCategory(), HttpStatus.OK);
    }

    @PostMapping("/public/category")
    public ResponseEntity<APIResposne> addCategory(@Valid @RequestBody Category newCategory) {
        CategoryDTO savedCategory = categoryService.addCategory(newCategory);
        APIResposne apiResposne = new APIResposne();

        apiResposne.setMessage("Category Created Successfully.");
        apiResposne.setObject(savedCategory.toString());
        apiResposne.setStatusCode(HttpStatus.CREATED);

        return new ResponseEntity<>(apiResposne, HttpStatus.CREATED);
    }

    @PutMapping("/admin/category/{catId}")
    public ResponseEntity<APIResposne> updateCategory(@PathVariable Long catId, @Valid @RequestBody Category category) {

        CategoryDTO updatedCategory = categoryService.updateCategory(catId, category);

        APIResposne apiResposne = new APIResposne();

        apiResposne.setMessage("Category Updated Successfully.");
        apiResposne.setObject(updatedCategory.toString());
        apiResposne.setStatusCode(HttpStatus.OK);

        return new ResponseEntity<>(apiResposne, HttpStatus.OK);


    }

    @DeleteMapping("/admin/category/{catId}")
    public ResponseEntity<APIResposne> deleteCategory(@PathVariable Long catId) {

        CategoryDTO deleteCategory = categoryService.deleteCategory(catId);

        APIResposne apiResposne = new APIResposne();

        apiResposne.setMessage("Category Deleted Successfully.");
        apiResposne.setObject(deleteCategory.toString());
        apiResposne.setStatusCode(HttpStatus.OK);

        return new ResponseEntity<>(apiResposne, HttpStatus.OK);

    }


}
