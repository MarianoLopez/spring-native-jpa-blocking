package com.z.nativejpablocking.dao;

import com.z.nativejpablocking.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDAO extends PagingAndSortingRepository<Person, Long> {
    Page<Person> findByLastNameContaining(String lastName, Pageable pageable);
}
