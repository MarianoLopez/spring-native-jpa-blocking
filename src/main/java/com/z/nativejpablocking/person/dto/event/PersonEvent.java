package com.z.nativejpablocking.person.dto.event;

import com.z.nativejpablocking.person.domain.Person;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class PersonEvent extends ApplicationEvent {
    private Person person;

    public PersonEvent(Object source, Person person) {
        super(source);
        this.person = person;
    }
}
