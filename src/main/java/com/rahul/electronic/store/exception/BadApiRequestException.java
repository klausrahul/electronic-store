package com.rahul.electronic.store.exception;

public class BadApiRequestException extends RuntimeException {
 
	public BadApiRequestException() {
		super("Bad request !!!");
	}
	
	public BadApiRequestException(String s) {
		super(s);
	}
}
