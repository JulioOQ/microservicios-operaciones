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
	private int juridico;
	private int natural;
	private int max_deposito;
	private int max_retiro;
	private Double com_deposito;
	private Double com_retiro;
	private Double com_mantenimiento;
}
