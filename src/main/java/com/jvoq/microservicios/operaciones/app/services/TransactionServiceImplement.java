package com.jvoq.microservicios.operaciones.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvoq.microservicios.operaciones.app.clients.ProductAccountClientFeign;
import com.jvoq.microservicios.operaciones.app.models.documents.Account;
import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;
import com.jvoq.microservicios.operaciones.app.models.repository.TransactionRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImplement implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;
	

	ProductAccountClientFeign productAccountClientFeign;

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
	public Mono<Account> findByIdAccount(String id) {
		return productAccountClientFeign.getByIdProductAccount(id);
	}

	@Override
	public Mono<Account> updateAccount(Account account) {
		return productAccountClientFeign.updateProductAccount(account);
	}
}
