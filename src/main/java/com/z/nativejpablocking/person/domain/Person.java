package com.z.nativejpablocking.person.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.z.nativejpablocking.job.domain.Job;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name="country_iso_code", referencedColumnName="country_iso_code"),
            @JoinColumn(name="city_name", referencedColumnName="name")
    })
    private City city;

    @JsonManagedReference // to avoid circular references
    @ManyToMany
    @JoinTable(
            name = "person_job",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "job_name"))
    private Set<Job> jobs;


    public static Person toPerson(CreatePersonRequest createPersonRequest) {
        var country = new Country();
        country.setISOCode(createPersonRequest.getCountryISOCode());
        var city = new City();
        city.setId(new CityId(createPersonRequest.getCityName(), country.getISOCode()));
        city.setCountry(country);

        return new Person(
                null, createPersonRequest.getFirstName(), createPersonRequest.getLastName(),
                true, city, createPersonRequest.getJobs().stream().map(Job::toJob).collect(Collectors.toSet()));
    }
}
