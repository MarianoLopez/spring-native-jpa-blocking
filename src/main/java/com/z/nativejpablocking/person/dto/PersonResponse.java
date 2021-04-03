package com.z.nativejpablocking.person.dto;

import com.z.nativejpablocking.job.dto.JobResponse;
import com.z.nativejpablocking.person.domain.Person;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class PersonResponse {
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
    private final Set<JobResponse> jobs;

    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime lastModifiedDate;
    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime createdDate;

    public static PersonResponse from(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getEnabled(),
                person.getCity().getCountry().getISOCode(),
                person.getCity().getId().getName(),
                person.getJobs().stream().map(JobResponse::from).collect(Collectors.toSet()),
                person.getLastModifiedDate(),
                person.getCreatedDate());
    }
}
