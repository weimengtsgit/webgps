package com.sosgps.wzt.locate.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TTerminal;

public interface LocateService {

	public String  findLastLoc(String deviceIds);
	public String  findLastLoc(String deviceIds,boolean isEncypt);
	/**
	 * t_locrecord中查找没有偏转及获取描述的数据并获取位置描述及偏转数据
	 * @return
	 */
	public void updateNoEncryptionAndNoLocDesc();
	/**
	 * 坐标为偏转坐标
	 * @param deviceIds
	 * @param isRoadConvert 是否启用道路纠偏  add zhangwei 2009-06-02
	 * @return
	 */
	public String  findLastLocWithDeflect(String deviceIds,boolean isRoadConvert);
	public String queryClosestLocSelectedTerm(String entCode, String gpsDeviceIds,String lbsDeviceIds, String username);//add by yanglei
	/**
	 * 由坐标取得位置
	 * @param x
	 * @param y
	 * @return
	 */
	public String positionDesc(String x, String y);// add by sunjingwei 20100305
	// sos通过deviceids查最后一条
	public List queryLocByTime(String deviceIds);
	// sos通过deviceids和time查最后一条
	public List queryLocByTime(String deviceIds, String time);
	// sos轨迹回放
	public List queryLocsByTime(String deviceId, String startTime, String endTime);
	// sos轨迹回放 add by liuhongxiao 2011-12-02
	public List queryLocsByTime(TTerminal terminal, String startDate, String endDate, String startTime, String endTime);
	//sos轨迹回放车辆信息分页
	public Page<TLocrecord>  listQueryLocsByTime(String deviceId, String startTime, String endTime,int pageNo, int pageSize);
	// sos电信获得token
	// 返回字符串数组：
	// 0未授权的Request Token
	// 1未授权的Request Token对应的Request Token Secret
	// 2授权的oauth_verifier 验证码
	// 3授权的Access Token
	// 4授权的Access Token对应的Access Token Secret
	public String[] telecomGetUnauthorizedToken();
	
	// sos电信定位
	public List locTelecom(String oauth_token_access, String oauth_verifier, String oauth_token_secret_access, String deviceIds);
	
	public List queryLocsByTime2(TTerminal terminal, String startDate, String endDate,
            String startTime, String endTime);
	
	public String lastLocrecordList(String entCode, String deviceIds, String name, String locateType, int gpstime, int inputtime, int startint, int limitint);
	public void lastLocrecordListExcel(HttpServletResponse response, String entCode, String deviceIds, String name, String locateType, int gpstime, int inputtime);
	
}
