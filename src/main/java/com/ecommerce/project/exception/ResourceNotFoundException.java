package com.ecommerce.project.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName,  String fieldName, String field) {
        super(String.format("%s is not found with %s: %s", resourceName, fieldName, field));

    }

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldId) {
        super(String.format("%s is not found with %s: %d", resourceName, fieldName, fieldId));

    }


}
