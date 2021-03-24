package com.z.nativejpablocking.controller;

import com.z.nativejpablocking.dto.CreatePersonRequest;
import com.z.nativejpablocking.dto.PersonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface PersonManagementController {
    ResponseEntity<PersonResponse> save(CreatePersonRequest createPersonRequest);
    ResponseEntity<Page<PersonResponse>> findAll(Pageable pageable);
    ResponseEntity<Optional<PersonResponse>> findById(Long id);
    ResponseEntity<Page<PersonResponse>> findByLastNameContaining(String lastName, Pageable pageable);
}
