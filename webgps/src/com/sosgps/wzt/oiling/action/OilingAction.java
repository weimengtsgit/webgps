package com.sosgps.wzt.oiling.action;

import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.mapabc.geom.CoordCvtAPI;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.oiling.service.OilingService;
import com.sosgps.wzt.oiling.service.impl.OilingServiceImpl;
import com.sosgps.wzt.orm.TOiling;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TVisitTj;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class OilingAction extends DispatchWebActionSupport {
	private OilingService oilingService = (OilingServiceImpl) SpringHelper.getBean("OilingServiceImpl");

	public ActionForward listOiling(ActionMapping mapping,
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
			Page<Object[]> list =oilingService.listOiling(entCode,userId, 1, 65536, startDate, endDate, searchValue);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("车牌号", 15);
			excelWorkBook.addHeader("手机号", 15);
			excelWorkBook.addHeader("所属组", 15);
			excelWorkBook.addHeader("加油量(升)", 15);
			excelWorkBook.addHeader("加油金额", 15);
			excelWorkBook.addHeader("加油日期", 15);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				//String deviceId = (String) objects[0];
	    		String termName = (String)objects[1];
	    		String vehicleNumber = (String)objects[2];
	    		String simcard = (String)objects[3];
	    		String groupName = (String)objects[4];
	    		TOiling oiling = (TOiling)objects[5];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(termName));
//				excelWorkBook.addCell(col++, ++row, CharTools.killNullLong2String(visitCount, "0"));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(vehicleNumber));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(simcard));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(groupName));
				excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(oiling.getOilLiter(), "0"));
				excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(oiling.getOilCost(), "0"));
				excelWorkBook.addCell(col++, row, DateUtility.dateToStr(oiling.getCreateDate()));
			}
			excelWorkBook.write();
			return null;
		}
		
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		Page<Object[]> list = oilingService.listOiling(entCode, userId, page, limitint, startDate, endDate, searchValue);
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
	    		TOiling oiling = (TOiling)userObj[5];
	    		jsonSb.append("{");
	    		jsonSb.append("id:'"+id+"',");
	    		jsonSb.append("deviceId:'"+CharTools.javaScriptEscape(oiling.getDeviceId())+"',");
	    		jsonSb.append("termName:'"+CharTools.javaScriptEscape(termName)+"',");
	    		jsonSb.append("vehicleNumber:'"+CharTools.javaScriptEscape(vehicleNumber)+"',");
	    		jsonSb.append("simcard:'"+CharTools.javaScriptEscape(simcard)+"',");
	    		jsonSb.append("groupName:'"+CharTools.javaScriptEscape(groupName)+"',");
	    		jsonSb.append("oilLiter:'"+CharTools.killNullLong2String(oiling.getOilLiter(), "0")+"',");
	    		jsonSb.append("oilCost:'"+CharTools.killNullLong2String(oiling.getOilCost(), "0")+"',");
	    		jsonSb.append("createDate:'"+DateUtility.dateToStr(oiling.getCreateDate())+"'");
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
	
	public ActionForward addOiling(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		
		String deviceId = request.getParameter("deviceId");
		String oilLiter = request.getParameter("oilLiter");
		String oilCost = request.getParameter("oilCost");
		String createDate = request.getParameter("createDate");
		
		deviceId = CharTools.killNullString(deviceId);
		oilLiter = CharTools.killNullString(oilLiter);
		oilCost = CharTools.killNullString(oilCost);
		createDate = CharTools.killNullString(createDate);
		
		deviceId = java.net.URLDecoder.decode(deviceId, "UTF-8");
		oilLiter = java.net.URLDecoder.decode(oilLiter, "UTF-8");
		oilCost = java.net.URLDecoder.decode(oilCost, "UTF-8");
		deviceId = CharTools.killNullString(deviceId);
		oilLiter = CharTools.killNullString(oilLiter);
		oilCost = CharTools.killNullString(oilCost);
		createDate = CharTools.killNullString(createDate);
		TOiling tOiling = new TOiling();
		tOiling.setDeviceId(deviceId);
		tOiling.setOilLiter(Long.parseLong(oilLiter));
		tOiling.setOilCost(Long.parseLong(oilCost));
		tOiling.setCreateDate(DateUtility.strToDate(createDate));
		tOiling.setChangeDate(new Date());
		tOiling.setEmpCode(empCode);
		tOiling.setUserId(userId);
		oilingService.save(tOiling);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.OILING_SET);
		tOptLog.setFunCType(LogConstants.OILING_SET_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("加油记录添加成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward updateOiling(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String id = request.getParameter("id");
		String deviceId = request.getParameter("deviceId");
		String oilLiter = request.getParameter("oilLiter");
		String oilCost = request.getParameter("oilCost");
		String createDate = request.getParameter("createDate");
		deviceId = CharTools.killNullString(deviceId);
		oilLiter = CharTools.killNullString(oilLiter);
		oilCost = CharTools.killNullString(oilCost);
		createDate = CharTools.killNullString(createDate);
		TOiling tOiling = new TOiling();
		tOiling.setId(Long.parseLong(id));
		tOiling.setDeviceId(deviceId);
		tOiling.setOilLiter(Long.parseLong(oilLiter));
		tOiling.setOilCost(Long.parseLong(oilCost));
		tOiling.setCreateDate(DateUtility.strToDate(createDate));
		tOiling.setChangeDate(new Date());
		tOiling.setEmpCode(empCode);
		tOiling.setUserId(userId);
		oilingService.update(tOiling);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.OILING_SET);
		tOptLog.setFunCType(LogConstants.OILING_SET_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("加油记录修改成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
	
	public ActionForward delOiling(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		oilingService.deleteAll(ids);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.OILING_SET);
		tOptLog.setFunCType(LogConstants.OILING_SET_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("加油记录删除成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//成功
		return mapping.findForward(null);
	}
}
