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

import com.buildingweb.entity.Role;
import com.buildingweb.entity.User;
import com.buildingweb.repository.custom.UserRepositoryCustom;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> findAllByRoleAndStatus(String role, Integer status, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); // lấy builder partern criteria
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class); // tạo criteria query cho đối tượng user
        Root<User> userRoot = query.from(User.class); // lập chủ sở hữu là User
        List<Predicate> predicates = new ArrayList<>(); // danh sách các điều kiện

        Join<User, Role> roleJoin = userRoot.join("roles", JoinType.LEFT); // join 2 bảng
        predicates.add(criteriaBuilder.equal(roleJoin.get("code"), role));
        predicates.add(criteriaBuilder.equal(userRoot.get("status"), status));
        query.select(userRoot).distinct(true).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        List<User> users = entityManager.createQuery(query).getResultList();
        return new PageImpl<>(users, pageable, users.size());
    }
}