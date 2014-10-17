package com.sosgps.wzt.sms.action;

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
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.SmsAccounts;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.sms.service.SmsService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

/**
 * @Title:����action
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-25 ����12:10:15
 */
public class SmsAction extends DispatchWebActionSupport {
	private SmsService smsService = (SmsService) SpringHelper
			.getBean("SmsServiceImpl");

	// sos���ж����б�
	public ActionForward listAllSms(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss

		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);

		String searchValue = request.getParameter("searchValue");

		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.killNullString(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			List<Object> list = smsService.listReceiveSmsLog(entCode, userId,
					startTime, endTime, searchValue);
			// ��ѯ���Ϊ��
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);// vehicleNumber
			excelWorkBook.addHeader("�ֻ�����", 15);// simcard
			excelWorkBook.addHeader("��������", 30);// content
			excelWorkBook.addHeader("ʱ��", 20);// createTime
			int row = 0;
			for (int i = 0; i < list.size(); i++) {
				Object[] objects = (Object[]) list.get(i);
				String logId = (String) objects[0];
				String simcard = (String) objects[1];
				String content = (String) objects[2];
				Date createTime = (Date) objects[3];
				String recvId = (String) objects[4];
				String vehicleNumber = (String) objects[5];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(content));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(DateUtility
								.dateTimeToStr(createTime)));
			}
			excelWorkBook.write();
			// add by 2012-12-17 zss �������ж��� ��־��¼
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.SMS_SET);
			tOptLog.setFunCType(LogConstants.SMS_SET_RECEIVEEXCEL);
			tOptLog.setOptDesc("�������ж���");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			return null;
		}

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = smsService.listReceiveSmsLog(entCode, userId,
					pageNo, pageSize, startTime, endTime, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();

				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] objects = iterator.next();
					String logId = (String) objects[0];
					String simcard = (String) objects[1];
					String content = (String) objects[2];
					Date createTime = (Date) objects[3];
					String recvId = (String) objects[4];
					String vehicleNumber = (String) objects[5];
					logId = CharTools.javaScriptEscape(logId);
					vehicleNumber = CharTools.javaScriptEscape(vehicleNumber);
					simcard = CharTools.javaScriptEscape(simcard);
					content = CharTools.javaScriptEscape(content);
					String createTimes = CharTools.javaScriptEscape(DateUtility
							.dateTimeToStr(createTime));

					boolean recvIdF = recvId == null ? true : false;
					if (!recvIdF) {
						// logId = logId+"_recv";
						vehicleNumber = vehicleNumber + "_recv";
						simcard = simcard + "_recv";
						content = content + "_recv";
						createTimes = createTimes + "_recv";
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + logId + "',");// id
					jsonSb.append("vehicleNumber:'" + vehicleNumber + "',");// vehicleNumber
					jsonSb.append("simcard:'" + simcard + "',");// simcard
					jsonSb.append("content:'" + content + "',");// content
					jsonSb.append("createTime:'" + createTimes + "',");// createTime
					jsonSb.append("hasRead:'"
							+ (recvId == null ? "true" : "false") + "'");// �Ƿ��Ѷ�
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		// System.out.println("{total:" + total + ",data:[" + jsonSb.toString()
		// + "]}");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");

		// ��־��¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.SMS_SET);
		tOptLog.setFunCType(LogConstants.SMS_SET_RECEIVE);
		tOptLog.setOptDesc("��ѯ���ն��ųɹ�");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}

	// sosδ�������б�
	public ActionForward listNotReadSms(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss

		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);

		String searchValue = request.getParameter("searchValue");

		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = smsService.listNotReadReceiveSms(entCode,
					userId, 1, 65536, startTime, endTime, searchValue);
			// ��ѯ���Ϊ��
			// if(list == null || list.getResult().size() == 0){
			// response.setContentType("text/json; charset=utf-8");
			// response.getWriter().write("{result:\"3\"}");// ��ѯ���Ϊ��
			// return null;
			// }
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);// vehicleNumber
			excelWorkBook.addHeader("�ֻ�����", 15);// simcard
			excelWorkBook.addHeader("��������", 20);// content
			excelWorkBook.addHeader("ʱ��", 20);// createTime
			int row = 0;
			for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
					.hasNext();) {
				Object[] objects = iterator.next();
				String logId = (String) objects[0];
				String simcard = (String) objects[1];
				String content = (String) objects[2];
				Date createTime = (Date) objects[3];
				String vehicleNumber = (String) objects[4];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(content));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(DateUtility
								.dateTimeToStr(createTime)));
			}
			excelWorkBook.write();
			// add by 2012-12-17 zss ����δ������ ��־��¼
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.SMS_SET);
			tOptLog.setFunCType(LogConstants.SMS_SET_UNREADEXCEL);
			tOptLog.setOptDesc("����δ������ ");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			return null;
		}

		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {

			int startint = Integer.parseInt(start);
			int pageSize = Integer.parseInt(limit);
			int pageNo = startint / pageSize + 1;
			Page<Object[]> list = smsService.listNotReadReceiveSms(entCode,
					userId, pageNo, pageSize, startTime, endTime, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();

				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] objects = iterator.next();
					String logId = (String) objects[0];
					String simcard = (String) objects[1];
					String content = (String) objects[2];
					Date createTime = (Date) objects[3];
					String vehicleNumber = (String) objects[4];
					jsonSb.append("{");
					jsonSb.append("id:'" + CharTools.javaScriptEscape(logId)
							+ "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// vehicleNumber
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// simcard
					jsonSb.append("content:'"
							+ CharTools.javaScriptEscape(content) + "',");// content
					jsonSb.append("createTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(createTime)) + "'");// createTime
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
		// add by 2012-12-17 zss ��ѯδ������ ��־��¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.SMS_SET);
		tOptLog.setFunCType(LogConstants.SMS_SET_UNREAD);
		tOptLog.setOptDesc("��ѯδ������ ");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}

	// sos���Ͷ���
	public ActionForward saveSms(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String simcards = request.getParameter("simcards");// simcard���","����
		simcards = java.net.URLDecoder.decode(simcards, "utf-8");
		String message = request.getParameter("message");// ��������
		message = java.net.URLDecoder.decode(message, "utf-8");
		int msglen = message.length() % 67 == 0 ? message.length() / 67
				: message.length() / 67 + 1;

		for (int i = 0; i < msglen; i++) {
			String msg = "";
			if ((i + 1) == msglen) {
				msg = message.substring(i * 67);
			} else {
				msg = message.substring(i * 67, (i + 1) * 67);
			}
			if (msg != null && msg.length() > 0) {
				String[] ss = CharTools.split(simcards, ",");
				smsService.sendSms(entCode, ss, msg);
				// ��־��¼
				TOptLog tOptLog = new TOptLog();
				tOptLog.setEmpCode(userInfo.getEmpCode());
				tOptLog.setUserName(userInfo.getUserAccount());
				tOptLog.setAccessIp(userInfo.getIp());
				tOptLog.setUserId(userInfo.getUserId());
				tOptLog.setFunFType(LogConstants.SMS_SET);
				tOptLog.setFunCType(LogConstants.SMS_SET_SEND);
				tOptLog.setOptDesc("�� " + simcards + " ���ŷ��ͳɹ�,�������ݣ�'" + msg
						+ "'");
				tOptLog.setResult(new Long(1));
				LogFactory.newLogInstance("optLogger").info(tOptLog);
			}
		}

		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sos��������Ϊ�Ѷ�
	public ActionForward readSms(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String recvId = request.getParameter("recvId");// ��������

		smsService.readSms(recvId);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{result:\"1\"}");// �ɹ�

		// add by 2012-12-17 zss ����Ѷ����� ��־��¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.SMS_SET);
		tOptLog.setFunCType(LogConstants.SMS_SET_UNREADUPDATE);
		tOptLog.setOptDesc("��������Ϊ�Ѷ��ɹ�");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		return mapping.findForward(null);
	}

	// sos�ѷ������б�
	public ActionForward listSendSmsLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		// HH:mm:ss

		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);

		String searchValue = request.getParameter("searchValue");

		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			List<Object> list = smsService.listSendSmsLog(entCode, userId,
					startTime, endTime, searchValue);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);// vehicleNumber
			excelWorkBook.addHeader("�ֻ�����", 15);// simcard
			excelWorkBook.addHeader("��������", 30);// content
			excelWorkBook.addHeader("ʱ��", 20);// createTime
			int row = 0;
			for (int i = 0; i < list.size(); i++) {
				Object[] objects = (Object[]) list.get(i);
				String logId = (String) objects[0];
				String simcard = (String) objects[1];
				String content = (String) objects[2];
				Date createTime = (Date) objects[3];
				String vehicleNumber = (String) objects[4];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(content));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(DateUtility
								.dateTimeToStr(createTime)));
			}
			excelWorkBook.write();

			// add by 2012-12-17 zss �����ѷ�����Excel ��־��¼
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.SMS_SET);
			tOptLog.setFunCType(LogConstants.SMS_SET_QUERYEXCEL);
			tOptLog.setOptDesc("�����ѷ��Ͷ���Excel�ɹ�");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			return null;
		}

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = smsService.listSendSmsLog(entCode, userId,
					pageNo, pageSize, startTime, endTime, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();

				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] objects = iterator.next();
					String logId = (String) objects[0];
					String simcard = (String) objects[1];
					String content = (String) objects[2];
					Date createTime = (Date) objects[3];
					String vehicleNumber = (String) objects[4];
					jsonSb.append("{");
					jsonSb.append("id:'" + CharTools.javaScriptEscape(logId)
							+ "',");// id
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");// vehicleNumber
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");// simcard
					jsonSb.append("content:'"
							+ CharTools.javaScriptEscape(content) + "',");// content
					jsonSb.append("createTime:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(createTime)) + "'");// createTime
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		// System.out.println("{total:" + total + ",data:[" + jsonSb.toString()
		// + "]}");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");

		// ��־��¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.SMS_SET);
		tOptLog.setFunCType(LogConstants.SMS_SET_QUERY);
		tOptLog.setOptDesc("��ѯ�ѷ��Ͷ��ųɹ�");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}

	// ��ѯ��ҵ��ǰ���ö������� add by liuhongxiao 2011-12-08
	public ActionForward smsavailable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();

		int total = 0;
		Long smsType = -1L;
		List<SmsAccounts> smsAccountsList = smsService.findByEntCode(entCode);
		if (smsAccountsList != null && smsAccountsList.size() > 0) {
			SmsAccounts smsAccounts = smsAccountsList.get(0);
			total = smsAccounts.getSmsAvailable();
			smsType = smsAccounts.gettEnt().getSmsType();
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",smsType:" + smsType + "}");
		return mapping.findForward(null);
	}
}
