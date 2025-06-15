package com.guhan.formcraft.exceptions;

public class DataPersistanceException extends RuntimeException{
	public DataPersistanceException(String message, Exception e) {
		super(message,e);
	}
}
