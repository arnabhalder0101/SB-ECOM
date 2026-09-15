package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;

    }



    @Override
    public CategoryResponse getAllCategory() {
        List<Category> categories = categoryRepository.findAll();

        if(categories.isEmpty()){
            throw new APIException("No Data Exists.");
        }

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setLength(categoryDTOS.size());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(Category newCategory) {

        Category existingCategory = categoryRepository.findByCatName(newCategory.getCatName());

        if(existingCategory != null){
            throw new APIException("Category with category name \""+ newCategory.getCatName()+ "\" exists.");
        }
        Category savedCategory =  categoryRepository.save(newCategory);

        return modelMapper.map(savedCategory, CategoryDTO.class);

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
    public CategoryDTO updateCategory(Long id, Category category) {

//List<Category> allCategories= categoryRepository.findAll();

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id));


        // updates
        if (category.getCatName() != null)
            existingCategory.setCatName(category.getCatName());
        if (category.getCatDescription() != null)
            existingCategory.setCatDescription(category.getCatDescription());

        Category savedCategory = categoryRepository.save(existingCategory);

        return modelMapper.map(savedCategory, CategoryDTO.class);


    }


}
