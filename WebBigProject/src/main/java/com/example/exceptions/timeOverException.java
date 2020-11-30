package com.example.exceptions;

public class timeOverException extends Exception {

	public timeOverException() {
		super("the end date of the coupon already over");
	}
}
