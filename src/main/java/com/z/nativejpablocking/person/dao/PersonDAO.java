package com.z.nativejpablocking.person.dao;

import com.z.nativejpablocking.person.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonDAO extends JpaRepository<Person, Long> {
    @EntityGraph(Person.PERSON_FULL_GRAPH)
    Page<Person> findByLastNameContaining(String lastName, Pageable pageable);

    @Override
    @EntityGraph(Person.PERSON_FULL_GRAPH)
    Optional<Person> findById(Long id);

    @Override
    @EntityGraph(Person.PERSON_FULL_GRAPH)
    Page<Person> findAll(Pageable pageable);

    @Override
    @EntityGraph(Person.PERSON_FULL_GRAPH)
    Person getOne(Long aLong);
}
