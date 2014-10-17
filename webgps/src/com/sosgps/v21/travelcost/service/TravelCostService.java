package com.sosgps.v21.travelcost.service;

import java.util.List;
import java.util.Map;

import com.sosgps.v21.model.TravelCost;

/**
 * 差旅信息服务接口
 * @author Administrator
 *
 */
public interface TravelCostService {

    public List<TravelCost> queryTravelCostInfoByCondition(Map<String, Object> paramMap, String entCode);
    
    /**
     * 列出差旅详细信息:分页
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
     * 审核差旅信息
     * @param ids
     * @param entCode
     * @return
     */
    public String verifyTravelCost(String ids,String entCode,String flag);
     
    /**
     * 通过终端ID和出差标示来找出该员工的旅行信息
     * @param deviceId
     * @param entCode
     * @param flag
     */
    public TravelCost getTravelCost(String deviceId, String entCode, int flag);
    /**
     * 列出差旅详细信息:不分页
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
     * 得到差旅信息总数根据所查询的条件
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
     * 通过终端和企业获取差旅信息
     * @param deviceId
     * @param entCode
     * @return
     */
    public TravelCost getTravelCost(String deviceId, String entCode);
}
