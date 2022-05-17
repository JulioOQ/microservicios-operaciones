package com.jvoq.microservicios.operaciones.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jvoq.microservicios.operaciones.app.clients.ProductClientFeign;
import com.jvoq.microservicios.operaciones.app.models.documents.Product;

import reactor.core.publisher.Mono;

@Service
public class ProductServiceImplement implements ProductService {

	@Autowired
	ProductClientFeign productClientFeign;

	@Override
	public Mono<Product> findById(String id) {
		return productClientFeign.findProductById(id);
	}
}
