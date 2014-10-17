package com.sosgps.wzt.vehiclesMaintenance.action;

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
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TVehiclesMaintenance;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.vehiclesMaintenance.service.VehiclesMaintenanceService;
import com.sosgps.wzt.vehiclesMaintenance.service.impl.VehiclesMaintenanceServiceImpl;

public class VehiclesMaintenanceAction extends DispatchWebActionSupport {
	private VehiclesMaintenanceService vehiclesMaintenanceService = (VehiclesMaintenanceServiceImpl) SpringHelper.getBean("VehiclesMaintenanceServiceImpl");

	public ActionForward listVehiclesMaintenance(ActionMapping mapping,
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
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list =vehiclesMaintenanceService.listVehiclesMaintenance(entCode,userId, 1, 65536, startDate, endDate, searchValue);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("车牌号", 15);
			excelWorkBook.addHeader("手机号", 15);
			excelWorkBook.addHeader("所属组", 15);
			excelWorkBook.addHeader("维护日期", 15);
			excelWorkBook.addHeader("维护费用", 15);
			excelWorkBook.addHeader("维护情况", 15);
			excelWorkBook.addHeader("经手人", 15);
			excelWorkBook.addHeader("到期日期", 15);
			excelWorkBook.addHeader("备注", 15);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				//String deviceId = (String) objects[0];
	    		String termName = (String)objects[1];
	    		String vehicleNumber = (String)objects[2];
	    		String simcard = (String)objects[3];
	    		String groupName = (String)objects[4];
	    		TVehiclesMaintenance tVehiclesMaintenance = (TVehiclesMaintenance)objects[5];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(termName));
//				excelWorkBook.addCell(col++, ++row, CharTools.killNullLong2String(visitCount, "0"));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(groupName));
				excelWorkBook.addCell(col++, row, DateUtility.dateToStr(tVehiclesMaintenance.getExpireDate()));
				excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(tVehiclesMaintenance.getExpenses(),"0"));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(tVehiclesMaintenance.getCondition()));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(tVehiclesMaintenance.getHandler()));
				excelWorkBook.addCell(col++, row, DateUtility.dateToStr(tVehiclesMaintenance.getMaintenanceDate()));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(tVehiclesMaintenance.getDemo()));
			}
			excelWorkBook.write();
			return null;
		}
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		Page<Object[]> list = vehiclesMaintenanceService.listVehiclesMaintenance(entCode, userId, page, limitint, startDate, endDate, searchValue);
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
	    		TVehiclesMaintenance tVehiclesMaintenance = (TVehiclesMaintenance)userObj[5];
	    		jsonSb.append("{");
	    		jsonSb.append("id:'"+id+"',");
	    		jsonSb.append("deviceId:'"+CharTools.javaScriptEscape(tVehiclesMaintenance.getDeviceId())+"',");
	    		jsonSb.append("termName:'"+CharTools.javaScriptEscape(termName)+"',");
	    		jsonSb.append("vehicleNumber:'"+CharTools.javaScriptEscape(vehicleNumber)+"',");
	    		jsonSb.append("simcard:'"+CharTools.javaScriptEscape(simcard)+"',");
	    		jsonSb.append("groupName:'"+CharTools.javaScriptEscape(groupName)+"',");
	    		jsonSb.append("expireDate:'"+DateUtility.dateToStr(tVehiclesMaintenance.getExpireDate())+"',");
	    		jsonSb.append("expenses:'"+CharTools.killNullLong2String(tVehiclesMaintenance.getExpenses(),"0")+"',");
	    		jsonSb.append("condition:'"+CharTools.javaScriptEscape(tVehiclesMaintenance.getCondition())+"',");
	    		jsonSb.append("handler:'"+CharTools.javaScriptEscape(tVehiclesMaintenance.getHandler())+"',");
	    		jsonSb.append("demo:'"+CharTools.javaScriptEscape(tVehiclesMaintenance.getDemo())+"',");
	    		jsonSb.append("maintenanceDate:'"+DateUtility.dateToStr(tVehiclesMaintenance.getMaintenanceDate())+"'");
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
	
	public ActionForward addVehiclesMaintenance(ActionMapping mapping,
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
		String maintenanceDate = request.getParameter("maintenanceDate");
		String expenses = request.getParameter("expenses");
		String condition = request.getParameter("condition");
		String handler = request.getParameter("handler");
		String expireDate = request.getParameter("expireDate");
		String demo = request.getParameter("demo");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		deviceId = CharTools.killNullString(deviceId);
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		deviceId = CharTools.killNullString(deviceId);
		
		condition = CharTools.killNullString(condition);
		condition = java.net.URLDecoder.decode(condition, "utf-8");
		condition = CharTools.killNullString(condition);
		
		demo = CharTools.killNullString(demo);
		demo = java.net.URLDecoder.decode(demo, "utf-8");
		demo = CharTools.killNullString(demo);
		
		
		handler = CharTools.killNullString(handler);
		handler = java.net.URLDecoder.decode(handler, "utf-8");
		handler = CharTools.killNullString(handler);
		
		expenses = CharTools.killNullString(expenses);
		
		TVehiclesMaintenance tVehiclesMaintenance = new TVehiclesMaintenance();
		tVehiclesMaintenance.setDeviceId(deviceId);
		tVehiclesMaintenance.setEmpCode(empCode);
		tVehiclesMaintenance.setUserId(userId);
		tVehiclesMaintenance.setCreateDate(new Date());
		tVehiclesMaintenance.setChangeDate(new Date());
		tVehiclesMaintenance.setMaintenanceDate(DateUtility.strToDate(maintenanceDate));
		tVehiclesMaintenance.setExpenses(CharTools.str2Long(expenses,(long)0));
		tVehiclesMaintenance.setExpireDate(DateUtility.strToDate(expireDate));
		tVehiclesMaintenance.setCondition(condition);
		tVehiclesMaintenance.setHandler(handler);
		tVehiclesMaintenance.setDemo(demo);
		vehiclesMaintenanceService.save(tVehiclesMaintenance);
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
		tOptLog.setOptDesc("车辆维护记录添加成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward updateVehiclesMaintenance(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String id = request.getParameter("id");
		String deviceId = request.getParameter("deviceId");
		String maintenanceDate = request.getParameter("maintenanceDate");
		String expenses = request.getParameter("expenses");
		String condition = request.getParameter("condition");
		String handler = request.getParameter("handler");
		String expireDate = request.getParameter("expireDate");
		String demo = request.getParameter("demo");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		condition = CharTools.killNullString(condition);
		condition = java.net.URLDecoder.decode(condition, "utf-8");
		condition = CharTools.killNullString(condition);
		
		demo = CharTools.killNullString(demo);
		demo = java.net.URLDecoder.decode(demo, "utf-8");
		demo = CharTools.killNullString(demo);
		
		handler = CharTools.killNullString(handler);
		handler = java.net.URLDecoder.decode(handler, "utf-8");
		handler = CharTools.killNullString(handler);
		expenses = CharTools.killNullString(expenses);
		
		TVehiclesMaintenance tVehiclesMaintenance = new TVehiclesMaintenance();
		tVehiclesMaintenance.setId(Long.parseLong(id));
		tVehiclesMaintenance.setDeviceId(deviceId);
		tVehiclesMaintenance.setEmpCode(empCode);
		tVehiclesMaintenance.setUserId(userId);
		tVehiclesMaintenance.setMaintenanceDate(DateUtility.strToDate(maintenanceDate));
		tVehiclesMaintenance.setExpenses(CharTools.str2Long(expenses,(long)0));
		tVehiclesMaintenance.setExpireDate(DateUtility.strToDate(expireDate));
		tVehiclesMaintenance.setCondition(condition);
		tVehiclesMaintenance.setHandler(handler);
		tVehiclesMaintenance.setDemo(demo);
		tVehiclesMaintenance.setChangeDate(new Date());
		vehiclesMaintenanceService.update(tVehiclesMaintenance);
		
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.VEHICLES_MAINTENANCE_SET);
		tOptLog.setFunCType(LogConstants.VEHICLES_MAINTENANCE_SET_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("车辆维护记录修改成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward delVehiclesMaintenance(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		vehiclesMaintenanceService.deleteAll(ids);
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
