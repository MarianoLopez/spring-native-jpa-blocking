package com.z.nativejpablocking.person.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.z.nativejpablocking.person.dto.event.CreatePersonEvent;
import com.z.nativejpablocking.person.dto.event.DeletePersonEvent;
import com.z.nativejpablocking.person.dto.event.PersonEvent;
import com.z.nativejpablocking.person.dto.event.UpdatePersonEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

@Component
@Slf4j
public class PersonEventListener {
    private final ObjectMapper objectMapper;
    private final SubmissionPublisher<SseEmitter.SseEventBuilder> publisher;

    public PersonEventListener(ObjectMapper objectMapper,
                               Flow.Subscriber<SseEmitter.SseEventBuilder> subscriber) {
        this.objectMapper = objectMapper;
        this.publisher = new SubmissionPublisher<>();
        publisher.subscribe(subscriber);
    }

    @TransactionalEventListener
    public void onCreatePerson(CreatePersonEvent createPersonEvent) {
        logEvent(createPersonEvent);
        publishEvent(createPersonEvent);
    }

    @TransactionalEventListener
    public void onUpdatePerson(UpdatePersonEvent updatePersonEvent) {
        logEvent(updatePersonEvent);
        publishEvent(updatePersonEvent);
    }

    @TransactionalEventListener
    public void onDeletePerson(DeletePersonEvent deletePersonEvent) {
        logEvent(deletePersonEvent);
        publishEvent(deletePersonEvent);
    }

    private void publishEvent(PersonEvent personEvent) {
        this.publisher.submit(this.buildSseEvent(personEvent));
    }

    private SseEmitter.SseEventBuilder buildSseEvent(PersonEvent personEvent) {
        return SseEmitter
                .event()
                .data(personEvent.getPerson(), MediaType.APPLICATION_JSON)
                .id(UUID.randomUUID().toString())
                .name(personEvent.getClass().getSimpleName());
    }


    @SneakyThrows
    private void logEvent(PersonEvent personEvent) {
        var json = objectMapper.writeValueAsString(personEvent.getPerson());
        var node = objectMapper.readValue(json, ObjectNode.class);
        node.put("class", personEvent.getClass().getSimpleName());
        log.debug(node.toString());
    }

}
