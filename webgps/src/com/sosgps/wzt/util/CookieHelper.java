package com.sosgps.wzt.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title:处理cookie帮助类
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2009-6-2 下午03:29:26
 */
public class CookieHelper {
	/**
	 * 对应key的value增加至cookie中
	 * 
	 * @param request
	 * @param response
	 * @param expiry
	 * @param name
	 * @param value
	 * @return
	 */
	public static boolean setValue(HttpServletRequest request,
			HttpServletResponse response, int expiry, String key, String value) {
		if (request == null || response == null || key == null
				|| key.equals("") || value == null) {
			return false;
		}
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c == null || c.getName() == null)
					continue;
				if (c.getName().equals(key)) {
					cookie = c;
					break;
				}
			}
		}
		if (cookie == null) {
			cookie = new Cookie(key, value);
		}
		cookie.setPath("/");
		cookie.setMaxAge(expiry);
		cookie.setValue(value);
		response.addCookie(cookie);
		return true;
	}

	/**
	 * 取得cookie中对应key的value
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getValue(HttpServletRequest request, String key) {
		if (request == null || key == null || key.equals("")) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c == null || c.getName() == null)
					continue;
				if (c.getName().equals(key)) {
					cookie = c;
					break;
				}
			}
		}
		if (cookie == null) {
			return null;
		}
		String value = cookie.getValue();
		try {
			value = URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return value;
	}
}
