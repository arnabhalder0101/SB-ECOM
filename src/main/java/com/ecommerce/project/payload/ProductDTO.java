package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long productId;

    @NotBlank(message = "Product name must not be blank")
    @Size(min=2, max = 40, message = "min length is 5 max is 20")
    private String productName;

    @Size(max=1000, message = "description must be 5 char to 1000")
    private String productDescription;

    @NotNull(message = "Product quantity must not be blank")
    private Integer productQuantity;

    @NotNull(message = "Product price must not be blank")
    private Double productPrice;

    private Double productDiscount;

    private Double productSpecialPrice;
}
