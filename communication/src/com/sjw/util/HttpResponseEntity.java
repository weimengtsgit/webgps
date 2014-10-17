package com.sjw.util;

/**
 * httpÏìÓ¦
 */


public class HttpResponseEntity {
	private int response_code;
	private String response_message;
	private byte[] response_content;
	private String content_type;

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public HttpResponseEntity() {
		super();
	}

	public HttpResponseEntity(int response_code, String response_message,
			byte[] response_content, String content_type) {
		super();
		this.response_code = response_code;
		this.response_message = response_message;
		this.response_content = response_content;
		this.content_type = content_type;
	}

	public int getResponse_code() {
		return response_code;
	}

	public void setResponse_code(int response_code) {
		this.response_code = response_code;
	}

	public String getResponse_message() {
		return response_message;
	}

	public void setResponse_message(String response_message) {
		this.response_message = response_message;
	}

	public byte[] getResponse_content() {
		return response_content;
	}

	public void setResponse_content(byte[] response_content) {
		this.response_content = response_content;
	}

	@Override
	public String toString() {
		String rn = "\r\n";
		StringBuffer re = new StringBuffer();
		re.append("code: ");
		re.append(response_code);
		re.append(rn);
		re.append("messgae: ");
		re.append(response_message);
		re.append(rn);
		re.append("type: ");
		re.append(content_type);
		re.append(rn);
		re.append("content: ");
		re.append(response_content==null?"":new String(response_content));
		return re.toString();
	}
}
