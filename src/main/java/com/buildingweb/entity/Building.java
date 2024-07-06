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
    private Long id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "districtid", nullable = false)
    @ManyToOne
    private District district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "street")
    private String street;

    @Column(name = "structure")
    private String structure;

    @Column(name = "numberofbasement")
    private Long numberOfBasement;

    @Column(name = "floorarea")
    private Long floorArea;

    @Column(name = "direction")
    private String direction;

    @Column(name = "level")
    private String level;

    @Column(name = "rentprice")
    private Long rentPrice;

    @Column(name = "rentpricedescription")
    private String rentPriceDescription;

    @Column(name = "servicefee")
    private Long serviceFee;

    @Column(name = "carfee")
    private Long carFee;

    @Column(name = "overtimefee")
    private Long overtimeFee;

    @Column(name = "electricityfee")
    private Long electricity;

    @Column(name = "deposit")
    private Long deposit;

    @Column(name = "payment")
    private Long payment;

    @Column(name = "renttime")
    private Long rentTime;

    @Column(name = "decorationtime")
    private Long descorationTime;

    @Column(name = "managername")
    private String managerName;

    @Column(name = "brokeragefee")
    private Long brokerageFee;

    @Column(name = "note")
    private String note;

    @Column(name = "managerphonenumber")
    private String managerPhonenumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "building", fetch = FetchType.LAZY)
    private List<RentArea> rentAreas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "assignmentbuilding",
        joinColumns = @JoinColumn(name = "buildingid"),
        inverseJoinColumns = @JoinColumn(name = "userid")
    )
    private List<User> users = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "buildings", fetch = FetchType.LAZY)
    private List<RentType> rentTypes = new ArrayList<>();
}
