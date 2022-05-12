package com.jvoq.microservicios.operaciones.app.models.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	private String id_producto;
	private String id_banco;
	private String tipo_producto;
	private String nombre;
	private String descripcion;
}
