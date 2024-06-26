package com.buildingweb.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name = "rent_type")
public class RentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "buildingrenttype",
        joinColumns = @JoinColumn(name = "rentTypeId"),
        inverseJoinColumns = @JoinColumn(name = "buildingId")
    )
    private List<Building> buildings;
}