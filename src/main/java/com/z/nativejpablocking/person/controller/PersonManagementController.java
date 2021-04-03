package com.z.nativejpablocking.person.controller;

import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.person.dto.GetPersonRequest;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.person.dto.UpdatePersonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

public interface PersonManagementController {
    ResponseEntity<Page<PersonResponse>> findAll(Pageable pageable, GetPersonRequest getPersonRequest);
    ResponseEntity<PersonResponse> findById(Long id);
    ResponseEntity<PersonResponse> save(CreatePersonRequest createPersonRequest);
    ResponseEntity<PersonResponse> update(UpdatePersonRequest updatePersonRequest) throws EntityNotFoundException, ConstraintViolationException;
    ResponseEntity<PersonResponse> deleteById(Long id);
    SseEmitter stream();
}
