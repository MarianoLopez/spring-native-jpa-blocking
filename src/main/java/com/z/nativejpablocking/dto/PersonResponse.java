package com.z.nativejpablocking.dto;

import com.z.nativejpablocking.domain.Person;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PersonResponse {
    @ApiModelProperty(example = "999")
    private final Long id;
    @ApiModelProperty(example = "John")
    private final String firstName;
    @ApiModelProperty(example = "Doe")
    private final String lastName;

    public static PersonResponse from(Person person) {
        return new PersonResponse(person.getId(), person.getFirstName(), person.getLastName());
    }
}
