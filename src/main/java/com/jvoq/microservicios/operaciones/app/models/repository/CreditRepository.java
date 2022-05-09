package com.jvoq.microservicios.operaciones.app.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jvoq.microservicios.operaciones.app.models.documents.Credit;


@Repository
public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {

}
