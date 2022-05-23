package com.jvoq.microservicios.operaciones.app.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id_cardetail", "id_tarjeta", "id_cuenta", "principal" })
public class CardDetailDto {
	private String idCardetail;
	private String idTarjeta;
	private String idCuenta;
	private Boolean principal;
}
