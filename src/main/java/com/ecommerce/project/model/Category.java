package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catId;

    @NotBlank(message = "category name must not be blank")
    @Size(min=2, max = 20, message = "min length is 5 max is 20")
    private String catName;

    @Size(max=255, message = "description must be 5 char to 255")
    private String catDescription;

    public Category() {
    }

    public Category(Long id, String name, String desc){
        this.catName = name;
        this.catId = id;
        this.catDescription = desc;
    }
    public String getCatDescription() {
        return catDescription;
    }

    public void setCatDescription(String catDescription) {
        this.catDescription = catDescription;
    }

    public Long getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    @Override
    public String toString() {
        return "Category{" +
                "catId=" + catId +
                ", catName='" + catName + '\'' +
                ", catDescription='" + catDescription + '\'' +
                '}';
    }

}
