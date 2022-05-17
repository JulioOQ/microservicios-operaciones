package com.jvoq.microservicios.operaciones.app.services;


import com.jvoq.microservicios.operaciones.app.models.documents.Client;

import reactor.core.publisher.Mono;

public interface ClientService {
	
	public Mono<Client> findById(String id);

}
