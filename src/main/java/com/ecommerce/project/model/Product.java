package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @NotBlank(message = "Product name must not be blank")
    @Size(min=2, max = 40, message = "min length is 5 max is 20")
    private String productName;

    @Size(max=1000, message = "description must be 5 char to 1000")
    private String productDescription;

    @NotNull(message = "Product quantity must not be blank")
    private Integer productQuantity;

    @NotNull(message = "Product price must not be blank")
    private Double productPrice;

    private String productImage;

    private Double productDiscount;

    private Double productSpecialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
