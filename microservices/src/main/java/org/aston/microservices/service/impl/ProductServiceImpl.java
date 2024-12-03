package org.aston.microservices.service.impl;

import lombok.RequiredArgsConstructor;
import org.aston.microservices.dto.CreateProductDto;
import org.aston.microservices.event.ProductCreatedEvent;
import org.aston.microservices.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public String createProduct(CreateProductDto createProductDto) {
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, createProductDto.getTitle(),
                createProductDto.getPrice(), createProductDto.getDescription());

        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate
                .send("product-created-event-topic", productId, productCreatedEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Product created successfully: {}", result.getRecordMetadata());
            }
        });

        LOGGER.info("Return: {}", productId);

        return productId;
    }
}
