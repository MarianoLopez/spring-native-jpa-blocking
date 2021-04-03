package com.z.nativejpablocking.job.dto;

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
