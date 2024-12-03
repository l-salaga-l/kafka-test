package org.aston.microservices.service;

import org.aston.microservices.dto.CreateProductDto;

public interface ProductService {
    String createProduct(CreateProductDto createProductDto);
}
