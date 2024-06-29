package com.buildingweb.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.User;
import com.buildingweb.model.UserDTO;

@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;

    public UserDTO toUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}