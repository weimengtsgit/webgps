package com.sosgps.wzt.diarymark.action;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;
import com.mapabc.geom.CoordCvtAPI;
import com.sosgps.wzt.diary.service.DiaryService;
import com.sosgps.wzt.diarymark.service.DiaryMarkService;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.locate.service.LocateService;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiary;
import com.sosgps.wzt.orm.TDiaryMark;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.sms.service.SmsService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.terminal.bean.TTerminalBean;
import com.sosgps.wzt.terminal.service.TerminalService;
import com.sosgps.wzt.terminal.service.impl.TerminalServiceImpl;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.Config;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.GeoUtils;
import com.sosgps.wzt.util.GeoUtils.GaussSphere;

public class DiaryMarkAction extends DispatchWebActionSupport {
	private DiaryMarkService diaryMarkService = (DiaryMarkService) SpringHelper.getBean("DiaryMarkServiceImpl");
	
	public ActionForward listDiaryMarkById(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		//String start = request.getParameter("start");
		//String limit = request.getParameter("limit");
		//String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		//String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String id = request.getParameter("id");
		if(id == null){
			response.getWriter().write("{result:\"9\"}");
			return mapping.findForward(null);
		}
		StringBuffer jsonSb = new StringBuffer();
		Page<TDiaryMark> list = diaryMarkService.listDiaryMarkById(entCode, userId, 1, 65535, Long.parseLong(id));
		if (list != null && list.getResult() != null && list.getResult().size() > 0) {
			for (Iterator<TDiaryMark> iterator = list.getResult().iterator(); iterator.hasNext();) {
				//Object[] objects = iterator.next();
				TDiaryMark tDiaryMark = (TDiaryMark) iterator.next();
				jsonSb.append("[");
				jsonSb.append("'" + tDiaryMark.getId() + "',");
				jsonSb.append("'" + tDiaryMark.getDeviceId() + "',");
				jsonSb.append("'" + tDiaryMark.getLongitude() + "',");
				jsonSb.append("'" + tDiaryMark.getLatitude() + "',");
				jsonSb.append("'" + tDiaryMark.getEntCode() + "',");
				jsonSb.append("'获取'");
				jsonSb.append("],");
			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}
		response.setContentType("text/json; charset=utf-8");
		//System.out.println("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		response.getWriter().write("[" + jsonSb.toString() + "]");
	    // 日志记录
	    TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.DIARY_MARK_SET);
		tOptLog.setFunCType(LogConstants.DIARY_MARK_SET_QUERY_USER);
		tOptLog.setOptDesc("查询业务员日志标注点成功");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}
	
	public ActionForward listDiaryMarkByDeviceId(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		//String start = request.getParameter("start");
		//String limit = request.getParameter("limit");
		//String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		//String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String diaryDate = request.getParameter("diaryDate");
		String deviceId = request.getParameter("deviceId");
		if(diaryDate == null || deviceId == null){
			response.getWriter().write("{result:\"9\"}");
			return mapping.findForward(null);
		}
		Date startTime = DateUtility.strToDateTime(diaryDate+" 00:00:00");
		Date endTime = DateUtility.strToDateTime(diaryDate+" 23:59:59");
		StringBuffer jsonSb = new StringBuffer();
		Page<TDiaryMark> list = diaryMarkService.listDiaryMarkByDeviceId(entCode, userId, 1, 65535, startTime, endTime, "", deviceId);
		if (list != null && list.getResult() != null && list.getResult().size() > 0) {
			for (Iterator<TDiaryMark> iterator = list.getResult().iterator(); iterator.hasNext();) {
				//Object[] objects = iterator.next();
				TDiaryMark tDiaryMark = (TDiaryMark) iterator.next();
				jsonSb.append("[");
				jsonSb.append("'" + tDiaryMark.getId() + "',");
				jsonSb.append("'" + tDiaryMark.getDeviceId() + "',");
				jsonSb.append("'" + tDiaryMark.getLongitude() + "',");
				jsonSb.append("'" + tDiaryMark.getLatitude() + "',");
				jsonSb.append("'" + tDiaryMark.getEntCode() + "',");
				jsonSb.append("'获取'");
				jsonSb.append("],");
			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}
		response.setContentType("text/json; charset=utf-8");
		//System.out.println("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		response.getWriter().write("[" + jsonSb.toString() + "]");
	    // 日志记录
	    TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.DIARY_MARK_SET);
		tOptLog.setFunCType(LogConstants.DIARY_MARK_SET_QUERY_USER);
		tOptLog.setOptDesc("查询业务员日志标注点成功");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}
}
