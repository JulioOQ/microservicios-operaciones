package com.jvoq.microservicios.operaciones.app.services;

import com.jvoq.microservicios.operaciones.app.models.documents.Product;

import reactor.core.publisher.Mono;

public interface ProductService {

	public Mono<Product> findById(String id);
}
