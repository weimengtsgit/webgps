package com.sosgps.v21.signBill.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import com.sosgps.v21.dao.SignBillDao;
import com.sosgps.v21.dao.TargetDao;
import com.sosgps.v21.dao.TargetTemplateDao;
import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.model.SignBill;
import com.sosgps.v21.signBill.service.SignBillService;
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

public class SignBillServiceImpl implements SignBillService {

    private SignBillDao signBillDao;

    private TargetDao targetDao;

    private TargetTemplateDao targetTemplateDao;

    public TargetTemplateDao getTargetTemplateDao() {
        return targetTemplateDao;
    }

    public void setTargetTemplateDao(TargetTemplateDao targetTemplateDao) {
        this.targetTemplateDao = targetTemplateDao;
    }

    public TargetDao getTargetDao() {
        return targetDao;
    }

    public void setTargetDao(TargetDao targetDao) {
        this.targetDao = targetDao;
    }

    public SignBillDao getSignBillDao() {
        return signBillDao;
    }

    public void setSignBillDao(SignBillDao signBillDao) {
        this.signBillDao = signBillDao;
    }

    /**
     * ǩ��������ͼ
     * 
     * @param userInfo
     * @return
     */
    public String getSignBillsByTime(HttpServletRequest request, UserInfo userInfo)
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
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        deviceIds = CharTools.splitAndAdd(deviceIds);

