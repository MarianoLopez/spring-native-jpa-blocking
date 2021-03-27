package com.z.nativejpablocking.person.service;

import com.z.nativejpablocking.person.dao.PersonDAO;
import com.z.nativejpablocking.person.domain.Person;
import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.person.dto.UpdatePersonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonManagementServiceImpl implements PersonManagementService {
    private final PersonDAO personDAO;

    @Transactional
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

    @Transactional
    @Override
    public PersonResponse update(UpdatePersonRequest updatePersonRequest) throws EntityNotFoundException {
        var person= findByIdOrElseThrow(updatePersonRequest.getId());

        this.mergeIfNotNull(person, updatePersonRequest);
        person.setLastModifiedDate(LocalDateTime.now());
        this.personDAO.save(person);
        return PersonResponse.from(person);
    }

    @Transactional
    @Override
    public PersonResponse deleteById(Long id) throws EntityNotFoundException{
        var person = findByIdOrElseThrow(id);
        person.setEnabled(false);
        this.personDAO.save(person);
        return PersonResponse.from(person);
    }

    private Person findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return this.personDAO
                .findById(id)
                .orElseThrow(() -> this.entityNotFoundException(id));
    }

    private void mergeIfNotNull(Person person, UpdatePersonRequest updatePersonRequest) {
        if (updatePersonRequest.getFirstName() != null) {
            person.setFirstName(updatePersonRequest.getFirstName());
        }

        if (updatePersonRequest.getLastName() != null) {
            person.setLastName(updatePersonRequest.getLastName());
        }
    }

    private EntityNotFoundException entityNotFoundException(Long id) {
        return new EntityNotFoundException(String.format("Entity [%d] not found", id));
    }
}
