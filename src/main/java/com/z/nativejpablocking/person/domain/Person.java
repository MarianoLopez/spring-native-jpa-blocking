package com.z.nativejpablocking.person.domain;

import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.utils.jpa.JPAAuditor;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@DynamicUpdate
@NamedEntityGraph(name = Person.PERSON_FULL_GRAPH, attributeNodes = {
        @NamedAttributeNode(value = "city", subgraph = "city"),
        @NamedAttributeNode(value = "jobs")
}, subgraphs = {
        @NamedSubgraph(name = "city", attributeNodes = {
                @NamedAttributeNode("country")
        })
})
public class Person extends JPAAuditor {
    public final static String PERSON_FULL_GRAPH = "PERSON_FULL_GRAPH";
    private final static String PERSON_SEQUENCE_NAME = "PERSON_SEQUENCE";

    @Id
    @SequenceGenerator(name = PERSON_SEQUENCE_NAME, sequenceName = PERSON_SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PERSON_SEQUENCE_NAME)
    @EqualsAndHashCode.Include
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="country_iso_code", referencedColumnName="country_iso_code"),
            @JoinColumn(name="city_name", referencedColumnName="name")
    })
    private City city;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}) //oder does matter
    @JoinTable(
            name = "person_job",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "job_name"))
    private Set<Job> jobs;

    public static Person toPerson(CreatePersonRequest createPersonRequest) {
        var city = new City();
        city.setId(new CityId(createPersonRequest.getCityName(), createPersonRequest.getCountryISOCode()));

        return Person.builder()
                .firstName(createPersonRequest.getFirstName())
                .lastName(createPersonRequest.getLastName())
                .enabled(true)
                .city(city)
                .jobs(createPersonRequest.getJobs().stream().map(Job::toJob).collect(Collectors.toSet()))
                .build();
    }
}
