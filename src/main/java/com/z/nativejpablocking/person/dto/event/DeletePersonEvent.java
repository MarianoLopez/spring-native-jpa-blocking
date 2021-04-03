package com.z.nativejpablocking.person.dto.event;

import com.z.nativejpablocking.person.domain.Person;

public class DeletePersonEvent extends PersonEvent{
    public DeletePersonEvent(Object source, Person person) {
        super(source, person);
    }
}
