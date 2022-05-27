package com.jvoq.microservicios.operaciones.app.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {

	@JsonProperty("id_wallet")
	private String idWallet;
	@JsonProperty("id_card")
	private String idCard;
	@JsonProperty("cellphone_number")
	private String cellphoneNumber;
	private String imei;
	private String email;
	private Double balance;
	@JsonProperty("fecha_creacion")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
	private Date fechaCreacion;
}
