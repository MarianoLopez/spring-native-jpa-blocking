package com.z.nativejpablocking.person.dto.event;

import com.z.nativejpablocking.person.domain.Person;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class PersonEvent extends ApplicationEvent {
    private final Person person;

    public PersonEvent(Object source, Person person) {
        super(source);
        this.person = person;
    }
}
