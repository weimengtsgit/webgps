package com.sosgps.wzt.vehiclenorm.action;

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
import com.sosgps.wzt.orm.TVehicleNorm;
import com.sosgps.wzt.orm.TVehiclesMaintenance;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.vehiclenorm.service.VehicleNormService;
import com.sosgps.wzt.vehiclenorm.service.impl.VehicleNormServiceImpl;
import com.sosgps.wzt.vehiclesMaintenance.service.VehiclesMaintenanceService;
import com.sosgps.wzt.vehiclesMaintenance.service.impl.VehiclesMaintenanceServiceImpl;

public class VehicleNormAction extends DispatchWebActionSupport {
	private VehicleNormService vehicleNormService = (VehicleNormServiceImpl) SpringHelper.getBean("vehicleNormImpl");
	public ActionForward addVehicleNorm(ActionMapping mapping,
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
		String mileagenorm = request.getParameter("mileagenorm");
		String expensenorm = request.getParameter("expensenorm");
		String returnnorm = request.getParameter("returnnorm");
		String frmhandler = request.getParameter("frmhandler");
		String frmdemo = request.getParameter("frmdemo");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		deviceId = CharTools.killNullString(deviceId);
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		deviceId = CharTools.killNullString(deviceId);
		
		mileagenorm = CharTools.killNullString(mileagenorm);
		mileagenorm = java.net.URLDecoder.decode(mileagenorm, "utf-8");
		mileagenorm = CharTools.killNullString(mileagenorm);
		
		expensenorm = CharTools.killNullString(expensenorm);
		expensenorm = java.net.URLDecoder.decode(expensenorm, "utf-8");
		expensenorm = CharTools.killNullString(expensenorm);
		
		returnnorm = CharTools.killNullString(returnnorm);
		returnnorm = java.net.URLDecoder.decode(returnnorm, "utf-8");
		returnnorm = CharTools.killNullString(returnnorm);
		
		frmhandler = CharTools.killNullString(frmhandler);
		frmhandler = java.net.URLDecoder.decode(frmhandler, "utf-8");
		frmhandler = CharTools.killNullString(frmhandler);
		
		frmdemo = CharTools.killNullString(frmdemo);
		frmdemo = java.net.URLDecoder.decode(frmdemo, "utf-8");
		frmdemo = CharTools.killNullString(frmdemo);
		
		
		TVehicleNorm tVehicleNorm = new TVehicleNorm();
		tVehicleNorm.setUserId(userId);
		tVehicleNorm.setDeviceId(deviceId);
		tVehicleNorm.setMileageNorm(CharTools.str2Long(mileagenorm,(long)0));
		tVehicleNorm.setExpenseNorm(CharTools.str2Long(expensenorm,(long)0));
		tVehicleNorm.setReturnNorm(CharTools.str2Long(returnnorm,(long)0));
		tVehicleNorm.setDemo(frmdemo);
		tVehicleNorm.setCreateMan(frmhandler);
		tVehicleNorm.setSaveDate(new Date());
		vehicleNormService.save(tVehicleNorm);
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
		tOptLog.setOptDesc("车辆附加标准信息添加成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	
	public ActionForward listVehicleNorm(ActionMapping mapping,
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
		Page<Object[]> list = vehicleNormService.listVehicleNorm(entCode, userId, page, limitint, startDate, endDate, searchValue);
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
	    		TVehicleNorm tVehicleNorm = (TVehicleNorm)userObj[5];
	    		jsonSb.append("{");
	    		jsonSb.append("id:'"+id+"',");
	    		jsonSb.append("deviceId:'"+CharTools.javaScriptEscape(tVehicleNorm.getDeviceId())+"',");
	    		jsonSb.append("termName:'"+CharTools.javaScriptEscape(termName)+"',");
	    		jsonSb.append("vehicleNumber:'"+CharTools.javaScriptEscape(vehicleNumber)+"',");
	    		jsonSb.append("simcard:'"+CharTools.javaScriptEscape(simcard)+"',");
	    		jsonSb.append("groupName:'"+CharTools.javaScriptEscape(groupName)+"',");
	    		jsonSb.append("mileageNorm:'"+CharTools.killNullLong2String(tVehicleNorm.getMileageNorm(),"0")+"',");
	    		jsonSb.append("expenseNorm:'"+CharTools.killNullLong2String(tVehicleNorm.getExpenseNorm(),"0")+"',");
	    		jsonSb.append("returnNorm:'"+CharTools.killNullLong2String(tVehicleNorm.getReturnNorm(),"0")+"',");
	    		jsonSb.append("createMan:'"+CharTools.javaScriptEscape(tVehicleNorm.getCreateMan())+"',");
	    		jsonSb.append("saveDate:'"+DateUtility.dateToStr(tVehicleNorm.getSaveDate())+"',");
	    		jsonSb.append("demo:'"+CharTools.javaScriptEscape(tVehicleNorm.getDemo())+"'");
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
	
	public ActionForward updateVehicleNorm(ActionMapping mapping,
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
		String createDate = request.getParameter("createDate");
		String mileagenorm = request.getParameter("mileagenorm");
		String expensenorm = request.getParameter("expensenorm");
		String returnnorm = request.getParameter("returnnorm");
		String frmhandler = request.getParameter("frmhandler");
		String frmdemo = request.getParameter("frmdemo");
		if(deviceId == null || deviceId.length() <= 0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		deviceId = CharTools.killNullString(deviceId);
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		deviceId = CharTools.killNullString(deviceId);
		
		mileagenorm = CharTools.killNullString(mileagenorm);
		mileagenorm = java.net.URLDecoder.decode(mileagenorm, "utf-8");
		mileagenorm = CharTools.killNullString(mileagenorm);
		
		expensenorm = CharTools.killNullString(expensenorm);
		expensenorm = java.net.URLDecoder.decode(expensenorm, "utf-8");
		expensenorm = CharTools.killNullString(expensenorm);
		
		returnnorm = CharTools.killNullString(returnnorm);
		returnnorm = java.net.URLDecoder.decode(returnnorm, "utf-8");
		returnnorm = CharTools.killNullString(returnnorm);
		
		frmhandler = CharTools.killNullString(frmhandler);
		frmhandler = java.net.URLDecoder.decode(frmhandler, "utf-8");
		frmhandler = CharTools.killNullString(frmhandler);
		
		frmdemo = CharTools.killNullString(frmdemo);
		frmdemo = java.net.URLDecoder.decode(frmdemo, "utf-8");
		frmdemo = CharTools.killNullString(frmdemo);
		
		
		TVehicleNorm tVehicleNorm = new TVehicleNorm();
		tVehicleNorm.setUserId(userId);
		String id = request.getParameter("id");
		tVehicleNorm.setDeviceId(deviceId);
		tVehicleNorm.setMileageNorm(CharTools.str2Long(mileagenorm,(long)0));
		tVehicleNorm.setExpenseNorm(CharTools.str2Long(expensenorm,(long)0));
		tVehicleNorm.setReturnNorm(CharTools.str2Long(returnnorm,(long)0));
		tVehicleNorm.setSaveDate(DateUtility.strToDate(createDate));
		tVehicleNorm.setDemo(frmdemo);
		tVehicleNorm.setCreateMan(frmhandler);
		tVehicleNorm.setSaveDate(new Date());
		tVehicleNorm.setId(Long.parseLong(id));
		vehicleNormService.update(tVehicleNorm);
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
		tOptLog.setOptDesc("车辆附加标准信息修改成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward delVehicleNorm(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		vehicleNormService.deleteAll(ids);
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
		tOptLog.setOptDesc("车辆附加标准信息删除成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
}
