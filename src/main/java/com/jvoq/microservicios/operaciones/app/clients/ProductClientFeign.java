package com.jvoq.microservicios.operaciones.app.clients;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jvoq.microservicios.operaciones.app.models.documents.Product;

import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "jvoq-microservicio-productos", url = "localhost:8002")
public interface ProductClientFeign {

	@GetMapping("/products/{id}")
	public Mono<Product> findProductById(@PathVariable String id);

}
