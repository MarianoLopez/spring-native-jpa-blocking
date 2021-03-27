package com.z.nativejpablocking.person.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.z.nativejpablocking.person.domain.Person;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class PersonResponse {
    private static final String defaultDateTimeFormat = "dd-MM-yyyy HH:mm:ss";

    @ApiModelProperty(example = "999")
    private final Long id;
    @ApiModelProperty(example = "John")
    private final String firstName;
    @ApiModelProperty(example = "Doe")
    private final String lastName;
    @ApiModelProperty(example = "true")
    private final Boolean enabled;
    @ApiModelProperty(example = "AR")
    private final String countryISOCode;
    @ApiModelProperty(example = "Corrientes")
    private final String cityName;

    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = defaultDateTimeFormat)
    private final LocalDateTime lastModifiedDate;
    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = defaultDateTimeFormat)
    private final LocalDateTime createdDate;

    public static PersonResponse from(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getEnabled(),
                person.getCity().getId().getCountry_iso_code(),
                person.getCity().getId().getName(),
                person.getLastModifiedDate(),
                person.getCreatedDate());
    }
}
