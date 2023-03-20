package com.org.service;

import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.gateway.ContractException;

import com.org.model.LetterOfCredit;

public interface LocService {
	String GenerateLoc(String transactionInitiatedBy, LetterOfCredit locData) throws Exception;

	String ApproveOrRejectLoC(String transactionInitiatedBy, String letterOfCreditNo,String status)throws Exception;

	String ShipGoodsBySeller(String transactionInitiatedBy, String letterOfCreditNo,String shipmentStatus)throws Exception;

	String GoodsVerifiedBySeller(String transactionInitiatedBy, String letterOfCreditNo, String verificationStatus)throws Exception;

	String GoodsVerifiedByBuyer(String transactionInitiatedBy, String letterOfCreditNo, String verificationStatus)throws Exception;

	String VerifiedShippingGoodsByBank(String transactionInitiatedBy, String letterOfCreditNo,String verificationStatus)throws Exception;

	String ReleasePayment(String transactionInitiatedBy, String letterOfCreditNo, String paymentAmount)throws Exception;

	String GetLoc(String letterOfCreditNo)throws Exception;
//	 LetterOfCredit[] QueryAllLoc();

}
