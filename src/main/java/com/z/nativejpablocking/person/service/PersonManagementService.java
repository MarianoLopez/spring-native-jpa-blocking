package com.z.nativejpablocking.person.service;

import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.person.dto.GetPersonRequest;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.person.dto.UpdatePersonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public interface PersonManagementService {
    PersonResponse save(CreatePersonRequest createPersonRequest);
    Page<PersonResponse> findAll(Pageable pageable, GetPersonRequest getPersonRequest);
    Optional<PersonResponse> findById(Long id);
    PersonResponse update(UpdatePersonRequest updatePersonRequest) throws EntityNotFoundException;
    PersonResponse deleteById(Long id) throws EntityNotFoundException;
}
