package com.z.nativejpablocking.person.domain;

import com.z.nativejpablocking.person.dto.JobRequest;
import com.z.nativejpablocking.utils.jpa.JPAAuditor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job extends JPAAuditor {

    @Id
    private String name;

    public static Job toJob(JobRequest jobRequest) {
        return new Job(jobRequest.getName());
    }
}
