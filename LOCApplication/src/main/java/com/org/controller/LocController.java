package com.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.org.model.LetterOfCredit;
import com.org.model.RequestBodyData;
import com.org.service.LocServiceImpl;

@RestController
public class LocController {

	@Autowired
	LocServiceImpl locService;

	@PostMapping(value = "generateLoc")
	public String GenerateLoc(@RequestBody LetterOfCredit locData, @PathVariable String transactionInitiatedBy)
			throws Exception {
		return locService.GenerateLoc(transactionInitiatedBy, locData);

	}
	@PostMapping(value = "approveOrRejectLoC")
	public String ApproveOrRejectLoC(@RequestBody RequestBodyData data) throws Exception {
		return locService.ApproveOrRejectLoC(data.getTransactionInitiatedBy(), data.getLetterOfCreditNo(), data.getInput());
	}
	
	@PostMapping(value = "shipGoodsBySeller")
	public String  ShipGoodsBySeller(@RequestBody RequestBodyData data) throws Exception {
		return locService. ShipGoodsBySeller(data.getTransactionInitiatedBy(), data.getLetterOfCreditNo(), data.getInput());
	}
	@PostMapping(value = "goodsVerifiedBySeller")
	public String  GoodsVerifiedBySeller(@RequestBody RequestBodyData data) throws Exception {
		return locService. GoodsVerifiedBySeller(data.getTransactionInitiatedBy(), data.getLetterOfCreditNo(), data.getInput());
	}
	@PostMapping(value = "goodsVerifiedByBuyer")
	public String  GoodsVerifiedByBuyer(@RequestBody RequestBodyData data) throws Exception {
		return locService. GoodsVerifiedByBuyer(data.getTransactionInitiatedBy(), data.getLetterOfCreditNo(), data.getInput());
	}@PostMapping(value = "verifiedShippingGoodsByBank")
	public String  VerifiedShippingGoodsByBank(@RequestBody RequestBodyData data) throws Exception {
		return locService. VerifiedShippingGoodsByBank(data.getTransactionInitiatedBy(), data.getLetterOfCreditNo(), data.getInput());
	}
	@PostMapping(value = "releasePayment")
	public String  ReleasePayment(@RequestBody RequestBodyData data) throws Exception {
		return locService. ReleasePayment(data.getTransactionInitiatedBy(), data.getLetterOfCreditNo(), data.getInput());
	}
	@GetMapping(value = "getLoc")
	public String  GetLoc(@RequestBody String data) throws Exception {
		return locService. GetLoc(data);
	}


}
