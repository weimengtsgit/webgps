package com.sosgps.v21.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.sos.helper.SpringHelper;
import org.springframework.beans.BeanUtils;

import cn.net.sosgps.memcache.Memcache;
import cn.net.sosgps.memcache.bean.Group;
import cn.net.sosgps.memcache.bean.Terminal;

import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.model.TargetTemplate;
import com.sosgps.v21.target.ColumeNumberException;
import com.sosgps.v21.target.ParseDataException;
import com.sosgps.v21.target.service.TargetService;
import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.terminal.service.TerminalService;

/**
 * ����ת��������(Ŀ��ά��/����ά��)
 * 
 * @author liuhx
 * 
 */
public class TargetUtils {

    private static final Logger logger = Logger.getLogger(TargetUtils.class);

    /**
     * ������ά�����ݴ�String��ʽת����List<Kpi>
     * 
     * @param data ����ά������(ת��ǰ)
     *        id,type,pass,good,excellent;id,type,pass,good,excellent...
     * @param entCode ��ҵ����
     * @return ����ά������(ת����)
     */
    public static List<Kpi> convertRateFromStringToObject(String entCode, String data) {
        List<Kpi> kpiList = new ArrayList<Kpi>();
        try {
            String[] datas = data.split(";");
            for (String tmp : datas) {
                String[] kpiDatas = tmp.split(",");
                Kpi kpiData = new Kpi();
                if (!kpiDatas[0].equals("-1")) kpiData.setId(Long.valueOf(kpiDatas[0]));
                kpiData.setEntCode(entCode);
                kpiData.setType(Integer.valueOf(kpiDatas[1]));
                kpiData.setKey(kpiDatas[1] + "_" + kpiDatas[2] + "_" + kpiDatas[3] + "_"
                        + kpiDatas[4]);
                kpiData.setValue(kpiDatas[2] + "," + kpiDatas[3] + "," + kpiDatas[4]);
                kpiData.setStates(0);

                kpiList.add(kpiData);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ParseDataException("����ά�����ݸ�ʽ����");
        }
        return kpiList;
    }

    /**
     * ������ά�����ݴ�List<Kpi>ת����Json
     * 
     * @param kpiList ����ά������(ת��ǰ)
     * @return ����ά������(ת����)
     */
    public static String convertRateFromObjectToJson(List<Kpi> kpiList) {
        StringBuffer json = new StringBuffer();
        if (kpiList == null || kpiList.size() < 1) {
            /** ��װJSON * */
            for (int i = 1; i < 6; i++) {
                if (json.length() < 1) {
                    json.append("{");
                } else {
                    json.append(",{");
                }
                json.append("id:-" + i + ",");
                json.append("type:'" + i + "',");
                switch (i) {
                    case 1:
                        json.append("name:'ǩ��������',");
                        break;
                    case 2:
                        json.append("name:'�ؿ������',");
                        break;
                    case 3:
                        json.append("name:'���ö�ʹ����',");
                        break;
                    case 4:
                        json.append("name:'Ա�����ô����',");
                        break;
                    case 5:
                        json.append("name:'�ͻ��ݷø�����',");
                        break;
                }
                //modify by wangzhen start 2012-09-13 �޸���ҳ����ʾ��Ĭ��ֵ
                if (i == 3) {
                    json.append("pass:160,");
                    json.append("good:90,");
                    json.append("excellent:60");
                    //modify by wangzhen end 2012-09-13 �޸���ҳ����ʾ��Ĭ��ֵ
                    json.append("}");
                    continue;

                }
                json.append("pass:60,");
                json.append("good:90,");
                json.append("excellent:160");
                //modify by wangzhen end 2012-09-13 �޸���ҳ����ʾ��Ĭ��ֵ
                json.append("}");
            }
        } else {
            /** ��װJSON * */
            for (Kpi kpi : kpiList) {
                if (json.length() < 1) {
                    json.append("{");
                } else {
                    json.append(",{");
                }
                json.append("id:" + kpi.getId() + ",");
                int type = kpi.getType();
                json.append("type:'" + type + "',");
                switch (type) {
                    case 1:
                        json.append("name:'ǩ��������',");
                        break;
                    case 2:
                        json.append("name:'�ؿ������',");
                        break;
                    case 3:
                        json.append("name:'���ö�ʹ����',");
                        break;
                    case 4:
                        json.append("name:'Ա�����ô����',");
                        break;
                    case 5:
                        json.append("name:'�ͻ��ݷø�����',");
                        break;
                }
                String[] values = kpi.getValue().split(",");
                if (type == 3) {
                    json.append("pass:" + values[2] + ",");
                    json.append("good:" + values[1] + ",");
                    json.append("excellent:" + values[0] + "");
                    json.append("}");
                    continue;
                }
                json.append("pass:" + values[0] + ",");
                json.append("good:" + values[1] + ",");
                json.append("excellent:" + values[2] + "");
                json.append("}");
            }
        }
        return json.toString();
    }

    /**
     * ��ʱ�������б��List<Map<String, Object>>ת����Map<Integer, Map<String,
     * Object>>
     * 
     * @param dateList ʱ����������
     * @return ʱ����������(ת����)
     */
    public static Map<Integer, Map<String, Object>> convertTargetOnFromListToMap(
            List<Map<String, Object>> dateList) {
        Map<Integer, Map<String, Object>> map = new HashMap<Integer, Map<String, Object>>();
        for (Map<String, Object> date : dateList) {
            Integer no = (Integer) date.get("no");
            map.put(no, date);
        }
        return map;
    }

    /**
     * ��Ŀ�����ݴ�List<TargetTemplate>ת����Json
     * 
     * @param dateList ʱ����������
     * @param targetList Ŀ������
     * @return Ŀ������(ת����)
     */
    public static String convertTargetFromObjectToJson(List<Map<String, Object>> dateList,
            List<TargetTemplate> targetList) {
        Map<Integer, Map<String, Object>> dateMap = convertTargetOnFromListToMap(dateList);
        int targetOn = DateUtils.getTargetOnFromNow(dateList);// ��ȡ���޸ĵ�ʱ������ֵ

        TargetService targetService = (TargetService) SpringHelper.getBean("targetService");
        StringBuffer json = new StringBuffer();

        /** ��װJSON * */
        for (TargetTemplate targetTemplate : targetList) {
            Terminal terminal = getTerminal(targetTemplate.getDeviceId(), targetService);
            String groupName = getGroupFullName(targetTemplate.getGroupId(), targetService);
            if (json.length() < 1) {
                json.append("{");
            } else {
                json.append(",{");
            }
            json.append("deviceId:'" + targetTemplate.getDeviceId() + "',");
            //modify by wangzhen start
            //			json.append("vehicleNumber:'" + targetTemplate.getVehicleNumber()
            //					+ "',");
            json.append("vehicleNumber:'" + targetTemplate.getTerminalName() + "',");
            //modify by wangzhen end
            json.append("simcard:'" + terminal.getSimcard() + "',");
            json.append("groupName:'" + groupName + "',");

            json.append("id:'" + targetTemplate.getId() + "',");
            try {
                json.append("billAmount:'"
                        + displaySciCount(String.valueOf(targetTemplate.getBillAmount())) + "',");
                json.append("cashAmount:'"
                        + displaySciCount(String.valueOf(targetTemplate.getCashAmount())) + "',");
                json.append("costAmount:'"
                        + displaySciCount(String.valueOf(targetTemplate.getCostAmount())) + "',");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //			json.append("billAmount:'" + targetTemplate.getBillAmount() + "',");
            //			json.append("cashAmount:'" + targetTemplate.getCashAmount() + "',");
            //			json.append("costAmount:'" + targetTemplate.getCostAmount() + "',");
            json.append("visitAmount:'" + targetTemplate.getVisitAmount() + "',");
            if (targetTemplate.getCusVisitAmount() == null) {
                json.append("cusVisitAmount:'" + 0 + "',");
            } else {
                json.append("cusVisitAmount:'" + targetTemplate.getCusVisitAmount() + "',");
            }

            json.append("targetOn:'" + targetTemplate.getTargetOn() + "',");
            json.append("year:'" + targetTemplate.getYear() + "',");

            Map<String, Object> date = dateMap.get(targetTemplate.getTargetOn());
            json.append("targetDesc:'" + date.get("desc") + "',");
            if (targetTemplate.getTargetOn() >= targetOn) {
                json.append("allowModify:true");
            } else {
                json.append("allowModify:false");
            }
            json.append("}");
        }

        return json.toString();
    }

    /**
     * ����ҵ��Ŀ������ת��ΪJSON��ʽ����ҳ�����ʾ
     * 
     * @param dateList
     * @param targetList
     * @return
     */
    public static String convertEntTargetFromObjectToJson(List<Map<String, Object>> dateList,
            List targetList, int year) {

        Map<Integer, Map<String, Object>> dateMap = convertTargetOnFromListToMap(dateList);
        int targetOn = DateUtils.getTargetOnFromNow(dateList);// ��ȡ���޸ĵ�ʱ������ֵ
        /** ��װJSON * */
        StringBuffer json = new StringBuffer();
        Iterator itr = targetList.iterator();
        while (itr.hasNext()) {
            Object[] nextObj = (Object[]) itr.next();

            if (json.length() < 1) {
                json.append("{");
            } else {
                json.append(",{");
            }

            try {
                json.append("billAmount:'" + displaySciCount(String.valueOf(nextObj[1])) + "',");
                json.append("cashAmount:'" + displaySciCount(String.valueOf(nextObj[2])) + "',");
                json.append("costAmount:'" + displaySciCount(String.valueOf(nextObj[3])) + "',");
            } catch (Exception e) {
                e.printStackTrace();
            }

            json.append("visitAmount:'" + nextObj[4] + "',");
            if (nextObj[5] == null) {
                json.append("cusVisitAmount:'0',");
            } else {
                json.append("cusVisitAmount:'" + nextObj[5] + "',");
            }

            json.append("targetOn:'" + nextObj[0] + "',");
            json.append("year:'" + year + "',");
            Map<String, Object> date = dateMap.get(nextObj[0]);

            json.append("targetDesc:'" + date.get("desc") + "',");
            int a = (Integer) nextObj[0];
            if (a >= targetOn) {
                json.append("allowModify:true");
            } else {
                json.append("allowModify:false");
            }
            json.append("}");
        }

        return json.toString();
    }

    /**
     * ��Ŀ�����ݴ�String��ʽת����List<TargetTemplate>
     * 
     * @param data Ŀ������(ת��ǰ)
     *        id,bill,cash,cost,visit;id,bill,cash,cost,visit...
     * @return Ŀ������(ת����)
     */
    public static List<TargetTemplate> convertTargetFromStringToObject(String data) {
        List<TargetTemplate> targetList = new ArrayList<TargetTemplate>();
        try {
            String[] datas = data.split(";");
            for (String targetStr : datas) {
                String[] targets = targetStr.split(",");
                TargetTemplate target = new TargetTemplate();
                target.setId(Long.valueOf(targets[0]));
                target.setBillAmount(Double.valueOf(targets[1]));
                target.setCashAmount(Double.valueOf(targets[2]));
                target.setCostAmount(Double.valueOf(targets[3]));
                target.setVisitAmount(Integer.valueOf(targets[4]));
                target.setCusVisitAmount(Integer.valueOf(targets[5]));
                target.setStates(0);

                targetList.add(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new ParseDataException("Ŀ�����ݸ�ʽ����");
        }
        return targetList;
    }

    /**
     * ����ҵ��Ŀ�����ݴ�ǰ̨��ȡ��ת��Ϊ��Ҫ�洢�Ķ���
     * 
     * @param data
     * @return
     * @author wangzhen
     */
    public static List<TargetTemplate> convertEntTargetFromStringToObject(String data, int type,
            String entCode) {
        List<TargetTemplate> targetList = new ArrayList<TargetTemplate>();
        try {
            String[] datas = data.split(";");
            for (String targetStr : datas) {
                String[] targets = targetStr.split(",");
                TargetTemplate target = new TargetTemplate();

                target.setBillAmount(Double.valueOf(targets[1]));
                target.setCashAmount(Double.valueOf(targets[2]));
                target.setCostAmount(Double.valueOf(targets[3]));
                target.setVisitAmount(Integer.valueOf(targets[4]));
                //add by wangzhen 2012-09-20
                target.setCusVisitAmount(Integer.valueOf(targets[5]));
                target.setTargetOn(Integer.valueOf(targets[6]));
                target.setYear(Integer.valueOf(targets[7]));
                target.setEntCode(entCode);
                target.setType(type);
                target.setStates(0);

                targetList.add(target);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ParseDataException("Ŀ�����ݸ�ʽ����");
        }
        return targetList;
    }

    /**
     * ������ָ���ϴ����ļ����м��
     * 
     * @param wb
     * @param dateList
     * @param type
     * @param year
     * @return
     * @author wangzhen
     */
    public static boolean checkUploadExcelFile(Workbook workbook,
            List<Map<String, Object>> dateList, Integer type, Integer year, String entCode) {
        Sheet sheet = workbook.getSheetAt(0);

        TerminalService terminalService = (TerminalService) SpringHelper
                .getBean("tTargetObjectService");
        Row row = null;
        //Ѯ���¡�������Ӧ������
        int dateSize = dateList.size();
        //���һ�е�ָ��
        int lastRowNum = sheet.getLastRowNum();
        Map<String, Integer> deviceIdMap = new HashMap<String, Integer>();
        List<String> devIdExcelList = new ArrayList<String>();

        //Ϊ��ֹ���빫˾ָ��
        if (lastRowNum < 4) {
            throw new ColumeNumberException("�����ļ���������ģ�治һ��");
        }

        for (int i = 4; i <= lastRowNum; i++) {
            row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            // ���������ļ�������ģ�治һ��
            if (lastCellNum != (4 + dateSize * 5)) {
                throw new ColumeNumberException("�����ļ���������ģ�治һ��");
            }
            String deviceId = row.getCell(0).getStringCellValue();
            deviceIdMap.put(deviceId, i);
            devIdExcelList.add(deviceId);
        }

        //���ݿ������µģ���ҵ�����µ��ն˺ţ���Ŀǰ��ҵ�е�Ա����
        List<String> devIdDbs = terminalService.getDeviceId(entCode);
        StringBuffer sb = new StringBuffer();

        for (String devId : devIdDbs) {
            int count = 0;
            List<Integer> devRepeats = new ArrayList<Integer>();
            //start for devIdDbs
            for (Map.Entry<String, Integer> entry : deviceIdMap.entrySet()) {
                if (devId.equals(entry.getKey())) {
                    count++;
                    devRepeats.add(entry.getValue());
                }
            } //end for devIdDbs
            if (count > 1) {
                int start = devRepeats.size();
                int end = devRepeats.size() - 1;
                String join = "";
                for (int i = 1; i < devRepeats.size() - 1; i++) {
                    join = devRepeats.get(i) + ",";
                }
                join = join + end;
                sb.append("��" + start + "�е�1���ն�ID���" + join + "�е�1���ظ�");
                if (sb.length() > 0) {

                }
            }
            if (count < 1) {
                //�������У���û���ڣ�Excel���в������նˣ������û����롣
                throw new ColumeNumberException("�����ļ���������ģ�治һ��");
            }
        }

        for (Map.Entry<String, Integer> entry : deviceIdMap.entrySet()) {
            int count = 0;
            for (String devId : devIdDbs) {
                if (entry.getKey().equals(devId)) {
                    count++;
                }
            }
            //���ն�û�����ն����ݿ��з���
            if (count == 0) {
                throw new ParseDataException("�����ļ���������ģ�治һ��");
            }
        }
        return true;
    }

    /**
     * ��Ŀ�����ݴ�Excel��ʽת����List<TargetTemplate>
     * 
     * @param workbook Ŀ������(ת��ǰ)
     * @param dateList ʱ����������
     * @param type ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
     * @param year ���
     * @return Ŀ������(ת����)
     */
    public static List<TargetTemplate> convertTargetFromExcelToObject(Workbook workbook,
            List<Map<String, Object>> dateList, int type, int year) {
        Row row = null;
        Cell cell = null;

        TerminalService terminalService = (TerminalService) SpringHelper
                .getBean("tTargetObjectService");
        Map<String, Integer> deviceIdMap = new HashMap<String, Integer>();
        List<TargetTemplate> targetList = new ArrayList<TargetTemplate>();
        StringBuffer sb = new StringBuffer();
        long currentTime = new Date().getTime();
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        int dateSize = dateList.size();
        /** �������� * */
        int errorSize = 0;
        L1: for (int i = 4; i <= lastRowNum; i++) {
            row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            // ���������ļ�������ģ�治һ��
            if (lastCellNum != (4 + dateSize * 5)) {
                throw new ColumeNumberException("�����ļ���������ģ�治һ��");
            }
            String deviceId = row.getCell(0).getStringCellValue();
            if (i == 4) {
                deviceIdMap.put(deviceId, i);
            } else {
                Integer rownum = deviceIdMap.get(deviceId);
                if (rownum != null) {
                    if (sb.length() < 1) {
                        sb.append("��" + (rownum + 1) + "�е�1���ն�ID���" + (i + 1) + "�е�1���ظ�");
                    } else {
                        sb.append(",��" + (rownum + 1) + "�е�1���ն�ID���" + (i + 1) + "�е�1���ظ�");
                    }
                }
                if (++errorSize >= 20) {
                    break L1;
                }
            }
            int columenum = 4;
            L2: for (Map<String, Object> date : dateList) {
                Date endTime = (Date) date.get("endDay");
                Integer targetOn = (Integer) date.get("no");
                // ���˵���ǰʱ��֮ǰ������
                if (currentTime > endTime.getTime()) {
                    columenum = columenum + 5;
                    continue;
                }

                /** �ն���Ϣ* */
                Terminal termCached = Constants.TERMINAL_CACHE.get(deviceId);
                if (termCached == null) {
                    TTerminal terminal = terminalService.findTerminal(deviceId);
                    if (terminal != null) {
                        termCached = new Terminal();
                        BeanUtils.copyProperties(terminal, termCached);
                        RefTermGroup refTermGroup = terminalService.getTermGroup(terminal);
                        termCached.setGroupId(refTermGroup.getTTermGroup().getId());
                        Constants.TERMINAL_CACHE
                                .set(deviceId, termCached, 10 * Memcache.EXPIRE_DAY);// �ն�ʱ�䵽��,oracle_job���޸��ն˱�ʶΪ�����ڡ�,��˻���10��
                    }
                }
                // �ն˲����ڻ��ն˵���
                if (termCached == null || termCached.getExpirationFlag() != 0) {
                    continue;
                }

                /** ����Ϣ* */
                String groupId = String.valueOf(termCached.getGroupId());
                Group groupCached = Constants.GROUP_CACHE.get(groupId);
                if (groupCached == null) {
                    TTermGroup group = terminalService.getTTargetGroupByGroupId(termCached
                            .getGroupId());
                    if (group != null) {
                        groupCached = new Group();
                        BeanUtils.copyProperties(group, groupCached);
                        Constants.GROUP_CACHE.set(groupId, groupCached, 10 * Memcache.EXPIRE_DAY);// ����ҵ�ն˱���һ��,����10��
                    }
                }

                TargetTemplate target = new TargetTemplate();
                target.setDeviceId(deviceId);
                target.setStates(0);
                target.setType(type);
                target.setYear(year);
                target.setTargetOn(targetOn);
                target.setGroupId(groupCached.getId());
                target.setGroupName(groupCached.getGroupName());
                target.setEntCode(termCached.getEntCode());
                //modify by wangzhen
                //				target.setVehicleNumber(termCached.getVehicleNumber());
                //				target.setVehicleNumber(termCached.getTermName());
                target.setTerminalName(termCached.getTermName());

                // ǩ����(Ԫ)
                cell = row.getCell(columenum);
                try {
                    int cellType = cell.getCellType();
                    switch (cellType) {
                        case 0:
                            //target.setBillAmount(cell.getNumericCellValue());
                            String billAmout = isSciCount(String
                                    .valueOf(cell.getNumericCellValue()));
                            target.setBillAmount(Double.valueOf(billAmout));
                            billAmout = "0.0";
                            break;
                        case 1:
                            String billAmout2 = isSciCount(cell.getStringCellValue());
                            target.setBillAmount(Double.valueOf(billAmout2));
                            billAmout2 = "0.0";
                            break;
                        default:
                            if (sb.length() < 1) {
                                sb.append("��" + (i + 1) + "�е�" + (columenum + 1) + "�����ݸ�ʽ����");
                            } else {
                                sb.append(",��" + (i + 1) + "�е�" + (columenum + 1) + "�����ݸ�ʽ����");
                            }
                            if (++errorSize >= 20) {
                                break L1;
                            }
                    }
                } catch (Exception e) {
                    logger.error(e);
                    if (sb.length() < 1) {
                        sb.append("��" + (i + 1) + "�е�" + (columenum + 1) + "�����ݸ�ʽ����");
                    } else {
                        sb.append(",��" + (i + 1) + "�е�" + (columenum + 1) + "�����ݸ�ʽ����");
                    }
                    if (++errorSize >= 20) {
                        break L1;
                    }
                }

                // �ؿ��(Ԫ)
                cell = row.getCell(columenum + 1);
                try {
                    int cellType = cell.getCellType();
                    switch (cellType) {
                        case 0:
                            //target.setCashAmount(cell.getNumericCellValue());
                            String cashAmout = isSciCount(String
                                    .valueOf(cell.getNumericCellValue()));
                            target.setCashAmount(Double.valueOf(cashAmout));
                            cashAmout = "0.0";
                            break;
                        case 1:
                            String cashAmout2 = isSciCount(cell.getStringCellValue());
                            target.setCashAmount(Double.valueOf(cashAmout2));
                            cashAmout2 = "0.0";
                            break;
                        default:
                            if (sb.length() < 1) {
                                sb.append("��" + (i + 1) + "�е�" + (columenum + 2) + "�����ݸ�ʽ����");
                            } else {
                                sb.append(",��" + (i + 1) + "�е�" + (columenum + 2) + "�����ݸ�ʽ����");
                            }
                            if (++errorSize >= 20) {
                                break L1;
                            }
                    }
                } catch (Exception e) {
                    logger.error(e);
                    if (sb.length() < 1) {
                        sb.append("��" + (i + 1) + "�е�" + (columenum + 2) + "�����ݸ�ʽ����");
                    } else {
                        sb.append(",��" + (i + 1) + "�е�" + (columenum + 2) + "�����ݸ�ʽ����");
                    }
                    if (++errorSize >= 20) {
                        break L1;
                    }
                }

                // ���ö�(Ԫ)
                cell = row.getCell(columenum + 2);
                try {
                    int cellType = cell.getCellType();
                    switch (cellType) {
                        case 0:
                            //target.setCostAmount(cell.getNumericCellValue());
                            String costAmout = isSciCount(String
                                    .valueOf(cell.getNumericCellValue()));
                            target.setCostAmount(Double.valueOf(costAmout));
                            costAmout = "0.0";
                            break;
                        case 1:
                            String costAmout2 = isSciCount(cell.getStringCellValue());
                            target.setCostAmount(Double.valueOf(costAmout2));
                            costAmout2 = "0.0";
                            break;
                        default:
                            if (sb.length() < 1) {
                                sb.append("��" + (i + 1) + "�е�" + (columenum + 3) + "�����ݸ�ʽ����");
                            } else {
                                sb.append(",��" + (i + 1) + "�е�" + (columenum + 3) + "�����ݸ�ʽ����");
                            }
                            if (++errorSize >= 20) {
                                break L1;
                            }
                    }
                } catch (Exception e) {
                    logger.error(e);
                    if (sb.length() < 1) {
                        sb.append("��" + (i + 1) + "�е�" + (columenum + 3) + "�����ݸ�ʽ����");
                    } else {
                        sb.append(",��" + (i + 1) + "�е�" + (columenum + 3) + "�����ݸ�ʽ����");
                    }
                    if (++errorSize >= 20) {
                        break L1;
                    }
                }

                // ҵ��Ա���ô���(��)
                cell = row.getCell(columenum + 3);
                try {
                    int cellType = cell.getCellType();
                    switch (cellType) {
                        case 0:
                            String visitAmout = isSciCount(String.valueOf(cell
                                    .getNumericCellValue()));
                            boolean check = isInteger(visitAmout);
                            //boolean check = isInteger(String.valueOf(cell.getNumericCellValue()));
                            if (check) {
                                target.setVisitAmount((int) cell.getNumericCellValue());
                            } else {
                                throw new Exception();
                            }
                            visitAmout = "0";
                            break;
                        case 1:
                            Integer.parseInt(cell.getStringCellValue());
                            target.setVisitAmount(Integer.valueOf(cell.getStringCellValue()));
                            break;
                        default:
                            if (sb.length() < 1) {
                                sb.append("��" + (i + 1) + "�е�" + (columenum + 4) + "�����ݸ�ʽ����");
                            } else {
                                sb.append(",��" + (i + 1) + "�е�" + (columenum + 4) + "�����ݸ�ʽ����");
                            }
                            if (++errorSize >= 20) {
                                break L1;
                            }
                    }
                } catch (Exception e) {
                    logger.error(e);
                    if (sb.length() < 1) {
                        sb.append("��" + (i + 1) + "�е�" + (columenum + 4) + "�����ݸ�ʽ����");
                    } else {
                        sb.append(",��" + (i + 1) + "�е�" + (columenum + 4) + "�����ݸ�ʽ����");
                    }
                    if (++errorSize >= 20) {
                        break L1;
                    }
                }

                // �ݷÿͻ���(��)
                cell = row.getCell(columenum + 4);
                try {
                    int cellType = cell.getCellType();
                    switch (cellType) {
                        case 0:
                            String cusVisitAmout = isSciCount(String.valueOf(cell
                                    .getNumericCellValue()));
                            boolean check = isInteger(cusVisitAmout);
                            //boolean check = isInteger(String.valueOf(cell.getNumericCellValue()));
                            if (check) {
                                target.setCusVisitAmount((int) cell.getNumericCellValue());
                            } else {
                                throw new Exception();
                            }
                            cusVisitAmout = "0";
                            break;
                        case 1:
                            Integer.parseInt(cell.getStringCellValue());
                            target.setCusVisitAmount(Integer.valueOf(cell.getStringCellValue()));
                            break;
                        default:
                            if (sb.length() < 1) {
                                sb.append("��" + (i + 1) + "�е�" + (columenum + 5) + "�����ݸ�ʽ����");
                            } else {
                                sb.append(",��" + (i + 1) + "�е�" + (columenum + 5) + "�����ݸ�ʽ����");
                            }
                            if (++errorSize >= 20) {
                                break L1;
                            }
                    }
                } catch (Exception e) {
                    logger.error(e);
                    if (sb.length() < 1) {
                        sb.append("��" + (i + 1) + "�е�" + (columenum + 5) + "�����ݸ�ʽ����");
                    } else {
                        sb.append(",��" + (i + 1) + "�е�" + (columenum + 5) + "�����ݸ�ʽ����");
                    }
                    if (++errorSize >= 20) {
                        break L1;
                    }
                }
                targetList.add(target);

                columenum = columenum + 5;
            }
        }
        if (sb.length() > 0) {
            throw new ParseDataException(sb.toString());
        }
        //modify by wangzhen start 2012-09-12 ��ʾ��Ҫ���빫˾�����ģ��
        if (lastRowNum < 4) {
            throw new ColumeNumberException("�����ļ���������ģ�治һ��");
        }
        //modify by wangzhen end 2012-09-12 
        return targetList;
    }

    //	/**
    //	 * 
    //	 * @param numericCellValue
    //	 * @return
    //	 */
    //	private static boolean judgeDecimal(double numericCellValue) {
    //		String temp = Double.toString(numericCellValue);
    //    	int start = temp.indexOf(".");
    //        String o = temp.substring(start+1,start+2);
    //        if("0".equals(o)) {
    //        	return true;
    //        } else {
    //        	return false;
    //        }
    //	}

    //	/**
    //	 * ����Excel�е�С�����Ϊ��λ����true,���򷵻�false.
    //	 * @param numericCellValue
    //	 * @return
    //	 */
    //	private static boolean checkDecimalNO(double numericCellValue) {
    //    	String temp = Double.toString(numericCellValue);
    //    	int start=temp.indexOf(".");
    //    	try{
    //    		temp.substring(start+1,start+3);
    //    	}catch(Exception e) {
    //    		return false;
    //    	}
    //    	return true;
    //	}

    /**
     * ���ϴ����ļ������ɶ���
     * 
     * @param workbook
     * @param dateList
     * @param type
     * @param year
     * @return
     * @author wangzhen
     */
    public static List<TargetTemplate> convertEntTargetFromExcelToObject(Workbook workbook,
            List<Map<String, Object>> dateList, int type, int year, String entCode) {

        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum == 0 || lastRowNum == 1) {
            throw new ColumeNumberException("�����ļ���������ģ�治һ��");
        }
        int dateSize = dateList.size();

        /** �������� * */
        //��Excel�ļ��е����п�ʼ�������������е��±�Ϊ2
        Row row = sheet.getRow(2);
        int lastCellNum = row.getLastCellNum();
        if (lastCellNum != (dateSize * 5)) {
            throw new ColumeNumberException("�����ļ���������ģ�治һ��");
        }

        int columenum = 0;
        long currentTime = new Date().getTime();
        Cell cell = null;
        List<TargetTemplate> targetList = new ArrayList<TargetTemplate>();
        StringBuffer sb = new StringBuffer();

        //ѭ��������������
        for (Map<String, Object> date : dateList) {
            Date endTime = (Date) date.get("endDay");
            Integer targetOn = (Integer) date.get("no");

            // ���˵���ǰʱ��֮ǰ������
            if (currentTime > endTime.getTime()) {
                columenum = columenum + 5;
                continue;
            }

            TargetTemplate target = new TargetTemplate();
            target.setStates(0);//0,��Ч��1 Ϊɾ��
            target.setType(type);
            target.setYear(year);
            target.setTargetOn(targetOn);
            target.setEntCode(entCode);

            cell = row.getCell(columenum);

            // ǩ����(Ԫ)
            try {
                int cellType = cell.getCellType();
                switch (cellType) {
                    case 0:
                        String billAmount = isSciCount(String.valueOf(cell.getNumericCellValue()));
                        target.setBillAmount(Double.valueOf(billAmount));
                        billAmount = "0.0";
                        break;
                    case 1:
                        String billAmount2 = isSciCount(cell.getStringCellValue());
                        target.setBillAmount(Double.valueOf(billAmount2));
                        billAmount2 = "0.0";
                        break;
                    case 3:
                        break;
                    default:
                        if (sb.length() < 1) {
                            sb.append("��" + (3) + "�е�" + (columenum + 1) + "�����ݸ�ʽ����");
                        } else {
                            sb.append(",��" + (3) + "�е�" + (columenum + 1) + "�����ݸ�ʽ����");
                        }
                }
            } catch (Exception e) {
                logger.error(e);
                if (sb.length() < 1) {
                    sb.append("��" + (3) + "�е�" + (columenum + 1) + "�����ݸ�ʽ����");
                } else {
                    sb.append(",��" + (3) + "�е�" + (columenum + 1) + "�����ݸ�ʽ����");
                }
            }

            // �ؿ��(Ԫ)
            cell = row.getCell(columenum + 1);
            try {
                int cellType = cell.getCellType();
                switch (cellType) {
                    case 0:
                        String cashAmout = isSciCount(String.valueOf(cell.getNumericCellValue()));
                        target.setCashAmount(Double.valueOf(cashAmout));
                        cashAmout = "0.0";
                        break;
                    case 1:
                        String cashAmout2 = isSciCount(cell.getStringCellValue());
                        target.setCashAmount(Double.valueOf(cashAmout2));
                        cashAmout2 = "0.0";
                        break;
                    case 3:
                        break;
                    default:
                        if (sb.length() < 1) {
                            sb.append("��" + (3) + "�е�" + (columenum + 2) + "�����ݸ�ʽ����");
                        } else {
                            sb.append(",��" + (3) + "�е�" + (columenum + 2) + "�����ݸ�ʽ����");
                        }
                }
            } catch (Exception e) {
                logger.error(e);
                if (sb.length() < 1) {
                    sb.append("��" + (3) + "�е�" + (columenum + 2) + "�����ݸ�ʽ����");
                } else {
                    sb.append(",��" + (3) + "�е�" + (columenum + 2) + "�����ݸ�ʽ����");
                }
            }

            // ���ö�(Ԫ)
            cell = row.getCell(columenum + 2);
            try {
                int cellType = cell.getCellType();
                switch (cellType) {
                    case 0:
                        String costAmout = isSciCount(String.valueOf(cell.getNumericCellValue()));
                        target.setCostAmount(Double.parseDouble(costAmout));
                        costAmout = "0.0";
                        //target.setCostAmount(cell.getNumericCellValue());
                        break;
                    case 1:
                        String costAmout2 = isSciCount(cell.getStringCellValue());
                        target.setCostAmount(Double.valueOf(costAmout2));
                        costAmout2 = "0.0";
                        break;
                    case 3:
                        break;
                    default:
                        if (sb.length() < 1) {
                            sb.append("��" + (3) + "�е�" + (columenum + 3) + "�����ݸ�ʽ����");
                        } else {
                            sb.append(",��" + (3) + "�е�" + (columenum + 3) + "�����ݸ�ʽ����");
                        }
                }
            } catch (Exception e) {
                logger.error(e);
                if (sb.length() < 1) {
                    sb.append("��" + (3) + "�е�" + (columenum + 3) + "�����ݸ�ʽ����");
                } else {
                    sb.append(",��" + (3) + "�е�" + (columenum + 3) + "�����ݸ�ʽ����");
                }
            }

            //ҵ��Ա���ô������Σ�
            cell = row.getCell(columenum + 3);
            try {
                int cellType = cell.getCellType();
                switch (cellType) {
                    case 0:
                        /*
                         * �����жϸ����ǲ���С����
                         * �ж����Ƿ�������ַ�
                         * ������ΪAPI��ԭ��cell.getNumericCellValue ȡ����ֵΪDoubleֵ��
                         * ���Զ�20089.0��������ҲҪ����������
                         */
                        String visitAmout = isSciCount(String.valueOf(cell.getNumericCellValue()));
                        boolean check = isInteger(visitAmout);
                        if (check) {
                            target.setVisitAmount((int) cell.getNumericCellValue());
                        } else {
                            throw new Exception();
                        }
                        visitAmout = "0";
                        break;
                    case 1:
                        target.setVisitAmount(Integer.valueOf(cell.getStringCellValue()));
                        break;
                    case 3:
                        break;
                    default:
                        if (sb.length() < 1) {
                            sb.append("��" + (3) + "�е�" + (columenum + 4) + "�����ݸ�ʽ����");
                        } else {
                            sb.append(",��" + (3) + "�е�" + (columenum + 4) + "�����ݸ�ʽ����");
                        }
                }
            } catch (Exception e) {
                logger.error(e);
                if (sb.length() < 1) {
                    sb.append("��" + (3) + "�е�" + (columenum + 4) + "�����ݸ�ʽ����");
                } else {
                    sb.append(",��" + (3) + "�е�" + (columenum + 4) + "�����ݸ�ʽ����");
                }
            }

            // �ݷÿͻ���(��)
            cell = row.getCell(columenum + 4);
            try {
                int cellType = cell.getCellType();
                switch (cellType) {
                    case 0:
                        String cusVisitAmout = isSciCount(String
                                .valueOf(cell.getNumericCellValue()));
                        boolean check = isInteger(cusVisitAmout);
                        if (check) {
                            target.setCusVisitAmount((int) cell.getNumericCellValue());
                        } else {
                            throw new Exception();
                        }
                        cusVisitAmout = "0";
                        break;
                    case 1:
                        target.setCusVisitAmount(Integer.valueOf(cell.getStringCellValue()));
                        break;
                    case 3:
                        break;
                    default:
                        if (sb.length() < 1) {
                            sb.append("��" + (3) + "�е�" + (columenum + 5) + "�����ݸ�ʽ����");
                        } else {
                            sb.append(",��" + (3) + "�е�" + (columenum + 5) + "�����ݸ�ʽ����");
                        }
                }
            } catch (Exception e) {
                logger.error(e);
                if (sb.length() < 1) {
                    sb.append("��" + (3) + "�е�" + (columenum + 5) + "�����ݸ�ʽ����");
                } else {
                    sb.append(",��" + (3) + "�е�" + (columenum + 5) + "�����ݸ�ʽ����");
                }
            }
            targetList.add(target);
            columenum = columenum + 5;
        }
        if (sb.length() > 0) {
            throw new ParseDataException(sb.toString());
        }
        return targetList;
    }

    /**
     * ����ѧ������
     * 
     * @param str
     * @return
     * @throws ParseException
     */
    private static String isSciCount(String str) throws ParseException {
        if (null != str && str.indexOf(".") != -1 && str.indexOf("E") != -1) {
            DecimalFormat df = new DecimalFormat();
            str = df.parse(str).toString();
        }
        return str;
    }

    /**
     * ����ѧ���������ַ�����ҳ��������ʾ
     * 
     * @param str
     * @return
     * @throws ParseException
     */
    private static String displaySciCount(String str) throws ParseException {
        if (null != str && str.indexOf(".") != -1 && str.indexOf("E") != -1) {
            DecimalFormat df = new DecimalFormat();
            str = df.parse(str).toString();
            str = str + ".0";
        }
        return str;
    }

    /**
     * ���ܣ��������isInteger�����Ĳ����Ƿ�Ϊ����
     * 
     * @param str String
     * @return ����boolean���ͣ�false��ʾ����������true��ʾ������
     */
    private static boolean isInteger(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        //���з��ŵĸ�������
        if (str.startsWith("+") || str.startsWith("-")) {
            return false;
        }
        if (null != str && str.endsWith(".0")) {
            //int size =  str.length(); 
            //str =  str.substring(0, size - 2); 
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)) && '0' != str.charAt(i + 1)) {
                return false;
            }
        }

        return true;
    }

    /**
     * ��Ŀ�����ݴ�Map<String, TargetTemplate>��ʽת����Excel
     * 
     * @param terminalList �ն�-������
     * @param dateList ʱ����������
     * @param targetMap Ŀ������(ת��ǰ)
     * @param type ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
     * @param clazz Excel���ݰ汾,HSSFWorkbook-2003�����°汾,XSSFWorkbook-2007�����°汾
     * @return Ŀ������(ת����)
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Workbook convertTargetFromObjectToExcel(List<TTerminal> terminalList,
            List<Map<String, Object>> dateList, Map<String, TargetTemplate> targetMap, int type,
            Class<? extends Workbook> clazz) throws InstantiationException, IllegalAccessException {
        Workbook wb = clazz.newInstance();
        CellStyle defaultCs = getDefaultCellStyle(wb);
        CellStyle headerCs = getHeaderCellStyle(wb);
        CellStyle tipCs = getTipCellStyle(wb);
        Sheet sheet = wb.createSheet("ָ��ά��" + (type == 0 ? "��" : (type == 1 ? "Ѯ" : "��")) + "ģ��");

        /** ����ʾ��Ϣ * */
        addCell(sheet, 0, 0, 0, 0, "Ĭ�ϰ�Ա����������", tipCs, true);
        addCell(sheet, 1, 1, 0, 0, "�ն�ID��Ա������һһ��Ӧ�����޸ģ���ͬ�����¶�λƽ̨�ն˹����е���Ϣ", tipCs, true);

        /** ��װͷ��Ϣ * */
        addCell(sheet, 2, 3, 0, 0, "�ն�ID", headerCs, true);
        addCell(sheet, 2, 3, 1, 1, "Ա������", headerCs, false);
        sheet.setColumnWidth(1, 5000);
        addCell(sheet, 2, 3, 2, 2, "�ֻ�����", headerCs, false);
        sheet.setColumnWidth(2, 5000);
        addCell(sheet, 2, 3, 3, 3, "��������", headerCs, false);
        sheet.setColumnWidth(3, 7000);
        int columenum = 4;
        long currentTime = new Date().getTime();
        for (Map<String, Object> date : dateList) {
            Date endTime = (Date) date.get("endDay");

            addCell(sheet, 2, 2, columenum, columenum + 4, (String) date.get("desc"), headerCs,
                    false);
            addCell(sheet, 3, 3, columenum, columenum, "ǩ����(Ԫ)", headerCs, false);
            addCell(sheet, 3, 3, columenum + 1, columenum + 1, "�ؿ��(Ԫ)", headerCs, false);
            addCell(sheet, 3, 3, columenum + 2, columenum + 2, "���ö�(Ԫ)", headerCs, false);
            addCell(sheet, 3, 3, columenum + 3, columenum + 3, "ҵ��Ա���ô���(��)", headerCs, false);
            addCell(sheet, 3, 3, columenum + 4, columenum + 4, "�ݷÿͻ���(��)", headerCs, false);

            sheet.setColumnWidth(columenum, 4000);
            sheet.setColumnWidth(columenum + 1, 4000);
            sheet.setColumnWidth(columenum + 2, 4000);
            sheet.setColumnWidth(columenum + 3, 6000);
            sheet.setColumnWidth(columenum + 4, 5000);

            // ���ص�С�ڵ�ǰʱ�����;����,��ģ��,7��17�յ�����ģ����ֻ��ʾ7�·ݼ�7�·��Ժ��,��ȥʱ�������Ĭ������
            if (currentTime > endTime.getTime()) {
                sheet.setColumnHidden(columenum, true);
                sheet.setColumnHidden(columenum + 1, true);
                sheet.setColumnHidden(columenum + 2, true);
                sheet.setColumnHidden(columenum + 3, true);
                sheet.setColumnHidden(columenum + 4, true);
            }

            columenum = columenum + 5;
        }

        /** ��װ���� * */
        TargetService targetService = (TargetService) SpringHelper.getBean("targetService");
        int rownum = 4;
        for (TTerminal terminal : terminalList) {
            String groupName = getGroupFullName(getGroupId(terminal), targetService);
            addCell(sheet, rownum, rownum, 0, 0, terminal.getDeviceId(), defaultCs, true);
            //modify by wangzhen
            //			addCell(sheet, rownum, rownum, 1, 1, terminal.getVehicleNumber(),
            //					defaultCs, false);
            addCell(sheet, rownum, rownum, 1, 1, terminal.getTermName(), defaultCs, false);
            addCell(sheet, rownum, rownum, 2, 2, terminal.getSimcard(), defaultCs, false);
            addCell(sheet, rownum, rownum, 3, 3, groupName, defaultCs, false);

            // ָ����Ϣ
            columenum = 4;
            for (Map<String, Object> date : dateList) {
                TargetTemplate templateData = targetMap.get(terminal.getDeviceId() + "_"
                        + date.get("no"));
                if (templateData == null) {
                    addCell(sheet, rownum, rownum, columenum, columenum, 0, defaultCs, false);
                    addCell(sheet, rownum, rownum, columenum + 1, columenum + 1, 0, defaultCs,
                            false);
                    addCell(sheet, rownum, rownum, columenum + 2, columenum + 2, 0, defaultCs,
                            false);
                    addCell(sheet, rownum, rownum, columenum + 3, columenum + 3, 0, defaultCs,
                            false);
                    addCell(sheet, rownum, rownum, columenum + 4, columenum + 4, 0, defaultCs,
                            false);
                } else {
                    if (templateData.getBillAmount() == null) addCell(sheet, rownum, rownum,
                            columenum, columenum, 0, defaultCs, false);
                    else addCell(sheet, rownum, rownum, columenum, columenum,
                            templateData.getBillAmount(), defaultCs, false);

                    if (templateData.getCashAmount() == null) addCell(sheet, rownum, rownum,
                            columenum + 1, columenum + 1, 0, defaultCs, false);
                    else addCell(sheet, rownum, rownum, columenum + 1, columenum + 1,
                            templateData.getCashAmount(), defaultCs, false);

                    if (templateData.getCostAmount() == null) addCell(sheet, rownum, rownum,
                            columenum + 2, columenum + 2, 0, defaultCs, false);
                    else addCell(sheet, rownum, rownum, columenum + 2, columenum + 2,
                            templateData.getCostAmount(), defaultCs, false);

                    if (templateData.getVisitAmount() == null) addCell(sheet, rownum, rownum,
                            columenum + 3, columenum + 3, 0, defaultCs, false);
                    else addCell(sheet, rownum, rownum, columenum + 3, columenum + 3,
                            templateData.getVisitAmount(), defaultCs, false);

                    if (templateData.getCusVisitAmount() == null) addCell(sheet, rownum, rownum,
                            columenum + 4, columenum + 4, 0, defaultCs, false);
                    else addCell(sheet, rownum, rownum, columenum + 4, columenum + 4,
                            templateData.getCusVisitAmount(), defaultCs, false);
                }

                columenum = columenum + 5;
            }
            rownum++;
        }

        /** ����Excel���� * */
        sheet.createFreezePane(4, 4);

        return wb;
    }

