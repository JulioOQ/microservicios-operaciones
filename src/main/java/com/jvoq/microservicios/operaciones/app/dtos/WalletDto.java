package com.jvoq.microservicios.operaciones.app.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "idMonedero", "numCelular", "imei", "correo", "balance", "idTarjeta","fechaCreacion"})
public class WalletDto {

	@JsonProperty("id_monedero")
	private String idMonedero;
	@JsonProperty("id_tarjeta")
	private String idTarjeta;
	@JsonProperty("numero_celular")
	private String numCelular;
	private String imei;
	private String correo;
	private Double saldo;
	@JsonProperty("fecha_creacion")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
	private Date fechaCreacion;
}
