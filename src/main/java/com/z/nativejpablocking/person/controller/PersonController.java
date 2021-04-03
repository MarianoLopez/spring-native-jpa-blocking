package com.z.nativejpablocking.person.controller;

import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.person.dto.GetPersonRequest;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.person.dto.UpdatePersonRequest;
import com.z.nativejpablocking.person.dto.validation.PersonIdGroup;
import com.z.nativejpablocking.person.dto.validation.UpdatePersonGroup;
import com.z.nativejpablocking.person.service.PersonManagementService;
import com.z.nativejpablocking.person.service.SseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.net.URI;
import java.util.Set;

import static com.z.nativejpablocking.person.controller.PersonController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class PersonController implements PersonManagementController {
    static final String BASE_URL = "/api/v1/person";

    private final PersonManagementService personManagementService;
    private final Validator validator;
    private final SseEmitterService sseEmitterService;


    public PersonController(PersonManagementService personManagementService,
                            Validator validator,
                            SseEmitterService sseEmitterService) {
        this.personManagementService = personManagementService;
        this.validator = validator;
        this.sseEmitterService = sseEmitterService;
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
        return ResponseEntity.accepted().body(this.personManagementService.update(updatePersonRequest));
    }

    private void validateUpdatePersonGroup(UpdatePersonRequest updatePersonRequest) throws ConstraintViolationException {
        Set<ConstraintViolation<UpdatePersonRequest>> constraintViolations = validator.validate(updatePersonRequest, UpdatePersonGroup.class);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<PersonResponse> deleteById(@PathVariable Long id) {
        return ResponseEntity.accepted().body(this.personManagementService.deleteById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<PersonResponse>> findAll(Pageable pageable, GetPersonRequest getPersonRequest) {
        return ResponseEntity.ok(this.personManagementService.findAll(pageable, getPersonRequest));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(@PathVariable Long id) {
        return this.personManagementService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return this.sseEmitterService.createEmitter();
    }
}
