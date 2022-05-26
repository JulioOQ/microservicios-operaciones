package com.jvoq.microservicios.operaciones.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "idTarjeta", "numTarjeta", "tipo", "fechExpiracion", "fechCreacion" })
public class Card {
	
	@JsonProperty("id_tarjeta")
	@Id
	private String idTarjeta;
	@Field("numero_tarjeta")
	@JsonProperty("numero_tarjeta")
	private String numTarjeta;
	private String tipo;
	@Field("fecha_expiracion")
	@JsonProperty("fecha_expiracion")
	private String fechaExpiracion;
	@Field("fecha_creacion")
	@JsonProperty("fecha_creacion")
	private Date fechaCreacion;
}
