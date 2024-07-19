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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.buildingweb.entity.Customer;
import com.buildingweb.entity.User;
import com.buildingweb.repository.custom.CustomerRepositoryCustom;
import com.buildingweb.request.CustomerSearchRequest;
import com.buildingweb.utils.UtilFunction;

import lombok.SneakyThrows;

public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SneakyThrows
    public Page<Customer> findByCustomerSearchRequestAndUser(CustomerSearchRequest request, User user,
            Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> customerQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> rootCustomer = customerQuery.from(Customer.class);
        List<Predicate> predicates = new ArrayList<>();
        if (UtilFunction.checkString(request.getName())) {
            predicates.add(criteriaBuilder.like(rootCustomer.get("fullname"), "%" + request.getName() + "%"));
        }
        if (UtilFunction.checkString(request.getEmail())) {
            predicates.add(criteriaBuilder.equal(rootCustomer.get("email"), request.getEmail()));
        }
        if (UtilFunction.checkString(request.getPhone())) {
            predicates.add(criteriaBuilder.equal(rootCustomer.get("phone"), request.getPhone()));
        }

        Join<Customer, User> joinUser = rootCustomer.join("users", JoinType.LEFT);
        if (user != null) {
            predicates.add(criteriaBuilder.equal(joinUser.get("id"), user.getId()));
        }

        if (UtilFunction.checkLong(request.getStaffId())) {
            predicates.add(criteriaBuilder.equal(joinUser.get("id"), request.getStaffId()));
        }

        predicates.add(criteriaBuilder.equal(rootCustomer.get("isActive"), 1));
        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        customerQuery.select(rootCustomer).distinct(true).where(finalPredicate);
        List<Customer> customers = new ArrayList<>();
        try {
            customers = entityManager.createQuery(customerQuery).getResultList();
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage());
        }
        return new PageImpl<>(customers, pageable, customers.size());
    }

    @Override
    @SneakyThrows
    public List<Customer> findAllByUserAndIsActive(User user, Integer isActive) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> queryCustomer = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> rootCustomer = queryCustomer.from(Customer.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<Customer, User> joinUser = rootCustomer.join("users", JoinType.LEFT);
        if (user != null) {
            predicates.add(criteriaBuilder.equal(joinUser.get("id"), user.getId()));
        }
        if (isActive != null) {
            predicates.add(criteriaBuilder.equal(rootCustomer.get("isActive"), isActive));
        }
        predicates.add(criteriaBuilder.equal(joinUser.get("status"), 1));
        queryCustomer.select(rootCustomer).distinct(true)
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        try {
            return entityManager.createQuery(queryCustomer).getResultList();
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public Customer findByIdAndIsActiveAndStaff(Long id, Integer isActive, User user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> customerQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> rootCustomer = customerQuery.from(Customer.class);
        List<Predicate> predicates = new ArrayList<>();

        if (UtilFunction.checkLong(id)) {
            predicates.add(criteriaBuilder.equal(rootCustomer.get("id"), id));
        }
        if (isActive != null) {
            predicates.add(criteriaBuilder.equal(rootCustomer.get("isActive"), isActive));
        }

        Join<Customer, User> joinUser = rootCustomer.join("users", JoinType.LEFT);
        if (user != null) {
            predicates.add(criteriaBuilder.equal(joinUser.get("id"), user.getId()));
        }
        customerQuery.select(rootCustomer).distinct(true).where(predicates.toArray(new Predicate[0]));
        try {
            return entityManager.createQuery(customerQuery).getSingleResult();
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage());
        }
    }
}