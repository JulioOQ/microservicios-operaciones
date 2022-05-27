package com.jvoq.microservicios.operaciones.app.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvoq.microservicios.operaciones.app.dtos.CardDetailDto;
import com.jvoq.microservicios.operaciones.app.models.documents.CardDetail;
import com.jvoq.microservicios.operaciones.app.models.repository.CardDetailRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardDetailServiceImplement implements CardDetailService {

	@Autowired
	private CardDetailRepository cardDetailRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Flux<CardDetailDto> findAll() {
		return cardDetailRepository.findAll().map(this::convertEntityToDto);
	}

	@Override
	public Mono<CardDetailDto> findById(String id) {
		return cardDetailRepository.findById(id).map(this::convertEntityToDto);
	}

	@Override
	public Mono<CardDetailDto> save(CardDetailDto cardDetailDto) {
		CardDetail cardDetail = this.convertDtoToEntity(cardDetailDto);
		return cardDetailRepository.save(cardDetail).map(this::convertEntityToDto);
	}

	@Override
	public Mono<CardDetailDto> update(CardDetailDto cardDetailDto, String id) {
		return this.findById(id).flatMap(c -> {
			c.setIdTarjeta(c.getIdTarjeta());
			c.setIdCuenta(c.getIdCuenta());
			c.setPrincipal(cardDetailDto.getPrincipal());
			return this.save(c);
		});
	}

	@Override
	public Mono<Void> delete(CardDetail cardDetail) {
		return cardDetailRepository.delete(cardDetail);
	}

	@Override
	public Mono<CardDetailDto> findAccountByIdTarjeta(String idTarjeta) {
		return cardDetailRepository.findByIdTarjetaAndPrincipal(idTarjeta, true).map(this::convertEntityToDto);
	}

	private CardDetailDto convertEntityToDto(CardDetail cardDetail) {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
		return mapper.map(cardDetail, CardDetailDto.class);
	}

	private CardDetail convertDtoToEntity(CardDetailDto cardDetailDto) {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
		return mapper.map(cardDetailDto, CardDetail.class);
	}
}
