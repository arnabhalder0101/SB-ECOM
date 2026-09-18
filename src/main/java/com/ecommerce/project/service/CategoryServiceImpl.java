package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public CategoryResponse getAllCategory(Integer pageNum, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        System.out.println("DEBUG sortBy: [" + sortBy + "], sortOrder: [" + sortOrder + "]");

        // pageable obj
        Pageable pageDetails = PageRequest.of(pageNum, pageSize, sortByAndOrder);
        // JPA pageable repository method
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();

        if(categories.isEmpty()){
            throw new APIException("No Data Exists.");
        }

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO newCategoryDTO) {

        Category newCategory = modelMapper.map(newCategoryDTO, Category.class);

        Category existingCategory = categoryRepository.findByCategoryName(newCategory.getCategoryName());

        if(existingCategory != null){
            throw new APIException("Category with category name '"+ newCategory.getCategoryName()+ "' exists.");
        }
        Category savedCategory =  categoryRepository.save(newCategory);

        return modelMapper.map(savedCategory, CategoryDTO.class);

    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {

        Category category = modelMapper.map(categoryDTO, Category.class);

        // check for same name category
        Category sameNamedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(sameNamedCategory != null &&
                !Objects.equals(sameNamedCategory.getCategoryId(), id)){
            throw new APIException("Category exists with same name ::"+sameNamedCategory.getCategoryName());
        }

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id));


        // updates
        if (category.getCategoryName() != null)
            existingCategory.setCategoryName(category.getCategoryName());
        if (category.getCategoryDescription() != null)
            existingCategory.setCategoryDescription(category.getCategoryDescription());

        Category savedCategory = categoryRepository.save(existingCategory);

        return modelMapper.map(savedCategory, CategoryDTO.class);


    }

    @Override
    public CategoryDTO deleteCategory(Long id) {

        /*
        List<Category> allCategories= categoryRepository.findAll();

        Category category = allCategories.stream()
                .filter(c -> c.getCatId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id));
        */

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Id", id));

        categoryRepository.delete(existingCategory);

        return modelMapper.map(existingCategory, CategoryDTO.class);
    }




}
