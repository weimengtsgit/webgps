package com.sosgps.wzt.poi.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefLayerPoi;
import com.sosgps.wzt.orm.RefTermPoi;
import com.sosgps.wzt.orm.RefUserLayer;
import com.sosgps.wzt.orm.TLayers;
import com.sosgps.wzt.orm.TPoi;

/**
 * @Title:poi数据层接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-10 下午03:36:20
 */
public interface LayerAndPoiDao {
	// sos查看此用户创建的图层列表
	public Page<TLayers> listLayerCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos查询图层信息
	public TLayers queryLayer(Long layerId);

	public void save(TLayers transientInstance);

	public void delete(TLayers persistentInstance);

	public void attachDirty(TLayers instance);

	public TLayers findById(java.lang.Long id);

	// sos新增用户可见图层关系
	public void saveRefUserLayer(RefUserLayer refUserLayer);

	public void deleteRefUserLayersByLayerId(Long layerId);

	// sos查看用户可以查看的图层列表
	public Page<RefUserLayer> listLayerVisibleByUserId(String entCode,
			Long userId, int pageNo, int pageSize);

	// sos查询用户可以查看的所有图层关系列表
	@SuppressWarnings("rawtypes")
	public List findAllRefUserLayerByUserId(Long userId);

	// sos修改用户图层可见性
	public void updateRefUserLayerVisible(RefUserLayer instance);

	// sos查询用户可管理图层下poi列表
	public Page<Object[]> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, Date startDate,
			Date endDate);

	Page<Object[]> queryMatchingPoi(String sql, int start, int limit);

	Page<Object[]> queryMatchingPoi(String entCode,long userId, int start, int limit,
			String poiName, String poiDescription, String locDescription,
			Integer layerId, String terminalName, String[] terminalGroupIds,
			Date startDate, Date endDate);

	Page<Object[]> queryBindingPoi(String entCode,long userId, int start, int limit,
			String poiName, String poiDescription, String locDescription,
			Integer layerId, String terminalName, String[] terminalGroupIds,
			Date startDate, Date endDate);

	Page<Object[]> queryNotBindingPoi(String entCode,long userId, int start, int limit,
			String poiName, String poiDescription, String locDescription,
			Integer layerId, String terminalName, String[] terminalGroupIds,
			Date startDate, Date endDate);

	// sos查询用户可管理图层下未绑定终端的poi列表
	public Page<TPoi> listNotRefPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos查询用户可查看的图层下poi列表
	public Page<TPoi> listPoiVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos查询poi
	public TPoi findPoiById(java.lang.Long id);

	// sos新增poi
	public void savePoi(TPoi transientInstance);

	// sos修改poi
	public void attachDirtyPoi(TPoi instance);

	// sos删除poi
	public void deletePoi(TPoi persistentInstance);

	// sos删除poi与图层关系
	public void deleteRefLayerPoisByPoiId(Long poiId);

	// sos删除poi与终端关联关系
	public void deleteRefTermPoisByPoiId(Long poiId);

	// sos删除poi与终端关联关系
	public void deleteRefTermPoisByDeviceId(String deviceId);

	// sos新增poi与图层关系
	public void saveRefLayerPoi(RefLayerPoi refLayerPoi);

	// sos修改poi与图层关系
	public void updateRefLayerPoi(RefLayerPoi refLayerPoi);

	// sos新增poi与终端关系
	public void saveRefTermPoi(RefTermPoi refTermPoi);

	// sos批量新增poi与终端关系
	public void saveRefTermPoi(Set<RefTermPoi> refTermPois);

	// sos查询poi信息
	public TPoi queryPoi(Long poiId);

	// sos查看此用户可见的图层列表
	public Page<TLayers> listVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize);

	// sos查看此用户可见的图层以及poi列表
	public Page<TLayers> listVisibleAndPoiByUserId(String entCode, Long userId,
			int pageNo, int pageSize);

	// sos查询终端关联的poi列表(过滤用户可见的图层)
	public Page<TPoi> queryPoiByDeviceId(String deviceId, Long userId);

	// sos查询终端关联的poi列表(过滤用户可管理的图层)
	public Page<TPoi> queryManagePoiByDeviceId(String deviceId, Long userId);

	// sos删除企业下所有图层
	public void deleteLayersByEntCode(String entCode);

	// sos查询已绑定的关系
	@SuppressWarnings("rawtypes")
	public List listRefs(String[] poiIds, String[] deviceIds);

	// add by magiejue 2010-12-7 查询用户所有标注点（不分页）
	public List<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			String searchValue, String poiLayer, Date startDate, Date endDate);

	// add by zhaofeng 2011-10-24查询用户所有标注点（分页）
	public Page<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String poiLayer,
			Date startDate, Date endDate);

	void saveRefTermPoi(String[] poiIds, String[] deviceIds);

	void deleteRefTermPois(String[] poiIds, String[] deviceIds);

	// add by magiejue 2010-12-7 查询用户所有标注点（不分页）
	public List<TPoi> listPoiByPoiName(String entCode, String tmpPoiName);

	/**
	 * 2011-11-22 weimeng 标注批量修改图层,批量提交方法
	 */
	public void updatePoiBatchLayerId(final String poiIds, final String layerId);

	/**
	 * 2011-11-22 weimeng 标注批量修改范围,批量提交方法
	 */
	public void updatePoiBatchVisitDistance(final String poiIds,
			final String visitDistance);

	/**
	 * 2011-11-22 weimeng 标注批量修改图标,批量提交方法
	 */
	public void updatePoiBatchIconpath(final String poiIds,
			final String iconpath);
	
	/**
	 * v2.1
	 * @param entCode
	 * @return
	 */
	public Long queryPoiCount(final String entCode);
	
	public int checkPoi(String layerIds);

}
