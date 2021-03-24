package com.z.nativejpablocking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@RequiredArgsConstructor
@Data
public class CreatePersonRequest {
    @ApiModelProperty(example = "John")
    @NotEmpty
    private final String firstName;

    @ApiModelProperty(example = "Doe")
    @NotEmpty
    private final String lastName;
}
