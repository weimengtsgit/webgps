package com.sosgps.v21.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sosgps.wzt.util.DateUtility;

public class JsonUtil {

	private static Logger logger = Logger.getLogger(JsonUtil.class);

	/**
	 * 
	 * @param result
	 * @return
	 */
	public static String buildResult(int result) {
		return "{result:" + result + "}";
	}

	public static String buildResult(String result) {
		return "{result:" + result + "}";
	}

	public static String buildNullResult() {
		return "{result:0,data:[]}";
	}

	/**
	 * 
	 * @param beanProperties
	 * @param objs
	 * @param size
	 * @return
	 */
	public static <T> String convertToJsonStr(String[] beanProperties,
			List<T> objs, int size) {
		StringBuffer buf = new StringBuffer("{total:");
		if (beanProperties == null || objs == null || objs.size() == 0) {
			return buf.append("0,data:[]}").toString();
		}
		return buildStringIfNotEmpty(beanProperties, objs, size);
	}

	/**
	 * 将对象转化为JSON字符并返回
	 * 
	 * @param beanProperties
	 * @param objs
	 * @return
	 */
	public static <T> String convertToJsonStr(String[] beanProperties,
			List<T> objs) {

		StringBuffer buf = new StringBuffer("{total:");
		if (beanProperties == null || objs == null || objs.size() == 0) {
			return buf.append("0,data:[]}").toString();
		}

		return buildStringIfNotEmpty(beanProperties, objs, null);
	}

	/**
	 * 
	 * @param content
	 * @throws IOException
	 */
	public static void sendJsonString(HttpServletResponse response,
			String content) {
		response.setContentType("text/json; charset=utf-8");
		try {
			response.getWriter().write(content);
		} catch (IOException e) {
			logger.error("JsonUtil sendJsonString======>>>", e);
		}
	}

	/**
	 * 
	 * @param beanProperties
	 * @param objs
	 * @param size
	 * @return
	 */
	private static <T> String buildStringIfNotEmpty(String[] beanProperties,
			List<T> objs, Integer size) {

		Class<?> clazz = objs.get(0).getClass();
		// 如果是数组则要特殊处理
		if (clazz.isArray()) {
			return buildStringIfArray(beanProperties, objs, size);
		}
		StringBuffer buf = new StringBuffer("{total:");

		// 获得属性与对应的get方法的映射
		Map<String, Method> mtds = new HashMap<String, Method>();
		for (Method m : objs.get(0).getClass().getMethods()) {
			if (m.getName().startsWith("get")) {
				mtds.put(m.getName().toLowerCase(), m);
			}
		}

		Map<String, Method> methods = new LinkedHashMap<String, Method>();
		Method mtd = null;
		for (int i = 0; i < mtds.size() && i < beanProperties.length; i++) {
			mtd = mtds.get("get" + beanProperties[i].toLowerCase());
			if (mtd != null) {
				methods.put(beanProperties[i], mtd);
			}
		}
		mtds = null;
		// 构造对象集合的JSON格式字符串并返回
		buf.append(size == null ? objs.size() : size.intValue()).append(
				",data:[");
		try {
			Object[] objects = new Object[] {};
			Set<Entry<String, Method>> entrys = methods.entrySet();
			Object value = null;

			for (T obj : objs) {
				buf.append("{");
				for (Entry<String, Method> entry : entrys) {
					value = entry.getValue().invoke(obj, objects);
					buf.append(entry.getKey()).append(":").append("'");
					if (value != null) {
						if (value instanceof java.util.Date) {
							buf.append(DateUtility
									.dateTimeToStr((java.util.Date) value));
						} else if (value instanceof java.lang.String) {
							buf.append(CharTools
									.javaScriptEscape((String) value));
						} else {
							buf.append(value);
						}
					}
					buf.append("',");
				}
				buf.deleteCharAt(buf.length() - 1).append("},");
			}
			buf.deleteCharAt(buf.length() - 1).append("]}");
			return buf.toString();
		} catch (Exception e) {
			logger.error("JsonUtil buildStringIfNotEmpty======>>>", e);
			return null;
		}

	}

	/**
	 * 
	 * @param beanProperties
	 * @param objs
	 * @param size
	 * @return
	 */
	private static <T> String buildStringIfArray(String[] beanProperties,
			List<T> objs, Integer size) {
		StringBuffer buf = new StringBuffer("{total:");
		buf.append(size == null ? objs.size() : size.intValue()).append(
				",data:[");
		int len = ((Object[]) objs.get(0)).length;
		Object[] objects = null;
		for (T obj : objs) {
			objects = (Object[]) obj;
			buf.append("{");
			for (int i = 0; i < beanProperties.length && i < len; i++) {
				if (objects[i] == null) {
					buf.append(beanProperties[i]).append(":'',");
				} else {
					if (objects[i] instanceof java.util.Date) {
						buf.append(beanProperties[i])
								.append(":'")
								.append(DateUtility
										.dateTimeToStr((java.util.Date) objects[i]))
								.append("',");
					} else if (objects[i] instanceof java.lang.String) {
						buf.append(beanProperties[i])
								.append(":'")
								.append(CharTools
										.javaScriptEscape((String) objects[i]))
								.append("',");
					} else {
						buf.append(beanProperties[i]).append(":'")
								.append(objects[i]).append("',");
					}
				}
			}
			buf.deleteCharAt(buf.length() - 1).append("},");
		}
		buf.deleteCharAt(buf.length() - 1).append("]}");
		return buf.toString();
	}
}