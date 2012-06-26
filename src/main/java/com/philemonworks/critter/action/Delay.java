package com.philemonworks.critter.action;

import com.philemonworks.critter.rule.RuleContext;

public class Delay extends RuleIngredient implements Action { 
	long milliSeconds;
	
	public void perform(RuleContext context){
		try {
			Thread.sleep(this.milliSeconds);
		} catch (InterruptedException e) {
			
		}
	}
	@Override
	public String explain() {
		return "delay the processing with ["+milliSeconds+"] milliseconds";
	}	
}
