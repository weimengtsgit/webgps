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
 * @date: 2010-10-8 ����10:28:40
 */
public class HttpUtil {
	public static String[] get(String url, int timeout){
		String[] re = new String[4];
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
//			getMethod.setRequestHeader("test", "test");
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
}
