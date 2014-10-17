/**
 * 
 */
package com.sosgps.wzt.group.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

 
import com.sosgps.wzt.group.dao.GroupDAO;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTermGroupDAO;

/**
 * @author shiguang.zhou
 *
 */
public class GroupDAOImpl extends TTermGroupDAO implements GroupDAO {

	/**
	 * 获得组结构
	 */
	public List getTTargetGroup(String empCode) {
		List list  =  new ArrayList();
		try{
			list  = this.getHibernateTemplate().find(
				"from TTermGroup  t  where t.TEnt.entCode='"+empCode+"' order by t.parentId,t.groupSort,t.id ");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}
	/**
	 * 更新组结构
	 */
	public void update(TTermGroup tTargetGroup) {
		this.getHibernateTemplate().update(tTargetGroup);
	}
	/**
	 * 获得子组/部门
	 */
	public List findChild(Long parentId){
		return this.getHibernateTemplate().find(" from TTermGroup t where t.parentId="+parentId);
	}
	/**
	 * 按同组级别倒序排列返回子组/部门
	 */
	public List findChildLastSortById(Long groupId){
		List list=this.findByParent(groupId);//this.getHibernateTemplate().find("from TTermGroup t where t.parentId="+groupId+" order by t.groupSort desc ");
		return 	list;
	}
	/**
	 * 根据父id找到TTargetGroup
	 */
	public TTermGroup findByParentId(Long parentId){
		List l=this.getHibernateTemplate().find("from TTermGroup t where t.parentId="+parentId);
		
		return (TTermGroup)l.get(0);
	}
	public List findRefTermGroupById(final Long groupId) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				String hql = "select t from TTerminal t "
						+ "left join t.refTermGroups ref "
						+ "left join ref.TTermGroup g " + "where g.id=:groupId";
				Query query = session.createQuery(hql);
				query.setParameter("groupId", groupId);
				return query.list();
			}
		});
	}
}
