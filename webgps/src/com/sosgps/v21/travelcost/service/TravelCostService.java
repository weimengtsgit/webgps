package com.sosgps.v21.travelcost.service;

import java.util.List;
import java.util.Map;

import com.sosgps.v21.model.TravelCost;

/**
 * ������Ϣ����ӿ�
 * @author Administrator
 *
 */
public interface TravelCostService {

    public List<TravelCost> queryTravelCostInfoByCondition(Map<String, Object> paramMap, String entCode);
    
    /**
     * �г�������ϸ��Ϣ:��ҳ
     * @param entCode
     * @param pageNo
     * @param pageSize
     * @param startDate
     * @param endDate
     * @param valueOf
     * @param deviceIds
     * @return
     */
    public List<TravelCost> listTravelDetails(String entCode, int start, int limit, String startDate,
            String endDate, String reviewStates, String deviceIds);
    /**
     * ��˲�����Ϣ
     * @param ids
     * @param entCode
     * @return
     */
    public String verifyTravelCost(String ids,String entCode,String flag);
     
    /**
     * ͨ���ն�ID�ͳ����ʾ���ҳ���Ա����������Ϣ
     * @param deviceId
     * @param entCode
     * @param flag
     */
    public TravelCost getTravelCost(String deviceId, String entCode, int flag);
    /**
     * �г�������ϸ��Ϣ:����ҳ
     * @param entCode
     * @param st
     * @param et
     * @param revStates
     * @param deviceIds
     * @return
     */
    public List<TravelCost> listTravelCost(String entCode, String st, String et, String revStates,
            String deviceIds);

    /**
     * �õ�������Ϣ������������ѯ������
     * @param entCode
     * @param st
     * @param et
     * @param revStates
     * @param deviceIds
     * @return
     */
    public int getTravelCostCount(String entCode, String st, String et,
            String revStates, String deviceIds);
    
    public TravelCost getTravelCostById(Long id);
    
    /**
     * ͨ���ն˺���ҵ��ȡ������Ϣ
     * @param deviceId
     * @param entCode
     * @return
     */
    public TravelCost getTravelCost(String deviceId, String entCode);
}
