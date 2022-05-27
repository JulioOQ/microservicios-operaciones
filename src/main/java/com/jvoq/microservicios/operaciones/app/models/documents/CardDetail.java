package com.jvoq.microservicios.operaciones.app.models.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "card_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "idCardetail", "idTarjeta", "idCuenta", "principal" })
public class CardDetail {

	@JsonProperty("id_cardetail")
	@Id
	private String idCardetail;
	@Field("id_tarjeta")
	@JsonProperty("id_tarjeta")
	private String idTarjeta;
	@Field("id_cuenta")
	@JsonProperty("id_cuenta")
	private String idCuenta;
	private Boolean principal;
}
