package com.z.nativejpablocking.person.dto.event;

import com.z.nativejpablocking.person.domain.Person;

public class UpdatePersonEvent extends PersonEvent{
    public UpdatePersonEvent(Object source, Person person) {
        super(source, person);
    }
}
