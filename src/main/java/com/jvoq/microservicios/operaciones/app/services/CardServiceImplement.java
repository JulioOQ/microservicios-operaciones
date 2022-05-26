package com.jvoq.microservicios.operaciones.app.services;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvoq.microservicios.operaciones.app.dtos.CardDto;
import com.jvoq.microservicios.operaciones.app.models.documents.Card;
import com.jvoq.microservicios.operaciones.app.models.repository.CardRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardServiceImplement implements CardService {

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Flux<CardDto> findAll() {
		return cardRepository.findAll().map(this::convertEntityToDto);
	}

	@Override
	public Mono<CardDto> findById(String id) {
		return cardRepository.findById(id).map(this::convertEntityToDto);
	}

	@Override
	public Mono<CardDto> save(CardDto cardDto) {
		cardDto.setFechaCreacion(new Date());
		Card card = this.convertDtoToEntity(cardDto);
		return cardRepository.save(card).map(this::convertEntityToDto);
	}

	@Override
	public Mono<CardDto> update(CardDto cardDto, String id) {
		return this.findById(id).flatMap(c -> {
			c.setNumTarjeta(cardDto.getNumTarjeta());
			c.setTipo(cardDto.getTipo());
			c.setFechaExpiracion(cardDto.getFechaExpiracion());
			c.setFechaCreacion(cardDto.getFechaCreacion());
			return this.save(c);
		});
	}

	@Override
	public Mono<Void> delete(Card card) {
		return cardRepository.delete(card);
	}

	private CardDto convertEntityToDto(Card card) {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper.map(card, CardDto.class);
	}

	private Card convertDtoToEntity(CardDto cardDto) {
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper.map(cardDto, Card.class);
	}
}
