package com.jvoq.microservicios.operaciones.app.models.documents;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

	private String id_cliente;
	private String tipo_documento;
	private String numero_documento;
	private String nombres;
	private String correo;
	private String direccion;
	private String telefono;
	private String tipo_cliente;
	private List<Client> representantes;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
	private Date fecha_creacion;
}