package com.sosgps.wzt.stat.action;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;
import sun.misc.BASE64Decoder;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.stat.service.AttendanceStatService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.service.UserService;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.CoordConvertAPI;

public class AttendanceStatAction extends DispatchWebActionSupport {
	private static final Logger logger = Logger.getLogger(AttendanceStatAction.class);
	private AttendanceStatService attendanceStatService = (AttendanceStatService) SpringHelper.getBean("AttendanceStatServiceImpl");
	private UserService userService = (UserService) SpringHelper.getBean("userService");

	//private static final Logger logger = Logger
	//		.getLogger(AttendanceStatAction.class);
	
	public ActionForward listAttendanceRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		st = st.replace("-", "");
		et = et.replace("-", "");
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		
		if(st == null || et == null || deviceIds.equals("")){
			response.getWriter().write("参数不全");// 未登录
			return mapping.findForward(null);
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			String entCode = request.getParameter("entCode");
			String userAccount = request.getParameter("userAccount");
			String password = request.getParameter("password");
			
			entCode = URLDecoder.decode(entCode, "utf-8");
			userAccount = URLDecoder.decode(userAccount, "utf-8");
			password = URLDecoder.decode(password, "UTF-8");
			
			entCode = CharTools.javaScriptEscape(entCode);
			userAccount = CharTools.javaScriptEscape(userAccount);
			password = CharTools.javaScriptEscape(password);
			
			entCode = new String(new BASE64Decoder().decodeBuffer(entCode));
			userAccount = new String(new BASE64Decoder().decodeBuffer(userAccount));
			password = new String(new BASE64Decoder().decodeBuffer(password));
			TUser tUser = userService.findUserByLoginParam(entCode, userAccount, password);
			if(tUser == null){
				response.getWriter().write("无权访问");// 未登录
				return mapping.findForward(null);
			}
			//Long userId = tUser.getId();
			Page<Object[]> list = attendanceStatService.listAttendanceRecord(0, 65536, st, et, deviceIds);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.addHeader("部门", 15);
			excelWorkBook.addHeader("名称", 15);
			excelWorkBook.addHeader("考勤日期", 15);
			excelWorkBook.addHeader("签到时间", 20);
			excelWorkBook.addHeader("签到位置", 40);
			excelWorkBook.addHeader("签退时间", 20);
			excelWorkBook.addHeader("签退位置", 40);
			//excelWorkBook.addHeader("创建时间", 20);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				String termName = (String) objects[0];
				Number attendanceDate = (Number)objects[1];
				String signinTime = (String)objects[2];
				String signinDesc = (String)objects[3];
				String signoffTime = (String)objects[4];
				String signoffDesc = (String)objects[5];
				String groupName = (String)objects[8];
				Double signinLongitude = ((BigDecimal)objects[9]) == null ? null : ((BigDecimal)objects[9]).doubleValue();
				Double signinLatitude = ((BigDecimal)objects[10]) == null ? null : ((BigDecimal)objects[10]).doubleValue();
				Double signoffLongitude = ((BigDecimal)objects[11]) == null ? null : ((BigDecimal)objects[11]).doubleValue();
				Double signoffLatitude = ((BigDecimal)objects[12]) == null ? null : ((BigDecimal)objects[12]).doubleValue();
				
				if((signinDesc == null || signinDesc.length() <= 0) && signinLongitude != null && signinLatitude != null){signinDesc = CoordConvertAPI.getLocDescByLngLat(signinLongitude, signinLatitude, logger);}
				if((signoffDesc == null || signoffDesc.length() <= 0) && signoffLongitude != null && signoffLatitude != null){signoffDesc = CoordConvertAPI.getLocDescByLngLat(signoffLongitude, signoffLatitude, logger);}
				
				String newAttendanceDate = attendanceDate.toString().substring(0,4)+"-"+attendanceDate.toString().substring(4,6)+"-"+attendanceDate.toString().substring(6,8);
				
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(groupName));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(termName));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(newAttendanceDate));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(signinTime));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(signinDesc));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(signoffTime));
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(signoffDesc));
				//excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(createTime));
			}
			//add by 2012-12-18 zss 导出考勤记录表
			UserInfo userInfo = this.getCurrentUser(request);
			TOptLog tOptLog = new TOptLog(); 
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount()); 
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_ATTENDANCE_RECORD);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount()+"导出考勤记录表");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			
			excelWorkBook.write();
			return null;
		}
		UserInfo userInfo = this.getCurrentUser(request);
		
		if(start == null || limit == null || userInfo == null){
				response.getWriter().write("{result:\"9\"}");// 未登录
				return mapping.findForward(null);
		}
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = attendanceStatService.listAttendanceRecord(startint, pageSize, st, et, deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					String termName = (String) objects[0];
					Number attendanceDate = (Number)objects[1];
					String signinTime = (String)objects[2];
					String signinDesc = (String)objects[3];
					String signoffTime = (String)objects[4];
					String signoffDesc = (String)objects[5];
					String deviceId = (String)objects[6];
					//String createTime = (String)objects[7];
					String groupName = (String)objects[8];
					Double signinLongitude = ((BigDecimal)objects[9]) == null ? null : ((BigDecimal)objects[9]).doubleValue();
					Double signinLatitude = ((BigDecimal)objects[10]) == null ? null : ((BigDecimal)objects[10]).doubleValue();
					Double signoffLongitude = ((BigDecimal)objects[11]) == null ? null : ((BigDecimal)objects[11]).doubleValue();
					Double signoffLatitude = ((BigDecimal)objects[12]) == null ? null : ((BigDecimal)objects[12]).doubleValue();
					
					if((signinDesc == null || signinDesc.length() <= 0) && signinLongitude != null && signinLatitude != null){signinDesc = CoordConvertAPI.getLocDescByLngLat(signinLongitude, signinLatitude, logger);}
					if((signoffDesc == null || signoffDesc.length() <= 0) && signoffLongitude != null && signoffLatitude != null){signoffDesc = CoordConvertAPI.getLocDescByLngLat(signoffLongitude, signoffLatitude, logger);}
					
					
					String newAttendanceDate = attendanceDate.toString().substring(0,4)+"-"+attendanceDate.toString().substring(4,6)+"-"+attendanceDate.toString().substring(6,8);
					
					jsonSb.append("{");
					jsonSb.append("id:'" + deviceId + "',");// id
					jsonSb.append("groupName:'" + CharTools.javaScriptEscape(groupName) + "',");
					jsonSb.append("termName:'" + CharTools.javaScriptEscape(termName) + "',");
					jsonSb.append("attendanceDate:'" + CharTools.javaScriptEscape(newAttendanceDate) + "',");
					jsonSb.append("signinTime:'" + CharTools.javaScriptEscape(signinTime) + "',");
					jsonSb.append("signinDesc:'" + CharTools.javaScriptEscape(signinDesc) + "',");
					jsonSb.append("signoffTime:'" + CharTools.javaScriptEscape(signoffTime) + "',");
					jsonSb.append("signoffDesc:'" + CharTools.javaScriptEscape(signoffDesc) + "'");
					
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{total:" + total + ",data:[" + jsonSb.toString() + "]}");

		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_ATTENDANCE_RECORD);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount() + "查询考勤记录成功");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		
		return mapping.findForward(null);
	}
}
