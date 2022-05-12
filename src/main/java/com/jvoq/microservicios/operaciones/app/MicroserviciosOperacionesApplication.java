package com.jvoq.microservicios.operaciones.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableReactiveFeignClients
public class MicroserviciosOperacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosOperacionesApplication.class, args);
	}

}
