package com.z.nativejpablocking.person.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.z.nativejpablocking.person.dto.event.CreatePersonEvent;
import com.z.nativejpablocking.person.dto.event.DeletePersonEvent;
import com.z.nativejpablocking.person.dto.event.PersonEvent;
import com.z.nativejpablocking.person.dto.event.UpdatePersonEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class PersonEventListener {
    private final ObjectMapper objectMapper;

    @TransactionalEventListener
    public void onCreatePerson(CreatePersonEvent createPersonEvent){
        logEvent(createPersonEvent);
    }

    @TransactionalEventListener
    public void onUpdatePerson(UpdatePersonEvent updatePersonEvent){
        logEvent(updatePersonEvent);
    }

    @TransactionalEventListener
    public void onDeletePerson(DeletePersonEvent deletePersonEvent){
        logEvent(deletePersonEvent);
    }

    @SneakyThrows
    private void logEvent(PersonEvent personEvent) {
        var json = objectMapper.writeValueAsString(personEvent.getPerson());
        var node = objectMapper.readValue(json, ObjectNode.class);
        node.put("class", personEvent.getClass().getSimpleName());
        log.debug(node.toString());
    }

}
