package com.autonavi.util;

/**
 * <p>Title: 图盟科技企业移动平台</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Mapabc.com</p>
 * @author Mark Zeng
 * @version 1.0
 */
import javax.servlet.*;
import java.net.*;
import java.util.Hashtable;

import javax.servlet.http.*;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import com.autonavi.directl.Log;

import java.io.*;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class HttpUtil {

	public static byte[] getURLData(String url) throws Exception {
		URLConnection c = null;
		URL imageURL = null;
		DataInputStream is = null;
		byte[] btemp;
		try {
			imageURL = new URL(url);
			c = imageURL.openConnection();
			is = new DataInputStream(c.getInputStream());
			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
			byte[] bufferByte = new byte[256];
			int l = -1;
			int downloadSize = 0;
			while ((l = is.read(bufferByte)) > -1) {
				downloadSize += l;
				out.write(bufferByte, 0, l);
				out.flush();
			}
			btemp = out.toByteArray();
			out.close();
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}

		return btemp;
	}

	public static String getURLDecoderStr(String s) {
		return java.net.URLDecoder.decode(s);
	}
 
	public static String getURLData(String url, String encoding)
			throws Exception {
		byte[] b = getURLData(url);
		String r = new String(b, encoding);
		return r;
	}

	// post data
	public static byte[] getPostURLData(String urlStr, byte[] postData,
			String propertyValue) throws Exception {
		byte[] btemp  = null;
		URL url = new URL(urlStr);
		URLConnection urlConn = null;
		DataOutputStream printout = null;
		DataInputStream input = null;
		java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
		try {
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
 			urlConn.setDoOutput(true);
  			urlConn.setUseCaches(false);
  			
			urlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			if (propertyValue != null && propertyValue.trim().length() > 0) {
				urlConn.setRequestProperty("deviceid", propertyValue);
			}
			urlConn.setRequestProperty("Content=length", String
					.valueOf(postData.length));
			
			printout = new DataOutputStream(urlConn.getOutputStream());
			for (int i = 0; i < postData.length; i++) {
				printout.write(postData[i]);
			}
			printout.flush();
			printout.close();
			
			input = new DataInputStream(urlConn.getInputStream());
			byte[] bufferByte = new byte[256];
			int l = -1;
			int downloadSize = 0;
			
			while ((l = input.read(bufferByte)) > -1) {
				downloadSize += l;
				bout.write(bufferByte, 0, l);
				bout.flush();
			}
			  btemp = bout.toByteArray();
			System.out.println("bout.toByteArray 成功！");
			bout.close();
			input.close();
			input = null;
			return btemp;
		} catch (Exception ex) {
			Log.getInstance().errorLog("发送POST数据异常", ex);
		} finally {
 		}
		return btemp;
	}

	public static byte[] getPostData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		java.io.DataInputStream servletIn = null;
		java.io.ByteArrayOutputStream bout = null;
		byte[] inByte = null;
		try {
			servletIn = new java.io.DataInputStream(request.getInputStream());
			bout = new java.io.ByteArrayOutputStream();
			byte[] bufferByte = new byte[256];
			int l = -1;
			while ((l = servletIn.read(bufferByte)) > -1) {
				bout.write(bufferByte, 0, l);
				bout.flush();
			}
			inByte = bout.toByteArray();
		} catch (Exception ex) {
			try {
				servletIn.close();
				bout.close();
			} catch (Exception ex2) {
			}
		}
		if (inByte == null || inByte.length == 0) {
			return null;
		}
		return inByte;
	}

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
				content_type = getMethod.getResponseHeader("Content-Type")
						.toString();
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
	public static HttpResponseEntity sendPost(String url,
			Hashtable<String, String> postParams, int timeout) {
		HttpResponseEntity re = new HttpResponseEntity();
		// HttpClient默认的构造函数
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = new HttpClientParams();// 参数
		params.setContentCharset("UTF-8");
		params.setSoTimeout(timeout);// 超时
		httpClient.setParams(params);

		// 创建POST方法的实例
		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(url);
			// 自动处理转发过程去掉
			postMethod.setFollowRedirects(false);
			// 使用UTF-8发送
			postMethod.setRequestHeader("Content-Type",
					PostMethod.FORM_URL_ENCODED_CONTENT_TYPE
							+ "; charset=UTF-8");
			// 添加参数
			for (java.util.Enumeration<String> e = postParams.keys(); e.hasMoreElements();) {
				String parameterName = e.nextElement();
				String parameterValue = postParams.get(parameterName);
				postMethod.setParameter(parameterName, parameterValue);
			}
			// 执行postMethod
			int response_code = httpClient.executeMethod(postMethod);// 返回状态码
			String response_message = postMethod.getStatusText();// 返回描述
			byte[] responseContent = null;// 响应内容
			String content_type = null;// 编码
			if (response_code == HttpStatus.SC_OK) {
				content_type = postMethod.getResponseHeader("Content-Type")
						.toString();
				responseContent = postMethod.getResponseBody();
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


}
