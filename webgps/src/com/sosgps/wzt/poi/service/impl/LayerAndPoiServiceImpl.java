package com.sosgps.wzt.poi.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefLayerPoi;
import com.sosgps.wzt.orm.RefTermPoi;
import com.sosgps.wzt.orm.RefUserLayer;
import com.sosgps.wzt.orm.TLayers;
import com.sosgps.wzt.orm.TPoi;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.poi.dao.LayerAndPoiDao;
import com.sosgps.wzt.poi.service.LayerAndPoiService;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.StringUtility;

/**
 * @Title:图层poi业务层接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-10 下午03:35:35
 */
public class LayerAndPoiServiceImpl implements LayerAndPoiService {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(LayerAndPoiServiceImpl.class);

	private LayerAndPoiDao layerAndPoiDao;

	public LayerAndPoiDao getLayerAndPoiDao() {
		return layerAndPoiDao;
	}

	public void setLayerAndPoiDao(LayerAndPoiDao layerAndPoiDao) {
		this.layerAndPoiDao = layerAndPoiDao;
	}

	public Page<Object[]> listMatchingPois(String entCode, long userId,
			int pageNo, int pageSize, int bindingStatus, String poiName,
			String poiDescription, String locDescription, Integer layerId,
			String terminalName, String[] tGroups, Date startDate, Date endDate) {

		System.out.println(tGroups == null ? null : tGroups.length);
		if (bindingStatus == binding_yes) {
			return layerAndPoiDao.queryBindingPoi(entCode,userId, (pageNo - 1)
					* pageSize, pageSize, poiName, poiDescription,
					locDescription, layerId, terminalName, tGroups, startDate,
					endDate);
		} else if (bindingStatus == binding_no) {
			return layerAndPoiDao.queryNotBindingPoi(entCode,userId, (pageNo - 1)
					* pageSize, pageSize, poiName, poiDescription,
					locDescription, layerId, terminalName, tGroups, startDate,
					endDate);
		} else {
			return layerAndPoiDao.queryMatchingPoi(entCode,userId, (pageNo - 1)
					* pageSize, pageSize, poiName, poiDescription,
					locDescription, layerId, terminalName, tGroups, startDate,
					endDate);
		}
	}

