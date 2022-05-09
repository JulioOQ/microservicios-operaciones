package com.jvoq.microservicios.operaciones.app.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jvoq.microservicios.operaciones.app.models.documents.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

	Flux<Account> findByIdProductoAndIdCliente(String idProducto, String idCliente);

	Flux<Account> findAccountByIdCliente(String idCliente);

	Mono<Account> findAccountByIdClienteAndIdProducto(String idClient, String idProduct);
}
