package com.z.nativejpablocking.person.dao;

import com.z.nativejpablocking.person.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonDAO extends PagingAndSortingRepository<Person, Long> {
    @EntityGraph(Person.PERSON_FULL_GRAPH)
    Page<Person> findByLastNameContaining(String lastName, Pageable pageable);

    @EntityGraph(Person.PERSON_FULL_GRAPH)
    Optional<Person> findById(Long id);

    @EntityGraph(Person.PERSON_FULL_GRAPH)
    Page<Person> findAll(Pageable pageable);
}
