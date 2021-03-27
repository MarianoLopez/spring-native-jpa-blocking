package com.z.nativejpablocking.person.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Country {
    @Id
    @Column(name = "country_iso_code", nullable = false, updatable = false)
    private String ISOCode;

    @NotNull
    @Column(unique = true, nullable = false)
    private String name;
}