    /**
     * ��˾����Ŀ������д��Excel�ļ���ȥ
     * 
     * @author wangzhen
     */
    public static Workbook convertEntTargetFromObjectToExcel(Map<String, Object[]> targetData,
            List<Map<String, Object>> dateList, int type, Class<? extends Workbook> clazz)
            throws InstantiationException, IllegalAccessException {

        Workbook wb = clazz.newInstance();
        //Excel�ļ�����ʽ
        CellStyle defaultCs = getDefaultCellStyle(wb);
        CellStyle headerCs = getHeaderCellStyle(wb);
        CellStyle tipCs = getTipCellStyle(wb);
        //����sheet
        Sheet sheet = wb.createSheet("��˾����ָ��ά��" + (type == 0 ? "��" : (type == 1 ? "Ѯ" : "��"))
                + "ģ��");

        int columenum = 0;
        addCell(sheet, 0, 0, columenum, columenum, null, headerCs, true);
        addCell(sheet, 1, 1, columenum, columenum, null, headerCs, true);

        long currentTime = new Date().getTime();
        //�������⣨�磺�·ݡ��·��µ�ǩ����...
        for (Map<String, Object> date : dateList) {
            Date endTime = (Date) date.get("endDay");
            addCell(sheet, 0, 0, columenum, columenum + 4, (String) date.get("desc"), headerCs,
                    false);
            addCell(sheet, 1, 1, columenum, columenum, "ǩ����(Ԫ)", headerCs, false);
            addCell(sheet, 1, 1, columenum + 1, columenum + 1, "�ؿ��(Ԫ)", headerCs, false);
            addCell(sheet, 1, 1, columenum + 2, columenum + 2, "���ö�(Ԫ)", headerCs, false);
            addCell(sheet, 1, 1, columenum + 3, columenum + 3, "ҵ��Ա���ô���(��)", headerCs, false);
            addCell(sheet, 1, 1, columenum + 4, columenum + 4, "�ݷÿͻ���(��)", headerCs, false);

            sheet.setColumnWidth(columenum, 4000);
            sheet.setColumnWidth(columenum + 1, 4000);
            sheet.setColumnWidth(columenum + 2, 4000);
            sheet.setColumnWidth(columenum + 3, 6000);
            sheet.setColumnWidth(columenum + 4, 5000);
            // ���ص�С�ڵ�ǰʱ�����;����,��ģ��,7��17�յ�����ģ����ֻ��ʾ7�·ݼ�7�·��Ժ��,��ȥʱ�������Ĭ������
            if (currentTime > endTime.getTime()) {
                sheet.setColumnHidden(columenum, true);
                sheet.setColumnHidden(columenum + 1, true);
                sheet.setColumnHidden(columenum + 2, true);
                sheet.setColumnHidden(columenum + 3, true);
                sheet.setColumnHidden(columenum + 4, true);
            }
            columenum = columenum + 5;
        }

        //װ������
        int rownum = 2;
        columenum = 0;
        addCell(sheet, rownum, rownum, columenum, columenum, null, defaultCs, true);
        for (Map<String, Object> date : dateList) {
            Object[] dataObj = targetData.get(String.valueOf(date.get("no")));
            if (dataObj == null) {
                addCell(sheet, rownum, rownum, columenum, columenum, 0, defaultCs, false);
                addCell(sheet, rownum, rownum, columenum + 1, columenum + 1, 0, defaultCs, false);
                addCell(sheet, rownum, rownum, columenum + 2, columenum + 2, 0, defaultCs, false);
                addCell(sheet, rownum, rownum, columenum + 3, columenum + 3, 0, defaultCs, false);
                addCell(sheet, rownum, rownum, columenum + 4, columenum + 4, 0, defaultCs, false);
            } else {
                //0 ��targetOn,1��ǩ���2�ǻؿ�3�Ƿ��ö4�ǰݷô���
                if (dataObj[1] == null) addCell(sheet, rownum, rownum, columenum, columenum, 0,
                        defaultCs, false);
                else addCell(sheet, rownum, rownum, columenum, columenum,
                        String.valueOf(dataObj[1]), defaultCs, false);

                if (dataObj[2] == null) addCell(sheet, rownum, rownum, columenum + 1,
                        columenum + 1, 0, defaultCs, false);
                else addCell(sheet, rownum, rownum, columenum + 1, columenum + 1,
                        String.valueOf(dataObj[2]), defaultCs, false);

                if (dataObj[3] == null) addCell(sheet, rownum, rownum, columenum + 2,
                        columenum + 2, 0, defaultCs, false);
                else addCell(sheet, rownum, rownum, columenum + 2, columenum + 2,
                        String.valueOf(dataObj[3]), defaultCs, false);

                if (dataObj[4] == null) addCell(sheet, rownum, rownum, columenum + 3,
                        columenum + 3, 0, defaultCs, false);
                else addCell(sheet, rownum, rownum, columenum + 3, columenum + 3,
                        String.valueOf(dataObj[4]), defaultCs, false);
                if (dataObj[5] == null) addCell(sheet, rownum, rownum, columenum + 4,
                        columenum + 4, 0, defaultCs, false);
                else addCell(sheet, rownum, rownum, columenum + 4, columenum + 4,
                        String.valueOf(dataObj[5]), defaultCs, false);
            }
            columenum = columenum + 5;
        }
        return wb;
    }

