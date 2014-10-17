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

		UserInfo userInfo = this.getCurrentUser(request);// ��¼�û���Ϣ

		String epCode = userInfo.getEmpCode();// //redo
		String deviceId = statForm.getDeviceId() == null ? ""
				: statForm.getDeviceId();// �ֻ�ID �ձ�ʾ���е��ն�
		String startDate = statForm.getStrStartDate() == null ? ""
				: statForm.getStrStartDate();// ��ʼʱ��
		String endDate = statForm.getStrEndDate() == null ? ""
				: statForm.getStrEndDate();// ����ʱ��

		String pageNo = statForm.getPageNo();// �ڼ�ҳ
		String pageSize = statForm.getPageSize();// ÿҳ����
		String paramName = statForm.getParamName();// �����ֶ�����
		String paramValue = statForm.getParamValue();// �����ֶ�ֵ
		String vague = statForm.getVague();// �����ֶ��Ƿ�ģ��ƥ��

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
			excelWorkBook.addHeader("������",15);
			excelWorkBook.addHeader("��������",20);
			excelWorkBook.addHeader("����ʱ��",20);
			excelWorkBook.addHeader("���ʵ�ַ",15);
			excelWorkBook.addHeader("�������",15);
			// excelWorkBook.addHeader("�鿴��ͼ");

			for (int i = 0; i < list.size(); i++) {
				TOptLog bean = list.get(i);
				excelWorkBook.addCell(0, (i + 1), bean.getUserName());
				excelWorkBook.addCell(1, (i + 1), bean.getOptDesc());
				excelWorkBook.addCell(2, (i + 1), bean.getOptTime().toString());
				excelWorkBook.addCell(3, (i + 1), bean.getAccessIp());
				excelWorkBook.addCell(4, (i+1), bean.getResult().intValue()==1?"�ɹ�":"ʧ��");
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
	 * ɾ����¼��־
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
		// ɾ���ɹ�
		if (ret) {
			tOptLog.setOptDesc("ɾ��������־�ɹ�!");
			tOptLog.setResult(new Long(1));
			return mapping.findForward("displayList");
			// ɾ��ʧ��
		} else {
			tOptLog.setOptDesc("ɾ����־ʧ��!");
			tOptLog.setResult(new Long(0));
			request.setAttribute("msg", "ɾ����־ʧ��");
			return mapping.findForward("message");
		}
	}
	
	// sos������־
	public ActionForward listOptLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
		
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);
		
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.killNullString(expExcel);
		if(expExcel.equalsIgnoreCase("true")){
			Page<TOptLog> re = optLoggerService.listOptLog(entCode, 1, 65535,
					startTime, endTime, searchValue);
			// ��ѯ���Ϊ��
//			if(re == null || re.getResult().size() == 0){
//				response.setContentType("text/json; charset=utf-8");
//				response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
//				return null;
//			}
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			List<TOptLog> list = re.getResult();

			// header
			excelWorkBook.addHeader("������",15);
			excelWorkBook.addHeader("��������",20);
			excelWorkBook.addHeader("����ʱ��",20);
			excelWorkBook.addHeader("���ʵ�ַ",15);
			excelWorkBook.addHeader("�������",15);
			// excelWorkBook.addHeader("�鿴��ͼ");

			for (int i = 0; i < list.size(); i++) {
				TOptLog bean = list.get(i);
				excelWorkBook.addCell(0, (i + 1), bean.getUserName());
				excelWorkBook.addCell(1, (i + 1), bean.getOptDesc());
				excelWorkBook.addCell(2, (i + 1), bean.getOptTime().toString());
				excelWorkBook.addCell(3, (i + 1), bean.getAccessIp());
				excelWorkBook.addCell(4, (i+1), bean.getResult().intValue()==1?"�ɹ�":"ʧ��");
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
					jsonSb.append("userName:'" + CharTools.javaScriptEscape(optLog.getUserName()) + "',");// ������
					jsonSb.append("optDesc:'" + CharTools.javaScriptEscape(optLog.getOptDesc()) + "',");// ��������
					jsonSb.append("optTime:'" + CharTools.javaScriptEscape(DateUtility.dateTimeToStr(optLog.getOptTime())) + "',");// ����ʱ��
					jsonSb.append("accessIp:'" + CharTools.javaScriptEscape(optLog.getAccessIp()) + "',");// ���ʵ�ַ
					jsonSb.append("result:'" + (optLog.getResult().intValue()==1?"�ɹ�":"ʧ��") + "'");// �������
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
