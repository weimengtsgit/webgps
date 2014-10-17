package com.sosgps.v21.visit.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;

import cn.net.sosgps.memcache.bean.Terminal;

import com.sosgps.v21.dao.TargetDao;
import com.sosgps.v21.dao.TargetTemplateDao;
import com.sosgps.v21.dao.VisitDao;
import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.model.Visit;
import com.sosgps.v21.target.service.TargetService;
import com.sosgps.v21.util.DateUtils;
import com.sosgps.v21.util.TargetUtils;
import com.sosgps.v21.visit.service.VisitService;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.poi.dao.LayerAndPoiDao;
import com.sosgps.wzt.stat.action.VisitStatAction;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.dao.TUserDao;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.CoordConvertAPI;
import com.sosgps.wzt.util.DateUtility;

public class VisitServiceImpl implements VisitService {

    private static final Logger logger = Logger.getLogger(VisitStatAction.class);

    private VisitDao visitDao;

    private TargetDao targetDao;

    private TargetTemplateDao targetTemplateDao;

    private LayerAndPoiDao layerAndPoiDao;

    private TUserDao tUserDao;

    public LayerAndPoiDao getLayerAndPoiDao() {
        return layerAndPoiDao;
    }

    public void setLayerAndPoiDao(LayerAndPoiDao layerAndPoiDao) {
        this.layerAndPoiDao = layerAndPoiDao;
    }

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

    public VisitDao getVisitDao() {
        return visitDao;
    }

    public void setVisitDao(VisitDao visitDao) {
        this.visitDao = visitDao;
    }

    public List<Visit> queryVisitsByCondition(Map<String, Object> paramMap, String entCode) {
        return visitDao.queryVisitsByCondition(paramMap, entCode);
    }

