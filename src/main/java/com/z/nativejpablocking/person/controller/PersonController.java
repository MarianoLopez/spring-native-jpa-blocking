package com.z.nativejpablocking.person.controller;

import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.person.dto.GetPersonRequest;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.person.dto.UpdatePersonRequest;
import com.z.nativejpablocking.person.dto.validation.PersonIdGroup;
import com.z.nativejpablocking.person.dto.validation.UpdatePersonGroup;
import com.z.nativejpablocking.person.service.PersonManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.Flow;

import static com.z.nativejpablocking.person.controller.PersonController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@Slf4j
public class PersonController implements PersonManagementController, Flow.Subscriber<SseEmitter.SseEventBuilder> {
    static final String BASE_URL = "/api/v1/person";

    private final PersonManagementService personManagementService;
    private final Validator validator;
    private final SseEmitter sseEmitter;
    private Flow.Subscription subscription;

    public PersonController(PersonManagementService personManagementService,
                            Validator validator) {
        this.personManagementService = personManagementService;
        this.validator = validator;
        this.sseEmitter = new SseEmitter(Long.MAX_VALUE);
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

    @GetMapping("/stream")
    public SseEmitter stream() {
        return this.sseEmitter;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(SseEmitter.SseEventBuilder item) {
        try {
            this.sseEmitter.send(item);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error(throwable.getLocalizedMessage());
    }

    @Override
    public void onComplete() {
        this.sseEmitter.complete();
    }
}
