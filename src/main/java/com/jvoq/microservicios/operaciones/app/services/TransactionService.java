package com.jvoq.microservicios.operaciones.app.services;

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

	public Flux<Transaction> getTransactionsByCuentaAndTipoTransaccion(String idCuenta, String tipoTransaccion);

	public Flux<Transaction> getMovementsByClienteAndProducto(String idCliente, String idProducto);

	public Flux<Object> findProductsByIdClient(String idCliente);
}
