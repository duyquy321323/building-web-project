package com.buildingweb.converter;

import java.util.stream.Collectors;

import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.Building;
import com.buildingweb.entity.RentArea;
import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.response.BuildingResponse;
import com.buildingweb.utils.UtilFunction;

import lombok.SneakyThrows;

@Component
public class BuildingConverter {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Condition<?, ?> skipNullAndBlank;

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

    @SneakyThrows
    public Building buildingRequestAddToBuilding(BuildingRequestAdd buildingRequestAdd) {
        Building building = modelMapper.map(buildingRequestAdd, Building.class);
        building.setRentTypes(
                buildingRequestAdd.getRentTypes().stream().map(it -> it.toString()).collect(Collectors.joining(",")));
        building.setRentAreas(UtilFunction.stringToListNumber(buildingRequestAdd.getRentArea()).stream()
                .map(it -> RentArea.builder().value(it).building(building).build()).collect(Collectors.toList()));
        if (buildingRequestAdd.getLinkOfBuilding() != null) {
            building.setLinkOfBuilding(buildingRequestAdd.getLinkOfBuilding().getBytes());
        }
        return building;
    }

    @SneakyThrows
    public void buildingRequestAddToBuildingExisted(BuildingRequestAdd buildingRequestAdd, Building building) {
        modelMapper.getConfiguration().setPropertyCondition(skipNullAndBlank);
        modelMapper.map(buildingRequestAdd, building);
        if (buildingRequestAdd.getRentTypes() != null && !buildingRequestAdd.getRentTypes().isEmpty()) {
            building.setRentTypes(buildingRequestAdd.getRentTypes().stream().map(it -> it.toString())
                    .collect(Collectors.joining(",")));
        }
        if (buildingRequestAdd.getRentArea() != null && !buildingRequestAdd.getRentArea().isBlank()) {
            building.getRentAreas().clear();
            building.getRentAreas().addAll(UtilFunction.stringToListNumber(buildingRequestAdd.getRentArea()).stream()
                    .map(it -> RentArea.builder().value(it).building(building).build()).collect(Collectors.toList()));
        }
        if (buildingRequestAdd.getLinkOfBuilding() != null && !buildingRequestAdd.getLinkOfBuilding().isEmpty()) {
            building.setLinkOfBuilding(buildingRequestAdd.getLinkOfBuilding().getBytes());
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNull());
    }
}