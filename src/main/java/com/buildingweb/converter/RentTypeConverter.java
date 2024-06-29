package com.buildingweb.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.RentType;
import com.buildingweb.model.RentTypeDTO;

@Component
public class RentTypeConverter {
    @Autowired
    private ModelMapper modelMapper;

    public RentTypeDTO toRentTypeDTO(RentType rentType){
        return modelMapper.map(rentType, RentTypeDTO.class);
    }
}