    /**
     * ����ҵ��ָ��ƽ�����䵽��ҵԱ����ָ����
     * 
     * @param targets
     * @param count
     * @return
     * @author wangzhen
     */
    public static List<TargetTemplate> avgEntTarget(List<TargetTemplate> fullTargets, int count,
            TargetTemplate tt) {

        //��ǰ̨���������Ķ���õ��ܵ�ָ�꣺���á����ݷô���...
        Double billCount = null;
        Double cashCount = null;
        Double costCount = null;
        int visitCount = 0;
        int cusVisitAmount = 0;
        if (tt.getBillAmount() == null) {
            billCount = 0d;
        } else {
            billCount = tt.getBillAmount();
        }
        if (tt.getCashAmount() == null) {
            cashCount = 0d;
        } else {
            cashCount = tt.getCashAmount();
        }
        if (tt.getCostAmount() == null) {
            costCount = 0d;
        } else {
            costCount = tt.getCostAmount();
        }
        if (tt.getVisitAmount() == null) {
            visitCount = 0;
        } else {
            visitCount = tt.getVisitAmount();
        }
        if (tt.getCusVisitAmount() == null) {
            cusVisitAmount = 0;
        } else {
            cusVisitAmount = tt.getCusVisitAmount();
        }

        //����ƽ����ҵָ����list
        List<TargetTemplate> tarListDeals = new ArrayList<TargetTemplate>();

        boolean bill = false;
        boolean cash = false;
        boolean cost = false;
        boolean visit = false;
        boolean cusVisit = false;

        //������ݴ���С��λ2λ���Ͻ��и�ʽ��Ϊ��λ��
        NumberFormat nf = new DecimalFormat("0.00");
        billCount = Double.valueOf(nf.format(billCount));
        cashCount = Double.valueOf(nf.format(cashCount));
        costCount = Double.valueOf(nf.format(costCount));

        /*
         * ��Ϊ�������λС��ƽ������Ҫ������������С��������2.42 --2.42 *100=242 �ڶ�242����ƽ����
         */
        long billCountLong = dealMoneyIntegers(billCount);
        long cashCountLong = dealMoneyIntegers(cashCount);
        long costCountLong = dealMoneyIntegers(costCount);

        //��������������Ϳ���ֱ����ƽ������
        if (billCountLong % count == 0) {
            bill = true;
        }
        if (cashCountLong % count == 0) {
            cash = true;
        }
        if (costCountLong % count == 0) {
            cost = true;
        }
        if (visitCount % count == 0) {
            visit = true;
        }
        if (cusVisitAmount % count == 0) {
            cusVisit = true;
        }

        //���ܿɲ�������������ȡ������֮�����������,����������������ٶ��������д���
        long avgBillCount = billCountLong / count;
        long avgCashCount = cashCountLong / count;
        long avgCostCount = costCountLong / count;
        int avgVisitCount = visitCount / count;
        int avgCusVisitCount = cusVisitAmount / count;

        if (bill && cash && cost && visit && cusVisit) {
            tarListDeals = dealAllDiv(fullTargets, avgBillCount, avgCashCount, avgCostCount,
                    avgVisitCount, avgCusVisitCount);
        } else if (bill && cash && cost && cusVisit && !(visit)) {
            //visit�����������ȶ�����������Ȼ�������������
            tarListDeals = dealAllDiv(fullTargets, avgBillCount, avgCashCount, avgCostCount,
                    avgVisitCount, avgCusVisitCount);
            tarListDeals = dealRemDataforVisit(tarListDeals, visitCount, count, "visit");

        } else if (bill && cash && (cost) && !(cusVisit) && !(visit)) {
            tarListDeals = dealAllDiv(fullTargets, avgBillCount, avgCashCount, avgCostCount,
                    avgVisitCount, avgCusVisitCount);
            tarListDeals = dealRemDataforVisit(tarListDeals, visitCount, count, "visit");
            tarListDeals = dealRemDataforVisit(tarListDeals, cusVisitAmount, count, "cusVisit");

        } else if (bill && cash && !(cost) && !(cusVisit) && !(visit)) {

            tarListDeals = dealRemMoneyData(fullTargets, costCountLong, count, "cost");
            tarListDeals = dealRemDataforVisit(tarListDeals, visitCount, count, "visit");
            tarListDeals = dealRemDataforVisit(tarListDeals, cusVisitAmount, count, "cusVisit");
            tarListDeals = dealDivMoneyData(tarListDeals, cashCountLong, count, "cash");
            tarListDeals = dealDivMoneyData(tarListDeals, billCountLong, count, "bill");

        } else if (bill && !(cash) && !(cost) && !(cusVisit) && !(visit)) {
            tarListDeals = dealRemMoneyData(fullTargets, costCountLong, count, "cost");
            tarListDeals = dealRemDataforVisit(tarListDeals, visitCount, count, "visit");
            tarListDeals = dealRemDataforVisit(tarListDeals, cusVisitAmount, count, "cusVisit");
            tarListDeals = dealRemMoneyData(tarListDeals, cashCountLong, count, "cash");
            tarListDeals = dealDivMoneyData(tarListDeals, billCountLong, count, "bill");

        } else {
            tarListDeals = dealRemMoneyData(fullTargets, costCountLong, count, "cost");
            tarListDeals = dealRemDataforVisit(tarListDeals, visitCount, count, "visit");
            tarListDeals = dealRemDataforVisit(tarListDeals, cusVisitAmount, count, "cusVisit");
            tarListDeals = dealRemMoneyData(tarListDeals, cashCountLong, count, "cash");
            tarListDeals = dealRemMoneyData(tarListDeals, billCountLong, count, "bill");
        }
        return tarListDeals;
    }

