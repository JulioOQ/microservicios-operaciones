package com.jvoq.microservicios.operaciones.app.controllers;

import java.net.URI;
import java.util.Date;

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

import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;
import com.jvoq.microservicios.operaciones.app.services.AccountService;
import com.jvoq.microservicios.operaciones.app.services.CreditService;
import com.jvoq.microservicios.operaciones.app.services.TransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
//@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	AccountService accountService;

	@Autowired
	private CreditService creditService;

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

	@PostMapping("/deposit")
	public Mono<ResponseEntity<Transaction>> deposit(@RequestBody Transaction transaction) {
		return accountService.findById(transaction.getDestino()).flatMap(a -> {
			if (transaction.getMonto() > 0) {
				a.setSaldo(a.getSaldo() + transaction.getMonto());
				accountService.save(a).subscribe();

				transaction.setFecha(new Date());
				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException("El saldo de la cuenta origen es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/withdraw")
	public Mono<ResponseEntity<Transaction>> withdraw(@RequestBody Transaction transaction) {
		return accountService.findById(transaction.getOrigen()).flatMap(a -> {
			if (a.getSaldo() >= transaction.getMonto() && transaction.getMonto() > 0) {
				a.setSaldo(a.getSaldo() - transaction.getMonto());
				accountService.save(a).subscribe();

				transaction.setFecha(new Date());
				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException("El saldo de la cuenta origen es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/transfer")
	public Mono<ResponseEntity<Transaction>> transfer(@RequestBody Transaction transaction) {
		return accountService.findById(transaction.getOrigen()).flatMap(a1 -> {
			if (a1.getSaldo() >= transaction.getMonto() && transaction.getMonto() > 0) {
				return accountService.findById(transaction.getDestino()).flatMap(a2 -> {

					a1.setSaldo(a1.getSaldo() - transaction.getMonto());
					accountService.save(a1).subscribe();

					a2.setSaldo(a2.getSaldo() + transaction.getMonto());
					accountService.save(a2).subscribe();

					transaction.setFecha(new Date());
					return transactionService.save(transaction);
				});
			} else {
				return Mono.error(new RuntimeException("El saldo de la cuenta origen es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
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

	@PostMapping("/pay")
	public Mono<ResponseEntity<Transaction>> pay(@RequestBody Transaction transaction) {
		return creditService.findById(transaction.getDestino()).flatMap(a -> {
			if (transaction.getMonto() > 0 && (transaction.getMonto() + a.getSaldo()) <= a.getLineaCredito()) {
				a.setSaldo(a.getSaldo() + transaction.getMonto());
				creditService.save(a).subscribe();

				transaction.setFecha(new Date());
				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException(
						"El monto a pagar debe ser mayor a cero y menor o igual a la linea de credito"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/buy")
	public Mono<ResponseEntity<Transaction>> buy(@RequestBody Transaction transaction) {
		return creditService.findById(transaction.getOrigen()).flatMap(a -> {
			if (transaction.getMonto() > 0 && transaction.getMonto() <= a.getSaldo()) {
				a.setSaldo(a.getSaldo() - transaction.getMonto());
				creditService.save(a).subscribe();

				transaction.setFecha(new Date());
				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException("El saldo es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
