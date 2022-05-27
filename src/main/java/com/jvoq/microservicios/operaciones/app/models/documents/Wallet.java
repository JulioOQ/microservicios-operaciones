package com.jvoq.microservicios.operaciones.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

	@Id
	@JsonProperty("id_wallet")
	private String idWallet;
	@Field("id_card")
	@JsonProperty("id_card")
	private String idCard;
	@Field("cellphone_number")
	@JsonProperty("cellphone_number")
	private String cellphoneNumber;
	private String imei;
	private String email;
	private Double balance;
	@Field("fecha_creacion")
	@JsonProperty("fecha_creacion")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
	private Date fechaCreacion;
}
