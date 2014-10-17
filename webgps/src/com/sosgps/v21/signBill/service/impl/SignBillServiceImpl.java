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
     * 签单额趋势图
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
        String deviceIds = request.getParameter("deviceIds");// 查询终端deviceId，多个","隔开
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
        // 当前签单额趋势值
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
                    createon_ = sArr[0] + "年" + sArr[1] + "月" + sArr[2] + "日";
                }
                sb.append("{value:" + sign_billsS_ + ", date:'" + createon_ + "'},");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            json.append(sb.toString());
        }
        json.append("],");
        String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'" + "签单额趋势图("
                + DateUtils.dateTimeToStr(startTime, "yyyy年MM月") + ")'" : "chartTitle:'"
                + "签单额趋势图(" + DateUtils.dateTimeToStr(startTime, "yyyy年MM月dd日") + "~"
                + DateUtils.dateTimeToStr(endTime, "yyyy年MM月dd日") + ")'";
        json.append(chartTitle);
        // 取得目标模板时间范围内签单额总值
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
     * 历史签单额趋势图
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
        // 按时间查询签单额趋势值
        List<Object[]> signBills = signBillDao.getSignBillsByTime(startTime, endTime, entCode,
                poiName, deviceIds);
        String chartDatasJson = TargetUtils.getHisChartByTargetType(targetTemplateType_, signBills,
                "chartDatas");
        String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime,
                targetTemplateType_, "签单额趋势图", "chartTitle");
        // 取得目标模板时间范围内签单额总值
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
     * 签单明细报表
     */
    public String listSignBillDetails(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {

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

        // searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " 查询签单额明细");
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
                            + DateUtility.dateTimeToStr(new Date(signBill.getCreateOn())) + "',");// 日期
                    jsonSb.append("groupName:'"
                            + CharTools.javaScriptEscape(signBill.getGroupName()) + "',");// 部门
                    jsonSb.append("vehicleNumber:'"
                            + CharTools.javaScriptEscape(signBill.getTerminalName()) + "',");// 员工姓名
                    jsonSb.append("poiName:'" + CharTools.javaScriptEscape(signBill.getPoiName())
                            + "',");// 客户名称
                    jsonSb.append("signBillAmount:'" + df.format(signBillAmountD) + "',");// 签单金额
                    jsonSb.append("approved:'" + signBill.getApproved() + "'");// 状态
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
     * 签单明细报表导出
     */
    public String listSignBillDetailsExpExcel(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {

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
        optLog.setOptDesc(userInfo.getUserAccount() + " 导出签单额明细");
        optLog.setFunFType(LogConstants.LOG_Exp);
        optLog.setFunCType(LogConstants.LOG_Exp_SignBillDetail);
        LogFactory.newLogInstance("optLogger").info(optLog);

        Page<SignBill> list = signBillDao.listSignBillDetails(entCode, 1, 65536, startDateL,
                endDateL, approved, poiName, deviceIds);
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("日期", 15);
        excelWorkBook.addHeader("部门", 15);
        excelWorkBook.addHeader("员工姓名", 15);
        excelWorkBook.addHeader("客户名称", 15);
        excelWorkBook.addHeader("签单金额", 15);
        excelWorkBook.addHeader("状态", 15);
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
                excelWorkBook.addCell(col++, row, signBill.getApproved() == 0 ? "未审核" : "审核");
            }

        }
        excelWorkBook.write();
        return "{result:\"1\"}";
    }

    /**
     * 签单审核通过
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
        optLog.setOptDesc(userInfo.getUserAccount() + " 审核签单额");
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
        // 查询仪表盘日志入库
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " 签单额仪表盘");
        optLog.setFunFType(LogConstants.LOG_Gauge);
        optLog.setFunCType(LogConstants.LOG_Gauge_SignBill);
        LogFactory.newLogInstance("optLogger").info(optLog);

        // 取得签单区间维护值,仪表盘红黄绿区间值
        int types = 1;// 1-签单额达成率,2-回款额达成率,3-费用额使用率,4-员工出访达成率,5-客户拜访覆盖率
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
            json.append("gaugeKpi:{red:" + red + ", yellow:" + yellow + ", green:" + green + "},");
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
        List<Object> targetTemplate_ = targetTemplateDao.getTargetTemplate(entCode,
                targetTemplateType_, year, targetOn, "billAmount");
        String amount = "0";
        if (targetTemplate_ != null && targetTemplate_.size() >= 1) {
            Double amountD = (Double) targetTemplate_.get(0);
            amount = CharTools.killNullDouble2String(amountD, "0");
        }
        json.append("targetTemplates:" + df.format(Double.parseDouble(amount)) + ",");

        Date d1 = new Date();
        // 取得目标模板时间范围内签单额总值
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
