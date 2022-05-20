package com.jvoq.microservicios.operaciones.app.services;
import com.jvoq.microservicios.operaciones.app.dtos.AccountDto;
import com.jvoq.microservicios.operaciones.app.models.documents.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

	public Flux<AccountDto> findAll();

	public Mono<AccountDto> findById(String id);

	public Mono<AccountDto> save(AccountDto accountDto);

	public Mono<AccountDto> actualize(AccountDto accountDto, String id);
	
	public Mono<Void> delete(Account account);

	public Flux<Account> findAccoutsByIdClient(String idClient);

	public Flux<Account> findAccountsByIdClientAndIdProduct(String idClient, String idProduct);

}
