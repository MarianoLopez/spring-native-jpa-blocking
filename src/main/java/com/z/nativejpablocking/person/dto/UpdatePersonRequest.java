package com.z.nativejpablocking.person.dto;

import com.z.nativejpablocking.person.dto.validation.PersonIdGroup;
import com.z.nativejpablocking.person.dto.validation.UpdatePersonGroup;
import com.z.nativejpablocking.utils.validator.NotEmptyIfNotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Data
public class UpdatePersonRequest {

    @ApiModelProperty(example = "999")
    @NotNull(groups = PersonIdGroup.class)
    @Min(value = 1, groups = PersonIdGroup.class)
    private final Long id;

    @ApiModelProperty(example = "John")
    @NotEmptyIfNotNull(message = "firstName shouldn't be empty if is present", groups = UpdatePersonGroup.class)
    private final String firstName;

    @ApiModelProperty(example = "Doe")
    @NotEmptyIfNotNull(message = "lastName shouldn't be empty if is present", groups = UpdatePersonGroup.class)
    private final String lastName;
}
