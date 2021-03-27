package com.z.nativejpablocking.person.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobRequest {
    @ApiModelProperty(example = "Java Developer")
    @NotEmpty
    private String name;
}
