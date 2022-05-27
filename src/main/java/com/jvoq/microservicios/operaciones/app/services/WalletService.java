package com.jvoq.microservicios.operaciones.app.services;

import com.jvoq.microservicios.operaciones.app.dtos.WalletDto;
import com.jvoq.microservicios.operaciones.app.models.documents.Wallet;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletService {
	public Flux<WalletDto> findAll();

	public Mono<WalletDto> findById(String id);

	public Mono<WalletDto> save(WalletDto walletDto);

	public Mono<WalletDto> update(WalletDto walletDto, String id);

	public Mono<Void> delete(Wallet wallet);
	
	public Mono<WalletDto> findByCellphoneNumber(String cellphoneNumber);
}
