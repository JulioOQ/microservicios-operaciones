package com.jvoq.microservicios.operaciones.app.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.jvoq.microservicios.operaciones.app.services.ClientService;
import com.jvoq.microservicios.operaciones.app.services.CreditService;
import com.jvoq.microservicios.operaciones.app.services.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/credits")
public class CreditController {
	@Autowired
	private CreditService creditService;

	@Autowired
	ClientService clientService;

	@Autowired
	ProductService productService;

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

		/*
		 * return creditService.save(credit) .map(c ->
		 * ResponseEntity.created(URI.create("/credits".concat(c.getIdCredito())))
		 * .contentType(MediaType.APPLICATION_JSON).body(c));
		 */

		// Obtener los creditos del cliente con dicho producto
		List<Credit> credits = new ArrayList<>();
		creditService.findCreditsByIdClientAndIdProduct(credit.getIdCliente(), credit.getIdProducto()).collectList()
				.subscribe(credits::addAll);

		// verificar si cliente existe
		return clientService.findById(credit.getIdCliente()).flatMap(c -> {
			// Obtener detalle del producto solicitado
			return productService.findById(credit.getIdProducto()).flatMap(p -> {
				int creditosCreadosConProducto = credits.size();
				int creditosACrearConProducto = (c.isJuridico()) ? p.getJuridico() : p.getNatural();
				if (creditosCreadosConProducto < creditosACrearConProducto || creditosACrearConProducto == -1) {
					return creditService.save(credit);
				} else {
					return Mono.error(new RuntimeException(
							"Ya registra creditos con dicho producto o simplemente no califica para adquirir el producto"));
				}
			});
		}).map(c -> ResponseEntity.created(URI.create("/credits".concat(c.getIdCredito())))
				.contentType(MediaType.APPLICATION_JSON).body(c)).defaultIfEmpty(ResponseEntity.notFound().build());
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