    /**
     * ��ѯ�ͻ��ݷô�����
     */
    public String listVisitRanks(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {
        // ��request�л�ȡ����
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        if (st == null || et == null || deviceIds.equals("")) {
            return "{result:\"9\"}";
        }
        //deviceIds = CharTools.splitAndAdd(deviceIds);
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ��ѯ�ͻ��ݷô�����");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_VisitRank);
        LogFactory.newLogInstance("optLogger").info(optLog);

        //		int startint = Integer.parseInt(start);
        //		int pageSize = Integer.parseInt(limit);
        //		int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = deviceIds.split(",").length;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // paramMap.put("start", start);
        // paramMap.put("limit", limit);
        paramMap.put("startTime", startDateL);
        paramMap.put("endTime", endDateL);
        paramMap.put("deviceIds", deviceIds.split(","));
        List<Object[]> list = visitDao.queryVisitRank(paramMap, entCode);
        String deviceIds_ = "," + deviceIds + ",";
        TargetService targetService = (TargetService) SpringHelper.getBean("targetService");
        if (list != null && list.size() > 0) {
            for (Object[] object_ : list) {
                String deviceId = (String) object_[3];
                String deviceId_ = ",'" + deviceId + "',";
                int i = deviceIds_.indexOf(deviceId_);
                if (i != -1) {
                    String deviceIdHead = deviceIds_.substring(0, i);
                    String deviceIdEnd = deviceIds_.substring(deviceId_.length() + i);
                    deviceIds_ = deviceIdHead + "," + deviceIdEnd;
                }
                jsonSb.append("{");
                jsonSb.append("vehicleNumber:'" + CharTools.javaScriptEscape((String) object_[1])
                        + "',");// id
                jsonSb.append("groupName:'"
                        + TargetUtils.getGroupFullName((Long) object_[0], targetService) + "',");// id
                jsonSb.append("visitCounts:'" + (Long) object_[2] + "'");
                jsonSb.append("},");
            }
        }

        String[] deviceIdArr = deviceIds_.split(",");
        for (int i = 0; i < deviceIdArr.length; i++) {
            if (deviceIdArr[i].length() > 0) {
                String deviceId = deviceIdArr[i].substring(1, deviceIdArr[i].length() - 1);
                Terminal terminal = TargetUtils.getTerminal(deviceId, targetService);
                jsonSb.append("{");
                jsonSb.append("vehicleNumber:'" + terminal.getTermName() + "',");// id
                jsonSb.append("groupName:'"
                        + TargetUtils.getGroupFullName(terminal.getGroupId(), targetService) + "',");// id
                jsonSb.append("visitCounts:'0'");
                jsonSb.append("},");
            }
        }
        if (jsonSb.length() > 0) {
            jsonSb.deleteCharAt(jsonSb.length() - 1);
        }

        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";
    }

    /**
     * �����ͻ��ݷô�����
     */
    public String listVisitRanksExpExcel(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {
        // ��request�л�ȡ����
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        if (st == null || et == null || deviceIds.equals("")) {
            return "{result:\"9\"}";
        }
        //deviceIds = CharTools.splitAndAdd(deviceIds);
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
        optLog.setOptDesc(userInfo.getUserAccount() + " �����ͻ��ݷô�����");
        optLog.setFunFType(LogConstants.LOG_Exp);
        optLog.setFunCType(LogConstants.LOG_Exp_VisitRank);
        LogFactory.newLogInstance("optLogger").info(optLog);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("startTime", startDateL);
        paramMap.put("endTime", endDateL);
        paramMap.put("deviceIds", deviceIds.split(","));
        List<Object[]> list = visitDao.queryVisitRank(paramMap, entCode);
        String deviceIds_ = "," + deviceIds + ",";
        TargetService targetService = (TargetService) SpringHelper.getBean("targetService");
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("Ա������", 15);
        excelWorkBook.addHeader("����", 15);
        excelWorkBook.addHeader("�ͻ��ݷ���(��)", 15);
        int row = 0;

        if (list != null && list.size() > 0) {
            for (Object[] object_ : list) {
                String deviceId = (String) object_[3];
                int i = deviceIds_.indexOf(deviceId);
                if (i != -1) {
                    String deviceIdHead = deviceIds_.substring(0, i);
                    String deviceIdEnd = deviceIds_.substring(deviceId.length() + i);
                    deviceIds_ = deviceIdHead + "," + deviceIdEnd;
                }
                int col = 0;
                row += 1;
                excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape((String) object_[1]));
                excelWorkBook.addCell(col++, row,
                        TargetUtils.getGroupFullName((Long) object_[0], targetService));
                excelWorkBook.addCell(col++, row, (Long) object_[2] + "");
            }
        }

        String[] deviceIdArr = deviceIds_.split(",");
        for (int i = 0; i < deviceIdArr.length; i++) {
            if (deviceIdArr[i].length() > 0) {
                Terminal terminal = TargetUtils.getTerminal(deviceIdArr[i], targetService);
                int col = 0;
                row += 1;
                excelWorkBook.addCell(col++, row, terminal.getTermName());
                excelWorkBook.addCell(col++, row,
                        TargetUtils.getGroupFullName(terminal.getGroupId(), targetService));
                excelWorkBook.addCell(col++, row, "0");
            }
        }
        excelWorkBook.write();
        return "{result:\"1\"}";
    }

    /**
     * Ա�����ô���ʶ�����ͼ
     * 
     * @param userInfo
     * @return
     */
    public String getVisitsByTime(UserInfo userInfo) {
        StringBuffer json = new StringBuffer();
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        //��ǰԱ�����ô��������ֵ
        List<Object[]> visits = visitDao.getVisitsByTime(startTime, endTime, entCode);
        json.append("{chartDatas:[");
        if (visits != null && visits.size() >= 1) {
            StringBuffer sb = new StringBuffer();
            for (Iterator<Object[]> iterator = visits.iterator(); iterator.hasNext();) {
                Object[] object_ = (Object[]) iterator.next();
                BigDecimal visits_ = (BigDecimal) object_[0];
                String visitsS_ = "0";
                if (visits_ != null) {
                    Double visitsD_ = visits_.doubleValue();
                    visitsS_ = CharTools.killNullDouble2String(visitsD_, "0");
                }
                String createon_ = (String) object_[1];
                if (targetTemplateType_ == 2) {
                    String[] sArr = createon_.split("-");
                    createon_ = sArr[0] + "��" + sArr[1] + "��" + sArr[2] + "��";
                }
                sb.append("{value:" + visitsS_ + ", date:'" + createon_ + "'},");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            json.append(sb.toString());
        }
        json.append("],");
        String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'" + "ҵ��Ա����ͳ������ͼ("
                + DateUtils.dateTimeToStr(startTime, "yyyy��MM��") + ")'" : "chartTitle:'"
                + "ҵ��Ա����ͳ������ͼ(" + DateUtils.dateTimeToStr(startTime, "yyyy��MM��dd��") + "~"
                + DateUtils.dateTimeToStr(endTime, "yyyy��MM��dd��") + ")'";
        json.append(chartTitle);
        json.append(", unaudited:''");
        json.append(", startTime:" + startTime);
        json.append(", endTime:" + endTime);
        json.append("}");
        logger.info("[getVisitsByTime] json = " + json.toString());
        return json.toString();
    }

    /**
     * ��ʷԱ�����ô��������ͼ
     */
    public String getVisitHisByTime(UserInfo userInfo) {
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetTypeHis(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        //��ʱ���ѯǩ��������ֵ
        List<Object[]> visits = visitDao.getVisitsByTime(startTime, endTime, entCode);
        String chartDatasJson = TargetUtils.getHisChartByTargetType(targetTemplateType_, visits,
                "chartDatas");
        String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime,
                targetTemplateType_, "ҵ��Ա����ͳ������ͼ", "chartTitle");
        // ȡ��Ŀ��ģ��ʱ�䷶Χ��ǩ������ֵ
        String json = "{" + chartDatasJson + "," + chartTitleJson + ", unaudited:'', startTime:"
                + startTime + ", endTime:" + endTime + "}";
        logger.info("[getVisitHisByTime] json = " + json);
        return json;
    }

    /**
     * �ͻ��ݷø���������ͼ
     * 
     * @param userInfo
     * @return
     */
    public String getCusVisitsByTime(UserInfo userInfo) {
        StringBuffer json = new StringBuffer();
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        //��ǰǩ��������ֵ
        List<Object[]> visits = visitDao.getCusVisitsByTime(startTime, endTime, entCode);
        json.append("{chartDatas:[");
        if (visits != null && visits.size() >= 1) {
            StringBuffer sb = new StringBuffer();
            for (Iterator<Object[]> iterator = visits.iterator(); iterator.hasNext();) {
                Object[] object_ = (Object[]) iterator.next();
                BigDecimal visits_ = (BigDecimal) object_[0];
                String visitsS_ = "0";
                if (visits_ != null) {
                    Double visitsD_ = visits_.doubleValue();
                    visitsS_ = CharTools.killNullDouble2String(visitsD_, "0");
                }
                String createon_ = (String) object_[1];
                if (targetTemplateType_ == 2) {
                    String[] sArr = createon_.split("-");
                    createon_ = sArr[0] + "��" + sArr[1] + "��" + sArr[2] + "��";
                }
                sb.append("{value:" + visitsS_ + ", date:'" + createon_ + "'},");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            json.append(sb.toString());
        }
        json.append("],");
        String chartTitle = targetTemplateType_ == 2 ? "chartTitle:'" + "�ͻ����ݷ�ͳ������ͼ("
                + DateUtils.dateTimeToStr(startTime, "yyyy��MM��") + ")'" : "chartTitle:'"
                + "�ͻ����ݷ�ͳ������ͼ(" + DateUtils.dateTimeToStr(startTime, "yyyy��MM��dd��") + "~"
                + DateUtils.dateTimeToStr(endTime, "yyyy��MM��dd��") + ")'";
        json.append(chartTitle);
        json.append(", unaudited:''");
        json.append(", startTime:" + startTime);
        json.append(", endTime:" + endTime);
        json.append("}");
        logger.info("[getCusVisitsByTime] json = " + json.toString());
        return json.toString();
    }

    /**
     * ��ʷ�ͻ��ݷø���������ͼ
     */
    public String getCusVisitHisByTime(UserInfo userInfo) {
        String entCode = userInfo.getEmpCode();
        String targetTemplateType = userInfo.getTargetTemplateType();
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        Long startTime = DateUtils.getStartTimeByTargetTypeHis(targetTemplateType_, d);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d);
        //��ʱ���ѯǩ��������ֵ
        List<Object[]> visits = visitDao.getCusVisitsByTime(startTime, endTime, entCode);
        String chartDatasJson = TargetUtils.getHisChartByTargetType(targetTemplateType_, visits,
                "chartDatas");
        String chartTitleJson = TargetUtils.getHisChartTitle(startTime, endTime,
                targetTemplateType_, "�ͻ��ݷ�����ͼ", "chartTitle");
        String json = "{" + chartDatasJson + "," + chartTitleJson + ", unaudited:'', startTime:"
                + startTime + ", endTime:" + endTime + "}";
        logger.info("[getCusVisitHisByTime] json = " + json);
        return json;
    }

    /**
     * �ͻ��ݷ���ϸ����
     */
    public String listVisitDetails(ActionMapping mapping, HttpServletRequest request,
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
        //String approvedStr = request.getParameter("duration");// ״̬:-1:ȫ��,0:δ���,1:���
        //int approved = CharTools.str2Integer(approvedStr, -1);// ״̬
        if (st == null || et == null || deviceIds.equals("")) {
            response.getWriter().write("������ȫ");// δ��¼
            return "";
        }
        deviceIds = CharTools.splitAndAdd(deviceIds);
        Date startDate = DateUtility.strToDateTime(st);
        Date endDate = DateUtility.strToDateTime(et);
        Long startDateL = startDate.getTime();
        Long endDateL = endDate.getTime();
        if (start == null || limit == null || userInfo == null) {
            response.getWriter().write("{result:\"9\"}");// δ��¼
            return "";
        }
        String entCode = userInfo.getEmpCode();
        Long userId = userInfo.getUserId();
        int startint = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = 0;
        if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
            Page<Visit> list = visitDao.listVisitDetails(entCode, pageNo, pageSize, startDateL,
                    endDateL, poiName, deviceIds);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                total = list.getTotalCount();
                DecimalFormat df = new DecimalFormat("#");
                for (Visit visit : list.getResult()) {
                    String signInDesc = visit.getSignInDesc();
                    String signOutDesc = visit.getSignOutDesc();

                    Double signInLongitude = (Double) visit.getSignInLng() == null ? null
                            : (Double) visit.getSignInLng();
                    Double signInLatitude = (Double) visit.getSignInLat() == null ? null
                            : (Double) visit.getSignInLat();
                    Double signOutLongitude = (Double) visit.getSignOutLng() == null ? null
                            : (Double) visit.getSignOutLng();
                    Double signOutLatitude = (Double) visit.getSignOutLat() == null ? null
                            : (Double) visit.getSignOutLat();

                    if (signInDescIsNUll(signInDesc, signInLongitude, signInLatitude)) {
                        signInDesc = CoordConvertAPI.getLocDescByLngLat(signInLongitude,
                                signInLatitude, logger);
                    }
                    if (signInDescIsNUll(signOutDesc, signOutLongitude, signOutLatitude)) {
                        signOutDesc = CoordConvertAPI.getLocDescByLngLat(signOutLongitude,
                                signOutLatitude, logger);
                    }
                    String signInDistance = (Double) visit.getSignInDistance() == null ? null : df
                            .format((Double) visit.getSignInDistance());
                    String signOutDistance = (Double) visit.getSignOutDistance() == null ? null
                            : df.format((Double) visit.getSignOutDistance());

                    jsonSb.append("{");
                    jsonSb.append("id:'" + visit.getId() + "NO.',");// id
                    //jsonSb.append("groupName:'" + CharTools.javaScriptEscape(visit.getGroupName()) + "',");// ����
                    jsonSb.append("vehicleNumber:'"
                            + CharTools.javaScriptEscape(visit.getTermName()) + "',");// Ա������
                    jsonSb.append("poiName:'" + CharTools.javaScriptEscape(visit.getPoiName())
                            + "',");// �ͻ�����
                    jsonSb.append("signInTime:'"
                            + DateUtils.dateTimeToStr(visit.getSignInTime(), "yyyy-MM-dd HH:mm:ss")
                            + "',");
                    jsonSb.append("signInRendertime:'"
                            + DateUtils.dateTimeToStr(visit.getSignInRenderTime(),
                                    "yyyy-MM-dd HH:mm:ss") + "',");
                    jsonSb.append("signInLocType:'"
                            + CharTools.killNullLong2String(visit.getLocationTypeIn(), "") + "',");
                    jsonSb.append("signInLng:'"
                            + CharTools.killNullDouble2String(signInLongitude, "") + "',");
                    jsonSb.append("signInLat:'"
                            + CharTools.killNullDouble2String(signInLatitude, "") + "',");
                    jsonSb.append("signInDistance:'" + CharTools.killNullString(signInDistance, "")
                            + "',");
                    jsonSb.append("signOutTime:'"
                            + DateUtils.dateTimeToStr(visit.getSignOutTime(), "yyyy-MM-dd HH:mm:ss")
                            + "',");
                    jsonSb.append("signOutRendertime:'"
                            + DateUtils.dateTimeToStr(visit.getSignOutRenderTime(),
                                    "yyyy-MM-dd HH:mm:ss") + "',");
                    jsonSb.append("signOutLocType:'"
                            + CharTools.killNullLong2String(visit.getLocationTypeOut(), "") + "',");
                    jsonSb.append("signOutLng:'"
                            + CharTools.killNullDouble2String(signOutLongitude, "") + "',");
                    jsonSb.append("signOutLat:'"
                            + CharTools.killNullDouble2String(signOutLatitude, "") + "',");
                    jsonSb.append("signOutDistance:'"
                            + CharTools.killNullString(signOutDistance, "") + "',");
                    jsonSb.append("signInDesc:'" + CharTools.javaScriptEscape(signInDesc) + "',");
                    jsonSb.append("signOutDesc:'" + CharTools.javaScriptEscape(signOutDesc) + "'");
                    jsonSb.append("},");

                }
                if (jsonSb.length() > 0) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            }
        }
        //searchLog
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " ��ѯ�ͻ��ݷ���ϸ��¼");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_VisitReport);
        LogFactory.newLogInstance("optLogger").info(optLog);

        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";
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
        optLog.setOptDesc(userInfo.getUserAccount() + " ҵ��Ա���ô�����Ǳ���");
        optLog.setFunFType(LogConstants.LOG_Gauge);
        optLog.setFunCType(LogConstants.LOG_Gauge_Visit);
        LogFactory.newLogInstance("optLogger").info(optLog);

        // ȡ��ǩ������ά��ֵ,�Ǳ��̺��������ֵ
        int types = 4;// 1-ǩ��������,2-�ؿ������,3-���ö�ʹ����,4-Ա�����ô����,5-�ͻ��ݷø�����
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
        //Ŀ��ģ������ 0:��;1:Ѯ;2:��
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        int targetOn = DateUtils.getTargetOnInThisYear(d, targetTemplateType_);
        int year = DateUtils.getCurrentYear(d);
        // ����Ŀ��ά������(��/Ѯ/��)ȡ�õ�ǰ(��/Ѯ/��)ʱ�䷶Χ�ڵ�����Ŀ��ά������
        List<Object> targetTemplate_ = targetTemplateDao.getTargetTemplate(entCode,
                targetTemplateType_, year, targetOn, "visitAmount");
        String amount = "0";
        if (targetTemplate_ != null && targetTemplate_.size() >= 1) {
            Long amountD = (Long) targetTemplate_.get(0);
            amount = CharTools.killNullLong2String(amountD, "0");
        }
        json.append("targetTemplates:" + df.format(Double.parseDouble(amount)) + ",");

        Date d1 = new Date();
        // ȡ��Ŀ��ģ��ʱ�䷶Χ��ǩ������ֵ
        Long startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d1);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d1);
        List<Long> counts = visitDao.getVisitCount(startTime, endTime, entCode);
        String counts_ = "0";
        if (counts != null && counts.size() >= 1) {
            Long countsL_ = counts.get(0);
            counts_ = CharTools.killNullLong2String(countsL_, "0");
        }

        json.append("totals:" + df.format(Double.parseDouble(counts_)) + ",");
        String needle = "0";
        if (!(amount.equals("0") || amount.equals("0.0"))) {
            needle = df.format(Double.parseDouble(counts_) / Double.parseDouble(amount) * 100);
        }
        json.append("needle:" + needle);
        logger.info("[getGaugeByTargetType] json = " + json.toString());
        return "{" + json.toString() + "}";

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
    public String getGaugeByTargetTypeCus(ActionMapping mapping, HttpServletRequest request,
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
        optLog.setOptDesc(userInfo.getUserAccount() + " �ͻ��ݷø����Ǳ���");
        optLog.setFunFType(LogConstants.LOG_Gauge);
        optLog.setFunCType(LogConstants.LOG_Gauge_CusVisit);
        LogFactory.newLogInstance("optLogger").info(optLog);

        // ȡ��ǩ������ά��ֵ,�Ǳ��̺��������ֵ
        int types = 5;// 1-ǩ��������,2-�ؿ������,3-���ö�ʹ����,4-Ա�����ô����,5-�ͻ��ݷø�����
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
        //Ŀ��ģ������ 0:��;1:Ѯ;2:��
        int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer
                .valueOf(targetTemplateType);
        Date d = new Date();
        int targetOn = DateUtils.getTargetOnInThisYear(d, targetTemplateType_);
        int year = DateUtils.getCurrentYear(d);
        // ����Ŀ��ά������(��/Ѯ/��)ȡ�õ�ǰ(��/Ѯ/��)ʱ�䷶Χ�ڵ�����Ŀ��ά������
        List<Object> targetTemplate_ = targetTemplateDao.getTargetTemplate(entCode,
                targetTemplateType_, year, targetOn, "cusVisitAmount");

        //�ͻ�����Ŀ
        //Long poiCountL_ = layerAndPoiDao.queryPoiCount(entCode);
        //String poiCount_ = CharTools.killNullLong2String(poiCountL_, "0");
        //json.append("targetTemplates:" + poiCount_ + ",");

        String poiCount_ = "0";
        if (targetTemplate_ != null && targetTemplate_.size() >= 1) {
            Long amountD = (Long) targetTemplate_.get(0);
            poiCount_ = CharTools.killNullLong2String(amountD, "0");
        }
        json.append("targetTemplates:" + poiCount_ + ",");

        Date d1 = new Date();
        // ȡ��Ŀ��ģ��ʱ�䷶Χ�ͻ����ݷõ�����
        Long startTime = DateUtils.getStartTimeByTargetType(targetTemplateType_, d1);
        Long endTime = DateUtils.getEndTimeByTargetType(targetTemplateType_, d1);
        List<Long> counts = visitDao.getCusVisitCount(startTime, endTime, entCode);
        String counts_ = "0";
        if (counts != null && counts.size() >= 1) {
            //Long countsL_ = counts.get(0);
            //counts_ = CharTools.killNullLong2String(countsL_, "0");
            counts_ = counts.size() + "";
        }

        json.append("totals:" + df.format(Double.parseDouble(counts_)) + ",");
        String needle = "0";
        if (!(poiCount_.equals("0") || poiCount_.equals("0.0"))) {
            needle = df.format(Double.parseDouble(counts_) / Double.parseDouble(poiCount_) * 100);
        }
        json.append("needle:" + needle);
        logger.info("[getGaugeByTargetTypeCus] json = " + json.toString());
        return "{" + json.toString() + "}";

    }

    /**
     * �ͻ����ݷ�ͳ��
     * 
     * @param mapping
     * @param request
     * @param response
     * @param userInfo
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String listCustomVisitCountTj(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {
        String entCode = userInfo.getEmpCode();
        Long userId = userInfo.getUserId();
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
        String deviceIdsStr = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
        Long duration = CharTools.str2Long(durationStr, 15L);// Ĭ��15����
        String deviceIds = CharTools.javaScriptEscape(deviceIdsStr);
        String searchValue = request.getParameter("searchValue");
        searchValue = CharTools.killNullString(searchValue);
        searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
        searchValue = CharTools.killNullString(searchValue);

        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " �ͻ����ݷ�ͳ��v2.1");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_CusVisit_v2_1);
        LogFactory.newLogInstance("optLogger").info(optLog);

        deviceIds = CharTools.splitAndAdd(deviceIds);
        Date startDate = DateUtility.strToDateTime(st);
        Date endDate = DateUtility.strToDateTime(et);
        int startint = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        // add by renxianliang 2013-06-25 �Ƿ񵼳�excel
        String expExcel = request.getParameter("expExcel");// trueΪ����
        expExcel = CharTools.javaScriptEscape(expExcel);
        if (expExcel.equalsIgnoreCase("true")) {
            ArrayList arr = new ArrayList();
            ExcelWorkBook excelWorkBook = expExcelCustomVisitCount(response, userInfo, entCode,
                    userId, duration, deviceIds, searchValue, startDate, endDate, startint,
                    pageSize, arr);
            expExcelCustomVisitCountDetail(entCode, duration, deviceIds, startDate, endDate,
                    startint, pageSize, arr, excelWorkBook);
            excelWorkBook.write();
            return null;
        }
        if (start == null || limit == null || userInfo == null) {
            return "{result:\"9\"}";
        }

        //int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = 0;
        if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
            Page<Object[]> list = visitDao.listCustomVisitCountTj(entCode, startint, pageSize,
                    startDate.getTime(), endDate.getTime(), searchValue, deviceIds, duration);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                total = list.getTotalCount();
                for (Object[] objects : list.getResult()) {
                    Long poiId = (Long.parseLong(objects[0].toString()));
                    String poiName = (String) objects[1];
                    Long visitCount = objects[2] == null ? (long) 0 : Long.parseLong(objects[2]
                            .toString());
                    jsonSb.append("{");
                    jsonSb.append("id:'" + poiId + "',");// id
                    jsonSb.append("visitName:'" + CharTools.javaScriptEscape(poiName) + "',");// �ͻ�����
                    jsonSb.append("visitCount:'" + CharTools.killNullLong2String(visitCount, "0")
                            + "'");// �ݷô���
                    jsonSb.append("},");
                }
                if (jsonSb.length() > 0) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            }
        }
        logger.info("[listCustomVisitCountTj] json = " + "{total:" + total + ",data:["
                + jsonSb.toString() + "]}");
        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";

    }

    @SuppressWarnings("unchecked")
    private ExcelWorkBook expExcelCustomVisitCount(HttpServletResponse response, UserInfo userInfo,
            String entCode, Long userId, Long duration, String deviceIds, String searchValue,
            Date startDate, Date endDate, int startint, int pageSize, ArrayList arr)
            throws Exception, IOException {
        Page<Object[]> list = visitDao.listCustomVisitCountTj(entCode, startint, pageSize,
                startDate.getTime(), endDate.getTime(), searchValue, deviceIds, duration);
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("��ע����", 15);
        excelWorkBook.addHeader("�ݷô���", 15);
        int row = 0;
        for (Object[] objects : list.getResult()) {
            arr.add(objects[0].toString());
            String poiName = (String) objects[1];
            Long visitCount = objects[2] == null ? (long) 0 : Long.parseLong(objects[2].toString());
            int col = 0;
            row += 1;
            excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(poiName));
            excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(visitCount, "0"));
        }
        return excelWorkBook;
    }

    private void expExcelCustomVisitCountDetail(String entCode, Long duration, String deviceIds,
            Date startDate, Date endDate, int startint, int pageSize, ArrayList arr,
            ExcelWorkBook excelWorkBook) {
            int row = 0;
            // ����sheet
             excelWorkBook.addWorkSheet("��ϸ��Ϣ");
             // ����header
             excelWorkBook.addHeader("���ÿͻ�", 15);
             excelWorkBook.addHeader("�ݷ�ҵ��Ա", 15);
             excelWorkBook.addHeader("����ʱ��", 20);
             excelWorkBook.addHeader("�뿪ʱ��", 20);
             excelWorkBook.addHeader("ͣ��ʱ��(����)", 20);
             excelWorkBook.addHeader("ǩ��λ������", 50);
             excelWorkBook.addHeader("ǩ��λ������", 50);
        for (int i = 0; i < arr.size(); i++) {
            Long poiId = CharTools.str2Long(arr.get(i).toString(), -1L);
            Page<Object[]> list = visitDao.listCustomVisitCountTjByCustom(entCode, startint,
                    pageSize, startDate.getTime(), endDate.getTime(), poiId, deviceIds, duration);
            if (list != null && list.getResult().size() > 0) {
                DecimalFormat df = new DecimalFormat("#.##");
                for (Object[] objects : list.getResult()) {
                    String poiName = (String) objects[0];
                    String termName = (String) objects[1];
                    String signInTime = (BigDecimal) objects[2] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[2]).longValue()));
                    String signOutTime = (BigDecimal) objects[3] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[3]).longValue()));
                    String stayTime = (BigDecimal) objects[4] == null ? "0" : df
                            .format(((BigDecimal) objects[4]).doubleValue());
                    String signInDesc = (String) objects[5];
                    String signOutDesc = (String) objects[6];
                    Double signInLongitude = (BigDecimal) objects[8] == null ? null
                            : ((BigDecimal) objects[8]).doubleValue();
                    Double signInLatitude = (BigDecimal) objects[9] == null ? null
                            : ((BigDecimal) objects[9]).doubleValue();
                    Double signOutLongitude = (BigDecimal) objects[10] == null ? null
                            : ((BigDecimal) objects[10]).doubleValue();
                    Double signOutLatitude = (BigDecimal) objects[11] == null ? null
                            : ((BigDecimal) objects[11]).doubleValue();

                    if (signInDescIsNUll(signInDesc, signInLongitude, signInLatitude)) {
                        signInDesc = CoordConvertAPI.getLocDescByLngLat(signInLongitude,
                                signInLatitude, logger);
                    }
                    if (signInDescIsNUll(signOutDesc, signOutLongitude, signOutLatitude)) {
                        signOutDesc = CoordConvertAPI.getLocDescByLngLat(signOutLongitude,
                                signOutLatitude, logger);
                    }
                    int col = 0;
                    row += 1;
                    excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(poiName));
                    excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(termName));
                    excelWorkBook.addCell(col++, row,
                            CharTools.javaScriptEscape(String.valueOf(signInTime)));
                    excelWorkBook.addCell(col++, row,
                            CharTools.javaScriptEscape(String.valueOf(signOutTime)));
                    excelWorkBook.addCell(col++, row, String.valueOf(stayTime));
                    excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(signInDesc));
                    excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(signOutDesc));
                }
            }
        }
    }

    private boolean signInDescIsNUll(String signInDesc, Double signInLongitude,
            Double signInLatitude) {
        return (signInDesc == null || signInDesc.length() <= 0) && signInLongitude != null
                && signInLatitude != null;
    }

    /**
     * �ͻ����ݷ�ͳ��,�����������תҳ��,��ѯ��ϸ�ı��ݷÿͻ���Ϣͬ2.0
     */
    public String listCustomVisitCountTjByCustom(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {
        String entCode = userInfo.getEmpCode();
        Long userId = userInfo.getUserId();
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
        String deviceIds = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
        Long duration = CharTools.str2Long(durationStr, 15L);// Ĭ��15����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        String poiIdStr = request.getParameter("poiId");// �ͻ�id
        Long poiId = CharTools.str2Long(poiIdStr, -1L);
        if (st == null || et == null || deviceIds.equals("") || userInfo == null) {
            return "{result:\"9\"}";
        }
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " �ͻ����ݷ�ͳ����ϸ��Ϣv2.1");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_CusVisit_Detail_v2_1);
        LogFactory.newLogInstance("optLogger").info(optLog);

        deviceIds = CharTools.splitAndAdd(deviceIds);
        Date startDate = DateUtility.strToDateTime(st);
        Date endDate = DateUtility.strToDateTime(et);
        if (start == null || limit == null || userInfo == null) {
            return "{result:\"9\"}";
        }
        int startint = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        //int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = 0;
        if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
            Page<Object[]> list = visitDao.listCustomVisitCountTjByCustom(entCode, startint,
                    pageSize, startDate.getTime(), endDate.getTime(), poiId, deviceIds, duration);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                total = list.getTotalCount();
                DecimalFormat df = new DecimalFormat("#.##");
                for (Object[] objects : list.getResult()) {
                    String poiName = (String) objects[0];
                    String termName = (String) objects[1];
                    String signInTime = (BigDecimal) objects[2] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[2]).longValue()));
                    String signOutTime = (BigDecimal) objects[3] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[3]).longValue()));
                    String stayTime = (BigDecimal) objects[4] == null ? "0" : df
                            .format(((BigDecimal) objects[4]).doubleValue());
                    String signInDesc = (String) objects[5];
                    String signOutDesc = (String) objects[6];
                    BigDecimal id = (BigDecimal) objects[7];

                    Double signInLongitude = (BigDecimal) objects[8] == null ? null
                            : ((BigDecimal) objects[8]).doubleValue();
                    Double signInLatitude = (BigDecimal) objects[9] == null ? null
                            : ((BigDecimal) objects[9]).doubleValue();
                    Double signOutLongitude = (BigDecimal) objects[10] == null ? null
                            : ((BigDecimal) objects[10]).doubleValue();
                    Double signOutLatitude = (BigDecimal) objects[11] == null ? null
                            : ((BigDecimal) objects[11]).doubleValue();
                    String createOn = (BigDecimal) objects[12] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[12]).longValue()));

                    if (signInDescIsNUll(signInDesc, signInLongitude, signInLatitude)) {
                        signInDesc = CoordConvertAPI.getLocDescByLngLat(signInLongitude,
                                signInLatitude, logger);
                    }
                    if (signInDescIsNUll(signOutDesc, signOutLongitude, signOutLatitude)) {
                        signOutDesc = CoordConvertAPI.getLocDescByLngLat(signOutLongitude,
                                signOutLatitude, logger);
                    }

                    jsonSb.append("{");
                    jsonSb.append("id:'" + id + "',");// id
                    jsonSb.append("vehicleNumber:'" + CharTools.javaScriptEscape(termName) + "',");// ����
                    jsonSb.append("arriveTime:'" + signInTime + "',");// ����ʱ��
                    jsonSb.append("leaveTime:'" + signOutTime + "',");// �뿪ʱ��
                    jsonSb.append("stayTime:'" + stayTime + "',");// ͣ��ʱ��,��λ����
                    jsonSb.append("signInDesc:'" + CharTools.javaScriptEscape(signInDesc) + "',");// λ������
                    jsonSb.append("signOutDesc:'" + CharTools.javaScriptEscape(signOutDesc) + "',");// λ������
                    jsonSb.append("visitName:'" + CharTools.javaScriptEscape(poiName) + "',");// ���ÿͻ�����
                    jsonSb.append("tjDate:'" + CharTools.javaScriptEscape(createOn) + "'");// ͳ��ʱ��
                    jsonSb.append("},");
                }
                if (jsonSb.length() > 0) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            }
        }
        logger.info("[listCustomVisitCountTjByCustom] json = " + "{total:" + total + ",data:["
                + jsonSb.toString() + "]}");
        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";

    }

    // ҵ��Ա����ͳ��(sql)
    public String listVisitCountTjSql(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {
        String entCode = userInfo.getEmpCode();
        Long userId = userInfo.getUserId();
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
        String deviceIdsStr = request.getParameter("deviceIds");// ��ѯ�ն�deviceId�����","����
        String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
        Long duration = CharTools.str2Long(durationStr, 15L);// Ĭ��15����
        deviceIdsStr = CharTools.javaScriptEscape(deviceIdsStr);
        String searchValue = request.getParameter("searchValue");
        searchValue = CharTools.killNullString(searchValue);
        searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
        searchValue = CharTools.killNullString(searchValue);

        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " ҵ��Ա����ͳ��v2.1");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_Visit_v2_1);
        LogFactory.newLogInstance("optLogger").info(optLog);

        String deviceIds = CharTools.splitAndAdd(deviceIdsStr);
        Date startDate = DateUtility.strToDateTime(st);
        Date endDate = DateUtility.strToDateTime(et);
        int startint = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        // add by renxianliang 2013-06-25 �Ƿ񵼳�excel
        String expExcel = request.getParameter("expExcel");// trueΪ����
        expExcel = CharTools.javaScriptEscape(expExcel);
        if (expExcel.equalsIgnoreCase("true")) {
            ExcelWorkBook excelWorkBook = expExcelVisitCount(response, userInfo, entCode, userId,
                    duration, searchValue, deviceIds, startDate, endDate, startint, pageSize);
            expExcelVisitCountCustom(entCode, deviceIdsStr, duration, startDate, endDate, startint,
                    pageSize, excelWorkBook);
            excelWorkBook.write();
            return null;
        }
        if (start == null || limit == null || userInfo == null) {
            return "{result:\"9\"}";
        }
        // int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = deviceIds.split(",").length;
        if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
            String deviceIds_ = "," + deviceIds + ",";
            TargetService targetService = (TargetService) SpringHelper.getBean("targetService");
            Page<Object[]> list = visitDao.listVisitCountTjSql(entCode, startint, pageSize,
                    startDate.getTime(), endDate.getTime(), searchValue, deviceIds, duration);
            if (list != null && list.getResult().size() > 0) {
                //total = list.getTotalCount();
                for (Object[] objects : list.getResult()) {
                    String deviceId = (String) objects[0];
                    String deviceId_ = ",'" + deviceId + "',";
                    int i = deviceIds_.indexOf(deviceId_);
                    if (i != -1) {
                        String deviceIdHead = deviceIds_.substring(0, i);
                        String deviceIdEnd = deviceIds_.substring(deviceId_.length() + i);
                        deviceIds_ = deviceIdHead + "," + deviceIdEnd;
                    }
                    String vehicleNumber = (String) objects[1];
                    Long visitCount = ((BigDecimal) objects[2]).longValue();
                    Long visitCusCount = ((BigDecimal) objects[3]).longValue();
                    // Long visitPlaceCount = ((BigDecimal)
                    // objects[4]).longValue();
                    jsonSb.append("{");
                    jsonSb.append("deviceId:'" + CharTools.javaScriptEscape(deviceId) + "',");// id
                    jsonSb.append("vehicleNumber:'" + CharTools.javaScriptEscape(vehicleNumber)
                            + "',");// ����
                    jsonSb.append("visitCount:'" + CharTools.killNullLong2String(visitCount, "0")
                            + "',");// �ݷô���
                    jsonSb.append("visitCusCount:'"
                            + CharTools.killNullLong2String(visitCusCount, "0") + "',");// �ݷÿͻ���
                    jsonSb.append("visitPlaceCount:'"
                            + CharTools.killNullLong2String(visitCusCount, "0") + "'");// �ݷõص���
                    jsonSb.append("},");
                }

                String[] deviceIdArr = deviceIds_.split(",");
                for (int i = 0; i < deviceIdArr.length; i++) {
                    if (deviceIdArr[i].length() > 0) {
                        String deviceId = deviceIdArr[i].substring(1, deviceIdArr[i].length() - 1);
                        Terminal terminal = TargetUtils.getTerminal(deviceId, targetService);
                        jsonSb.append("{");
                        jsonSb.append("deviceId:'" + deviceId + "',");
                        jsonSb.append("vehicleNumber:'" + terminal.getTermName() + "',");
                        jsonSb.append("visitCount:'0',");
                        jsonSb.append("visitCusCount:'0',");
                        jsonSb.append("visitPlaceCount:'0'");
                        jsonSb.append("},");
                    }
                }
                if (jsonSb.length() > 0) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            }
        }
        logger.info("[listVisitCountTjSql] json = " + "{total:" + total + ",data:["
                + jsonSb.toString() + "]}");
        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";
    }

    //����ҵ��Ա���û���
    private ExcelWorkBook expExcelVisitCount(HttpServletResponse response, UserInfo userInfo,
            String entCode, Long userId, Long duration, String searchValue, String deviceIds,
            Date startDate, Date endDate, int startint, int pageSize) throws Exception, IOException {
        Page<Object[]> list = visitDao.listVisitCountTjSql(entCode, startint, pageSize,
                startDate.getTime(), endDate.getTime(), searchValue, deviceIds, duration);
        ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
        // header
        excelWorkBook.addHeader("ҵ��Ա����", 15);
        excelWorkBook.addHeader("���ô���", 15);
        excelWorkBook.addHeader("���ÿͻ���", 15);
        int row = 0;
        for (Object[] objects : list.getResult()) {
            String vehicleNumber = (String) objects[1];
            Long visitCount = ((BigDecimal) objects[2]).longValue();
            Long visitCusCount = ((BigDecimal) objects[3]).longValue();
            int col = 0;
            row += 1;
            excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(vehicleNumber));
            excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(visitCount, "0"));
            excelWorkBook.addCell(col++, row, CharTools.killNullLong2String(visitCusCount, "0"));
        }
        return excelWorkBook;
    }

    //����ҵ��Ա��������
    private void expExcelVisitCountCustom(String entCode, String deviceIdsStr, Long duration,
            Date startDate, Date endDate, int startint, int pageSize, ExcelWorkBook excelWorkBook) {
        int row;
        String[] deviceIdss = CharTools.split(deviceIdsStr, ",");
        for (String deviceId : deviceIdss) {//listCustomVisitCountTjByCustom
            Page<Object[]> list = null;
            list = visitDao.listVisitCountTjByCustom(entCode, startint, pageSize,
                    startDate.getTime(), endDate.getTime(), deviceId, duration);
            if (list != null && list.getResult().size() > 0) {
                row = 0;
                DecimalFormat df = new DecimalFormat("#.##");
                for (Object[] objects : list.getResult()) {
                    String poiName = (String) objects[0];
                    String termName = (String) objects[1];
                    String signInTime = (BigDecimal) objects[2] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[2]).longValue()));
                    String signOutTime = (BigDecimal) objects[3] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[3]).longValue()));
                    String stayTime = (BigDecimal) objects[4] == null ? "0" : df
                            .format(((BigDecimal) objects[4]).doubleValue());
                    String signInDesc = (String) objects[5];
                    String signOutDesc = (String) objects[6];
                    Double signInLongitude = (BigDecimal) objects[8] == null ? null
                            : ((BigDecimal) objects[8]).doubleValue();
                    Double signInLatitude = (BigDecimal) objects[9] == null ? null
                            : ((BigDecimal) objects[9]).doubleValue();
                    Double signOutLongitude = (BigDecimal) objects[10] == null ? null
                            : ((BigDecimal) objects[10]).doubleValue();
                    Double signOutLatitude = (BigDecimal) objects[11] == null ? null
                            : ((BigDecimal) objects[11]).doubleValue();

                    if (signInDescIsNUll(signInDesc, signInLongitude, signInLatitude)) {
                        signInDesc = CoordConvertAPI.getLocDescByLngLat(signInLongitude,
                                signInLatitude, logger);
                    }
                    if (signInDescIsNUll(signOutDesc, signOutLongitude, signOutLatitude)) {
                        signOutDesc = CoordConvertAPI.getLocDescByLngLat(signOutLongitude,
                                signOutLatitude, logger);
                    }
                    int col = 0;
                    if (row == 0) {
                        // ����sheet
                        excelWorkBook.addWorkSheet(CharTools.javaScriptEscape(termName));
                        // ����header
                        excelWorkBook.addHeader("���ÿͻ�", 15);
                        excelWorkBook.addHeader("����ʱ��", 20);
                        excelWorkBook.addHeader("�뿪ʱ��", 20);
                        excelWorkBook.addHeader("ͣ��ʱ��(����)", 20);
                        excelWorkBook.addHeader("ǩ��λ������", 50);
                        excelWorkBook.addHeader("ǩ��λ������", 50);
                    }
                    row += 1;
                    excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(poiName));
                    excelWorkBook.addCell(col++, row,
                            CharTools.javaScriptEscape(String.valueOf(signInTime)));
                    excelWorkBook.addCell(col++, row,
                            CharTools.javaScriptEscape(String.valueOf(signOutTime)));
                    excelWorkBook.addCell(col++, row, String.valueOf(stayTime));
                    excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(signInDesc));
                    excelWorkBook.addCell(col++, row, CharTools.javaScriptEscape(signOutDesc));
                }
            }
        }
    }

    public String listVisitCountTjByCustom(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, UserInfo userInfo) throws Exception {
        String entCode = userInfo.getEmpCode();
        Long userId = userInfo.getUserId();
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
        String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
        String deviceIds = request.getParameter("deviceId");// ��ѯ�ն�deviceId�����","����
        String durationStr = request.getParameter("duration");// ���˰ݷ�ʱ��С��X����
        Long duration = CharTools.str2Long(durationStr, 15L);// Ĭ��15����
        deviceIds = CharTools.javaScriptEscape(deviceIds);
        String poiIdStr = request.getParameter("poiId");// �ͻ�id
        Long poiId = CharTools.str2Long(poiIdStr, -1L);
        if (st == null || et == null || deviceIds.equals("") || userInfo == null) {
            return "{result:\"9\"}";
        }
        TOptLog optLog = new TOptLog();
        optLog.setEmpCode(entCode);
        optLog.setUserName(userInfo.getUserAccount());
        optLog.setAccessIp(userInfo.getIp());
        optLog.setUserId(userId);
        optLog.setOptTime(new Date());
        optLog.setResult(1L);
        optLog.setOptDesc(userInfo.getUserAccount() + " ҵ��Ա������ϸ��Ϣv2.1");
        optLog.setFunFType(LogConstants.LOG_STAT);
        optLog.setFunCType(LogConstants.LOG_STAT_Visit_Detail_v2_1);
        LogFactory.newLogInstance("optLogger").info(optLog);

        //deviceIds = CharTools.splitAndAdd(deviceIds);
        Date startDate = DateUtility.strToDateTime(st);
        Date endDate = DateUtility.strToDateTime(et);
        if (start == null || limit == null || userInfo == null) {
            return "{result:\"9\"}";
        }
        int startint = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        //int pageNo = startint / pageSize + 1;
        StringBuffer jsonSb = new StringBuffer();
        int total = 0;
        if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
            Page<Object[]> list = visitDao.listVisitCountTjByCustom(entCode, startint, pageSize,
                    startDate.getTime(), endDate.getTime(), deviceIds, duration);
            if (list != null && list.getResult() != null && list.getResult().size() > 0) {
                total = list.getTotalCount();
                DecimalFormat df = new DecimalFormat("#.##");
                for (Object[] objects : list.getResult()) {
                    String poiName = (String) objects[0];
                    String termName = (String) objects[1];
                    String signInTime = (BigDecimal) objects[2] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[2]).longValue()));
                    String signOutTime = (BigDecimal) objects[3] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[3]).longValue()));
                    String stayTime = (BigDecimal) objects[4] == null ? "0" : df
                            .format(((BigDecimal) objects[4]).doubleValue());
                    String signInDesc = (String) objects[5];
                    String signOutDesc = (String) objects[6];
                    BigDecimal id = (BigDecimal) objects[7];

                    Double signInLongitude = (BigDecimal) objects[8] == null ? null
                            : ((BigDecimal) objects[8]).doubleValue();
                    Double signInLatitude = (BigDecimal) objects[9] == null ? null
                            : ((BigDecimal) objects[9]).doubleValue();
                    Double signOutLongitude = (BigDecimal) objects[10] == null ? null
                            : ((BigDecimal) objects[10]).doubleValue();
                    Double signOutLatitude = (BigDecimal) objects[11] == null ? null
                            : ((BigDecimal) objects[11]).doubleValue();
                    String createOn = (BigDecimal) objects[12] == null ? null : DateUtility
                            .dateTimeToStr(new Date(((BigDecimal) objects[12]).longValue()));

                    if (signInDescIsNUll(signInDesc, signInLongitude, signInLatitude)) {
                        signInDesc = CoordConvertAPI.getLocDescByLngLat(signInLongitude,
                                signInLatitude, logger);
                    }
                    if (signInDescIsNUll(signOutDesc, signOutLongitude, signOutLatitude)) {
                        signOutDesc = CoordConvertAPI.getLocDescByLngLat(signOutLongitude,
                                signOutLatitude, logger);
                    }

                    jsonSb.append("{");
                    jsonSb.append("id:'" + id + "',");// id
                    //jsonSb.append("vehicleNumber:'" + CharTools.javaScriptEscape(termName) + "',");// ����
                    jsonSb.append("arriveTime:'" + signInTime + "',");// ����ʱ��
                    jsonSb.append("leaveTime:'" + signOutTime + "',");// �뿪ʱ��
                    jsonSb.append("stayTime:'" + stayTime + "',");// ͣ��ʱ��,��λ����
                    jsonSb.append("signInDesc:'" + CharTools.javaScriptEscape(signInDesc) + "',");// λ������
                    jsonSb.append("signOutDesc:'" + CharTools.javaScriptEscape(signOutDesc) + "',");// λ������
                    jsonSb.append("visitName:'" + CharTools.javaScriptEscape(poiName) + "',");// ���ÿͻ�����
                    jsonSb.append("tjDate:'" + CharTools.javaScriptEscape(createOn) + "'");// ͳ��ʱ��
                    jsonSb.append("},");
                }
                if (jsonSb.length() > 0) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            }
        }
        logger.info("[listCustomVisitCountTjByCustom] json = " + "{total:" + total + ",data:["
                + jsonSb.toString() + "]}");
        return "{total:" + total + ",data:[" + jsonSb.toString() + "]}";

    }
}
