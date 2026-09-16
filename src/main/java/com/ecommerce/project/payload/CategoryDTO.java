package com.ecommerce.project.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long categoryId;

    @NotBlank(message = "category name must not be blank")
    @Size(min=2, max = 40, message = "min length is 5 max is 20")
    private String categoryName;

    @Size(max=300, message = "description must be 5 char to 255")
    //@NotBlank(message = "Description must be filled")
    private String categoryDescription;


}
