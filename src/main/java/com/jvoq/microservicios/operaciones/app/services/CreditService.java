package com.jvoq.microservicios.operaciones.app.services;

import com.jvoq.microservicios.operaciones.app.models.documents.Credit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {

	public Flux<Credit> findAll();

	public Mono<Credit> findById(String id);

	public Mono<Credit> save(Credit credit);

	public Mono<Void> delete(Credit credit);

	public Flux<Credit> findCreditsByIdClientAndIdProduct(String idClient, String idProduct);
	
	public Flux<Credit> findCreditByIdClient(String idClient);
}
