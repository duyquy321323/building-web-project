package com.buildingweb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass // map các properties này xuống bảng của các lớp kế thừa mà không tự tạo bảng
@EntityListeners(AuditingEntityListener.class) // lắng nghe các sự kiện như tạo, cập nhật các bản ghi
@Getter
@Setter
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createddate")
    @CreatedDate // sau khi bắt được sự kiện ngày tạo bản ghi thì gắn vào đây
    private Date createdDate;

    @Column(name = "createdby")
    @CreatedBy // sự kiện tạo bởi ai
    private String createdBy;

    @Column(name = "modifieddate")
    @LastModifiedDate // lần cuối cùng sửa là vào ngày nào
    private Date modifiedDate;

    @Column(name = "modifiedby")
    @LastModifiedBy // sửa bởi ai
    private String modifiedBy;
}