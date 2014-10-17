package com.sosgps.v21.cash.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sosgps.v21.dao.CashDao;
import com.sosgps.v21.dao.TargetDao;
import com.sosgps.v21.dao.TargetTemplateDao;
import com.sosgps.v21.cash.service.CashService;
import com.sosgps.v21.model.Cash;
import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.signBill.service.impl.SignBillServiceImpl;
import com.sosgps.v21.util.DateUtils;
import com.sosgps.v21.util.TargetUtils;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;


public class CashServiceImpl implements CashService {

	private static final Logger logger = LoggerFactory
			.getLogger(SignBillServiceImpl.class);

	private CashDao cashDao;

	private TargetDao targetDao;

	private TargetTemplateDao targetTemplateDao;

	public TargetDao getTargetDao() {
		return targetDao;
	}

	public void setTargetDao(TargetDao targetDao) {
		this.targetDao = targetDao;
	}

	public TargetTemplateDao getTargetTemplateDao() {
		return targetTemplateDao;
	}

	public void setTargetTemplateDao(TargetTemplateDao targetTemplateDao) {
		this.targetTemplateDao = targetTemplateDao;
	}

	public CashDao getCashDao() {
		return cashDao;
	}

	public void setCashDao(CashDao cashDao) {
		this.cashDao = cashDao;
	}

	/**
	 * 回款额趋势图
	 * 
	 * @param userInfo
	 * @return
	 */
	public String getCashsByTime(HttpServletRequest request, UserInfo userInfo)
			throws Exception {
		StringBuffer json = new StringBuffer();
		String entCode = userInfo.getEmpCode();
		String targetTemplateType = userInfo.getTargetTemplateType();
		int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
				.valueOf(targetTemplateType);
		String poiName = request.getParameter("poiName");
		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String reportYear = request.getParameter("reportYear");
		String reportNum = request.getParameter("reportNum");
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);

		Date d = new Date();
		Long startTime = DateUtils.getStartTimeByTargetType(
				targetTemplateType_, d);
		Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
		if (reportYear != null && reportNum != null) {
			startTime = DateUtils.startTimeByTargetOn(targetTemplateType_,
					Integer.parseInt(reportYear), Integer.parseInt(reportNum));
			endTime = DateUtils.endTimeByTargetOn(targetTemplateType_,
					Integer.parseInt(reportYear), Integer.parseInt(reportNum));
		}
		// 当前签单额趋势值
		List<Object[]> cashs = cashDao.getCashsByTime(startTime, endTime,
				entCode, poiName, deviceIds);
		json.append("{chartDatas:[");
		if (cashs != null && cashs.size() >= 1) {
			StringBuffer sb = new StringBuffer();
			DecimalFormat df = new DecimalFormat("#.##");
			for (Iterator<Object[]> iterator = cashs.iterator(); iterator
					.hasNext();) {
				Object[] object_ = (Object[]) iterator.next();
				BigDecimal cashs_ = (BigDecimal) object_[0];
				String cashsS_ = "0";
				if (cashs_ != null) {
					Double cashsD_ = cashs_.doubleValue();
					cashsS_ = df.format(cashsD_);
				}
				String createon_ = (String) object_[1];
				if (targetTemplateType_ == 2) {
					String[] sArr = createon_.split("-");
					createon_ = sArr[0] + "年" + sArr[1] + "月" + sArr[2] + "日";
				}
				sb.append("{value:" + cashsS_ + ", date:'" + createon_ + "'},");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			json.append(sb.toString());
		}
		json.append("],");
		String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'"
				+ "回款额趋势图(" + DateUtils.dateTimeToStr(startTime, "yyyy年MM月")
				+ ")'" : "chartTitle:'" + "回款额趋势图("
				+ DateUtils.dateTimeToStr(startTime, "yyyy年MM月dd日") + "~"
				+ DateUtils.dateTimeToStr(endTime, "yyyy年MM月dd日") + ")'";
		json.append(chartTitle);
		// 取得目标模板时间范围内回款额总值
		List<BigDecimal> counts = cashDao.getCashCount(startTime, endTime,
				entCode, 0, 0, poiName, deviceIds);
		String counts_ = "0";
		if (counts != null && counts.size() >= 1) {
			Float countsF_ = (BigDecimal) counts.get(0) == null ? 0f
					: ((BigDecimal) counts.get(0)).floatValue();
			counts_ = CharTools.killNullFloat2String(countsF_, "0");
		}

		DecimalFormat df = new DecimalFormat("#.##");
		json.append(", unaudited:" + df.format(Double.parseDouble(counts_)));
		json.append(", startTime:" + startTime);
		json.append(", endTime:" + endTime);
		json.append("}");
		// logger.info("[getCashsByTime] json = "+json.toString());
		return json.toString();
	}

