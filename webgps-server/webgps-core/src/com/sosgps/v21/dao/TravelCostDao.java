package com.sosgps.v21.dao;

import java.util.List;
import java.util.Map;

import com.sosgps.v21.model.TravelCost;
 

public interface TravelCostDao {

    List<TravelCost> queryTravelCostInfoByCondition(Map<String, Object> paramMap, String entCode);

    List<TravelCost> listTravelDetails(String entCode, int pageNo, int pageSize, String startDate,
            String endDate, String reviwStates, String deviceIds);

    public boolean verifyTravelCost(String ids,String entCode,String flag);

    TravelCost getTravelCost(String deviceId, String entCode, int flag);

    List<TravelCost> listTravelCost(String entCode, String st, String et, String revStates,
            String deviceIds);

    int getTravelCostCount(String entCode, String st, String et, String revStates, String deviceIds);

    TravelCost getTravelCostById(Long id);

    TravelCost getTravelCost(String deviceId, String entCode);
    

}
