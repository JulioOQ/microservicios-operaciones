package com.jvoq.microservicios.operaciones.app.controllers;

import com.jvoq.microservicios.operaciones.app.dtos.WalletDto;
import com.jvoq.microservicios.operaciones.app.dtos.WalletTransactionDto;
import com.jvoq.microservicios.operaciones.app.models.documents.Transaction;
import com.jvoq.microservicios.operaciones.app.models.documents.Wallet;
import com.jvoq.microservicios.operaciones.app.services.AccountService;
import com.jvoq.microservicios.operaciones.app.services.CardDetailService;
import com.jvoq.microservicios.operaciones.app.services.WalletService;
import com.jvoq.microservicios.operaciones.app.services.WalletTransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@RefreshScope
@RestController
@RequestMapping("/wallets")
public class WalletController {

	private final WalletService walletService;

	private final WalletTransactionService transactionService;

	private final CardDetailService cardDetailService;

	private final AccountService accountService;

	private final ModelMapper mapper;

	public WalletController(WalletService walletService, WalletTransactionService transactionService,
			CardDetailService cardDetailService, AccountService accountService, ModelMapper mapper) {
		this.walletService = walletService;
		this.transactionService = transactionService;
		this.cardDetailService = cardDetailService;
		this.accountService = accountService;
		this.mapper = mapper;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<WalletDto>>> getAll() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(walletService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<WalletDto>> getById(@PathVariable String id) {
		return walletService.findById(id).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<WalletDto>> create(@RequestBody WalletDto walletDto) {
		return walletService.save(walletDto)
				.map(w -> ResponseEntity.created(URI.create("/wallets".concat(w.getIdWallet())))
						.contentType(MediaType.APPLICATION_JSON).body(w));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<WalletDto>> update(@RequestBody WalletDto walletDto, @PathVariable String id) {
		return walletService.update(walletDto, id)
				.map(w -> ResponseEntity.created(URI.create("/wallets".concat(w.getIdWallet())))
						.contentType(MediaType.APPLICATION_JSON).body(w))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> drop(@PathVariable String id) {
		return walletService.findById(id).flatMap(c -> {
			Wallet wallet = mapper.map(c, Wallet.class);
			return walletService.delete(wallet).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping("/send")
	public Mono<ResponseEntity<WalletTransactionDto>> sendMoney(@RequestBody WalletTransactionDto transaction) {
		return walletService.findByCellphoneNumber(transaction.getSource()).flatMap(w1 -> {
			if (transaction.getAmount() > 0 && w1.getBalance() >= transaction.getAmount()) {
				return walletService.findByCellphoneNumber(transaction.getDestination()).flatMap(w2 -> {
					// verificar si esta asociado a una tarjeta
					if (w1.getIdCard() == null) {
						// descontar de monedero
						w1.setBalance(w1.getBalance() - transaction.getAmount());
						walletService.save(w1).subscribe();
					} else {
						// descontar de cuenta asociada a tarjeta
						cardDetailService.findAccountByIdTarjeta(w1.getIdCard())
								.flatMap(t -> accountService.findById(t.getIdCuenta())).mapNotNull(a -> {
									a.setSaldo(a.getSaldo() - transaction.getAmount());
									return accountService.save(a).subscribe();
								});
						try {
							TimeUnit.SECONDS.sleep(2L);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					// verificar si esta asociado a una tarjeta
					if (w2.getIdCard() == null) {
						// Aumentar el saldo al monedero
						w2.setBalance(w2.getBalance() + transaction.getAmount());
						walletService.save(w2).subscribe();
					} else {
						// Aumentar el saldo a la cuenta asociada a tarjeta
						cardDetailService.findAccountByIdTarjeta(w2.getIdCard())
								.flatMap(t -> accountService.findById(t.getIdCuenta())).mapNotNull(a -> {
									a.setSaldo(a.getSaldo() + transaction.getAmount());
									return accountService.save(a).subscribe();
								});
						try {
							TimeUnit.SECONDS.sleep(2L);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					transaction.setIdWallet(w1.getIdWallet());
					return transactionService.save(transaction);
				});
			} else {
				return Mono.error(new RuntimeException("El saldo del monedero es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/wallets/transactions/send".concat(t.getIdTransaction())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/receive")
	public Mono<ResponseEntity<Transaction>> receiveMoney(@RequestBody Transaction transaction) {
		return null;
	}

}
