package com.sosgps.wzt.poi.service;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefUserLayer;
import com.sosgps.wzt.orm.TLayers;
import com.sosgps.wzt.orm.TPoi;

/**
 * @Title:ͼ��poiҵ���ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-10 ����03:34:56
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

	// sos�鿴���û�������ͼ���б�
	public Page<TLayers> listLayerCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos��ѯͼ����Ϣ
	public TLayers queryLayer(String id);

	// sos����ͼ��
	public void addLayer(String entCode, String layerName, String layerDesc,
			Long userId, String visible, String mapLevel, String[] userIds);

	// sos�޸�ͼ��
	public void updateLayer(Long id, String entCode, String layerName,
			String layerDesc, Long userId, String visible, String mapLevel,
			String[] userIds);

	// sosɾ��ͼ��
	public void deleteLayers(String[] layerIds);

	// sos�鿴�û����Բ鿴��ͼ���б�
	public Page<RefUserLayer> listLayerVisibleByUserId(String entCode,
			Long userId, int pageNo, int pageSize);

	// sos�����û����Բ鿴��ͼ���б�Ŀɼ���
	public void updateLayerVisibleByUserId(String entCode, Long userId,
			String[] visibleRefUserLayerIds);

	// sos��ѯ�û��ɹ���ͼ����poi�б�
	public Page<Object[]> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, Date startDate,
			Date endDate);

	// sos��ѯ�û��ɹ���ͼ����poi�б�
	public Page<Object[]> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue,
			String poiDescription, String layerName, String bindingStatus,
			Date startDate, Date endDate);

	// sos��ѯ�û��ɹ���ͼ����δ���ն˵�poi�б�
	public Page<TPoi> listNotRefPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos��ѯ�û��ɲ鿴��ͼ����poi�б�
	public Page<TPoi> listPoiVisibleByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue);

	// sos����poi
	public void addPoi(String entCode, String poiName, String poiDesc,
			String poiType, String poiDatas, String poiEncryptDatas,
			String telephone, String address, String iconpath, String visible,
			Long visitDistance, Long layerId, String[] deviceIds,
			String locDesc, String deviceId);

	public void addPoi(TPoi tPoi, Long layerId, String[] deviceIds);

	// sos�޸�poi
	public void updatePoi(Long id, String entCode, String poiName,
			String poiDesc, String poiType, String poiDatas,
			String poiEncryptDatas, String telephone, String address,
			String iconpath, String visible, Long visitDistance, Long layerId,
			String[] deviceIds);

	// sos�޸�poi
	public void updatePoi(Long id, String entCode, String poiName,
			String poiDesc, String poiType, String poiDatas,
			String poiEncryptDatas, String telephone, String address,
			String iconpath, String visible, Long visitDistance);

	// sosɾ��poi
	public void deletePois(String[] poiIds);

	// sos��ѯpoi��Ϣ
	public TPoi queryPoi(String id);

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

	// sos�����޸���Ȥ��󶨹�ϵ
	public void updateRefTerm(String[] poiIds, String[] deviceIds);

	// sosɾ���ն˵����а󶨹�ϵ
	public void deleteRefTerm(String[] deviceIds);

	// sos����׷���޸���Ȥ��󶨹�ϵ
	public void updateRefTermForAdd(String[] poiIds, String[] deviceIds);

	void updateBinding(String[] poiIds, String[] deviceIds, int operation_flag);

	// add by magiejue 2010-12-7 ��ѯ�û����б�ע�㣨����ҳ��
	public List<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			String searchValue, String poiLayer, Date startDate, Date endDate);

	// add by magiejue 2010-12-7 �����޸ı�ע��
	// public void updatePoiBatch(List<Long> poiIds, String poiLayerId, String
	// visitDistance,
	// String iconpath);
	public boolean updatePoiBatch(String poiIds, String poiLayerId,
			String visitDistance, String iconpath);

	// add by zhaofeng 2011-10-19��ѯ��ע��(��֤�Ƿ����ظ���ע��)
	public List<TPoi> listPoiByPoiName(String entCode, String tmpPoiName);

	// add by zhaofeng 2011-10-24 ��ѯ�û����б�ע�㣨��ҳ��
	public Page<TPoi> listPoiCreateByUserId(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String poiLayer,
			Date startDate, Date endDate);
	public int checkPoi(String layerIds);
}
