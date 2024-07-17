package com.buildingweb.service.impl;

import java.util.Optional;

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
        if (buildingRequest.getUser() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()
                    || authentication instanceof AnonymousAuthenticationToken) { // kiểm tra xem đã được xác thực hay
                                                                                 // chưa
                throw new NotAllowRoleException("You must authentication");
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (!userRepository.findByUsername(userDetails.getUsername()).isManager()) {
                throw new NotAllowRoleException("You mustn't manager role");
            }
        }
        Page<Building> buildings = buildingRepository.findByBuildingRequestSearch(buildingRequest, pageable);
        return buildings.map((it) -> buildingConverter.toBuildingResponse(it));
    }

    @Override
    @SneakyThrows
    public void addNewBuilding(BuildingRequestAdd buildingRequest) {
        Building building = buildingConverter.buildingRequestAddToBuilding(buildingRequest);
        buildingRepository.save(building);
    }

    @Override
    @SneakyThrows
    public void updateBuilding(Long id, BuildingRequestAdd building) {
        if (buildingRepository.existsById(id)) {
            Building newBuilding = buildingConverter.buildingRequestAddToBuildingExisted(id, building);
            buildingRepository.save(newBuilding);
        } else
            throw new EntityNotFoundException("Building is not found");
    }

    @Override
    public void deleteByListId(Long[] ids) {
        buildingRepository.deleteByIdIn(ids);
    }

    @Override
    public Page<BuildingResponse> findByNameContaining(String s, Pageable pageable) {
        Page<Building> buildings = buildingRepository.findByNameContaining(s, pageable);
        return buildings.map((it) -> buildingConverter.toBuildingResponse(it));
    }
}