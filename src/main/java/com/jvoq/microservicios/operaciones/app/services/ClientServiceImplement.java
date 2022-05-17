package com.jvoq.microservicios.operaciones.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvoq.microservicios.operaciones.app.clients.UserClientFeign;
import com.jvoq.microservicios.operaciones.app.models.documents.Client;

import reactor.core.publisher.Mono;

@Service
public class ClientServiceImplement implements ClientService {

	@Autowired
	UserClientFeign userClientFeign;

	@Override
	public Mono<Client> findById(String id) {
		return userClientFeign.findById(id);
	}
}
