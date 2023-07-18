package com.emre.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass //Bu super class'ı extend olduğu yere özelliklerinin map'lenmesini, tanımlanmasını sağlıyor.
public class Base {
    private Long createdDate;
    private Long updatedDate;
}