    /**
     * ����Ǯ�����ݣ��������
     * 
     * @param fullTargets
     * @param moneyCount
     * @param count
     * @param flag
     * @return
     * @author wangzhen
     */
    private static List<TargetTemplate> dealRemMoneyData(List<TargetTemplate> fullTargets,
            long moneyCount, int count, String flag) {
        long rem = moneyCount % count;
        long div = moneyCount / count;
        long avg = div;

        List<TargetTemplate> newTargets = new ArrayList<TargetTemplate>();

        for (TargetTemplate tt : fullTargets) {
            if (rem > 0) {
                div = div + 1;
                rem--;
            }
            if ("cash".equals(flag)) {
                tt.setCashAmount(divide(div, 100));
            }
            if ("cost".equals(flag)) {
                tt.setCostAmount(divide(div, 100));
            }
            if ("bill".equals(flag)) {
                tt.setBillAmount(divide(div, 100));
            }
            div = avg;
            newTargets.add(tt);
        }
        return newTargets;
    }

    /**
     * ����Ǯ�����ݣ��������
     * 
     * @param fullTargets
     * @param moneyCount
     * @param count
     * @param flag
     * @return
     * @author wangzhen
     */
    private static List<TargetTemplate> dealDivMoneyData(List<TargetTemplate> fullTargets,
            long moneyCount, int count, String flag) {
        long div = moneyCount / count;

        List<TargetTemplate> newTargets = new ArrayList<TargetTemplate>();

        for (TargetTemplate tt : fullTargets) {
            if ("cash".equals(flag)) {
                tt.setCashAmount(divide(div, 100));
            }
            if ("cost".equals(flag)) {
                tt.setCostAmount(divide(div, 100));
            }
            if ("bill".equals(flag)) {
                tt.setBillAmount(divide(div, 100));
            }
            newTargets.add(tt);
        }
        return newTargets;
    }

