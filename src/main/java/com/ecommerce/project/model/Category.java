package com.ecommerce.project.model;

public class Category {

    private Long catId;
    private String catName;



    private String catDescription;

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
