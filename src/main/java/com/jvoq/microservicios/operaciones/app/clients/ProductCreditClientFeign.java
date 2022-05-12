package com.jvoq.microservicios.operaciones.app.clients;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jvoq.microservicios.operaciones.app.models.documents.Account;
import com.jvoq.microservicios.operaciones.app.models.documents.Credit;

import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "jvoq-microservicio-productos")
public interface ProductCreditClientFeign {

	@GetMapping("/{id}")
	public Mono<Credit> getById(@PathVariable String id);
	
	@PostMapping
	public Mono<Credit> create(@RequestBody Account account);
}
