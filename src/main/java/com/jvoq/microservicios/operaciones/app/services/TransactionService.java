package com.jvoq.microservicios.operaciones.app.services;



import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

	public Flux<Transaction> findAll();

	public Mono<Transaction> findById(String id);

	public Mono<Transaction> save(Transaction transaction);

	public Mono<Void> delete(Transaction transaction);
}
