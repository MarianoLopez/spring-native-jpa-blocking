package com.z.nativejpablocking.person.service;

import com.z.nativejpablocking.person.dao.PersonDAO;
import com.z.nativejpablocking.job.domain.Job;
import com.z.nativejpablocking.person.domain.City;
import com.z.nativejpablocking.person.domain.CityId;
import com.z.nativejpablocking.person.domain.Person;
import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.person.dto.GetPersonRequest;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.person.dto.UpdatePersonRequest;
import com.z.nativejpablocking.person.dto.event.CreatePersonEvent;
import com.z.nativejpablocking.person.dto.event.DeletePersonEvent;
import com.z.nativejpablocking.person.dto.event.UpdatePersonEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonManagementServiceImpl implements PersonManagementService {
    private final PersonDAO personDAO;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Override
    public PersonResponse save(CreatePersonRequest createPersonRequest) {
        var person = Person.toPerson(createPersonRequest);

        this.personDAO.save(person);
        this.applicationEventPublisher.publishEvent(new CreatePersonEvent(this, person));

        return PersonResponse.from(person);
    }

    @Override
    public Page<PersonResponse> findAll(Pageable pageable, GetPersonRequest getPersonRequest) {
        Page<Person> result;

        if (getPersonRequest.getLastName() != null) {
            result = this.personDAO.findByLastNameContaining(getPersonRequest.getLastName(), pageable);
        } else {
            result = this.personDAO.findAll(pageable);
        }
        return result.map(PersonResponse::from);
    }

    @Override
    public Optional<PersonResponse> findById(Long id) {
        return this.personDAO.findById(id).map(PersonResponse::from);
    }

    @Transactional
    @Override
    public PersonResponse update(UpdatePersonRequest updatePersonRequest) throws EntityNotFoundException {
        var person = findByIdOrElseThrow(updatePersonRequest.getId());

        this.merge(person, updatePersonRequest);
        person.setLastModifiedDate(LocalDateTime.now());
        this.personDAO.save(person);
        this.applicationEventPublisher.publishEvent(new UpdatePersonEvent(this, person));
        return PersonResponse.from(person);
    }

    @Transactional
    @Override
    public PersonResponse deleteById(Long id) throws EntityNotFoundException {
        var person = findByIdOrElseThrow(id);
        person.setEnabled(false);
        this.personDAO.save(person);
        this.applicationEventPublisher.publishEvent(new DeletePersonEvent(this, person));
        return PersonResponse.from(person);
    }

    private Person findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return this.personDAO
                .findById(id)
                .orElseThrow(() -> this.entityNotFoundException(id));
    }

    private void merge(Person person, UpdatePersonRequest updatePersonRequest) {
        if (updatePersonRequest.getFirstName() != null) {
            person.setFirstName(updatePersonRequest.getFirstName());
        }

        if (updatePersonRequest.getLastName() != null) {
            person.setLastName(updatePersonRequest.getLastName());
        }

        if (updatePersonRequest.getCityName() != null) {
            person.getCity().getId().setName(updatePersonRequest.getCityName());
        }

        if (updatePersonRequest.getCountryISOCode() != null) {
            person.getCity().getId().setCountryIsoCode(updatePersonRequest.getCountryISOCode());
        }

        person.setJobs(updatePersonRequest.getJobs().stream().map(Job::toJob).collect(Collectors.toSet()));
    }

    private EntityNotFoundException entityNotFoundException(Long id) {
        return new EntityNotFoundException(String.format("Entity [%d] not found", id));
    }
}
