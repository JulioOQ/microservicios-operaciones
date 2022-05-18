package com.jvoq.microservicios.operaciones.app.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;
import com.jvoq.microservicios.operaciones.app.services.AccountService;
import com.jvoq.microservicios.operaciones.app.services.ClientService;
import com.jvoq.microservicios.operaciones.app.services.ProductService;
import com.jvoq.microservicios.operaciones.app.services.TransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
@RequestMapping("accounts")
public class AccountController {

	@Autowired
	AccountService accountService;

	@Value("${mensaje.verificacion:default}")
	private String mensaje;

	@GetMapping("verificar")
	public String viewDiscounts() {
		return "Mensaje -> " + mensaje;
	}

	@Autowired
	ClientService clientService;

	@Autowired
	ProductService productService;

	@Autowired
	private TransactionService transactionService;

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
						account.setMaxDeposito(p.getMax_deposito());
						account.setMaxRetiro(p.getMax_retiro());
						account.setComDeposito(p.getCom_deposito());
						account.setComRetiro(p.getCom_retiro());
						account.setComMantenimiento(p.getCom_mantenimiento());

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
			a.setIdProducto(a.getIdProducto());
			a.setIdCliente(a.getIdCliente());
			a.setNumeroCuenta(account.getNumeroCuenta());
			a.setMoneda(account.getMoneda());
			a.setSaldo(account.getSaldo());
			a.setMaxDeposito(account.getMaxDeposito());
			a.setMaxRetiro(account.getMaxRetiro());
			a.setComDeposito(account.getComDeposito());
			a.setComRetiro(account.getComRetiro());
			a.setComMantenimiento(account.getComMantenimiento());

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

	@PostMapping("/deposit")
	public Mono<ResponseEntity<Transaction>> deposit(@RequestBody Transaction transaction) throws InterruptedException {
		// Obtener los depositos del *presente mes y año
		List<Transaction> deposits = new ArrayList<>();
		transactionService.getTransactionsByCuentaAndTipoTransaccion(transaction.getDestino(), "Deposito").collectList()
				.subscribe(deposits::addAll);
		TimeUnit.SECONDS.sleep(2L);
		return accountService.findById(transaction.getDestino()).flatMap(a -> {
			if (transaction.getMonto() > 0) {
				Double comision = (deposits.size() >= a.getMaxDeposito()) ? a.getComDeposito() : 0;
				a.setSaldo(a.getSaldo() + transaction.getMonto() - comision);
				accountService.save(a).subscribe();

				transaction.setFecha(new Date());
				transaction.setComision(comision);
				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException("El saldo de la cuenta origen es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/withdraw")
	public Mono<ResponseEntity<Transaction>> withdraw(@RequestBody Transaction transaction) throws InterruptedException {
		// Obtener los retiros del *presente mes y año
		List<Transaction> withdraws = new ArrayList<>();
		transactionService.getTransactionsByCuentaAndTipoTransaccion(transaction.getOrigen(), "Retiro").collectList()
				.subscribe(withdraws::addAll);
		TimeUnit.SECONDS.sleep(2L);
		return accountService.findById(transaction.getOrigen()).flatMap(a -> {
			Double comision = (withdraws.size() >= a.getMaxRetiro()) ? a.getComRetiro() : 0;
			if (a.getSaldo() >= (transaction.getMonto() + comision) && transaction.getMonto() > 0) {
				a.setSaldo(a.getSaldo() - transaction.getMonto() - comision);
				accountService.save(a).subscribe();

				transaction.setFecha(new Date());
				transaction.setComision(comision);
				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException("El saldo de la cuenta origen es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/buy")
	public Mono<ResponseEntity<Transaction>> buy(@RequestBody Transaction transaction) {
		return accountService.findById(transaction.getOrigen()).flatMap(a -> {
			if (transaction.getMonto() > 0 && transaction.getMonto() <= a.getSaldo()) {
				a.setSaldo(a.getSaldo() - transaction.getMonto());
				accountService.save(a).subscribe();

				transaction.setFecha(new Date());
				return transactionService.save(transaction);
			} else {
				return Mono.error(new RuntimeException("El saldo es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/transactions".concat(t.getIdTransaccion())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
