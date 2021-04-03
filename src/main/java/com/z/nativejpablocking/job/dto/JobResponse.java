package com.z.nativejpablocking.job.dto;

import com.z.nativejpablocking.job.domain.Job;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class JobResponse {
    private final String name;

    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime lastModifiedDate;
    @ApiModelProperty(example = "2021-03-26T22:31:41.062922")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime createdDate;

    public static JobResponse from(Job job) {
        return new JobResponse(
                job.getName(),
                job.getLastModifiedDate(),
                job.getCreatedDate()
        );
    }
}
