package com.example.exceptions;

public class couponAlreadyExistException extends Exception {

	public couponAlreadyExistException() {
		super("coupon already exists!");
	}
}
