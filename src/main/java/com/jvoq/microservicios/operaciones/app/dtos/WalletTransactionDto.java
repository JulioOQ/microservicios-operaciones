package com.jvoq.microservicios.operaciones.app.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionDto {

	@JsonProperty("id_transaccion_monedero")
	private String idTransacionMonedero;
	@JsonProperty("id_monedero")
	private String idMonedero;
	
	 @JsonProperty("celular_origen")
	private String celularOrigen;
	 
	 @JsonProperty("celular_destino")
	private String celularDestino;
	 
	private String asunto;
	private String descripcion;
	private String moneda;
	private Double monto;
	private Double comision;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
	@JsonProperty("fecha")
	private Date fecha;
}
