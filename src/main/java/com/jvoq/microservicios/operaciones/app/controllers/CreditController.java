package com.jvoq.microservicios.operaciones.app.controllers;

import java.net.URI;

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

import com.jvoq.microservicios.operaciones.app.models.documents.Credit;
import com.jvoq.microservicios.operaciones.app.services.CreditService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RefreshScope
@RestController
@RequestMapping("/credits")
public class CreditController {
	@Autowired
	private CreditService creditService;
	
	@Value("${offers.discount:default}")
	private String discount;
	
	@GetMapping("view-discounts")
	public String viewDiscounts() {
		return "Discount operaciones is " + discount;
	}


	@GetMapping
	public Mono<ResponseEntity<Flux<Credit>>> getAll() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(creditService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Credit>> getById(@PathVariable String id) {
		return creditService.findById(id).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
				.defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@PostMapping
	public Mono<ResponseEntity<Credit>> create(@RequestBody Credit credit) {
		return creditService.save(credit)
				.map(c -> ResponseEntity.created(URI.create("/credits".concat(c.getIdCredito())))
						.contentType(MediaType.APPLICATION_JSON).body(c));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Credit>> update(@RequestBody Credit credit, @PathVariable String id) {
		return creditService.findById(id).flatMap(c -> {
			c.setIdProducto(credit.getIdProducto());
			c.setIdCliente(credit.getIdCliente());
			c.setNumeroCredito(credit.getNumeroCredito());
			c.setMoneda(credit.getMoneda());
			c.setLineaCredito(credit.getLineaCredito());
			c.setSaldo(credit.getSaldo());
			c.setConsumido(credit.getConsumido());
			c.setInteres(credit.getInteres());

			return creditService.save(c);
		}).map(c -> ResponseEntity.created(URI.create("/credits".concat(c.getIdCredito())))
				.contentType(MediaType.APPLICATION_JSON).body(c)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> drop(@PathVariable String id) {
		return creditService.findById(id).flatMap(c -> {
			return creditService.delete(c).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
}
