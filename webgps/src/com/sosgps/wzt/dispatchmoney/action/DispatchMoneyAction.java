package com.sosgps.wzt.dispatchmoney.action;

import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.dispatchmoney.service.impl.DispatchMoneyServiceImpl;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.insurance.service.InsuranceService;
import com.sosgps.wzt.insurance.service.impl.InsuranceServiceImpl;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDispatchMoney;
import com.sosgps.wzt.orm.TInsurance;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TVehiclesMaintenance;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.vehiclesMaintenance.service.VehiclesMaintenanceService;
import com.sosgps.wzt.vehiclesMaintenance.service.impl.VehiclesMaintenanceServiceImpl;

public class DispatchMoneyAction extends DispatchWebActionSupport {
	private DispatchMoneyServiceImpl dispatchMoneyService = (DispatchMoneyServiceImpl) SpringHelper.getBean("DispatchMoneyServiceImpl");

	public ActionForward listdispatchCondition(ActionMapping mapping,
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
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		Page<Object[]> list = dispatchMoneyService.listdispatchCondition(entCode, userId, page, limitint, startDate, endDate, searchValue);
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
	    		TDispatchMoney tDispatchMoney = (TDispatchMoney)userObj[5];
	    		jsonSb.append("{");
	    		jsonSb.append("id:'"+id+"',");
	    		jsonSb.append("deviceId:'"+CharTools.javaScriptEscape(tDispatchMoney.getDeviceId())+"',");
	    		jsonSb.append("termName:'"+CharTools.javaScriptEscape(termName)+"',");
	    		jsonSb.append("vehicleNumber:'"+CharTools.javaScriptEscape(vehicleNumber)+"',");
	    		jsonSb.append("simcard:'"+CharTools.javaScriptEscape(simcard)+"',");
	    		jsonSb.append("groupName:'"+CharTools.javaScriptEscape(groupName)+"',");
	    		jsonSb.append("dispatchDate:'"+DateUtility.dateToStr(tDispatchMoney.getDispatchDate())+"',");
	    		jsonSb.append("dispatchamount:'"+CharTools.killNullLong2String(tDispatchMoney.getDispatchAmount(),"0")+"',");
	    		jsonSb.append("frmhandler:'"+CharTools.javaScriptEscape(tDispatchMoney.getFrmhandler())+"',");
	    		jsonSb.append("demo:'"+CharTools.javaScriptEscape(tDispatchMoney.getFrmdemo())+"'");
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
	
	public ActionForward addDispatchMoney(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String deviceId = request.getParameter("tmpdeviceId");
		String dispatchDate = request.getParameter("dispatchDate");
		String dispatchAmount = request.getParameter("dispatchAmount");
		String frmhandler = request.getParameter("frmhandler");
		String frmdemo = request.getParameter("frmdemo");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		deviceId = CharTools.killNullString(deviceId);
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		deviceId = CharTools.killNullString(deviceId);
		
		dispatchAmount = CharTools.killNullString(dispatchAmount);
		dispatchAmount = java.net.URLDecoder.decode(dispatchAmount, "utf-8");
		dispatchAmount = CharTools.killNullString(dispatchAmount);
		
		frmhandler = CharTools.killNullString(frmhandler);
		frmhandler = java.net.URLDecoder.decode(frmhandler, "utf-8");
		frmhandler = CharTools.killNullString(frmhandler);
		
		
		frmdemo = CharTools.killNullString(frmdemo);
		frmdemo = java.net.URLDecoder.decode(frmdemo, "utf-8");
		frmdemo = CharTools.killNullString(frmdemo);
		
		
		TDispatchMoney tDispatchMoney = new TDispatchMoney();
		tDispatchMoney.setUserId(userId);
		tDispatchMoney.setDeviceId(deviceId);
		tDispatchMoney.setDispatchDate(DateUtility.strToDate(dispatchDate));
		tDispatchMoney.setDispatchAmount(CharTools.str2Long(dispatchAmount,(long)0));
		tDispatchMoney.setFrmdemo(frmdemo);
		tDispatchMoney.setFrmhandler(frmhandler);
		tDispatchMoney.setSaveDate(new Date());
		dispatchMoneyService.save(tDispatchMoney);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.VEHICLES_MAINTENANCE_SET);
		tOptLog.setFunCType(LogConstants.VEHICLES_MAINTENANCE_SET_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("车辆配送金额添加成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward updateDispatchMoney(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String id = request.getParameter("id");
		String deviceId = request.getParameter("tmpdeviceId");
		String dispatchDate = request.getParameter("dispatchDate");
		String dispatchAmount = request.getParameter("dispatchAmount");
		String frmhandler = request.getParameter("frmhandler");
		String frmdemo = request.getParameter("frmdemo");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		deviceId = CharTools.killNullString(deviceId);
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		deviceId = CharTools.killNullString(deviceId);
		
		dispatchAmount = CharTools.killNullString(dispatchAmount);
		dispatchAmount = java.net.URLDecoder.decode(dispatchAmount, "utf-8");
		dispatchAmount = CharTools.killNullString(dispatchAmount);
		
		frmhandler = CharTools.killNullString(frmhandler);
		frmhandler = java.net.URLDecoder.decode(frmhandler, "utf-8");
		frmhandler = CharTools.killNullString(frmhandler);
		
		
		frmdemo = CharTools.killNullString(frmdemo);
		frmdemo = java.net.URLDecoder.decode(frmdemo, "utf-8");
		frmdemo = CharTools.killNullString(frmdemo);
		
		
		TDispatchMoney tDispatchMoney = new TDispatchMoney();
		tDispatchMoney.setId(Long.parseLong(id));
		tDispatchMoney.setUserId(userId);
		tDispatchMoney.setDeviceId(deviceId);
		tDispatchMoney.setDispatchDate(DateUtility.strToDate(dispatchDate));
		tDispatchMoney.setDispatchAmount(CharTools.str2Long(dispatchAmount,(long)0));
		tDispatchMoney.setFrmdemo(frmdemo);
		tDispatchMoney.setFrmhandler(frmhandler);
		tDispatchMoney.setSaveDate(new Date());
		dispatchMoneyService.update(tDispatchMoney);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.VEHICLES_MAINTENANCE_SET);
		tOptLog.setFunCType(LogConstants.VEHICLES_MAINTENANCE_SET_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("车辆配送金额修改成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward delDispatchMoney(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		dispatchMoneyService.deleteAll(ids);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.VEHICLES_MAINTENANCE_SET);
		tOptLog.setFunCType(LogConstants.VEHICLES_MAINTENANCE_SET_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("车辆维护记录删除成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
}
