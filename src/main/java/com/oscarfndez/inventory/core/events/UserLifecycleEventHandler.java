package com.oscarfndez.inventory.core.events;

import com.oscarfndez.inventory.core.services.GameItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLifecycleEventHandler {

    private final GameItemService gameItemService;

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
        int deactivatedItems = gameItemService.deactivateCollectionByUserId(event.userId());
        log.info(
                "Received user deleted event userId={} email={} role={} deactivatedCollectionItems={}",
                event.userId(),
                event.email(),
                event.role(),
                deactivatedItems
        );
    }
}
