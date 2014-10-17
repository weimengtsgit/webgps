package com.sosgps.wzt.diarymarktj.action;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.mapabc.geom.CoordCvtAPI;
import com.sosgps.wzt.diarymarktj.service.DiaryMarkTjService;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryMark;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TVisitTj;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class DiaryMarkTjAction extends DispatchWebActionSupport {
	private DiaryMarkTjService diaryMarkTjService = (DiaryMarkTjService) SpringHelper.getBean("DiaryMarkTjServiceImpl");
	
	/*
	 * 车辆信息
	 */
	@SuppressWarnings("static-access")
	public ActionForward listDiaryMarkTj(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");
		if(start == null || limit == null || deviceIds == null){
			response.getWriter().write("{result:\"9\"}");
			return mapping.findForward(null);
		}
		st = st+" 00:00:00";
		et = et+" 23:59:59";
		deviceIds = CharTools.splitAndAdd(deviceIds);
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.killNullString(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			//int startint = Integer.parseInt(start);
			//int pageSize = Integer.parseInt(limit);
			//String userAccount = request.getParameter("userAccount");
			Page<Object[]> list = diaryMarkTjService.listDiaryMarkTj(deviceIds,entCode, userId, 0, 65535, st, et, "");
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("部门", 15);
			excelWorkBook.addHeader("手机号码", 15);
			excelWorkBook.addHeader("日志日期", 15);
			excelWorkBook.addHeader("到达率", 15);
			int row = 0;
			String[] deviceIdDiaryDate = new String[list.getTotalCount()];
			for (Object[] objects : list.getResult()) {
				//BigDecimal id = (BigDecimal) objects[0];
				//String tj_date = (String) objects[1];
				String diary_date = (String) objects[2];
				diary_date = CharTools.javaScriptEscape(diary_date);
				BigDecimal arrival_rate = (BigDecimal) objects[3];
				String device_id = (String) objects[4];
				String term_name = (String) objects[5];
				//String ent_code = (String) objects[6];
				String group_name = (String) objects[7];
				String simcard = (String) objects[8];
				deviceIdDiaryDate[row] = device_id + "," + diary_date;
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(term_name));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(group_name));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row, diary_date);
				if(arrival_rate == null){
					excelWorkBook.addCell(col++, row, "");
				}else{
					excelWorkBook.addCell(col++, row, (Math.round(arrival_rate.doubleValue()*100)/100)+"%");
				}
			}
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
			for (String deviceIdDate : deviceIdDiaryDate) {
				String[] deviceIdDateArr = deviceIdDate.split(",");
				if(deviceIdDateArr.length == 2){
					Page<Object[] > list2 = diaryMarkTjService.listDiaryTjDetail(deviceIdDateArr[0], entCode, deviceIdDateArr[1], 1, 65535);
					if (list2 != null && list2.getResult() != null && list2.getResult().size() > 0) {
						row = 0;
						for (Object[] objects : list2.getResult()) {
							TDiaryMark tDiaryMark = (TDiaryMark)objects[0];
							String termname = (String)objects[1];
							Double longitude = tDiaryMark.getLongitude();
							Double latitude = tDiaryMark.getLatitude();
							Long isArrive = tDiaryMark.getIsArrive();
							
							String locDesc = "";
								if (tDiaryMark.getLongitude() > 0 && tDiaryMark.getLatitude() > 0) {
									double[] xs = { longitude };
									double[] ys = { latitude };
									try{
										com.sos.sosgps.api.DPoint[] dPoint =coordApizw.encryptConvert(xs, ys);
										String lngX = dPoint[0].getEncryptX();
										String latY = dPoint[0].getEncryptY();
										// 取得位置描述
										locDesc = coordCvtApi.getAddress(longitude+"", latitude+"");
									}catch(Exception ex){
										//this.logger.error("listAttendanceReportDetail-encryptConvert error,"+ex.getMessage());
									}
									
								}// 经纬度坐标为0
								else {
									locDesc = "没有收到卫星信号";// 位置描述
								}
							int col = 0;
							if (row == 0) {
								excelWorkBook.addWorkSheet(CharTools.javaScriptEscape(termname));
								excelWorkBook.addHeader("经度", 15);
								excelWorkBook.addHeader("纬度", 20);
								excelWorkBook.addHeader("是否到达", 20);
								excelWorkBook.addHeader("位置描述", 50);
							}
							row += 1;
							excelWorkBook.addCell(col++, row, longitude+"");
							excelWorkBook.addCell(col++, row, latitude+"");
							if(isArrive == 1){
								excelWorkBook.addCell(col++, row, "是");
							}else{
								excelWorkBook.addCell(col++, row, "否");
							}
							excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(locDesc));
						}
					}
					
				}
				
			}
			//add by 2012-12-14 zss 车辆到达信息
			TOptLog tOptLog = new TOptLog(); 
			tOptLog.setEmpCode(entCode);
			tOptLog.setUserName(userInfo.getUserAccount()); 
			tOptLog.setUserId(userId);
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_CARARRIVAL);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount()+" 导出车辆到达信息成功");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			
			excelWorkBook.write();
			return null;
		}
		
		//searchLog
		TOptLog tOptLog = new TOptLog(); 
		tOptLog.setEmpCode(entCode);
		tOptLog.setUserName(userInfo.getUserAccount()); 
		tOptLog.setUserId(userId);
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_CARARRIVAL);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount()+"查询车辆到达信息成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
			int startint = Integer.parseInt(start);
			int pageSize = Integer.parseInt(limit);
			Page<Object[]> list = diaryMarkTjService.listDiaryMarkTj(deviceIds,
					entCode, userId, startint, pageSize,
					st, et, "");
			if (list != null && list.getResult() != null && list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					BigDecimal id = (BigDecimal) objects[0];
					String tj_date = (String) objects[1];
					String diary_date = (String) objects[2];
					BigDecimal arrival_rate = (BigDecimal) objects[3];
					String device_id = (String) objects[4];
					String term_name = (String) objects[5];
					String ent_code = (String) objects[6];
					String group_name = (String) objects[7];
					String simcard = (String) objects[8];
					jsonSb.append("{");
					if(id == null){
						jsonSb.append("id:'0',");
					}else{
						jsonSb.append("id:'" + id + "',");
					}
					jsonSb.append("tjDate:'" + CharTools.javaScriptEscape(tj_date) + "',");
					jsonSb.append("diaryDate:'" + CharTools.javaScriptEscape(diary_date) + "',");
					if(arrival_rate == null){
						jsonSb.append("arrivalRate:'',");
					}else{
						jsonSb.append("arrivalRate:'" + arrival_rate + "',");
					}
					jsonSb.append("deviceId:'" + CharTools.javaScriptEscape(device_id) + "',");
					jsonSb.append("termName:'" + CharTools.javaScriptEscape(term_name) + "',");
					jsonSb.append("entCode:'" + CharTools.javaScriptEscape(ent_code) + "',");
					jsonSb.append("groupName:'" + CharTools.javaScriptEscape(group_name) + "',");
					jsonSb.append("simcard:'" + CharTools.javaScriptEscape(simcard) + "'");
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}
	
	public ActionForward listDiaryTjDetail(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		//Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String deviceId = request.getParameter("deviceId");
		String diaryDate = request.getParameter("deviceIds");
		if(start == null || limit == null || diaryDate == null || deviceId == null){
			response.getWriter().write("{result:\"9\"}");
			return mapping.findForward(null);
		}
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			int startint = Integer.parseInt(start);
			int pageSize = Integer.parseInt(limit);
			int pageNo = startint / pageSize + 1;
			Page<Object[]> list = diaryMarkTjService.listDiaryTjDetail(deviceId, entCode, diaryDate,
					pageNo, pageSize);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					TDiaryMark tDiaryMark = (TDiaryMark)objects[0];
					jsonSb.append("{");
					jsonSb.append("deviceId:'" + CharTools.javaScriptEscape(tDiaryMark.getDeviceId()) + "',");
					jsonSb.append("diaryDate:'" + DateUtility.dateToStr(tDiaryMark.getDiaryDate()) + "',");
					jsonSb.append("diaryid:'" + tDiaryMark.getDiaryId() + "',");
					jsonSb.append("entCode:'" + CharTools.javaScriptEscape(tDiaryMark.getEntCode()) + "',");
					jsonSb.append("isArrive:'" + tDiaryMark.getIsArrive() + "',");
					jsonSb.append("longitude:'" + tDiaryMark.getLongitude() + "',");
					jsonSb.append("latitude:'" + tDiaryMark.getLatitude() + "',");
					jsonSb.append("pd:''");
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}
}
