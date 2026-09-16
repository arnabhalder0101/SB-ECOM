package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CategoryService {
    public CategoryResponse getAllCategory(Integer pageNum, Integer pageSize, String sortBy, String sortOrder);
    public CategoryDTO addCategory(CategoryDTO newCategory);
    public CategoryDTO updateCategory(Long id, CategoryDTO category);
    public CategoryDTO deleteCategory(Long id);
}
