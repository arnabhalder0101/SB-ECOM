package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CategoryService {
    public CategoryResponse getAllCategory();
    public CategoryDTO addCategory(Category newCategory);
    public CategoryDTO updateCategory(Long id, Category category);
    public String deleteCategory(Long id);
}
