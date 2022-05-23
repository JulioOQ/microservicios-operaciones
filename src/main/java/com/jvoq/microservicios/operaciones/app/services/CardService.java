package com.jvoq.microservicios.operaciones.app.services;

import com.jvoq.microservicios.operaciones.app.dtos.CardDto;
import com.jvoq.microservicios.operaciones.app.models.documents.Card;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardService {
	public Flux<CardDto> findAll();

	public Mono<CardDto> findById(String id);

	public Mono<CardDto> save(CardDto cardDto);

	public Mono<CardDto> update(CardDto cardDto, String id);

	public Mono<Void> delete(Card card);
}
