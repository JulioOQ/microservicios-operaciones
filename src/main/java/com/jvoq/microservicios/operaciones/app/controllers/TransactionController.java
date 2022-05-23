package com.jvoq.microservicios.operaciones.app.controllers;

import java.net.URI;
import java.util.Date;
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
import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;
import com.jvoq.microservicios.operaciones.app.services.TransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Value("${mensaje.verificacion:default}")
	private String mensaje;

	@GetMapping("verificar")
	public String viewDiscounts() {
		return "Mensaje -> " + mensaje;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Transaction>>> getAll() {
		return Mono
				.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(transactionService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Transaction>> getById(@PathVariable String id) {
		return transactionService.findById(id)
				.map(t -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(t))
				.defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@PostMapping
	public Mono<ResponseEntity<Transaction>> create(@RequestBody Transaction transaction) {
		transaction.setFecha(new Date());
		return transactionService.save(transaction)
				.map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
						.contentType(MediaType.APPLICATION_JSON).body(t));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Transaction>> update(@RequestBody Transaction transaction, @PathVariable String id) {
		return transactionService.findById(id).flatMap(t -> {
			t.setOrigen(transaction.getOrigen());
			t.setDestino(transaction.getDestino());
			t.setTipoTransaccion(transaction.getTipoTransaccion());
			t.setDescripcion(transaction.getDescripcion());
			t.setMoneda(transaction.getMoneda());
			t.setMonto(transaction.getMonto());
			t.setComision(transaction.getComision());
			t.setFecha(transaction.getFecha());

			return transactionService.save(t);
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> drop(@PathVariable String id) {
		return transactionService.findById(id).flatMap(t -> {
			return transactionService.delete(t).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	
	  @GetMapping("/client/{idCliente}/product/{idProducto}")
	  public Mono<ResponseEntity<Flux<Transaction>>> getMovementsByClientAndProduct(@PathVariable String idCliente,@PathVariable String idProducto) {
	    return Mono
	        .just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(transactionService.getMovementsByClienteAndProducto(idCliente, idProducto)));
	  }
	 
}
