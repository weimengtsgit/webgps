package com.sosgps.wzt.vehicleExpense.action;

import java.math.BigDecimal;
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
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TToll;
import com.sosgps.wzt.orm.TVehicleExpense;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.vehicleExpense.service.VehicleExpenseService;
import com.sosgps.wzt.vehicleExpense.service.impl.VehicleExpenseServiceImpl;

public class VehicleExpenseAction extends DispatchWebActionSupport {
	private VehicleExpenseService vehicleExpenseService = (VehicleExpenseServiceImpl) SpringHelper
			.getBean("VehicleExpenseServiceImpl");

	public ActionForward listVehicleExpense(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
													// HH:mm:ss
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
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
			Page<Object[]> list = vehicleExpenseService.listVehicleExpense(
					entCode, userId, 1, 65536, startDate, endDate, searchValue);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			// header
			excelWorkBook.addHeader("����", 15);
			excelWorkBook.addHeader("���ƺ�", 15);
			excelWorkBook.addHeader("�ֻ���", 15);
			excelWorkBook.addHeader("������", 15);
			excelWorkBook.addHeader("�����۾ɷ���", 15);
			excelWorkBook.addHeader("��Ա����", 15);
			excelWorkBook.addHeader("����̯������", 15);
			excelWorkBook.addHeader("ά�ޱ�������", 15);
			excelWorkBook.addHeader("��·���ŷ�", 15);
			excelWorkBook.addHeader("��켰��������", 15);
			excelWorkBook.addHeader("��������", 15);
			int row = 0;
			for (Object[] objects : list.getResult()) {
				// String deviceId = (String) objects[0];
				String termName = (String) objects[1];
				String vehicleNumber = (String) objects[2];
				String simcard = (String) objects[3];
				String groupName = (String) objects[4];
				TVehicleExpense ehicleExpense = (TVehicleExpense) objects[5];
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(termName)); // �ն�����
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNumber)); // ���ƺ�
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(simcard)); // �ֻ���
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(groupName)); // ������
				excelWorkBook.addCell(
						col++,
						row,
						CharTools.killNullLong2String(
								ehicleExpense.getVehicleDepreciation(), "0")); // �����۾ɷ���
				excelWorkBook.addCell(
						col++,
						row,
						CharTools.killNullLong2String(
								ehicleExpense.getPersonalExpenses(), "0")); // ��Ա����
				excelWorkBook.addCell(
						col++,
						row,
						CharTools.killNullLong2String(
								ehicleExpense.getInsurance(), "0")); // ����̯������
				excelWorkBook.addCell(
						col++,
						row,
						CharTools.killNullLong2String(
								ehicleExpense.getMaintenance(), "0")); // ά�ޱ�������
				excelWorkBook.addCell(col++, row, CharTools
						.killNullLong2String(ehicleExpense.getToll(), "0")); // ��·���ŷ�
				excelWorkBook.addCell(
						col++,
						row,
						CharTools.killNullLong2String(
								ehicleExpense.getAnnualCheck(), "0")); // ��켰��������
				excelWorkBook.addCell(col++, row,
						DateUtility.dateToStr(ehicleExpense.getCreateDate())); // ��������
			}
			excelWorkBook.write();
			return null;
		}

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;

		Page<Object[]> list = vehicleExpenseService.listVehicleExpense(entCode,
				userId, page, limitint, startDate, endDate, searchValue);
		StringBuffer jsonSb = new StringBuffer();
		String total = "";
		if (list != null && list.getResult() != null) {
			total = "{total:" + list.getTotalCount() + ",data:[";
			Iterator<Object[]> i = list.getResult().iterator();
			while (i.hasNext()) {
				Object[] userObj = (Object[]) i.next();
				Long id = (Long) userObj[0];
				String termName = (String) userObj[1];
				String vehicleNumber = (String) userObj[2];
				String simcard = (String) userObj[3];
				String groupName = (String) userObj[4];
				TVehicleExpense vehicleExpense = (TVehicleExpense) userObj[5];
				jsonSb.append("{");
				jsonSb.append("id:'" + id + "',");
				jsonSb.append("deviceId:'"
						+ CharTools.javaScriptEscape(vehicleExpense
								.getDeviceId()) + "',");
				jsonSb.append("termName:'"
						+ CharTools.javaScriptEscape(termName) + "',");
				jsonSb.append("vehicleNumber:'"
						+ CharTools.javaScriptEscape(vehicleNumber) + "',");
				jsonSb.append("simcard:'" + CharTools.javaScriptEscape(simcard)
						+ "',");
				jsonSb.append("groupName:'"
						+ CharTools.javaScriptEscape(groupName) + "',");
				jsonSb.append("vehicleDepreciation:'"
						+ CharTools.killNullLong2String(
								vehicleExpense.getVehicleDepreciation(), "0")
						+ "',");
				jsonSb.append("personalExpenses:'"
						+ CharTools.killNullLong2String(
								vehicleExpense.getPersonalExpenses(), "0")
						+ "',");
				jsonSb.append("insurance:'"
						+ CharTools.killNullLong2String(
								vehicleExpense.getInsurance(), "0") + "',");
				jsonSb.append("maintenance:'"
						+ CharTools.killNullLong2String(
								vehicleExpense.getMaintenance(), "0") + "',");
				jsonSb.append("toll:'"
						+ CharTools.killNullLong2String(
								vehicleExpense.getToll(), "0") + "',");
				jsonSb.append("annualCheck:'"
						+ CharTools.killNullLong2String(
								vehicleExpense.getAnnualCheck(), "0") + "',");
				jsonSb.append("createDate:'"
						+ DateUtility.dateToStr(vehicleExpense.getCreateDate())
						+ "'");
				jsonSb.append("},");
			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
			jsonSb.append("]}");
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(total + jsonSb.toString());
		return mapping.findForward(null);
	}

	public ActionForward addVehicleExpense(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String deviceId = request.getParameter("deviceId");
		String vehicleDepreciation = request
				.getParameter("vehicleDepreciation");
		String personalExpenses = request.getParameter("personalExpenses");
		String insurance = request.getParameter("insurance");
		String maintenance = request.getParameter("maintenance");
		String toll = request.getParameter("toll");
		String annualCheck = request.getParameter("annualCheck");
		String createDate = request.getParameter("createDate");
		deviceId = CharTools.killNullString(deviceId);
		vehicleDepreciation = CharTools
				.killNullString(vehicleDepreciation, "0");
		personalExpenses = CharTools.killNullString(personalExpenses, "0");
		insurance = CharTools.killNullString(insurance, "0");
		maintenance = CharTools.killNullString(maintenance, "0");
		toll = CharTools.killNullString(toll, "0");
		annualCheck = CharTools.killNullString(annualCheck, "0");
		createDate = CharTools.killNullString(createDate);
		TVehicleExpense tVehicleExpense = new TVehicleExpense();
		tVehicleExpense.setDeviceId(deviceId);
		tVehicleExpense.setVehicleDepreciation(Long
				.parseLong(vehicleDepreciation));
		tVehicleExpense.setPersonalExpenses(Long.parseLong(personalExpenses));
		tVehicleExpense.setInsurance(Long.parseLong(insurance));
		tVehicleExpense.setMaintenance(Long.parseLong(maintenance));
		tVehicleExpense.setToll(Long.parseLong(toll));
		tVehicleExpense.setAnnualCheck(Long.parseLong(annualCheck));
		tVehicleExpense.setCreateDate(DateUtility.strToDate(createDate));
		tVehicleExpense.setChangeDate(new Date());
		tVehicleExpense.setEmpCode(empCode);
		tVehicleExpense.setUserId(userId);
		vehicleExpenseService.save(tVehicleExpense);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.VEHICLE_EXPENSE_SET);
		tOptLog.setFunCType(LogConstants.VEHICLE_EXPENSE_SET_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("�������ü�¼��ӳɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	public ActionForward updateVehicleExpense(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String id = request.getParameter("id");
		String deviceId = request.getParameter("deviceId");
		String vehicleDepreciation = request
				.getParameter("vehicleDepreciation");
		String personalExpenses = request.getParameter("personalExpenses");
		String insurance = request.getParameter("insurance");
		String maintenance = request.getParameter("maintenance");
		String toll = request.getParameter("toll");
		String annualCheck = request.getParameter("annualCheck");
		String createDate = request.getParameter("createDate");
		deviceId = CharTools.killNullString(deviceId);
		vehicleDepreciation = CharTools
				.killNullString(vehicleDepreciation, "0");
		personalExpenses = CharTools.killNullString(personalExpenses, "0");
		insurance = CharTools.killNullString(insurance, "0");
		maintenance = CharTools.killNullString(maintenance, "0");
		toll = CharTools.killNullString(toll, "0");
		annualCheck = CharTools.killNullString(annualCheck, "0");
		createDate = CharTools.killNullString(createDate);
		TVehicleExpense tVehicleExpense = new TVehicleExpense();
		tVehicleExpense.setDeviceId(deviceId);
		tVehicleExpense.setVehicleDepreciation(Long
				.parseLong(vehicleDepreciation));
		tVehicleExpense.setPersonalExpenses(Long.parseLong(personalExpenses));
		tVehicleExpense.setInsurance(Long.parseLong(insurance));
		tVehicleExpense.setMaintenance(Long.parseLong(maintenance));
		tVehicleExpense.setToll(Long.parseLong(toll));
		tVehicleExpense.setAnnualCheck(Long.parseLong(annualCheck));
		tVehicleExpense.setCreateDate(DateUtility.strToDate(createDate));
		tVehicleExpense.setChangeDate(new Date());
		tVehicleExpense.setEmpCode(empCode);
		tVehicleExpense.setUserId(userId);
		tVehicleExpense.setId(Long.parseLong(id));
		vehicleExpenseService.update(tVehicleExpense);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.VEHICLE_EXPENSE_SET);
		tOptLog.setFunCType(LogConstants.VEHICLE_EXPENSE_SET_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("�������ü�¼�޸ĳɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	public ActionForward delVehicleExpense(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		String ids = request.getParameter("ids");
		vehicleExpenseService.deleteAll(ids);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.VEHICLE_EXPENSE_SET);
		tOptLog.setFunCType(LogConstants.VEHICLE_EXPENSE_SET_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("�������ü�¼ɾ���ɹ�! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	/*
	 * ��������ͳ��
	 */
	public ActionForward listVehicleMsg(ActionMapping mapping, ActionForm form,
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
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
		String deviceIdsStr = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
		String searchValue = request.getParameter("searchValue");
		String carTypeInfoId = request.getParameter("duration");
		if (start == null || limit == null || st == null || et == null
				|| deviceIdsStr == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}

		// Date startDate = DateUtility.strToDateTime(st);
		// Date endDate = DateUtility.strToDateTime(et);
		deviceIdsStr = CharTools.killNullString(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		carTypeInfoId = CharTools.killNullString(carTypeInfoId);
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.killNullString(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			//String userAccount = request.getParameter("userAccount");
			String cms = request.getParameter("cms");
			String[] arrCms = cms.split(",");
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);

			for (String str : arrCms) {
//				System.out.println("=====" + str);
				if (str.equals("groupName")) {
					excelWorkBook.addHeader("����", 15);
				}
				if (str.equals("termName")) {
					excelWorkBook.addHeader("����", 15);
				}
				if (str.equals("vehicleNumber")) {
					excelWorkBook.addHeader("���ƺ�", 15);
				}
				if (str.equals("typeName")) {
					excelWorkBook.addHeader("�����ͺ�", 15);
				}
				if (str.equals("simcard")) {
					excelWorkBook.addHeader("�ֻ�����", 15);
				}
				if (str.equals("oilLiter")) {
					excelWorkBook.addHeader("������(��)", 15);
				}
				if (str.equals("oilCost")) {
					excelWorkBook.addHeader("���ͽ��", 15);
				}
				if (str.equals("vehicleDepreciation")) {
					excelWorkBook.addHeader("�����۾�", 15);
				}
				if (str.equals("personalExpenses")) {
					excelWorkBook.addHeader("��Ա����", 15);
				}
				if (str.equals("mileagenorm")) {
					excelWorkBook.addHeader("��̱�׼", 15);
				}
				if (str.equals("expensenorm")) {
					excelWorkBook.addHeader("���ñ�׼", 15);
				}
				if (str.equals("returnnorm")) {
					excelWorkBook.addHeader("������׼", 15);
				}
				if (str.equals("dispatchamount")) {
					excelWorkBook.addHeader("���ͽ��", 15);
				}
				if (str.equals("insurance")) {
					excelWorkBook.addHeader("����̯��", 15);
				}
				if (str.equals("maintenance")) {
					excelWorkBook.addHeader("ά�ޱ�������", 15);
				}
				if (str.equals("toll")) {
					excelWorkBook.addHeader("��·���ŷ���", 15);
				}
				if (str.equals("annualCheck")) {
					excelWorkBook.addHeader("��켰��������", 15);
				}
				if (str.equals("oilperkm")) {
					excelWorkBook.addHeader("ÿ�����ͺ�", 15);
				}
				if (str.equals("costperkm")) {
					excelWorkBook.addHeader("ÿ�������", 15);
				}
			}
			Page<Object[]> list = vehicleExpenseService.listVehicleMsg(
					deviceIds, entCode, userId, 0, 65535, st, et, searchValue,
					carTypeInfoId);

			// header
			int row = 0;
			for (Object[] objects : list.getResult()) {
				BigDecimal vehicleDepreciation = (BigDecimal) objects[0];
				BigDecimal personalExpenses = (BigDecimal) objects[1];
				BigDecimal insurance = (BigDecimal) objects[2];
				BigDecimal maintenance = (BigDecimal) objects[3];
				BigDecimal toll = (BigDecimal) objects[4];
				BigDecimal annualCheck = (BigDecimal) objects[5];
				BigDecimal oilLiter = (BigDecimal) objects[6];
				BigDecimal oilCost = (BigDecimal) objects[7];
				BigDecimal distance = (BigDecimal) objects[8];
				BigDecimal mileagenorm = (BigDecimal) objects[9];
				BigDecimal expensenorm = (BigDecimal) objects[10];
				BigDecimal returnnorm = (BigDecimal) objects[11];
				BigDecimal dispatchamount = (BigDecimal) objects[12];
				String deviceId = (String) objects[13];
				String typeName = (String) objects[14];
				BigDecimal oilWear = (BigDecimal) objects[15];
				String termName = (String) objects[16];
				String vehicleNumber = (String) objects[17];
				String simcard = (String) objects[18];
				String groupName = (String) objects[19];
				BigDecimal oilperkm = (BigDecimal) objects[20];
				BigDecimal costperkm = (BigDecimal) objects[21];

				String vehicleDepreciations = "";
				String personalExpensess = "";
				String insurances = "";
				String maintenances = "";
				String tolls = "";
				String annualChecks = "";
				String oilLiters = "";
				String oilCosts = "";
				String distances = "";
				String oilWears = "";
				String oilperkms = "";
				String costperkms = "";
				String dispatchamounts = "";
				String mileagenorms = "";
				String expensenorms = "";
				String returnnorms = "";
				if (vehicleDepreciation != null) {
					vehicleDepreciations = vehicleDepreciation + "";
				}
				if (personalExpenses != null) {
					personalExpensess = personalExpenses + "";
				}
				if (insurance != null) {
					insurances = insurance + "";
				}
				if (maintenance != null) {
					maintenances = maintenance + "";
				}
				if (toll != null) {
					tolls = toll + "";
				}
				if (annualCheck != null) {
					annualChecks = annualCheck + "";
				}
				if (dispatchamount != null) {
					dispatchamounts = dispatchamount + "";
				}
				if (mileagenorm != null) {
					mileagenorms = mileagenorm + "";
				}
				if (expensenorm != null) {
					expensenorms = expensenorm + "";
				}
				if (returnnorm != null) {
					returnnorms = returnnorm + "";
				}
				if (oilLiter != null) {
					oilLiters = oilLiter + "";
				}
				if (oilCost != null) {
					oilCosts = oilCost + "";
				}
				if (distance != null) {
					distances = distance + "";
				}
				if (oilWear != null) {
					oilWears = oilWear + "";
				}
				if (personalExpenses != null) {
					personalExpensess = personalExpenses + "";
				}
				if (oilperkm == null) {
					oilperkms = "";
				} else {
					oilperkms = oilperkm.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue() + "";
				}
				if (costperkm == null) {
					costperkms = "";
				} else {
					costperkms = costperkm
							.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue()
							+ "";
				}

				int col = 0;
				row += 1;
				for (String str : arrCms) {
					if(str.equals("groupName"))
					{
						excelWorkBook.addCell(col++, row,
								CharTools.javaScriptEscape(groupName));		
					}
					if (str.equals("termName")) {
						excelWorkBook.addCell(col++, row,
								CharTools.javaScriptEscape(termName));
					}
				
					if (str.equals("vehicleNumber")) {
						excelWorkBook.addCell(col++, row,
								CharTools.javaScriptEscape(vehicleNumber));
					}
					if (str.equals("typeName")) {
						excelWorkBook.addCell(col++, row,
								CharTools.javaScriptEscape(typeName));
					}
					if (str.equals("simcard")) {
						excelWorkBook.addCell(col++, row,
								CharTools.javaScriptEscape(simcard));
					}
					if (str.equals("oilLiter")) {
						excelWorkBook.addCell(col++, row, oilLiters);
					}
					if (str.equals("oilCost")) {
						excelWorkBook.addCell(col++, row, oilCosts);
					}
					if (str.equals("vehicleDepreciation")) {
						excelWorkBook.addCell(col++, row, vehicleDepreciations);
					}
					if (str.equals("personalExpenses")) {
						excelWorkBook.addCell(col++, row, personalExpensess);
					}
					if (str.equals("mileagenorm")) {
						excelWorkBook.addCell(col++, row, mileagenorms);
					}
					
					if (str.equals("expensenorm")) {
						excelWorkBook.addCell(col++, row, expensenorms);
					}
					if (str.equals("returnnorm")) {
						excelWorkBook.addCell(col++, row, returnnorms);
					}
					if (str.equals("dispatchamount")) {
						excelWorkBook.addCell(col++, row, dispatchamounts);
					}
					if (str.equals("insurance")) {
						excelWorkBook.addCell(col++, row, insurances);
					}
					if (str.equals("maintenance")) {
						excelWorkBook.addCell(col++, row, maintenances);
					}
					if (str.equals("toll")) {
						excelWorkBook.addCell(col++, row, tolls);
					}
					if (str.equals("annualCheck")) {
						excelWorkBook.addCell(col++, row, annualChecks);
					}
					if (str.equals("oilperkm")) {
						excelWorkBook.addCell(col++, row, oilperkms);
					}
					if (str.equals("costperkm")) {
						excelWorkBook.addCell(col++, row, costperkms);
					}
				
				}
				
			}
			//add by 2012-12-18 zss  ����������Ϣͳ��
			TOptLog tOptLog = new TOptLog(); 
			tOptLog.setEmpCode(entCode);
			tOptLog.setUserName(userInfo.getUserAccount()); 
			tOptLog.setUserId(userId);
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setOptTime(new Date());
			tOptLog.setFunFType(LogConstants.LOG_STAT);
			tOptLog.setFunCType(LogConstants.LOG_STAT_CARINFO);
			tOptLog.setResult(new Long(1));
			tOptLog.setOptDesc(userInfo.getUserAccount()+"����������Ϣͳ�Ƴɹ�");
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			
			excelWorkBook.write();
			return null;
		}

		//searchLog
		TOptLog tOptLog = new TOptLog(); 
		tOptLog.setEmpCode(entCode);
		tOptLog.setUserName(userInfo.getUserAccount()); 
		tOptLog.setUserId(userId);
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setOptTime(new Date());
		tOptLog.setFunFType(LogConstants.LOG_STAT);
		tOptLog.setFunCType(LogConstants.LOG_STAT_CARINFO);
		tOptLog.setResult(new Long(1));
		tOptLog.setOptDesc(userInfo.getUserAccount()+"��ѯ������Ϣͳ�Ƴɹ�");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		
		// int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = vehicleExpenseService.listVehicleMsg(
					deviceIds, entCode, userId, startint, pageSize, st, et,
					searchValue, carTypeInfoId);
			if (list != null && list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Object[] objects : list.getResult()) {
					BigDecimal vehicleDepreciation = (BigDecimal) objects[0];
					BigDecimal personalExpenses = (BigDecimal) objects[1];
					BigDecimal insurance = (BigDecimal) objects[2];
					BigDecimal maintenance = (BigDecimal) objects[3];
					BigDecimal toll = (BigDecimal) objects[4];
					BigDecimal annualCheck = (BigDecimal) objects[5];
					BigDecimal oilLiter = (BigDecimal) objects[6];
					BigDecimal oilCost = (BigDecimal) objects[7];
					BigDecimal distance = (BigDecimal) objects[8];
					BigDecimal mileagenorm = (BigDecimal) objects[9];
					BigDecimal expensenorm = (BigDecimal) objects[10];
					BigDecimal returnnorm = (BigDecimal) objects[11];
					BigDecimal dispatchamount = (BigDecimal) objects[12];
					String deviceId = (String) objects[13];
					String typeName = (String) objects[14];
					BigDecimal oilWear = (BigDecimal) objects[15];
					String termName = (String) objects[16];
					String vehicleNumber = (String) objects[17];
					String simcard = (String) objects[18];
					String groupName = (String) objects[19];
					BigDecimal oilperkm = (BigDecimal) objects[20];
					BigDecimal costperkm = (BigDecimal) objects[21];

					String vehicleDepreciations = "";
					String personalExpensess = "";
					String insurances = "";
					String maintenances = "";
					String tolls = "";
					String annualChecks = "";
					String oilLiters = "";
					String oilCosts = "";
					String distances = "";
					String oilWears = "";
					String oilperkms = "";
					String costperkms = "";
					String dispatchamounts = "";
					String mileagenorms = "";
					String expensenorms = "";
					String returnnorms = "";
					if (vehicleDepreciation != null) {
						vehicleDepreciations = vehicleDepreciation + "";
					}
					if (personalExpenses != null) {
						personalExpensess = personalExpenses + "";
					}
					if (insurance != null) {
						insurances = insurance + "";
					}
					if (maintenance != null) {
						maintenances = maintenance + "";
					}
					if (toll != null) {
						tolls = toll + "";
					}
					if (annualCheck != null) {
						annualChecks = annualCheck + "";
					}
					if (oilLiter != null) {
						oilLiters = oilLiter + "";
					}
					if (oilCost != null) {
						oilCosts = oilCost + "";
					}
					if (distance != null) {
						distances = distance + "";
					}
					if (dispatchamount != null) {
						dispatchamounts = dispatchamount + "";
					}
					if (mileagenorm != null) {
						mileagenorms = mileagenorm + "";
					}
					if (expensenorm != null) {
						expensenorms = expensenorm + "";
					}
					if (returnnorm != null) {
						returnnorms = returnnorm + "";
					}
					if (oilWear != null) {
						oilWears = oilWear + "";
					}
					if (oilperkm != null) {
						oilperkms = oilperkm.setScale(2,
								BigDecimal.ROUND_HALF_UP).doubleValue()
								+ "";
					}
					if (costperkm != null) {
						costperkms = costperkm.setScale(2,
								BigDecimal.ROUND_HALF_UP).doubleValue()
								+ "";
					}

					jsonSb.append("{");
					jsonSb.append("vehicleDepreciation:'"
							+ vehicleDepreciations + "',");
					jsonSb.append("personalExpenses:'" + personalExpensess
							+ "',");
					jsonSb.append("insurance:'" + insurances + "',");
					jsonSb.append("maintenance:'" + maintenances + "',");
					jsonSb.append("toll:'" + tolls + "',");
					jsonSb.append("annualCheck:'" + annualChecks + "',");
					jsonSb.append("oilLiter:'" + oilLiters + "',");
					jsonSb.append("oilCost:'" + oilCosts + "',");
					jsonSb.append("distance:'" + distances + "',");
					jsonSb.append("mileagenorm:'" + mileagenorms + "',");
					jsonSb.append("expensenorm:'" + expensenorms + "',");
					jsonSb.append("returnnorm:'" + returnnorms + "',");
					jsonSb.append("dispatchamount:'" + dispatchamounts + "',");
					jsonSb.append("deviceId:'"
							+ CharTools.javaScriptEscape(deviceId) + "',");
					jsonSb.append("typeName:'"
							+ CharTools.javaScriptEscape(typeName) + "',");
					jsonSb.append("oilWear:'" + oilWears + "',");
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(termName) + "',");
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(vehicleNumber) + "',");
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(simcard) + "',");
					jsonSb.append("groupName:'"
							+ CharTools.javaScriptEscape(groupName) + "',");
					jsonSb.append("oilperkm:'" + oilperkms + "',");
					jsonSb.append("costperkm:'" + costperkms + "'");
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
		return mapping.findForward(null);
	}

}
