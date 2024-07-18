package com.buildingweb.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.BuildingConverter;
import com.buildingweb.entity.Building;
import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.exception.custom.NotAllowRoleException;
import com.buildingweb.exception.custom.RequestNullException;
import com.buildingweb.repository.BuildingRepository;
import com.buildingweb.repository.UserRepository;
import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.request.BuildingRequestSearch;
import com.buildingweb.response.BuildingResponse;
import com.buildingweb.service.BuildingService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
@Transactional
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;

    @Autowired
    private BuildingConverter buildingConverter;

    @Override
    public Page<BuildingResponse> searchBuildingByBuildingRequest(BuildingRequestSearch buildingRequest,
            Pageable pageable) {
        if (buildingRequest != null) {
            if (buildingRequest.getUserId() != null) {
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                if (!userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1).isManager()) {
                    throw new NotAllowRoleException("You mustn't manager role");
                }
            }
            Page<Building> buildings = buildingRepository.findByBuildingRequestSearch(buildingRequest, pageable);
            return buildings.map((it) -> buildingConverter.toBuildingResponse(it));
        }
        throw new RequestNullException();
    }

    @Override
    @SneakyThrows
    public void addNewBuilding(BuildingRequestAdd buildingRequest) {
        if (buildingRequest != null) {
            Building building = buildingConverter.buildingRequestAddToBuilding(buildingRequest);
            buildingRepository.save(building);
            return;
        }
        throw new RequestNullException();
    }

    @Override
    @SneakyThrows
    public void updateBuilding(Long id, BuildingRequestAdd building) {
        if (id != null && building != null) {
            if (buildingRepository.existsById(id)) {
                Building bui = buildingRepository.findById(id).get();
                buildingConverter.buildingRequestAddToBuildingExisted(building, bui);
                buildingRepository.save(bui);
            } else
                throw new EntityNotFoundException("Building is not found");
            return;
        }
        throw new RequestNullException();
    }

    @Override
    public void deleteByListId(Long[] ids) {
        if (ids != null) {
            buildingRepository.deleteByIdIn(ids);
            return;
        }
        throw new RequestNullException();
    }

    @Override
    public Page<BuildingResponse> findByNameContaining(String s, Pageable pageable) {
        if (s != null && !s.isEmpty()) {
            Page<Building> buildings = buildingRepository.findByNameContaining(s, pageable);
            if (buildings != null)
                return buildings.map((it) -> buildingConverter.toBuildingResponse(it));
        }
        throw new RequestNullException("name is null or empty");
    }
}