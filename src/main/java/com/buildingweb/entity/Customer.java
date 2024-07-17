package com.buildingweb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class Customer extends BaseEntity {
    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "demand", nullable = false)
    private String demand;

    @Column(name = "status", nullable = false)
    private Integer status;
}