package com.jvoq.microservicios.operaciones.app.controllers;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvoq.microservicios.operaciones.app.dtos.CardDetailDto;
import com.jvoq.microservicios.operaciones.app.dtos.CardDto;
import com.jvoq.microservicios.operaciones.app.models.documents.Card;
import com.jvoq.microservicios.operaciones.app.services.CardDetailService;
import com.jvoq.microservicios.operaciones.app.services.CardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
@RequestMapping("/cards")
public class CardController {

	@Autowired
	private CardService cardService;

	@Autowired
	private CardDetailService cardDetailService;

	@Autowired
	private ModelMapper mapper;

	@Value("${mensaje.verificacion:default}")
	private String mensaje;

	@GetMapping("verificar")
	public String viewDiscounts() {
		return "Mensaje -> " + mensaje;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<CardDto>>> getAll() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(cardService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<CardDto>> getById(@PathVariable String id) {
		return cardService.findById(id).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<CardDto>> create(@RequestBody CardDto cardDto) {
		return cardService.save(cardDto).map(c -> ResponseEntity.created(URI.create("/cards".concat(c.getId_tarjeta())))
				.contentType(MediaType.APPLICATION_JSON).body(c));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<CardDto>> update(@RequestBody CardDto cardDto, @PathVariable String id) {
		return cardService.update(cardDto, id)
				.map(c -> ResponseEntity.created(URI.create("/cards".concat(c.getId_tarjeta())))
						.contentType(MediaType.APPLICATION_JSON).body(c))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> drop(@PathVariable String id) {
		return cardService.findById(id).flatMap(c -> {
			Card card = mapper.map(c, Card.class);
			return cardService.delete(card).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping("/asociar")
	public Mono<ResponseEntity<CardDetailDto>> asociar(@RequestBody CardDetailDto cardDetailDto) {
		return cardService.findById(cardDetailDto.getIdTarjeta()).flatMap(ca -> {
			return cardDetailService.save(cardDetailDto)
					.map(c -> ResponseEntity.created(URI.create("/card_details".concat(c.getIdCardetail())))
							.contentType(MediaType.APPLICATION_JSON).body(c));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
