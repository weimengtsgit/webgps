package com.sosgps.wzt.driverLicense.action;

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
import com.sosgps.wzt.driverLicense.service.DriverLicenseService;
import com.sosgps.wzt.insurance.service.InsuranceService;
import com.sosgps.wzt.insurance.service.impl.InsuranceServiceImpl;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAnnualExamination;
import com.sosgps.wzt.orm.TDriverLicense;
import com.sosgps.wzt.orm.TInsurance;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class DriverLicenseAction extends DispatchWebActionSupport {
	private DriverLicenseService driverLicenseService = (DriverLicenseService) SpringHelper.getBean("DriverLicenseServiceImpl");

	public ActionForward listDriverLicense(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd HH:mm:ss
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// 从session中获取企业代码
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		Page<Object[]> list = driverLicenseService.listDriverLicense(entCode, userId, page, limitint, startDate, endDate, searchValue);
		StringBuffer jsonSb = new StringBuffer();
	    String total = "";
		if (list != null && list.getResult()!=null) {
			total = "{total:"+list.getTotalCount()+",data:[";
	    	Iterator<Object[]> i = list.getResult().iterator();
	    	while(i.hasNext()){
	    		Object userObj = (Object)i.next();
	    		TDriverLicense tDriverLicense = (TDriverLicense)userObj;
	    		jsonSb.append("{");
	    		jsonSb.append("id:'"+tDriverLicense.getId()+"',");
	    		jsonSb.append("employeeNum:'"+CharTools.javaScriptEscape(tDriverLicense.getEmployeeNum())+"',");
	    		jsonSb.append("employeeWorknum:'"+CharTools.javaScriptEscape(tDriverLicense.getEmployeeWorknum())+"',");
	    		jsonSb.append("employeeName:'"+CharTools.javaScriptEscape(tDriverLicense.getEmployeeName())+"',");
	    		jsonSb.append("employeeId:'"+CharTools.javaScriptEscape(tDriverLicense.getEmployeeId())+"',");
	    		jsonSb.append("department:'"+CharTools.javaScriptEscape(tDriverLicense.getDepartment())+"',");
	    		jsonSb.append("examineDate:'"+DateUtility.dateToStr(tDriverLicense.getExaminationDate())+"',");
	    		jsonSb.append("expireDate:'"+DateUtility.dateToStr(tDriverLicense.getExpireDate())+"',");
	    		jsonSb.append("condition:'"+CharTools.javaScriptEscape(tDriverLicense.getCondition())+"',");
	    		jsonSb.append("demo:'"+CharTools.javaScriptEscape(tDriverLicense.getDemo())+"'");
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
	
	public ActionForward addDriverLicense(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String employeeNum = request.getParameter("employeeNum");
		String employeeWorknum = request.getParameter("employeeWorknum");
		String employeeName = request.getParameter("employeeName");
		String employeeId = request.getParameter("employeeId");
		String department = request.getParameter("department");
		String examinationDate = request.getParameter("examineDate");
		String expireDate = request.getParameter("expireDate");
		String condition = request.getParameter("condition");
		String demo = request.getParameter("demo");

		employeeNum = java.net.URLDecoder.decode(employeeNum, "utf-8");
		employeeNum = CharTools.javaScriptEscape(employeeNum);
		employeeWorknum = java.net.URLDecoder.decode(employeeWorknum, "utf-8");
		employeeWorknum = CharTools.javaScriptEscape(employeeWorknum);
		employeeName = java.net.URLDecoder.decode(employeeName, "utf-8");
		employeeName = CharTools.javaScriptEscape(employeeName);
		employeeId = java.net.URLDecoder.decode(employeeId, "utf-8");
		employeeId = CharTools.javaScriptEscape(employeeId);
		department = java.net.URLDecoder.decode(department, "utf-8");
		department = CharTools.javaScriptEscape(department);
		
		condition = java.net.URLDecoder.decode(condition, "utf-8");
		condition = CharTools.javaScriptEscape(condition);
		demo = java.net.URLDecoder.decode(demo, "utf-8");
		demo = CharTools.javaScriptEscape(demo);
		
		TDriverLicense tDriverLicense = new TDriverLicense();
		tDriverLicense.setEmpCode(empCode);
		tDriverLicense.setUserId(userId);
		tDriverLicense.setCreateDate(new Date());
		tDriverLicense.setChangeDate(new Date());
		tDriverLicense.setEmployeeNum(employeeNum);
		tDriverLicense.setEmployeeWorknum(employeeWorknum);
		tDriverLicense.setEmployeeId(employeeId);
		tDriverLicense.setEmployeeName(employeeName);
		tDriverLicense.setDepartment(department);
		tDriverLicense.setExaminationDate(DateUtility.strToDate(examinationDate));
		tDriverLicense.setExpireDate(DateUtility.strToDate(expireDate));
		tDriverLicense.setCondition(condition);
		tDriverLicense.setDemo(demo);
		driverLicenseService.save(tDriverLicense);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.DriverLicense_SET);
		tOptLog.setFunCType(LogConstants.DriverLicense_SET_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("驾照年审记录添加成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward updateDriverLicense(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String id = request.getParameter("id");
		String employeeNum = request.getParameter("employeeNum");
		String employeeWorknum = request.getParameter("employeeWorknum");
		String employeeName = request.getParameter("employeeName");
		String employeeId = request.getParameter("employeeId");
		String department = request.getParameter("department");
		String examinationDate = request.getParameter("examineDate");
		String expireDate = request.getParameter("expireDate");
		String condition = request.getParameter("condition");
		String demo = request.getParameter("demo");

		employeeNum = java.net.URLDecoder.decode(employeeNum, "utf-8");
		employeeNum = CharTools.javaScriptEscape(employeeNum);
		employeeWorknum = java.net.URLDecoder.decode(employeeWorknum, "utf-8");
		employeeWorknum = CharTools.javaScriptEscape(employeeWorknum);
		employeeName = java.net.URLDecoder.decode(employeeName, "utf-8");
		employeeName = CharTools.javaScriptEscape(employeeName);
		employeeId = java.net.URLDecoder.decode(employeeId, "utf-8");
		employeeId = CharTools.javaScriptEscape(employeeId);
		department = java.net.URLDecoder.decode(department, "utf-8");
		department = CharTools.javaScriptEscape(department);
		condition = java.net.URLDecoder.decode(condition, "utf-8");
		condition = CharTools.javaScriptEscape(condition);
		demo = java.net.URLDecoder.decode(demo, "utf-8");
		demo = CharTools.javaScriptEscape(demo);

		TDriverLicense tDriverLicense = new TDriverLicense();
		tDriverLicense.setId(Long.parseLong(id));
		tDriverLicense.setEmpCode(empCode);
		tDriverLicense.setUserId(userId);
		tDriverLicense.setCreateDate(new Date());
		tDriverLicense.setChangeDate(new Date());
		tDriverLicense.setEmployeeNum(employeeNum);
		tDriverLicense.setEmployeeWorknum(employeeWorknum);
		tDriverLicense.setEmployeeId(employeeId);
		tDriverLicense.setEmployeeName(employeeName);
		tDriverLicense.setDepartment(department);
		tDriverLicense.setExaminationDate(DateUtility.strToDate(examinationDate));
		tDriverLicense.setExpireDate(DateUtility.strToDate(expireDate));
		tDriverLicense.setCondition(condition);
		tDriverLicense.setDemo(demo);
		
		driverLicenseService.update(tDriverLicense);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.DriverLicense_SET);
		tOptLog.setFunCType(LogConstants.DriverLicense_SET_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("驾照年审记录修改成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward delDriverLicense(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		driverLicenseService.deleteAll(ids);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.DriverLicense_SET);
		tOptLog.setFunCType(LogConstants.DriverLicense_SET_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("驾照年审记录删除成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
}
