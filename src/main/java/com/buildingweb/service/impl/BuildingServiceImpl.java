package com.buildingweb.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.BuildingConverter;
import com.buildingweb.entity.Building;
import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.exception.custom.NotAllowRoleException;
import com.buildingweb.repository.BuildingRepository;
import com.buildingweb.repository.UserRepository;
import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.request.BuildingRequestSearch;
import com.buildingweb.response.BuildingResponse;
import com.buildingweb.service.BuildingService;

import lombok.RequiredArgsConstructor;

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
        if (buildingRequest.getUserId() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && !(authentication instanceof AnonymousAuthenticationToken)) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails != null) {
                    if (!userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1).isManager()) {
                        throw new NotAllowRoleException("You must be a role manager to be allowed");
                    }
                }
            } else
                throw new NotAllowRoleException("You must be a role manager to be allowed.");
        }
        Page<Building> buildings = buildingRepository.findByBuildingRequestSearch(buildingRequest, pageable);
        return buildings.map((it) -> buildingConverter.toBuildingResponse(it));
    }

    @Override
    public void addNewBuilding(BuildingRequestAdd buildingRequest) {
        Building building = buildingConverter.buildingRequestAddToBuilding(buildingRequest);
        buildingRepository.save(building);
    }

    @Override
    public void updateBuilding(Long id, BuildingRequestAdd building) {
        if (buildingRepository.existsById(id)) {
            Building bui = buildingRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Building is not found"));
            buildingConverter.buildingRequestAddToBuildingExisted(building, bui);
            buildingRepository.save(bui);
        } else
            throw new EntityNotFoundException("Building is not found");
    }

    @Override
    public void deleteByListId(Long[] ids) {
        for (Long id : ids) {
            if (buildingRepository.existsById(id)) {
                buildingRepository.deleteByIdIn(ids);
                return;
            }
        }
        throw new EntityNotFoundException("Building not found");
    }

    @Override
    public Page<BuildingResponse> findByNameContaining(String s, Pageable pageable) {
        Page<Building> buildings = buildingRepository.findByNameContaining(s, pageable);
        if (buildings != null)
            return buildings.map((it) -> buildingConverter.toBuildingResponse(it));
        throw new EntityNotFoundException("Building is not found");
    }
}