package com.jvoq.microservicios.operaciones.app.services;

import com.jvoq.microservicios.operaciones.app.models.documents.Client;
import com.jvoq.microservicios.operaciones.app.models.documents.Product;
import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

	public Flux<Transaction> findAll();

	public Mono<Transaction> findById(String id);

	public Mono<Transaction> save(Transaction transaction);

	public Mono<Void> delete(Transaction transaction);

	public Mono<Product> findProductById(String id);
	
	public Mono<Client> findClientById(String id);
}
