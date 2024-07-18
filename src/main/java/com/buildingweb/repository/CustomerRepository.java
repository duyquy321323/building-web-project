package com.buildingweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.Customer;
import com.buildingweb.repository.custom.CustomerRepositoryCustom;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    public List<Customer> findByIdInAndIsActive(List<Long> id, Integer isActive);

    public Customer findByIdAndIsActive(Long id, Integer isActive);
}