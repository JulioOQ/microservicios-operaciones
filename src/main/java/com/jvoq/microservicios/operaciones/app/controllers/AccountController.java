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

import com.jvoq.microservicios.operaciones.app.models.documents.Account;
import com.jvoq.microservicios.operaciones.app.services.AccountService;
import com.jvoq.microservicios.operaciones.app.services.ClientService;
import com.jvoq.microservicios.operaciones.app.services.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
@RequestMapping("accounts")
public class AccountController {

	@Autowired
	AccountService accountService;
	
	@Value("${offers.discount:default}")
	private String discount;
	
	@GetMapping("view-discounts")
	public String viewDiscounts() {
		return "Discount operaciones is " + discount;
	}

	@Autowired
	ClientService clientService;

	@Autowired
	ProductService productService;

	@GetMapping
	public Mono<ResponseEntity<Flux<Account>>> getAll() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accountService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Account>> getById(@PathVariable String id) {
		return accountService.findById(id).map(a -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(a))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping("/client/{id}")
	public Mono<ResponseEntity<Flux<Account>>> getAccountByIdClient(@PathVariable String id) {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.body(accountService.findAccoutsByIdClient(id)));
	}

	@PostMapping
	public Mono<ResponseEntity<Account>> create(@RequestBody Account account) {
		// Obtener las cuentas del cliente con dicho producto
		List<Account> accounts = new ArrayList<>();
		accountService.findAccountsByIdClientAndIdProduct(account.getIdCliente(), account.getIdProducto()).collectList()
				.subscribe(accounts::addAll);

		account.setFechaCreacion(new Date());
		// verificar si cliente existe
		return clientService.findById(account.getIdCliente()).flatMap(c -> {
			if (account.getSaldo() >= 0) {
				// Obtener detalle del producto solicitado
				return productService.findById(account.getIdProducto()).flatMap(p -> {
					int cuentasCreadasConProducto = accounts.size();
					int cuentasACrearConProducto = (c.isJuridico()) ? p.getJuridico() : p.getNatural();
					if (cuentasCreadasConProducto < cuentasACrearConProducto || cuentasACrearConProducto == -1) {
						return accountService.save(account);
					} else {
						return Mono.error(new RuntimeException(
								"Ya registra cuentas con dicho producto o simplemente no califica para adquirir el producto"));
					}
				});
			} else {
				return Mono.error(new RuntimeException("La apertura mininima debe ser 0."));
			}
		}).map(a -> ResponseEntity.created(URI.create("/accounts".concat(a.getIdCuenta())))
				.contentType(MediaType.APPLICATION_JSON).body(a)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Account>> update(@RequestBody Account account, @PathVariable String id) {
		return accountService.findById(id).flatMap(a -> {
			a.setIdProducto(account.getIdProducto());
			a.setIdCliente(account.getIdCliente());
			a.setNumeroCuenta(account.getNumeroCuenta());
			a.setMoneda(account.getMoneda());
			a.setSaldo(account.getSaldo());

			return accountService.save(a);
		}).map(a -> ResponseEntity.created(URI.create("/accounts".concat(a.getIdCuenta())))
				.contentType(MediaType.APPLICATION_JSON).body(a)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> drop(@PathVariable String id) {
		return accountService.findById(id).flatMap(a -> {
			return accountService.delete(a).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
}
