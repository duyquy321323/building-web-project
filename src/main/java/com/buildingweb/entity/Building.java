package com.buildingweb.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.buildingweb.enums.District;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "building")
public class Building extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "street")
    private String street;

    @Column(name = "ward")
    private String ward;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "district")
    private District district;

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

    @Column(name = "rentpricedescription", columnDefinition = "TEXT")
    private String rentPriceDescription;

    @Column(name = "servicefee")
    private String serviceFee;

    @Column(name = "carfee")
    private String carFee;

    @Column(name = "motofee")
    private String motoFee;

    @Column(name = "overtimefee")
    private String overtimeFee;

    @Column(name = "waterfee")
    private String waterFee;

    @Column(name = "electricityfee")
    private String electricity;

    @Column(name = "deposit")
    private String deposit;

    @Column(name = "payment")
    private String payment;

    @Column(name = "renttime")
    private String rentTime;

    @Column(name = "decorationtime")
    private String descorationTime;

    @Column(name = "brokeragefee", precision = 13, scale = 2)
    private BigDecimal brokerageFee;

    @Column(name = "type")
    private String rentTypes;

    @Column(name = "note")
    private String note;

    @Column(name = "managername")
    private String managerName;

    @Column(name = "managerphone")
    private String managerPhonenumber;

    @Lob
    @Column(name = "linkofbuilding")
    private byte[] linkOfBuilding;

    @OneToMany(cascade = { CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REMOVE }, mappedBy = "building", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RentArea> rentAreas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "assignmentbuilding", joinColumns = @JoinColumn(name = "buildingid"), inverseJoinColumns = @JoinColumn(name = "staffid"))
    private List<User> users = new ArrayList<>();
}
