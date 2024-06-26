package com.buildingweb.model;

import com.buildingweb.entity.RentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentTypeDTO {
    private Integer id;
    private String code;
    private String name;

    public static RentTypeDTO fromDomain(RentType rentType)
    {
        return RentTypeDTO.builder().id(rentType.getId()).code(rentType.getCode()).name(rentType.getName()).build();
    }
}