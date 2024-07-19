package com.buildingweb.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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

    @Column(name = "status")
    private String status;

    @Column(name = "is_active", nullable = false)
    private Integer isActive;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "assignmentcustomer", joinColumns = @JoinColumn(name = "customerid"), inverseJoinColumns = @JoinColumn(name = "staffid"))
    private List<User> users = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Transaction> transactions = new ArrayList<>();
}