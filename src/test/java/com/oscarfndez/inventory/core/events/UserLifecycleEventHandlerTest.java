package com.oscarfndez.inventory.core.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;

class UserLifecycleEventHandlerTest {

    private final UserLifecycleEventHandler handler = new UserLifecycleEventHandler();

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
}
