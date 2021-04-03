package com.z.nativejpablocking.person.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class City {

    @EmbeddedId
    private CityId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_iso_code", insertable = false, updatable = false)
    private Country country;
}
