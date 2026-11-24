package com.ecommerce.project.controller;

import com.ecommerce.project.payload.APIResposne;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<APIResposne> createUserAddress(@Valid @RequestBody AddressDTO addressDTO){

        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO);
        APIResposne response = new APIResposne();
        response.setMessage("Address saved Successfully");
        response.setObject(savedAddressDTO);
        response.setStatus(true);

        return ResponseEntity.ok().body(response);

    }

}
