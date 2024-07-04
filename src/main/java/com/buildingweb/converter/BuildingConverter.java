package com.buildingweb.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.Building;
import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.response.BuildingResponse;

@Component
public class BuildingConverter {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DistrictConverter districtConverter;

    public BuildingResponse toBuildingResponse(Building building){
        BuildingResponse buildingResponse = modelMapper.map(building, BuildingResponse.class);
        buildingResponse.setAddress(building.getStreet() + "," + building.getWard()+ "," + building.getDistrict().getName());
        buildingResponse.setLeasedArea(building.getRentAreas().stream().map(item -> item.getArea().toString()).collect(Collectors.joining(",")));
        return buildingResponse;
    }

    public Building buildingRequestAddToBuilding(BuildingRequestAdd buildingRequestAdd){
        Building building = modelMapper.map(buildingRequestAdd, Building.class);
        building.setDistrict(districtConverter.districtDTOToDistrict(buildingRequestAdd.getDistrict()));
        return building;
    }

    public Building buildingRequestAddToBuildingExisted(Integer id, BuildingRequestAdd buildingRequestAdd){
        Building building = modelMapper.map(buildingRequestAdd, Building.class);
        building.setId(id);
        building.setDistrict(districtConverter.districtDTOToDistrict(buildingRequestAdd.getDistrict()));
        return building;
    }
}