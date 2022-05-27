package com.jvoq.microservicios.operaciones.app.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jvoq.microservicios.operaciones.app.models.documents.CardDetail;

import reactor.core.publisher.Mono;

@Repository
public interface CardDetailRepository extends ReactiveMongoRepository<CardDetail, String> {
	public Mono<CardDetail> findByIdTarjetaAndPrincipal(String idTarjeta, Boolean principal);
}
