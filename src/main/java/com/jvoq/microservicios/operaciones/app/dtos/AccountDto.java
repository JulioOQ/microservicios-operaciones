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
@JsonPropertyOrder({ "idCuenta", "numeroCuenta", "saldo", "moneda", "idCliente", "idProducto", "fechaCreacion" })
public class AccountDto {  
  
  @JsonProperty("id_cuenta")
  private String idCuenta;
  
  @JsonProperty("id_producto")
  private String idProducto;
  
  @JsonProperty("id_cliente")
  private String idCliente;
  
  @JsonProperty("numero_cuenta")
  private String numeroCuenta;
  
  private String moneda;
  
  private Double saldo;
  
  @JsonProperty("fecha_creacion")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
  private Date fechaCreacion;
  
  @JsonProperty("max_deposito")
  private int maxDeposito;
  
  @JsonProperty("max_retiro")
  private int maxRetiro;
  
  @JsonProperty("com_deposito")
  private Double comDeposito;
  
  @JsonProperty("com_retiro")
  private Double comRetiro;
  
  @JsonProperty("com_mantenimiento")
  private Double comMantenimiento;

}
