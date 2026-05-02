package com.oscarfndez.inventory.adapters.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarfndez.inventory.core.events.UserLifecycleEvent;
import com.oscarfndez.inventory.core.events.UserLifecycleEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserLifecycleEventListener {

    private final ObjectMapper objectMapper;
    private final UserLifecycleEventHandler userLifecycleEventHandler;

    @JmsListener(
            destination = "${user.events.topic:games-collection.user-events}",
            subscription = "${user.events.subscription:inventory-service-user-events}"
    )
    public void onUserLifecycleEvent(String payload) {
        try {
            UserLifecycleEvent event = objectMapper.readValue(payload, UserLifecycleEvent.class);
            userLifecycleEventHandler.handle(event);
        } catch (JsonProcessingException exception) {
            log.error("Unable to parse user lifecycle event payload={}", payload, exception);
            throw new IllegalArgumentException("Invalid user lifecycle event payload.", exception);
        }
    }
}
