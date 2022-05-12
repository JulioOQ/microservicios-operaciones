package com.jvoq.microservicios.operaciones.app.clients;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jvoq.microservicios.operaciones.app.models.documents.Client;

import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "jvoq-microservicio-clientes", url = "localhost:8001")
public interface UserClientFeign {

	@GetMapping("/{id}")
	public Mono<Client> findClientById(@PathVariable String id);
}
