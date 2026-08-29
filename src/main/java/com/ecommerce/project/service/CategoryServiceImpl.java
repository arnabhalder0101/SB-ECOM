package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    List<Category> categories = new ArrayList<>();
    Long nextId = 1L;

    @Override
    public List<Category> getAllCategory() {
        return categories;
    }

    @Override
    public void addCategory(Category newCategory) {
        newCategory.setCatId(nextId++);
        categories.add(newCategory);

    }

    @Override
    public String deleteCategory(Long id) {

        Category category = categories.stream()
                .filter(c -> c.getCatId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

        categories.remove(category);

        return "Deleted \n" + category;
    }

    @Override
    public Category updateCategory(Long id, Category category) {

        Optional<Category> cat = categories.stream()
                .filter(c -> c.getCatId().equals(id))
                .findFirst();

        if(cat.isPresent()){

            Category existingcategory = cat.get();

            // updates
            if (category.getCatName()!=null)
                existingcategory.setCatName(category.getCatName());
            if (category.getCatDescription()!=null)
                existingcategory.setCatDescription(category.getCatDescription());

            return existingcategory;

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found");
        }


    }


}
