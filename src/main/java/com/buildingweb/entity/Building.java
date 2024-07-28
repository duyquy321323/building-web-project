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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "ward", nullable = false)
    private String ward;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "district", nullable = false)
    private District district;

    @Column(name = "structure")
    private String structure;

    @Column(name = "numberofbasement")
    private Long numberOfBasement;

    @Column(name = "floorarea", nullable = false)
    private Long floorArea;

    @Column(name = "direction")
    private String direction;

    @Column(name = "level")
    private String level;

    @Column(name = "rentprice", nullable = false)
    private Long rentPrice;

    @Column(name = "rentpricedescription", columnDefinition = "TEXT")
    private String rentPriceDescription;

    @Column(name = "servicefee")
    private Long serviceFee;

    @Column(name = "carfee")
    private Long carFee;

    @Column(name = "motofee")
    private Long motorFee;

    @Column(name = "overtimefee")
    private Long overtimeFee;

    @Column(name = "waterfee")
    private Long waterFee;

    @Column(name = "electricityfee")
    private Long electricity;

    @Column(name = "deposit")
    private Long deposit;

    @Column(name = "payment")
    private Long payment;

    @Column(name = "renttime")
    private Long rentTime;

    @Column(name = "decorationtime")
    private Long decorationTime;

    @Column(name = "brokeragefee", precision = 13, scale = 2)
    private BigDecimal brokerageFee;

    @Column(name = "type", nullable = false)
    private String rentTypes;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;

    @Column(name = "managername")
    private String managerName;

    @Column(name = "managerphone")
    private String managerPhoneNumber;

    @Lob
    @Column(name = "linkofbuilding", columnDefinition = "LONGBLOB")
    private byte[] linkOfBuilding;

    @OneToMany(cascade = { CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REMOVE }, mappedBy = "building", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RentArea> rentAreas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "assignmentbuilding", joinColumns = @JoinColumn(name = "buildingid", nullable = false), inverseJoinColumns = @JoinColumn(name = "staffid", nullable = false))
    private List<User> users = new ArrayList<>();
}
