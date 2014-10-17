package org.sos.exception;

import org.apache.log4j.Logger;

public class WebException extends RuntimeException {
	private static final Logger logger = Logger.getLogger(WebException.class);

	private static final long serialVersionUID = 2307602051608142210L;

	private String key;// 得到本地资源文件key
	private Object[] values;

	public WebException() {
		super();
	}

	public WebException(String message, Throwable arg1) {
		super(message, arg1);
	}

	public WebException(String message) {
		super(message);
	}

	public WebException(Throwable message) {
		super(message);
	}

	public WebException(String key, String message, Throwable arg1) {
		super(message, arg1);
		this.key = key;
	}

	public WebException(String key, Object value) {
		super();
		this.key = key;
		values = new Object[] { value };
	}

	public WebException(String key, Object value1, Object value2) {
		super();
		this.key = key;
		values = new Object[] { value1, value2 };
	}

	public WebException(String key, Object[] values) {
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
