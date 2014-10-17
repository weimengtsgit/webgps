package org.sos.exception;

import org.apache.log4j.Logger;

public class OperateFailureException extends RuntimeException {
	private static final Logger logger = Logger.getLogger(OperateFailureException.class);
	private static final long serialVersionUID = -92100741801033510L;

	private String key;// 得到本地资源文件key
	private Object[] values;

	public OperateFailureException() {
		super();
	}

	public OperateFailureException(String message, Throwable arg1) {
		super(message, arg1);
	}

	public OperateFailureException(String message) {
		super(message);
	}

	public OperateFailureException(Throwable message) {
		super(message);
	}

	public OperateFailureException(String key, String message, Throwable arg1) {
		super(message, arg1);
		this.key = key;
	}

	public OperateFailureException(String key, Object value) {
		super();
		this.key = key;
		values = new Object[] { value };
	}

	public OperateFailureException(String key, Object value1, Object value2) {
		super();
		this.key = key;
		values = new Object[] { value1, value2 };
	}

	public OperateFailureException(String key, Object[] values) {
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
