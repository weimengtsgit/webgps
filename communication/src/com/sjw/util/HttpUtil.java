package com.sjw.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

/**
 * http���󹤾���
 * 
 * @author sjw
 * 
 */
public class HttpUtil {
	private static Logger log = Logger.getLogger(HttpUtil.class);
	private static boolean debug = false;

	/**
	 * ��ȡurl����
	 * 
	 * @param url
	 * @param timeout
	 * @return
	 */
	public static HttpResponseEntity sendGet(String url, int timeout) {
		HttpResponseEntity re = new HttpResponseEntity();
		// HttpClientĬ�ϵĹ��캯��
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = new HttpClientParams();// ����
		params.setSoTimeout(timeout);// ��ʱ
		httpClient.setParams(params);

		// ����GET������ʵ��
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);
			// �Զ�����ת������ȥ��
			getMethod.setFollowRedirects(false);
			// ִ��getMethod
			int response_code = httpClient.executeMethod(getMethod);// ����״̬��
			String response_message = getMethod.getStatusText();// ��������
			byte[] responseContent = null;// ��Ӧ����
			String content_type = null;// ����
			if (response_code == HttpStatus.SC_OK) {
				content_type = getMethod.getResponseHeader("Content-Type") != null ? getMethod
						.getResponseHeader("Content-Type").toString()
						: null;
				responseContent = getMethod.getResponseBody();
			}
			re.setContent_type(content_type);
			re.setResponse_code(response_code);
			re.setResponse_content(responseContent);
			re.setResponse_message(response_message);
		} catch (Exception e) {
			re.setContent_type("");
			re.setResponse_code(903);
			re.setResponse_message(e.getMessage());
			re.setResponse_content(null);
			if (debug)
				log.error("����!" + url, e);
		} finally {
			// �ͷ�����
			if (getMethod != null) {
				try {
					getMethod.releaseConnection();
				} catch (Exception e) {
				}
			}
		}
		return re;
	}

	/**
	 * ��ȡurl����
	 * 
	 * @param url
	 * @param requestEntity
	 * @param timeout
	 * @return
	 */
	public static HttpResponseEntity sendPost(String url, String requestEntity,
			int timeout) {
		HttpResponseEntity re = new HttpResponseEntity();
		// HttpClientĬ�ϵĹ��캯��
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = new HttpClientParams();// ����
		params.setSoTimeout(timeout);// ��ʱ
		// params.setContentCharset("GBK");
		// params.setContentCharset("UTF-8");
		// params.setContentCharset("ISO8859-1");
		httpClient.setParams(params);

		// ����POST������ʵ��
		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(url);
			// �Զ�����ת������ȥ��
			postMethod.setFollowRedirects(false);
			// ��Ӳ���
			postMethod.setRequestEntity(new StringRequestEntity(requestEntity,
			// "text/html", "utf-8"));
					"text/html", "GBK"));
			// "text/html", "ISO8859-1"));
			// ִ��postMethod
			int response_code = httpClient.executeMethod(postMethod);// ����״̬��
			String response_message = postMethod.getStatusText();// ��������
			byte[] responseContent = null;// ��Ӧ����
			String content_type = null;// ����
			if (response_code == HttpStatus.SC_OK) {
				content_type = postMethod.getResponseHeader("Content-Type") == null ? ""
						: postMethod.getResponseHeader("Content-Type")
								.toString();
				responseContent = postMethod.getResponseBody();
			}
			re.setContent_type(content_type);
			re.setResponse_code(response_code);
			re.setResponse_content(responseContent);
			re.setResponse_message(response_message);
		} catch (Exception e) {
			e.printStackTrace();
			re.setContent_type("");
			re.setResponse_code(903);
			re.setResponse_message(e.getMessage());
			re.setResponse_content(null);
			if (debug)
				log.error("����!" + url, e);
		} finally {
			// �ͷ�����
			if (postMethod != null) {
				try {
					postMethod.releaseConnection();
				} catch (Exception e) {
				}
			}
		}
		return re;
	}

	/**
	 * ��ȡurl����
	 * 
	 * @param url
	 * @param requestEntity
	 * @param timeout
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public static HttpResponseEntity sendPost(String url, int timeout,
			String[]... objects) {
		HttpResponseEntity re = new HttpResponseEntity();
		// HttpClientĬ�ϵĹ��캯��
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = new HttpClientParams();// ����
		params.setSoTimeout(timeout);// ��ʱ
		// params.setContentCharset("GBK");
		// params.setContentCharset("UTF-8");
		// params.setContentCharset("ISO8859-1");
		httpClient.setParams(params);

		// ����POST������ʵ��
		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(url);
			// �Զ�����ת������ȥ��
			postMethod.setFollowRedirects(false);
			// ��Ӳ���
			if (objects != null)
				for (String[] object : objects) {
					if (object != null && object.length == 2) {
						String paramName = object[0];
						String paramValue = object[1];
						postMethod.addParameter(paramName, paramValue);
					}
				}
			// postMethod.setRequestEntity(new
			// StringRequestEntity(requestEntity,
			// "text/html", "utf-8"));
			// "text/html", "GBK"));
			// "text/html", "ISO8859-1"));
			// ִ��postMethod
			int response_code = httpClient.executeMethod(postMethod);// ����״̬��
			String response_message = postMethod.getStatusText();// ��������
			byte[] responseContent = null;// ��Ӧ����
			String content_type = null;// ����
			if (response_code == HttpStatus.SC_OK) {
				content_type = postMethod.getResponseHeader("Content-Type") == null ? ""
						: postMethod.getResponseHeader("Content-Type")
								.toString();
				responseContent = postMethod.getResponseBody();
			}
			re.setContent_type(content_type);
			re.setResponse_code(response_code);
			re.setResponse_content(responseContent);
			re.setResponse_message(response_message);
		} catch (Exception e) {
			e.printStackTrace();
			re.setContent_type("");
			re.setResponse_code(903);
			re.setResponse_message(e.getMessage());
			re.setResponse_content(null);
			if (debug)
				log.error("����!" + url, e);
		} finally {
			// �ͷ�����
			if (postMethod != null) {
				try {
					postMethod.releaseConnection();
				} catch (Exception e) {
				}
			}
		}
		return re;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		HttpUtil.debug = debug;
	}
}