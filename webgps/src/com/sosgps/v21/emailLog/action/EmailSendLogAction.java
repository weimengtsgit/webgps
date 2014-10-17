package com.sosgps.v21.emailLog.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.v21.emailLog.service.EmailSendLogService;
import com.sosgps.v21.model.EmailSendLog;
import com.sosgps.v21.util.EmailSendLogUtils;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.impl.TEntServiceImpl;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class EmailSendLogAction extends DispatchWebActionSupport {

	private static final Logger logger = LoggerFactory
			.getLogger(EmailSendLogAction.class);

	private EmailSendLogService emailLogService = (EmailSendLogService) SpringHelper
			.getBean("EmailSendLogService");

	private TEntServiceImpl tEntService = (TEntServiceImpl) SpringHelper
			.getBean("tEntService");

	public ActionForward deleteEmailLogs(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String idstr = request.getParameter("ids");
		String[] ids = idstr.split(",");
		List<Long> longArray = new ArrayList<Long>();
		response.setContentType("text/html; charset=gbk");
		for (int i=0; i < ids.length; i++) {
		    longArray.add(Long.valueOf(ids[i]));
		}
		Long[] id = longArray.toArray(new Long[ids.length]);
		
		try {
		    emailLogService.deleteEmailLogs(id);
		} catch (Exception e) {
		    String res = "{success:false,info:\"" + e.getMessage() +"\"}";
		    response.getWriter().write(res);
		}
		response.getWriter().write(
                "{success:true,info:\"删除成功\"}");
		return mapping.findForward(null);
	}

	public ActionForward listEmailLogs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startDate");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String et = request.getParameter("endDate");// ����ʱ�䣬��ʽyyyy-MM-dd
		String contactName = request.getParameter("userName");
		contactName = URLDecoder.decode(contactName, "utf-8");
		String entName = request.getParameter("entName");
		entName = URLDecoder.decode(entName, "utf-8");

		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		Long startDateL = startDate.getTime();
		Long endDateL = endDate.getTime();
		if (start == null || limit == null || userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		entCode = URLDecoder.decode(entCode, "utf-8");

		// String entName = entCached.getEntName();
		Long userId = userInfo.getUserId();
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuilder jsonSb = new StringBuilder();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<EmailSendLog> list = emailLogService.listEmailLogs(startDateL,
					endDateL, entCode, entName, contactName, pageNo, pageSize);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (EmailSendLog emailLog : list.getResult()) {
					jsonSb.append("{");
					jsonSb.append("id:'" + emailLog.getId() + "',");// id
					jsonSb.append("sendTime:'"
							+ DateUtility.dateTimeToStr(new Date(emailLog
									.getCreateon())) + "',");// ����
					jsonSb.append("entCode:'" + entCode + "',");// ����
					jsonSb.append("entName:'" + emailLog.getEntName() + "',");
					jsonSb.append("userName:'" + emailLog.getContactName()
							+ "',");// Ա������
					jsonSb.append("position:'"
							+ CharTools
									.javaScriptEscape(emailLog.getPosition())
							+ "',");
					jsonSb.append("email:'" + emailLog.getEmail() + "',");// ״̬
					// modify for wangzhen start 2012-10-18
					if(null == emailLog.getContent()) {
					    jsonSb.append("content:'',");
					} else {
					    jsonSb.append("content:'" + emailLog.getContent() + "',");
					}
					// modify for wangzhen end 2012-10-18
					jsonSb.append("type:'',");
					jsonSb.append("status:''");
					jsonSb.append("},");

				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}

		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	/**
	 * �����ʼ�������־
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportEmailLogs(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			request.setAttribute("error", "�û�δ��¼!");
			return mapping.findForward("error");
		}
		String entCode = request.getParameter("entCode");
		entCode = URLDecoder.decode(entCode, "utf-8");
		String entName = request.getParameter("entName");
		entName = URLDecoder.decode(entName, "utf-8");
		String userName = request.getParameter("userName");
		userName = URLDecoder.decode(userName, "utf-8");
		String startDate = request.getParameter("startDate");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		String endDate = request.getParameter("endDate");// ����ʱ�䣬��ʽyyyy-MM-dd
		Date startTime = DateUtility.strToDateTime(startDate);
		Date endTime = DateUtility.strToDateTime(endDate);
		Long startTimeL = startTime.getTime();
		Long endTimeL = endTime.getTime();

		Page<EmailSendLog> page = emailLogService.listEmailLogs(startTimeL,
				endTimeL, entCode, entName, userName);
		List<EmailSendLog> logs = page.getResult();
		Workbook excel = EmailSendLogUtils
				.convertEmailSendLogFromObjectToExcel(logs, XSSFWorkbook.class);

		// ����
		response.setHeader("Content-Disposition",
				"attachment; filename=template.xlsx");
		ServletOutputStream os = response.getOutputStream();
		excel.write(os);
		os.close();
		return mapping.findForward(null);
	}
}
