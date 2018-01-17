package com.api.helper;

/**
 * Unchecked exception specific to microservice testing
 */
public class MicroServiceTestException extends RuntimeException {

	public MicroServiceTestException(String message) {
		super(message);
	}

	public MicroServiceTestException(String message, Throwable cause) {
		super(message, cause);
	}
}
