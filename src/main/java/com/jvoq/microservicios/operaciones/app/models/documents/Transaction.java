package com.jvoq.microservicios.operaciones.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "idTransaccion", "tipoTransaccion", "origen", "destino", "monto", "moneda", "comision","descripcion","fecha"})
public class Transaction {

	@JsonProperty("id_transaccion")
	@Id
	private String idTransaccion;
	private String origen;
	private String destino;
	@JsonProperty("tipo_transaccion")
	private String tipoTransaccion;
	private String descripcion;
	private String moneda;
	private Double monto;
	private Double comision;
	private Date fecha;
}
