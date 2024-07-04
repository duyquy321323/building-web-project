package com.buildingweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.BuildingConverter;
import com.buildingweb.entity.Building;
import com.buildingweb.entity.RentArea;
import com.buildingweb.entity.RentType;
import com.buildingweb.entity.User;
import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.model.RentTypeDTO;
import com.buildingweb.repository.BuildingRepository;
import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.request.BuildingRequestSearch;
import com.buildingweb.response.BuildingResponse;
import com.buildingweb.service.BuildingService;
import com.buildingweb.utils.UtilFunction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;

    @PersistenceContext // inject đối tượng EntityManager vào do spring boot quản lý vòng đời của nó
    private EntityManager entityManager;

    @Autowired
    private BuildingConverter buildingConverter;
    @Override
    public List<BuildingResponse> searchBuildingByBuildingRequest(BuildingRequestSearch buildingRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); // lấy đối tượng CriteriaBuilder từ EntityManager
        CriteriaQuery<Building> query = criteriaBuilder.createQuery(Building.class); // khai báo kiểu dữ liệu mà bạn muốn truy vấn
        Root<Building> root = query.from(Building.class); // chỉ định đối tượng cơ sở truy vấn
        List<Predicate> predicates = new ArrayList<>(); // danh sách các truy vấn 
        if(UtilFunction.checkString(buildingRequest.getName())){
            predicates.add(criteriaBuilder.like(root.get("name"),"%" + buildingRequest.getName() + "%"));
        }
        if(UtilFunction.checkInteger(buildingRequest.getFloorArea())){
            predicates.add(criteriaBuilder.equal(root.get("floorArea"), buildingRequest.getFloorArea()));
        }
        if(buildingRequest.getDistrict() != null && UtilFunction.checkInteger(buildingRequest.getDistrict().getId())){
            predicates.add(criteriaBuilder.equal(root.get("district").get("id"), buildingRequest.getDistrict().getId()));
        }
        if(UtilFunction.checkString(buildingRequest.getWard())){
            predicates.add(criteriaBuilder.like(root.get("ward"), "%" + buildingRequest.getWard() + "%"));
        }
        if(UtilFunction.checkString(buildingRequest.getStreet())){
            predicates.add(criteriaBuilder.like(root.get("street"), "%" + buildingRequest.getStreet() + "%"));
        }
        if(UtilFunction.checkInteger(buildingRequest.getNumberOfBasement())){
            predicates.add(criteriaBuilder.equal(root.get("numberOfBasement"), buildingRequest.getNumberOfBasement()));
        }
        if(UtilFunction.checkString(buildingRequest.getDirection())){
            predicates.add(criteriaBuilder.like(root.get("direction"), "%" + buildingRequest + "%"));
        }
        if(UtilFunction.checkString(buildingRequest.getLevel())){
            predicates.add(criteriaBuilder.like(root.get("level"), buildingRequest.getLevel()));
        }

        // join diện tích thuê và building
        Join<Building,RentArea> joinBuildRentArea = root.join("rentAreas", JoinType.LEFT);
        if(UtilFunction.checkInteger(buildingRequest.getAreaFrom())){
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(joinBuildRentArea.get("area"), buildingRequest.getAreaFrom()));
        }
        if(UtilFunction.checkInteger(buildingRequest.getAreaTo())){
            predicates.add(criteriaBuilder.lessThanOrEqualTo(joinBuildRentArea.get("area"), buildingRequest.getAreaTo()));
        }
        if(UtilFunction.checkInteger(buildingRequest.getRentPriceFrom())){
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rentPrice"), buildingRequest.getRentPriceFrom()));
        }
        if(UtilFunction.checkInteger(buildingRequest.getRentPriceTo())){
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rentPrice"), buildingRequest.getRentPriceTo()));
        }
        if(UtilFunction.checkString(buildingRequest.getManagerName())){
            predicates.add(criteriaBuilder.like(root.get("managerName"), "%" + buildingRequest.getManagerName() + "%"));
        }
        if(UtilFunction.checkString(buildingRequest.getManagerPhonenumber())){
            predicates.add(criteriaBuilder.equal(root.get("managerPhonenumber"), buildingRequest.getManagerPhonenumber()));
        }

        Join<Building,User> userJoin = root.join("users", JoinType.LEFT); // lấy tất cả các bản ghi root kể cả không có bản ghi tương ứng là joinUser 
        if(buildingRequest.getUser() != null && UtilFunction.checkInteger(buildingRequest.getUser().getId())){
            predicates.add(criteriaBuilder.equal(userJoin.get("id"), buildingRequest.getUser().getId()));
        }

        Join<Building,RentType> rentTypeJoin = root.join("rentTypes", JoinType.LEFT);
        if(buildingRequest.getRentTypes() != null && !buildingRequest.getRentTypes().isEmpty()){
            List<Predicate> rentTypePredicate = new ArrayList<>();
            for(RentTypeDTO rentType : buildingRequest.getRentTypes()){
                rentTypePredicate.add(criteriaBuilder.equal(rentTypeJoin.get("id"), rentType.getId()));
            }
            predicates.add(criteriaBuilder.or(rentTypePredicate.toArray(new Predicate[0])));
        }
        if(!predicates.isEmpty())
        {
            Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            query.where(finalPredicate);
        }
        query.select(root).distinct(true);
        List<Building> buildings = entityManager.createQuery(query).getResultList();
        return buildings.stream().map(it -> buildingConverter.toBuildingResponse(it)).collect(Collectors.toList());
    }

    @Override
    public void addNewBuilding(BuildingRequestAdd buildingRequest) {
        Building building = buildingConverter.buildingRequestAddToBuilding(buildingRequest);
        buildingRepository.save(building);
    }

    @Override
    public void updateBuilding(Integer id, BuildingRequestAdd building) {
        Building oldBuilding = buildingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Building is not found"));
        if(oldBuilding != null){
            Building newBuilding = buildingConverter.buildingRequestAddToBuildingExisted(id, building);
            buildingRepository.save(newBuilding);
        }
    }

    @Override
    public void deleteBuilding(Integer id) {
        Building building = buildingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Building is not found"));
        if(building != null){
            buildingRepository.delete(building);
        }
    }
}