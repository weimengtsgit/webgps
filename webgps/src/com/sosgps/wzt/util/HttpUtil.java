package com.sosgps.wzt.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * @Title:
 * @Description:
 * @Company: 
 * @author: 
 * @version 1.0
 * @date: 2010-10-8 上午10:28:40
 */
public class HttpUtil {
	public static String[] get(String url, int timeout){
		String[] re = new String[4];
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
//			getMethod.setRequestHeader("test", "test");
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
			re[0] = String.valueOf(response_code);
			re[1] = content_type;
			re[2] = new String(responseContent);
			re[3] = response_message;
		} catch (Exception e) {
			e.printStackTrace();
			re[0] = String.valueOf(903);
			re[1] = "";
			re[2] = null;
			re[3] = e.getMessage();
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
}
