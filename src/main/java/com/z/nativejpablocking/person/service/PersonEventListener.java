package com.z.nativejpablocking.person.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.person.dto.event.PersonEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

@Component
@Slf4j
public class PersonEventListener implements ApplicationListener<PersonEvent> {
    private final ObjectMapper objectMapper;
    private final SubmissionPublisher<SseEmitter.SseEventBuilder> publisher;

    public PersonEventListener(ObjectMapper objectMapper,
                               Flow.Subscriber<SseEmitter.SseEventBuilder> subscriber) {
        this.objectMapper = objectMapper;
        this.publisher = new SubmissionPublisher<>();
        publisher.subscribe(subscriber);
    }

    private void publishEvent(PersonEvent personEvent) {
        this.publisher.submit(this.buildSseEvent(personEvent));
    }

    @SneakyThrows
    private SseEmitter.SseEventBuilder buildSseEvent(PersonEvent personEvent) {
        var json = objectMapper.writeValueAsString(PersonResponse.from(personEvent.getPerson()));
        return SseEmitter
                .event()
                .data(json, MediaType.TEXT_EVENT_STREAM)
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

    @TransactionalEventListener
    @Override
    public void onApplicationEvent(PersonEvent personEvent) {
        logEvent(personEvent);
        publishEvent(personEvent);
    }
}
