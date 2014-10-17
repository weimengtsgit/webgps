package com.sosgps.wzt.stat.dao.hibernate;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.orm.TReportFilepathDAO;
/**
 * ��ϸ�����ļ�dao�ӿ�hibernateʵ����
 * @author magiejue
 * @add by zhumingzhe 2010-09-15
 */
public class TReportFilepathHibernateDao extends TReportFilepathDAO implements com.sosgps.wzt.stat.dao.TReportFilepathDao  {
	//����ļ�·������
	public List getReportFilepath(String deviceIds,String endCode,Date startDate,Date endDate) {
		// TODO Auto-generated method stub
    	String hql="select tr.filePath from TReportFilepath tr "
			+"where tr.deviceId in ("+deviceIds+") and tr.entCode=? "
			+"and tr.reportDate between ? and ? "
			+"order by tr.deviceId, tr.reportDate";
        return getHibernateTemplate().find(hql,new Object[]{endCode,startDate,endDate}); 
        
	}

}
