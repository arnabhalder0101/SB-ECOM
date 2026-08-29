package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategory();
    public void addCategory(Category newCategory);

    public String deleteCategory(Long id);
    public Category updateCategory(Long id, Category category);
}
