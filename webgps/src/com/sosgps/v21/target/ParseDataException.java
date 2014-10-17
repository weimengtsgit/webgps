package com.sosgps.v21.target;

@SuppressWarnings("serial")
public class ParseDataException extends RuntimeException {
	public ParseDataException(String msg) {
		super(msg);
	}

	public ParseDataException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
