package com.jvoq.microservicios.operaciones.app.services;



import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jvoq.microservicios.operaciones.app.dtos.AccountDto;
import com.jvoq.microservicios.operaciones.app.models.documents.Account;
import com.jvoq.microservicios.operaciones.app.models.repository.AccountRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImplement implements AccountService {

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private ModelMapper mapper;

  @Override
  public Flux<AccountDto> findAll() {
    return accountRepository.findAll().map(a -> this.convertEntityToDto(a));
  }

  @Override
  public Mono<AccountDto> findById(String id) {
    return accountRepository.findById(id).map(a -> this.convertEntityToDto(a));
  }

  @Override
  public Mono<AccountDto> save(AccountDto accountDto) {
    Account account =this.convertDtoToEntity(accountDto);
    return accountRepository.save(account).map(a -> this.convertEntityToDto(a));
  }

  @Override
  public Mono<AccountDto> actualize(AccountDto accountDto, String id) {
    return this.findById(id).flatMap(a -> {
      a.setIdProducto(a.getIdProducto());
      a.setIdCliente(a.getIdCliente());
      a.setNumeroCuenta(accountDto.getNumeroCuenta());
      a.setMoneda(accountDto.getMoneda());
      a.setSaldo(accountDto.getSaldo());
      a.setMaxDeposito(accountDto.getMaxDeposito());
      a.setMaxRetiro(accountDto.getMaxRetiro());
      a.setComDeposito(accountDto.getComDeposito());
      a.setComRetiro(accountDto.getComRetiro());
      a.setComMantenimiento(accountDto.getComMantenimiento());
      return this.save(a);

    });
  }

  @Override
  public Mono<Void> delete(Account account) {
    return accountRepository.delete(account);
  }

  @Override
  public Flux<Account> findAccoutsByIdClient(String idClient) {
    return accountRepository.findAccountByIdCliente(idClient);
  }

  @Override
  public Flux<Account> findAccountsByIdClientAndIdProduct(String idClient, String idProduct) {
    return accountRepository.findAccountByIdClienteAndIdProducto(idClient, idProduct);
  }

  private AccountDto convertEntityToDto(Account account) {
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    AccountDto accountDTO = new AccountDto();
    accountDTO = mapper.map(account, AccountDto.class);
    return accountDTO;
  }

  private Account convertDtoToEntity(AccountDto accountDto) {
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    Account account = new Account();
    account = mapper.map(accountDto, Account.class);
    return account;
  }

}
