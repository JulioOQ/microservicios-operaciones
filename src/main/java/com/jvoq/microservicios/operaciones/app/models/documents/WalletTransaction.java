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

@Document(collection = "wallet_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction {

	@Id
	@JsonProperty("id_transaction")
	private String idTransaction;
	@JsonProperty("id_wallet")
	@Field("id_wallet")
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
	@Field("transaction_date")
	private Date transactionDate;
}