	/**
	 * 根据条件查找标注点
	 * <ul>
	 * 选择标注点名称、选择终端名称、选择未绑定 ，则查找满足以下条件的标注点：a未与该终端绑定 b名称模糊匹配所选名称
	 * <ul>
	 * 选择标注点名称、选择终端所属组名称、选择未绑定 ，则查找满足以下条件的标注点：a未与该组下任一终端绑定 b名称模糊匹配所选名称
	 * <ul>
	 * 选择标注点名称、选择终端名称、选择终端所属组名称、选择未绑定 ，则查找满足以下下条件的标注点：a未与该组下该终端绑定 c名称模糊匹配所选名称
	 * <ul>
	 * 选择标注点名称、未选择终端名称或终端所属组、选择未绑定 ，则查找满足以下条件的标注点：a未与任何终端绑定 b名称模糊匹配所选名称
	 */
	public Page<Object[]> listMatchingPoi(String entCode, Long userId,
			int pageNo, int pageSize, int bindingStatus, String poiName,
			String poiDescription, String locDescription, Integer layerId,
			String terminalName, String[] tGroups, Date startDate, Date endDate) {

		// 终端所属组ID组成的字符串
		StringBuffer terminalGroupIds = null;
		if (tGroups != null && tGroups.length > 0) {
			terminalGroupIds = new StringBuffer();
			for (String s : tGroups) {
				if (s.length() > 0) {
					terminalGroupIds.append(s).append(",");
				}
			}
			terminalGroupIds.deleteCharAt(terminalGroupIds.length() - 1);
		}

		StringBuffer sql = new StringBuffer()
				.append(" from (select p.id pid, p.poi_name pname, p.poi_type ptype,  p.poi_datas pdatas, p.poi_encrypt_datas pendatas,")
				.append(" p.iconpath picon, p.loc_desc pldesc, p.address paddr, p.telephone ptel, p.visit_distance pvidis,")
				.append(" to_char(p.cdate, 'yyyy-MM-dd hh24:mi:ss') pcdata, p.device_id pdid, p.poi_desc pdesc,")
				.append(" ly.id lid, ly.layer_name lname")
				.append(" from t_poi p")
				.append(" join ref_layer_poi rlp on p.id = rlp.poi_id")
				.append(" join t_layers ly on rlp.layer_id = ly.id")
				.append(" where (ly.states = 0 or ly.states is null) and ly.use_status = 1 and p.poi_datas is not null")
				.append(" and instr(p.poi_datas, ',') <> 0")
				//v2.1 weimeng 2012-8-31
				.append(" and (p.states = 0 or p.states is null) ");

		if (!StringUtility.isNullOrBlank(poiName)) {
			sql.append(" and p.poi_name like ").append("'%").append(poiName)
					.append("%'");
		}
		if (!StringUtility.isNullOrBlank(poiDescription)) {
			sql.append(" and p.poi_desc like ").append("'%")
					.append(poiDescription).append("%'");
		}
		if (!StringUtility.isNullOrBlank(locDescription)) {
			sql.append(" and p.loc_desc like ").append("'%")
					.append(locDescription).append("%'");
		}
		if (startDate != null) {
			sql.append(" and p.cdate > ").append("to_date('")
					.append(DateUtility.dateTimeToStr(startDate))
					.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		if (endDate != null) {
			sql.append(" and p.cdate < ").append("to_date('")
					.append(DateUtility.dateTimeToStr(endDate))
					.append("','yyyy-mm-dd hh24:mi:ss')");
		}

		if (!StringUtility.isNullOrBlank(entCode)) {
			sql.append(" and ly.entcode = ").append("'").append(entCode)
					.append("'");
		}
		if (userId != null) {
			sql.append(" and ly.user_id = ").append(userId);
		}
		if (layerId != null) {
			sql.append(" and ly.id = ").append(layerId);
		}

		if (bindingStatus == binding_no) {
			sql.append(") ply left join");

			if (StringUtility.isNullOrBlank(terminalName)
					&& terminalGroupIds == null) {
				sql.append(" ref_term_poi");

			} else if (!StringUtility.isNullOrBlank(terminalName)
					&& terminalGroupIds == null) {
				sql.append(
						" (select distinct rtp.poi_id from ref_term_poi rtp join t_terminal tml")
						.append(" on rtp.device_id = tml.device_id where tml.term_name like '%")
						.append(terminalName).append("%')");

			} else if (StringUtility.isNullOrBlank(terminalName)
					&& terminalGroupIds != null) {

				sql.append(" (select distinct rtp.poi_id from ref_term_poi rtp")
						.append(" join t_terminal tml on rtp.device_id = tml.device_id")
						.append(" join ref_term_group rtg on tml.device_id = rtg.device_id")
						.append(" join t_term_group tg on rtg.group_id = tg.id where tg.id in(")
						.append(terminalGroupIds).append("))");

			} else {
				sql.append(" (select distinct rtp.poi_id from ref_term_poi rtp")
						.append(" join t_terminal tml on rtp.device_id = tml.device_id")
						.append(" join ref_term_group rtg on tml.device_id = rtg.device_id")
						.append(" join t_term_group tg on rtg.group_id = tg.id")
						.append(" where tml.term_name like '%")
						.append(terminalName).append("%' and tg.id in(")
						.append(terminalGroupIds).append("))");
			}
			sql.append(
					" rt on ply.pid = rt.poi_id where rt.poi_id is null) plt")
					.append(" left join t_terminal t on plt.pdid = t.device_id")
					.insert(0,
							"select plt.*, t.term_name  from (select distinct ply.*");
		}

		// 已绑定或不区分绑定
		// 当terminalName不为null或terminalGroupIds不为null时，不区分绑定将等同于绑定
		if (bindingStatus == binding_yes || bindingStatus == binding_ignore) {
			if (bindingStatus == binding_ignore
					&& StringUtility.isNullOrBlank(terminalName)
					&& terminalGroupIds == null) {
				sql.append(
						") ply left join t_terminal t on ply.pdid = t.device_id")
						.insert(0, "select distinct ply.*, t.term_name");

			} else {
				sql.append(") ply join");

				if (StringUtility.isNullOrBlank(terminalName)
						&& terminalGroupIds == null) {
					sql.append(" ref_term_poi");

				} else if (!StringUtility.isNullOrBlank(terminalName)
						&& terminalGroupIds == null) {
					sql.append(
							" (select distinct rtp.poi_id from ref_term_poi rtp join t_terminal tml")
							.append(" on rtp.device_id = tml.device_id where tml.term_name like '%")
							.append(terminalName).append("%')");

				} else if (StringUtility.isNullOrBlank(terminalName)
						&& terminalGroupIds != null) {

					sql.append(
							" (select distinct rtp.poi_id from ref_term_poi rtp")
							.append(" join t_terminal tml on rtp.device_id = tml.device_id")
							.append(" join ref_term_group rtg on tml.device_id = rtg.device_id")
							.append(" join t_term_group tg on rtg.group_id = tg.id where tg.id in(")
							.append(terminalGroupIds).append("))");

				} else {
					sql.append(
							" (select distinct rtp.poi_id from ref_term_poi rtp")
							.append(" join t_terminal tml on rtp.device_id = tml.device_id")
							.append(" join ref_term_group rtg on tml.device_id = rtg.device_id")
							.append(" join t_term_group tg on rtg.group_id = tg.id")
							.append(" where tml.term_name like '%")
							.append(terminalName).append("%' and tg.id in(")
							.append(terminalGroupIds).append("))");
				}
				sql.append(" rt on ply.pid = rt.poi_id) plt")
						.append(" left join t_terminal t on plt.pdid = t.device_id")
						.insert(0,
								"select plt.*, t.term_name  from (select distinct ply.*");
			}
		}

		return layerAndPoiDao.queryMatchingPoi(sql.toString(), (pageNo - 1)
				* pageSize, pageSize);
	}

	public Page<TLayers> listLayerCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue) {
		try {
			return layerAndPoiDao.listLayerCreateByUserId(entCode, userId,
					pageNo, pageSize, searchValue);
		} catch (Exception e) {
			logger.error("查询图层列表失败", e);
			return null;
		}
	}

