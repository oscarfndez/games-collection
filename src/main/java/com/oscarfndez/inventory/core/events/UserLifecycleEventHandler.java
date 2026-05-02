package com.oscarfndez.inventory.core.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserLifecycleEventHandler {

    public void handle(UserLifecycleEvent event) {
        switch (event.eventType()) {
            case "afterCreating" -> handleUserCreated(event);
            case "afterUpdating" -> handleUserUpdated(event);
            case "afterDeleting" -> handleUserDeleted(event);
            default -> log.warn("Ignoring unknown user lifecycle event type={} userId={}", event.eventType(), event.userId());
        }
    }

    private void handleUserCreated(UserLifecycleEvent event) {
        log.info("Received user created event userId={} email={} role={}", event.userId(), event.email(), event.role());
    }

    private void handleUserUpdated(UserLifecycleEvent event) {
        log.info("Received user updated event userId={} email={} role={}", event.userId(), event.email(), event.role());
    }

    private void handleUserDeleted(UserLifecycleEvent event) {
        log.info("Received user deleted event userId={} email={} role={}", event.userId(), event.email(), event.role());
    }
}
