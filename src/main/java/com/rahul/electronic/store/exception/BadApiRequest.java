package com.rahul.electronic.store.exception;

public class BadApiRequest extends RuntimeException {
 
	public BadApiRequest() {
		super("Bad request !!!");
	}
	
	public BadApiRequest(String s) {
		super(s);
	}
}