	public TLayers queryLayer(String id) {
		try {
			Long layerId = Long.parseLong(id);
			return layerAndPoiDao.queryLayer(layerId);
		} catch (Exception e) {
			logger.error("查询图层信息失败", e);
			return null;
		}
	}

	public void addLayer(String entCode, String layerName, String layerDesc,
			Long userId, String visible, String mapLevel, String[] userIds) {
		try {
			TLayers transientInstance = new TLayers();
			transientInstance.setUseStatus(1L);// 可用
			transientInstance.setEntcode(entCode);
			transientInstance.setUserId(userId);
			transientInstance.setLayerName(layerName);
			transientInstance.setVisible(visible);
			transientInstance.setLayerDesc(layerDesc);
			transientInstance.setInfo1(mapLevel);
			transientInstance.setCtdate(Calendar.getInstance().getTime());
			transientInstance.setStates(0L);
			layerAndPoiDao.save(transientInstance);
			Set<RefUserLayer> refUserLayers = new HashSet<RefUserLayer>();
			boolean containsCreator = false;// 是否包括创建者自己
			for (int i = 0; i < userIds.length; i++) {
				Long user_id = Long.parseLong(userIds[i]);
				if (user_id.intValue() == userId.intValue()) {
					containsCreator = true;
				}
				RefUserLayer refUserLayer = new RefUserLayer();
				refUserLayer.setUserId(user_id);
				refUserLayer.setTLayers(transientInstance);
				refUserLayer.setVisible("1");// 默认1可见
				layerAndPoiDao.saveRefUserLayer(refUserLayer);
				refUserLayers.add(refUserLayer);
			}
			// 新增图层对创建者也可见
			if (!containsCreator) {
				RefUserLayer refUserLayer = new RefUserLayer();
				refUserLayer.setUserId(userId);
				refUserLayer.setTLayers(transientInstance);
				refUserLayer.setVisible("1");// 默认1可见
				layerAndPoiDao.saveRefUserLayer(refUserLayer);
				refUserLayers.add(refUserLayer);
			}
			transientInstance.setRefUserLayers(refUserLayers);
		} catch (Exception e) {
			logger.error("增加图层失败", e);
		}
	}

