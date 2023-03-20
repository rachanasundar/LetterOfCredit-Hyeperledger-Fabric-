package main

import (
	"encoding/json"
	"fmt"
	"strconv"
	"time"

	"github.com/hyperledger/fabric-contract-api-go/contractapi"
	"github.com/sony/sonyflake"
)

type LetterOfCreditCC struct {
	contractapi.Contract
}

type LetterOfCredit struct {
	Buyer                         string        `json:"buyer"`
	DateOfCreation                string        `json:"dateOfCreation"`
	BuyerBank                     string        `json:"buyerBank"`
	Seller                        string        `json:"seller"`
	Remarks                       string        `json:"remark"`
	LocStatus                     string        `json:"locStatus"`
	IsShipGoodsBySeller           string        `json:"isShipGoodsBySeller"`
	IsVerifiedSellerDocs          string        `json:"isVerifiedSellerDocs"`
	IsVerifiedBuyerDocs           string        `json:"isVerifiedBuyerDocs"`
	IsVerifiedShippingGoodsByBank string        `json:"isVerifiedShippingGoodsByBank"`
	ReleasePayment                string        `json:"releasePayment"`
	PaymentAmount                 string        `json:"paymentAmount"`
	Transaction                   []Transaction `json:"transaction"`
}

type Transaction struct {
	TransactionId          string `json:"transactionId"`
	TransactionType        string `json:"transactionType"`
	TimeStamp              string `json:"timeStamp"`
	TransactionInitiatedBy string `json:"transactionInitiatedBy"`
	Remarks                string `json:"remarks"`
	LetterOfCreditNo       string `json:"letterOfCreditNo"`
}
type QueryResult struct {
	LetterOfCreditNo string `json:"letterOfCreditNo"`
	Record           *LetterOfCredit
}

func (t *LetterOfCreditCC) InitLedger(ctx contractapi.TransactionContextInterface) string {
	fmt.Println("LetterOfCredit Init")
	fmt.Println("************************")
	return "loc init successfully!!!!"

}

//function to create LOC by buyer bank
func (t *LetterOfCreditCC) GenerateLoc(ctx contractapi.TransactionContextInterface, transactionInitiatedBy string, locData string) (string, error) {
	flake := sonyflake.NewSonyflake(sonyflake.Settings{})
label:
	id, err := flake.NextID()
	letterOfCreditNo := "LOC" + strconv.FormatUint(id, 10)
	if err != nil {
		goto label
	}

	isExist, err := ctx.GetStub().GetState(letterOfCreditNo)
	if isExist == nil {

		var loc LetterOfCredit
		json.Unmarshal([]byte(locData), &loc)
		//setting default value as NO
		loc.DateOfCreation = time.Now().String()
		loc.LocStatus = "No"
		loc.IsShipGoodsBySeller = "No"
		loc.IsVerifiedBuyerDocs = "No"
		loc.IsVerifiedSellerDocs = "No"
		loc.IsVerifiedShippingGoodsByBank = "No"
		loc.ReleasePayment = "No"
		loc.PaymentAmount = "0"

		//setting transaction values
		transaction := setTransaction("GenerateLoc", transactionInitiatedBy, letterOfCreditNo)
		transaction.Remarks = "LOC Generated "
		loc.Transaction = append(loc.Transaction, transaction)

		locJSONasBytes, err := json.Marshal(loc)

		if err != nil {
			return "", err
		}

		err = ctx.GetStub().PutState(letterOfCreditNo, locJSONasBytes)

		if err != nil {
			return "", err
		}
		return "Letter Of Credit added Successfully.  LetterOfCreditNo: " + letterOfCreditNo, nil
	} else {
		goto label
	}
}

//to get a LoC
func (t *LetterOfCreditCC) GetLoc(ctx contractapi.TransactionContextInterface, letterOfCreditNo string) (*LetterOfCredit, error) {

	locAsByte, err := ctx.GetStub().GetState(letterOfCreditNo)
	if err != nil {
		return nil, fmt.Errorf("Failed to get loc: %s with error: %s", letterOfCreditNo, err)
	}
	if locAsByte == nil {
		return nil, fmt.Errorf("loc not found: %s", letterOfCreditNo)
	}

	loc := new(LetterOfCredit)
	_ = json.Unmarshal(locAsByte, &loc)

	return loc, nil

}

