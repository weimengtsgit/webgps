package com.sosgps.wzt.log.action;

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
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.log.form.OptLogForm;
import com.sosgps.wzt.log.service.OptLoggerService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.stat.alarmstat.util.DateTool;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class OptLogAction extends DispatchWebActionSupport {
	private OptLoggerService optLoggerService = (OptLoggerService) SpringHelper
			.getBean("optLoggerService");

	public ActionForward displayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OptLogForm statForm = (OptLogForm) form;

		UserInfo userInfo = this.getCurrentUser(request);// 登录用户信息

		String epCode = userInfo.getEmpCode();// //redo
		String deviceId = statForm.getDeviceId() == null ? ""
				: statForm.getDeviceId();// 手机ID 空表示所有的终端
		String startDate = statForm.getStrStartDate() == null ? ""
				: statForm.getStrStartDate();// 开始时间
		String endDate = statForm.getStrEndDate() == null ? ""
				: statForm.getStrEndDate();// 结束时间

		String pageNo = statForm.getPageNo();// 第几页
		String pageSize = statForm.getPageSize();// 每页行数
		String paramName = statForm.getParamName();// 过滤字段名称
		String paramValue = statForm.getParamValue();// 过滤字段值
		String vague = statForm.getVague();// 过滤字段是否模糊匹配

		if (startDate.equals("")) {
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			int year = calendar.get(calendar.YEAR);
			int month = calendar.get(calendar.MONTH) + 1;
			String strMonth = String.valueOf(month);
			if (month < 10) {
				strMonth = "0" + month;
			}
			int day = calendar.get(calendar.DAY_OF_MONTH);
			String strDay = String.valueOf(day);
			if (day < 10) {
				strDay = "0" + day;
			}
			statForm.setStrStartYear(String.valueOf(year));
			statForm.setStrStartYearId(String.valueOf(year));
			statForm.setStrStartMonth(String.valueOf(month));
			statForm.setStrStartMonthId(String.valueOf(month));
			statForm.setStrStartDay(String.valueOf(day));
			statForm.setStrStartDayId(String.valueOf(day));

			statForm.setStrEndYear(String.valueOf(year));
			statForm.setStrEndYearId(String.valueOf(year));
			statForm.setStrEndMonth(String.valueOf(month));
			statForm.setStrEndMonthId(String.valueOf(month));
			statForm.setStrEndDay(String.valueOf(day));
			statForm.setStrEndDayId(String.valueOf(day));

			startDate += year + "-" + strMonth + "-" + strDay;
			endDate=startDate;
		}


		Page<TOptLog> re= null;		
		
		if (statForm.getIsImportExcel() != null
				&& statForm.getIsImportExcel().equalsIgnoreCase("true")) {

			pageNo="0";
			pageSize = "65536";

			re = optLoggerService.queryOptLog(epCode, deviceId, startDate, endDate,
					pageNo, pageSize, paramName, paramValue, vague, true, null);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			
			List<TOptLog> list = re.getResult();

			// header
			excelWorkBook.addHeader("操作人",15);
			excelWorkBook.addHeader("操作描述",20);
			excelWorkBook.addHeader("操作时间",20);
			excelWorkBook.addHeader("访问地址",15);
			excelWorkBook.addHeader("操作结果",15);
			// excelWorkBook.addHeader("查看地图");

			for (int i = 0; i < list.size(); i++) {
				TOptLog bean = list.get(i);
				excelWorkBook.addCell(0, (i + 1), bean.getUserName());
				excelWorkBook.addCell(1, (i + 1), bean.getOptDesc());
				excelWorkBook.addCell(2, (i + 1), bean.getOptTime().toString());
				excelWorkBook.addCell(3, (i + 1), bean.getAccessIp());
				excelWorkBook.addCell(4, (i+1), bean.getResult().intValue()==1?"成功":"失败");
			}
			excelWorkBook.write();
			return null;
		}
			
		re = optLoggerService.queryOptLog(epCode, deviceId, startDate, endDate,
					pageNo, pageSize, paramName, paramValue, vague, true, null);
		
		request.setAttribute("optList", re);
		request.setAttribute("paramName", paramName);
		request.setAttribute("paramValue", paramValue);
		request.setAttribute("vague", vague);
		

		statForm.setDayList(DateTool.getDayList());
		statForm.setMonthList(DateTool.getMonthList());
		statForm.setYearList(DateTool.getYearList());
		
		return mapping.findForward("displayList");
	}

	/**
	 * 删除登录日志
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] sids = request.getParameterValues("ids");
		Long[] longIds = new Long[sids.length];
		for (int x = 0; x < sids.length; x++) {
			longIds[x] = Long.valueOf(sids[x]);
		}

		boolean ret = optLoggerService.deleteAll(longIds);
		UserInfo userInfo = this.getCurrentUser(request);

		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_F_CODE);
		tOptLog.setFunCType(LogConstants.LOG_C_LOGIN_CODE);
		// 删除成功
		if (ret) {
			tOptLog.setOptDesc("删除操作日志成功!");
			tOptLog.setResult(new Long(1));
			return mapping.findForward("displayList");
			// 删除失败
		} else {
			tOptLog.setOptDesc("删除日志失败!");
			tOptLog.setResult(new Long(0));
			request.setAttribute("msg", "删除日志失败");
			return mapping.findForward("message");
		}
	}
	
	// sos操作日志
	public ActionForward listOptLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd HH:mm:ss
		
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);
		
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.killNullString(expExcel);
		if(expExcel.equalsIgnoreCase("true")){
			Page<TOptLog> re = optLoggerService.listOptLog(entCode, 1, 65535,
					startTime, endTime, searchValue);
			// 查询结果为空
//			if(re == null || re.getResult().size() == 0){
//				response.setContentType("text/json; charset=utf-8");
//				response.getWriter().write("{result:\"3\"}");// 查询结果为空
//				return null;
//			}
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			List<TOptLog> list = re.getResult();

			// header
			excelWorkBook.addHeader("操作人",15);
			excelWorkBook.addHeader("操作描述",20);
			excelWorkBook.addHeader("操作时间",20);
			excelWorkBook.addHeader("访问地址",15);
			excelWorkBook.addHeader("操作结果",15);
			// excelWorkBook.addHeader("查看地图");

			for (int i = 0; i < list.size(); i++) {
				TOptLog bean = list.get(i);
				excelWorkBook.addCell(0, (i + 1), bean.getUserName());
				excelWorkBook.addCell(1, (i + 1), bean.getOptDesc());
				excelWorkBook.addCell(2, (i + 1), bean.getOptTime().toString());
				excelWorkBook.addCell(3, (i + 1), bean.getAccessIp());
				excelWorkBook.addCell(4, (i+1), bean.getResult().intValue()==1?"成功":"失败");
			}
			excelWorkBook.write();
			return null;
		}
		
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<TOptLog> list = optLoggerService.listOptLog(entCode, page, limitint,
					startTime, endTime, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator<TOptLog> iterator = list.getResult().iterator(); iterator
				.hasNext();) {
					TOptLog optLog = iterator.next();
					jsonSb.append("{");
					jsonSb.append("id:'" + optLog.getId() + "',");// id
					jsonSb.append("userName:'" + CharTools.javaScriptEscape(optLog.getUserName()) + "',");// 操作人
					jsonSb.append("optDesc:'" + CharTools.javaScriptEscape(optLog.getOptDesc()) + "',");// 操作描述
					jsonSb.append("optTime:'" + CharTools.javaScriptEscape(DateUtility.dateTimeToStr(optLog.getOptTime())) + "',");// 操作时间
					jsonSb.append("accessIp:'" + CharTools.javaScriptEscape(optLog.getAccessIp()) + "',");// 访问地址
					jsonSb.append("result:'" + (optLog.getResult().intValue()==1?"成功":"失败") + "'");// 操作结果
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return null;
	}
}