	public void updateLayer(Long id, String entCode, String layerName,
			String layerDesc, Long userId, String visible, String mapLevel,
			String[] userIds) {
		try {
			TLayers instance = layerAndPoiDao.findById(id);
			if (instance != null) {
				instance.setLayerName(layerName);
				instance.setVisible(visible);
				instance.setLayerDesc(layerDesc);
				instance.setInfo1(mapLevel);
				layerAndPoiDao.attachDirty(instance);
				// 先删除用户图层关系
				layerAndPoiDao.deleteRefUserLayersByLayerId(id);
				Set<RefUserLayer> refUserLayers = new HashSet<RefUserLayer>();
				boolean containsCreator = false;// 是否包括创建者自己
				for (int i = 0; i < userIds.length; i++) {
					Long user_id = Long.parseLong(userIds[i]);
					if (user_id.intValue() == userId.intValue()) {
						containsCreator = true;
					}
					RefUserLayer refUserLayer = new RefUserLayer();
					refUserLayer.setUserId(user_id);
					refUserLayer.setTLayers(instance);
					refUserLayer.setVisible("1");// 默认1可见
					layerAndPoiDao.saveRefUserLayer(refUserLayer);
					refUserLayers.add(refUserLayer);
				}
				// 新增图层对创建者也可见
				if (!containsCreator) {
					RefUserLayer refUserLayer = new RefUserLayer();
					refUserLayer.setUserId(userId);
					refUserLayer.setTLayers(instance);
					refUserLayer.setVisible("1");// 默认1可见
					layerAndPoiDao.saveRefUserLayer(refUserLayer);
					refUserLayers.add(refUserLayer);
				}
				instance.setRefUserLayers(refUserLayers);
			}
		} catch (Exception e) {
			logger.error("修改图层失败", e);
		}
	}

	public void deleteLayers(String[] layerIds) {
		try {
			for (int i = 0; i < layerIds.length; i++) {
				Long layerId = Long.parseLong(layerIds[i]);
				TLayers persistentInstance = layerAndPoiDao.findById(layerId);
				if (persistentInstance != null)
					layerAndPoiDao.delete(persistentInstance);
			}
		} catch (Exception e) {
			logger.error("删除图层失败", e);
		}
	}

