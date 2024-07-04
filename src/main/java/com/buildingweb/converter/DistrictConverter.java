package com.buildingweb.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.District;
import com.buildingweb.model.DistrictDTO;

@Component
public class DistrictConverter {
    @Autowired
    private ModelMapper modelMapper;

    public DistrictDTO toDistrictDTO(District district){
        return modelMapper.map(district, DistrictDTO.class);
    }

    public District districtDTOToDistrict(DistrictDTO districtDTO){
        return modelMapper.map(districtDTO, District.class);
    }
}