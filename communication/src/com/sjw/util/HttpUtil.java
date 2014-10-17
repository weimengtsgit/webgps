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
 * http请求工具类
 * 
 * @author sjw
 * 
 */
public class HttpUtil {
	private static Logger log = Logger.getLogger(HttpUtil.class);
	private static boolean debug = false;

	/**
	 * 读取url内容
	 * 
	 * @param url
	 * @param timeout
	 * @return
	 */
	public static HttpResponseEntity sendGet(String url, int timeout) {
		HttpResponseEntity re = new HttpResponseEntity();
		// HttpClient默认的构造函数
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = new HttpClientParams();// 参数
		params.setSoTimeout(timeout);// 超时
		httpClient.setParams(params);

		// 创建GET方法的实例
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);
			// 自动处理转发过程去掉
			getMethod.setFollowRedirects(false);
			// 执行getMethod
			int response_code = httpClient.executeMethod(getMethod);// 返回状态码
			String response_message = getMethod.getStatusText();// 返回描述
			byte[] responseContent = null;// 响应内容
			String content_type = null;// 编码
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
				log.error("错误!" + url, e);
		} finally {
			// 释放连接
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
	 * 读取url内容
	 * 
	 * @param url
	 * @param requestEntity
	 * @param timeout
	 * @return
	 */
	public static HttpResponseEntity sendPost(String url, String requestEntity,
			int timeout) {
		HttpResponseEntity re = new HttpResponseEntity();
		// HttpClient默认的构造函数
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = new HttpClientParams();// 参数
		params.setSoTimeout(timeout);// 超时
		// params.setContentCharset("GBK");
		// params.setContentCharset("UTF-8");
		// params.setContentCharset("ISO8859-1");
		httpClient.setParams(params);

		// 创建POST方法的实例
		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(url);
			// 自动处理转发过程去掉
			postMethod.setFollowRedirects(false);
			// 添加参数
			postMethod.setRequestEntity(new StringRequestEntity(requestEntity,
			// "text/html", "utf-8"));
					"text/html", "GBK"));
			// "text/html", "ISO8859-1"));
			// 执行postMethod
			int response_code = httpClient.executeMethod(postMethod);// 返回状态码
			String response_message = postMethod.getStatusText();// 返回描述
			byte[] responseContent = null;// 响应内容
			String content_type = null;// 编码
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
				log.error("错误!" + url, e);
		} finally {
			// 释放连接
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
	 * 读取url内容
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
		// HttpClient默认的构造函数
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = new HttpClientParams();// 参数
		params.setSoTimeout(timeout);// 超时
		// params.setContentCharset("GBK");
		// params.setContentCharset("UTF-8");
		// params.setContentCharset("ISO8859-1");
		httpClient.setParams(params);

		// 创建POST方法的实例
		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(url);
			// 自动处理转发过程去掉
			postMethod.setFollowRedirects(false);
			// 添加参数
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
			// 执行postMethod
			int response_code = httpClient.executeMethod(postMethod);// 返回状态码
			String response_message = postMethod.getStatusText();// 返回描述
			byte[] responseContent = null;// 响应内容
			String content_type = null;// 编码
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
				log.error("错误!" + url, e);
		} finally {
			// 释放连接
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