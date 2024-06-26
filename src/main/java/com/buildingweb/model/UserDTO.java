package com.buildingweb.model;

import com.buildingweb.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String phoneNumber;
    private String fullname;

    public static UserDTO fromDomain(User user)
    {
        return UserDTO.builder().id(user.getId()).fullname(user.getFullname()).phoneNumber(user.getPhoneNumber()).build();
    }
}