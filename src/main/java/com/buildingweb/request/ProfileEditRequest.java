package com.buildingweb.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileEditRequest {
    private String fullname;
    private String email;
    private MultipartFile avatar;
}