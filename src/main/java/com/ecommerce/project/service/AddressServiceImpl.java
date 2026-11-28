package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repository.AddressRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        User currentUser = authUtil.loggedInUser();
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(currentUser);

        Address savedAddress = addressRepository.save(address);

        List<Address> addresses = currentUser.getAddresses();
        addresses.add(savedAddress);

        // saving address to User table
        currentUser.setAddresses(addresses);

        userRepository.save(currentUser);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddress() {

        List<Address>  addressLists = addressRepository.findAll();

        List<AddressDTO> addressDTOs = addressLists.stream().map(
                add -> modelMapper.map(add, AddressDTO.class)
        ).toList();

        return addressDTOs;
    }

    @Override
    public List<AddressDTO> getAddressByUser() {
        User user = authUtil.loggedInUser();
        List<Address> addresses = addressRepository.getAddressesByUser(user.getUserId());

        List<AddressDTO> addressDTOs = addresses.stream().map(
                add -> modelMapper.map(add, AddressDTO.class)
        ).toList();
        return addressDTOs;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Address", "addressId", addressId)
                );
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Address", "addressId", addressId)
                );

        existingAddress.setLandmark(addressDTO.getLandmark());
        existingAddress.setCityName(addressDTO.getCityName());
        existingAddress.setPinCode(addressDTO.getPinCode());
        existingAddress.setStateName(addressDTO.getStateName());
        existingAddress.setCountryName(addressDTO.getCountryName());
        existingAddress.setStreetName(addressDTO.getStreetName());

        Address updatedAddress = addressRepository.save(existingAddress);


        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Address", "addressId", addressId)
                );

        addressRepository.delete(existingAddress);

        return "Address Deleted Successfully";
    }


}
