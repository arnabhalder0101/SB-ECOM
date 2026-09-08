package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;

    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();

        if(categories.isEmpty()){
            throw new APIException("No Data Exists.");
        }

        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(Category newCategory) {

        Category existingCategory = categoryRepository.findByCatName(newCategory.getCatName());

        if(existingCategory != null){
            throw new APIException("Category with category name \""+ newCategory.getCatName()+ "\" exists.");
        }
        categoryRepository.save(newCategory);


    }

    @Override
    public String deleteCategory(Long id) {

        /*
        List<Category> allCategories= categoryRepository.findAll();

        Category category = allCategories.stream()
                .filter(c -> c.getCatId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id));
        */

        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new APIException("Category with the ID: "+ id+ " does not  exists."));


        categoryRepository.delete(existingCategory);

        return "Deleted \n" + existingCategory;
    }

    @Override
    public Category updateCategory(Long id, Category category) {

        List<Category> allCategories= categoryRepository.findAll();

        Optional<Category> cat = allCategories.stream()
                .filter(c -> c.getCatId().equals(id))
                .findFirst();

        if (cat.isPresent()) {

            Category existingcategory = cat.get();

            // updates
            if (category.getCatName() != null)
                existingcategory.setCatName(category.getCatName());
            if (category.getCatDescription() != null)
                existingcategory.setCatDescription(category.getCatDescription());

            return categoryRepository.save(existingcategory);

        } else {
            throw new ResourceNotFoundException("Category", "Id", id);
        }


    }


}
