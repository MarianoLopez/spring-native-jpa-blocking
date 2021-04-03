package com.z.nativejpablocking.person.dto.event;

import com.z.nativejpablocking.person.domain.Person;

public class CreatePersonEvent extends PersonEvent{
    public CreatePersonEvent(Object source, Person person) {
        super(source, person);
    }
}
