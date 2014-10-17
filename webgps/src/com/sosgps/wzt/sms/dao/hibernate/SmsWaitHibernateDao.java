package com.sosgps.wzt.sms.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ShandongSmsWait;
import com.sosgps.wzt.orm.ShandongSmsWaitDAO;
import com.sosgps.wzt.sms.dao.SmsWaitDao;
import com.sosgps.wzt.util.PageQuery;

/**
 * @Title:待发短信入库dao层接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2009-3-24 下午03:08:19
 */
public class SmsWaitHibernateDao extends ShandongSmsWaitDAO implements
		SmsWaitDao {
	private Logger log = Logger.getLogger(SmsWaitHibernateDao.class);

	public void saveWaitSMS(String entCode, String phone, String message) {
		try {
			ShandongSmsWait transientInstance = new ShandongSmsWait();
			transientInstance.setLongid(entCode);
			transientInstance.setDesmobile(phone);
			transientInstance.setContent(message);
			save(transientInstance);
		} catch (Exception e) {
			log.error("待发短信入库Dao异常", e);
		}
	}

	public Page<Object[]> listSendSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue) {
		String hql = "select log.logId,log.desmobile,log.content,log.createTime,t.vehicleNumber "
				+ "from ShandongLogSend log,TTerminal t "
				+ "where log.createTime>=? and log.createTime<=? "
				+ "and log.ecode=0 "// 发送给网关成功
				+ "and log.desmobile=t.simcard "
				+ "and log.desmobile in "
				+ "(select distinct ref.TTerminal.simcard "
				+ "from RefTermGroup ref "
				+ "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? "
				+ "and (ref.TTerminal.simcard like ? or ref.TTerminal.vehicleNumber like ?)) "
				+ "order by log.createTime desc";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startTime, endTime, entCode,
				userId, "%" + searchValue + "%", "%" + searchValue + "%");
	}
	//add by magiejue 2010-12-13 查询所有发送短信未分页
	public List<Object> listSendSmsLog(String entCode, Long userId,Date startTime, Date endTime,String searchValue) {
		String hql = "select log.logId,log.desmobile,log.content,log.createTime,t.vehicleNumber "
				+ "from ShandongLogSend log,TTerminal t "
				+ "where log.createTime>=? and log.createTime<=? "
				+ "and log.ecode=0 "// 发送给网关成功
				+ "and log.desmobile=t.simcard "
				+ "and log.desmobile in "
				+ "(select distinct ref.TTerminal.simcard "
				+ "from RefTermGroup ref "
				+ "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? "
				+ "and (ref.TTerminal.simcard like ? or ref.TTerminal.vehicleNumber like ?)) "
				+ "order by log.createTime desc";
		return this.getHibernateTemplate().find(hql, new Object[]{startTime,endTime, entCode,userId, "%" + searchValue + "%", "%" + searchValue + "%"});
	}
}
