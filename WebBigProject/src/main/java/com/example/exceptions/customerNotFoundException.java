package com.example.exceptions;

public class customerNotFoundException extends Exception {

	public customerNotFoundException() {
		super("customer not found!");
	}
}
