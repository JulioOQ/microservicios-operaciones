package com.jvoq.microservicios.operaciones.app.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id_tarjeta", "num_tarjeta", "tipo", "fech_expiracion", "fecha_creacion" })
public class CardDto {
	private String id_tarjeta;
	private String num_tarjeta;
	private String tipo;
	private String fech_expiracion;
	private Date fechCreacion;
}
