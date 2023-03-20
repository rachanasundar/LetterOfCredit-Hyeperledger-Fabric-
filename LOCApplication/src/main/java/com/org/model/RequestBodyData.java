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
public class RequestBodyData {
	private String transactionInitiatedBy;
	private String letterOfCreditNo;
	private String input;
	public RequestBodyData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RequestBodyData(String transactionInitiatedBy, String letterOfCreditNo, String input) {
		super();
		this.transactionInitiatedBy = transactionInitiatedBy;
		this.letterOfCreditNo = letterOfCreditNo;
		this.input = input;
	}
	public String getTransactionInitiatedBy() {
		return transactionInitiatedBy;
	}
	public void setTransactionInitiatedBy(String transactionInitiatedBy) {
		this.transactionInitiatedBy = transactionInitiatedBy;
	}
	public String getLetterOfCreditNo() {
		return letterOfCreditNo;
	}
	public void setLetterOfCreditNo(String letterOfCreditNo) {
		this.letterOfCreditNo = letterOfCreditNo;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	@Override
	public String toString() {
		return "RequestBodyData [transactionInitiatedBy=" + transactionInitiatedBy + ", letterOfCreditNo="
				+ letterOfCreditNo + ", input=" + input + "]";
	}
	
}
