package com.sosgps.v21.mobileMonitoring.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.sos.helper.SpringHelper;

import com.sosgps.v21.mobileMonitoring.service.MobileeventdataService;
import com.sosgps.v21.util.JsonUtil;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

/**
 */
public class MobileeventdataAction extends DispatchAction {

	private MobileeventdataService mobileeventdataService = (MobileeventdataService) SpringHelper
			.getBean("mobileeventdataService");

	// public void setMobileeventdataService(
	// MobileeventdataService mobileeventdataService) {
	// this.mobileeventdataService = mobileeventdataService;
	// }

	// 手机操作查询
	public ActionForward listAllMobile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			response.setCharacterEncoding("UTF-8");
			// 关键字
			String keyWords = request.getParameter("keyWords");
			String startTime1 = request.getParameter("startTime");
			startTime1 = startTime1 + " 00:00:00";
			String endTime1 = request.getParameter("endTime");
			endTime1 = endTime1 + " 23:59:59";
			// 模块类型
			String type = request.getParameter("type");

			String start = request.getParameter("start");
			String limit = request.getParameter("limit");
			String deviceId = request.getParameter("deviceId");
			deviceId = URLDecoder.decode(deviceId, "utf-8");
			deviceId = CharTools.javaScriptEscape(deviceId);
			String deviceIds = CharTools.splitAndAdd(deviceId);// 如：1,2,3

			Date starDate = StringUtils.isNotBlank(startTime1) ? DateUtility
					.strToDateTime(startTime1) : null;
			Date endDate = StringUtils.isNotBlank(endTime1) ? DateUtility
					.strToDateTime(endTime1) : null;
			// keyWords = StringUtils.isNotBlank(keyWords) ? URLDecoder.decode(
			// keyWords, "UTF-8") : null;
			if (keyWords != null) {
				keyWords = java.net.URLDecoder.decode(keyWords, "UTF-8");
			}

			type = StringUtils.isNotBlank(type) ? URLDecoder.decode(type,
					"UTF-8") : null;
			if ("-1".equals(type)) {
				type = "('gps_open_or_close','net_open_or_close')";
			} else {
				type = "('" + type + "')";
			}
			int startInt = NumberUtils.isNumber(start) ? NumberUtils
					.toInt(start) : 0;
			int limitInt = NumberUtils.isNumber(limit) ? NumberUtils
					.toInt(limit) : 10;
			JsonUtil.sendJsonString(response, mobileeventdataService
					.queryMobile(keyWords, starDate, endDate, type, deviceIds,
							startInt / limitInt + 1, limitInt));

			
			
			// add by 2012-12-18 手机模块监控报表查询
			HttpSession session = (HttpSession) request.getSession();
			response.setContentType("text/json; charset=UTF-8");
			UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
			TOptLog optLog = new TOptLog();
			optLog.setEmpCode(userInfo.getEmpCode());
			optLog.setUserName(userInfo.getUserAccount());
			optLog.setAccessIp(userInfo.getIp());
			optLog.setUserId(userInfo.getUserId());
			optLog.setOptTime(new Date());
			optLog.setResult(1L);
			optLog.setOptDesc(userInfo.getUserAccount() + "手机模块监控报表查询");
			optLog.setFunFType(LogConstants.LOG_STAT);
			optLog.setFunCType(LogConstants.LOG_STAT_MobileList);
			LogFactory.newLogInstance("optLogger").info(optLog);

			
			
		} catch (UnsupportedEncodingException e) {
			JsonUtil.sendJsonString(response, JsonUtil.buildNullResult());
			// logger.error("MobileeventdataAction listAllMobile error", e);
		}
		return null;
	}
	// /**
	// * 查询统计手机开关机操作次数
	// */
	// public ActionForward countMobileeventdata(ActionMapping mapping,
	// ActionForm form,
	// HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	// try {
	// String entName = request.getParameter("entName");
	// String entCode = request.getParameter("entCode");
	// String termName = request.getParameter("termName");
	// String startTime1 = request.getParameter("startTime");
	// String entTime1 = request.getParameter("entTime");
	// String start = request.getParameter("start");
	// String limit = request.getParameter("limit");
	//
	// Date starDate = StringUtils.isNotBlank(startTime1) ? DateUtility
	// .strToDateTime(startTime1) : null;
	// Date endDate = StringUtils.isNotBlank(entTime1) ? DateUtility
	// .strToDateTime(entTime1) : null;
	// entName = StringUtils.isNotBlank(entName) ? URLDecoder.decode(
	// entName, "UTF-8") : null;
	// entCode = StringUtils.isNotBlank(entCode) ? URLDecoder.decode(
	// entCode, "UTF-8") : null;
	// termName = StringUtils.isNotBlank(termName) ? URLDecoder.decode(
	// termName, "UTF-8") : null;
	// int startInt = NumberUtils.isNumber(start) ? NumberUtils
	// .toInt(start) : 0;
	// int limitInt = NumberUtils.isNumber(limit) ? NumberUtils
	// .toInt(limit) : 10;
	// JsonUtil.sendJsonString(response, mobileeventdataService
	// .countMobileeventdata(entName, entCode, termName, starDate, endDate,
	// startInt / limitInt + 1, limitInt));
	// } catch (UnsupportedEncodingException e) {
	// JsonUtil.sendJsonString(response, JsonUtil.buildNullResult());
	// //logger.error("MobileeventdataAction listAllMobile error", e);
	// }
	// return null;
	// }
}
