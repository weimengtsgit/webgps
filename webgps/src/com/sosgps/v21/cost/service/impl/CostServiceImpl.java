package com.sosgps.v21.cost.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;

import com.sosgps.v21.cost.service.CostService;
import com.sosgps.v21.dao.CostDao;
import com.sosgps.v21.dao.TargetDao;
import com.sosgps.v21.dao.TargetTemplateDao;
import com.sosgps.v21.model.Cost;
import com.sosgps.v21.model.Kpi;
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

public class CostServiceImpl implements CostService {

    //private static final Logger logger = LoggerFactory.getLogger(CostServiceImpl.class);
    private static final Logger logger = Logger.getLogger(CostServiceImpl.class);

    private CostDao costDao;

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

    public CostDao getCostDao() {
        return costDao;
    }

    public void setCostDao(CostDao costDao) {
        this.costDao = costDao;
    }

    /**
     * 费用额趋势图
     * 
     * @param userInfo
     * @return
     */
    public String getCostsByTime(UserInfo userInfo) {
        StringBuffer json = new StringBuffer();
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        //当前费用额趋势值
        List<Object[]> costs = costDao.getCostsByTime(startTime, endTime, entCode);
        json.append("{chartDatas:[");
        if (costs != null && costs.size() >= 1) {
            StringBuffer sb = new StringBuffer();
            for (Iterator<Object[]> iterator = costs.iterator(); iterator.hasNext();) {
                Object[] object_ = (Object[]) iterator.next();
                BigDecimal costs_ = (BigDecimal) object_[0];
                String costsS_ = "0";
                if (costs_ != null) {
                    Double costsD_ = costs_.doubleValue();
                    costsS_ = CharTools.killNullDouble2String(costsD_, "0");
                }
                String createon_ = (String) object_[1];
                if (targetTemplateType_ == 2) {
                    String[] sArr = createon_.split("-");
                    createon_ = sArr[0] + "年" + sArr[1] + "月" + sArr[2] + "日";
                }
                sb.append("{value:" + costsS_ + ", date:'" + createon_ + "'},");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            json.append(sb.toString());
        }
        json.append("],");
        String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'" + "费用额趋势图("
                + DateUtils.dateTimeToStr(startTime, "yyyy年MM月") + ")'" : "chartTitle:'"
                + "费用额趋势图(" + DateUtils.dateTimeToStr(startTime, "yyyy年MM月dd日") + "~"
                + DateUtils.dateTimeToStr(endTime, "yyyy年MM月dd日") + ")'";
        json.append(chartTitle);
        // 取得目标模板时间范围内费用额总值
        List<BigDecimal> counts = costDao.getCostCount(startTime, endTime, entCode, 0, 0, "");
        String counts_ = "0";
        if (counts != null && counts.size() >= 1) {
            Float countsF_ = (BigDecimal) counts.get(0) == null ? 0f : ((BigDecimal) counts.get(0))
                    .floatValue();
            counts_ = CharTools.killNullFloat2String(countsF_, "0");
        }

        DecimalFormat df = new DecimalFormat("#.##");
        json.append(", unaudited:" + df.format(Double.parseDouble(counts_)));
        json.append(", startTime:" + startTime);
        json.append(", endTime:" + endTime);
        json.append("}");
        logger.info("[getCostsByTime] json = " + json.toString());
        return json.toString();
    }

    /**
     * 费用额趋势图
     * 
     * @param userInfo
     * @return
     */
    public String getCostReportByTime(HttpServletRequest request, UserInfo userInfo) {
        StringBuffer json = new StringBuffer();
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        String reportYear = request.getParameter("reportYear");
        String reportNum = request.getParameter("reportNum");
        String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        deviceIds = CharTools.splitAndAdd(deviceIds);

        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        if (reportYear != null && reportNum != null) {
            startTime = DateUtils.startTimeByTargetOn(targetTemplateType_,
                    Integer.parseInt(reportYear), Integer.parseInt(reportNum));
            endTime = DateUtils.endTimeByTargetOn(targetTemplateType_,
                    Integer.parseInt(reportYear), Integer.parseInt(reportNum));
        }
        //当前费用额趋势值
        List<Object[]> costs = costDao.getCostsByTime(startTime, endTime, entCode, deviceIds);
        json.append("{chartDatas:[");
        if (costs != null && costs.size() >= 1) {
            StringBuffer sb = new StringBuffer();
            for (Iterator<Object[]> iterator = costs.iterator(); iterator.hasNext();) {
                Object[] object_ = (Object[]) iterator.next();
                String costs_ = (String) object_[0];
                if (costs_ == null || costs_.length() <= 0) {
                    costs_ = "0,0,0,0,0,0";
                }
                String createon_ = (String) object_[1];
                if (targetTemplateType_ == 2) {
                    createon_ = (createon_.split("-"))[2];
                }
                sb.append("{value:'" + costs_ + "', date:'" + createon_ + "'},");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            json.append(sb.toString());
        }
        json.append("],");
        String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'" + "费用额趋势图("
                + DateUtils.dateTimeToStr(startTime, "yyyy年MM月") + ")'" : "chartTitle:'"
                + "费用额趋势图(" + DateUtils.dateTimeToStr(startTime, "yyyy年MM月dd日") + "~"
                + DateUtils.dateTimeToStr(endTime, "yyyy年MM月dd日") + ")'";
        json.append(chartTitle);
        // 取得目标模板时间范围内费用额总值
        List<BigDecimal> counts = costDao
                .getCostCount(startTime, endTime, entCode, 0, 0, deviceIds);
        String counts_ = "0";
        if (counts != null && counts.size() >= 1) {
            Float countsF_ = (BigDecimal) counts.get(0) == null ? 0f : ((BigDecimal) counts.get(0))
                    .floatValue();
            counts_ = CharTools.killNullFloat2String(countsF_, "0");
        }
        DecimalFormat df = new DecimalFormat("#.##");
        json.append(", unaudited:" + df.format(Double.parseDouble(counts_)));
        json.append(", startTime:" + startTime);
        json.append(", endTime:" + endTime);
        json.append("}");
        logger.info("[getCostsByTime] json = " + json.toString());
        return json.toString();
    }

    /**
     * 历史费用额趋势图
     */
    public String getCostHisByTime(HttpServletRequest request, UserInfo userInfo) {
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetTypeHis(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        //按时间查询费用额趋势值
        List<Object[]> costs = costDao.getCostsByTime(startTime, endTime, entCode);
        String chartDatasJson = TargetUtils.getHisChartByTargetType(targetTemplateType_, costs,
                "chartDatas");
        String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime,
                targetTemplateType_, "费用额趋势图", "chartTitle");
        // 取得目标模板时间范围内费用额总值
        List<BigDecimal> counts = costDao.getCostCount(startTime, endTime, entCode, 0, 0, "");
        String counts_ = "0";
        if (counts != null && counts.size() >= 1) {
            Float countsF_ = (BigDecimal) counts.get(0) == null ? 0f : ((BigDecimal) counts.get(0))
                    .floatValue();
            counts_ = CharTools.killNullFloat2String(countsF_, "0");
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String json = "{" + chartDatasJson + "," + chartTitleJson + ", unaudited:"
                + df.format(Double.parseDouble(counts_)) + ", startTime:" + startTime
                + ", endTime:" + endTime + "}";

        logger.info("[getCostHisByTime] json = " + json);
        return json;
    }

    /**
     * 历史费用额趋势图
     */
    public String getCostHisReportByTime(HttpServletRequest request, UserInfo userInfo) {
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        String reportYear = request.getParameter("reportYear");
        String reportNum = request.getParameter("reportNum");
        String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        deviceIds = CharTools.splitAndAdd(deviceIds);
        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetTypeHis(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        if (reportYear != null && reportNum != null) {
            startTime = DateUtils.startTimeByTargetOn(targetTemplateType_,
                    Integer.parseInt(reportYear), Integer.parseInt(reportNum));
            endTime = DateUtils.endTimeByTargetOn(targetTemplateType_,
                    Integer.parseInt(reportYear), Integer.parseInt(reportNum));
            startTime = DateUtils.getStartTimeByTargetTypeHis(targetTemplateType_, new Date(
                    startTime));
            endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, new Date(endTime));
        }
        //按时间查询费用额趋势值
        List<Object[]> costs = costDao.getCostsByTime(startTime, endTime, entCode, deviceIds);
        String chartDatasJson = TargetUtils.getHisChartByTargetTypeCost(targetTemplateType_, costs,
                "chartDatas");
        String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime,
                targetTemplateType_, "费用额趋势图", "chartTitle");
        // 取得目标模板时间范围内费用额总值
        List<BigDecimal> counts = costDao
                .getCostCount(startTime, endTime, entCode, 0, 0, deviceIds);
        String counts_ = "0";
        if (counts != null && counts.size() >= 1) {
            Float countsF_ = (BigDecimal) counts.get(0) == null ? 0f : ((BigDecimal) counts.get(0))
                    .floatValue();
            counts_ = CharTools.killNullFloat2String(countsF_, "0");
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String json = "{" + chartDatasJson + "," + chartTitleJson + ", unaudited:"
                + df.format(Double.parseDouble(counts_)) + ", startTime:" + startTime
                + ", endTime:" + endTime + "}";

        logger.info("[getCostHisByTime] json = " + json);
        return json;
    }

    /**
     * 历史费用额趋势图
     */
    /*public String getCostHisByTime(UserInfo userInfo){
    	String entCode = userInfo.getEmpCode();
    	String targetTemplateType = userInfo.getTargetTemplateType();
    	int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer.valueOf(targetTemplateType);
    	Date d = new Date();
    	Long startTime = DateUtils.getStartTimeByTargetTypeHis(targetTemplateType_, d);
    	Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
    	//按时间查询费用额趋势值
    	List<Object[]> costs = costDao.getCostsByTime(startTime, endTime, entCode);
    	String chartDatasJson = TargetUtils.getHisChartByTargetTypeCost(targetTemplateType_, costs, "chartDatas");
    	String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime, targetTemplateType_, "费用额趋势图", "chartTitle");
    	// 取得目标模板时间范围内费用额总值
    	List<Double> counts = costDao.getCostCount(startTime,
    			endTime, entCode, 0, 0);
    	String counts_ = "0";
    	if (counts != null && counts.size() >= 1) {
    		Double countsD_ = counts.get(0);
    		counts_ = CharTools.killNullDouble2String(countsD_, "0");
    	}
    	String json = "{"+chartDatasJson+","+chartTitleJson+", unaudited:"+counts_+"}";
    	
    	logger.info("[getCostHisByTime] json = "+json);
    	return json;
    }*/

    public String listCostDetails(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {

        // 从request中获取参数
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
        String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        //String poiName = request.getParameter("searchValue");// 客户名称
        //poiName = CharTools.killNullString(poiName);
        //poiName = java.net.URLDecoder.decode(poiName, "utf-8");
        //poiName = CharTools.killNullString(poiName);
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

        //searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " 查询费用额明细");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_CostDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        int startint = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = 0;
        if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
            Page<Cost> list = costDao.listCostDetails(entCode, pageNo, pageSize, startDateL,
                    endDateL, approved, deviceIds);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                total = list.getTotalCount();
                NumberFormat nf = NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                for (Cost Cost : list.getResult()) {
                    Double mealD = (BigDecimal) Cost.getMeal() == null ? 0D : ((BigDecimal) Cost
                            .getMeal()).doubleValue();
                    Double transportationD = (BigDecimal) Cost.getTransportation() == null ? 0D
                            : ((BigDecimal) Cost.getTransportation()).doubleValue();
                    Double accommodationD = (BigDecimal) Cost.getAccommodation() == null ? 0D
                            : ((BigDecimal) Cost.getAccommodation()).doubleValue();
                    Double communicationD = (BigDecimal) Cost.getCommunication() == null ? 0D
                            : ((BigDecimal) Cost.getCommunication()).doubleValue();
                    Double giftD = (BigDecimal) Cost.getGift() == null ? 0D : ((BigDecimal) Cost
                            .getGift()).doubleValue();
                    Double otherD = (BigDecimal) Cost.getOther() == null ? 0D : ((BigDecimal) Cost
                            .getOther()).doubleValue();
                    String costAmount = nf.format(mealD + transportationD + accommodationD
                            + communicationD + giftD + otherD);
                    jsonSb.append("{");
                    jsonSb.append("id:'" + Cost.getId() + "',");// id
                    jsonSb.append("createOn:'"
                            + DateUtility.dateTimeToStr(new Date(Cost.getLastUpdateOn())) + "',");// 日期
                    jsonSb.append("groupName:'" + CharTools.javaScriptEscape(Cost.getGroupName())
                            + "',");// 部门
                    jsonSb.append("vehicleNumber:'"
                            + CharTools.javaScriptEscape(Cost.getTerminalName()) + "',");// 员工姓名
                    jsonSb.append("meal:'" + nf.format(mealD) + "',");// 回款金额
                    jsonSb.append("transportation:'" + nf.format(transportationD) + "',");// 回款金额
                    jsonSb.append("accommodation:'" + nf.format(accommodationD) + "',");// 回款金额
                    jsonSb.append("communication:'" + nf.format(communicationD) + "',");// 回款金额
                    jsonSb.append("gift:'" + nf.format(giftD) + "',");// 回款金额
                    jsonSb.append("other:'" + nf.format(otherD) + "',");// 回款金额
                    jsonSb.append("costAmount:'" + costAmount + "',");// 费用金额
                    //add by renxianliang for wenan 2013-1-21
                    jsonSb.append("remarks:'" + Cost.getRemarks() + "',");// 备注
                    if ("".equals(Cost.getDateTime()) || Cost.getDateTime() == null) {
                        jsonSb.append("dateTime:'" + "" + "',");// 费用时间
                    } else {
                        jsonSb.append("dateTime:'"
                                + DateUtility.dateTimeToStr(new Date(Cost.getDateTime())) + "',");// 费用时间
                    }
                    jsonSb.append("approved:'" + Cost.getApproved() + "'");// 状态
                    jsonSb.append("},");

                }
                if (jsonSb.length() > 0) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            }
        }

        logger.info("[listCostDetails] json = {total:" + total + ",data:[" + jsonSb.toString()
                + "]}");
        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";
    }

    public String listCostDetailsExpExcel(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {

        // 从request中获取参数
        String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
        String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
        deviceIds = CharTools.javaScriptEscape(deviceIds);
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

        //searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " 导出费用明细");
        optLog.setFunFType(LogConstants.LOG_Exp);
        optLog.setFunCType(LogConstants.LOG_Exp_CostDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        Page<Cost> list = costDao.listCostDetails(entCode, 1, 65536, startDateL, endDateL,
                approved, deviceIds);
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("日期", 20);
        excelWorkBook.addHeader("部门", 15);
        excelWorkBook.addHeader("员工姓名", 15);
        excelWorkBook.addHeader("餐费", 15);
        excelWorkBook.addHeader("交通费", 15);
        excelWorkBook.addHeader("住宿费", 15);
        excelWorkBook.addHeader("通信费", 15);
        excelWorkBook.addHeader("礼品费", 15);
        excelWorkBook.addHeader("其他", 15);
        excelWorkBook.addHeader("总金额", 15);
        //add by renxianliang for wenan 2013-1-21
        Properties properties = (Properties) request.getSession().getAttribute("message");
        if (entCode.equals("wakj") || entCode.equals("ytsm")) {
            excelWorkBook.addHeader(properties.getProperty("cn.net.sosgps.info.cost.costRemarks"),
                    15);
            excelWorkBook.addHeader(properties.getProperty("cn.net.sosgps.info.cost.costDateTime"),
                    20);
        }
        excelWorkBook.addHeader("状态", 15);
        int row = 0;
        if (list != null && list.getResult() != null && list.getResult().size() > 0) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            for (Cost cost : list.getResult()) {
                Double mealD = (BigDecimal) cost.getMeal() == null ? 0D : ((BigDecimal) cost
                        .getMeal()).doubleValue();
                Double transportationD = (BigDecimal) cost.getTransportation() == null ? 0D
                        : ((BigDecimal) cost.getTransportation()).doubleValue();
                Double accommodationD = (BigDecimal) cost.getAccommodation() == null ? 0D
                        : ((BigDecimal) cost.getAccommodation()).doubleValue();
                Double communicationD = (BigDecimal) cost.getCommunication() == null ? 0D
                        : ((BigDecimal) cost.getCommunication()).doubleValue();
                Double giftD = (BigDecimal) cost.getGift() == null ? 0D : ((BigDecimal) cost
                        .getGift()).doubleValue();
                Double otherD = (BigDecimal) cost.getOther() == null ? 0D : ((BigDecimal) cost
                        .getOther()).doubleValue();

                int col = 0;
                row += 1;
                String meal = nf.format(mealD);
                String transportation = nf.format(transportationD);
                String accommodation = nf.format(accommodationD);
                String communication = nf.format(communicationD);
                String gift = nf.format(giftD);
                String other = nf.format(otherD);
                String costAmount = nf.format(mealD + transportationD + accommodationD
                        + communicationD + giftD + otherD);
                excelWorkBook.addCell(col++, row,
                        DateUtility.dateTimeToStr(new Date(cost.getLastUpdateOn())));
                excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(cost.getGroupName()));
                excelWorkBook.addCell(col++, row,
                        CharTools.javaScriptEscape(cost.getTerminalName()));
                excelWorkBook.addCell(col++, row, meal);
                excelWorkBook.addCell(col++, row, transportation);
                excelWorkBook.addCell(col++, row, accommodation);
                excelWorkBook.addCell(col++, row, communication);
                excelWorkBook.addCell(col++, row, gift);
                excelWorkBook.addCell(col++, row, other);
                excelWorkBook.addCell(col++, row, costAmount);
                //add by renxianliang 2013-1-21 for wenan 
                if (entCode.equals("wakj") || entCode.equals("ytsm")) {
                    excelWorkBook.addCell(col++, row, cost.getRemarks());//备注
                    if ("".equals(cost.getDateTime()) || cost.getDateTime() == null) {
                        excelWorkBook.addCell(col++, row, "");//费用时间
                    } else {
                        excelWorkBook.addCell(col++, row,
                                DateUtility.dateTimeToStr(new Date(cost.getDateTime())));//费用时间
                    }
                }
                excelWorkBook.addCell(col++, row, cost.getApproved() == 0 ? "未审核" : "审核");
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
        //searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " 审核费用");
        optLog.setFunFType(LogConstants.LOG_Approved);
        optLog.setFunCType(LogConstants.LOG_Approved_Cost);
        LogFactory.newLogInstance("optLogger").info(optLog);
        boolean flag = costDao.approved(idsL, entCode);
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
    public String getGaugeByTargetType(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {
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
        optLog.setOptDesc(userInfo.getUserAccount() + " 费用额仪表盘");
        optLog.setFunFType(LogConstants.LOG_Gauge);
        optLog.setFunCType(LogConstants.LOG_Gauge_Cost);
        LogFactory.newLogInstance("optLogger").info(optLog);

        // 取得费用区间维护值,仪表盘红黄绿区间值
        int types = 3;// 1-费用额达成率,2-回款额达成率,3-费用额使用率,4-员工出访达成率,5-客户拜访覆盖率
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
                green = values[0];
                yellow = values[1];
                red = values[2];
            }
            json.append("gaugeKpi:{red:" + red + ", yellow:" + yellow + ", green:" + green + "},");
        } else {
            json.append("gaugeKpi:{red:60, yellow:90, green:160},");
        }

        DecimalFormat df = new DecimalFormat("#.##");
        String targetTemplateType = userInfo.getTargetTemplateType();
        //目标模板类型 0:周;1:旬;2:月
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        int targetOn = DateUtils.getTargetOnInThisYear(d, targetTemplateType_);
        int year = DateUtils.getCurrentYear(d);
        // 根据目标维护类型(周/旬/月)取得当前(周/旬/月)时间范围内的所有目标维护数据
        List<Object> targetTemplate_ = targetTemplateDao.getTargetTemplate(entCode,
                targetTemplateType_, year, targetOn, "costAmount");
        String amount = "0";
        if (targetTemplate_ != null && targetTemplate_.size() >= 1) {
            Double amountD = (Double) targetTemplate_.get(0);
            amount = CharTools.killNullDouble2String(amountD, "0");
        }
        json.append("targetTemplates:" + df.format(Double.parseDouble(amount)) + ",");

        Date d1 = new Date();
        // 取得目标模板时间范围内费用额总值
        Long startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d1);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d1);
        List<BigDecimal> counts = costDao.getCostCount(startTime, endTime, entCode, 0, 1, "");
        String counts_ = "0";
        if (counts != null && counts.size() >= 1) {
            Float countsF_ = (BigDecimal) counts.get(0) == null ? 0f : ((BigDecimal) counts.get(0))
                    .floatValue();
            counts_ = CharTools.killNullFloat2String(countsF_, "0");
        }

        json.append("totals:" + df.format(Double.parseDouble(counts_)) + ",");
        String needle = "0";
        if (!(amount.equals("0") || amount.equals("0.0"))) {
            needle = df.format(Double.parseDouble(counts_) / Double.parseDouble(amount) * 100);
        }
        json.append("needle:" + needle);
        //logger.info("[getGaugeByTargetType] json = "+json.toString());
        return "{" + json.toString() + "}";

    }

    /**
     * 新华脉集团定制化费用明细
     */
    @Override
    public String listCostDetailsForXinHuaMai(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {

        // 从request中获取参数
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
        String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
        deviceIds = CharTools.javaScriptEscape(deviceIds);
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

        //searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " 查询费用额明细");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_CostDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        int startint = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = 0;
        if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
            Page<Cost> list = costDao.listCostDetails(entCode, pageNo, pageSize, startDateL,
                    endDateL, approved, deviceIds);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                total = list.getTotalCount();
                NumberFormat nf = NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                for (Cost Cost : list.getResult()) {
                    Double mealD = (BigDecimal) Cost.getMeal() == null ? 0D : ((BigDecimal) Cost
                            .getMeal()).doubleValue();
                    Double transportationD = (BigDecimal) Cost.getTransportation() == null ? 0D
                            : ((BigDecimal) Cost.getTransportation()).doubleValue();
                    Double accommodationD = (BigDecimal) Cost.getAccommodation() == null ? 0D
                            : ((BigDecimal) Cost.getAccommodation()).doubleValue();
                    Double communicationD = (BigDecimal) Cost.getCommunication() == null ? 0D
                            : ((BigDecimal) Cost.getCommunication()).doubleValue();
                    Double giftD = (BigDecimal) Cost.getGift() == null ? 0D : ((BigDecimal) Cost
                            .getGift()).doubleValue();
                    Double otherD = (BigDecimal) Cost.getOther() == null ? 0D : ((BigDecimal) Cost
                            .getOther()).doubleValue();
                    Double expand1 = (BigDecimal) Cost.getExpand1() == null ? 0D
                            : ((BigDecimal) Cost.getExpand1()).doubleValue();
                    Double expand2 = (BigDecimal) Cost.getExpand2() == null ? 0D
                            : ((BigDecimal) Cost.getExpand2()).doubleValue();
                    Double expand3 = (BigDecimal) Cost.getExpand3() == null ? 0D
                            : ((BigDecimal) Cost.getExpand3()).doubleValue();
                    Double expand4 = (BigDecimal) Cost.getExpand4() == null ? 0D
                            : ((BigDecimal) Cost.getExpand4()).doubleValue();
                    Double expand5 = (BigDecimal) Cost.getExpand5() == null ? 0D
                            : ((BigDecimal) Cost.getExpand5()).doubleValue();
                    String costAmount = nf.format(mealD + transportationD + accommodationD
                            + communicationD + giftD + otherD + expand1 + expand2 + expand3
                            + expand4 + expand5);
                    jsonSb.append("{");
                    jsonSb.append("id:'" + Cost.getId() + "',");// id
                    jsonSb.append("createOn:'"
                            + DateUtility.dateTimeToStr(new Date(Cost.getLastUpdateOn())) + "',");// 日期
                    jsonSb.append("groupName:'" + CharTools.javaScriptEscape(Cost.getGroupName())
                            + "',");// 部门
                    jsonSb.append("vehicleNumber:'"
                            + CharTools.javaScriptEscape(Cost.getTerminalName()) + "',");// 员工姓名
                    jsonSb.append("meal:'" + nf.format(mealD) + "',");// 交通费
                    jsonSb.append("transportation:'" + nf.format(transportationD) + "',");// 住宿费
                    jsonSb.append("accommodation:'" + nf.format(accommodationD) + "',");// 福利费
                    jsonSb.append("communication:'" + nf.format(communicationD) + "',");// 投标费
                    jsonSb.append("gift:'" + nf.format(giftD) + "',");// 配件费
                    jsonSb.append("other:'" + nf.format(otherD) + "',");//样机运费
                    jsonSb.append("expand1:'" + nf.format(expand1) + "',");// 快递费
                    jsonSb.append("expand2:'" + nf.format(expand2) + "',");// 看机费
                    jsonSb.append("expand3:'" + nf.format(expand3) + "',");// 招待费
                    jsonSb.append("expand4:'" + nf.format(expand4) + "',");// 会议费
                    jsonSb.append("expand5:'" + nf.format(expand5) + "',");// 展会展位费
                    jsonSb.append("costAmount:'" + costAmount + "',");// 费用金额
                    jsonSb.append("approved:'" + Cost.getApproved() + "'");// 状态
                    jsonSb.append("},");

                }
                if (jsonSb.length() > 0) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            }
        }

        logger.info("[listCostDetails] json = {total:" + total + ",data:[" + jsonSb.toString()
                + "]}");
        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";
    }

    /**
     * 新华脉集团定制化费用明细
     */
    @Override
    public String listCostDetailsExpExcelForXinHuaMai(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response, UserInfo userInfo)
            throws Exception {

        // 从request中获取参数
        String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
        String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
        deviceIds = CharTools.javaScriptEscape(deviceIds);
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

        //searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " 导出费用明细");
        optLog.setFunFType(LogConstants.LOG_Exp);
        optLog.setFunCType(LogConstants.LOG_Exp_CostDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        Page<Cost> list = costDao.listCostDetails(entCode, 1, 65536, startDateL, endDateL,
                approved, deviceIds);
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("日期", 20);
        excelWorkBook.addHeader("部门", 15);
        excelWorkBook.addHeader("员工姓名", 15);
        excelWorkBook.addHeader("交通费", 15);
        excelWorkBook.addHeader("住宿费", 15);
        excelWorkBook.addHeader("福利费", 15);
        excelWorkBook.addHeader("投标费", 15);
        excelWorkBook.addHeader("配件费", 15);
        excelWorkBook.addHeader("样机运费", 15);
        excelWorkBook.addHeader("快递费", 15);
        excelWorkBook.addHeader("看机费", 15);
        excelWorkBook.addHeader("招待费", 15);
        excelWorkBook.addHeader("会议费", 15);
        excelWorkBook.addHeader("展会展位费", 15);
        excelWorkBook.addHeader("总金额", 15);
        excelWorkBook.addHeader("状态", 15);
        int row = 0;
        if (list != null && list.getResult() != null && list.getResult().size() > 0) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            for (Cost cost : list.getResult()) {
                Double mealD = (BigDecimal) cost.getMeal() == null ? 0D : ((BigDecimal) cost
                        .getMeal()).doubleValue();
                Double transportationD = (BigDecimal) cost.getTransportation() == null ? 0D
                        : ((BigDecimal) cost.getTransportation()).doubleValue();
                Double accommodationD = (BigDecimal) cost.getAccommodation() == null ? 0D
                        : ((BigDecimal) cost.getAccommodation()).doubleValue();
                Double communicationD = (BigDecimal) cost.getCommunication() == null ? 0D
                        : ((BigDecimal) cost.getCommunication()).doubleValue();
                Double giftD = (BigDecimal) cost.getGift() == null ? 0D : ((BigDecimal) cost
                        .getGift()).doubleValue();
                Double otherD = (BigDecimal) cost.getOther() == null ? 0D : ((BigDecimal) cost
                        .getOther()).doubleValue();
                Double expandD1 = (BigDecimal) cost.getExpand1() == null ? 0D : ((BigDecimal) cost
                        .getExpand1()).doubleValue();
                Double expandD2 = (BigDecimal) cost.getExpand2() == null ? 0D : ((BigDecimal) cost
                        .getExpand2()).doubleValue();
                Double expandD3 = (BigDecimal) cost.getExpand3() == null ? 0D : ((BigDecimal) cost
                        .getExpand3()).doubleValue();
                Double expandD4 = (BigDecimal) cost.getExpand4() == null ? 0D : ((BigDecimal) cost
                        .getExpand4()).doubleValue();
                Double expandD5 = (BigDecimal) cost.getExpand5() == null ? 0D : ((BigDecimal) cost
                        .getExpand5()).doubleValue();
                int col = 0;
                row += 1;
                String meal = nf.format(mealD);
                String transportation = nf.format(transportationD);
                String accommodation = nf.format(accommodationD);
                String communication = nf.format(communicationD);
                String gift = nf.format(giftD);
                String other = nf.format(otherD);
                String expand1 = nf.format(expandD1);
                String expand2 = nf.format(expandD2);
                String expand3 = nf.format(expandD3);
                String expand4 = nf.format(expandD4);
                String expand5 = nf.format(expandD5);
                String costAmount = nf.format(mealD + transportationD + accommodationD
                        + communicationD + giftD + otherD+expandD1+expandD2+expandD3+expandD4+expandD5);
                excelWorkBook.addCell(col++, row,
                        DateUtility.dateTimeToStr(new Date(cost.getLastUpdateOn())));
                excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(cost.getGroupName()));
                excelWorkBook.addCell(col++, row,
                        CharTools.javaScriptEscape(cost.getTerminalName()));
                excelWorkBook.addCell(col++, row, meal);
                excelWorkBook.addCell(col++, row, transportation);
                excelWorkBook.addCell(col++, row, accommodation);
                excelWorkBook.addCell(col++, row, communication);
                excelWorkBook.addCell(col++, row, gift);
                excelWorkBook.addCell(col++, row, other);
                excelWorkBook.addCell(col++, row, expand1);
                excelWorkBook.addCell(col++, row, expand2);
                excelWorkBook.addCell(col++, row, expand3);
                excelWorkBook.addCell(col++, row, expand4);
                excelWorkBook.addCell(col++, row, expand5);
                excelWorkBook.addCell(col++, row, costAmount);
                excelWorkBook.addCell(col++, row, cost.getApproved() == 0 ? "未审核" : "审核");
            }

        }
        excelWorkBook.write();
        return "{result:\"1\"}";
    }

}
