package com.z.nativejpablocking.controller;

import com.z.nativejpablocking.dto.CreatePersonRequest;
import com.z.nativejpablocking.dto.PersonResponse;
import com.z.nativejpablocking.dto.UpdatePersonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

public interface PersonManagementController {
    ResponseEntity<Page<PersonResponse>> findAll(Pageable pageable);
    ResponseEntity<Optional<PersonResponse>> findById(Long id);
    ResponseEntity<PersonResponse> save(CreatePersonRequest createPersonRequest);
    ResponseEntity<PersonResponse> update(UpdatePersonRequest updatePersonRequest) throws EntityNotFoundException, ConstraintViolationException;
    ResponseEntity<PersonResponse> deleteById(Long id);
    ResponseEntity<Page<PersonResponse>> findByLastNameContaining(String lastName, Pageable pageable);
}