    /**
     * ��ҵƽ������ָ���������������һ������
     * 
     * @param fromTargets �� Ŀ��ֵ
     * @param visitCount �ݷÿͻ���
     * @param moneyCount ǩ���������ö��
     * @param count ��ҵ�µ�����Ա������
     * @return
     * @author wangzhen
     */
    private static List<TargetTemplate> dealRemDataforVisit(List<TargetTemplate> fromTargets,
            int visitCount, int count, String flag) {
        //��Ϊ���ݿ��аݷü�¼��int�ͣ���������ֲ�ͬ�Ĵ���
        long visitRem = visitCount % count;
        int div = visitCount / count;
        int avg = div;
        //���������䵽Ա����ָ���ֱ��������ϡ�
        List<TargetTemplate> newTargets = new ArrayList<TargetTemplate>();
        for (TargetTemplate tt : fromTargets) {
            if (visitRem > 0) {
                div = div + 1;
                visitRem--;
            }
            if ("visit".equals(flag)) {
                tt.setVisitAmount(div);
            }
            if ("cusVisit".equals(flag)) {
                tt.setCusVisitAmount(div);
            }
            div = avg;
            newTargets.add(tt);
        }

        return newTargets;
    }

    /**
     * �Խ����������
     * 
     * @param money
     * @return
     * @author wangzhen
     */
    private static long dealMoneyIntegers(Double money) {
        double moneyDouble = Math.round(money * 100);
        long moneyIntegers = new Double(moneyDouble).longValue();
        return moneyIntegers;
    }

