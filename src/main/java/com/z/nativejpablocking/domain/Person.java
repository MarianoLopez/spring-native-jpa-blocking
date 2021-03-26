package com.z.nativejpablocking.domain;

import com.z.nativejpablocking.dto.CreatePersonRequest;
import com.z.nativejpablocking.dto.UpdatePersonRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@DynamicUpdate
public class Person {
    private final static String PERSON_SEQUENCE_NAME = "PERSON_SEQUENCE";

    @Id
    @SequenceGenerator(name = PERSON_SEQUENCE_NAME, sequenceName = PERSON_SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PERSON_SEQUENCE_NAME)
    private long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    public static Person toPerson(CreatePersonRequest createPersonRequest) {
        return Person.builder()
                .firstName(createPersonRequest.getFirstName())
                .lastName(createPersonRequest.getLastName())
                .build();
    }
}
