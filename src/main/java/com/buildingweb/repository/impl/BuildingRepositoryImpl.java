package com.buildingweb.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.buildingweb.entity.Building;
import com.buildingweb.entity.RentArea;
import com.buildingweb.entity.RentType;
import com.buildingweb.entity.User;
import com.buildingweb.model.RentTypeDTO;
import com.buildingweb.repository.BuildingRepository;
import com.buildingweb.request.BuildingRequest;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BuildingRepositoryImpl implements BuildingRepository {
    private final EntityManager entityManager;

    @Override
    public List<Building> findBuildingByBuildingRequest(BuildingRequest buildingRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); // lấy đối tượng CriteriaBuilder từ EntityManager
        CriteriaQuery<Building> query = criteriaBuilder.createQuery(Building.class); // khai báo kiểu dữ liệu mà bạn muốn truy vấn
        Root<Building> root = query.from(Building.class); // chỉ định đối tượng cơ sở truy vấn
        List<Predicate> predicates = new ArrayList<>(); // danh sách các truy vấn 
        if(buildingRequest.getName() != null){
            predicates.add(criteriaBuilder.like(root.get("name"),"%" + buildingRequest.getName() + "%"));
        }
        if(buildingRequest.getFloorArea() != null){
            predicates.add(criteriaBuilder.equal(root.get("floorArea"), buildingRequest.getFloorArea()));
        }
        if(buildingRequest.getDistrict() != null && buildingRequest.getDistrict().getId() != null){
            predicates.add(criteriaBuilder.equal(root.get("district").get("id"), buildingRequest.getDistrict().getId()));
        }
        if(buildingRequest.getWard() != null){
            predicates.add(criteriaBuilder.like(root.get("ward"), "%" + buildingRequest.getWard() + "%"));
        }
        if(buildingRequest.getStreet() != null){
            predicates.add(criteriaBuilder.like(root.get("street"), "%" + buildingRequest.getStreet() + "%"));
        }
        if(buildingRequest.getNumberOfBasement() != null){
            predicates.add(criteriaBuilder.equal(root.get("numberOfBasement"), buildingRequest.getNumberOfBasement()));
        }
        if(buildingRequest.getDirection() != null){
            predicates.add(criteriaBuilder.like(root.get("direction"), "%" + buildingRequest + "%"));
        }
        if(buildingRequest.getLevel() != null){
            predicates.add(criteriaBuilder.like(root.get("level"), buildingRequest.getLevel()));
        }

        // join diện tích thuê và building
        Join<Building,RentArea> joinBuildRentArea = root.join("rentAreas", JoinType.LEFT);
        if(buildingRequest.getAreaFrom() != null){
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(joinBuildRentArea.get("area"), buildingRequest.getAreaFrom()));
        }
        if(buildingRequest.getAreaTo() != null){
            predicates.add(criteriaBuilder.lessThanOrEqualTo(joinBuildRentArea.get("area"), buildingRequest.getAreaTo()));
        }
        if(buildingRequest.getRentPriceFrom() != null){
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rentPrice"), buildingRequest.getRentPriceFrom()));
        }
        if(buildingRequest.getRentPriceTo() != null){
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rentPrice"), buildingRequest.getRentPriceTo()));
        }
        if(buildingRequest.getManagerName() != null){
            predicates.add(criteriaBuilder.like(root.get("managerName"), "%" + buildingRequest.getManagerName() + "%"));
        }
        if(buildingRequest.getManagerPhonenumber() != null){
            predicates.add(criteriaBuilder.equal(root.get("managerPhonenumber"), buildingRequest.getManagerPhonenumber()));
        }

        Join<Building,User> userJoin = root.join("users", JoinType.LEFT); // lấy tất cả các bản ghi root kể cả không có bản ghi tương ứng là joinUser 
        if(buildingRequest.getUser() != null){
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
        return entityManager.createQuery(query).getResultList();
    }
}