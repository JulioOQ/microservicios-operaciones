package com.jvoq.microservicios.operaciones.app.clients;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jvoq.microservicios.operaciones.app.models.documents.Client;

import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "jvoq-microservicio-clientes", url = "localhost:8090")
public interface UserClientFeign {

	@GetMapping("/api/clients/{id}")
	public Mono<Client> findById(@PathVariable String id);
}
