package com.ledger.marketplace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.ledger.marketplace.commands.CommandProcessor;


/**
 * The Ledger Co marketplace
 * 
 */
public class Main
{
	public static void main(final String[] args) throws Exception
	{
		
		CommandProcessor cmd = new CommandProcessor();
		File file = new File(args[0]);		 
		       
		BufferedReader br = new BufferedReader(new FileReader(file));
		 
		String st;
        while ((st = br.readLine()) != null)
            cmd.process(st);
	}
	
}
