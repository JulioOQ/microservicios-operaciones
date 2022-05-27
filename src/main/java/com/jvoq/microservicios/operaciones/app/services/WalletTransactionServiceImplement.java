package com.jvoq.microservicios.operaciones.app.services;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvoq.microservicios.operaciones.app.dtos.WalletTransactionDto;
import com.jvoq.microservicios.operaciones.app.models.documents.WalletTransaction;
import com.jvoq.microservicios.operaciones.app.models.repository.WalletTransactionRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WalletTransactionServiceImplement implements WalletTransactionService {

	@Autowired
	WalletTransactionRepository wTransactionRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Flux<WalletTransactionDto> findAll() {
		return wTransactionRepository.findAll().map(this::convertEntityToDto);
	}

	@Override
	public Mono<WalletTransactionDto> findById(String id) {
		return wTransactionRepository.findById(id).map(this::convertEntityToDto);
	}

	@Override
	public Mono<WalletTransactionDto> save(WalletTransactionDto walletTransactionDto) {
		walletTransactionDto.setTransactionDate(new Date());
		WalletTransaction wallet = this.convertDtoToEntity(walletTransactionDto);
		return wTransactionRepository.save(wallet).map(this::convertEntityToDto);
	}

	@Override
	public Mono<WalletTransactionDto> update(WalletTransactionDto walletTransactionDto, String id) {
		return this.findById(id).flatMap(c -> {
			c.setSubject(walletTransactionDto.getSubject());
			c.setDescription(walletTransactionDto.getDescription());
			return this.save(c);
		});
	}

	@Override
	public Mono<Void> delete(WalletTransaction wallet) {
		return wTransactionRepository.delete(wallet);
	}

	private WalletTransactionDto convertEntityToDto(WalletTransaction wallet) {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper.map(wallet, WalletTransactionDto.class);
	}

	private WalletTransaction convertDtoToEntity(WalletTransactionDto walletTransactionDto) {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper.map(walletTransactionDto, WalletTransaction.class);
	}
}
