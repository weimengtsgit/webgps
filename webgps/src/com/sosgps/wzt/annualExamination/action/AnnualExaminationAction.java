package com.sosgps.wzt.annualExamination.action;

import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.annualExamination.service.AnnualExaminationService;
import com.sosgps.wzt.annualExamination.service.impl.AnnualExaminationServiceImpl;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.insurance.service.InsuranceService;
import com.sosgps.wzt.insurance.service.impl.InsuranceServiceImpl;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAnnualExamination;
import com.sosgps.wzt.orm.TInsurance;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TToll;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class AnnualExaminationAction extends DispatchWebActionSupport {
	private AnnualExaminationService annualExaminationService = (AnnualExaminationServiceImpl) SpringHelper.getBean("AnnualExaminationServiceImpl");

	public ActionForward listAnnualExamination(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		String searchValue = request.getParameter("searchValue");
		searchValue =  CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		
		// ��session�л�ȡ��ҵ����
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = annualExaminationService.listAnnualExamination(entCode, userId, 1, 65536, startDate, endDate, searchValue);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("���ƺ�", 15);
			excelWorkBook.addHeader("�ֻ���", 15);
			excelWorkBook.addHeader("������", 15);
			excelWorkBook.addHeader("��������", 15);
			excelWorkBook.addHeader("������Ŀ", 15);
			excelWorkBook.addHeader("�������", 15);
			excelWorkBook.addHeader("������", 15);
			excelWorkBook.addHeader("������", 15);
			excelWorkBook.addHeader("��������", 15);
			excelWorkBook.addHeader("��ע", 15);
			int row = 0;
			for (Object[] objects : list.getResult()) {
	    		String termName = (String)objects[1];
	    		String vehicleNumber = (String)objects[2];
	    		String simcard = (String)objects[3];
	    		String groupName = (String)objects[4];
	    		TAnnualExamination tAnnualExamination = (TAnnualExamination)objects[5];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(termName));										//�ն�����
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(vehicleNumber));       							//���ƺ�
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(simcard));											//�ֻ���
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(groupName));										//������
				excelWorkBook.addCell(col++, row,DateUtility.dateToStr(tAnnualExamination.getExaminationDate()));				//��������
				excelWorkBook.addCell(col++, row,CharTools.javaScriptEscape(tAnnualExamination.getProject()));					//������Ŀ
				excelWorkBook.addCell(col++, row,CharTools.javaScriptEscape(tAnnualExamination.getCondition()));				//�������
				excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(tAnnualExamination.getExpenses(),"0"));			//������
				excelWorkBook.addCell(col++, row,CharTools.javaScriptEscape(tAnnualExamination.getHandler()));					//������
				excelWorkBook.addCell(col++, row,DateUtility.dateToStr(tAnnualExamination.getExpireDate()));					//��������
				excelWorkBook.addCell(col++, row,CharTools.javaScriptEscape(tAnnualExamination.getDemo()));						//��ע	
			}
			excelWorkBook.write();
			return null;
		}
		
		
		
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		Page<Object[]> list = annualExaminationService.listAnnualExamination(entCode, userId, page, limitint, startDate, endDate, searchValue);
		StringBuffer jsonSb = new StringBuffer();
	    String total = "";
		if (list != null && list.getResult()!=null) {
			total = "{total:"+list.getTotalCount()+",data:[";
	    	Iterator<Object[]> i = list.getResult().iterator();
	    	while(i.hasNext()){
	    		Object[] userObj = (Object[])i.next();
	    		Long id = (Long)userObj[0];
	    		String termName = (String)userObj[1];
	    		String vehicleNumber = (String)userObj[2];
	    		String simcard = (String)userObj[3];
	    		String groupName = (String)userObj[4];
	    		TAnnualExamination tAnnualExamination = (TAnnualExamination)userObj[5];
	    		jsonSb.append("{");
	    		jsonSb.append("id:'"+id+"',");
	    		jsonSb.append("deviceId:'"+CharTools.javaScriptEscape(tAnnualExamination.getDeviceId())+"',");
	    		jsonSb.append("termName:'"+CharTools.javaScriptEscape(termName)+"',");
	    		jsonSb.append("vehicleNumber:'"+CharTools.javaScriptEscape(vehicleNumber)+"',");
	    		jsonSb.append("simcard:'"+CharTools.javaScriptEscape(simcard)+"',");
	    		jsonSb.append("groupName:'"+CharTools.javaScriptEscape(groupName)+"',");
	    		jsonSb.append("examinationDate:'"+DateUtility.dateToStr(tAnnualExamination.getExaminationDate())+"',");
	    		jsonSb.append("project:'"+CharTools.javaScriptEscape(tAnnualExamination.getProject())+"',");
	    		jsonSb.append("condition:'"+CharTools.javaScriptEscape(tAnnualExamination.getCondition())+"',");
	    		jsonSb.append("expenses:'"+CharTools.killNullLong2String(tAnnualExamination.getExpenses(),"0")+"',");
	    		jsonSb.append("handler:'"+CharTools.javaScriptEscape(tAnnualExamination.getHandler())+"',");
	    		jsonSb.append("demo:'"+CharTools.javaScriptEscape(tAnnualExamination.getDemo())+"',");
	    		jsonSb.append("expireDate:'"+DateUtility.dateToStr(tAnnualExamination.getExpireDate())+"'");
	    		jsonSb.append("},");
	    	}
	    	if(jsonSb.length()>0){
	    		jsonSb.deleteCharAt(jsonSb.length()-1);
	    	}
	    	jsonSb.append("]}");
	    }
	    response.setContentType("text/json; charset=utf-8");
	    response.getWriter().write(total+jsonSb.toString());
		return mapping.findForward(null);
	}
	
	public ActionForward addAnnualExamination(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String deviceId = request.getParameter("deviceId");
		String examinationDate = request.getParameter("examinationDate");
		String project = request.getParameter("project");
		String condition = request.getParameter("condition");
		String expenses = request.getParameter("expenses");
		String handler = request.getParameter("handler");
		String expireDate = request.getParameter("expireDate");
		String demo = request.getParameter("demo");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		project =  CharTools.killNullString(project);
		project = java.net.URLDecoder.decode(project, "utf-8");
		project = CharTools.killNullString(project);
		
		condition =  CharTools.killNullString(condition);
		condition = java.net.URLDecoder.decode(condition, "utf-8");
		condition = CharTools.killNullString(condition);
		expenses = CharTools.killNullString(expenses);
		handler =  CharTools.killNullString(handler);
		handler = java.net.URLDecoder.decode(handler, "utf-8");
		handler = CharTools.killNullString(handler);
		demo =  CharTools.killNullString(demo);
		demo = java.net.URLDecoder.decode(demo, "utf-8");
		demo = CharTools.killNullString(demo);
		
		TAnnualExamination tAnnualExamination = new TAnnualExamination();
		tAnnualExamination.setDeviceId(deviceId);
		tAnnualExamination.setEmpCode(empCode);
		tAnnualExamination.setUserId(userId);
		tAnnualExamination.setCreateDate(new Date());
		tAnnualExamination.setChangeDate(new Date());
		tAnnualExamination.setExaminationDate(DateUtility.strToDate(examinationDate));
		tAnnualExamination.setProject(project);
		tAnnualExamination.setCondition(condition);
		tAnnualExamination.setExpenses(CharTools.str2Long(expenses,(long)0));
		tAnnualExamination.setHandler(handler);
		tAnnualExamination.setExpireDate(DateUtility.strToDate(expireDate));
		tAnnualExamination.setDemo(demo);
		
		annualExaminationService.save(tAnnualExamination);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.ANNUAL_EXAMINATION_SET);
		tOptLog.setFunCType(LogConstants.ANNUAL_EXAMINATION_SET_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("���������¼��ӳɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//�ɹ�
		return mapping.findForward(null);
	}
	
	public ActionForward updateAnnualExamination(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String id = request.getParameter("id");
		String deviceId = request.getParameter("deviceId");
		String examinationDate = request.getParameter("examinationDate");
		String project = request.getParameter("project");
		String condition = request.getParameter("condition");
		String expenses = request.getParameter("expenses");
		String handler = request.getParameter("handler");
		String expireDate = request.getParameter("expireDate");
		String demo = request.getParameter("demo");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		project =  CharTools.killNullString(project);
		project = java.net.URLDecoder.decode(project, "utf-8");
		project = CharTools.killNullString(project);
		
		condition =  CharTools.killNullString(condition);
		condition = java.net.URLDecoder.decode(condition, "utf-8");
		condition = CharTools.killNullString(condition);
		
		
		expenses = CharTools.killNullString(expenses);
		
		handler =  CharTools.killNullString(handler);
		handler = java.net.URLDecoder.decode(handler, "utf-8");
		handler = CharTools.killNullString(handler);
		
		demo =  CharTools.killNullString(demo);
		demo = java.net.URLDecoder.decode(demo, "utf-8");
		demo = CharTools.killNullString(demo);

		TAnnualExamination tAnnualExamination = new TAnnualExamination();
		tAnnualExamination.setId(Long.parseLong(id));
		tAnnualExamination.setDeviceId(deviceId);
		tAnnualExamination.setEmpCode(empCode);
		tAnnualExamination.setUserId(userId);
		tAnnualExamination.setChangeDate(new Date());
		tAnnualExamination.setExaminationDate(DateUtility.strToDate(examinationDate));
		tAnnualExamination.setProject(project);
		tAnnualExamination.setCondition(condition);
		tAnnualExamination.setExpenses(CharTools.str2Long(expenses,(long)0));
		tAnnualExamination.setHandler(handler);
		tAnnualExamination.setExpireDate(DateUtility.strToDate(expireDate));
		tAnnualExamination.setDemo(demo);
		
		annualExaminationService.update(tAnnualExamination);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.ANNUAL_EXAMINATION_SET);
		tOptLog.setFunCType(LogConstants.ANNUAL_EXAMINATION_SET_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("���������¼�޸ĳɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//�ɹ�
		return mapping.findForward(null);
	}
	
	public ActionForward delAnnualExamination(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		annualExaminationService.deleteAll(ids);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.ANNUAL_EXAMINATION_SET);
		tOptLog.setFunCType(LogConstants.ANNUAL_EXAMINATION_SET_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("���������¼ɾ���ɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//�ɹ�
		return mapping.findForward(null);
	}
}
