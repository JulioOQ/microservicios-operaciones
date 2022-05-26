package com.jvoq.microservicios.operaciones.app.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.jvoq.microservicios.operaciones.app.models.documents.Credit;
import reactor.core.publisher.Flux;

@Repository
public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {

	Flux<Credit> findCreditsByIdClienteAndIdProducto(String idClient, String idProduct);

	Flux<Credit> findCreditsByIdCliente(String idClient);

}
