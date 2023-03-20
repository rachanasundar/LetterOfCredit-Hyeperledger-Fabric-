package com.org.model;

import com.google.auto.value.AutoValue.Builder;

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
@Builder
public class LetterOfCredit {


	//private String  letterOfCreditNo; 
	private String buyer;
	private String dateOfCreation;
	private String buyerBank;
	private String seller;
	private String remarks;
	private String locStatus;
	private String isShipGoodsBySeller;
	private String isGoodsConfirmByBuyer;
	private String isVerifiedSellerDocs;
	private String isVerifiedBuyerDocs;
	private String releasePayment;
	private String paymentAmount;

	private Transaction transaction[];

}
