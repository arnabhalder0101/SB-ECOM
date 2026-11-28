package com.ecommerce.project.payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private String streetName;

    @NotBlank
    @Size(min = 5, message = "Landmark name must be at least 5 characters")
    private String landmark;

    @NotBlank
    @Size(min = 6, message = "Pin code must be at least 6 characters")
    private String pinCode;

    @NotBlank
    @Size(min = 2, message = "Street name must be at least 2 characters")
    private String stateName;

    @NotBlank
    @Size(min = 4, message = "City name must be at least 4 characters")
    private String cityName;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters")
    private String countryName;
}
