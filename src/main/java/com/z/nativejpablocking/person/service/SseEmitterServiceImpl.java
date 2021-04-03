package com.z.nativejpablocking.person.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow;

@Service
@Slf4j
public class SseEmitterServiceImpl implements SseEmitterService {
    private final Set<SseEmitter> sseEmitters = ConcurrentHashMap.newKeySet();
    private Flow.Subscription subscription;

    @Override
    public SseEmitter createEmitter() {
        final SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sseEmitter.onCompletion(() -> this.sseEmitters.remove(sseEmitter));
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitters.add(sseEmitter);

        return sseEmitter;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(SseEmitter.SseEventBuilder item) {
        this.sseEmitters.forEach(sseEmitter -> {
            try {
                sseEmitter.send(item);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
        });
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error(throwable.getLocalizedMessage());
    }

    @Override
    public void onComplete() {
        this.sseEmitters.forEach(SseEmitter::complete);
    }
}
