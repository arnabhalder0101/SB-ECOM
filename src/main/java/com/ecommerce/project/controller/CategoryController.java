package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
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
    public ResponseEntity<CategoryResponse> getAllCategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        CategoryResponse categoryResponse = this.categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/category")
    public ResponseEntity<APIResposne> addCategory(@Valid @RequestBody CategoryDTO newCategoryDTO) {
        CategoryDTO savedCategory = categoryService.addCategory(newCategoryDTO);

        APIResposne apiResposne = new APIResposne(
                "Category Created Successfully.",
                savedCategory.toString(),
                true);


        return new ResponseEntity<>(apiResposne, HttpStatus.CREATED);
    }

    @PutMapping("/admin/category/{catId}")
    public ResponseEntity<APIResposne> updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDTO categoryDTO) {

        CategoryDTO updatedCategory = categoryService.updateCategory(catId, categoryDTO);

        APIResposne apiResposne = new APIResposne("Category Updated Successfully.",
                updatedCategory.toString(),
                true
        );

        return new ResponseEntity<>(apiResposne, HttpStatus.OK);

    }

    @DeleteMapping("/admin/category/{catId}")
    public ResponseEntity<APIResposne> deleteCategory(@PathVariable Long catId) {

        CategoryDTO deleteCategory = categoryService.deleteCategory(catId);

        APIResposne apiResposne = new APIResposne("Category Deleted Successfully.",
                deleteCategory.toString(),
                true
                );

        return new ResponseEntity<>(apiResposne, HttpStatus.OK);

    }


}
