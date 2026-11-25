package com.ecommerce.project.controller;

import com.ecommerce.project.payload.APIResposne;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.payload.AllAddressesDTO;
import com.ecommerce.project.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<APIResposne> createUserAddress(@Valid @RequestBody AddressDTO addressDTO){

        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO);
        APIResposne response = new APIResposne("Address saved Successfully", savedAddressDTO, true);

        return ResponseEntity.status(201).body(response);

    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){

        AddressDTO fetchedAddress = addressService.getAddressById(addressId);

        return ResponseEntity.ok().body(fetchedAddress);

    }


    @GetMapping("/address")
    public ResponseEntity<AllAddressesDTO> getAllAddresses(){

        List<AddressDTO> fetchedAddresses = addressService.getAllAddress();

        AllAddressesDTO response = new AllAddressesDTO(fetchedAddresses.size(),
                "Addresses Fetched Successfully",fetchedAddresses);

        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/users/addresses")
    public ResponseEntity<AllAddressesDTO> getUserAddress(){

        List<AddressDTO> fetchedAddresses = addressService.getAddressByUser();

        AllAddressesDTO response = new AllAddressesDTO(fetchedAddresses.size(),
                "Addresses Fetched Successfully",fetchedAddresses);

        return ResponseEntity.ok().body(response);

    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<APIResposne> updateAddressById(@PathVariable Long addressId,
                                                        @RequestBody AddressDTO addressDTO){

        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);

        APIResposne resposne = new APIResposne("Address updated Successfully", updatedAddress, true);

        return ResponseEntity.ok().body(resposne);

    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<APIResposne> deleteAddressById(@PathVariable Long addressId){

        String status = addressService.deleteAddress(addressId);

        APIResposne response = new APIResposne(status, null, true);

        return ResponseEntity.ok().body(response);

    }


}
