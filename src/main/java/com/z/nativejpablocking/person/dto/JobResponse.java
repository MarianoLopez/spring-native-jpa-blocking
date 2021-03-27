package com.z.nativejpablocking.person.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.z.nativejpablocking.person.domain.Job;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

import static com.z.nativejpablocking.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

@RequiredArgsConstructor
@Getter
public class JobResponse {
    private final String name;

    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime lastModifiedDate;
    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime createdDate;

    public static JobResponse from(Job job) {
        return new JobResponse(
                job.getName(),
                job.getLastModifiedDate(),
                job.getCreatedDate()
        );
    }
}
