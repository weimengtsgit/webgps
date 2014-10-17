package com.sosgps.v21.dao.impl.hibernate;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sosgps.v21.dao.TargetTemplateDao;
 
public class TargetTemplateDaoImpl extends BaseHibernateDao implements
		TargetTemplateDao {

	private static final Logger logger = LoggerFactory
			.getLogger(TargetTemplateDaoImpl.class);

	/*
	 * 获得指定企业，当前(周/旬/月)目标模板值的总合 (non-Javadoc)
	 * 
	 * @see com.sosgps.v21.dao.TargetTemplateDao#getCurrentTargetTemplateCount(java.lang.String,
	 *      int, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getCurrentTargetTemplateCount(String entCode,
			int type, int year, int targetOn) {
		try {
			String hql = "select sum(t.billAmount), sum(t.cashAmount), sum(t.costAmount), sum(t.visitAmount) "
					+ "from TargetTemplate t "
					+ "where entCode = ? "
					+ "and states = 0 "
					+ "and type = ?  "
					+ "and year = ? "
					+ "and targetOn = ? ";
			return super.getHibernateTemplate().find(hql,
					new Object[] { entCode, type, year, targetOn });
		} catch (Exception e) {
			logger.error("[getCurrentTargetTemplateCount]", e);
			return null;
		}
	}
	
	/**
	 * 取得目标维护值
	 * @param entCode
	 * @param type
	 * @param year
	 * @param targetOn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getTargetTemplate(String entCode,
			int type, int year, int targetOn, String tableName) {
		try {
			String hql = "select sum("+tableName+") "
					+ "from TargetTemplate t "
					+ "where entCode = ? "
					+ "and states = 0 "
					+ "and type = ?  "
					+ "and year = ? "
					+ "and targetOn = ? ";
			return super.getHibernateTemplate().find(hql,
					new Object[] { entCode, type, year, targetOn });
		} catch (Exception e) {
			logger.error("[getCurrentTargetTemplateCount]", e);
			return null;
		}
	}
}