    /**
     * ��ҵָ�꣺����ƽ���ķ��䵽����ָ����
     * 
     * @param fromTargets
     * @param avgBillCount
     * @param avgCashCount
     * @param avgCostCount
     * @param avgVisitCount
     * @return
     * @author wangzhen
     */
    private static List<TargetTemplate> dealAllDiv(List<TargetTemplate> fromTargets,
            long avgBillCount, long avgCashCount, long avgCostCount, int avgVisitCount,
            int avgCusVisitCount) {

        List<TargetTemplate> toTargets = new ArrayList<TargetTemplate>();
        //modify by wangzhen start 2012-09-13 �����ݲ������������룬ȷ��������ȷ��
        double costCount = divide(avgCostCount, 100l);
        double cashCount = divide(avgCashCount, 100l);
        double billCount = divide(avgBillCount, 100l);
        //modify by wangzhen end 2012-09-13 �����ݲ������������룬ȷ��������ȷ��
        for (TargetTemplate t : fromTargets) {
            t.setBillAmount(billCount);
            t.setCashAmount(cashCount);
            t.setCostAmount(costCount);
            t.setVisitAmount(avgVisitCount);
            t.setCusVisitAmount(avgCusVisitCount);
            toTargets.add(t);
        }
        return toTargets;
    }

