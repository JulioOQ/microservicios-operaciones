package com.jvoq.microservicios.operaciones.app.services;

import com.jvoq.microservicios.operaciones.app.models.documents.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

	public Flux<Account> findAll();

	public Mono<Account> findById(String id);

	public Mono<Account> save(Account account);

	public Mono<Void> delete(Account account);

	public Flux<Account> findAccoutsByIdClient(String idClient);

	public Flux<Account> findAccountsByIdClientAndIdProduct(String idClient, String idProduct);

}
