package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "street_name")
    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private String streetName;

    @Column(name = "landmark")
    @NotBlank
    @Size(min = 5, message = "Landmark name must be at least 5 characters")
    private String landmark;

    @Column(name = "pin_code")
    @NotBlank
    @Size(min = 6, message = "Pin code must be at least 6 characters")
    private String pinCode;

    @Column(name = "state_name")
    @NotBlank
    @Size(min = 2, message = "Street name must be at least 2 characters")
    private String stateName;

    @Column(name = "city_name")
    @NotBlank
    @Size(min = 4, message = "City name must be at least 4 characters")
    private String cityName;

    @Column(name = "country_name")
    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters")
    private String countryName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "addresses"
    , cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<User> user = new ArrayList<>();

}
