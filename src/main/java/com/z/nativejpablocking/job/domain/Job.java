package com.z.nativejpablocking.job.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.z.nativejpablocking.person.domain.Person;
import com.z.nativejpablocking.job.dto.JobRequest;
import com.z.nativejpablocking.utils.jpa.JPAAuditor;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job extends JPAAuditor {

    @Id
    @EqualsAndHashCode.Include
    private String name;

    public static Job toJob(JobRequest jobRequest) {
        return new Job(jobRequest.getName(), Collections.emptySet());
    }

    @JsonBackReference // to avoid circular references
    @ManyToMany(mappedBy = "jobs")
    private Set<Person> persons;
}
