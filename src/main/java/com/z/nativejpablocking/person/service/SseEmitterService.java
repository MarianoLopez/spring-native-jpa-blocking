package com.z.nativejpablocking.person.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Flow;

public interface SseEmitterService extends Flow.Subscriber<SseEmitter.SseEventBuilder> {
    SseEmitter createEmitter();
}
