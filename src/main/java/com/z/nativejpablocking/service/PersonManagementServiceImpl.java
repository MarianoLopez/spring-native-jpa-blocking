package com.z.nativejpablocking.service;

import com.z.nativejpablocking.dao.PersonDAO;
import com.z.nativejpablocking.domain.Person;
import com.z.nativejpablocking.dto.CreatePersonRequest;
import com.z.nativejpablocking.dto.PersonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonManagementServiceImpl implements PersonManagementService {
    private final PersonDAO personDAO;

    @Override
    public PersonResponse save(CreatePersonRequest createPersonRequest) {
        var person = Person.toPerson(createPersonRequest);
        this.personDAO.save(person);
        return PersonResponse.from(person);
    }

    @Override
    public Page<PersonResponse> findAll(Pageable pageable) {
        return this.personDAO.findAll(pageable).map(PersonResponse::from);
    }

    @Override
    public Optional<PersonResponse> findById(Long id) {
        return this.personDAO.findById(id).map(PersonResponse::from);
    }

    @Override
    public Page<PersonResponse> findByLastNameContaining(String lastName, Pageable pageable) {
        return this.personDAO.findByLastNameContaining(lastName, pageable).map(PersonResponse::from);
    }
}
