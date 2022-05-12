package com.jvoq.microservicios.operaciones.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "idCuenta", "numeroCuenta", "saldo", "moneda","idCliente","idProducto","fechaCreacion"})
public class Account {

	@Id
	@JsonProperty("id_cuenta")
	private String idCuenta;
	
	@Field("id_producto")
	@JsonProperty("id_producto")
	private String idProducto;
	
	@Field("id_cliente")
	@JsonProperty("id_cliente")
	private String idCliente;
	
	@Field("numero_cuenta")
	@JsonProperty("numero_cuenta")
	private String numeroCuenta;
	private String moneda;
	private Double saldo;
	
	@Field("fecha_creacion")
	@JsonProperty("fecha_creacion")
	private Date fechaCreacion;
}