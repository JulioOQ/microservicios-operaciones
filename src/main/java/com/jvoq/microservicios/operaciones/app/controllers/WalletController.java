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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@RefreshScope
@RestController
@RequestMapping("/wallets")
public class WalletController {
  
  @Autowired
	private  WalletService walletService;
  @Autowired
	private  WalletTransactionService transactionService;
  @Autowired
  private ModelMapper mapper;





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
				.map(w -> ResponseEntity.created(URI.create("/wallets".concat(w.getIdMonedero())))
						.contentType(MediaType.APPLICATION_JSON).body(w));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<WalletDto>> update(@RequestBody WalletDto walletDto, @PathVariable String id) {
		return walletService.update(walletDto, id)
				.map(w -> ResponseEntity.created(URI.create("/wallets".concat(w.getIdMonedero())))
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

	@PostMapping("/send-money")
	public Mono<ResponseEntity<WalletTransactionDto>> sendMoney(@RequestBody WalletTransactionDto transaction) {
	  
	  return walletService.findByCellphoneNumber(transaction.getCelularOrigen()).flatMap(w1 -> {
			if (transaction.getMonto() > 0 && w1.getSaldo() >= transaction.getMonto()) {
			  System.out.println("entro if 1");
				return walletService.findByCellphoneNumber(transaction.getCelularDestino()).flatMap(w2 -> {
				  
				
          w1.setSaldo(w1.getSaldo() - transaction.getMonto());
          walletService.save(w1).subscribe(); 
          
          w2.setSaldo(w2.getSaldo() + transaction.getMonto());
          walletService.save(w2).subscribe();
          
         
					transaction.setIdMonedero(w1.getIdMonedero());
					return transactionService.save(transaction);
				});
			} else {
				return Mono.error(new RuntimeException("El saldo del monedero es insuficiente"));
			}
		}).map(t -> ResponseEntity.created(URI.create("/send".concat(t.getIdTransacionMonedero())))
				.contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping("/receive")
	public Mono<ResponseEntity<WalletTransactionDto>> receiveMoney(@RequestBody WalletTransactionDto transaction) {
	  
	  return walletService.findByCellphoneNumber(transaction.getCelularOrigen()).flatMap(w1 -> {
      if (transaction.getMonto() > 0 && w1.getSaldo() >= transaction.getMonto()) {
        System.out.println("entro if 1");
        return walletService.findByCellphoneNumber(transaction.getCelularDestino()).flatMap(w2 -> {
                          
          System.out.println("entro if 3");
          w2.setSaldo(w2.getSaldo() + transaction.getMonto());
          walletService.save(w2).subscribe();
          


          transaction.setIdMonedero(w1.getIdMonedero());
          return transactionService.save(transaction);
        });
      } else {
        return Mono.error(new RuntimeException("El saldo del monedero es insuficiente"));
      }
    }).map(t -> ResponseEntity.created(URI.create("/send".concat(t.getIdTransacionMonedero())))
        .contentType(MediaType.APPLICATION_JSON).body(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
