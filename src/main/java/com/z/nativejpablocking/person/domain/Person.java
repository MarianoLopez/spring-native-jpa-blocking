package com.z.nativejpablocking.person.domain;

import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.utils.jpa.JPAAuditor;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@DynamicUpdate
public class Person extends JPAAuditor {
    private final static String PERSON_SEQUENCE_NAME = "PERSON_SEQUENCE";

    @Id
    @SequenceGenerator(name = PERSON_SEQUENCE_NAME, sequenceName = PERSON_SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PERSON_SEQUENCE_NAME)
    @EqualsAndHashCode.Include
    private long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull
    private Boolean enabled;

    public static Person toPerson(CreatePersonRequest createPersonRequest) {
        return Person.builder()
                .firstName(createPersonRequest.getFirstName())
                .lastName(createPersonRequest.getLastName())
                .enabled(true)
                .build();
    }
}
