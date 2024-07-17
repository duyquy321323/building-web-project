package com.buildingweb.repository.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.buildingweb.entity.Building;
import com.buildingweb.entity.RentArea;
import com.buildingweb.entity.User;
import com.buildingweb.enums.RentType;
import com.buildingweb.repository.custom.BuildingRepositoryCustom;
import com.buildingweb.request.BuildingRequestSearch;
import com.buildingweb.utils.UtilFunction;

public class BuildingRepositoryImpl implements BuildingRepositoryCustom {

    @PersistenceContext // inject đối tượng EntityManager vào do spring boot quản lý vòng đời của nó
    private EntityManager entityManager;

    @Override
    public Page<Building> findByBuildingRequestSearch(BuildingRequestSearch buildingRequest, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); // lấy đối tượng CriteriaBuilder từ
                                                                              // EntityManager
        CriteriaQuery<Building> query = criteriaBuilder.createQuery(Building.class); // khai báo kiểu dữ liệu mà bạn
                                                                                     // muốn truy vấn
        Root<Building> root = query.from(Building.class); // chỉ định đối tượng cơ sở truy vấn
        List<Predicate> predicates = new ArrayList<>(); // danh sách các truy vấn
        if (UtilFunction.checkString(buildingRequest.getName())) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + buildingRequest.getName() + "%"));
        }
        if (UtilFunction.checkLong(buildingRequest.getFloorArea())) {
            predicates.add(criteriaBuilder.equal(root.get("floorArea"), buildingRequest.getFloorArea()));
        }
        if (buildingRequest.getDistrict() != null
                && UtilFunction.checkString(buildingRequest.getDistrict().toString())) {
            predicates.add(criteriaBuilder.equal(root.get("district"), buildingRequest.getDistrict().toString()));
        }
        if (UtilFunction.checkString(buildingRequest.getWard())) {
            predicates.add(criteriaBuilder.like(root.get("ward"), "%" + buildingRequest.getWard() + "%"));
        }
        if (UtilFunction.checkString(buildingRequest.getStreet())) {
            predicates.add(criteriaBuilder.like(root.get("street"), "%" + buildingRequest.getStreet() + "%"));
        }
        if (UtilFunction.checkLong(buildingRequest.getNumberOfBasement())) {
            predicates.add(criteriaBuilder.equal(root.get("numberOfBasement"), buildingRequest.getNumberOfBasement()));
        }
        if (UtilFunction.checkString(buildingRequest.getDirection())) {
            predicates.add(criteriaBuilder.like(root.get("direction"), "%" + buildingRequest + "%"));
        }
        if (UtilFunction.checkString(buildingRequest.getLevel())) {
            predicates.add(criteriaBuilder.like(root.get("level"), buildingRequest.getLevel()));
        }

        // join diện tích thuê và building
        Join<Building, RentArea> joinBuildRentArea = root.join("rentAreas", JoinType.LEFT);
        if (UtilFunction.checkLong(buildingRequest.getAreaFrom())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(joinBuildRentArea.get("area"), buildingRequest.getAreaFrom()));
        }
        if (UtilFunction.checkLong(buildingRequest.getAreaTo())) {
            predicates
                    .add(criteriaBuilder.lessThanOrEqualTo(joinBuildRentArea.get("area"), buildingRequest.getAreaTo()));
        }
        if (UtilFunction.checkLong(buildingRequest.getRentPriceFrom())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("rentPrice"), buildingRequest.getRentPriceFrom()));
        }
        if (UtilFunction.checkLong(buildingRequest.getRentPriceTo())) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rentPrice"), buildingRequest.getRentPriceTo()));
        }
        if (UtilFunction.checkString(buildingRequest.getManagerName())) {
            predicates.add(criteriaBuilder.like(root.get("managerName"), "%" + buildingRequest.getManagerName() + "%"));
        }
        if (UtilFunction.checkString(buildingRequest.getManagerPhoneNumber())) {
            predicates.add(
                    criteriaBuilder.equal(root.get("managerPhoneNumber"), buildingRequest.getManagerPhoneNumber()));
        }

        Join<Building, User> userJoin = root.join("users", JoinType.LEFT); // lấy tất cả các bản ghi root kể cả không có
                                                                           // bản ghi tương ứng là joinUser
        if (UtilFunction.checkLong(buildingRequest.getUserId())) {
            predicates.add(criteriaBuilder.equal(userJoin.get("id"), buildingRequest.getUserId()));
        }

        if (buildingRequest.getRentTypes() != null && !buildingRequest.getRentTypes().isEmpty()) {
            List<Predicate> rentTypePredicate = new ArrayList<>();
            for (RentType rentType : buildingRequest.getRentTypes()) {
                rentTypePredicate.add(criteriaBuilder.like(root.get("type"), "%" + rentType.toString() + "% "));
            }
            predicates.add(criteriaBuilder.or(rentTypePredicate.toArray(new Predicate[0])));
        }
        if (!predicates.isEmpty()) {
            Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            query.where(finalPredicate);
        }
        query.select(root).distinct(true);
        List<Building> buildings = entityManager.createQuery(query).getResultList();
        return new PageImpl<>(buildings, pageable, buildings.size());
    }
}