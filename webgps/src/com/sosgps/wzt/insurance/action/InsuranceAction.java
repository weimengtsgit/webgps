package com.sosgps.wzt.insurance.action;

import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.insurance.service.InsuranceService;
import com.sosgps.wzt.insurance.service.impl.InsuranceServiceImpl;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TInsurance;
import com.sosgps.wzt.orm.TOiling;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class InsuranceAction extends DispatchWebActionSupport {
	private InsuranceService insuranceService = (InsuranceServiceImpl) SpringHelper.getBean("InsuranceServiceImpl");

	public ActionForward listInsurance(ActionMapping mapping,
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
		searchValue = CharTools.killNullString(searchValue);
		// 从session中获取企业代码
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list =insuranceService.listInsurance(entCode,userId, 1, 65536, startDate, endDate, searchValue);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("车牌号", 15);
			excelWorkBook.addHeader("手机号", 15);
			excelWorkBook.addHeader("所属组", 15);
			excelWorkBook.addHeader("保险单号", 15);
			excelWorkBook.addHeader("保险名称", 15);
			excelWorkBook.addHeader("保险公司", 15);
			excelWorkBook.addHeader("投保日期", 15);
			excelWorkBook.addHeader("到期日期", 15);
			excelWorkBook.addHeader("保额", 15);
			excelWorkBook.addHeader("保费", 15);
			excelWorkBook.addHeader("经手人", 15);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				//String deviceId = (String) objects[0];
	    		String termName = (String)objects[1];
	    		String vehicleNumber = (String)objects[2];
	    		String simcard = (String)objects[3];
	    		String groupName = (String)objects[4];
	    		TInsurance insurance = (TInsurance)objects[5];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(termName));
//				excelWorkBook.addCell(col++, ++row, CharTools.killNullLong2String(visitCount, "0"));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(groupName));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(insurance.getInsuranceNo()));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(insurance.getInsuranceName()));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(insurance.getInsuranceCo()));
				excelWorkBook.addCell(col++, row, DateUtility.dateToStr(insurance.getInsuranceDate()));
				excelWorkBook.addCell(col++, row, DateUtility.dateToStr(insurance.getExpireDate()));
				excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(insurance.getSumInsured(), "0"));
				excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(insurance.getPremium(),"0"));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(insurance.getHandler()));
			}
			excelWorkBook.write();
			return null;
		}
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		Page<Object[]> list = insuranceService.listInsurance(entCode, userId, page, limitint, startDate, endDate, searchValue);
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
	    		TInsurance insurance = (TInsurance)userObj[5];
	    		jsonSb.append("{");
	    		jsonSb.append("id:'"+id+"',");
	    		jsonSb.append("deviceId:'"+CharTools.javaScriptEscape(insurance.getDeviceId())+"',");
	    		jsonSb.append("termName:'"+CharTools.javaScriptEscape(termName)+"',");
	    		jsonSb.append("vehicleNumber:'"+CharTools.javaScriptEscape(vehicleNumber)+"',");
	    		jsonSb.append("simcard:'"+CharTools.javaScriptEscape(simcard)+"',");
	    		jsonSb.append("groupName:'"+CharTools.javaScriptEscape(groupName)+"',");
	    		jsonSb.append("insuranceNo:'"+CharTools.javaScriptEscape(insurance.getInsuranceNo())+"',");
	    		jsonSb.append("insuranceName:'"+CharTools.javaScriptEscape(insurance.getInsuranceName())+"',");
	    		jsonSb.append("insuranceCo:'"+CharTools.javaScriptEscape(insurance.getInsuranceCo())+"',");
	    		jsonSb.append("insuranceDate:'"+DateUtility.dateToStr(insurance.getInsuranceDate())+"',");
	    		jsonSb.append("expireDate:'"+DateUtility.dateToStr(insurance.getExpireDate())+"',");
	    		jsonSb.append("sumInsured:'"+CharTools.killNullLong2String(insurance.getSumInsured(), "0")+"',");
	    		jsonSb.append("premium:'"+CharTools.killNullLong2String(insurance.getPremium(),"0")+"',");
	    		jsonSb.append("handler:'"+CharTools.javaScriptEscape(insurance.getHandler())+"'");
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
	
	public ActionForward addInsurance(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String deviceId = request.getParameter("deviceId");
		String insuranceNo = request.getParameter("insuranceNo");
		String insuranceName = request.getParameter("insuranceName");
		String insuranceCo = request.getParameter("insuranceCo");
		String insuranceDate = request.getParameter("insuranceDate");
		String expireDate = request.getParameter("expireDate");
		String sumInsured = request.getParameter("sumInsured");
		String premium = request.getParameter("premium");
		String handler = request.getParameter("handler");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		
		deviceId = CharTools.killNullString(deviceId);
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		deviceId = CharTools.killNullString(deviceId);
		
		
		insuranceNo = CharTools.killNullString(insuranceNo);
		insuranceNo = java.net.URLDecoder.decode(insuranceNo, "utf-8");
		insuranceNo = CharTools.killNullString(insuranceNo);
		
		insuranceName = CharTools.killNullString(insuranceName);
		insuranceName = java.net.URLDecoder.decode(insuranceName, "utf-8");
		insuranceName = CharTools.killNullString(insuranceName);
		
		insuranceCo = CharTools.killNullString(insuranceCo);
		insuranceCo = java.net.URLDecoder.decode(insuranceCo, "utf-8");
		insuranceCo = CharTools.killNullString(insuranceCo);
		
		handler = CharTools.killNullString(handler);
		handler = java.net.URLDecoder.decode(handler, "utf-8");
		handler = CharTools.killNullString(handler);
		
		TInsurance tInsurance = new TInsurance();
		tInsurance.setDeviceId(deviceId);
		tInsurance.setInsuranceNo(insuranceNo);
		tInsurance.setInsuranceName(insuranceName);
		tInsurance.setInsuranceCo(insuranceCo);
		tInsurance.setInsuranceDate(DateUtility.strToDate(insuranceDate));
		tInsurance.setExpireDate(DateUtility.strToDate(expireDate));
		tInsurance.setEmpCode(empCode);
		tInsurance.setUserId(userId);
		tInsurance.setSumInsured(Long.parseLong(sumInsured));
		tInsurance.setPremium(Long.parseLong(premium));
		tInsurance.setHandler(handler);
		tInsurance.setCreateDate(new Date());
		tInsurance.setChangeDate(new Date());
		insuranceService.save(tInsurance);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.INSURANCE_SET);
		tOptLog.setFunCType(LogConstants.INSURANCE_SET_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("保险记录添加成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward updateInsurance(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String id = request.getParameter("id");
		String deviceId = request.getParameter("deviceId");
		String insuranceNo = request.getParameter("insuranceNo");
		String insuranceName = request.getParameter("insuranceName");
		String insuranceCo = request.getParameter("insuranceCo");
		String insuranceDate = request.getParameter("insuranceDate");
		String expireDate = request.getParameter("expireDate");
		String sumInsured = request.getParameter("sumInsured");
		String premium = request.getParameter("premium");
		String handler = request.getParameter("handler");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		insuranceNo = CharTools.killNullString(insuranceNo);
		insuranceNo = java.net.URLDecoder.decode(insuranceNo, "utf-8");
		insuranceNo = CharTools.killNullString(insuranceNo);
		
		insuranceName = CharTools.killNullString(insuranceName);
		insuranceName = java.net.URLDecoder.decode(insuranceName, "utf-8");
		insuranceName = CharTools.killNullString(insuranceName);
		
		insuranceCo = CharTools.killNullString(insuranceCo);
		insuranceCo = java.net.URLDecoder.decode(insuranceCo, "utf-8");
		insuranceCo = CharTools.killNullString(insuranceCo);
		
		handler = CharTools.killNullString(handler);
		handler = java.net.URLDecoder.decode(handler, "utf-8");
		handler = CharTools.killNullString(handler);
		
		TInsurance tInsurance = new TInsurance();
		tInsurance.setId(Long.parseLong(id));
		tInsurance.setDeviceId(deviceId);
		tInsurance.setInsuranceNo(insuranceNo);
		tInsurance.setInsuranceName(insuranceName);
		tInsurance.setInsuranceCo(insuranceCo);
		tInsurance.setInsuranceDate(DateUtility.strToDate(insuranceDate));
		tInsurance.setExpireDate(DateUtility.strToDate(expireDate));
		tInsurance.setEmpCode(empCode);
		tInsurance.setUserId(userId);
		tInsurance.setSumInsured(Long.parseLong(sumInsured));
		tInsurance.setPremium(Long.parseLong(premium));
		tInsurance.setHandler(handler);
		tInsurance.setChangeDate(new Date());
		insuranceService.update(tInsurance);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.INSURANCE_SET);
		tOptLog.setFunCType(LogConstants.INSURANCE_SET_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("保险记录修改成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward delInsurance(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		insuranceService.deleteAll(ids);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.INSURANCE_SET);
		tOptLog.setFunCType(LogConstants.INSURANCE_SET_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("保险记录删除成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
}
