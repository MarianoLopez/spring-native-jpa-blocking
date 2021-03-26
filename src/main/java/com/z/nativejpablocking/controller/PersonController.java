package com.z.nativejpablocking.controller;

import com.z.nativejpablocking.dto.*;
import com.z.nativejpablocking.service.PersonManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

import static com.z.nativejpablocking.controller.PersonController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class PersonController implements PersonManagementController {
    static final String BASE_URL = "/api/v1/person";

    private final PersonManagementService personManagementService;
    private final Validator validator;

    public PersonController(PersonManagementService personManagementService,
                            Validator validator) {
        this.personManagementService = personManagementService;
        this.validator = validator;
    }

    @Override
    @PostMapping
    public ResponseEntity<PersonResponse> save(@RequestBody @Validated CreatePersonRequest createPersonRequest) {
        var response = this.personManagementService.save(createPersonRequest);
        return ResponseEntity
                .created(URI.create(String.format("%s/%d", BASE_URL, response.getId())))
                .body(response);
    }

    @Override
    @PutMapping
    public ResponseEntity<PersonResponse> update(@RequestBody @Validated(PersonIdGroup.class) UpdatePersonRequest updatePersonRequest) throws EntityNotFoundException {
        validateUpdatePersonGroup(updatePersonRequest);
        return ResponseEntity.ok(this.personManagementService.update(updatePersonRequest));
    }

    private void validateUpdatePersonGroup(UpdatePersonRequest updatePersonRequest) throws ConstraintViolationException{
        Set<ConstraintViolation<UpdatePersonRequest>> constraintViolations = validator.validate(updatePersonRequest, UpdatePersonGroup.class);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Override
    public ResponseEntity<PersonResponse> deleteById(Long id) {
        return null;
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<PersonResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(this.personManagementService.findAll(pageable));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Optional<PersonResponse>> findById(@PathVariable Long id) {
        var response = this.personManagementService.findById(id);

        if (response.isPresent()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    @GetMapping(params = {"lastName"})
    public ResponseEntity<Page<PersonResponse>> findByLastNameContaining(
            @RequestParam("lastName") String lastName, Pageable pageable) {
        return ResponseEntity.ok(this.personManagementService.findByLastNameContaining(lastName, pageable));
    }
}
