package com.jvoq.microservicios.operaciones.app.clients;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jvoq.microservicios.operaciones.app.models.documents.Account;

import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "jvoq-microservicio-productos", url="http://localhost:8002/accounts")
public interface ProductAccountClientFeign {
	
	
	@GetMapping("/{id}")
	public Mono<Account> getByIdProductAccount(@PathVariable String id);
	
	@PostMapping
	public Mono<Account> updateProductAccount(@RequestBody Account account);

}
