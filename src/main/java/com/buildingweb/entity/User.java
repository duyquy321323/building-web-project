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
@Table(name = "user")
public class User extends BaseEntity {

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private Integer status;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Building> buildings = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Transaction> transactions = new ArrayList<>();

    public Boolean isStaff() {
        for (Role role : roles) {
            if (role.getCode().toString().equals("STAFF")) {
                return true;
            }
        }
        return false;
    }

    public Boolean isManager() {
        for (Role role : roles) {
            if (role.getCode().toString().equals("MANAGER")) {
                return true;
            }
        }
        return false;
    }

}