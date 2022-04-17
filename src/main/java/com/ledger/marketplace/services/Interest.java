package com.ledger.marketplace.services;

public class Interest {

	//Given assumptions are not ceiling the value of interest
	public static double calculateInterest(double principalAmount, int years, double rateOfInterest) {
		return (principalAmount*years*rateOfInterest)/100;
	}
}