//get all records of LoC
func (t *LetterOfCreditCC) QueryAllLoc(ctx contractapi.TransactionContextInterface) ([]QueryResult, error) {
	startKey := ""
	endKey := ""

	resultsIterator, err := ctx.GetStub().GetStateByRange(startKey, endKey)

	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	results := []QueryResult{}

	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()

		if err != nil {
			return nil, err
		}

		loc := new(LetterOfCredit)
		_ = json.Unmarshal(queryResponse.Value, loc)

		queryResult := QueryResult{LetterOfCreditNo: queryResponse.Key, Record: loc}
		results = append(results, queryResult)
	}

	return results, nil
}

//to approve or reject the LoC
func (t *LetterOfCreditCC) ApproveOrRejectLoc(ctx contractapi.TransactionContextInterface, transactionInitiatedBy string, letterOfCreditNo string, status string) (string, error) {

	loc, err := t.GetLoc(ctx, letterOfCreditNo)
	var flag bool
	if err != nil {
		return "", err
	}

	//set transcation value
	transaction := setTransaction("ApproveOrRejectLoc", transactionInitiatedBy, letterOfCreditNo)

	if status == "true" {
		loc.LocStatus = "Accepted"
		transaction.Remarks = "Accepted the Letter of Credit"
		flag = true
	} else if status == "false" {
		loc.LocStatus = "Rejected"
		transaction.Remarks = "Rejected the Letter of Credit"
		flag = true
	} else {
		transaction.Remarks = "Invaild action"
		flag = false
		// return "Invaild action ", nil
	}
	loc.Transaction = append(loc.Transaction, transaction)
	locAsBytes, _ := json.Marshal(loc)

	err1 := ctx.GetStub().PutState(letterOfCreditNo, locAsBytes)
	if err1 != nil {
		return "", err1
	}

	if flag == true {
		return "Action completed Successfully", nil
	} else {
		return "Action Failed", nil
	}
}

func (t *LetterOfCreditCC) ShipGoodsBySeller(ctx contractapi.TransactionContextInterface, transactionInitiatedBy string, letterOfCreditNo string, shipmentStatus string) (string, error) {

	loc, err := t.GetLoc(ctx, letterOfCreditNo)
	var flag bool
	if err != nil {
		return "", err
	}

	transaction := setTransaction("ShipGoodsBySeller", transactionInitiatedBy, letterOfCreditNo)

	if shipmentStatus == "true" {
		loc.IsShipGoodsBySeller = "Yes"
		transaction.Remarks = "Goods shipped by the Seller"
		flag = true
	} else if shipmentStatus == "false" {
		loc.IsShipGoodsBySeller = "No"
		transaction.Remarks = "Goods yet to be shipped by the Seller"
		flag = true
	} else {
		transaction.Remarks = "Invaild action"
		flag = false

	}
	loc.Transaction = append(loc.Transaction, transaction)
	locAsBytes, _ := json.Marshal(loc)

	err1 := ctx.GetStub().PutState(letterOfCreditNo, locAsBytes)
	if err1 != nil {
		return "", err1
	}
	if flag == true {
		return "Action completed Successfully", nil
	} else {
		return "Action Failed", nil
	}

}
func (t *LetterOfCreditCC) GoodsVerifiedBySeller(ctx contractapi.TransactionContextInterface, transactionInitiatedBy string, letterOfCreditNo string, verificationStatus string) (string, error) {

	loc, err := t.GetLoc(ctx, letterOfCreditNo)
	var flag bool
	if err != nil {
		return "", err
	}
	transaction := setTransaction("GoodsVerifiedBySeller", transactionInitiatedBy, letterOfCreditNo)

	if verificationStatus == "true" {
		loc.IsVerifiedSellerDocs = "Yes"
		transaction.Remarks = "Goods verified by the Buyer"
		flag = true
	} else if verificationStatus == "false" {
		loc.IsVerifiedSellerDocs = "No"
		transaction.Remarks = "Goods yet to be verified by the Buyer"
		flag = true
	} else {
		transaction.Remarks = "Invaild action"
		flag = false
	}
	loc.Transaction = append(loc.Transaction, transaction)
	locAsBytes, _ := json.Marshal(loc)

	err1 := ctx.GetStub().PutState(letterOfCreditNo, locAsBytes)
	if err1 != nil {
		return "", err1
	}
	if flag == true {
		return "Action completed Successfully", nil
	} else {
		return "Action Failed", nil
	}

}
func (t *LetterOfCreditCC) GoodsVerifiedByBuyer(ctx contractapi.TransactionContextInterface, transactionInitiatedBy string, letterOfCreditNo string, verificationStatus string) (string, error) {

	loc, err := t.GetLoc(ctx, letterOfCreditNo)
	var flag bool
	if err != nil {
		return "", err
	}
	transaction := setTransaction("GoodsVerifiedByBuyer", transactionInitiatedBy, letterOfCreditNo)

	if verificationStatus == "true" {
		loc.IsVerifiedBuyerDocs = "Yes"
		transaction.Remarks = "Goods verified by the Buyer"
		flag = true
	} else if verificationStatus == "false" {
		loc.IsVerifiedBuyerDocs = "No"
		transaction.Remarks = "Goods yet to be verified by the Buyer"
		flag = true
	} else {
		transaction.Remarks = "Invaild action"
		flag = false
	}
	loc.Transaction = append(loc.Transaction, transaction)
	locAsBytes, _ := json.Marshal(loc)

	err1 := ctx.GetStub().PutState(letterOfCreditNo, locAsBytes)
	if err1 != nil {
		return "", err1
	}
	if flag == true {
		return "Action completed Successfully", nil
	} else {
		return "Action Failed", nil
	}

}