	public Page<RefUserLayer> listLayerVisibleByUserId(String entCode,
			Long userId, int pageNo, int pageSize) {
		try {
			return layerAndPoiDao.listLayerVisibleByUserId(entCode, userId,
					pageNo, pageSize);
		} catch (Exception e) {
			logger.error("查看用户可以查看的图层列表失败", e);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void updateLayerVisibleByUserId(String entCode, Long userId,
			String[] visibleRefUserLayerIds) {
		try {
			List refs = layerAndPoiDao.findAllRefUserLayerByUserId(userId);
			if (refs != null && refs.size() > 0) {
				for (Iterator iterator = refs.iterator(); iterator.hasNext();) {
					RefUserLayer ref = (RefUserLayer) iterator.next();
					Long layerId = ref.getTLayers().getId();
					String refVisible = ref.getVisible();
					boolean hasContain = false;// 是否包含
					boolean hasVisible = false;// 是否已可见
					for (int i = 0; i < visibleRefUserLayerIds.length; i++) {
						if (visibleRefUserLayerIds[i] == "")
							continue;
						Long visibleRefUserLayerId = Long
								.parseLong(visibleRefUserLayerIds[i]);
						if (visibleRefUserLayerId.intValue() == layerId
								.intValue()) {
							hasContain = true;
							if (refVisible.equals("1")) {
								hasVisible = true;
								break;
							}
						}
					}
					// 如果已包含都设置为可见
					if (hasContain) {
						if (hasVisible) {// 已可见
						} else {// 未可见设置为可见
							ref.setVisible("1");
							layerAndPoiDao.updateRefUserLayerVisible(ref);
						}
					} else {// 未包含的都设置为不可见
						if (!refVisible.equals("0")) {// 非不可见设置为不可见
							ref.setVisible("0");
							layerAndPoiDao.updateRefUserLayerVisible(ref);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("设置用户可以查看的图层列表的可见性失败", e);
		}
	}

	public Page<TPoi> listNotRefPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue) {
		try {
			return layerAndPoiDao.listNotRefPoiCreateByUserId(entCode, userId,
					pageNo, pageSize, searchValue);
		} catch (Exception e) {
			logger.error("查询用户可管理图层下未绑定终端的poi列表失败", e);
			return null;
		}
	}

	public Page<Object[]> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, Date startDate,
			Date endDate) {
		try {
			return layerAndPoiDao.listPoiCreateByUserId(entCode, userId,
					pageNo, pageSize, searchValue, startDate, endDate);
		} catch (Exception e) {
			logger.error("查询用户可管理图层下poi列表失败", e);
			return null;
		}
	}

	public Page<Object[]> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue,
			String poiDescription, String layerName, String bindingStatus,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<TPoi> listPoiVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue) {
		try {
			return layerAndPoiDao.listPoiVisibleByUserId(entCode, userId,
					pageNo, pageSize, searchValue);
		} catch (Exception e) {
			logger.error("查询用户可查看的图层下poi列表失败", e);
			return null;
		}
	}

	public void addPoi(String entCode, String poiName, String poiDesc,
			String poiType, String poiDatas, String poiEncryptDatas,
			String telephone, String address, String iconpath, String visible,
			Long visitDistance, Long layerId, String[] deviceIds,
			String locDesc, String deviceId) {
		try {
			TPoi transientInstance = new TPoi();
			transientInstance.setEntcode(entCode);
			transientInstance.setPoiName(poiName);
			transientInstance.setPoiDesc(poiDesc);
			transientInstance.setPoiType(Long.parseLong(poiType));
			transientInstance.setPoiDatas(poiDatas);
			transientInstance.setPoiEncryptDatas(poiEncryptDatas);
			transientInstance.setTelephone(telephone);
			transientInstance.setAddress(address);
			transientInstance.setIconpath(iconpath);
			transientInstance.setVisible(visible);
			transientInstance.setVisitDistance(visitDistance);
			transientInstance.setCdate(Calendar.getInstance().getTime());
			transientInstance.setLocDesc(locDesc);
			transientInstance.setDeviceId(deviceId);
			//v2.1 weimeng 2012-8-31
			transientInstance.setStates(0L);
			transientInstance.setCreateOn(new Date().getTime());
			transientInstance.setLastUpdateOn(new Date().getTime());
			layerAndPoiDao.savePoi(transientInstance);
			// poi与图层关系
			Set<RefLayerPoi> refLayerPois = new HashSet<RefLayerPoi>();
			RefLayerPoi refLayerPoi = new RefLayerPoi();
			refLayerPoi.setTPoi(transientInstance);
			TLayers layer = new TLayers();
			layer.setId(layerId);
			refLayerPoi.setTLayers(layer);
			layerAndPoiDao.saveRefLayerPoi(refLayerPoi);
			refLayerPois.add(refLayerPoi);
			transientInstance.setRefLayerPois(refLayerPois);
			// poi与终端关联关系
			Set<RefTermPoi> refTermPois = new HashSet<RefTermPoi>();
			for (int i = 0; i < deviceIds.length; i++) {
				// diveceId
				String devId = deviceIds[i];
				RefTermPoi refTermPoi = new RefTermPoi();
				refTermPoi.setTPoi(transientInstance);
				TTerminal terminal = new TTerminal();
				terminal.setDeviceId(devId);
				refTermPoi.setTTerminal(terminal);
				layerAndPoiDao.saveRefTermPoi(refTermPoi);
				refTermPois.add(refTermPoi);
			}
			transientInstance.setRefTermPois(refTermPois);
		} catch (Exception e) {
			logger.error("新增poi失败", e);
		}
	}

	public void addPoi(TPoi tPoi, Long layerId, String[] deviceIds) {
		addPoi(tPoi.getEntcode(), tPoi.getPoiName(), tPoi.getPoiDesc(), tPoi
				.getPoiType().toString(), tPoi.getPoiDatas(),
				tPoi.getPoiEncryptDatas(), tPoi.getTelephone(),
				tPoi.getAddress(), tPoi.getIconpath(), tPoi.getVisible(),
				tPoi.getVisitDistance(), layerId, deviceIds, tPoi.getLocDesc(),
				tPoi.getDeviceId());
	}

	@SuppressWarnings("unchecked")
	public void updatePoi(Long id, String entCode, String poiName,
			String poiDesc, String poiType, String poiDatas,
			String poiEncryptDatas, String telephone, String address,
			String iconpath, String visible, Long visitDistance, Long layerId,
			String[] deviceIds) {
		try {
			TPoi instance = layerAndPoiDao.queryPoi(id);
			if (instance != null) {
				// instance.setEntcode(entCode);
				instance.setPoiName(poiName);
				instance.setPoiDesc(poiDesc);
				instance.setPoiType(Long.parseLong(poiType));
				instance.setPoiDatas(poiDatas);
				instance.setPoiEncryptDatas(poiEncryptDatas);
				instance.setTelephone(telephone);
				instance.setAddress(address);
				instance.setIconpath(iconpath);
				instance.setVisible(visible);
				instance.setVisitDistance(visitDistance);
				//v2.1 weimeng 2012-9-1
				instance.setLastUpdateOn(new Date().getTime());
				layerAndPoiDao.attachDirtyPoi(instance);
				// poi与图层关系
				Set<RefLayerPoi> refLayerPois = instance.getRefLayerPois();
				RefLayerPoi refLayerPoi = refLayerPois.iterator().next();
				if (refLayerPoi.getTLayers().getId().intValue() != layerId
						.intValue()) {
					TLayers layer = new TLayers();
					layer.setId(layerId);
					refLayerPoi.setTLayers(layer);
					layerAndPoiDao.updateRefLayerPoi(refLayerPoi);
					refLayerPois.add(refLayerPoi);
				}
				instance.setRefLayerPois(refLayerPois);
				// 先删除poi与终端关联关系
				layerAndPoiDao.deleteRefTermPoisByPoiId(id);
				// poi与终端关联关系
				Set<RefTermPoi> refTermPois = instance.getRefTermPois();
				refTermPois.clear();
				for (int i = 0; i < deviceIds.length; i++) {
					String deviceId = deviceIds[i];
					RefTermPoi refTermPoi = new RefTermPoi();
					refTermPoi.setTPoi(instance);
					TTerminal terminal = new TTerminal();
					terminal.setDeviceId(deviceId);
					refTermPoi.setTTerminal(terminal);
					layerAndPoiDao.saveRefTermPoi(refTermPoi);
					refTermPois.add(refTermPoi);
				}
				instance.setRefTermPois(refTermPois);
			}
		} catch (Exception e) {
			logger.error("修改 poi失败", e);
		}
	}

	// add by magiejue 2010-12-7批量修改标注点
	/*
	 * public void updatePoiBatch(List<Long> poiIds, String poiLayerId, String
	 * visitDistance, String iconpath) { try { // 修改样式 if (iconpath != null &&
	 * iconpath != "") { for (int i = 0; i < poiIds.size(); i++) { TPoi instance
	 * = layerAndPoiDao.queryPoi(poiIds.get(i)); if (instance != null) {
	 * instance.setIconpath(iconpath); layerAndPoiDao.attachDirtyPoi(instance);
	 * } } } } catch (Exception e) { logger.error("批量修改 poi样式失败", e); }
	 * 
	 * try { // 修改范围 if (visitDistance != null && visitDistance != "") { for
	 * (int i = 0; i < poiIds.size(); i++) { TPoi instance =
	 * layerAndPoiDao.queryPoi(poiIds.get(i)); if (instance != null) {
	 * instance.setVisitDistance(Long.valueOf(visitDistance));
	 * layerAndPoiDao.attachDirtyPoi(instance); } } } } catch (Exception e) {
	 * logger.error("批量修改 poi范围失败", e); } try { // 修改图层 if (poiLayerId != null
	 * && poiLayerId != "") { for (int i = 0; i < poiIds.size(); i++) { TPoi
	 * instance = layerAndPoiDao.queryPoi(poiIds.get(i)); if (instance != null)
	 * { // poi与图层关系 Set<RefLayerPoi> refLayerPois = instance.getRefLayerPois();
	 * RefLayerPoi refLayerPoi = refLayerPois.iterator().next(); if
	 * (refLayerPoi.getTLayers().getId().intValue() != Integer
	 * .parseInt(poiLayerId)) { TLayers layer = new TLayers();
	 * layer.setId(Long.valueOf(poiLayerId)); refLayerPoi.setTLayers(layer);
	 * layerAndPoiDao.updateRefLayerPoi(refLayerPoi);
	 * refLayerPois.add(refLayerPoi); } instance.setRefLayerPois(refLayerPois);
	 * } } } } catch (Exception e) { logger.error("批量修改 poi图层失败", e); } }
	 */
	/**
	 * 2011-11-22 weimeng 批量修改
	 */
	public boolean updatePoiBatch(String poiIds, String poiLayerId,
			String visitDistance, String iconpath) {
		try {
			// 修改样式
			if (iconpath != null && iconpath != "") {
				layerAndPoiDao.updatePoiBatchIconpath(poiIds, iconpath);
			}
		} catch (Exception e) {
			logger.error("批量修改 poi样式失败", e);
			return false;
		}

		try {
			// 修改范围
			if (visitDistance != null && visitDistance != "") {
				layerAndPoiDao.updatePoiBatchVisitDistance(poiIds,
						visitDistance);
			}
		} catch (Exception e) {
			logger.error("批量修改 poi范围失败", e);
			return false;
		}
		try {
			// 修改图层
			if (poiLayerId != null && poiLayerId != "") {
				layerAndPoiDao.updatePoiBatchLayerId(poiIds, poiLayerId);
			}
		} catch (Exception e) {
			logger.error("批量修改 poi图层失败", e);
			return false;
		}
		return true;
	}

	public void updatePoi(Long id, String entCode, String poiName,
			String poiDesc, String poiType, String poiDatas,
			String poiEncryptDatas, String telephone, String address,
			String iconpath, String visible, Long visitDistance) {
		try {
			TPoi instance = layerAndPoiDao.findPoiById(id);
			if (instance != null) {
				// instance.setEntcode(entCode);
				instance.setPoiName(poiName);
				instance.setPoiDesc(poiDesc);
				instance.setPoiType(Long.parseLong(poiType));
				instance.setPoiDatas(poiDatas);
				instance.setPoiEncryptDatas(poiEncryptDatas);
				instance.setTelephone(telephone);
				instance.setAddress(address);
				instance.setIconpath(iconpath);
				instance.setVisible(visible);
				instance.setVisitDistance(visitDistance);
				//v2.1 weimeng 2012-9-1
				instance.setLastUpdateOn(new Date().getTime());
				layerAndPoiDao.attachDirtyPoi(instance);
			}
		} catch (Exception e) {
			logger.error("修改 poi属性失败", e);
		}
	}

	public void deletePois(String[] poiIds) {
		try {
			for (int i = 0; i < poiIds.length; i++) {
				Long poiId = Long.parseLong(poiIds[i]);
				TPoi persistentInstance = layerAndPoiDao.findPoiById(poiId);
				//if (persistentInstance != null)
				//	layerAndPoiDao.deletePoi(persistentInstance);
				//v2.1 weimeng 2012-9-1
				if (persistentInstance != null){
					persistentInstance.setStates(1L);
					persistentInstance.setLastUpdateOn(new Date().getTime());
					layerAndPoiDao.attachDirtyPoi(persistentInstance);
				}
			}
		} catch (Exception e) {
			logger.error("删除poi失败", e);
		}
	}

	public TPoi queryPoi(String id) {
		try {
			Long poiId = Long.parseLong(id);
			return layerAndPoiDao.queryPoi(poiId);
		} catch (Exception e) {
			logger.error("查询poi信息失败", e);
			return null;
		}
	}

	public Page<TLayers> listVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize) {
		try {
			return layerAndPoiDao.listVisibleByUserId(entCode, userId, pageNo,
					pageSize);
		} catch (Exception e) {
			logger.error("查询可见图层列表失败", e);
			return null;
		}
	}

	public Page<TLayers> listVisibleAndPoiByUserId(String entCode, Long userId,
			int pageNo, int pageSize) {
		try {
			return layerAndPoiDao.listVisibleAndPoiByUserId(entCode, userId,
					pageNo, pageSize);
		} catch (Exception e) {
			logger.error("查询可见图层以及poi列表失败", e);
			return null;
		}
	}

	public Page<TPoi> queryPoiByDeviceId(String deviceId, Long userId) {
		try {
			return layerAndPoiDao.queryPoiByDeviceId(deviceId, userId);
		} catch (Exception e) {
			logger.error("根据终端id查poi列表失败", e);
			return null;
		}
	}

	public Page<TPoi> queryManagePoiByDeviceId(String deviceId, Long userId) {
		try {
			return layerAndPoiDao.queryManagePoiByDeviceId(deviceId, userId);
		} catch (Exception e) {
			logger.error("根据终端id查poi列表(过滤用户可管理的图层)失败", e);
			return null;
		}
	}

	public void updateRefTerm(String[] poiIds, String[] deviceIds) {
		try {
			for (int j = 0; j < deviceIds.length; j++) {
				String deviceId = deviceIds[j];
				// 删除终端与poi关联关系
				layerAndPoiDao.deleteRefTermPoisByDeviceId(deviceId);
				Set<RefTermPoi> refTermPois = new HashSet<RefTermPoi>();
				for (int i = 0; i < poiIds.length; i++) {
					String poiId = poiIds[i];
					Long id = CharTools.str2Long(poiId, 0L);
					TPoi poi = new TPoi();
					poi.setId(id);
					RefTermPoi refTermPoi = new RefTermPoi();
					refTermPoi.setTPoi(poi);
					TTerminal terminal = new TTerminal();
					terminal.setDeviceId(deviceId);
					refTermPoi.setTTerminal(terminal);
					refTermPois.add(refTermPoi);
				}
				layerAndPoiDao.saveRefTermPoi(refTermPois);
			}
		} catch (Exception e) {
			logger.error("修改 poi失败", e);
		}
	}

	public void deleteRefTerm(String[] deviceIds) {
		try {
			for (int j = 0; j < deviceIds.length; j++) {
				String deviceId = deviceIds[j];
				// 删除终端与poi关联关系
				layerAndPoiDao.deleteRefTermPoisByDeviceId(deviceId);
			}
		} catch (Exception e) {
			logger.error("删除终端所有绑定poi关系失败", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void updateRefTermForAdd(String[] poiIds, String[] deviceIds) {
		try {
			// 查询已绑定关系
			List list = layerAndPoiDao.listRefs(poiIds, deviceIds);
			for (int j = 0; j < deviceIds.length; j++) {
				String deviceId = deviceIds[j];
				Set<RefTermPoi> refTermPois = new HashSet<RefTermPoi>();
				for (int i = 0; i < poiIds.length; i++) {
					String poiId = poiIds[i];
					// 判断是否已绑定
					if (list != null) {
						boolean isHas = false;
						for (Iterator iterator = list.iterator(); iterator
								.hasNext();) {
							RefTermPoi object = (RefTermPoi) iterator.next();
							if (object == null)
								continue;
							String deviceId2 = object.getTTerminal()
									.getDeviceId();
							Long poiId2 = object.getTPoi().getId();
							if (deviceId.equals(deviceId2)
									&& String.valueOf(poiId2).equals(poiId)) {
								isHas = true;
								break;
							}
						}
						if (isHas)
							continue;
					}
					Long id = CharTools.str2Long(poiId, 0L);
					TPoi poi = new TPoi();
					poi.setId(id);
					RefTermPoi refTermPoi = new RefTermPoi();
					refTermPoi.setTPoi(poi);
					TTerminal terminal = new TTerminal();
					terminal.setDeviceId(deviceId);
					refTermPoi.setTTerminal(terminal);
					refTermPois.add(refTermPoi);
				}
				layerAndPoiDao.saveRefTermPoi(refTermPois);
			}
		} catch (Exception e) {
			logger.error("修改 poi失败", e);
		}
	}

	public void updateBinding(String[] poiIds, String[] deviceIds,
			int operation_flag) {
		if (operation_flag == add_binding) {
			layerAndPoiDao.saveRefTermPoi(poiIds, deviceIds);
		} else if (operation_flag == reomve_binding) {
			layerAndPoiDao.deleteRefTermPois(poiIds, deviceIds);
		}
	}

	// add by magiejue 2010-12-7 查询用户所有标注点（不分页）
	public List<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			String searchValue, String poiLayer, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return layerAndPoiDao.listPoiCreateByUserId(entCode, userId,
				searchValue, poiLayer, startDate, endDate);
	}

	// 标注点校验 add by zhaofeng
	public List<TPoi> listPoiByPoiName(String entCode, String tmpPoiName) {
		return layerAndPoiDao.listPoiByPoiName(entCode, tmpPoiName);
	}

	// 查询所有标注点
	public Page<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String poiLayer,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return layerAndPoiDao.listPoiCreateByUserId(entCode, userId, pageNo,
				pageSize, searchValue, poiLayer, startDate, endDate);
	}

    public int checkPoi(String layerIds) {
        return layerAndPoiDao.checkPoi(layerIds);
    }
}
