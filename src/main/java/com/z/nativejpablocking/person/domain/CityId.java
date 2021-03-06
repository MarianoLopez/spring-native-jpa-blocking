package com.z.nativejpablocking.person.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class CityId implements Serializable {
    private String name;
    @Column(name = "country_iso_code", table = "city")
    private String countryIsoCode;
}
