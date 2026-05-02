package com.oscarfndez.inventory.core.events;

import com.oscarfndez.inventory.core.services.GameItemService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class UserLifecycleEventHandlerTest {

    private final GameItemService gameItemService = mock(GameItemService.class);
    private final UserLifecycleEventHandler handler = new UserLifecycleEventHandler(gameItemService);

    @Test
    void handleAcceptsKnownUserLifecycleEvents() {
        for (String eventType : java.util.List.of("afterCreating", "afterUpdating", "afterDeleting")) {
            UserLifecycleEvent event = new UserLifecycleEvent(
                    eventType,
                    UUID.randomUUID(),
                    "oscar@example.com",
                    "Oscar",
                    "Fernandez",
                    "USER",
                    Instant.now()
            );

            assertThatNoException().isThrownBy(() -> handler.handle(event));
        }
    }

    @Test
    void handleIgnoresUnknownUserLifecycleEvent() {
        UserLifecycleEvent event = new UserLifecycleEvent(
                "unexpected",
                UUID.randomUUID(),
                "oscar@example.com",
                "Oscar",
                "Fernandez",
                "USER",
                Instant.now()
        );

        assertThatNoException().isThrownBy(() -> handler.handle(event));
    }

    @Test
    void handleDeactivatesCollectionWhenUserIsDeleted() {
        UUID userId = UUID.randomUUID();
        UserLifecycleEvent event = event("afterDeleting", userId);

        handler.handle(event);

        verify(gameItemService).deactivateCollectionByUserId(userId);
    }

    @Test
    void handleDoesNotDeactivateCollectionWhenUserIsCreatedOrUpdated() {
        UUID userId = UUID.randomUUID();

        handler.handle(event("afterCreating", userId));
        handler.handle(event("afterUpdating", userId));

        verify(gameItemService, never()).deactivateCollectionByUserId(userId);
    }

    private static UserLifecycleEvent event(String eventType, UUID userId) {
        return new UserLifecycleEvent(
                eventType,
                userId,
                "oscar@example.com",
                "Oscar",
                "Fernandez",
                "USER",
                Instant.now()
        );
    }
}
