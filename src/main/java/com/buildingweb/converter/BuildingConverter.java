package com.buildingweb.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.Building;
import com.buildingweb.response.BuildingResponse;

@Component
public class BuildingConverter {
    @Autowired
    private ModelMapper modelMapper;

    public BuildingResponse toBuildingResponse(Building building){
        BuildingResponse buildingResponse = modelMapper.map(building, BuildingResponse.class);
        buildingResponse.setAddress(building.getStreet() + "," + building.getWard()+ "," + building.getDistrict().getName());
        buildingResponse.setLeasedArea(building.getRentAreas().stream().map(item -> item.getArea().toString()).collect(Collectors.joining(",")));
        return buildingResponse;
    }
}