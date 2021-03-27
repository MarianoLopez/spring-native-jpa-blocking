package com.z.nativejpablocking.person.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.z.nativejpablocking.person.domain.Person;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.z.nativejpablocking.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime lastModifiedDate;
    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime createdDate;

    public static PersonResponse from(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getEnabled(),
                person.getCity().getId().getCountry_iso_code(),
                person.getCity().getId().getName(),
                person.getJobs().stream().map(JobResponse::from).collect(Collectors.toSet()),
                person.getLastModifiedDate(),
                person.getCreatedDate());
    }
}
