package com.jvoq.microservicios.operaciones.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "wallet_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction {

	@Id
	@JsonProperty("id_transaction_monedero")
	private String idTransacionMonedero;
	
	@JsonProperty("id_monedero")
	@Field("id_monedero")
	private String idMonedero;
	
	@JsonProperty("celular_origen")
  @Field("celular_origen")
	private String celularOrigen;
	@JsonProperty("celular_destino")
  @Field("celular_destino")
	private String celularDestino;
	private String asunto;
	private String descripcion;
	private String moneda;
	private Double monto;
	private Double comision;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
	@JsonProperty("fecha")
	@Field("fecha")
	private Date fecha;
}
