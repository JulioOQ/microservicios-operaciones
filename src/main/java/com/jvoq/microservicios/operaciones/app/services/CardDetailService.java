package com.jvoq.microservicios.operaciones.app.services;

import com.jvoq.microservicios.operaciones.app.dtos.CardDetailDto;
import com.jvoq.microservicios.operaciones.app.models.documents.CardDetail;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardDetailService {
	public Flux<CardDetailDto> findAll();

	public Mono<CardDetailDto> findById(String id);

	public Mono<CardDetailDto> save(CardDetailDto cardDetailDto);

	public Mono<CardDetailDto> update(CardDetailDto cardDetailDto, String id);

	public Mono<Void> delete(CardDetail cardDetail);

	public Mono<CardDetailDto> findAccountByIdTarjeta(String idTarjeta);
}
