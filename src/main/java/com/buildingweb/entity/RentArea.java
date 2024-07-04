package com.buildingweb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name = "rent_area")
public class RentArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "area")
    private Integer area;

    @ManyToOne // EAGER
    @JoinColumn(name = "buildingId")
    private Building building;

    @Override
    public String toString() {
        return this.getArea().toString();
    }
}