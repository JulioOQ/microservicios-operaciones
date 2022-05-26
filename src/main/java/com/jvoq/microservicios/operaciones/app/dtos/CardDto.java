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
@JsonPropertyOrder({ "idTarjeta", "numTarjeta", "tipo", "fechaExpiracion", "fechCreacion" })
public class CardDto {
  
  @JsonProperty("id_tarjeta")
	private String idTarjeta;
	@JsonProperty("numero_tarjeta")
	private String numTarjeta;
	private String tipo;	
  @JsonProperty("fecha_expiracion")
	private String fechaExpiracion;	
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
  @JsonProperty("fecha_creacion")
	private Date fechaCreacion;
}
