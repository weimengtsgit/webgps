package org.sos.exception;

import org.apache.log4j.Logger;

public class AppException extends RuntimeException {
	private static final Logger logger = Logger.getLogger(AppException.class);
	private static final long serialVersionUID = -2155859930286481962L;

	private String key;// 得到本地资源文件key
	private Object[] values;

	public AppException() {
		super();
	}

	public AppException(String message, Throwable arg1) {
		super(message, arg1);
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable message) {
		super(message);
	}

	public AppException(String key, String message, Throwable arg1) {
		super(message, arg1);
		this.key = key;
	}

	public AppException(String key, Object value) {
		super();
		this.key = key;
		values = new Object[] { value };
	}

	public AppException(String key, Object value1, Object value2) {
		super();
		this.key = key;
		values = new Object[] { value1, value2 };
	}

	public AppException(String key, Object[] values) {
		super();
		this.key = key;
		this.values = values;
	}

	public String getKey() {
		return key;
	}

	public Object[] getValues() {
		return values;
	}
}
