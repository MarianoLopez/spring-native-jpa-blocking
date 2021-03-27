package com.z.nativejpablocking.person.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetPersonRequest {
    @ApiParam(value = "Optional lastName filter")
    private String lastName;
}
