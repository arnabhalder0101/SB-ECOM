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
    @NotBlank(message = "Name must be filled")
    private String categoryName;

    @Size(min = 5, max = 20, message = "must be within 5 to 20 char")
    @NotBlank(message = "Description must be filled")
    private String categoryDescription;


}
