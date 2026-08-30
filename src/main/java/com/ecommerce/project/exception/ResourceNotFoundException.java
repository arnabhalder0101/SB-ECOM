package com.ecommerce.project.exception;

public class ResourceNotFoundException extends RuntimeException{
    String resourceName;
    String field;
    String fieldName;
    String fieldId;

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }



}
