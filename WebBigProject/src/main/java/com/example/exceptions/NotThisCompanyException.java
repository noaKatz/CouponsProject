package com.example.exceptions;

public class NotThisCompanyException extends Exception {

	public NotThisCompanyException() {
		super("error! this is not your company");
	}
}
