package com.jvoq.microservicios.operaciones.app.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jvoq.microservicios.operaciones.app.models.documents.WalletTransaction;

@Repository
public interface WalletTransactionRepository extends ReactiveMongoRepository<WalletTransaction, String> {

}
