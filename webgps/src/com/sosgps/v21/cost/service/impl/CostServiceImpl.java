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
     * ���ö�����ͼ
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
        //��ǰ���ö�����ֵ
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
                    createon_ = sArr[0] + "��" + sArr[1] + "��" + sArr[2] + "��";
                }
                sb.append("{value:" + costsS_ + ", date:'" + createon_ + "'},");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            json.append(sb.toString());
        }
        json.append("],");
        String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'" + "���ö�����ͼ("
                + DateUtils.dateTimeToStr(startTime, "yyyy��MM��") + ")'" : "chartTitle:'"
                + "���ö�����ͼ(" + DateUtils.dateTimeToStr(startTime, "yyyy��MM��dd��") + "~"
                + DateUtils.dateTimeToStr(endTime, "yyyy��MM��dd��") + ")'";
        json.append(chartTitle);
        // ȡ��Ŀ��ģ��ʱ�䷶Χ�ڷ��ö���ֵ
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
     * ���ö�����ͼ
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
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
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
        //��ǰ���ö�����ֵ
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
        String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'" + "���ö�����ͼ("
                + DateUtils.dateTimeToStr(startTime, "yyyy��MM��") + ")'" : "chartTitle:'"
                + "���ö�����ͼ(" + DateUtils.dateTimeToStr(startTime, "yyyy��MM��dd��") + "~"
                + DateUtils.dateTimeToStr(endTime, "yyyy��MM��dd��") + ")'";
        json.append(chartTitle);
        // ȡ��Ŀ��ģ��ʱ�䷶Χ�ڷ��ö���ֵ
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
     * ��ʷ���ö�����ͼ
     */
    public String getCostHisByTime(HttpServletRequest request, UserInfo userInfo) {
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetTypeHis(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        //��ʱ���ѯ���ö�����ֵ
        List<Object[]> costs = costDao.getCostsByTime(startTime, endTime, entCode);
        String chartDatasJson = TargetUtils.getHisChartByTargetType(targetTemplateType_, costs,
                "chartDatas");
        String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime,
                targetTemplateType_, "���ö�����ͼ", "chartTitle");
        // ȡ��Ŀ��ģ��ʱ�䷶Χ�ڷ��ö���ֵ
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
     * ��ʷ���ö�����ͼ
     */
    public String getCostHisReportByTime(HttpServletRequest request, UserInfo userInfo) {
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        String reportYear = request.getParameter("reportYear");
        String reportNum = request.getParameter("reportNum");
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
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
        //��ʱ���ѯ���ö�����ֵ
        List<Object[]> costs = costDao.getCostsByTime(startTime, endTime, entCode, deviceIds);
        String chartDatasJson = TargetUtils.getHisChartByTargetTypeCost(targetTemplateType_, costs,
                "chartDatas");
        String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime,
                targetTemplateType_, "���ö�����ͼ", "chartTitle");
        // ȡ��Ŀ��ģ��ʱ�䷶Χ�ڷ��ö���ֵ
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
     * ��ʷ���ö�����ͼ
     */
    /*public String getCostHisByTime(UserInfo userInfo){
    	String entCode = userInfo.getEmpCode();
    	String targetTemplateType = userInfo.getTargetTemplateType();
    	int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer.valueOf(targetTemplateType);
    	Date d = new Date();
    	Long startTime = DateUtils.getStartTimeByTargetTypeHis(targetTemplateType_, d);
    	Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
    	//��ʱ���ѯ���ö�����ֵ
    	List<Object[]> costs = costDao.getCostsByTime(startTime, endTime, entCode);
    	String chartDatasJson = TargetUtils.getHisChartByTargetTypeCost(targetTemplateType_, costs, "chartDatas");
    	String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime, targetTemplateType_, "���ö�����ͼ", "chartTitle");
    	// ȡ��Ŀ��ģ��ʱ�䷶Χ�ڷ��ö���ֵ
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

        // ��request�л�ȡ����
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        //String poiName = request.getParameter("searchValue");// �ͻ�����
        //poiName = CharTools.killNullString(poiName);
        //poiName = java.net.URLDecoder.decode(poiName, "utf-8");
        //poiName = CharTools.killNullString(poiName);
        String approvedStr = request.getParameter("duration");// ״̬:-1:ȫ��,0:δ���,1:���
        int approved = CharTools.str2Integer(approvedStr, -1);// ״̬
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ��ѯ���ö���ϸ");
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
                            + DateUtility.dateTimeToStr(new Date(Cost.getLastUpdateOn())) + "',");// ����
                    jsonSb.append("groupName:'" + CharTools.javaScriptEscape(Cost.getGroupName())
                            + "',");// ����
                    jsonSb.append("vehicleNumber:'"
                            + CharTools.javaScriptEscape(Cost.getTerminalName()) + "',");// Ա������
                    jsonSb.append("meal:'" + nf.format(mealD) + "',");// �ؿ���
                    jsonSb.append("transportation:'" + nf.format(transportationD) + "',");// �ؿ���
                    jsonSb.append("accommodation:'" + nf.format(accommodationD) + "',");// �ؿ���
                    jsonSb.append("communication:'" + nf.format(communicationD) + "',");// �ؿ���
                    jsonSb.append("gift:'" + nf.format(giftD) + "',");// �ؿ���
                    jsonSb.append("other:'" + nf.format(otherD) + "',");// �ؿ���
                    jsonSb.append("costAmount:'" + costAmount + "',");// ���ý��
                    //add by renxianliang for wenan 2013-1-21
                    jsonSb.append("remarks:'" + Cost.getRemarks() + "',");// ��ע
                    if ("".equals(Cost.getDateTime()) || Cost.getDateTime() == null) {
                        jsonSb.append("dateTime:'" + "" + "',");// ����ʱ��
                    } else {
                        jsonSb.append("dateTime:'"
                                + DateUtility.dateTimeToStr(new Date(Cost.getDateTime())) + "',");// ����ʱ��
                    }
                    jsonSb.append("approved:'" + Cost.getApproved() + "'");// ״̬
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

        // ��request�л�ȡ����
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        String approvedStr = request.getParameter("duration");// ״̬:-1:ȫ��,0:δ���,1:���
        int approved = CharTools.str2Integer(approvedStr, -1);// ״̬
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ����������ϸ");
        optLog.setFunFType(LogConstants.LOG_Exp);
        optLog.setFunCType(LogConstants.LOG_Exp_CostDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        Page<Cost> list = costDao.listCostDetails(entCode, 1, 65536, startDateL, endDateL,
                approved, deviceIds);
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("����", 20);
        excelWorkBook.addHeader("����", 15);
        excelWorkBook.addHeader("Ա������", 15);
        excelWorkBook.addHeader("�ͷ�", 15);
        excelWorkBook.addHeader("��ͨ��", 15);
        excelWorkBook.addHeader("ס�޷�", 15);
        excelWorkBook.addHeader("ͨ�ŷ�", 15);
        excelWorkBook.addHeader("��Ʒ��", 15);
        excelWorkBook.addHeader("����", 15);
        excelWorkBook.addHeader("�ܽ��", 15);
        //add by renxianliang for wenan 2013-1-21
        Properties properties = (Properties) request.getSession().getAttribute("message");
        if (entCode.equals("wakj") || entCode.equals("ytsm")) {
            excelWorkBook.addHeader(properties.getProperty("cn.net.sosgps.info.cost.costRemarks"),
                    15);
            excelWorkBook.addHeader(properties.getProperty("cn.net.sosgps.info.cost.costDateTime"),
                    20);
        }
        excelWorkBook.addHeader("״̬", 15);
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
                    excelWorkBook.addCell(col++, row, cost.getRemarks());//��ע
                    if ("".equals(cost.getDateTime()) || cost.getDateTime() == null) {
                        excelWorkBook.addCell(col++, row, "");//����ʱ��
                    } else {
                        excelWorkBook.addCell(col++, row,
                                DateUtility.dateTimeToStr(new Date(cost.getDateTime())));//����ʱ��
                    }
                }
                excelWorkBook.addCell(col++, row, cost.getApproved() == 0 ? "δ���" : "���");
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ��˷���");
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
     * ����Ŀ��ģ��ȡ���Ǳ���ֵ
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ���ö��Ǳ���");
        optLog.setFunFType(LogConstants.LOG_Gauge);
        optLog.setFunCType(LogConstants.LOG_Gauge_Cost);
        LogFactory.newLogInstance("optLogger").info(optLog);

        // ȡ�÷�������ά��ֵ,�Ǳ��̺��������ֵ
        int types = 3;// 1-���ö�����,2-�ؿ������,3-���ö�ʹ����,4-Ա�����ô����,5-�ͻ��ݷø�����
        List<Kpi> gaugeInterval = targetDao.getKpi(entCode, types);
        if (gaugeInterval != null && gaugeInterval.size() > 0) {
            Kpi kpi_ = (Kpi) gaugeInterval.get(0);
            // ����ֵ
            String value = kpi_.getValue();
            String[] values = value.split(",");
            String red = "60";
            String yellow = "90";
            String green = "160";
            // ��������ֵ
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
        //Ŀ��ģ������ 0:��;1:Ѯ;2:��
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        int targetOn = DateUtils.getTargetOnInThisYear(d, targetTemplateType_);
        int year = DateUtils.getCurrentYear(d);
        // ����Ŀ��ά������(��/Ѯ/��)ȡ�õ�ǰ(��/Ѯ/��)ʱ�䷶Χ�ڵ�����Ŀ��ά������
        List<Object> targetTemplate_ = targetTemplateDao.getTargetTemplate(entCode,
                targetTemplateType_, year, targetOn, "costAmount");
        String amount = "0";
        if (targetTemplate_ != null && targetTemplate_.size() >= 1) {
            Double amountD = (Double) targetTemplate_.get(0);
            amount = CharTools.killNullDouble2String(amountD, "0");
        }
        json.append("targetTemplates:" + df.format(Double.parseDouble(amount)) + ",");

        Date d1 = new Date();
        // ȡ��Ŀ��ģ��ʱ�䷶Χ�ڷ��ö���ֵ
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
     * �»������Ŷ��ƻ�������ϸ
     */
    @Override
    public String listCostDetailsForXinHuaMai(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {

        // ��request�л�ȡ����
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        String approvedStr = request.getParameter("duration");// ״̬:-1:ȫ��,0:δ���,1:���
        int approved = CharTools.str2Integer(approvedStr, -1);// ״̬
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ��ѯ���ö���ϸ");
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
                            + DateUtility.dateTimeToStr(new Date(Cost.getLastUpdateOn())) + "',");// ����
                    jsonSb.append("groupName:'" + CharTools.javaScriptEscape(Cost.getGroupName())
                            + "',");// ����
                    jsonSb.append("vehicleNumber:'"
                            + CharTools.javaScriptEscape(Cost.getTerminalName()) + "',");// Ա������
                    jsonSb.append("meal:'" + nf.format(mealD) + "',");// ��ͨ��
                    jsonSb.append("transportation:'" + nf.format(transportationD) + "',");// ס�޷�
                    jsonSb.append("accommodation:'" + nf.format(accommodationD) + "',");// ������
                    jsonSb.append("communication:'" + nf.format(communicationD) + "',");// Ͷ���
                    jsonSb.append("gift:'" + nf.format(giftD) + "',");// �����
                    jsonSb.append("other:'" + nf.format(otherD) + "',");//�����˷�
                    jsonSb.append("expand1:'" + nf.format(expand1) + "',");// ��ݷ�
                    jsonSb.append("expand2:'" + nf.format(expand2) + "',");// ������
                    jsonSb.append("expand3:'" + nf.format(expand3) + "',");// �д���
                    jsonSb.append("expand4:'" + nf.format(expand4) + "',");// �����
                    jsonSb.append("expand5:'" + nf.format(expand5) + "',");// չ��չλ��
                    jsonSb.append("costAmount:'" + costAmount + "',");// ���ý��
                    jsonSb.append("approved:'" + Cost.getApproved() + "'");// ״̬
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
     * �»������Ŷ��ƻ�������ϸ
     */
    @Override
    public String listCostDetailsExpExcelForXinHuaMai(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response, UserInfo userInfo)
            throws Exception {

        // ��request�л�ȡ����
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        String approvedStr = request.getParameter("duration");// ״̬:-1:ȫ��,0:δ���,1:���
        int approved = CharTools.str2Integer(approvedStr, -1);// ״̬
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ����������ϸ");
        optLog.setFunFType(LogConstants.LOG_Exp);
        optLog.setFunCType(LogConstants.LOG_Exp_CostDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        Page<Cost> list = costDao.listCostDetails(entCode, 1, 65536, startDateL, endDateL,
                approved, deviceIds);
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("����", 20);
        excelWorkBook.addHeader("����", 15);
        excelWorkBook.addHeader("Ա������", 15);
        excelWorkBook.addHeader("��ͨ��", 15);
        excelWorkBook.addHeader("ס�޷�", 15);
        excelWorkBook.addHeader("������", 15);
        excelWorkBook.addHeader("Ͷ���", 15);
        excelWorkBook.addHeader("�����", 15);
        excelWorkBook.addHeader("�����˷�", 15);
        excelWorkBook.addHeader("��ݷ�", 15);
        excelWorkBook.addHeader("������", 15);
        excelWorkBook.addHeader("�д���", 15);
        excelWorkBook.addHeader("�����", 15);
        excelWorkBook.addHeader("չ��չλ��", 15);
        excelWorkBook.addHeader("�ܽ��", 15);
        excelWorkBook.addHeader("״̬", 15);
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
                excelWorkBook.addCell(col++, row, cost.getApproved() == 0 ? "δ���" : "���");
            }

        }
        excelWorkBook.write();
        return "{result:\"1\"}";
    }

}
