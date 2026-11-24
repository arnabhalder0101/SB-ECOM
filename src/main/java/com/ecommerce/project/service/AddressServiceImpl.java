package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
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
        User user = authUtil.loggedInUser();


        if(addressDTO == null){
            throw new APIException("Address is Null");
        }

        Address userAddress = new Address();

        List<User> users = new ArrayList<>();
        users.add(user);

        userAddress.setUser(users);
        userAddress.setStreetName(addressDTO.getStreetName());
        userAddress.setLandmark(addressDTO.getLandmark());
        userAddress.setPinCode(addressDTO.getPinCode());
        userAddress.setStateName(addressDTO.getStateName());
        userAddress.setCityName(addressDTO.getCityName());
        userAddress.setCountryName(addressDTO.getCountryName());

        Address savedAddress = addressRepository.save(userAddress);

//        Set<Address> addresses = new HashSet<>();
//        addresses.add(savedAddress);
//        user.setAddresses(addresses);

        user.getAddresses().add(savedAddress);
        User updatedUser = userRepository.save(user);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }
}
