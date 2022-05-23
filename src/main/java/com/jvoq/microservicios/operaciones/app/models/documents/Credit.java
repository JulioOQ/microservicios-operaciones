package com.jvoq.microservicios.operaciones.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "credits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

	@Id
	@JsonProperty("id_credito")
	private String idCredito;

	@Field("id_producto")
	@JsonProperty("id_producto")
	private String idProducto;

	@Field("id_cliente")
	@JsonProperty("id_cliente")
	private String idCliente;

	@Field("numero_credito")
	@JsonProperty("numero_credito")
	private String numeroCredito;
	private String moneda;

	@Field("linea_credito")
	@JsonProperty("linea_credito")
	private Double lineaCredito;
	private Double saldo;
	private Double consumido;
	private Double interes;
	@Field("fecha_pago")
	@JsonProperty("fecha_pago")
	private Date fechaPago;
}
