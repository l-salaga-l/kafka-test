package org.aston.emailnotification.handler;

import org.aston.microservices.event.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductCreatedEventHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductCreatedEventHandler.class);

    @KafkaListener(topics = "product-created-event-topic", groupId = "product-created-events")
    public void listen(ProductCreatedEvent event) {
        LOGGER.info("Received event {}", event.getTitle());
    }
}
