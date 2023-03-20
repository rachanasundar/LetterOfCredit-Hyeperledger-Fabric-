/*
SPDX-License-Identifier: Apache-2.0
*/

package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

public class ClientApp {

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
	}

	public static void main(String[] args) throws Exception {
		// Load a file system based wallet for managing identities.
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		Path networkConfigPath = Paths.get("connection.json");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "appUser1").networkConfig(networkConfigPath).discovery(true);

		// create a gateway connection
		try (Gateway gateway = builder.connect()) {

			// get the network and contract
			Network network = gateway.getNetwork("newtrustchannel");
			Contract contract = network.getContract("letterOfCredit");

			byte[] result;
           // To Generate loc
            //String Locdata = "{\"buyer\":\"hcl\",\"dateOfCreation\":\"19Aug2022\",\"buyerBank\":\"hdfc\",\"seller\":\"lenevo\",\"remark\":\"ok\"}";
//			result=contract.submitTransaction("GenerateLoc", "Bank", "{\"buyer\":\"hcl\",\"dateOfCreation\":\"\",\"buyerBank\":\"hdfc\",\"seller\":\"lenevo\",\"remark\":\"Ok\"}");
//		      System.out.println(new String(result));
		      
//            // To Approve Or Reject Loc
//			result=contract.submitTransaction("ApproveOrRejectLoc", "", "Lenovo", "", "true");
//		      System.out.println("ApproveOrRejectLoc :" + new String(result));
//		      
//            // To Ship Goods By Seller
//			result=  contract.submitTransaction("ShipGoodsBySeller", "", "Lenovo", "", "true");
//		      System.out.println("ShipGoodsBySeller:"+new String(result));
//		      
//            // To Goods Verified By Buyer
//			result= contract.submitTransaction("GoodsVerifiedByBuyer", "", "HCL", "",  "true");
//		      System.out.println("GoodsVerifiedByBuyer: "+new String(result));
//		      
//            // To Verified Shipping Goods By Bank
//			result=contract.submitTransaction("GoodsVerifiedBySeller", "", "Lenovo", "", "true");
//		      System.out.println("GoodsVerifiedBySeller :"+new String(result));
//		      
//            //To Verify Shipping Goods By Bank
//			result=  contract.submitTransaction("VerifiedShippingGoodsByBank", "", "Bank", "", "true");
//		      System.out.println("VerifiedShippingGoodsByBank:"+new String(result));
//		      
//            // To Release Payment
//			result=  contract.submitTransaction("ReleasePayment", "", "Bank", "", "180000");
//		      System.out.println("ReleasePayment: "+new String(result));
//		      
//			//Querying to get loc by ID
////		      "LOC422309318122034029"
////		      "LOC422313319605490541"
//		      //"LOC422344842786394989"
            result = contract.evaluateTransaction("GetLoc", "LOC422344842786394989");
            System.out.println(new String(result));
		}
	}

}