        Date d = new Date();
        Long startTime = 0L;
        Long endTime = 0L;
        if (reportYear != null && reportNum != null) {
            startTime = DateUtils.startTimeByTargetOn(targetTemplateType_,
                    Integer.parseInt(reportYear), Integer.parseInt(reportNum));
            endTime = DateUtils.endTimeByTargetOn(targetTemplateType_,
                    Integer.parseInt(reportYear), Integer.parseInt(reportNum));
        } else {
            startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d);
            endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        }
        // ��ǰǩ��������ֵ
        List<Object[]> signBills = signBillDao.getSignBillsByTime(startTime, endTime, entCode,
                poiName, deviceIds);
        json.append("{chartDatas:[");
        if (signBills != null && signBills.size() >= 1) {
            StringBuffer sb = new StringBuffer();
            DecimalFormat df = new DecimalFormat("#.##");
            for (Iterator<Object[]> iterator = signBills.iterator(); iterator.hasNext();) {
                Object[] object_ = (Object[]) iterator.next();
                BigDecimal sign_bills_ = (BigDecimal) object_[0];
                String sign_billsS_ = "0";
                if (sign_bills_ != null) {
                    Double sign_billsD_ = sign_bills_.doubleValue();
                    sign_billsS_ = df.format(sign_billsD_);
                }
                String createon_ = (String) object_[1];
                if (targetTemplateType_ == 2) {
                    String[] sArr = createon_.split("-");
                    createon_ = sArr[0] + "��" + sArr[1] + "��" + sArr[2] + "��";
                }
                sb.append("{value:" + sign_billsS_ + ", date:'" + createon_ + "'},");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            json.append(sb.toString());
        }
        json.append("],");
        String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'" + "ǩ��������ͼ("
                + DateUtils.dateTimeToStr(startTime, "yyyy��MM��") + ")'" : "chartTitle:'"
                + "ǩ��������ͼ(" + DateUtils.dateTimeToStr(startTime, "yyyy��MM��dd��") + "~"
                + DateUtils.dateTimeToStr(endTime, "yyyy��MM��dd��") + ")'";
        json.append(chartTitle);
        // ȡ��Ŀ��ģ��ʱ�䷶Χ��ǩ������ֵ
        List<BigDecimal> counts = signBillDao.getSignBillCount(startTime, endTime, entCode, 0, 0,
                poiName, deviceIds);
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
        // logger.info("[getSignBillsByTime] json = "+json.toString());
        return json.toString();
    }

    /**
     * ��ʷǩ��������ͼ
     */
    public String getSignBillHisByTime(HttpServletRequest request, UserInfo userInfo)
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
        // ��ʱ���ѯǩ��������ֵ
        List<Object[]> signBills = signBillDao.getSignBillsByTime(startTime, endTime, entCode,
                poiName, deviceIds);
        String chartDatasJson = TargetUtils.getHisChartByTargetType(targetTemplateType_, signBills,
                "chartDatas");
        String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime,
                targetTemplateType_, "ǩ��������ͼ", "chartTitle");
        // ȡ��Ŀ��ģ��ʱ�䷶Χ��ǩ������ֵ
        List<BigDecimal> counts = signBillDao.getSignBillCount(startTime, endTime, entCode, 0, 0,
                poiName, deviceIds);
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
        // logger.info("[getSignBillHisByTime] json = "+json);
        return json;
    }

    /**
     * ǩ����ϸ����
     */
    public String listSignBillDetails(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {

        // ��request�л�ȡ����
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        String poiName = request.getParameter("searchValue");// �ͻ�����
        poiName = CharTools.killNullString(poiName);
        poiName = java.net.URLDecoder.decode(poiName, "utf-8");
        poiName = CharTools.killNullString(poiName);
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

        // searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " ��ѯǩ������ϸ");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_SignBillDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        int startint = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = 0;
        if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
            Page<SignBill> list = signBillDao.listSignBillDetails(entCode, pageNo, pageSize,
                    startDateL, endDateL, approved, poiName, deviceIds);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                total = list.getTotalCount();
                NumberFormat nf = NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                DecimalFormat df = new DecimalFormat("#.##");
                for (SignBill signBill : list.getResult()) {
                    Double signBillAmountD = (BigDecimal) signBill.getSignBillAmount() == null ? 0D
                            : ((BigDecimal) signBill.getSignBillAmount()).doubleValue();

                    jsonSb.append("{");
                    jsonSb.append("id:'" + signBill.getId() + "',");// id
                    jsonSb.append("createOn:'"
                            + DateUtility.dateTimeToStr(new Date(signBill.getCreateOn())) + "',");// ����
                    jsonSb.append("groupName:'"
                            + CharTools.javaScriptEscape(signBill.getGroupName()) + "',");// ����
                    jsonSb.append("vehicleNumber:'"
                            + CharTools.javaScriptEscape(signBill.getTerminalName()) + "',");// Ա������
                    jsonSb.append("poiName:'" + CharTools.javaScriptEscape(signBill.getPoiName())
                            + "',");// �ͻ�����
                    jsonSb.append("signBillAmount:'" + df.format(signBillAmountD) + "',");// ǩ�����
                    jsonSb.append("approved:'" + signBill.getApproved() + "'");// ״̬
                    jsonSb.append("},");

                }
                if (jsonSb.length() > 0) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            }
        }

        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";
    }

    /**
     * ǩ����ϸ������
     */
    public String listSignBillDetailsExpExcel(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {

        // ��request�л�ȡ����
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        String poiName = request.getParameter("searchValue");// �ͻ�����
        poiName = CharTools.killNullString(poiName);
        poiName = java.net.URLDecoder.decode(poiName, "utf-8");
        poiName = CharTools.killNullString(poiName);
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
        // searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " ����ǩ������ϸ");
        optLog.setFunFType(LogConstants.LOG_Exp);
        optLog.setFunCType(LogConstants.LOG_Exp_SignBillDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        Page<SignBill> list = signBillDao.listSignBillDetails(entCode, 1, 65536, startDateL,
                endDateL, approved, poiName, deviceIds);
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("����", 15);
        excelWorkBook.addHeader("����", 15);
        excelWorkBook.addHeader("Ա������", 15);
        excelWorkBook.addHeader("�ͻ�����", 15);
        excelWorkBook.addHeader("ǩ�����", 15);
        excelWorkBook.addHeader("״̬", 15);
        int row = 0;
        if (list != null && list.getResult() != null && list.getResult().size() > 0) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            DecimalFormat df = new DecimalFormat("#.##");
            for (SignBill signBill : list.getResult()) {
                Double signBillAmountD = (BigDecimal) signBill.getSignBillAmount() == null ? 0D
                        : ((BigDecimal) signBill.getSignBillAmount()).doubleValue();

                int col = 0;
                row += 1;
                excelWorkBook.addCell(col++, row,
                        DateUtility.dateTimeToStr(new Date(signBill.getCreateOn())));
                excelWorkBook.addCell(col++, row,
                        CharTools.javaScriptEscape(signBill.getGroupName()));
                excelWorkBook.addCell(col++, row,
                        CharTools.javaScriptEscape(signBill.getTerminalName()));
                excelWorkBook
                        .addCell(col++, row, CharTools.javaScriptEscape(signBill.getPoiName()));
                excelWorkBook.addCell(col++, row, df.format(signBillAmountD));
                excelWorkBook.addCell(col++, row, signBill.getApproved() == 0 ? "δ���" : "���");
            }

        }
        excelWorkBook.write();
        return "{result:\"1\"}";
    }

    /**
     * ǩ�����ͨ��
     */
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ���ǩ����");
        optLog.setFunFType(LogConstants.LOG_Approved);
        optLog.setFunCType(LogConstants.LOG_Approved_SignBill);
        LogFactory.newLogInstance("optLogger").info(optLog);
        boolean flag = signBillDao.approved(idsL, entCode);
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
        // ��ѯ�Ǳ�����־���
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " ǩ�����Ǳ���");
        optLog.setFunFType(LogConstants.LOG_Gauge);
        optLog.setFunCType(LogConstants.LOG_Gauge_SignBill);
        LogFactory.newLogInstance("optLogger").info(optLog);

        // ȡ��ǩ������ά��ֵ,�Ǳ��̺��������ֵ
        int types = 1;// 1-ǩ��������,2-�ؿ������,3-���ö�ʹ����,4-Ա�����ô����,5-�ͻ��ݷø�����
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
                red = values[0];
                yellow = values[1];
                green = values[2];
            }
            json.append("gaugeKpi:{red:" + red + ", yellow:" + yellow + ", green:" + green + "},");
        } else {
            json.append("gaugeKpi:{red:60, yellow:90, green:160},");
        }

        DecimalFormat df = new DecimalFormat("#.##");
        String targetTemplateType = userInfo.getTargetTemplateType();
        // Ŀ��ģ������ 0:��;1:Ѯ;2:��
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        int targetOn = DateUtils.getTargetOnInThisYear(d, targetTemplateType_);
        int year = DateUtils.getCurrentYear(d);
        // ����Ŀ��ά������(��/Ѯ/��)ȡ�õ�ǰ(��/Ѯ/��)ʱ�䷶Χ�ڵ�����Ŀ��ά������
        List<Object> targetTemplate_ = targetTemplateDao.getTargetTemplate(entCode,
                targetTemplateType_, year, targetOn, "billAmount");
        String amount = "0";
        if (targetTemplate_ != null && targetTemplate_.size() >= 1) {
            Double amountD = (Double) targetTemplate_.get(0);
            amount = CharTools.killNullDouble2String(amountD, "0");
        }
        json.append("targetTemplates:" + df.format(Double.parseDouble(amount)) + ",");

        Date d1 = new Date();
        // ȡ��Ŀ��ģ��ʱ�䷶Χ��ǩ������ֵ
        Long startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d1);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d1);
        List<BigDecimal> counts = signBillDao.getSignBillCount(startTime, endTime, entCode, 0, 1,
                "", "");
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
        // logger.info("[getGaugeByTargetType] json = "+json.toString());
        return "{" + json.toString() + "}";

    }
}
