package com.ledger.marketplace.services;

public class Payment {
	
	private String bank;
	private String borrower;
	private double lumpSumAmount;
	private int emiNo;                    //after this no. of emi the lumpSumAmount is received
	
	public Payment(String bank, String borrower, double lumpSumAmount, int emiNo) {
		this.bank =  bank;
		this.borrower = borrower;
		this.lumpSumAmount = lumpSumAmount;
		this.emiNo = emiNo;
	}

}
