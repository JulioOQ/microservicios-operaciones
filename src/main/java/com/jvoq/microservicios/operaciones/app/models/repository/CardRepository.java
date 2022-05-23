package com.jvoq.microservicios.operaciones.app.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jvoq.microservicios.operaciones.app.models.documents.Card;

@Repository
public interface CardRepository extends ReactiveMongoRepository<Card, String> {

}
