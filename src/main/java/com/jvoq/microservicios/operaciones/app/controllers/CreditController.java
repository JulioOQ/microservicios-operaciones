package com.jvoq.microservicios.operaciones.app.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;
import com.jvoq.microservicios.operaciones.app.services.ClientService;
import com.jvoq.microservicios.operaciones.app.services.CreditService;
import com.jvoq.microservicios.operaciones.app.services.ProductService;
import com.jvoq.microservicios.operaciones.app.services.TransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
@RequestMapping("/credits")
public class CreditController {
	
	@Autowired
	private CreditService creditService;

	@Value("${mensaje.verificacion:default}")
	private String mensaje;

	@GetMapping("verificar")
	public String viewDiscounts() {
		return "Mensaje -> " + mensaje;
	}

	@Autowired
	private TransactionService transactionService;

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

	@GetMapping("/client/{id}")
	public Mono<ResponseEntity<Flux<Credit>>> getCreditsByIdClient(@PathVariable String id) {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(creditService.findCreditByIdClient(id)));
	}

	@PostMapping
	public Mono<ResponseEntity<Credit>> create(@RequestBody Credit credit) {

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

	@PostMapping("/withdraw")
	public Mono<ResponseEntity<Transaction>> withdraw(@RequestBody Transaction transaction) {
		return creditService.findById(transaction.getOrigen()).flatMap(a -> {
			if (a.getSaldo() >= transaction.getMonto() && transaction.getMonto() > 0) {
				a.setSaldo(a.getSaldo() - transaction.getMonto());
				creditService.save(a).subscribe();

				transaction.setFecha(new Date());
				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException("El saldo de la cuenta origen es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/pay")
	public Mono<ResponseEntity<Transaction>> pay(@RequestBody Transaction transaction) {
		return creditService.findById(transaction.getDestino()).flatMap(a -> {
			if (transaction.getMonto() > 0 && (transaction.getMonto() + a.getSaldo()) <= a.getLineaCredito()) {
				a.setSaldo(a.getSaldo() + transaction.getMonto());

				transaction.setIdCliente(a.getIdCliente());
				transaction.setIdProducto(a.getIdProducto());

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

				transaction.setIdCliente(a.getIdCliente());
				transaction.setIdProducto(a.getIdProducto());

				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException("El saldo es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
