package com.ecommerce.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "country_name")
    private String countryName;


}
