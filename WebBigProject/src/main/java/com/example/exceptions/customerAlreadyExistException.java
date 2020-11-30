package com.example.exceptions;

public class customerAlreadyExistException extends Exception {

	public customerAlreadyExistException() {
		super("customer already exist!");
	}
}
