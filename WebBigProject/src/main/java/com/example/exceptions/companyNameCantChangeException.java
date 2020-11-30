package com.example.exceptions;

public class companyNameCantChangeException extends Exception {
public companyNameCantChangeException() {
	super("error! you can not change the company name or id");
}
}
