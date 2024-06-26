package com.buildingweb.model;

import com.buildingweb.entity.District;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDTO {
    private Integer id;
    private String code;
    private String name;

    public static DistrictDTO fromDomain(District district)
    {
        return DistrictDTO.builder().id(district.getId()).code(district.getCode()).name(district.getName()).build();
    }
}