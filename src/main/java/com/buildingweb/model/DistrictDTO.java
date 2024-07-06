package com.buildingweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDTO {
    private Long id;
    private String code;
    private String name;
}