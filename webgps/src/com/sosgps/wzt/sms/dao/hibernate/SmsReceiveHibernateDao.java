package com.sosgps.wzt.sms.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ShandongSmsRecvDAO;
import com.sosgps.wzt.sms.dao.SmsReceiveDao;
import com.sosgps.wzt.util.PageQuery;

/**
 * @Title:短信接收dao层接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2009-3-25 上午10:59:23
 */
public class SmsReceiveHibernateDao extends ShandongSmsRecvDAO implements
		SmsReceiveDao {
	private Logger log = Logger.getLogger(SmsReceiveHibernateDao.class);

	public Page<Object[]> listNotReadReceiveSms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue) {
		String hql = "select rec.recvId,rec.srcmobile,rec.content,rec.createTime,t.vehicleNumber "
				+ "from ShandongSmsRecv rec,TTerminal t "
				+ "where rec.createTime>=? and rec.createTime<=? "
				+ "and rec.srcmobile=t.simcard "
				+ "and rec.srcmobile in "
				+ "(select distinct ref.TTerminal.simcard "
				+ "from RefTermGroup ref "
				+ "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? "
				+ "and (ref.TTerminal.simcard like ? or ref.TTerminal.vehicleNumber like ?)) "
				+ "order by rec.createTime desc";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startTime, endTime, entCode,
				userId, "%" + searchValue + "%", "%" + searchValue + "%");
	}

	public Page<Object[]> listReceiveSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue) {
		String hql = "select log.logId,log.srcmobile,log.content,log.createTime,rec.recvId,t.vehicleNumber "
				+ "from ShandongLogRecv log left join log.shandongSmsRecv rec,TTerminal t "
				+ "where log.createTime>=? and log.createTime<=? "
				+ "and log.srcmobile=t.simcard "
				+ "and log.srcmobile in "
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
	//add by magiejue 2010-12-13 查询所有接受短信未分页
	public List<Object> listReceiveSmsLog(String entCode, Long userId,Date startTime, Date endTime,String searchValue) {
		String hql = "select log.logId,log.srcmobile,log.content,log.createTime,rec.recvId,t.vehicleNumber "
				+ "from ShandongLogRecv log left join log.shandongSmsRecv rec,TTerminal t "
				+ "where log.createTime>=? and log.createTime<=? "
				+ "and log.srcmobile=t.simcard "
				+ "and log.srcmobile in "
				+ "(select distinct ref.TTerminal.simcard "
				+ "from RefTermGroup ref "
				+ "left join ref.TTermGroup g "
				+ "left join g.refUserTgroups ref2 "
				+ "where g.TEnt.entCode=? and ref2.id.userId=? "
				+ "and (ref.TTerminal.simcard like ? or ref.TTerminal.vehicleNumber like ?)) "
				+ "order by log.createTime desc";
		 return getHibernateTemplate().find(hql,new Object[]{startTime,endTime,entCode,userId ,"%" + searchValue + "%", "%" + searchValue + "%"});
	}
}
