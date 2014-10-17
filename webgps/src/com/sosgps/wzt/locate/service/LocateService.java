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
	 * t_locrecord�в���û��ƫת����ȡ���������ݲ���ȡλ��������ƫת����
	 * @return
	 */
	public void updateNoEncryptionAndNoLocDesc();
	/**
	 * ����Ϊƫת����
	 * @param deviceIds
	 * @param isRoadConvert �Ƿ����õ�·��ƫ  add zhangwei 2009-06-02
	 * @return
	 */
	public String  findLastLocWithDeflect(String deviceIds,boolean isRoadConvert);
	public String queryClosestLocSelectedTerm(String entCode, String gpsDeviceIds,String lbsDeviceIds, String username);//add by yanglei
	/**
	 * ������ȡ��λ��
	 * @param x
	 * @param y
	 * @return
	 */
	public String positionDesc(String x, String y);// add by sunjingwei 20100305
	// sosͨ��deviceids�����һ��
	public List queryLocByTime(String deviceIds);
	// sosͨ��deviceids��time�����һ��
	public List queryLocByTime(String deviceIds, String time);
	// sos�켣�ط�
	public List queryLocsByTime(String deviceId, String startTime, String endTime);
	// sos�켣�ط� add by liuhongxiao 2011-12-02
	public List queryLocsByTime(TTerminal terminal, String startDate, String endDate, String startTime, String endTime);
	//sos�켣�طų�����Ϣ��ҳ
	public Page<TLocrecord>  listQueryLocsByTime(String deviceId, String startTime, String endTime,int pageNo, int pageSize);
	// sos���Ż��token
	// �����ַ������飺
	// 0δ��Ȩ��Request Token
	// 1δ��Ȩ��Request Token��Ӧ��Request Token Secret
	// 2��Ȩ��oauth_verifier ��֤��
	// 3��Ȩ��Access Token
	// 4��Ȩ��Access Token��Ӧ��Access Token Secret
	public String[] telecomGetUnauthorizedToken();
	
	// sos���Ŷ�λ
	public List locTelecom(String oauth_token_access, String oauth_verifier, String oauth_token_secret_access, String deviceIds);
	
	public List queryLocsByTime2(TTerminal terminal, String startDate, String endDate,
            String startTime, String endTime);
	
	public String lastLocrecordList(String entCode, String deviceIds, String name, String locateType, int gpstime, int inputtime, int startint, int limitint);
	public void lastLocrecordListExcel(HttpServletResponse response, String entCode, String deviceIds, String name, String locateType, int gpstime, int inputtime);
	
}
