package com.oscarfndez.inventory.adapters.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarfndez.inventory.core.events.UserLifecycleEvent;
import com.oscarfndez.inventory.core.events.UserLifecycleEventHandler;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserLifecycleEventListenerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final UserLifecycleEventHandler userLifecycleEventHandler = mock(UserLifecycleEventHandler.class);
    private final UserLifecycleEventListener listener = new UserLifecycleEventListener(objectMapper, userLifecycleEventHandler);

    @Test
    void onUserLifecycleEventParsesPayloadAndDelegatesToHandler() throws Exception {
        UUID userId = UUID.randomUUID();
        String payload = objectMapper.writeValueAsString(new UserLifecycleEvent(
                "afterDeleting",
                userId,
                "oscar@example.com",
                "Oscar",
                "Fernandez",
                "USER",
                Instant.parse("2026-05-02T10:15:30Z")
        ));

        listener.onUserLifecycleEvent(payload);

        var eventCaptor = forClass(UserLifecycleEvent.class);
        verify(userLifecycleEventHandler).handle(eventCaptor.capture());
        UserLifecycleEvent event = eventCaptor.getValue();
        assertThat(event.eventType()).isEqualTo("afterDeleting");
        assertThat(event.userId()).isEqualTo(userId);
        assertThat(event.email()).isEqualTo("oscar@example.com");
    }

    @Test
    void onUserLifecycleEventThrowsWhenPayloadIsInvalid() {
        assertThatThrownBy(() -> listener.onUserLifecycleEvent("not-json"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid user lifecycle event payload.");
    }
}
