package com.sosgps.v21.travelcost.service.impl;

import java.util.List;
import java.util.Map;

import com.sosgps.v21.dao.TravelCostDao;
import com.sosgps.v21.model.EmployeeAttend;
import com.sosgps.v21.model.TravelCost;
import com.sosgps.v21.travelcost.service.TravelCostService;


public class TravelCostServiceImpl implements TravelCostService {

    private TravelCostDao travelCostDao;
    
    public TravelCostDao getTravelCostDao() {
        return travelCostDao;
    }
    public void setTravelCostDao(TravelCostDao travelCostDao) {
        this.travelCostDao = travelCostDao;
    }

    public List<TravelCost> queryTravelCostInfoByCondition(Map<String, Object> paramMap, String entCode) {
        return travelCostDao.queryTravelCostInfoByCondition(paramMap,entCode);
    }
    public List<TravelCost>  listTravelDetails(String entCode, int start, int limit, String startDate,
            String endDate, String reviwStates, String deviceIds) {
        return travelCostDao.listTravelDetails(entCode,start,limit,startDate,endDate,reviwStates,deviceIds);
    }
    public String verifyTravelCost(String ids,String entCode,String flag) {
        boolean isVerify = travelCostDao.verifyTravelCost(ids,entCode,flag);
        if(isVerify) {
            return "{success:true,info:\"…Û∫À≥…π¶!\"}";
        }else {
            return "{failure:true,info:\"…Û∫À ß∞‹!\"}";
        }
    }
    public TravelCost getTravelCost(String deviceId, String entCode, int flag) {
        
        return travelCostDao.getTravelCost(deviceId,entCode,flag);
    }
    public List<TravelCost> listTravelCost(String entCode, String st, String et, String revStates,
            String deviceIds) {
        return travelCostDao.listTravelCost(entCode,st,et,revStates,deviceIds);
    }
    public int getTravelCostCount(String entCode, String st, String et, String revStates,
            String deviceIds) {
        return travelCostDao.getTravelCostCount(entCode,st,et,revStates,deviceIds);
    }

    public TravelCost getTravelCostById(Long id) {
        return travelCostDao.getTravelCostById(id);
    }
    public TravelCost getTravelCost(String deviceId, String entCode) {
        return travelCostDao.getTravelCost(deviceId,entCode);
    }
    
}