func (t *LetterOfCreditCC) VerifiedShippingGoodsByBank(ctx contractapi.TransactionContextInterface, transactionInitiatedBy string, letterOfCreditNo string, verificationStatus string) (string, error) {

	loc, err := t.GetLoc(ctx, letterOfCreditNo)
	var flag bool
	if err != nil {
		return "", err
	}

	transaction := setTransaction("VerifiedShippingGoodsByBank", transactionInitiatedBy, letterOfCreditNo)

	if loc.LocStatus == "Accepted" && loc.IsShipGoodsBySeller == "Yes" && loc.IsVerifiedBuyerDocs == "Yes" && loc.IsVerifiedSellerDocs == "Yes" {
		loc.IsVerifiedShippingGoodsByBank = "Yes"
		transaction.Remarks = "Goods shipment verified by the Bank"
		flag = true
	} else {
		loc.IsVerifiedShippingGoodsByBank = "No"
		transaction.Remarks = "Goods shipment yet to be verified by the Bank"
		flag = false
	}
	loc.Transaction = append(loc.Transaction, transaction)
	locAsBytes, _ := json.Marshal(loc)

	err1 := ctx.GetStub().PutState(letterOfCreditNo, locAsBytes)
	if err1 != nil {
		return "", err1
	}
	if flag == true {
		return "Action completed Successfully", nil
	} else {
		return "Action Failed", nil
	}

}

func (t *LetterOfCreditCC) ReleasePayment(ctx contractapi.TransactionContextInterface, transactionInitiatedBy string, letterOfCreditNo string, paymentAmount string) (string, error) {
	loc, err := t.GetLoc(ctx, letterOfCreditNo)
	var flag bool
	if err != nil {
		return "", err
	}
	transaction := setTransaction("ReleasePayment", transactionInitiatedBy, letterOfCreditNo)

	if loc.IsVerifiedShippingGoodsByBank == "Yes" && loc.ReleasePayment == "No" {
		loc.ReleasePayment = "Yes"
		loc.PaymentAmount = paymentAmount
		transaction.Remarks = "Amount paid successfully"
		flag = true
	} else {
		transaction.Remarks = "Failed to pay amount"
		flag = false
	}
	loc.Transaction = append(loc.Transaction, transaction)
	locAsBytes, _ := json.Marshal(loc)

	err1 := ctx.GetStub().PutState(letterOfCreditNo, locAsBytes)
	if err1 != nil {
		return "", err1
	}
	if flag == true {
		return "Action completed Successfully", nil
	} else {
		return "Action Failed", nil
	}

}

func setTransaction(transactionType string, transactionInitiatedBy string, letterOfCreditNo string) Transaction {
	flake := sonyflake.NewSonyflake(sonyflake.Settings{})
label:
	id, err := flake.NextID()
	if err != nil {
		goto label
	}

	var transaction Transaction
	transaction.TransactionId = "TX" + strconv.FormatUint(id, 10)
	transaction.TransactionType = transactionType
	transaction.TimeStamp = time.Now().String()
	transaction.TransactionInitiatedBy = transactionInitiatedBy
	transaction.LetterOfCreditNo = letterOfCreditNo

	return transaction
}
func (t *LetterOfCreditCC) DeletelocForUs(ctx contractapi.TransactionContextInterface, loc string) (string, error) {
	err := ctx.GetStub().DelState(loc)
	if err != nil {
		return "", err
	}
	return "Deleted locnumber : " + loc, nil
}

func main() {
	chaincode, err := contractapi.NewChaincode(new(LetterOfCreditCC))
	if err != nil {
		fmt.Printf("Error in LetterOfCredit chaincode:%s ", err.Error())
	}

	if err := chaincode.Start(); err != nil {
		fmt.Printf("Error in LetterOfCredit chaincode:%s ", err.Error())
	}
}
