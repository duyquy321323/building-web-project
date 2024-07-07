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

    public BuildingResponse toBuildingResponse(Building building) {
        StringBuilder address = new StringBuilder();
        BuildingResponse buildingResponse = modelMapper.map(building, BuildingResponse.class);
        if (building.getStreet() != null) {
            address.append(building.getStreet());
        }
        if (building.getWard() != null) {
            address.append("," + building.getWard());
        }
        if (building.getDistrict() != null) {
            address.append("," + building.getDistrict().getDistrictName());
        }
        if (!address.isEmpty()) {
            buildingResponse.setAddress(address.toString());
        }
        buildingResponse.setLeasedArea(building.getRentAreas().stream().map(item -> item.getValue().toString())
                .collect(Collectors.joining(",")));
        return buildingResponse;
    }

    public Building buildingRequestAddToBuilding(BuildingRequestAdd buildingRequestAdd) {
        Building building = modelMapper.map(buildingRequestAdd, Building.class);
        return building;
    }

    public Building buildingRequestAddToBuildingExisted(Long id, BuildingRequestAdd buildingRequestAdd) {
        Building building = modelMapper.map(buildingRequestAdd, Building.class);
        building.setId(id);
        return building;
    }
}