	/**
	 * 历史回款额趋势图
	 */
	public String getCashHisByTime(HttpServletRequest request, UserInfo userInfo)
			throws Exception {
		String entCode = userInfo.getEmpCode();
		String targetTemplateType = userInfo.getTargetTemplateType();
		int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
				.valueOf(targetTemplateType);
		String poiName = request.getParameter("poiName");
		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String reportYear = request.getParameter("reportYear");
		String reportNum = request.getParameter("reportNum");
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date d = new Date();
		Long startTime = DateUtils.getStartTimeByTargetTypeHis(
				targetTemplateType_, d);
		Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
		if (reportYear != null && reportNum != null) {
			startTime = DateUtils.startTimeByTargetOn(targetTemplateType_,
					Integer.parseInt(reportYear), Integer.parseInt(reportNum));
			endTime = DateUtils.endTimeByTargetOn(targetTemplateType_,
					Integer.parseInt(reportYear), Integer.parseInt(reportNum));
			startTime = DateUtils.getStartTimeByTargetTypeHis(
					targetTemplateType_, new Date(startTime));
			endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_,
					new Date(endTime));
		}
		// 按时间查询回款额趋势值
		List<Object[]> cashs = cashDao.getCashsByTime(startTime, endTime,
				entCode, poiName, deviceIds);
		String chartDatasJson = TargetUtils.getHisChartByTargetType(
				targetTemplateType_, cashs, "chartDatas");
		String chartTitleJson = TargetUtils.getHisChartTitle(startTime,
				endTime, targetTemplateType_, "回款额趋势图", "chartTitle");
		// 取得目标模板时间范围内签单额总值
		List<BigDecimal> counts = cashDao.getCashCount(startTime, endTime,
				entCode, 0, 0, poiName, deviceIds);
		String counts_ = "0";
		if (counts != null && counts.size() >= 1) {
			Float countsF_ = (BigDecimal) counts.get(0) == null ? 0f
					: ((BigDecimal) counts.get(0)).floatValue();
			counts_ = CharTools.killNullFloat2String(countsF_, "0");
		}
		DecimalFormat df = new DecimalFormat("#.##");
		String json = "{" + chartDatasJson + "," + chartTitleJson
				+ ", unaudited:" + df.format(Double.parseDouble(counts_))
				+ ", startTime:" + startTime + ", endTime:" + endTime + "}";
		// logger.info("[getCashHisByTime] json = "+json);
		return json;
	}

	/**
	 * 回款明细报表查询
	 */
	public String listCashDetails(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String poiName = request.getParameter("searchValue");// 客户名称

		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String approvedStr = request.getParameter("duration");// 状态:-1:全部,0:未审核,1:审核
		int approved = CharTools.str2Integer(approvedStr, -1);// 状态
		if (st == null || et == null || deviceIds.equals("")) {
			return "{result:\"9\"}";
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		Long startDateL = startDate.getTime();
		Long endDateL = endDate.getTime();
		if (start == null || limit == null || userInfo == null) {
			return "{result:\"9\"}";
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		// add by 2012-12-17  zss 查询销售上报日志记录
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
//		optLog.setOptDesc(userInfo.getUserAccount() + " 查询回款额明细");
//		optLog.setFunFType(LogConstants.LOG_STAT);
//		optLog.setFunCType(LogConstants.LOG_STAT_CashDetail);
	
		optLog.setOptDesc(userInfo.getUserAccount() + "查询销售上报");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_SalesReported);
		
		LogFactory.newLogInstance("optLogger").info(optLog);

		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Cash> list = cashDao.listCashDetails(entCode, pageNo,
					pageSize, startDateL, endDateL, approved, poiName,
					deviceIds);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				DecimalFormat df = new DecimalFormat("#.##");
				for (Cash Cash : list.getResult()) {
					Double cashAmountD = (BigDecimal) Cash.getCashAmount() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount()).doubleValue();
					Double cashAmount2D = (BigDecimal) Cash.getCashAmount2() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount2())
									.doubleValue();
					Double cashAmount3D = (BigDecimal) Cash.getCashAmount3() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount3())
									.doubleValue();
					Double cashAmount4D = (BigDecimal) Cash.getCashAmount4() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount4())
									.doubleValue();
					Double cashAmount5D = (BigDecimal) Cash.getCashAmount5() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount5())
									.doubleValue();
					Double cashAmount6D = (BigDecimal) Cash.getCashAmount6() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount6())
									.doubleValue();
					Double cashAmount7D = (BigDecimal) Cash.getCashAmount7() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount7())
									.doubleValue();
					Double cashAmount8D = (BigDecimal) Cash.getCashAmount8() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount8())
									.doubleValue();
					Double cashAmount9D = (BigDecimal) Cash.getCashAmount9() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount9())
									.doubleValue();
					Double cashAmount10D = (BigDecimal) Cash.getCashAmount10() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount10())
									.doubleValue();
					Double cashAmount11D = (BigDecimal) Cash.getCashAmount11() == null ? 0D
							: ((BigDecimal) Cash.getCashAmount11())
									.doubleValue();

					jsonSb.append("{");
					jsonSb.append("id:'" + Cash.getId() + "',");// id
					jsonSb.append("createOn:'"
							+ DateUtility.dateTimeToStr(new Date(Cash
									.getCreateOn())) + "',");// 日期
					jsonSb.append("groupName:'"
							+ CharTools.javaScriptEscape(Cash.getGroupName())
							+ "',");// 部门
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(Cash.getTerminalName())
							+ "',");// 员工姓名
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(Cash.getPoiName())
							+ "',");// 客户名称
					jsonSb.append("cashAmount:'" + df.format(cashAmountD)
							+ "',");
					jsonSb.append("cashAmount2:'" + df.format(cashAmount2D)
							+ "',");
					jsonSb.append("cashAmount3:'" + nf.format(cashAmount3D)
							+ "',");
					jsonSb.append("cashAmount4:'" + df.format(cashAmount4D)
							+ "',");
					jsonSb.append("cashAmount5:'" + df.format(cashAmount5D)
							+ "',");
					jsonSb.append("cashAmount6:'" + df.format(cashAmount6D)
							+ "',");
					jsonSb.append("cashAmount7:'" + df.format(cashAmount7D)
							+ "',");
					jsonSb.append("cashAmount8:'" + df.format(cashAmount8D)
							+ "',");
					jsonSb.append("cashAmount9:'" + df.format(cashAmount9D)
							+ "',");
					jsonSb.append("cashAmount10:'" + df.format(cashAmount10D)
							+ "',");
					jsonSb.append("cashAmount11:'" + df.format(cashAmount11D)
							+ "',");
					jsonSb.append("approved:'" + Cash.getApproved() + "'");// 状态
					jsonSb.append("},");

				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";
	}

	// add by 2102-12-12 销售上报导出

	public String listEnquiriesExpExcel(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {
		// 从request中获取参数
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String poiName = request.getParameter("searchValue");// 客户名称
		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String approvedStr = request.getParameter("duration");// 状态:-1:全部,0:未审核,1:审核
		int approved = CharTools.str2Integer(approvedStr, -1);// 状态
		System.out.println(st+et+deviceIds);
		if (st == null || et == null || deviceIds.equals("")) {
			return "{result:\"9\"}";
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		Long startDateL = startDate.getTime();
		Long endDateL = endDate.getTime();
		if (userInfo == null) {
			return "{result:\"9\"}";
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// add by 2012-12-17 zss 销售上报导出日志记录
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + "销售上报明细导出");
		optLog.setFunFType(LogConstants.LOG_STAT);
		optLog.setFunCType(LogConstants.LOG_STAT_SalesExcel);
		LogFactory.newLogInstance("optLogger").info(optLog);

		Page<Cash> list = cashDao.listCashDetails(entCode, 1, 65536,
				startDateL, endDateL, approved, poiName, deviceIds);
		ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
		// header
		excelWorkBook.addHeader("日期", 20);
		excelWorkBook.addHeader("部门", 15);
		excelWorkBook.addHeader("员工姓名", 15);
		excelWorkBook.addHeader("客户名称", 15);
		excelWorkBook.addHeader("满口香", 15);
		excelWorkBook.addHeader("地之香",15);
		excelWorkBook.addHeader("天之香", 15);
		excelWorkBook.addHeader("小青花窖藏",15);
		excelWorkBook.addHeader("小青花典藏", 15);
		excelWorkBook.addHeader("大青花臻藏",15);
		excelWorkBook.addHeader("大青花臻品", 15);
		excelWorkBook.addHeader("新五年原浆",15);
		excelWorkBook.addHeader("新十年原浆", 15);
		excelWorkBook.addHeader("十五年原浆",15);
		excelWorkBook.addHeader("状态", 15);
		int row = 0;
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			DecimalFormat df = new DecimalFormat("#.##");
			for (Cash cash : list.getResult()) {
				Double cashAmountD = (BigDecimal) cash.getCashAmount() == null ? 0D
						: ((BigDecimal) cash.getCashAmount()).doubleValue();
				Double cashAmount2D = (BigDecimal) cash.getCashAmount2() == null ? 0D
						: ((BigDecimal) cash.getCashAmount2()).doubleValue();
				Double cashAmount3D = (BigDecimal) cash.getCashAmount3() == null ? 0D
						: ((BigDecimal) cash.getCashAmount3()).doubleValue();
				Double cashAmount4D = (BigDecimal) cash.getCashAmount4() == null ? 0D
						: ((BigDecimal) cash.getCashAmount4()).doubleValue();
				Double cashAmount5D = (BigDecimal) cash.getCashAmount5() == null ? 0D
						: ((BigDecimal) cash.getCashAmount5()).doubleValue();
				Double cashAmount6D = (BigDecimal) cash.getCashAmount6() == null ? 0D
						: ((BigDecimal) cash.getCashAmount6()).doubleValue();
				Double cashAmount7D = (BigDecimal) cash.getCashAmount7() == null ? 0D
						: ((BigDecimal) cash.getCashAmount7()).doubleValue();
				Double cashAmount8D = (BigDecimal) cash.getCashAmount8() == null ? 0D
						: ((BigDecimal) cash.getCashAmount8()).doubleValue();
				Double cashAmount9D = (BigDecimal) cash.getCashAmount9() == null ? 0D
						: ((BigDecimal) cash.getCashAmount9()).doubleValue();
				Double cashAmount10D = (BigDecimal) cash.getCashAmount10() == null ? 0D
						: ((BigDecimal) cash.getCashAmount10()).doubleValue();

				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row, 
						DateUtility.dateTimeToStr(new Date(cash.getCreateOn())));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(cash.getGroupName()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(cash.getTerminalName()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(cash.getPoiName()));
				excelWorkBook.addCell(col++, row, df.format(cashAmountD));
				excelWorkBook.addCell(col++, row, df.format(cashAmount2D));
				excelWorkBook.addCell(col++, row, df.format(cashAmount3D));
				excelWorkBook.addCell(col++, row, df.format(cashAmount4D));
				excelWorkBook.addCell(col++, row, df.format(cashAmount5D));
				excelWorkBook.addCell(col++, row, df.format(cashAmount6D));
				excelWorkBook.addCell(col++, row, df.format(cashAmount7D));
				excelWorkBook.addCell(col++, row, df.format(cashAmount8D));
				excelWorkBook.addCell(col++, row, df.format(cashAmount9D));
				excelWorkBook.addCell(col++, row, df.format(cashAmount10D));

				excelWorkBook.addCell(col++, row,
						cash.getApproved() == 0 ? "未审核" : "审核");
			}

		}
		excelWorkBook.write();
		return "{result:\"1\"}";
	}

	/**
	 * 回款明细报表导出
	 */
	public String listCashDetailsExpExcel(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {

		// 从request中获取参数
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
		deviceIds = CharTools.javaScriptEscape(deviceIds);
		String poiName = request.getParameter("searchValue");// 客户名称
		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);
		String approvedStr = request.getParameter("duration");// 状态:-1:全部,0:未审核,1:审核
		int approved = CharTools.str2Integer(approvedStr, -1);// 状态
		if (st == null || et == null || deviceIds.equals("")) {
			return "{result:\"9\"}";
		}
		deviceIds = CharTools.splitAndAdd(deviceIds);
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		Long startDateL = startDate.getTime();
		Long endDateL = endDate.getTime();
		if (userInfo == null) {
			return "{result:\"9\"}";
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// searchLog
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + " 导出回款额明细");
		optLog.setFunFType(LogConstants.LOG_Exp);
		optLog.setFunCType(LogConstants.LOG_Exp_CashDetail);
		LogFactory.newLogInstance("optLogger").info(optLog);

		Page<Cash> list = cashDao.listCashDetails(entCode, 1, 65536,
				startDateL, endDateL, approved, poiName, deviceIds);
		ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
		// header
		excelWorkBook.addHeader("日期", 15);
		excelWorkBook.addHeader("部门", 15);
		excelWorkBook.addHeader("员工姓名", 15);
		excelWorkBook.addHeader("客户名称", 15);
//		Properties properties1 = (Properties) request.getAttribute("message");
		Properties properties = (Properties) request.getSession().getAttribute(
				"message");

		if (entCode.equals("tstx") || entCode.equals("cpbcs")
				|| entCode.equals("tspt")) {
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount2"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount3"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount4"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount5"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount6"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount7"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount8"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount9"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount10"), 15);
			excelWorkBook.addHeader(properties
					.getProperty("cn.net.sosgps.info.cash.cashAmount11"), 15);
		} else {
			excelWorkBook.addHeader("回款金额", 15);
		}
		excelWorkBook.addHeader("状态", 15);
		int row = 0;
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {

			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			DecimalFormat df = new DecimalFormat("#.##");
			for (Cash cash : list.getResult()) {
				Double cashAmountD = (BigDecimal) cash.getCashAmount() == null ? 0D
						: ((BigDecimal) cash.getCashAmount()).doubleValue();
				Double cashAmount2D = (BigDecimal) cash.getCashAmount2() == null ? 0D
						: ((BigDecimal) cash.getCashAmount2()).doubleValue();
				Double cashAmount3D = (BigDecimal) cash.getCashAmount3() == null ? 0D
						: ((BigDecimal) cash.getCashAmount3()).doubleValue();
				Double cashAmount4D = (BigDecimal) cash.getCashAmount4() == null ? 0D
						: ((BigDecimal) cash.getCashAmount4()).doubleValue();
				Double cashAmount5D = (BigDecimal) cash.getCashAmount5() == null ? 0D
						: ((BigDecimal) cash.getCashAmount5()).doubleValue();
				Double cashAmount6D = (BigDecimal) cash.getCashAmount6() == null ? 0D
						: ((BigDecimal) cash.getCashAmount6()).doubleValue();
				Double cashAmount7D = (BigDecimal) cash.getCashAmount7() == null ? 0D
						: ((BigDecimal) cash.getCashAmount7()).doubleValue();
				Double cashAmount8D = (BigDecimal) cash.getCashAmount8() == null ? 0D
						: ((BigDecimal) cash.getCashAmount8()).doubleValue();
				Double cashAmount9D = (BigDecimal) cash.getCashAmount9() == null ? 0D
						: ((BigDecimal) cash.getCashAmount9()).doubleValue();
				Double cashAmount10D = (BigDecimal) cash.getCashAmount10() == null ? 0D
						: ((BigDecimal) cash.getCashAmount10()).doubleValue();
				Double cashAmount11D = (BigDecimal) cash.getCashAmount11() == null ? 0D
						: ((BigDecimal) cash.getCashAmount11()).doubleValue();

				int col = 0;
				row += 1;
				excelWorkBook
						.addCell(col++, row, DateUtility
								.dateTimeToStr(new Date(cash.getCreateOn())));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(cash.getGroupName()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(cash.getTerminalName()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(cash.getPoiName()));
				if (entCode.equals("tstx") || entCode.equals("cpbcs")
						|| entCode.equals("tspt")) {
					excelWorkBook.addCell(col++, row, df.format(cashAmountD));
					excelWorkBook.addCell(col++, row, df.format(cashAmount2D));
					String cashAmount3S = nf.format(cashAmount3D);
					if (cashAmount3S.equals("0")) {
						cashAmount3S = "";
					} else if (cashAmount3S.equals("1")) {
						cashAmount3S = "新签";
					} else if (cashAmount3S.equals("2")) {
						cashAmount3S = "续单";
					} else if (cashAmount3S.equals("3")) {
						cashAmount3S = "续费";
					}
					excelWorkBook.addCell(col++, row, cashAmount3S);
					excelWorkBook.addCell(col++, row, df.format(cashAmount4D));
					excelWorkBook.addCell(col++, row, df.format(cashAmount5D));
					excelWorkBook.addCell(col++, row, df.format(cashAmount6D));
					excelWorkBook.addCell(col++, row, df.format(cashAmount7D));
					excelWorkBook.addCell(col++, row, df.format(cashAmount8D));
					excelWorkBook.addCell(col++, row, df.format(cashAmount9D));
					excelWorkBook.addCell(col++, row, df.format(cashAmount10D));
					excelWorkBook.addCell(col++, row, df.format(cashAmount11D));
				} else {
					excelWorkBook.addCell(col++, row, df.format(cashAmountD));
				}
				excelWorkBook.addCell(col++, row,
						cash.getApproved() == 0 ? "未审核" : "审核");
			}

		}
		excelWorkBook.write();
		return "{result:\"1\"}";
	}

	public String approved(ActionMapping mapping, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws Exception {
		String ids = request.getParameter("ids");
		ids = CharTools.javaScriptEscape(ids);
		if (ids.equals("") || userInfo == null) {
			return "{result:\"9\"}";
		}
		Long[] idsL = CharTools.convertionToLong(ids.split(","));
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// searchLog
		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		//optLog.setOptDesc(userInfo.getUserAccount() + " 审核回款额");
		
		//add by 2012-12-17 zss
		optLog.setOptDesc(userInfo.getUserAccount() + "审核"+entCode+"回款额");
		
		optLog.setFunFType(LogConstants.LOG_Approved);
		optLog.setFunCType(LogConstants.LOG_Approved_Cash);
		LogFactory.newLogInstance("optLogger").info(optLog);
		boolean flag = cashDao.approved(idsL, entCode);
		if (flag) {
			return "{result:\"1\"}";
		} else {
			return "{result:\"3\"}";
		}

	}

	/**
	 * 根据目标模板取得仪表盘值
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public String getGaugeByTargetType(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UserInfo userInfo) throws Exception {
		if (userInfo == null) {
			return "{result:\"9\"}";
		}
		StringBuffer json = new StringBuffer();
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();

		TOptLog optLog = new TOptLog();
		optLog.setEmpCode(entCode);
		optLog.setUserName(userInfo.getUserAccount());
		optLog.setAccessIp(userInfo.getIp());
		optLog.setUserId(userId);
		optLog.setOptTime(new Date());
		optLog.setResult(1L);
		optLog.setOptDesc(userInfo.getUserAccount() + " 回款额仪表盘");
		optLog.setFunFType(LogConstants.LOG_Gauge);
		optLog.setFunCType(LogConstants.LOG_Gauge_Cash);
		LogFactory.newLogInstance("optLogger").info(optLog);

		// 取得回款区间维护值,仪表盘红黄绿区间值
		int types = 2;// 1-签单额达成率,2-回款额达成率,3-费用额使用率,4-员工出访达成率,5-客户拜访覆盖率
		List<Kpi> gaugeInterval = targetDao.getKpi(entCode, types);
		if (gaugeInterval != null && gaugeInterval.size() > 0) {
			Kpi kpi_ = (Kpi) gaugeInterval.get(0);
			// 区间值
			String value = kpi_.getValue();
			String[] values = value.split(",");
			String red = "60";
			String yellow = "90";
			String green = "160";
			// 解析区间值
			if (values.length == 3) {
				red = values[0];
				yellow = values[1];
				green = values[2];
			}
			json.append("gaugeKpi:{red:" + red + ", yellow:" + yellow
					+ ", green:" + green + "},");
		} else {
			json.append("gaugeKpi:{red:60, yellow:90, green:160},");
		}
		DecimalFormat df = new DecimalFormat("#.##");
		String targetTemplateType = userInfo.getTargetTemplateType();
		// 目标模板类型 0:周;1:旬;2:月
		int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
				.valueOf(targetTemplateType);
		Date d = new Date();
		int targetOn = DateUtils.getTargetOnInThisYear(d, targetTemplateType_);
		int year = DateUtils.getCurrentYear(d);
		// 根据目标维护类型(周/旬/月)取得当前(周/旬/月)时间范围内的所有目标维护数据
		List<Object> targetTemplate_ = targetTemplateDao.getTargetTemplate(
				entCode, targetTemplateType_, year, targetOn, "cashAmount");
		String amount = "0";
		if (targetTemplate_ != null && targetTemplate_.size() >= 1) {
			Double amountD = (Double) targetTemplate_.get(0);
			amount = CharTools.killNullDouble2String(amountD, "0");
		}
		json.append("targetTemplates:" + df.format(Double.parseDouble(amount))
				+ ",");

		Date d1 = new Date();
		// 取得目标模板时间范围内签单额总值
		Long startTime = DateUtils.getStartTimeByTargetType(
				targetTemplateType_, d1);
		Long endTime = DateUtils
				.getEndTimeByTargetType(targetTemplateType_, d1);
		List<BigDecimal> counts = cashDao.getCashCount(startTime, endTime,
				entCode, 0, 1, "", "");
		String counts_ = "0";
		if (counts != null && counts.size() >= 1) {
			Float countsF_ = (BigDecimal) counts.get(0) == null ? 0f
					: ((BigDecimal) counts.get(0)).floatValue();
			counts_ = CharTools.killNullFloat2String(countsF_, "0");
		}

		json.append("totals:" + df.format(Double.parseDouble(counts_)) + ",");
		String needle = "0";
		if (!(amount.equals("0") || amount.equals("0.0"))) {
			needle = df.format(Double.parseDouble(counts_)
					/ Double.parseDouble(amount) * 100);
		}
		json.append("needle:" + needle);
		// logger.info("[getGaugeByTargetType] json = "+json.toString());
		return "{" + json.toString() + "}";

	}
}
