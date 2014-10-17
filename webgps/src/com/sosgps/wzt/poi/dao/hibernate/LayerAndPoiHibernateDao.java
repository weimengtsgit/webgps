package com.sosgps.wzt.poi.dao.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefLayerPoi;
import com.sosgps.wzt.orm.RefTermPoi;
import com.sosgps.wzt.orm.RefUserLayer;
import com.sosgps.wzt.orm.TLayers;
import com.sosgps.wzt.orm.TLayersDao;
import com.sosgps.wzt.orm.TPoi;
import com.sosgps.wzt.poi.dao.LayerAndPoiDao;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.PageQuery;
import com.sosgps.wzt.util.StringUtility;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 * @Title:poi数据层接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-10 下午03:38:17
 */
public class LayerAndPoiHibernateDao extends TLayersDao implements
		LayerAndPoiDao {

	private static final Logger logger = Logger
			.getLogger(LayerAndPoiHibernateDao.class);

	public Page<TLayers> listLayerCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue) {
		// String hql = "select l from TLayers l where l.useStatus=1 " +
		// "and l.entcode=? and l.userId=? and l.layerName like ?";
		// 查询states为0或null的图层
		String hql = "select l from TLayers l where l.useStatus=1 "
				+ "and l.entcode=? and l.userId=? and l.layerName like ? and (l.states = 0 or l.states is null)";
		PageQuery<TLayers> pageQuery = new PageQuery<TLayers>(this);
		Page<TLayers> page = new Page<TLayers>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, userId, "%"
				+ searchValue + "%");
	}

	public TLayers queryLayer(final Long layerId) {
		return (TLayers) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = "select l from TLayers l "
								+ "left join fetch l.refUserLayers ref "
								+ "where l.useStatus=1 and l.id=:layerId and (l.states = 0 or l.states is null)";
						Query query = session.createQuery(hql);
						query.setParameter("layerId", layerId);
						Object obj = query.uniqueResult();
						if (obj != null)
							return obj;
						return null;
					}
				});
	}

	/**
	 * 
	 */
	public void deleteRefUserLayersByLayerId(final Long layerId) {
		getHibernateTemplate().execute(new HibernateCallback() {

			@SuppressWarnings("rawtypes")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from RefUserLayer ref where ref.TLayers.id=:layerId and (ref.TLayers.states = 0 or ref.TLayers.states is null)";
				Query query = session.createQuery(hql);
				query.setParameter("layerId", layerId);
				List refs = query.list();
				if (refs != null) {
					for (Iterator iterator = refs.iterator(); iterator
							.hasNext();) {
						RefUserLayer ref = (RefUserLayer) iterator.next();
						session.delete(ref);
					}
				}
				return null;
			}
		});
	}

	public Page<RefUserLayer> listLayerVisibleByUserId(String entCode,
			Long userId, int pageNo, int pageSize) {
		String hql = "select ref from RefUserLayer ref "
				+ "left join ref.TLayers l "
				+ "where (l.states = 0 or l.states is null) and l.useStatus=1 and l.visible=1 "
				+ "and l.entcode=? and ref.userId=?";
		PageQuery<RefUserLayer> pageQuery = new PageQuery<RefUserLayer>(this);
		Page<RefUserLayer> page = new Page<RefUserLayer>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, userId);
	}

	public void saveRefUserLayer(RefUserLayer refUserLayer) {
		getHibernateTemplate().save(refUserLayer);
	}

	@SuppressWarnings("rawtypes")
	public List findAllRefUserLayerByUserId(final Long userId) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from RefUserLayer ref where ref.userId=:userId";
				Query query = session.createQuery(hql);
				query.setParameter("userId", userId);
				return query.list();
			}
		});
	}

	public void updateRefUserLayerVisible(RefUserLayer instance) {
		getHibernateTemplate().saveOrUpdate(instance);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Object[]> listPoiCreateByUserId(String entCode, Long userId,
			int startint, int limitint, String searchValue, Date startDate,
			Date endDate) {
		Session session = null;
		List<Object[]> list = null;
		int pageNo = startint / limitint + 1;
		try {
			String sql = " select count(p.id) as num from t_poi p "
					+ " left join ref_layer_poi rlp on rlp.poi_id = p.id  "
					+ " left join t_layers l on l.id = rlp.layer_id  "
					+ " left join t_terminal t on t.device_id = p.device_id  "
					+ " where l.entcode = '"
					+ entCode
					+ "' and l.user_id = "
					+ userId
					+ " and l.use_status = 1 and (l.states = 0 or l.states is null)"
					+ " and (p.poi_name like '%" + searchValue
					+ "%' or t.term_name like'%" + searchValue
					+ "%' or l.layer_name like '%" + searchValue + "%')  "
					+ " and (p.cdate >= TO_DATE('"
					+ DateUtility.dateTimeToStr(startDate)
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " and p.cdate <= TO_DATE('"
					+ DateUtility.dateTimeToStr(endDate)
					+ "', 'yyyy-mm-dd hh24:mi:ss'))    "
					// v2.1 weimeng 2012-8-31
					+ " and (p.states = 0 or p.states is null) ";
			//

			session = getHibernateTemplate().getSessionFactory().openSession();
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(pageNo);
			page.setPageSize(limitint);
			Query query = session.createSQLQuery(sql);
			list = query.list();
			Iterator iterator = list.iterator();
			if (iterator.hasNext()) {
				BigDecimal object = (BigDecimal) iterator.next();
				int count = object.intValue();
				page.setTotalCount(count);
			} else {
				return null;
			}

			sql = " select * from (select a.*, rownum row_num from ( select "
					+ " p.id as p_id, p.poi_type, p.poi_datas, p.poi_encrypt_datas, p.iconpath, "
					+ " p.poi_name, p.loc_desc, p.address, p.telephone, p.visit_distance, "
					+ " to_char(p.cdate,'yyyy-MM-dd hh24:mi:ss'), p.device_id "
					+ " , l.id as layer_id, l.layer_name "
					+ " , t.term_name, p.poi_desc " + " from t_poi p "
					+ " left join ref_layer_poi rlp on rlp.poi_id = p.id  "
					+ " left join t_layers l on l.id = rlp.layer_id  "
					+ " left join t_terminal t on t.device_id = p.device_id  "
					+ " where l.entcode = '"
					+ entCode
					+ "' and l.user_id = "
					+ userId
					+ " and l.use_status = 1 and (l.states = 0 or l.states is null) "
					+ " and (p.poi_name like '%"
					+ searchValue
					+ "%' or t.term_name like'%"
					+ searchValue
					+ "%' or l.layer_name like '%"
					+ searchValue
					+ "%')  "
					+ " and (p.cdate >= TO_DATE('"
					+ DateUtility.dateTimeToStr(startDate)
					+ "', 'yyyy-mm-dd hh24:mi:ss')  "
					+ " and p.cdate <= TO_DATE('"
					+ DateUtility.dateTimeToStr(endDate)
					+ "', 'yyyy-mm-dd hh24:mi:ss'))    "
					// v2.1 weimeng 2012-8-30
					+ " and  (p.states = 0 or p.states is null) "
					+ " order by l.id, p.poi_name,p.cdate desc  ) a) b "
					+ " where b.row_num between "
					+ (startint + 1)
					+ " and "
					+ (startint + limitint) + "";
			query = session.createSQLQuery(sql);
			list = query.list();
			page.setResult(list);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (session != null) {
					session.clear();
					session.close();
					getHibernateTemplate().getSessionFactory().close();
				}
			} catch (Exception e) {

			}
		}
	}

	/**
	 * 查询符合条件的点
	 */
	public Page<Object[]> queryMatchingPoi(final String sql, final int start,
			final int limit) {
		if (sql == null) {
			return null;
		}
		List<?> list = (List<?>) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createSQLQuery(
								new StringBuffer("select count(*) from(")
										.append(sql).append(")").toString())
								.list();

					}
				});

		// 获得符合条件的记录条数
		int size = list != null && list.size() > 0 ? ((BigDecimal) list.get(0))
				.intValue() : 0;

		if (size > 0) {
			List<?> li = (List<?>) getHibernateTemplate().execute(
					new HibernateCallback() {

						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {

							return session.createSQLQuery(sql)
									.setFirstResult(start).setMaxResults(limit)
									.list();
						}
					});
			List<Object[]> result = new ArrayList<Object[]>();
			for (Object obj : li) {
				result.add((Object[]) obj);
			}
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(start / limit + 1);
			page.setPageSize(limit);
			page.setTotalCount(size);
			page.setResult(result);
			return page;
		}
		return null;

	}

	/**
	 * <ul>
	 * 查询所有符合条件的点
	 * <ul>
	 * 不区分是否绑定
	 */
	public Page<Object[]> queryMatchingPoi(String entCode, long userId,
			final int start, final int limit, String poiName,
			String poiDescription, String locDescription, Integer layerId,
			String terminalName, String[] terminalGroupIds, Date startDate,
			Date endDate) {

		if (StringUtils.isNotBlank(terminalName) || terminalGroupIds != null) {
			return queryBindingPoi(entCode, userId, start, limit, poiName,
					poiDescription, locDescription, layerId, terminalName,
					terminalGroupIds, startDate, endDate);
		}

		final List<Object> params = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer()
				.append(" select distinct ply.*, t.term_name")
				.append(" from (select p.id pid, p.poi_name pname, p.poi_type ptype,  p.poi_datas pdatas, p.poi_encrypt_datas pendatas,")
				.append(" p.iconpath picon, p.loc_desc pldesc, p.address paddr, p.telephone ptel, p.visit_distance pvidis,")
				.append(" to_char(p.cdate, 'yyyy-MM-dd hh24:mi:ss') pcdata, p.device_id pdid, p.poi_desc pdesc,")
				.append(" ly.id lid, ly.layer_name lname")
				.append(" from t_poi p")
				.append(" join ref_layer_poi rlp on p.id = rlp.poi_id")
				.append(" join t_layers ly on rlp.layer_id = ly.id")
				.append(" join ref_user_layer rul on ly.id = rul.layer_id")
				.append(" where (ly.states = 0 or ly.states is null) and ly.use_status = 1 and rul.user_id=? and p.poi_datas is not null")
				.append(" and instr(p.poi_datas, ',') <> 0 ")
				// v2.1 weimeng 2012-8-31
				.append(" and (p.states = 0 or p.states is null) ");
		params.add(userId);
		if (!StringUtility.isNullOrBlank(poiName)) {
			sql.append(" and p.poi_name like ?");
			params.add("%" + poiName + "%");
		}
		if (!StringUtility.isNullOrBlank(poiDescription)) {
			sql.append(" and p.poi_desc like ?");
			params.add("%" + poiDescription + "%");
		}
		if (!StringUtility.isNullOrBlank(locDescription)) {
			sql.append(" and p.loc_desc like ?");
			params.add("%" + locDescription + "%");
		}
		if (startDate != null) {
			sql.append(" and p.cdate > ?");
			params.add(startDate);
		}
		if (endDate != null) {
			sql.append(" and p.cdate < ?");
			params.add(endDate);
		}

		if (!StringUtility.isNullOrBlank(entCode)) {
			sql.append(" and ly.entcode = ?");
			params.add(entCode);
		}
		// if (userId != null) {
		// sql.append(" and ly.user_id = ?");
		// params.add(userId);
		// }
		if (layerId != null) {
			sql.append(" and ly.id = ?");
			params.add(layerId);
		}
		sql.append(") ply left join t_terminal t on ply.pdid = t.device_id");

		List<?> list = (List<?>) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(new StringBuffer(
								"select count(*) from(").append(sql)
								.append(")").toString());

						int count = 0;
						Iterator<Object> it = params.iterator();
						while (it.hasNext()) {
							query.setParameter(count++, it.next());
						}
						return query.list();
					}
				});

		int size = list != null && list.size() > 0 ? ((BigDecimal) list.get(0))
				.intValue() : 0;

		if (size > 0) {

			List<?> li = (List<?>) getHibernateTemplate().execute(
					new HibernateCallback() {

						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createSQLQuery(sql.append(
									" order by ply.lid, ply.pid").toString());
							int count = 0;
							Iterator<Object> it = params.iterator();
							while (it.hasNext()) {
								query.setParameter(count++, it.next());
							}
							return query.setFirstResult(start)
									.setMaxResults(limit).list();
						}
					});

			List<Object[]> result = new ArrayList<Object[]>();
			for (Object obj : li) {
				result.add((Object[]) obj);
			}
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(start / limit + 1);
			page.setPageSize(limit);
			page.setTotalCount(size);
			page.setResult(result);
			return page;
		}
		return null;
	}

	/**
	 * 查询与终端绑定的点
	 */
	public Page<Object[]> queryBindingPoi(String entCode, long userId,
			final int start, final int limit, String poiName,
			String poiDescription, String locDescription, Integer layerId,
			String terminalName, String[] terminalGroupIds, Date startDate,
			Date endDate) {

		final List<Object> params = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer()
				.append("select plt.*, t.term_name")
				.append(" from (select distinct ply.*")
				.append(" from (select p.id pid, p.poi_name pname, p.poi_type ptype,  p.poi_datas pdatas, p.poi_encrypt_datas pendatas,")
				.append(" p.iconpath picon, p.loc_desc pldesc, p.address paddr, p.telephone ptel, p.visit_distance pvidis,")
				.append(" to_char(p.cdate, 'yyyy-MM-dd hh24:mi:ss') pcdata, p.device_id pdid, p.poi_desc pdesc,")
				.append(" ly.id lid, ly.layer_name lname")
				.append(" from t_poi p")
				.append(" join ref_layer_poi rlp on p.id = rlp.poi_id")
				.append(" join t_layers ly on rlp.layer_id = ly.id")
				.append(" join ref_user_layer rul on ly.id = rul.layer_id")
				.append(" where (ly.states = 0 or ly.states is null) and ly.use_status = 1 and rul.user_id=? and p.poi_datas is not null")
				.append(" and instr(p.poi_datas, ',') <> 0")
				// v2.1 weimeng 2012-8-31
				.append(" and (p.states = 0 or p.states is null) ");
		params.add(userId);
		if (!StringUtility.isNullOrBlank(poiName)) {
			sql.append(" and p.poi_name like ?");
			params.add("%" + poiName + "%");
		}
		if (!StringUtility.isNullOrBlank(poiDescription)) {
			sql.append(" and p.poi_desc like ?");
			params.add("%" + poiDescription + "%");
		}
		if (!StringUtility.isNullOrBlank(locDescription)) {
			sql.append(" and p.loc_desc like ?");
			params.add("%" + locDescription + "%");
		}
		if (startDate != null) {
			sql.append(" and p.cdate > ?");
			params.add(startDate);
		}
		if (endDate != null) {
			sql.append(" and p.cdate < ?");
			params.add(endDate);
		}

		if (!StringUtility.isNullOrBlank(entCode)) {
			sql.append(" and ly.entcode = ?");
			params.add(entCode);
		}
		// if (userId != null) {
		// sql.append(" and ly.user_id = ?");
		// params.add(userId);
		// }
		if (layerId != null) {
			sql.append(" and ly.id = ?");
			params.add(layerId);
		}
		sql.append(") ply join");

		if (StringUtility.isNullOrBlank(terminalName)
				&& terminalGroupIds == null) {
			sql.append(" ref_term_poi");

		} else if (!StringUtility.isNullOrBlank(terminalName)
				&& terminalGroupIds == null) {
			sql.append(
					" (select distinct rtp.poi_id from ref_term_poi rtp join t_terminal tml")
					.append(" on rtp.device_id = tml.device_id where tml.term_name = ?")
					.append(")");
			params.add(terminalName);

		} else if (StringUtility.isNullOrBlank(terminalName)
				&& terminalGroupIds != null) {

			sql.append(" (select distinct rtp.poi_id from ref_term_poi rtp")
					.append(" join t_terminal tml on rtp.device_id = tml.device_id")
					.append(" join ref_term_group rtg on tml.device_id = rtg.device_id")
					.append(" join t_term_group tg on rtg.group_id = tg.id where tg.id in(");

			for (String s : terminalGroupIds) {
				sql.append("?,");
				params.add(s);
			}
			sql.deleteCharAt(sql.length() - 1).append("))");

		} else {

			sql.append(" (select distinct rtp.poi_id from ref_term_poi rtp")
					.append(" join t_terminal tml on rtp.device_id = tml.device_id")
					.append(" join ref_term_group rtg on tml.device_id = rtg.device_id")
					.append(" join t_term_group tg on rtg.group_id = tg.id")
					.append(" where tml.term_name = ? and tg.id in(");

			params.add(terminalName);
			for (String s : terminalGroupIds) {
				sql.append("?,");
				params.add(s);
			}
			sql.deleteCharAt(sql.length() - 1).append("))");

		}
		sql.append(" rt on ply.pid = rt.poi_id) plt").append(
				" left join t_terminal t on plt.pdid = t.device_id");

		List<?> list = (List<?>) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(new StringBuffer(
								"select count(*) from(").append(sql)
								.append(")").toString());

						int count = 0;
						Iterator<Object> it = params.iterator();
						while (it.hasNext()) {
							query.setParameter(count++, it.next());
						}
						return query.list();
					}
				});
		int size = list != null && list.size() > 0 ? ((BigDecimal) list.get(0))
				.intValue() : 0;

		if (size > 0) {

			List<?> li = (List<?>) getHibernateTemplate().execute(
					new HibernateCallback() {

						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createSQLQuery(sql.append(
									" order by plt.lid, plt.pid").toString());
							int count = 0;
							Iterator<Object> it = params.iterator();
							while (it.hasNext()) {
								query.setParameter(count++, it.next());
							}
							return query.setFirstResult(start)
									.setMaxResults(limit).list();
						}
					});

			List<Object[]> result = new ArrayList<Object[]>();
			for (Object obj : li) {
				result.add((Object[]) obj);
			}
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(start / limit + 1);
			page.setPageSize(limit);
			page.setTotalCount(size);
			page.setResult(result);
			return page;
		}
		return null;
	}

	/**
	 * 根据条件查找标注点
	 * <ul>
	 * 选择标注点名称、选择终端名称，则查找满足以下条件的标注点：a未与该终端绑定 b名称模糊匹配所选名称
	 * <ul>
	 * 选择标注点名称、选择终端所属组名称，则查找满足以下条件的标注点：a未与该组下任一终端绑定 b名称模糊匹配所选名称
	 * <ul>
	 * 选择标注点名称、选择终端名称、选择终端所属组名称，则查找满足以下下条件的标注点：a未与该组下该终端绑定 c名称模糊匹配所选名称
	 * <ul>
	 * 选择标注点名称、未选择终端名称或终端所属组 ，则查找满足以下条件的标注点：a未与任何终端绑定 b名称模糊匹配所选名称
	 */
	public Page<Object[]> queryNotBindingPoi(String entCode, long userId,
			final int start, final int limit, String poiName,
			String poiDescription, String locDescription, Integer layerId,
			String terminalName, String[] terminalGroupIds, Date startDate,
			Date endDate) {
		final List<Object> params = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer()
				.append("select plt.*, t.term_name")
				.append(" from (select distinct ply.*")
				.append(" from (select p.id pid, p.poi_name pname, p.poi_type ptype,  p.poi_datas pdatas, p.poi_encrypt_datas pendatas,")
				.append(" p.iconpath picon, p.loc_desc pldesc, p.address paddr, p.telephone ptel, p.visit_distance pvidis,")
				.append(" to_char(p.cdate, 'yyyy-MM-dd hh24:mi:ss') pcdata, p.device_id pdid, p.poi_desc pdesc,")
				.append(" ly.id lid, ly.layer_name lname")
				.append(" from t_poi p")
				.append(" join ref_layer_poi rlp on p.id = rlp.poi_id")
				.append(" join t_layers ly on rlp.layer_id = ly.id")
				.append(" join ref_user_layer rul on ly.id = rul.layer_id")
				.append(" where (ly.states = 0 or ly.states is null) and ly.use_status = 1 and rul.user_id=? and p.poi_datas is not null")
				.append(" and instr(p.poi_datas, ',') <> 0")
				// v2.1 weimeng 2012-8-31
				.append(" and (p.states = 0 or p.states is null) ");
		params.add(userId);
		if (!StringUtility.isNullOrBlank(poiName)) {
			sql.append(" and p.poi_name like ?");
			params.add("%" + poiName + "%");
		}
		if (!StringUtility.isNullOrBlank(poiDescription)) {
			sql.append(" and p.poi_desc like ?");
			params.add("%" + poiDescription + "%");
		}
		if (!StringUtility.isNullOrBlank(locDescription)) {
			sql.append(" and p.loc_desc like ?");
			params.add("%" + locDescription + "%");
		}
		if (startDate != null) {
			sql.append(" and p.cdate > ?");
			params.add(startDate);
		}
		if (endDate != null) {
			sql.append(" and p.cdate < ?");
			params.add(endDate);
		}

		if (!StringUtility.isNullOrBlank(entCode)) {
			sql.append(" and ly.entcode = ?");
			params.add(entCode);
		}
		// if (userId != null) {
		// sql.append(" and ly.user_id = ?");
		// params.add(userId);
		// }
		if (layerId != null) {
			sql.append(" and ly.id = ?");
			params.add(layerId);
		}
		sql.append(") ply left join");

		if (StringUtility.isNullOrBlank(terminalName)
				&& terminalGroupIds == null) {
			sql.append(" ref_term_poi");

		} else if (!StringUtility.isNullOrBlank(terminalName)
				&& terminalGroupIds == null) {
			sql.append(
					" (select distinct rtp.poi_id from ref_term_poi rtp join t_terminal tml")
					.append(" on rtp.device_id = tml.device_id where tml.term_name = ?")
					.append(")");
			params.add(terminalName);

		} else if (StringUtility.isNullOrBlank(terminalName)
				&& terminalGroupIds != null) {

			sql.append(" (select distinct rtp.poi_id from ref_term_poi rtp")
					.append(" join t_terminal tml on rtp.device_id = tml.device_id")
					.append(" join ref_term_group rtg on tml.device_id = rtg.device_id")
					.append(" join t_term_group tg on rtg.group_id = tg.id where tg.id in(");

			for (String s : terminalGroupIds) {
				sql.append("?,");
				params.add(s);
			}
			sql.deleteCharAt(sql.length() - 1).append("))");

		} else {

			sql.append(" (select distinct rtp.poi_id from ref_term_poi rtp")
					.append(" join t_terminal tml on rtp.device_id = tml.device_id")
					.append(" join ref_term_group rtg on tml.device_id = rtg.device_id")
					.append(" join t_term_group tg on rtg.group_id = tg.id")
					.append(" where tml.term_name = ? and tg.id in(");

			params.add(terminalName);
			for (String s : terminalGroupIds) {
				sql.append("?,");
				params.add(s);
			}
			sql.deleteCharAt(sql.length() - 1).append("))");

		}
		sql.append(" rt on ply.pid = rt.poi_id where rt.poi_id is null) plt")
				.append(" left join t_terminal t on plt.pdid = t.device_id");

		List<?> list = (List<?>) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(new StringBuffer(
								"select count(*) from(").append(sql)
								.append(")").toString());

						int count = 0;
						Iterator<Object> it = params.iterator();
						while (it.hasNext()) {
							query.setParameter(count++, it.next());
						}
						return query.list();
					}
				});
		int size = list != null && list.size() > 0 ? ((BigDecimal) list.get(0))
				.intValue() : 0;

		if (size > 0) {

			List<?> li = (List<?>) getHibernateTemplate().execute(
					new HibernateCallback() {

						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createSQLQuery(sql.append(
									" order by plt.lid, plt.pid").toString());
							int count = 0;
							Iterator<Object> it = params.iterator();
							while (it.hasNext()) {
								query.setParameter(count++, it.next());
							}
							return query.setFirstResult(start)
									.setMaxResults(limit).list();
						}
					});

			List<Object[]> result = new ArrayList<Object[]>();
			for (Object obj : li) {
				result.add((Object[]) obj);
			}
			Page<Object[]> page = new Page<Object[]>();
			page.setPageNo(start / limit + 1);
			page.setPageSize(limit);
			page.setTotalCount(size);
			page.setResult(result);
			return page;
		}
		return null;
	}

	public Page<TPoi> listNotRefPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue) {
		String hql = "select p from TPoi p "
				+ "left join fetch p.refLayerPois ref left join fetch ref.TLayers l "
				+ "where (l.states = 0 or l.states is null) and l.entcode=? and l.userId=? and l.useStatus=1 "
				+ "and p.id not in "
				+ "(select distinct ref2.TPoi.id from RefTermPoi ref2 "
				+ "left join ref2.TPoi pp left join pp.refLayerPois ref22 "
				+ "left join ref22.TLayers ll "
				// + "where ll.entcode=? and ll.userId=? and ll.useStatus=1 ) ";
				// v2.1 weimeng 2012-8-31
				+ "where (ll.states = 0 or ll.states is null) and ll.entcode=? and ll.userId=? and ll.useStatus=1 and (pp.states = 0 or pp.states is null) ) "
				+ " and (p.states = 0 or p.states is null) ";
		// + "and (p.cdate >= ? and p.cdate <= ?) ";
		if (searchValue != null && !searchValue.equals("")) {
			hql += "and (p.poiName like ? or l.layerName like ?) ";
		}
		hql += "order by l.layerName,p.poiName";
		PageQuery<TPoi> pageQuery = new PageQuery<TPoi>(this);
		Page<TPoi> page = new Page<TPoi>();
		page.setAutoCount(false);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if (searchValue != null && !searchValue.equals("")) {
			return pageQuery.findByPage(page, hql, entCode, userId, entCode,
					userId, "%" + searchValue + "%", "%" + searchValue + "%");
		} else {
			return pageQuery.findByPage(page, hql, entCode, userId, entCode,
					userId);
		}
	}

	public Page<TPoi> listPoiVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue) {
		String hql = "select p from TPoi p "
				+ "left join fetch p.refLayerPois ref left join fetch ref.TLayers l "
				+ "left join fetch l.refUserLayers ref1 "
				// + "left join p.refTermPois ref2 left join ref2.TTerminal t "
				+ "where (l.states = 0 or l.states is null) and l.entcode=? and ref1.userId=? and l.useStatus=1 "
				+ "and l.visible=1 "
				+ "and (p.poiName like ? or l.layerName like ?) "
				// v2.1 weimeng 2012-8-31
				+ " and (p.states = 0 or p.states is null) "
				+ "order by l.layerName,p.poiName";
		PageQuery<TPoi> pageQuery = new PageQuery<TPoi>(this);
		Page<TPoi> page = new Page<TPoi>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, userId, "%"
				+ searchValue + "%", "%" + searchValue + "%");
	}

	public void attachDirtyPoi(TPoi instance) {
		getHibernateTemplate().saveOrUpdate(instance);
	}

	public void deletePoi(TPoi persistentInstance) {
		getHibernateTemplate().delete(persistentInstance);
	}

	public void savePoi(TPoi transientInstance) {
		getHibernateTemplate().save(transientInstance);
	}

	public TPoi findPoiById(Long id) {
		TPoi instance = (TPoi) getHibernateTemplate().get(
				"com.sosgps.wzt.orm.TPoi", id);
		return instance;
	}

	public void deleteRefLayerPoisByPoiId(final Long poiId) {
		getHibernateTemplate().execute(new HibernateCallback() {

			@SuppressWarnings("rawtypes")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from RefLayerPoi ref where ref.TPoi.id=:poiId";
				Query query = session.createQuery(hql);
				query.setParameter("poiId", poiId);
				List refs = query.list();
				if (refs != null) {
					for (Iterator iterator = refs.iterator(); iterator
							.hasNext();) {
						RefLayerPoi ref = (RefLayerPoi) iterator.next();
						session.delete(ref);
					}
				}
				return null;
			}
		});
	}

	public void deleteRefTermPoisByPoiId(final Long poiId) {
		getHibernateTemplate().execute(new HibernateCallback() {

			@SuppressWarnings("rawtypes")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from RefTermPoi ref where ref.TPoi.id=:poiId";
				Query query = session.createQuery(hql);
				query.setParameter("poiId", poiId);
				List refs = query.list();
				if (refs != null) {
					for (Iterator iterator = refs.iterator(); iterator
							.hasNext();) {
						RefTermPoi ref = (RefTermPoi) iterator.next();
						session.delete(ref);
					}
				}
				return null;
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public void deleteRefTermPoisByDeviceId(final String deviceId) {
		Object obj = getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from RefTermPoi ref where ref.TTerminal.deviceId=:deviceId";
				Query query = session.createQuery(hql);
				query.setParameter("deviceId", deviceId);
				List refs = query.list();
				return refs;
			}
		});
		if (obj != null) {
			List entities = (List) obj;
			getHibernateTemplate().deleteAll(entities);
		}
	}

	public void saveRefLayerPoi(RefLayerPoi refLayerPoi) {
		getHibernateTemplate().save(refLayerPoi);
	}

	public void saveRefTermPoi(RefTermPoi refTermPoi) {
		getHibernateTemplate().save(refTermPoi);
	}

	public void saveRefTermPoi(Set<RefTermPoi> refTermPois) {
		getHibernateTemplate().saveOrUpdateAll(refTermPois);
	}

	public void updateRefLayerPoi(RefLayerPoi refLayerPoi) {
		getHibernateTemplate().saveOrUpdate(refLayerPoi);
	}

	public TPoi queryPoi(final Long poiId) {
		return (TPoi) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "select p from TPoi p "
						+ "left join fetch p.refLayerPois ref "
						+ "left join fetch p.refTermPois ref2 "
						+ "where p.id=:poiId "
						// v2.1 weimeng 2012-8-31
						+ " and (p.states = 0 or p.states is null) ";
				Query query = session.createQuery(hql);
				query.setParameter("poiId", poiId);
				Object obj = query.uniqueResult();
				if (obj != null)
					return obj;
				return null;
			}
		});
	}

	public Page<TLayers> listVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize) {
		String hql = "select l from TLayers l "
				+ "left join fetch l.refUserLayers ref "
				+ "where l.useStatus=1 and l.visible=1 "
				+ "and l.entcode=? and ref.userId=? and (l.states = 0 or l.states is null)";
		PageQuery<TLayers> pageQuery = new PageQuery<TLayers>(this);
		Page<TLayers> page = new Page<TLayers>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, userId);
	}

	public Page<TLayers> listVisibleAndPoiByUserId(String entCode, Long userId,
			int pageNo, int pageSize) {
		String hql = "select l from TLayers l "
				+ "left join fetch l.refLayerPois ref left join fetch ref.TPoi p "
				+ "left join fetch l.refUserLayers ref "
				+ "where (l.states = 0 or l.states is null) and l.useStatus=1 and l.visible=1 and ref.visible=1 "
				+ "and l.entcode=? and ref.userId=? "
				// v2.1 weimeng 2012-8-31
				+ " and (p.states = 0 or p.states is null) "
				+ "order by l.layerName";
		PageQuery<TLayers> pageQuery = new PageQuery<TLayers>(this);
		Page<TLayers> page = new Page<TLayers>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, userId);
	}

	public Page<TPoi> queryPoiByDeviceId(String deviceId, Long userId) {
		String hql = "select p from TPoi p "
				+ "left join p.refTermPois ref left join ref.TTerminal t "
				+ "left join p.refLayerPois ref1 left join ref1.TLayers l "
				+ "left join l.refUserLayers ref2 "
				+ "where (l.states = 0 or l.states is null) and ref2.userId=? and l.useStatus=1 "
				+ "and t.deviceId=? "
				// v2.1 weimeng 2012-8-31
				+ " and (p.states = 0 or p.states is null) "
				+ "order by p.poiName";
		PageQuery<TPoi> pageQuery = new PageQuery<TPoi>(this);
		Page<TPoi> page = new Page<TPoi>();
		page.setAutoCount(true);
		page.setPageNo(1);
		page.setPageSize(Integer.MAX_VALUE);
		return pageQuery.findByPage(page, hql, userId, deviceId);
	}

	public Page<TPoi> queryManagePoiByDeviceId(String deviceId, Long userId) {
		String hql = "select p from TPoi p "
				+ "left join p. refTermPois ref left join ref.TTerminal t "
				+ "left join p.refLayerPois ref1 left join ref1.TLayers l "
				+ "where (l.states = 0 or l.states is null) and l.useStatus=1 and l.visible=1 and l.userId=? "
				+ "and t.deviceId=? "
				// v2.1 weimeng 2012-8-31
				+ " and (p.states = 0 or p.states is null) "
				+ "order by p.poiName";
		PageQuery<TPoi> pageQuery = new PageQuery<TPoi>(this);
		Page<TPoi> page = new Page<TPoi>();
		page.setAutoCount(true);
		page.setPageNo(1);
		page.setPageSize(Integer.MAX_VALUE);
		return pageQuery.findByPage(page, hql, userId, deviceId);
	}

	@SuppressWarnings("unchecked")
	public void deleteLayersByEntCode(String entCode) {
		List<TLayers> list = findByEntcode(entCode);
		if (list != null)
			for (TLayers layer : list) {
				delete(layer);
			}
	}

	@SuppressWarnings("rawtypes")
	public List listRefs(final String[] poiIds, final String[] deviceIds) {
		if (poiIds == null || poiIds.length == 0 || deviceIds == null
				|| deviceIds.length == 0)
			return null;
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String ids = "";
				for (int i = 0; i < poiIds.length; i++) {
					String id = poiIds[i];
					if (i < poiIds.length - 1) {
						ids += id + ",";
					} else {
						ids += id;
					}
				}
				String dIds = "";
				for (int i = 0; i < deviceIds.length; i++) {
					String dId = deviceIds[i];
					if (i < deviceIds.length - 1) {
						dIds += "'" + dId + "'" + ",";
					} else {
						dIds += "'" + dId + "'";
					}
				}
				String hql = "select ref from TPoi p "
						+ "left join p.refTermPois ref left join ref.TTerminal t "
						+ "where p.id in (" + ids + ") or t.deviceId in ("
						+ dIds + ")"
						// v2.1 weimeng 2012-8-31
						+ " and (ps = 0 or p.states is null) ";
				Query query = arg0.createQuery(hql);
				return query.list();
			}
		});
	}

	// add by magiejue 2010-12-7 查询用户所有标注点（不分页）
	@SuppressWarnings("unchecked")
	public List<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			String searchValue, String poiLayer, Date startDate, Date endDate) {
		String hql = "select p from TPoi p "
				+ "left join fetch p.refLayerPois ref left join fetch ref.TLayers l "
				+ "where (l.states = 0 or l.states is null) and l.entcode=? and l.userId=? and l.useStatus=1 "
				+ "and (p.poiName like ? or p.poiDesc like ? or p.address like ? or p.telephone like ? ) "
				+ " and (p.cdate >= ? and p.cdate <= ?) ";
		if (poiLayer != null && !poiLayer.equals("-1") && !poiLayer.equals("")) {
			hql += " and l.id=" + Long.valueOf(poiLayer);
		}
		// if(!poiLayer.equals("")){
		// hql+=" and l.id="+Long.valueOf(poiLayer);
		// }
		// v2.1 weimeng 2012-8-31
		hql += " and (ps = 0 or p.states is null) ";
		hql += " order by l.layerName,p.poiName";
		return getHibernateTemplate().find(
				hql,
				new Object[] { entCode, userId, "%" + searchValue + "%",
						"%" + searchValue + "%", "%" + searchValue + "%",
						"%" + searchValue + "%", startDate, endDate });
	}

	public void saveRefTermPoi(final String[] poiIds, final String[] deviceIds) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Transaction tx = session.beginTransaction();
				StringBuilder inDeviceId = new StringBuilder();
				// update for renxianliang 2013-1-16
				if (deviceIds.length < 0 || poiIds.length < 0) {
					return null;
				}
				try {
					for (String did : deviceIds) {
						String sql = "select rtp.TPoi.id,rtp.TTerminal.deviceId from RefTermPoi rtp where rtp.TTerminal.deviceId='"
								+ did + "'";
						Query query = session.createQuery(sql);
						List<Object> list = query.list();
						Hashtable table = new Hashtable();
						for (Iterator iterator = list.iterator(); iterator
								.hasNext();) {
							Object[] obj = (Object[]) iterator.next();
							long poi = (Long) obj[0];
							String device = (String) obj[1];
							table.put(poi, device);
						}
						StringBuilder poiIdsSb = new StringBuilder();
						for (String pid : poiIds) {
							if (!did.equals(table.get(Long.parseLong(pid)))) {
								poiIdsSb.append("'").append(pid).append("'")
										.append(",");
							}
						}
						if (poiIdsSb.length() > 0 && poiIdsSb != null) {
							poiIdsSb.deleteCharAt(poiIdsSb.length() - 1);

							StringBuffer buf = new StringBuffer(
									"insert into RefTermPoi(TPoi, TTerminal) from TPoi poi, TTerminal tml"
											+ " where poi.id in (" + poiIdsSb
											+ ") and tml.deviceId = '" + did
											+ "'");
							session.createQuery(buf.toString()).executeUpdate();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					tx.rollback();
				}
				tx.commit();
				return null;
			}
		});
	}

	public void deleteRefTermPois(final String[] poiIds,
			final String[] deviceIds) {

		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Transaction tx = session.beginTransaction();
				try {
					StringBuilder poiSb=new StringBuilder();
					for(String poi:poiIds){
						poiSb.append("'").append(poi).append("'").append(",");
					}
					poiSb.deleteCharAt(poiSb.length()-1);
					for (String did : deviceIds) {
						session.createQuery(
							"delete from RefTermPoi rtp where rtp.TPoi.id in ("+poiSb+") and rtp.TTerminal.deviceId = '"+did+"'").executeUpdate();
					}
				} catch (Exception e) {
					tx.rollback();
					e.printStackTrace();
				}
				tx.commit();
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<TPoi> listPoiByPoiName(String entCode, String tmpPoiName) {
		String hql = "select p from TPoi p "
				+ "where p.poiName = ?  and  p.entcode = ? "
				// v2.1 weimeng 2012-8-31
				+ " and (p.states = 0 or p.states is null) ";
		return getHibernateTemplate().find(hql,
				new Object[] { tmpPoiName, entCode });
	}

	public Page<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String poiLayer,
			Date startDate, Date endDate) {
		String hql = "select p from TPoi p "
				+ "left join fetch p.refLayerPois ref left join fetch ref.TLayers l "
				+ "where (l.states = 0 and l.states is null) and l.entcode=? and l.userId=? and l.useStatus=1 "
				+ "and (p.poiName like ? or p.poiDesc like ? or p.address like ? or p.telephone like ? ) "
				+ " and (p.cdate >= ? and p.cdate <= ?) ";
		if (poiLayer != null && !poiLayer.equals("-1") && !poiLayer.equals("")) {
			hql += " and l.id=" + Long.valueOf(poiLayer);
		}
		// if(!poiLayer.equals("")){
		// hql+=" and l.id="+Long.valueOf(poiLayer);
		// }
		// v2.1 weimeng 2012-8-31
		hql += " and (p.states = 0 or p.states is null) ";
		hql += " order by l.layerName,p.poiName";

		PageQuery<TPoi> pageQuery = new PageQuery<TPoi>(this);
		Page<TPoi> page = new Page<TPoi>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, userId, "%"
				+ searchValue + "%", "%" + searchValue + "%", "%" + searchValue
				+ "%", "%" + searchValue + "%", startDate, endDate);

		// return getHibernateTemplate().find(
		// hql,new Object[] { entCode, userId, "%" + searchValue + "%", "%" +
		// searchValue + "%",
		// "%" + searchValue + "%", "%" + searchValue + "%", startDate, endDate
		// });
	}

	/**
	 * 2011-11-22 weimeng 标注批量修改图层,批量提交方法
	 */
	public void updatePoiBatchLayerId(final String poiIds, final String layerId) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "update RefLayerPoi t set t.TLayers.id = "
						+ layerId + " where t.TPoi.id in (" + poiIds + ")";
				logger.info("[LayerAndPoiHibernateDao]updatePoiBatchLayerId hql = "
						+ hql);
				Query query = session.createQuery(hql);
				query.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * 2011-11-22 weimeng 标注批量修改范围,批量提交方法
	 */
	public void updatePoiBatchVisitDistance(final String poiIds,
			final String visitDistance) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "update TPoi t set t.visitDistance = "
						+ visitDistance + " where t.id in (" + poiIds + ")";
				logger.info("[LayerAndPoiHibernateDao]updatePoiBatchVisitDistance hql = "
						+ hql);
				Query query = session.createQuery(hql);
				query.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * 2011-11-22 weimeng 标注批量修改图标,批量提交方法
	 */
	public void updatePoiBatchIconpath(final String poiIds,
			final String iconpath) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "update TPoi t set t.iconpath = '" + iconpath
						+ "' where t.id in (" + poiIds + ")";
				logger.info("[LayerAndPoiHibernateDao]updatePoiBatchIconpath hql = "
						+ hql);
				Query query = session.createQuery(hql);
				query.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * v2.1 查询未删除企业poi点总数
	 * 
	 * @param entCode
	 * @return
	 */
	public Long queryPoiCount(final String entCode) {
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = " select count(p) from TPoi p "
						+ " where (p.states = 0 or p.states is null) "
						+ " and entcode = :entCode";
				Query query = session.createQuery(hql);
				query.setParameter("entCode", entCode);
				Object obj = query.uniqueResult();
				if (obj != null)
					return obj;
				return null;
			}
		});
	}

    public int checkPoi(final String layerIds) {
        Session session = null;
            try {
                String sql="select * from t_poi ttp where ttp.id in (select tl.poi_id from ref_layer_poi tl where" +
                		" tl.layer_id in ("+layerIds+")) and (ttp.states='0' or ttp.states is null)";
                session = getHibernateTemplate().getSessionFactory().openSession();
                Query query = session.createSQLQuery(sql);
                return query.list().size();
            } catch (HibernateException e) {
                e.printStackTrace();
            }finally{
                session.clear();
                session.close();
                getHibernateTemplate().getSessionFactory().close();
            }
            return 0;
    }
}
