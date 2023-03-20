package com.org.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;

import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.stereotype.Service;

import com.org.model.LetterOfCredit;

@Service
public class LocServiceImpl implements LocService {
	byte[] result;

	private static Contract Connection() throws Exception {
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		Path networkConfigPath = Paths.get("connection.json");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "appUser1").networkConfig(networkConfigPath).discovery(true);
		try (Gateway gateway = builder.connect()) {

			// get the network and contract
			Network network = gateway.getNetwork("newtrustchannel");
			Contract contract = network.getContract("letterOfCredit");
			return contract;
		}

	}

	@Override
	public String GenerateLoc(String transactionInitiatedBy, LetterOfCredit locData) throws Exception {
		result = LocServiceImpl.Connection().submitTransaction("GenerateLoc", transactionInitiatedBy,
				locData.toString());
		return new String(result);
	}

	@Override
	public String ApproveOrRejectLoC(String transactionInitiatedBy, String letterOfCreditNo, String status)
			throws Exception {
		result = LocServiceImpl.Connection().submitTransaction("ApproveOrRejectLoC", transactionInitiatedBy,
				letterOfCreditNo, status);
		return new String(result);
	}

	@Override
	public String ShipGoodsBySeller(String transactionInitiatedBy, String letterOfCreditNo,String shipmentStatus)
			throws Exception {
		result = LocServiceImpl.Connection().submitTransaction("ShipGoodsBySeller", transactionInitiatedBy,
				letterOfCreditNo,shipmentStatus);
		return new String(result);
	}

	@Override
	public String GoodsVerifiedBySeller(String transactionInitiatedBy, String letterOfCreditNo,
			String verificationStatus) throws Exception {
		result = LocServiceImpl.Connection().submitTransaction("GoodsVerifiedBySeller", transactionInitiatedBy,
				letterOfCreditNo, verificationStatus);
		return new String(result);
	}

	@Override
	public String GoodsVerifiedByBuyer(String transactionInitiatedBy, String letterOfCreditNo,
			String verificationStatus) throws Exception {
		result = LocServiceImpl.Connection().submitTransaction("GoodsVerifiedByBuyer", transactionInitiatedBy,
				letterOfCreditNo,verificationStatus);
		return new String(result);
	}

	@Override
	public String VerifiedShippingGoodsByBank(String transactionInitiatedBy, String letterOfCreditNo,
			String verificationStatus) throws Exception {
		result = LocServiceImpl.Connection().submitTransaction("VerifiedShippingGoodsByBank", transactionInitiatedBy,
				letterOfCreditNo,verificationStatus );
		return new String(result);
	}

	@Override
	public String ReleasePayment(String transactionInitiatedBy, String letterOfCreditNo, String paymentAmount)
			throws Exception {
		result = LocServiceImpl.Connection().submitTransaction("ReleasePayment", transactionInitiatedBy,
				letterOfCreditNo, paymentAmount);
		return new String(result);
	}

	@Override
	public String GetLoc(String letterOfCreditNo) throws Exception {
		result = LocServiceImpl.Connection().submitTransaction("GetLoc", letterOfCreditNo);
		return new String(result);
	}

}