    /**
     * ��������
     * 
     * @param data
     * @return
     */
    private static Double divide(long data, long div) {
        BigDecimal b1 = new BigDecimal(Long.toString(data));
        //���ø÷���֮ǰ�������ǳ���100֮��ȡ�õ����ݣ���˳���100
        BigDecimal b2 = new BigDecimal(Long.toString(div));
        //2�Ǳ�����С��λ����BigDecimal.ROUND_HALF_EVEN ������ģʽ
        Double d = b1.divide(b2, 2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        return d;
    }

    private static Long getGroupId(TTerminal terminal) {
        Terminal termCached = Constants.TERMINAL_CACHE.get(terminal.getDeviceId());
        if (termCached == null) {
            /** ���ն���Ϣ���浽memcache��* */
            termCached = new Terminal();
            BeanUtils.copyProperties(terminal, termCached);
            Set<RefTermGroup> refTermGroups = terminal.getRefTermGroups();
            RefTermGroup refTermGroup = refTermGroups.iterator().next();
            termCached.setGroupId(refTermGroup.getTTermGroup().getId());
            Constants.TERMINAL_CACHE.set(terminal.getDeviceId(), termCached,
                    10 * Memcache.EXPIRE_DAY);// �ն�ʱ�䵽��,oracle_job���޸��ն˱�ʶΪ�����ڡ�,��˻���10��
        }
        if (termCached != null) {
            return termCached.getGroupId();
        } else {
            return null;
        }
    }

    public static Terminal getTerminal(String deviceId, TargetService targetService) {
        Terminal termCached = Constants.TERMINAL_CACHE.get(deviceId);
        if (termCached == null) {
            TTerminal terminal = targetService.getTerminal(deviceId);
            if (terminal != null) {
                /** ���ն���Ϣ���浽memcache��* */
                termCached = new Terminal();
                BeanUtils.copyProperties(terminal, termCached);
                Set<RefTermGroup> refTermGroups = terminal.getRefTermGroups();
                RefTermGroup refTermGroup = refTermGroups.iterator().next();
                termCached.setGroupId(refTermGroup.getTTermGroup().getId());
                Constants.TERMINAL_CACHE.set(terminal.getDeviceId(), termCached,
                        10 * Memcache.EXPIRE_DAY);// �ն�ʱ�䵽��,oracle_job���޸��ն˱�ʶΪ�����ڡ�,��˻���10��
            }
        }
        return termCached;
    }

    /**
     * ��ȡ������(��������ϵ),��:����1-����2-����3
     * 
     * @param groupId
     * @param targetService
     * @return
     */
    public static String getGroupFullName(Long groupId, TargetService targetService) {
        if (groupId == null) return "";
        Group groupCached = Constants.GROUP_CACHE.get(String.valueOf(groupId));
        if (groupCached == null) {
            TTermGroup tg = targetService.getTermGroup(groupId);
            if (tg != null) {
                groupCached = new Group();
                BeanUtils.copyProperties(tg, groupCached);
                Constants.GROUP_CACHE.set(String.valueOf(groupId), groupCached,
                        10 * Memcache.EXPIRE_DAY);// ����ҵ�ն˱���һ��,����10��
            }
        }
        if (groupCached == null) {
            return "";
        } else if (groupCached.getParentId() != null && groupCached.getParentId() != -1L) {
            return getGroupFullName(groupCached.getParentId(), targetService) + "-"
                    + groupCached.getGroupName();
        } else {
            return groupCached.getGroupName();
        }
    }

    private static CellStyle getDefaultCellStyle(Workbook wb) {
        CellStyle cs = wb.createCellStyle();
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cs.setWrapText(false);
        return cs;
    }

    private static CellStyle getHeaderCellStyle(Workbook wb) {
        CellStyle cs = getDefaultCellStyle(wb);
        Font font = wb.createFont();
        // ���������СΪ24
        font.setFontHeightInPoints((short) 11);
        // ����������ʽΪ��������
        font.setFontName("����");
        // �Ӵ�
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // б��
        // font.setItalic(true);
        // ���ɾ����
        // font.setStrikeout(true);
        // ��������ӵ���ʽ��
        cs.setFont(font);
        return cs;
    }

    private static CellStyle getTipCellStyle(Workbook wb) {
        CellStyle cs = wb.createCellStyle();
        Font font = wb.createFont();
        // ���������СΪ24
        font.setFontHeightInPoints((short) 9);
        // ����������ʽΪ��������
        font.setFontName("����");
        // �Ӵ�
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // ��ɫ
        font.setColor(Font.COLOR_RED);
        // б��
        // font.setItalic(true);
        // ���ɾ����
        // font.setStrikeout(true);
        // ��������ӵ���ʽ��
        cs.setFont(font);
        return cs;
    }

    private static Cell addCellStyle(Sheet sheet, int startrownum, int endrownum,
            int startcolumenum, int endcolumenum, CellStyle cs, boolean create) {
        if (sheet == null) {
            throw new IllegalArgumentException("parameter 'sheet' cann't be null!");
        }
        Row row = null;
        Cell cell = null;

        if (startrownum != endrownum || startcolumenum != endcolumenum) {
            for (int i = startrownum; i <= endrownum; i++) {
                if (create) row = sheet.createRow(i);
                else row = sheet.getRow(i);
                for (int j = startcolumenum; j <= endcolumenum; j++) {
                    cell = row.createCell(j);
                    if (cs != null) cell.setCellStyle(cs);
                }
            }

            sheet.addMergedRegion(new CellRangeAddress(startrownum, endrownum, startcolumenum,
                    endcolumenum));

            row = sheet.getRow(startrownum);
            cell = row.getCell(startcolumenum);
        } else {
            if (create) row = sheet.createRow(startrownum);
            else row = sheet.getRow(startrownum);
            cell = row.createCell(startcolumenum);

            if (cs != null) cell.setCellStyle(cs);
        }
        return cell;
    }

    private static void addCell(Sheet sheet, int startrownum, int endrownum, int startcolumenum,
            int endcolumenum, String value, CellStyle cs, boolean create) {
        Cell cell = addCellStyle(sheet, startrownum, endrownum, startcolumenum, endcolumenum, cs,
                create);
        cell.setCellValue(value);
    }

    private static void addCell(Sheet sheet, int startrownum, int endrownum, int startcolumenum,
            int endcolumenum, int value, CellStyle cs, boolean create) {
        Cell cell = addCellStyle(sheet, startrownum, endrownum, startcolumenum, endcolumenum, cs,
                create);
        cell.setCellValue(value);
    }

    private static void addCell(Sheet sheet, int startrownum, int endrownum, int startcolumenum,
            int endcolumenum, double value, CellStyle cs, boolean create) {
        Cell cell = addCellStyle(sheet, startrownum, endrownum, startcolumenum, endcolumenum, cs,
                create);
        cell.setCellValue(value);
    }

    public static void main(String[] args) throws Exception {
        /*
         * TargetUtils ta = new TargetUtils(); String file =
         * "E:\\downloadbz.xlsx"; TTerminal terminal = new TTerminal();
         * terminal.setDeviceId("123"); terminal.setVehicleNumber("1");
         * terminal.setSimcard("135"); TTermGroup group = new TTermGroup();
         * group.setGroupName("test"); Object[] objs = { terminal, group }; List<Object[]>
         * objList = new ArrayList<Object[]>(); objList.add(objs);
         * 
         * List<Map<String, Object>> dateList =
         * DateUtils.getWeekTemplate(2012);
         * 
         * TargetTemplate kt = new TargetTemplate(); kt.setBillAmount(1.2);
         * kt.setCashAmount(2.2); kt.setCostAmount(3.3); kt.setVisitAmount(2);
         * Map<String, TargetTemplate> templateDataMap = new HashMap<String,
         * TargetTemplate>(); templateDataMap.put("123_1", kt); Workbook
         * workbook = new XSSFWorkbook(file); List<TargetTemplate> a =
         * ta.convertTargetFromExcelToObject(workbook, dateList, 0, 2012);
         * System.out.println("over!");
         */
        // FileOutputStream os = new FileOutputStream(file);
        // workbook.write(os);
        // os.close();
        int i = 121;
        int j = i / 12;
        int x = i % 12;
        System.out.println(j);
        System.out.println(x);

    }

    public static String getHisChartByTargetType(int targetTemplateType_, List<Object[]> object_,
            String key) {
        if (object_ != null && object_.size() > 0) {
            if (targetTemplateType_ == 0) {
                return getWeekHisChartJson(object_, key);
            } else if (targetTemplateType_ == 1) {
                return getXunHisChartJson(object_, key);
            } else if (targetTemplateType_ == 2) {
                return getMonthHisChartJson(object_, key);
            }
        }
        return key + ":[]";
    }

    /**
     * Ѯģ�����ʷ����ͼjson����
     * 
     * @param object_
     * @param key
     * @return
     */
    public static String getXunHisChartJson(List<Object[]> object_, String key) {
        Iterator<Object[]> it = object_.iterator();
        String startTime_ = "";
        String endTime_ = "";
        int lastMonth = 0;
        int lastDay = 0;
        Double value_ = 0D;
        StringBuffer json = new StringBuffer();
        while (it.hasNext()) {
            Object[] objects_ = it.next();
            String[] dates = ((String) objects_[1]).split("-");
            int month = Integer.parseInt(dates[1]);
            int date = Integer.parseInt(dates[2]);
            lastDay = date;
            if (lastMonth > 0 && lastMonth != month) {
                json.append("{value:" + value_ + ", date:'" + startTime_ + "~" + endTime_ + "'},");
                value_ = 0D;
                lastMonth = 0;
            }
            if (objects_[0] != null) value_ += ((BigDecimal) objects_[0]).doubleValue();
            if (date == 1 || date == 11 || date == 21) {
                startTime_ = (String) objects_[1];
            } else if (date == 10 || date == 20) {
                endTime_ = (String) objects_[1];
                json.append("{value:" + value_ + ", date:'" + startTime_ + "~" + endTime_ + "'},");
                value_ = 0D;
                lastMonth = 0;
            } else if (date > 21) {
                endTime_ = (String) objects_[1];
                lastMonth = month;
            }

        }
        if (!(lastDay == 10 || lastDay == 20)) {
            json.append("{value:" + value_ + ", date:'" + startTime_ + "~" + endTime_ + "'},");
        }
        if (json.length() > 0) {
            json.deleteCharAt(json.length() - 1);
        }

        return key + ":[" + json.toString() + "]";
    }

    /**
     * ��ģ�����ʷ����ͼjson����
     * 
     * @param object_
     * @param key
     * @return
     */
    public static String getWeekHisChartJson(List<Object[]> object_, String key) {
        Iterator<Object[]> it = object_.iterator();
        String startTime_ = "";
        String endTime_ = "";
        Double value_ = 0D;
        int i = 0;
        StringBuffer json = new StringBuffer();
        while (it.hasNext()) {
            Object[] objects_ = it.next();
            if (i == 0) {
                startTime_ = (String) objects_[1];
            } else if (i == 6) {
                endTime_ = (String) objects_[1];
                json.append("{value:" + value_ + ", date:'" + startTime_ + "~" + endTime_ + "'},");
                value_ = 0D;
                i = 0;
                continue;
            }
            if (objects_[0] != null) value_ += ((BigDecimal) objects_[0]).doubleValue();
            i++;
        }
        if (json.length() > 0) {
            json.deleteCharAt(json.length() - 1);
        }

        return key + ":[" + json.toString() + "]";
    }

    /**
     * ��ģ�����ʷ����ͼjson����
     * 
     * @param object_
     * @param key
     * @return
     */
    public static String getMonthHisChartJson(List<Object[]> object_, String key) {
        Iterator<Object[]> it = object_.iterator();
        int lastMonth = 0;
        int lastYear = 0;
        Double value_ = 0D;
        StringBuffer json = new StringBuffer();
        while (it.hasNext()) {
            Object[] objects_ = it.next();
            String[] dates = ((String) objects_[1]).split("-");
            int month = Integer.parseInt(dates[1]);
            if (lastMonth == 0) {
                lastMonth = month;
                lastYear = Integer.parseInt(dates[0]);
                if (objects_[0] != null) value_ += ((BigDecimal) objects_[0]).doubleValue();
                continue;
            }
            if (lastMonth == month) {
                if (objects_[0] != null) value_ += ((BigDecimal) objects_[0]).doubleValue();
                lastMonth = month;
                lastYear = Integer.parseInt(dates[0]);
                continue;
            }
            json.append("{value:" + value_ + ", date:'" + lastYear + '��' + lastMonth + "��'},");
            value_ = 0D;
            if (objects_[0] != null) value_ = ((BigDecimal) objects_[0]).doubleValue();
            lastMonth = month;
            lastYear = Integer.parseInt(dates[0]);
        }
        json.append("{value:" + value_ + ", date:'" + lastYear + '��' + lastMonth + "��'},");
        if (json.length() > 0) {
            json.deleteCharAt(json.length() - 1);
        }

        return key + ":[" + json.toString() + "]";
    }

    public static String getHisChartByTargetTypeCost(int targetTemplateType_,
            List<Object[]> object_, String key) {
        if (object_ != null && object_.size() > 0) {
            if (targetTemplateType_ == 0) {
                return getWeekHisChartJsonCost(object_, key);
            } else if (targetTemplateType_ == 1) {
                return getXunHisChartJsonCost(object_, key);
            } else if (targetTemplateType_ == 2) {
                return getMonthHisChartJsonCost(object_, key);
            }
        }
        return key + ":[]";
    }

    /**
     * Ѯģ�����ʷ����ͼjson����
     * 
     * @param object_
     * @param key
     * @return
     */
    public static String getXunHisChartJsonCost(List<Object[]> object_, String key) {
        Iterator<Object[]> it = object_.iterator();
        String startTime_ = "";
        String endTime_ = "";
        int lastMonth = 0;
        int lastDay = 0;
        String value_ = "0,0,0,0,0,0";
        StringBuffer json = new StringBuffer();
        while (it.hasNext()) {
            Object[] objects_ = it.next();
            String[] dates = ((String) objects_[1]).split("-");
            int month = Integer.parseInt(dates[1]);
            int date = Integer.parseInt(dates[2]);
            lastDay = date;
            if (lastMonth > 0 && lastMonth != month) {
                json.append("{value:'" + value_ + "', date:'" + startTime_ + "~" + endTime_ + "'},");
                value_ = "0,0,0,0,0,0";
                lastMonth = 0;
            }
            if (objects_[0] != null) {
                String[] thisValues_ = ((String) objects_[0]).split(",");
                String[] lastValues_ = value_.split(",");
                if (thisValues_.length == 6 && lastValues_.length == 6) {
                    Double tmpvalue1 = Double.parseDouble(thisValues_[0])
                            + Double.parseDouble(lastValues_[0]);
                    Double tmpvalue2 = Double.parseDouble(thisValues_[1])
                            + Double.parseDouble(lastValues_[1]);
                    Double tmpvalue3 = Double.parseDouble(thisValues_[2])
                            + Double.parseDouble(lastValues_[2]);
                    Double tmpvalue4 = Double.parseDouble(thisValues_[3])
                            + Double.parseDouble(lastValues_[3]);
                    Double tmpvalue5 = Double.parseDouble(thisValues_[4])
                            + Double.parseDouble(lastValues_[4]);
                    Double tmpvalue6 = Double.parseDouble(thisValues_[5])
                            + Double.parseDouble(lastValues_[5]);
                    value_ = tmpvalue1 + "," + tmpvalue2 + "," + tmpvalue3 + "," + tmpvalue4 + ","
                            + tmpvalue5 + "," + tmpvalue6;
                } else if (thisValues_.length == 6) {
                    value_ = thisValues_[0] + "," + thisValues_[1] + "," + thisValues_[2] + ","
                            + thisValues_[3] + "," + thisValues_[4] + "," + thisValues_[5];
                }
            }
            if (date == 1 || date == 11 || date == 21) {
                startTime_ = (String) objects_[1];
            } else if (date == 10 || date == 20) {
                endTime_ = (String) objects_[1];
                json.append("{value:'" + value_ + "', date:'" + startTime_ + "~" + endTime_ + "'},");
                value_ = "0,0,0,0,0,0";
                lastMonth = 0;
            } else if (date > 21) {
                endTime_ = (String) objects_[1];
                lastMonth = month;
            }

        }
        if (!(lastDay == 10 || lastDay == 20)) {
            json.append("{value:'" + value_ + "', date:'" + startTime_ + "~" + endTime_ + "'},");
        }
        if (json.length() > 0) {
            json.deleteCharAt(json.length() - 1);
        }

        return key + ":[" + json.toString() + "]";
    }

    /**
     * ��ģ�����ʷ����ͼjson����
     * 
     * @param object_
     * @param key
     * @return
     */
    public static String getWeekHisChartJsonCost(List<Object[]> object_, String key) {
        Iterator<Object[]> it = object_.iterator();
        String startTime_ = "";
        String endTime_ = "";
        String value_ = "0,0,0,0,0,0";
        int i = 0;
        StringBuffer json = new StringBuffer();
        while (it.hasNext()) {
            Object[] objects_ = it.next();
            if (i == 0) {
                startTime_ = (String) objects_[1];
            } else if (i == 6) {
                endTime_ = (String) objects_[1];
                json.append("{value:'" + value_ + "', date:'" + startTime_ + "~" + endTime_ + "'},");
                value_ = "0,0,0,0,0,0";
                i = 0;
                continue;
            }
            if (objects_[0] != null) {
                String[] thisValues_ = ((String) objects_[0]).split(",");
                String[] lastValues_ = value_.split(",");
                if (thisValues_.length == 6 && lastValues_.length == 6) {
                    Double tmpvalue1 = Double.parseDouble(thisValues_[0])
                            + Double.parseDouble(lastValues_[0]);
                    Double tmpvalue2 = Double.parseDouble(thisValues_[1])
                            + Double.parseDouble(lastValues_[1]);
                    Double tmpvalue3 = Double.parseDouble(thisValues_[2])
                            + Double.parseDouble(lastValues_[2]);
                    Double tmpvalue4 = Double.parseDouble(thisValues_[3])
                            + Double.parseDouble(lastValues_[3]);
                    Double tmpvalue5 = Double.parseDouble(thisValues_[4])
                            + Double.parseDouble(lastValues_[4]);
                    Double tmpvalue6 = Double.parseDouble(thisValues_[5])
                            + Double.parseDouble(lastValues_[5]);
                    value_ = tmpvalue1 + "," + tmpvalue2 + "," + tmpvalue3 + "," + tmpvalue4 + ","
                            + tmpvalue5 + "," + tmpvalue6;
                } else if (thisValues_.length == 6) {
                    value_ = thisValues_[0] + "," + thisValues_[1] + "," + thisValues_[2] + ","
                            + thisValues_[3] + "," + thisValues_[4] + "," + thisValues_[5];
                }
            }
            i++;
        }
        if (json.length() > 0) {
            json.deleteCharAt(json.length() - 1);
        }

        return key + ":[" + json.toString() + "]";
    }

    /**
     * ��ģ�����ʷ����ͼjson����
     * 
     * @param object_
     * @param key
     * @return
     */
    public static String getMonthHisChartJsonCost(List<Object[]> object_, String key) {
        Iterator<Object[]> it = object_.iterator();
        int lastMonth = 0;
        int lastYear = 0;
        String value_ = "0,0,0,0,0,0";
        StringBuffer json = new StringBuffer();
        while (it.hasNext()) {
            Object[] objects_ = it.next();
            String[] dates = ((String) objects_[1]).split("-");
            int month = Integer.parseInt(dates[1]);
            if (lastMonth == 0) {
                lastMonth = month;
                lastYear = Integer.parseInt(dates[0]);
                continue;
            }
            if (lastMonth == month) {
                if (objects_[0] != null) {
                    String[] thisValues_ = ((String) objects_[0]).split(",");
                    String[] lastValues_ = value_.split(",");
                    if (thisValues_.length == 6 && lastValues_.length == 6) {
                        Double tmpvalue1 = Double.parseDouble(thisValues_[0])
                                + Double.parseDouble(lastValues_[0]);
                        Double tmpvalue2 = Double.parseDouble(thisValues_[1])
                                + Double.parseDouble(lastValues_[1]);
                        Double tmpvalue3 = Double.parseDouble(thisValues_[2])
                                + Double.parseDouble(lastValues_[2]);
                        Double tmpvalue4 = Double.parseDouble(thisValues_[3])
                                + Double.parseDouble(lastValues_[3]);
                        Double tmpvalue5 = Double.parseDouble(thisValues_[4])
                                + Double.parseDouble(lastValues_[4]);
                        Double tmpvalue6 = Double.parseDouble(thisValues_[5])
                                + Double.parseDouble(lastValues_[5]);
                        value_ = tmpvalue1 + "," + tmpvalue2 + "," + tmpvalue3 + "," + tmpvalue4
                                + "," + tmpvalue5 + "," + tmpvalue6;
                    } else if (thisValues_.length == 6) {
                        value_ = thisValues_[0] + "," + thisValues_[1] + "," + thisValues_[2] + ","
                                + thisValues_[3] + "," + thisValues_[4] + "," + thisValues_[5];
                    }
                }
                lastMonth = month;
                lastYear = Integer.parseInt(dates[0]);
                continue;
            }
            json.append("{value:'" + value_ + "', date:'" + lastYear + '��' + lastMonth + "��'},");
            value_ = "0,0,0,0,0,0";
            lastMonth = month;
            lastYear = Integer.parseInt(dates[0]);
        }
        json.append("{value:'" + value_ + "', date:'" + lastYear + '��' + lastMonth + "��'},");
        if (json.length() > 0) {
            json.deleteCharAt(json.length() - 1);
        }
        return key + ":[" + json.toString() + "]";
    }

    public static String getHisChartTitle(Long startTime, Long endTime, int targetTemplateType,
            String title, String key) {
        if (targetTemplateType == 0 || targetTemplateType == 1) {
            return key + ":'" + title + "(" + DateUtils.dateTimeToStr(startTime, "yyyy��MM��dd��")
                    + "~" + DateUtils.dateTimeToStr(endTime, "yyyy��MM��dd��") + ")'";
        } else if (targetTemplateType == 2) {
            return key + ":'" + title + "(" + DateUtils.dateTimeToStr(startTime, "yyyy��MM��") + "~"
                    + DateUtils.dateTimeToStr(endTime, "yyyy��MM��") + ")'";
        }

        return key + ":" + title;
    }

    /**
     * ��ת��ҵ��ָ����Ϣ
     * 
     * @param tterminals ĳ����ҵ�µ�����Ա��
     * @return
     * @author wangzhen
     */
    public static List<TargetTemplate> getTargetTemplate(List<TTerminal> tterminals) {

        List<TargetTemplate> tt = new ArrayList<TargetTemplate>();
        TargetService targetService = (TargetService) SpringHelper.getBean("targetService");

        for (TTerminal tte : tterminals) {
            TargetTemplate target = new TargetTemplate();
            target.setDeviceId(tte.getDeviceId());
            target.setStates(0);
            Long groupId = getGroupId(tte);
            target.setGroupId(groupId);
            target.setGroupName(getGroupFullName(groupId, targetService));
            //modify by wangzhen
            //			target.setVehicleNumber(tte.getVehicleNumber());
            //target.setVehicleNumber(tte.getTermName());
            target.setTerminalName(tte.getTermName());
            tt.add(target);
        }
        return tt;
    }

    /**
     * ������ҵƽ�����˵�ָ����Ϣ
     * 
     * @param entCode
     * @param type
     * @param year
     * @param targetOn
     * @param tts
     * @return
     */
    public static List<TargetTemplate> fillTarget(String entCode, Integer type, Integer year,
            Integer targetOn, List<TargetTemplate> tts) {
        List<TargetTemplate> fullTargets = new ArrayList<TargetTemplate>();

        for (TargetTemplate tt : tts) {
            tt.setType(type);
            tt.setYear(year);
            tt.setEntCode(entCode);
            tt.setTargetOn(targetOn);
            fullTargets.add(tt);
        }
        return fullTargets;
    }
}
