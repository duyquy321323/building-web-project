package com.buildingweb.repository.custom.impl;

import java.sql.SQLException;
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
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.buildingweb.entity.Building;
import com.buildingweb.entity.Customer;
import com.buildingweb.entity.Role;
import com.buildingweb.entity.User;
import com.buildingweb.enums.RoleConst;
import com.buildingweb.repository.custom.UserRepositoryCustom;
import com.buildingweb.utils.UtilFunction;

import lombok.SneakyThrows;

@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SneakyThrows
    public Page<User> findAllByRoleAndStatusAndIdBuildingOrIdCustomer(RoleConst role, Integer status, Long idBuilding,
            Long idCustomer,
            Pageable pageable) throws SQLException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); // lấy builder partern criteria
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class); // tạo criteria query cho đối tượng user
        Root<User> userRoot = query.from(User.class); // lập chủ sở hữu là User
        List<Predicate> predicates = new ArrayList<>(); // danh sách các điều kiện

        Join<User, Role> roleJoin = userRoot.join("roles", JoinType.LEFT); // join 2 bảng
        predicates.add(criteriaBuilder.equal(roleJoin.get("code"), role));
        predicates.add(criteriaBuilder.equal(userRoot.get("status"), status));

        if (UtilFunction.checkLong(idBuilding)) {
            Join<User, Building> buildingJoin = userRoot.join("buildings", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(buildingJoin.get("id"), idBuilding));
        }
        if (UtilFunction.checkLong(idCustomer)) {
            Join<User, Customer> customerJoin = userRoot.join("customers", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(customerJoin.get("id"), idCustomer));
        }
        query.select(userRoot).distinct(true).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        List<User> users = new ArrayList<>();
        try {
            users = entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage());
        }
        return new PageImpl<>(users, pageable, users.size());
    }
}