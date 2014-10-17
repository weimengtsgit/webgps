package com.sosgps.wzt.poi.service;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefUserLayer;
import com.sosgps.wzt.orm.TLayers;
import com.sosgps.wzt.orm.TPoi;

/**
 * @Title:图层poi业务层接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-10 下午03:34:56
 */
public interface LayerAndPoiService {

	final int binding_ignore = 0;

	final int binding_no = -1;

	final int binding_yes = 1;

	final int add_binding = 1;

	final int reomve_binding = 0;

	public Page<Object[]> listMatchingPois(String entCode, long userId,
			int pageNo, int pageSize, int bindingStatus, String poiName,
			String poiDescription, String locDescription, Integer layerId,
			String terminalName, String[] tGroups, Date startDate, Date endDate);

	public Page<Object[]> listMatchingPoi(String entCode, Long userId,
			int pageNo, int pageSize, int bindingStatus, String poiName,
			String poiDescription, String locDescription, Integer layerId,
			String terminalName, String[] tGroups, Date startDate, Date endDate);

	// sos查看此用户创建的图层列表
	public Page<TLayers> listLayerCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos查询图层信息
	public TLayers queryLayer(String id);

	// sos新增图层
	public void addLayer(String entCode, String layerName, String layerDesc,
			Long userId, String visible, String mapLevel, String[] userIds);

	// sos修改图层
	public void updateLayer(Long id, String entCode, String layerName,
			String layerDesc, Long userId, String visible, String mapLevel,
			String[] userIds);

	// sos删除图层
	public void deleteLayers(String[] layerIds);

	// sos查看用户可以查看的图层列表
	public Page<RefUserLayer> listLayerVisibleByUserId(String entCode,
			Long userId, int pageNo, int pageSize);

	// sos设置用户可以查看的图层列表的可见性
	public void updateLayerVisibleByUserId(String entCode, Long userId,
			String[] visibleRefUserLayerIds);

	// sos查询用户可管理图层下poi列表
	public Page<Object[]> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, Date startDate,
			Date endDate);

	// sos查询用户可管理图层下poi列表
	public Page<Object[]> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue,
			String poiDescription, String layerName, String bindingStatus,
			Date startDate, Date endDate);

	// sos查询用户可管理图层下未绑定终端的poi列表
	public Page<TPoi> listNotRefPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos查询用户可查看的图层下poi列表
	public Page<TPoi> listPoiVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos新增poi
	public void addPoi(String entCode, String poiName, String poiDesc,
			String poiType, String poiDatas, String poiEncryptDatas,
			String telephone, String address, String iconpath, String visible,
			Long visitDistance, Long layerId, String[] deviceIds,
			String locDesc, String deviceId);

	public void addPoi(TPoi tPoi, Long layerId, String[] deviceIds);

	// sos修改poi
	public void updatePoi(Long id, String entCode, String poiName,
			String poiDesc, String poiType, String poiDatas,
			String poiEncryptDatas, String telephone, String address,
			String iconpath, String visible, Long visitDistance, Long layerId,
			String[] deviceIds);

	// sos修改poi
	public void updatePoi(Long id, String entCode, String poiName,
			String poiDesc, String poiType, String poiDatas,
			String poiEncryptDatas, String telephone, String address,
			String iconpath, String visible, Long visitDistance);

	// sos删除poi
	public void deletePois(String[] poiIds);

	// sos查询poi信息
	public TPoi queryPoi(String id);

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

	// sos批量修改兴趣点绑定关系
	public void updateRefTerm(String[] poiIds, String[] deviceIds);

	// sos删除终端的所有绑定关系
	public void deleteRefTerm(String[] deviceIds);

	// sos批量追加修改兴趣点绑定关系
	public void updateRefTermForAdd(String[] poiIds, String[] deviceIds);

	void updateBinding(String[] poiIds, String[] deviceIds, int operation_flag);

	// add by magiejue 2010-12-7 查询用户所有标注点（不分页）
	public List<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			String searchValue, String poiLayer, Date startDate, Date endDate);

	// add by magiejue 2010-12-7 批量修改标注点
	// public void updatePoiBatch(List<Long> poiIds, String poiLayerId, String
	// visitDistance,
	// String iconpath);
	public boolean updatePoiBatch(String poiIds, String poiLayerId,
			String visitDistance, String iconpath);

	// add by zhaofeng 2011-10-19查询标注点(验证是否有重复标注点)
	public List<TPoi> listPoiByPoiName(String entCode, String tmpPoiName);

	// add by zhaofeng 2011-10-24 查询用户所有标注点（分页）
	public Page<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String poiLayer,
			Date startDate, Date endDate);
	public int checkPoi(String layerIds);
}
