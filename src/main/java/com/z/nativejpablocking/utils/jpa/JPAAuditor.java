package com.z.nativejpablocking.utils.jpa;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass//Designates a class whose mapping information is applied to the entities that inherit from it. A mapped superclass has no separate table defined for it.
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class JPAAuditor {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}
