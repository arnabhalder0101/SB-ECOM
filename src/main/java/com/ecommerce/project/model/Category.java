package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank(message = "category name must not be blank")
    @Size(min=2, max = 40, message = "min length is 5 max is 20")
    private String categoryName;

    @Size(max=300, message = "description must be 5 char to 300")
    private String categoryDescription;

//    public String getCategoryDescription() {
//        return categoryDescription;
//    }
//
//    public void setCategoryDescription(String catDescription) {
//        this.categoryDescription = catDescription;
//    }
//
//    public Long getCategoryId() {
//        return categoryId;
//    }
//
//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public void setCategoryName(String catName) {
//        this.categoryName = categoryName;
//    }
//
//    public void setCatId(Long categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    @Override
//    public String toString() {
//        return "Category{" +
//                "catId=" + catId +
//                ", catName='" + catName + '\'' +
//                ", catDescription='" + catDescription + '\'' +
//                '}';
//    }

}
