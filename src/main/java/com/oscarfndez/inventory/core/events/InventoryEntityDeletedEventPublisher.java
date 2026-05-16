package com.oscarfndez.inventory.core.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryEntityDeletedEventPublisher {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${inventory.events.enabled:true}")
    private boolean inventoryEventsEnabled;

    @Value("${inventory.events.topic:games-collection.inventory-events}")
    private String inventoryEventsTopic;

    public void gameDeleted(UUID gameId, String gameName) {
        publish("game.deleted", "GAME", gameId, gameName);
    }

    public void platformDeleted(UUID platformId, String platformName) {
        publish("platform.deleted", "PLATFORM", platformId, platformName);
    }

    public void studioDeleted(UUID studioId, String studioName) {
        publish("studio.deleted", "STUDIO", studioId, studioName);
    }

    private void publish(String eventType, String entityType, UUID entityId, String entityName) {
        if (!inventoryEventsEnabled) {
            log.debug("Inventory event publishing disabled type={} entityType={} entityId={}", eventType, entityType, entityId);
            return;
        }

        InventoryEntityDeletedEvent event = new InventoryEntityDeletedEvent(
                eventType,
                entityType,
                entityId,
                entityName,
                Instant.now()
        );

        try {
            String payload = objectMapper.writeValueAsString(event);
            jmsTemplate.convertAndSend(inventoryEventsTopic, payload);
            log.info(
                    "Published inventory event type={} entityType={} entityId={} topic={}",
                    eventType,
                    entityType,
                    entityId,
                    inventoryEventsTopic
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize inventory deleted event.", exception);
        }
    }
}
