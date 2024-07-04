package com.buildingweb.entity;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "districtId", nullable = false)
    @ManyToOne
    private District district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "street")
    private String street;

    @Column(name = "structure")
    private String structure;

    @Column(name = "numberOfBasement")
    private Integer numberOfBasement;

    @Column(name = "floorArea")
    private Integer floorArea;

    @Column(name = "direction")
    private String direction;

    @Column(name = "level")
    private String level;

    @Column(name = "rentPrice")
    private Integer rentPrice;

    @Column(name = "rentPriceDescription")
    private String rentPriceDescription;

    @Column(name = "serviceFee")
    private Integer serviceFee;

    @Column(name = "carFee")
    private Integer carFee;

    @Column(name = "overtimeFee")
    private Integer overtimeFee;

    @Column(name = "electricity")
    private Integer electricity;

    @Column(name = "deposit")
    private Integer deposit;

    @Column(name = "payment")
    private Integer payment;

    @Column(name = "rentTime")
    private Integer rentTime;

    @Column(name = "descorationTime")
    private Integer descorationTime;

    @Column(name = "managermentName")
    private String managerName;

    @Column(name = "brokerageFee")
    private Integer brokerageFee;

    @Column(name = "note")
    private String note;

    @Column(name = "managerPhonenumber")
    private String managerPhonenumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "building", fetch = FetchType.LAZY)
    private List<RentArea> rentAreas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "assignmentBuilding",
        joinColumns = @JoinColumn(name = "buildingId"),
        inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<User> users = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "buildings", fetch = FetchType.LAZY)
    private List<RentType> rentTypes = new ArrayList<>();
}
