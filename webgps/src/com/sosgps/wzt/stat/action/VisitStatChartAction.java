package com.sosgps.wzt.stat.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ServletUtilities;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.stat.service.VisitStatService;
import com.sosgps.wzt.system.common.Constants;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.ChartTools;

public class VisitStatChartAction extends DispatchWebActionSupport {
	//private static final Logger logger = Logger
	//		.getLogger(VisitStatChartAction.class);
	private static final Logger logger = Logger.getLogger(VisitStatChartAction.class);
	private VisitStatService visitStatService = (VisitStatService) SpringHelper.getBean("VisitStatServiceImpl");

	// 业务员出访统计,拜访次数单人图表
	public ActionForward visitCountChart(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deviceId = request.getParameter("deviceId");
		deviceId = CharTools.javaScriptEscape(deviceId);
		String vehicleNumber = request.getParameter("vehicleNumber");
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleNumber = java.net.URLDecoder.decode(vehicleNumber, "utf-8");
		vehicleNumber = CharTools.javaScriptEscape(vehicleNumber);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String utcStartTime = request.getParameter("utcStartTime");// utc开始时间
		String utcEndTime = request.getParameter("utcEndTime");// utc结束时间
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		Page<Object[]> list = visitStatService.visitCountChartSql(0, 65535, st,
				et, utcStartTime, utcEndTime, deviceId, duration);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			int total = list.getTotalCount();
			long[] yAxis = new long[total];
			String[] xAxis = new String[total];
			double average = 0D;
			double j = 0D;
			long height = 0;
			for (Object[] objects : list.getResult()) {
				if (objects != null) {
					long visitCount = 0;
					if (objects[0] != null) {
						visitCount = ((BigDecimal) objects[0]).longValue();
					}
					if (visitCount > height) {
						height = visitCount;
					}
					String d = (String) objects[1];
					yAxis[(int)j] = visitCount;
					xAxis[(int)j] = d;
					average += visitCount;
					j++;
				}
			}
			average = Math.round(new BigDecimal(average/j).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100D)/100D;
			int width = 500;
			if (42 * (int)j > 500) {
				width = 42 * (int)j;
			}
			double barWidth = ChartTools.getBarWidth((int)j);
			JFreeChart chart = ChartTools
					.createBarChart(yAxis, xAxis, barWidth,vehicleNumber+"的拜访次数柱状图","日期","拜访次数","平均拜访次数="+average+"",average);
			// 生成图片
			String filename = ServletUtilities.saveChartAsPNG(chart, width,
					400, null, request.getSession());
			String graphURL = request.getContextPath()
					+ "/DisplayChart?filename=" + filename; // 通过servlet访问
			
			logger.info("[VisitStatChartAction-visitCountChart] graphURL: "+graphURL);
			
			/*try {
				OutputStream os = new FileOutputStream("d:/company.png");
				try {
					// 由ChartUtilities生成文件到一个体outputStream中去
					ChartUtilities.writeChartAsJPEG(os, chart, width, 400);
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			}*/

			request.getSession().setAttribute("graphURL", graphURL);
			request.getSession().setAttribute("filename", filename);
			return mapping.findForward("chart");
		} else {
			return mapping.findForward(null);
		}
	}

	// 业务员拜访次数所有人员统计图表(sql)
	public ActionForward visitCountChartAll(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deviceIdsStr = request.getParameter("deviceIds");
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		Page<Object[]> list = visitStatService.visitCountChartAll(0, 65535, st,
				et, searchValue, deviceIds, duration);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			int total = list.getTotalCount();
			long[] yAxis = new long[total];
			String[] xAxis = new String[total];
			double j = 0D;
			long height = 0;
			double average = 0D;
			for (Object[] objects : list.getResult()) {
				long visitCount = ((BigDecimal) objects[2]).longValue();
				if (visitCount > height) {
					height = visitCount;
				}
				String vehicleNumber = (String) objects[1];
				yAxis[(int)j] = visitCount;
				average += visitCount;
				xAxis[(int)j] = vehicleNumber;
				j++;
			}
			average = Math.round(new BigDecimal(average/j).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100D)/100D;
			int width = 500;
			if (42 * (int)j > 500) {
				width = 42 * (int)j;
			}
			double barWidth = ChartTools.getBarWidth((int)j);
			JFreeChart chart = ChartTools
					.createBarChart(yAxis, xAxis, barWidth,"业务员拜访次数柱状图","人员","拜访次数","平均拜访次数="+average+"",average);
			// 生成图片
			String filename = ServletUtilities.saveChartAsPNG(chart, width,
					400, null, request.getSession());
			String graphURL = request.getContextPath()
					+ "/DisplayChart?filename=" + filename; // 通过servlet访问
			/*try {
				OutputStream os = new FileOutputStream("d:/company.png");
				try {
					// 由ChartUtilities生成文件到一个体outputStream中去
					ChartUtilities.writeChartAsJPEG(os, chart, width, 400);
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			}*/

			request.getSession().setAttribute("graphURL", graphURL);
			request.getSession().setAttribute("filename", filename);
			return mapping.findForward("chart");
		} else {
			return mapping.findForward(null);
		}
	}

	// 业务员出访统计,拜访客户数单人图表
	public ActionForward visitCusCountChartSql(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deviceId = request.getParameter("deviceId");
		deviceId = CharTools.javaScriptEscape(deviceId);
		String vehicleNumber = request.getParameter("vehicleNumber");
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleNumber = java.net.URLDecoder.decode(vehicleNumber, "utf-8");
		vehicleNumber = CharTools.javaScriptEscape(vehicleNumber);

		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String utcStartTime = request.getParameter("utcStartTime");// utc开始时间
		String utcEndTime = request.getParameter("utcEndTime");// utc结束时间
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		// Date startDate = DateUtility.strToDateTime(st);
		// Date endDate = DateUtility.strToDateTime(et);
		Page<Object[]> list = visitStatService.visitCusCountChartSql(0, 65535,
				st, et, utcStartTime, utcEndTime, deviceId, duration);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			int total = list.getTotalCount();
			long[] yAxis = new long[total];
			String[] xAxis = new String[total];
			double average = 0D;
			double j = 0D;
			long height = 0;
			for (Object[] objects : list.getResult()) {
				if (objects != null) {
					long visitCount = 0;
					if (objects[0] != null) {
						visitCount = ((BigDecimal) objects[0]).longValue();
					}
					if (visitCount > height) {
						height = visitCount;
					}
					String d = (String) objects[1];
					yAxis[(int)j] = visitCount;
					xAxis[(int)j] = d;
					average += visitCount;
					j++;
				}
			}
			average = Math.round(new BigDecimal(average/j).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100D)/100D;
			int width = 500;
			if (42 * (int)j > 500) {
				width = 42 * (int)j;
			}
			double barWidth = ChartTools.getBarWidth((int)j);
			JFreeChart chart = ChartTools.createBarChart(yAxis, xAxis, barWidth, vehicleNumber+"的拜访客户数柱状图","日期","拜访客户数","平均拜访客户数="+average+"",average);
			// 生成图片
			String filename = ServletUtilities.saveChartAsPNG(chart, width, 400, null, request.getSession());
			String graphURL = request.getContextPath()
					+ "/DisplayChart?filename=" + filename; // 通过servlet访问
			/*try {
				// OutputStream os = new FileOutputStream("d:/company.jpeg");
				OutputStream os = new FileOutputStream("d:/company.png");
				try {
					// 由ChartUtilities生成文件到一个体outputStream中去
					ChartUtilities.writeChartAsJPEG(os, chart, width, 400);
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			}*/

			request.getSession().setAttribute("graphURL", graphURL);
			request.getSession().setAttribute("filename", filename);
			return mapping.findForward("chart");
		} else {
			return mapping.findForward(null);
		}
	}

	// 业务员出访统计,拜访地点数单人图表
	public ActionForward visitPlaceCountChartSql(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deviceId = request.getParameter("deviceId");
		deviceId = CharTools.javaScriptEscape(deviceId);
		String vehicleNumber = request.getParameter("vehicleNumber");
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleNumber = java.net.URLDecoder.decode(vehicleNumber, "utf-8");
		vehicleNumber = CharTools.javaScriptEscape(vehicleNumber);

		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String utcStartTime = request.getParameter("utcStartTime");// utc开始时间
		String utcEndTime = request.getParameter("utcEndTime");// utc结束时间
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		// Date startDate = DateUtility.strToDateTime(st);
		// Date endDate = DateUtility.strToDateTime(et);
		Page<Object[]> list = visitStatService.visitPlaceCountChartSql(0,
				65535, st, et, utcStartTime, utcEndTime, deviceId, duration);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			int total = list.getTotalCount();
			long[] yAxis = new long[total];
			String[] xAxis = new String[total];
			double average = 0;
			double j = 0;
			long height = 0;
			for (Object[] objects : list.getResult()) {
				if (objects != null) {
					long visitCount = 0;
					if (objects[0] != null) {
						visitCount = ((BigDecimal) objects[0]).longValue();
					}
					if (visitCount > height) {
						height = visitCount;
					}
					String d = (String) objects[1];
					yAxis[(int)j] = visitCount;
					xAxis[(int)j] = d;
					average += visitCount;
					j++;
				}
			}
			average = Math.round(new BigDecimal(average/j).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100D)/100D;
			int width = 500;
			if (42 * (int)j > 500) {
				width = 42 * (int)j;
			}
			double barWidth = ChartTools.getBarWidth((int)j);
			JFreeChart chart = ChartTools
					.createBarChart(yAxis, xAxis, barWidth, vehicleNumber+"的拜访地点数柱状图","日期","拜访地点数","平均拜访地点数="+average+"",average);
			// 生成图片
			String filename = ServletUtilities.saveChartAsPNG(chart, width,
					400, null, request.getSession());
			String graphURL = request.getContextPath()
					+ "/DisplayChart?filename=" + filename; // 通过servlet访问
			/*try {
				// OutputStream os = new FileOutputStream("d:/company.jpeg");
				OutputStream os = new FileOutputStream("d:/company.png");
				try {
					// 由ChartUtilities生成文件到一个体outputStream中去
					ChartUtilities.writeChartAsJPEG(os, chart, width, 400);
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			}*/

			request.getSession().setAttribute("graphURL", graphURL);
			request.getSession().setAttribute("filename", filename);
			return mapping.findForward("chart");
		} else {
			return mapping.findForward(null);
		}
	}

	// 业务员拜访客户数所有人员统计图表(sql)
	public ActionForward visitCusCountChartAll(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deviceIdsStr = request.getParameter("deviceIds");
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		// Date startDate = DateUtility.strToDateTime(st);
		// Date endDate = DateUtility.strToDateTime(et);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		Page<Object[]> list = visitStatService.visitCusCountChartAll(0, 65535,
				st, et, searchValue, deviceIds, duration);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			int total = list.getTotalCount();
			long[] yAxis = new long[total];
			String[] xAxis = new String[total];
			double j = 0;
			long height = 0;
			double average = 0;
			for (Object[] objects : list.getResult()) {
				long visitCount = ((BigDecimal) objects[2]).longValue();
				if (visitCount > height) {
					height = visitCount;
				}
				String vehicleNumber = (String) objects[1];
				yAxis[(int)j] = visitCount;
				xAxis[(int)j] = vehicleNumber;
				average += visitCount;
				j++;
			}
			average = Math.round(new BigDecimal(average/j).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100D)/100D;
			int width = 500;
			if (42 * (int)j > 500) {
				width = 42 * (int)j;
			}
			double barWidth = ChartTools.getBarWidth((int)j);
			JFreeChart chart = ChartTools
					.createBarChart(yAxis, xAxis, barWidth, "业务员拜访客户数柱状图","人员","拜访客户数","平均拜访客户数="+average,average);
			// 生成图片
			String filename = ServletUtilities.saveChartAsPNG(chart, width,
					400, null, request.getSession());
			String graphURL = request.getContextPath()
					+ "/DisplayChart?filename=" + filename; // 通过servlet访问
			/*try {
				// OutputStream os = new FileOutputStream("d:/company.jpeg");
				OutputStream os = new FileOutputStream("d:/company.png");
				try {
					// 由ChartUtilities生成文件到一个体outputStream中去
					ChartUtilities.writeChartAsJPEG(os, chart, width, 400);
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			}*/

			request.getSession().setAttribute("graphURL", graphURL);
			request.getSession().setAttribute("filename", filename);
			return mapping.findForward("chart");
		} else {
			return mapping.findForward(null);
		}
	}

	// 业务员拜访地点数所有人员统计图表(sql)
	public ActionForward visitPlaceCountChartAll(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deviceIdsStr = request.getParameter("deviceIds");
		deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		Page<Object[]> list = visitStatService.visitPlaceCountChartAll(0,
				65535, st, et, searchValue, deviceIds, duration);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			int total = list.getTotalCount();
			long[] yAxis = new long[total];
			String[] xAxis = new String[total];
			double j = 0;
			long height = 0;
			double average = 0;
			for (Object[] objects : list.getResult()) {
				long visitCount = ((BigDecimal) objects[2]).longValue();
				if (visitCount > height) {
					height = visitCount;
				}
				String vehicleNumber = (String) objects[1];
				yAxis[(int)j] = visitCount;
				xAxis[(int)j] = vehicleNumber;
				average += visitCount;
				j++;
			}
			average = Math.round(new BigDecimal(average/j).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100D)/100D;
			int width = 500;
			if (42 * (int)j > 500) {
				width = 42 * (int)j;
			}
			double barWidth = ChartTools.getBarWidth((int)j);
			JFreeChart chart = ChartTools
					.createBarChart(yAxis, xAxis, barWidth, "业务员拜访地点数柱状图","人员","拜访地点数","平均拜访地点数="+average,average);
			// 生成图片
			String filename = ServletUtilities.saveChartAsPNG(chart, width,
					400, null, request.getSession());
			String graphURL = request.getContextPath()
					+ "/DisplayChart?filename=" + filename; // 通过servlet访问
			/*try {
				// OutputStream os = new FileOutputStream("d:/company.jpeg");
				OutputStream os = new FileOutputStream("d:/company.png");
				try {
					// 由ChartUtilities生成文件到一个体outputStream中去
					ChartUtilities.writeChartAsJPEG(os, chart, width, 400);
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			}*/

			request.getSession().setAttribute("graphURL", graphURL);
			request.getSession().setAttribute("filename", filename);
			return mapping.findForward("chart");
		} else {
			return mapping.findForward(null);
		}
	}

	public ActionForward visitStatPlaceMap(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String deviceId = request.getParameter("deviceId");
		deviceId = CharTools.javaScriptEscape(deviceId);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		//Date startDate = DateUtility.strToDateTime(st);
		//Date endDate = DateUtility.strToDateTime(et);

		StringBuffer jsonSb = new StringBuffer();
		Page<Object[]> list = visitStatService.visitStatPlaceMap(0, 65535,
				st, et, deviceId, duration);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			//int total = list.getTotalCount();
			long radius = userInfo.getEnt().getVisitPlaceDistance() == null?500:userInfo.getEnt().getVisitPlaceDistance();
			for (Object[] objects : list.getResult()) {
				double x = ((BigDecimal) objects[0]).doubleValue();
				double y = ((BigDecimal) objects[1]).doubleValue();
				com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
				double[] xs = { x };
				double[] ys = { y };
				com.sos.sosgps.api.DPoint[] dPoint = null;
				try{
					dPoint = coordApi.encryptConvert(xs, ys);
					x = dPoint[0].x;
					y = dPoint[0].y;
				}catch(Exception ex){
					logger.error("visitStatPlaceMap-error," + ex.getMessage());
				}
				
				jsonSb.append("{");
				jsonSb.append("x:'" + CharTools.killNullDouble2String(x, "0")
						+ "',");
				jsonSb.append("y:'" + CharTools.killNullDouble2String(y, "0")
						+ "',");
				jsonSb.append("radius:'"+radius+"'");
				jsonSb.append("},");
			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}

		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"[" + jsonSb.toString() + "]");
		return mapping.findForward(null);
	}
	
	public ActionForward downloadChart(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
		logger.info("downloadChart");	
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		//String graphURL = (String)request.getSession().getAttribute("graphURL");
		String filename = (String)request.getSession().getAttribute("filename");
		logger.info("filename: "+Constants.CHART_PATH + filename);
		// 读到流中
		InputStream inStream = new FileInputStream(Constants.CHART_PATH + filename);// 文件的存放路径
		// 设置输出的格式
		response.reset();
		response.setContentType("bin");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		// 循环取出流中的数据
		byte[] b = new byte[100];
		int len;
		try {
			while ((len = inStream.read(b)) > 0) response.getOutputStream().write(b, 0, len);
			inStream.close();
		} catch (IOException e) {
			logger.error("downloadLocal-error," + e.getMessage());
			// e.printStackTrace();
		}
		return mapping.findForward(null);
	}
	
	public ActionForward visitStatPlaceMap2(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		UserInfo userInfo = this.getCurrentUser(request);
		
		// add by 2012-12-18 //拜访地点数地图
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(userInfo.getEmpCodeBase64());
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userInfo.getUserId());
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "查询拜访地点数");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_VisitNumber);
		LogFactory.newLogInstance("optLogger").info(optLog);

		
		
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String deviceId = request.getParameter("deviceId");
		deviceId = CharTools.killNullString(deviceId);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		String durationStr = request.getParameter("duration");// 过滤拜访时间小于X分钟
		int duration = CharTools.str2Integer(durationStr, 15);// 默认15分钟
		//Date startDate = DateUtility.strToDateTime(st);
		//Date endDate = DateUtility.strToDateTime(et);

		StringBuffer jsonSb = new StringBuffer();
		Page<Object[]> list = visitStatService.visitStatPlaceMap(0, 65535,
				st, et, deviceId, duration);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			//int total = list.getTotalCount();
			long radius = userInfo.getEnt().getVisitPlaceDistance() == null?500:userInfo.getEnt().getVisitPlaceDistance();
			for (Object objects : list.getResult()) {
				String poiDatas = (String) objects;
				String[] poiDatasArr = poiDatas.split(",");
				double longitude = 0;
				double latitude = 0;
				if(poiDatasArr.length == 2){
					String longitudeStr = poiDatasArr[0];
					String latitudeStr = poiDatasArr[1];
					longitude = Double.parseDouble(longitudeStr);
					latitude = Double.parseDouble(latitudeStr);
				}else{
					continue;
				}
				
				/*com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
				double[] xs = { longitude };
				double[] ys = { latitude };
				com.sos.sosgps.api.DPoint[] dPoint = null;
				try{
					dPoint = coordApi.encryptConvert(xs, ys);
					longitude = dPoint[0].x;
					latitude = dPoint[0].y;
				}catch(Exception ex){
					logger.error("visitStatPlaceMap-error," + ex.getMessage());
				}*/
				
				jsonSb.append("{");
				jsonSb.append("x:'" + CharTools.killNullDouble2String(longitude, "0") + "',");
				jsonSb.append("y:'" + CharTools.killNullDouble2String(latitude, "0") + "',");
				jsonSb.append("radius:'"+radius+"'");
				jsonSb.append("},");
			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}

		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"[" + jsonSb.toString() + "]");
		return mapping.findForward(null);
	}
}
