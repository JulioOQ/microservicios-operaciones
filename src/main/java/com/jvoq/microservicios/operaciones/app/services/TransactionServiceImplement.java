package com.jvoq.microservicios.operaciones.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvoq.microservicios.operaciones.app.clients.ProductClientFeign;
import com.jvoq.microservicios.operaciones.app.models.documents.Product;
import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;
import com.jvoq.microservicios.operaciones.app.models.repository.TransactionRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImplement implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	ProductClientFeign productClientFeign;

	@Override
	public Flux<Transaction> findAll() {
		return transactionRepository.findAll();
	}

	@Override
	public Mono<Transaction> findById(String id) {
		return transactionRepository.findById(id);
	}

	@Override
	public Mono<Transaction> save(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

	@Override
	public Mono<Void> delete(Transaction transaction) {
		return transactionRepository.delete(transaction);
	}

	@Override
	public Mono<Product> findProductById(String id) {
		return productClientFeign.findProductById(id);
	}

	@Override
	public Flux<Transaction> getTransactionsByCuentaAndTipoTransaccion(String idCuenta, String tipoTransaccion) {
		if (tipoTransaccion.equalsIgnoreCase("Deposito")) {
			return transactionRepository.findByDestinoAndTipoTransaccion(idCuenta, tipoTransaccion);
		} else {
			return transactionRepository.findByOrigenAndTipoTransaccion(idCuenta, tipoTransaccion);
		}
	}

  @Override
  public Flux<Transaction> getMovementsByClienteAndProducto(String idCliente, String idProducto) {
   
    return transactionRepository.findByIdClienteAndIdProducto(idCliente, idProducto);
  }
}
