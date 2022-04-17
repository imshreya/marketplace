package com.ledger.marketplace.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Loan {

	private String bank;
	private String borrower;
	private double principalAmount;
	private int years;
	private double rateOfInterest;
	private int totalEmi;
	private double totalAmount;
	private int monthlyEmiAmount;
	private int[] remainingEmi;
	private List<Payment> lumpSumPaymentList = new ArrayList<Payment>();           //unused list for this implementation
	private Map<Integer,Integer> emiMap= new HashMap<Integer,Integer>();          //collect all the lumpsum payments received;
	
	
	public Loan(String bank, String borrower, double principalAmount, int years, double rateOfInterest) {
		this.bank = bank;
		this.borrower = borrower;
		this.principalAmount = principalAmount;
		this.years = years;
		this.rateOfInterest = rateOfInterest;
		this.totalAmount = principalAmount  + Interest.calculateInterest(principalAmount, years, rateOfInterest);
		this.totalEmi = years * 12;
		this.monthlyEmiAmount =(int) Math.ceil(this.totalAmount / this.totalEmi);
		this.remainingEmi = new int[totalEmi+1];
		
		remainingEmi[0] = totalEmi;
		for(int i = 1; i <= totalEmi; i++) {
			emiMap.put(i, monthlyEmiAmount);
			remainingEmi[i] = totalEmi - i;
		}
		
	}
	
	public double getTotalAmount() {
		return totalAmount;
	}
	
	public List<Payment> getLumpSumPaymentList() {
		return this.lumpSumPaymentList;
	}
	
	public Map<Integer,Integer> getEmiMap(){
		return this.emiMap;
	}
	
	public int getMonthlyEmiAmount() {
		return this.monthlyEmiAmount;
	}
	
	public int[] getRemainingEmi() {
		return this.remainingEmi;
	}
	
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public void setLumpSumPaymentList(List<Payment> lumpSumPaymentList) {
		this.lumpSumPaymentList = lumpSumPaymentList;
	}
	
	public void setEmiMap(Map<Integer,Integer> emiMap){
		this.emiMap = emiMap;
	}
	
	public void setRemainingEmi(int[] remainingEmi) {
		this.remainingEmi = remainingEmi;
	}
}
