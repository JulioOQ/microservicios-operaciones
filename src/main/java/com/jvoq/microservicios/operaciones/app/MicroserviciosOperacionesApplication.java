package com.jvoq.microservicios.operaciones.app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableReactiveFeignClients
public class MicroserviciosOperacionesApplication {
  
  @Bean
  public ModelMapper modelMapper(){
    return new ModelMapper();
  }

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosOperacionesApplication.class, args);
	}

}
