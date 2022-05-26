package com.jvoq.microservicios.operaciones.app.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jvoq.microservicios.operaciones.app.models.documents.Account;

import reactor.core.publisher.Flux;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

	Flux<Account> findAccountsByIdCliente(String idClient);

	Flux<Account> findAccountsByIdClienteAndIdProducto(String idClient, String idProduct);
}
