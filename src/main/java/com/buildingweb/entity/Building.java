package com.buildingweb.entity;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "district_id")
    @ManyToOne
    private District district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "street")
    private String street;

    @Column(name = "structure")
    private String structure;

    @Column(name = "number_of_basement")
    private Integer numberOfBasement;

    @Column(name = "floor_area")
    private Integer floorArea;

    @Column(name = "direction")
    private String direction;

    @Column(name = "level")
    private String level;

    @Column(name = "rent_price")
    private Integer rentPrice;

    @Column(name = "rent_price_description")
    private String renPriceDescription;

    @Column(name = "service_fee")
    private Integer serviceFee;

    @Column(name = "car_fee")
    private Integer carFee;

    @Column(name = "overtime_fee")
    private Integer overtimeFee;

    @Column(name = "electricity")
    private Integer electricity;

    @Column(name = "deposit")
    private Integer deposit;

    @Column(name = "payment")
    private Integer payment;

    @Column(name = "rent_time")
    private Integer rentTime;

    @Column(name = "descoration_time")
    private Integer descorationTime;

    @Column(name = "managerment_name")
    private String managermentName;

    @Column(name = "brokerage_fee")
    private Integer brokerageFee;

    @Column(name = "note")
    private String note;

    @Column(name = "manager_phonenumber")
    private String managerPhonenumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "building")
    private List<RentArea> rentAreas;

    @ManyToMany
    @JoinTable(
        name = "assignment_building",
        joinColumns = @JoinColumn(name = "building_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "buildings")
    private List<RentType> rentTypes;
}
