package com.buildingweb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name = "rentarea")
public class RentArea extends BaseEntity {

    @Column(name = "value")
    private Long value;

    @ManyToOne // EAGER
    @JoinColumn(name = "buildingid")
    private Building building;

    @Override
    public String toString() {
        return this.getValue().toString();
    }
}