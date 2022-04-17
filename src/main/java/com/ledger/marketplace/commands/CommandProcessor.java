package com.ledger.marketplace.commands;

import java.util.HashMap;
import java.util.Map;

import com.ledger.marketplace.services.Loan;
import com.ledger.marketplace.services.Payment;

import static com.ledger.marketplace.MarketPlaceConstant.COMMAND_DELIMITOR;
import static com.ledger.marketplace.MarketPlaceConstant.LOAN_COMMAND;
import static com.ledger.marketplace.MarketPlaceConstant.PAYMENT_COMMAND;
import static com.ledger.marketplace.MarketPlaceConstant.BALANCE_COMMAND;

public class CommandProcessor {
	
	
	//Assumptions made during the implementation
	//one borrower cannot get multiple loan for a single bank
	
	
	//db to store all the loan, 
	//String => bank_borrower
	//all the details related to loan in there in Loan Object stroed in db Hashmap
	Map<String, Loan> db = new HashMap<String, Loan>(); 
	
	public void process(String commandString) {
		
		String[] commandVals = commandString.trim().split(COMMAND_DELIMITOR);
		String command = commandVals[0];
		
		if(command.equals(LOAN_COMMAND) && commandVals.length == 6) {
			processLoan(commandVals);
		}else if(command.equals(PAYMENT_COMMAND) && commandVals.length == 5) {
			processPayment(commandVals);
		}else if(command.equals(BALANCE_COMMAND) && commandVals.length == 4) {
			processBalance(commandVals);
		}else {
			System.out.println("INVALID COMMAND !! " + commandString); 
		}
		
	}
	
	
	private void processLoan(String[] commandVals) {
		
		String bank = commandVals[1];
		String borrower = commandVals[2];
		double principalAmount = Double.parseDouble(commandVals[3]);
		int years = Integer.parseInt(commandVals[4]);
		double rateOfIntrest = Double.parseDouble(commandVals[5]);
		
		String dbKey = bank + "_" + borrower;
		Loan loan =  new Loan(bank, borrower, principalAmount, years, rateOfIntrest);
		db.put(dbKey, loan);
		
	}
	
	private void processPayment(String[] commandVals) {
		//if the last emi is less than the monthly emi
		//create a map<month, reduce emis>
		
		String bank = commandVals[1];
		String borrower = commandVals[2];
		double lumpSumAmount = Double.parseDouble(commandVals[3]);
		int emiNo = Integer.parseInt(commandVals[4]);
		
		//get loan details from db
		String dbKey = bank + "_" + borrower;
		Loan loan = db.get(dbKey);
		
		//add the payment to the lumpSumPaymentList
		Payment payment  = new Payment(bank, borrower, lumpSumAmount, emiNo);
		loan.getLumpSumPaymentList().add(payment);
		
		//Modify the emiMap and remove the last entries and modify the last entry amount
		int monthlyEmiAmount = loan.getMonthlyEmiAmount();
		int lessNoEmi = (int) (lumpSumAmount/monthlyEmiAmount);
		int lastEmiReduce = (int) Math.ceil(lumpSumAmount - monthlyEmiAmount*lessNoEmi)   ;
		
		//reducing the number of emi
		int[] remainingEmi = loan.getRemainingEmi();
		int totalEmibeforeReduce = loan.getEmiMap().size();
		
		//if the last emi is already reduced with the last lump sum payments
		if(loan.getEmiMap().get(totalEmibeforeReduce) != monthlyEmiAmount) {
			if(lastEmiReduce > loan.getEmiMap().get(totalEmibeforeReduce)) {
				lessNoEmi+=1;
				lastEmiReduce = monthlyEmiAmount - (lastEmiReduce - loan.getEmiMap().get(totalEmibeforeReduce));
			}else {
				lastEmiReduce = loan.getEmiMap().get(totalEmibeforeReduce) - lastEmiReduce;
			}
		}
		
		//remove the emi if the lumpsum amount if already paid for this emi
		for(int i = totalEmibeforeReduce; i > totalEmibeforeReduce - lessNoEmi; i--) {
			loan.getEmiMap().remove(i);
			remainingEmi[i] = 0;
		}
		
		
		//updating the number of remaining emi's
		remainingEmi[totalEmibeforeReduce - lessNoEmi] = 0;
		for(int i = emiNo ; i<totalEmibeforeReduce - lessNoEmi ; i++) {
			remainingEmi[i] = remainingEmi[i] - lessNoEmi;
		}
		
		loan.getEmiMap().put(totalEmibeforeReduce - lessNoEmi, lastEmiReduce);
		
		//update the emiNo Amount
		loan.getEmiMap().put(emiNo, (int)lumpSumAmount + monthlyEmiAmount);
	}

	private void processBalance(String[] commandVals) {
		String bank = commandVals[1];
		String borrower = commandVals[2];
		int emiNo = Integer.parseInt(commandVals[3]);
		
		String dbKey = bank + "_" + borrower;
		
		//get loan details
		Loan loan = db.get(dbKey);
		
		int amountPaid = 0;
		int emiLeft = loan.getRemainingEmi()[emiNo];
	
		for(int i=1; i<=emiNo; i++) {
			amountPaid+=loan.getEmiMap().get(i);
		}
		
		String statement = bank + COMMAND_DELIMITOR + borrower + COMMAND_DELIMITOR + amountPaid + COMMAND_DELIMITOR + emiLeft;
		System.out.println(statement);
	}


}
