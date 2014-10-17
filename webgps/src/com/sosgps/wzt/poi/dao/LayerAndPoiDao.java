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
 * @Title:poi���ݲ�ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-10 ����03:36:20
 */
public interface LayerAndPoiDao {
	// sos�鿴���û�������ͼ���б�
	public Page<TLayers> listLayerCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos��ѯͼ����Ϣ
	public TLayers queryLayer(Long layerId);

	public void save(TLayers transientInstance);

	public void delete(TLayers persistentInstance);

	public void attachDirty(TLayers instance);

	public TLayers findById(java.lang.Long id);

	// sos�����û��ɼ�ͼ���ϵ
	public void saveRefUserLayer(RefUserLayer refUserLayer);

	public void deleteRefUserLayersByLayerId(Long layerId);

	// sos�鿴�û����Բ鿴��ͼ���б�
	public Page<RefUserLayer> listLayerVisibleByUserId(String entCode,
			Long userId, int pageNo, int pageSize);

	// sos��ѯ�û����Բ鿴������ͼ���ϵ�б�
	@SuppressWarnings("rawtypes")
	public List findAllRefUserLayerByUserId(Long userId);

	// sos�޸��û�ͼ��ɼ���
	public void updateRefUserLayerVisible(RefUserLayer instance);

	// sos��ѯ�û��ɹ���ͼ����poi�б�
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

	// sos��ѯ�û��ɹ���ͼ����δ���ն˵�poi�б�
	public Page<TPoi> listNotRefPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos��ѯ�û��ɲ鿴��ͼ����poi�б�
	public Page<TPoi> listPoiVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos��ѯpoi
	public TPoi findPoiById(java.lang.Long id);

	// sos����poi
	public void savePoi(TPoi transientInstance);

	// sos�޸�poi
	public void attachDirtyPoi(TPoi instance);

	// sosɾ��poi
	public void deletePoi(TPoi persistentInstance);

	// sosɾ��poi��ͼ���ϵ
	public void deleteRefLayerPoisByPoiId(Long poiId);

	// sosɾ��poi���ն˹�����ϵ
	public void deleteRefTermPoisByPoiId(Long poiId);

	// sosɾ��poi���ն˹�����ϵ
	public void deleteRefTermPoisByDeviceId(String deviceId);

	// sos����poi��ͼ���ϵ
	public void saveRefLayerPoi(RefLayerPoi refLayerPoi);

	// sos�޸�poi��ͼ���ϵ
	public void updateRefLayerPoi(RefLayerPoi refLayerPoi);

	// sos����poi���ն˹�ϵ
	public void saveRefTermPoi(RefTermPoi refTermPoi);

	// sos��������poi���ն˹�ϵ
	public void saveRefTermPoi(Set<RefTermPoi> refTermPois);

	// sos��ѯpoi��Ϣ
	public TPoi queryPoi(Long poiId);

	// sos�鿴���û��ɼ���ͼ���б�
	public Page<TLayers> listVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize);

	// sos�鿴���û��ɼ���ͼ���Լ�poi�б�
	public Page<TLayers> listVisibleAndPoiByUserId(String entCode, Long userId,
			int pageNo, int pageSize);

	// sos��ѯ�ն˹�����poi�б�(�����û��ɼ���ͼ��)
	public Page<TPoi> queryPoiByDeviceId(String deviceId, Long userId);

	// sos��ѯ�ն˹�����poi�б�(�����û��ɹ����ͼ��)
	public Page<TPoi> queryManagePoiByDeviceId(String deviceId, Long userId);

	// sosɾ����ҵ������ͼ��
	public void deleteLayersByEntCode(String entCode);

	// sos��ѯ�Ѱ󶨵Ĺ�ϵ
	@SuppressWarnings("rawtypes")
	public List listRefs(String[] poiIds, String[] deviceIds);

	// add by magiejue 2010-12-7 ��ѯ�û����б�ע�㣨����ҳ��
	public List<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			String searchValue, String poiLayer, Date startDate, Date endDate);

	// add by zhaofeng 2011-10-24��ѯ�û����б�ע�㣨��ҳ��
	public Page<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String poiLayer,
			Date startDate, Date endDate);

	void saveRefTermPoi(String[] poiIds, String[] deviceIds);

	void deleteRefTermPois(String[] poiIds, String[] deviceIds);

	// add by magiejue 2010-12-7 ��ѯ�û����б�ע�㣨����ҳ��
	public List<TPoi> listPoiByPoiName(String entCode, String tmpPoiName);

	/**
	 * 2011-11-22 weimeng ��ע�����޸�ͼ��,�����ύ����
	 */
	public void updatePoiBatchLayerId(final String poiIds, final String layerId);

	/**
	 * 2011-11-22 weimeng ��ע�����޸ķ�Χ,�����ύ����
	 */
	public void updatePoiBatchVisitDistance(final String poiIds,
			final String visitDistance);

	/**
	 * 2011-11-22 weimeng ��ע�����޸�ͼ��,�����ύ����
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
