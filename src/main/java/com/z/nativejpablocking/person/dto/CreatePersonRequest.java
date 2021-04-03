package com.z.nativejpablocking.person.dto;

import com.z.nativejpablocking.job.dto.JobRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@RequiredArgsConstructor
@Data
@Builder
public class CreatePersonRequest {
    @ApiModelProperty(example = "John")
    @NotEmpty
    private final String firstName;

    @ApiModelProperty(example = "Doe")
    @NotEmpty
    private final String lastName;

    @ApiModelProperty(example = "AR")
    @NotEmpty
    private final String countryISOCode;

    @ApiModelProperty(example = "Corrientes")
    @NotEmpty
    private final String cityName;

    private final Set<JobRequest> jobs;
}
