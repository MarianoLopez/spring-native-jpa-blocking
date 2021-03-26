package com.z.nativejpablocking.service;

import com.z.nativejpablocking.dto.CreatePersonRequest;
import com.z.nativejpablocking.dto.PersonResponse;
import com.z.nativejpablocking.dto.UpdatePersonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public interface PersonManagementService {
    PersonResponse save(CreatePersonRequest createPersonRequest);
    Page<PersonResponse> findAll(Pageable pageable);
    Optional<PersonResponse> findById(Long id);
    Page<PersonResponse> findByLastNameContaining(String lastName, Pageable pageable);
    PersonResponse update(UpdatePersonRequest updatePersonRequest) throws EntityNotFoundException;
}
