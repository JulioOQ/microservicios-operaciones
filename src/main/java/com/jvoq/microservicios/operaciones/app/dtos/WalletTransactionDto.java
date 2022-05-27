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
public class WalletTransactionDto {

	@JsonProperty("id_transaction")
	private String idTransaction;
	@JsonProperty("id_wallet")
	private String idWallet;
	private String source;
	private String destination;
	private String subject;
	private String description;
	private String currency;
	private Double amount;
	private Double commission;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
	@JsonProperty("transaction_date")
	private Date transactionDate;
}
