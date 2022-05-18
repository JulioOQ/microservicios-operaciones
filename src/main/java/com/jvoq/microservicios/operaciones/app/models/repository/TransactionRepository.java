package com.jvoq.microservicios.operaciones.app.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;

import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

	Flux<Transaction> findByDestinoAndTipoTransaccion(String idCuenta, String tipoTransaccion);

	Flux<Transaction> findByOrigenAndTipoTransaccion(String idCuenta, String tipoTransaccion);

}
