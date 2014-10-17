package com.sosgps.wzt.carTypeInfo.action;

import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;
import com.sosgps.wzt.carTypeInfo.service.CarTypeInfoService;
import com.sosgps.wzt.carTypeInfo.service.impl.CarTypeInfoServiceImpl;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.CarTypeInfo;
import com.sosgps.wzt.orm.TOiling;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TToll;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

public class CarTypeInfoAction extends DispatchWebActionSupport {
	private CarTypeInfoService carTypeInfoService = (CarTypeInfoServiceImpl) SpringHelper.getBean("CarTypeInfoServiceImpl");

	public ActionForward listCarTypeInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
//		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
//		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
//		Date startDate = DateUtility.strToDateTime(st);
//		Date endDate = DateUtility.strToDateTime(et);
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
			Page<Object[]> list= carTypeInfoService.listCarTypeInfo(entCode, userId, 1, 65536, null, null, searchValue);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("������������", 15);
			excelWorkBook.addHeader("������������", 15);
			excelWorkBook.addHeader("��������", 15);
			excelWorkBook.addHeader("��������", 15);
			int row = 0;
			for (Object obj : list.getResult()) {
//				String deviceId = (String) objects[0];
//				String termName = (String)objects[1];
//	    		String vehicleNumber = (String)objects[2];
//	    		String simcard = (String)objects[3];
//	    		String groupName = (String)objects[4];
	    		CarTypeInfo carType = (CarTypeInfo)obj;
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(carType.getTypeName()));				//������������
				excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(carType.getDesction()));       		//������������
				excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(carType.getOilWear(),"0"));		    //��������
				excelWorkBook.addCell(col++, row, DateUtility.dateToStr(carType.getCrtdate()));				        //��������
			}
			excelWorkBook.write();
			return null;
		}
		
		
		
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
	
		//Page<Object[]> list = carTypeInfoService.listCarTypeInfo(entCode, userId, page, limitint, startDate, endDate, searchValue);
		Page<Object[]> list = carTypeInfoService.listCarTypeInfo(entCode, userId, page, limitint, null, null, searchValue);
		StringBuffer jsonSb = new StringBuffer();
	    String total = "";
		if (list != null && list.getResult()!=null) {
			total = "{total:"+list.getTotalCount()+",data:[";
	    	Iterator i = list.getResult().iterator();
	    	while(i.hasNext()){
	    		Object userObj = (Object)i.next();
	    		CarTypeInfo carTypeInfo = (CarTypeInfo)userObj;
	    		jsonSb.append("{");
	    		jsonSb.append("id:'"+carTypeInfo.getId()+"',");
	    		jsonSb.append("typeName:'"+CharTools.javaScriptEscape(carTypeInfo.getTypeName())+"',");
	    		jsonSb.append("iconPath:'"+CharTools.javaScriptEscape(carTypeInfo.getIconPath())+"',");
	    		jsonSb.append("desction:'"+CharTools.javaScriptEscape(carTypeInfo.getDesction())+"',");
	    		jsonSb.append("enCode:'"+CharTools.javaScriptEscape(carTypeInfo.getEnCode())+"',");
	    		jsonSb.append("oilWear:'"+CharTools.killNullLong2String(carTypeInfo.getOilWear(), "0")+"',");
	    		jsonSb.append("userId:'"+CharTools.killNullLong2String(carTypeInfo.getUserId(), "0")+"',");
	    		jsonSb.append("crtdate:'"+DateUtility.dateTimeToStr(carTypeInfo.getCrtdate())+"'");
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
	
	public ActionForward addCarTypeInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		//String empCode = userInfo.getEmpCode();
		//Long userId = userInfo.getUserId();
		String typename = request.getParameter("typename");
		String desction = request.getParameter("desction");
		String oilwear = request.getParameter("oilwear");
		String empCode = request.getParameter("empCode");
		String userId = request.getParameter("userId");
		
		typename =  CharTools.killNullString(typename);
		typename = java.net.URLDecoder.decode(typename, "UTF-8");
		typename = CharTools.killNullString(typename);
		
		desction =  CharTools.killNullString(desction);
		desction = java.net.URLDecoder.decode(desction, "UTF-8");
		desction = CharTools.killNullString(desction);
		
		CarTypeInfo carTypeInfo = new CarTypeInfo();
		carTypeInfo.setTypeName(typename);
		carTypeInfo.setDesction(desction);
		carTypeInfo.setOilWear(Long.parseLong(oilwear));
		carTypeInfo.setEnCode(empCode);
		carTypeInfo.setUserId(Long.parseLong(userId));
		carTypeInfo.setChangeDate(new Date());
		carTypeInfo.setCrtdate(new Date());
		carTypeInfoService.save(carTypeInfo);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.CAR_TYPE_INFO_SET);
		tOptLog.setFunCType(LogConstants.CAR_TYPE_INFO_SET_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("����������ӳɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//�ɹ�
		return mapping.findForward(null);
	}
	
	public ActionForward updateCarTypeInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String id = request.getParameter("id");
		String typename = request.getParameter("typename");
		String desction = request.getParameter("desction");
		String oilwear = request.getParameter("oilwear");
		String empCode = request.getParameter("empCode");
		String userId = request.getParameter("userId");
		String crtdate = request.getParameter("crtdate");
		typename = CharTools.killNullString(typename);
		typename = java.net.URLDecoder.decode(typename, "UTF-8");
		typename = CharTools.killNullString(typename);
		
		desction = CharTools.killNullString(desction);
		desction = java.net.URLDecoder.decode(desction, "UTF-8");
		desction = CharTools.killNullString(desction);
		
		CarTypeInfo carTypeInfo = new CarTypeInfo();
		carTypeInfo.setId(Long.parseLong(id));
		carTypeInfo.setTypeName(typename);
		carTypeInfo.setDesction(desction);
		carTypeInfo.setOilWear(Long.parseLong(oilwear));
		carTypeInfo.setEnCode(empCode);
		carTypeInfo.setChangeUserId(Long.parseLong(userId));
		carTypeInfo.setChangeDate(new Date());
		carTypeInfo.setCrtdate(DateUtility.strToDateTime(crtdate));
		carTypeInfoService.update(carTypeInfo);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.CAR_TYPE_INFO_SET);
		tOptLog.setFunCType(LogConstants.CAR_TYPE_INFO_SET_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("���������޸ĳɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//�ɹ�
		return mapping.findForward(null);
	}
	
	public ActionForward delCarTypeInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		carTypeInfoService.deleteAll(ids);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.CAR_TYPE_INFO_SET);
		tOptLog.setFunCType(LogConstants.CAR_TYPE_INFO_SET_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("��������ɾ���ɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");//�ɹ�
		return mapping.findForward(null);
	}
	
	public ActionForward carTypeInfoCombo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String type = request.getParameter("type");
		
		//Page<Object[]> list = carTypeInfoService.listCarTypeInfo(entCode, userId, page, limitint, startDate, endDate, searchValue);
		Page<Object[]> list = carTypeInfoService.listCarTypeInfo(entCode, userId, 1, 65535, null, null, "");
		StringBuffer jsonSb = new StringBuffer();
		if (list != null && list.getResult()!=null) {
	    	Iterator i = list.getResult().iterator();
	    	if(type != null && type.equals("noallbox")){
	    		
	    	}else{
		    	jsonSb.append("[");
	    		jsonSb.append("'-1',");
	    		jsonSb.append("'ȫ������'");
	    		jsonSb.append("],");
	    	}
	    	while(i.hasNext()){
	    		Object userObj = (Object)i.next();
	    		CarTypeInfo carTypeInfo = (CarTypeInfo)userObj;
	    		jsonSb.append("[");
	    		jsonSb.append("'"+carTypeInfo.getId()+"',");
	    		jsonSb.append("'"+CharTools.javaScriptEscape(carTypeInfo.getTypeName())+"'");
	    		jsonSb.append("],");
	    	}
	    	if(jsonSb.length()>0){
	    		jsonSb.deleteCharAt(jsonSb.length()-1);
	    	}
	    }
	    response.setContentType("text/json; charset=utf-8");
	    response.getWriter().write("["+jsonSb.toString()+"]");
		return mapping.findForward(null);
	}
	
}
