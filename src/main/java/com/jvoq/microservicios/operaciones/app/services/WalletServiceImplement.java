package com.jvoq.microservicios.operaciones.app.services;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jvoq.microservicios.operaciones.app.dtos.WalletDto;
import com.jvoq.microservicios.operaciones.app.models.documents.Wallet;
import com.jvoq.microservicios.operaciones.app.models.repository.WalletRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class WalletServiceImplement implements WalletService {

	@Autowired
	WalletRepository walletRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Flux<WalletDto> findAll() {
		return walletRepository.findAll().map(this::convertEntityToDto);
	}

	@Override
	public Mono<WalletDto> findById(String id) {
		return walletRepository.findById(id).map(this::convertEntityToDto);
	}

	@Override
	public Mono<WalletDto> save(WalletDto walletDto) {
		walletDto.setFechaCreacion(new Date());
		Wallet wallet = this.convertDtoToEntity(walletDto);
		return walletRepository.save(wallet).map(this::convertEntityToDto);
	}

	@Override
	public Mono<WalletDto> update(WalletDto walletDto, String id) {
		return this.findById(id).flatMap(c -> {
			c.setCellphoneNumber(walletDto.getCellphoneNumber());
			c.setEmail(walletDto.getEmail());
			c.setImei(walletDto.getImei());
			c.setIdCard(walletDto.getIdCard());
			c.setBalance(c.getBalance()+ walletDto.getBalance());
			return this.save(c);
		});
	}

	@Override
	public Mono<Void> delete(Wallet wallet) {
		return walletRepository.delete(wallet);
	}

	@Override
	public Mono<WalletDto> findByCellphoneNumber(String cellphoneNumber) {
		return walletRepository.findByCellphoneNumber(cellphoneNumber).map(this::convertEntityToDto);
	}

	private WalletDto convertEntityToDto(Wallet wallet) {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper.map(wallet, WalletDto.class);
	}

	private Wallet convertDtoToEntity(WalletDto walletDto) {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper.map(walletDto, Wallet.class);
	}
}
