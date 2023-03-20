package com.org.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	private String transactionId;
	private String	transactionType;
	private String	transactionTimestamp;
	private String transactionInitiatedBy;
	private String remarks;
	private String letterOfCreditNo;

}
