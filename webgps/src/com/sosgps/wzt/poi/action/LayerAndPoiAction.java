package com.sosgps.wzt.poi.action;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.mapabc.geom.CoordCvtAPI;
import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefLayerPoi;
import com.sosgps.wzt.orm.RefTermPoi;
import com.sosgps.wzt.orm.RefUserLayer;
import com.sosgps.wzt.orm.TLayers;
import com.sosgps.wzt.orm.TPoi;
import com.sosgps.wzt.poi.service.LayerAndPoiService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.StringUtility;

/**
 * @Title:poiչ�ֲ�action
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-10 ����03:30:32
 */
public class LayerAndPoiAction extends DispatchWebActionSupport {
	LayerAndPoiService layerAndPoiService = (LayerAndPoiService) SpringHelper
			.getBean("LayerAndPoiServiceImpl");
	private static final Logger logger = Logger.getLogger(LayerAndPoiAction.class);
	
	public ActionForward listLayerIds(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:'9'}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		int startint = start != null ? Integer.parseInt(start) : 0;
		int limitint = limit != null ? Integer.parseInt(limit) : 100;
		StringBuffer jsonSb = new StringBuffer();
		Long userId = userInfo.getUserId();
		Page<TLayers> list = layerAndPoiService.listVisibleByUserId(entCode,
				userId, startint / limitint + 1, limitint);
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			jsonSb.append("[");
			for (Iterator<?> iterator = list.getResult().iterator(); iterator
					.hasNext();) {
				TLayers layer = (TLayers) iterator.next();
				jsonSb.append("['").append(layer.getId()).append("','")
						.append(layer.getLayerName()).append("'],");
			}
			jsonSb.deleteCharAt(jsonSb.length() - 1).append("]");
		}
		response.getWriter().write(jsonSb.toString());
		return mapping.findForward(null);
	}

	// sosͼ���б�
	@SuppressWarnings("rawtypes")
	public ActionForward listLayer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Long userId = userInfo.getUserId();
			Page<TLayers> list = layerAndPoiService.listLayerCreateByUserId(
					entCode, userId, page, limitint, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					TLayers layer = (TLayers) iterator.next();
					jsonSb.append("{");
					jsonSb.append("id:'" + layer.getId() + "',");// id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layer.getLayerName())
							+ "',");// ͼ������
					jsonSb.append("layerDesc:'"
							+ CharTools.javaScriptEscape(layer.getLayerDesc())
							+ "',");// ͼ������
					jsonSb.append("mapLevel:'" + layer.getInfo1() + "',");// ͼ���ͼ����
					jsonSb.append("visible:'" + layer.getVisible() + "'");// �Ƿ�ɼ�0���ɼ�1�ɼ�
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	@SuppressWarnings("rawtypes")
	public ActionForward comboboxListLayer(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		start = "0";
		limit = "10000";
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		// int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Long userId = userInfo.getUserId();
			Page<TLayers> list = layerAndPoiService.listLayerCreateByUserId(
					entCode, userId, page, limitint, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				// total = list.getTotalCount();
				for (Iterator iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					TLayers layer = (TLayers) iterator.next();
					jsonSb.append("[");
					jsonSb.append("'" + layer.getId() + "',");// id
					jsonSb.append("'"
							+ CharTools.javaScriptEscape(layer.getLayerName())
							+ "'");// ͼ������
					jsonSb.append("],");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}

		// System.out.println("[" + jsonSb.toString() + "]");
		response.getWriter().write("[" + jsonSb.toString() + "]");

		return mapping.findForward(null);
	}

	// sos��ѯͼ����Ϣ
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward queryLayer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String id = request.getParameter("id");// ͼ��id

		TLayers layer = layerAndPoiService.queryLayer(id);
		if (layer == null) {
			response.getWriter().write("{result:\"2\"}");// ��ѯʧ��
			return mapping.findForward(null);
		}
		Set<RefUserLayer> refUserLayers = layer.getRefUserLayers();
		String visibleUserIds = "";
		for (Iterator iterator = refUserLayers.iterator(); iterator.hasNext();) {
			RefUserLayer refUserLayer = (RefUserLayer) iterator.next();
			Long userId = refUserLayer.getUserId();
			if (userId.intValue() != userInfo.getUserId().intValue()) {
				visibleUserIds += userId + ",";
			}
		}
		if (visibleUserIds.length() > 0) {
			visibleUserIds = visibleUserIds.substring(0,
					visibleUserIds.length() - 1);
		}
		StringBuffer jsonSb = new StringBuffer();
		jsonSb.append("id:'" + layer.getId() + "',");// id
		jsonSb.append("visibleUserIds:'" + visibleUserIds + "'");// �ɼ���ͼ����û�id�б����������û�����
		// System.out.println("{" + jsonSb.toString() + "}");
		response.getWriter().write("{" + jsonSb.toString() + "}");
		return mapping.findForward(null);
	}

	// sos����ͼ��
	public ActionForward addLayer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String layerName = request.getParameter("layerName");// ͼ������
		String layerDesc = request.getParameter("layerDesc");// ͼ������
		String visible = request.getParameter("visible");// �Ƿ�ɼ�0���ɼ�1�ɼ�
		String mapLevel = request.getParameter("mapLevel");// ͼ���ͼ����
		String userIdss = request.getParameter("userIdss");// �ɼ��û�id�����","����
		layerName = CharTools.killNullString(layerName);
		layerName = java.net.URLDecoder.decode(layerName, "utf-8");
		layerName = CharTools.killNullString(layerName);

		layerDesc = CharTools.killNullString(layerDesc);
		layerDesc = java.net.URLDecoder.decode(layerDesc, "utf-8");
		layerDesc = CharTools.killNullString(layerDesc);

		visible = CharTools.killNullString(visible);
		mapLevel = CharTools.killNullString(mapLevel);
		userIdss = CharTools.killNullString(userIdss);

		String[] userIds = CharTools.split(userIdss, ",");
		Long userId = userInfo.getUserId();

		layerAndPoiService.addLayer(entCode, layerName, layerDesc, userId,
				visible, mapLevel, userIds);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sos�޸�ͼ��
	public ActionForward updateLayer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String id = request.getParameter("id");// ͼ��id
		String layerName = request.getParameter("layerName");// ͼ������
		String layerDesc = request.getParameter("layerDesc");// ͼ������
		String visible = request.getParameter("visible");// �Ƿ�ɼ�0���ɼ�1�ɼ�
		String mapLevel = request.getParameter("mapLevel");// ͼ���ͼ����
		String userIdss = request.getParameter("userIdss");// �ɼ��û�id�����","����
		layerName = CharTools.killNullString(layerName);
		layerName = java.net.URLDecoder.decode(layerName, "utf-8");
		layerName = CharTools.killNullString(layerName);

		layerDesc = CharTools.killNullString(layerDesc);
		layerDesc = java.net.URLDecoder.decode(layerDesc, "utf-8");
		layerDesc = CharTools.killNullString(layerDesc);

		visible = CharTools.killNullString(visible);
		mapLevel = CharTools.killNullString(mapLevel);
		userIdss = CharTools.killNullString(userIdss);

		String[] userIds = CharTools.split(userIdss, ",");
		Long userId = userInfo.getUserId();

		Long layerId = Long.parseLong(id);
		layerAndPoiService.updateLayer(layerId, entCode, layerName, layerDesc,
				userId, visible, mapLevel, userIds);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sosɾ��ͼ��
	public ActionForward deleteLayers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String ids = request.getParameter("ids");// ͼ��id�����","����
		String[] layerIds = CharTools.split(ids, ",");
		int tPoi=layerAndPoiService.checkPoi(ids);
        if(tPoi!=0){
            response.getWriter().write("{result:\"2\"}");
            return mapping.findForward(null);
        }
		layerAndPoiService.deleteLayers(layerIds);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// ��ѯ���������ı�ע��
	public ActionForward listMatchingPois(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:'9'}");// δ��¼
			return mapping.findForward(null);
		}

		String entCode = userInfo.getEmpCode();
		// Long userId = userInfo.getUserId();

		/*-------------------------��request�л�ȡ����------------------------*/
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String searchValue = request.getParameter("searchValue");
		String poiDescription = request.getParameter("poiDescription");
		String locDescription = request.getParameter("locDescription");
		String layer = request.getParameter("layer");
		String bindingStatus = request.getParameter("bindingStatus");
		String terminalName = request.getParameter("terminalName");
		String terminalGroupIds = request.getParameter("terminalGroupIds");
		// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
		String st = request.getParameter("startTime");
		System.out.println(st);
		// ��ʼʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
		String et = request.getParameter("endTime");
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����

		System.out.println(start + "  " + limit + "  " + searchValue + "  "
				+ poiDescription + "  " + layer + "  " + bindingStatus + "  "
				+ terminalName);

		/*-------------------------�Բ������д���------------------------*/
		int startInt = !StringUtility.isNullOrBlank(start)
				&& StringUtils.isNumeric(start) ? Integer.parseInt(start) : 0;
		int limitInt = !StringUtility.isNullOrBlank(limit)
				&& StringUtils.isNumeric(limit) ? Integer.parseInt(limit) : 10;
		searchValue = URLDecoder.decode(CharTools.killNullString(searchValue),
				"UTF-8");
		poiDescription = URLDecoder.decode(
				CharTools.killNullString(poiDescription), "UTF-8");
		locDescription = StringUtility.isNullOrBlank(locDescription) ? null
				: URLDecoder.decode(locDescription, "UTF-8");
		Integer layerInt = !StringUtility.isNullOrBlank(layer)
				&& StringUtils.isNumeric(layer) ? Integer.parseInt(layer)
				: null;

		int bindingStatusInt = !StringUtility.isNullOrBlank(bindingStatus)
				&& NumberUtils.isNumber(bindingStatus) ? Integer
				.parseInt(bindingStatus) : LayerAndPoiService.binding_ignore;

		terminalName = URLDecoder.decode(
				CharTools.killNullString(terminalName), "UTF-8");
		String[] tGroups = null;
		if (terminalGroupIds != null && terminalGroupIds.length() > 0) {
			tGroups = URLDecoder.decode(terminalGroupIds, "UTF-8").split(";");
		}

		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);

		expExcel = CharTools.killNullString(expExcel);

		/*---------------------------------------------------------------*/
		Page<Object[]> list = null;

		// �Ƿ񵼳�Excel
		if (expExcel.equalsIgnoreCase("true")) {

			list = layerAndPoiService.listMatchingPois(entCode,
					userInfo.getUserId(), 1, 65536, bindingStatusInt,
					searchValue, poiDescription, locDescription, layerInt,
					terminalName, tGroups, startDate, endDate);

			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {

				com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();

				ExcelWorkBook excelWorkBook = null;
				int row = 0;

				List<String> layerStrings = new ArrayList<String>();
				List<Long> layerIds = new ArrayList<Long>();
				int layerNameCount = 1;
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {

					Object[] object = iterator.next();
					String poiName = (String) object[1];
					String poiDatas = (String) object[3];
					String locDesc = (String) object[6];
					String address = (String) object[7];
					String telephone = (String) object[8];
					Long visitDistance = ((BigDecimal) object[9]).longValue();
					String cdate = (String) object[10];
					String poiDesc = (String) object[12];
					Long layerId = ((BigDecimal) object[13]).longValue();
					String layerName = (String) object[14];
					String termName = (String) object[15];

					if (excelWorkBook == null) {
						layerStrings.add(layerName);
						layerIds.add(layerId);

						excelWorkBook = new ExcelWorkBook(
								CharTools.javaScriptEscape(layerName), response);
						excelWorkBook.addHeader("��ע����", 15);// poi����
						excelWorkBook.addHeader("x����", 15);// x����
						excelWorkBook.addHeader("y����", 15);// y����
						excelWorkBook.addHeader("�绰", 15);// �绰
						excelWorkBook.addHeader("��ַ", 25);// ��ַ
						excelWorkBook.addHeader("����", 35);// ����
						excelWorkBook.addHeader("�ݷþ���", 15);// �ݷþ���
						excelWorkBook.addHeader("λ������", 50);// λ������
						excelWorkBook.addHeader("��עʱ��", 15);// ��עʱ��
						excelWorkBook.addHeader("��ע��", 15);// ��עʱ��
					}
					// ����ͬһͼ��
					if (!layerIds.contains(layerId)) {
						layerIds.add(layerId);
						if (layerStrings.contains(layerName)) {
							excelWorkBook.addWorkSheet(CharTools
									.javaScriptEscape(layerName)
									+ layerNameCount++);
						} else {
							layerStrings.add(layerName);
							excelWorkBook.addWorkSheet(CharTools
									.javaScriptEscape(layerName));
						}

						excelWorkBook.addHeader("��ע����", 15);// poi����
						excelWorkBook.addHeader("x����", 15);// x����
						excelWorkBook.addHeader("y����", 15);// y����
						excelWorkBook.addHeader("�绰", 15);// �绰
						excelWorkBook.addHeader("��ַ", 25);// ��ַ
						excelWorkBook.addHeader("����", 35);// ����
						excelWorkBook.addHeader("�ݷþ���", 15);// �ݷþ���
						excelWorkBook.addHeader("λ������", 50);// λ������
						excelWorkBook.addHeader("��עʱ��", 15);// ��עʱ��
						excelWorkBook.addHeader("��ע��", 15);
						row = 0;
					}
					int col = 0;
					row += 1;
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(poiName));// poi����
					if (poiDatas == null || poiDatas.length() <= 1
							|| poiDatas.equals(","))
						continue;
					int index = poiDatas.indexOf(",");
					if (index == -1)
						continue;
					String x = poiDatas.substring(0, index);
					String y = poiDatas.substring(index + 1);
					double lx = Double.parseDouble(x);
					double ly = Double.parseDouble(y);

					 if ((locDesc =
					 CharTools.javaScriptEscape(locDesc)).equals("")) {
					 }
					com.sos.sosgps.api.DPoint dPoint = null;
					try {
						dPoint = coordApi.decryptConvert(lx, ly);
						 locDesc = CoordCvtAPI.getAddress(x, y);
						lx = dPoint.x;
						ly = dPoint.y;
					} catch (Exception ex) {
						System.out
								.println("queryLocByTime-encryptConvert error,"
										+ ex.getMessage());
					}

					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(lx + ""));// x����
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(ly + ""));// y����
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(telephone));// �绰
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(address));// ��ַ
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(poiDesc));// ����
					excelWorkBook
							.addCell(col++, row, CharTools.killNullLong2String(
									visitDistance, "500"));// �ݷþ���

					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(locDesc));// λ������
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(cdate));// ��עʱ��
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(termName));
				}
				if (excelWorkBook != null)
					excelWorkBook.write();
				layerStrings = null;
				layerIds = null;
				excelWorkBook = null;
				return null;
			}
		}

		list = layerAndPoiService.listMatchingPois(entCode,
				userInfo.getUserId(), startInt / limitInt + 1, limitInt,
				bindingStatusInt, searchValue, poiDescription, locDescription,
				layerInt, terminalName, tGroups, startDate, endDate);

		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {

			StringBuffer jsonSb = new StringBuffer();
			int total = 0;

			total = list.getTotalCount();
			for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
					.hasNext();) {
				Object[] obj = iterator.next();

				Long poiId = ((BigDecimal) obj[0]).longValue();
				String poiName = (String) obj[1];
				Long poiType = ((BigDecimal) obj[2]).longValue();
				String poiDatas = (String) obj[3];
				String poiEncryptDatas = (String) obj[4];
				String iconpath = (String) obj[5];
				String locDesc = (String) obj[6];
				String address = (String) obj[7];
				String telephone = (String) obj[8];
				Long visitDistance = ((BigDecimal) obj[9]).longValue();
				String cdate = (String) obj[10];
				String deviceId = (String) obj[11];
				String poiDesc = (String) obj[12];
				Long layerId = ((BigDecimal) obj[13]).longValue();
				String layerName = (String) obj[14];
				String termName = (String) obj[15];

				if (poiDatas == null)
					continue;
				int index = poiDatas.indexOf(",");
				if (index == -1)
					continue;
				
				String x = poiDatas.substring(0, index); 
				String y = poiDatas.substring(index + 1); 
				locDesc = CharTools.javaScriptEscape(locDesc); 
				if (locDesc.equals("")){ 
					try { 
						locDesc = CoordCvtAPI.getAddress(x, y); 
					} catch(Exception ex) {
						System.out.println("queryLocByTime-encryptConvert error," +
					    ex.getMessage()); 
					} 
				}
				
				jsonSb.append("{");
				jsonSb.append("id:'"
						+ CharTools.killNullLong2String(poiId, "0") + "',");// id
				jsonSb.append("poiType:'"
						+ CharTools.killNullLong2String(poiType, "0") + "',");// poi����0��1��2��
				jsonSb.append("poiDatas:'"
						+ CharTools.javaScriptEscape(poiDatas) + "',");// poi����
				jsonSb.append("poiEncryptDatas:'"
						+ CharTools.javaScriptEscape(poiEncryptDatas) + "',");// poi��������
				jsonSb.append("iconpath:'"
						+ CharTools.javaScriptEscape(iconpath) + "',");// poiͼƬ��ַ
				jsonSb.append("poiName:'" + CharTools.javaScriptEscape(poiName)
						+ "',");// poi����
				jsonSb.append("poiDesc:'" + CharTools.javaScriptEscape(poiDesc)
						+ "',");// poi����
				jsonSb.append("address:'" + CharTools.javaScriptEscape(address)
						+ "',");// poi��ַ
				jsonSb.append("telephone:'"
						+ CharTools.javaScriptEscape(telephone) + "',");// poi�绰
				jsonSb.append("visitDistance:'"
						+ CharTools.killNullLong2String(visitDistance, "500")
						+ "',");// �ݷþ��룬��λ��
				jsonSb.append("layerId:'"
						+ CharTools.killNullLong2String(layerId, "") + "',");// ����ͼ��id
				jsonSb.append("layerName:'"
						+ CharTools.javaScriptEscape(layerName) + "',");// ����ͼ������
				jsonSb.append("locDesc:'" + CharTools.javaScriptEscape(locDesc)
						+ "',");// λ������
				jsonSb.append("cDate:'" + CharTools.javaScriptEscape(cdate)
						+ "',");// ����ʱ��
				jsonSb.append("deviceId:'" + CharTools.killNullString(deviceId)
						+ "',");
				jsonSb.append("termName:'"
						+ CharTools.javaScriptEscape(termName) + "'");
				jsonSb.append("},");

			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
			response.getWriter().write(
					"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		} else {
			response.getWriter().write("{total:0 ,data:[]}");
		}

		return mapping.findForward(null);
	}

	// sos��Ȥ���б�
	@SuppressWarnings({ "static-access" })
	public ActionForward listPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
													// HH:mm:ss
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
		CoordCvtAPI coordCvtApi = new CoordCvtAPI();
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = layerAndPoiService.listPoiCreateByUserId(
					entCode, userId, 0, 65536, searchValue, startDate, endDate);
			ExcelWorkBook excelWorkBook = null;
			int row = 0;
			Long lastLayerId = -1L;
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] object = iterator.next();
					// Long poiId = ((BigDecimal)object[0]).longValue();
					// Long poiType = ((BigDecimal)object[1]).longValue();
					String poiDatas = (String) object[2];
					// String poiEncryptDatas = (String)object[3];
					// String iconpath = (String)object[4];
					String poiName = (String) object[5];
					String locDesc = CharTools
							.javaScriptEscape((String) object[6]);
					String address = (String) object[7];
					String telephone = (String) object[8];
					Long visitDistance = ((BigDecimal) object[9]).longValue();
					String cdate = (String) object[10];
					// String deviceId = (String) object[11];
					Long layerId = ((BigDecimal) object[12]).longValue();
					String layerName = (String) object[13];
					String termName = (String) object[14];
					String poiDesc = (String) object[15];
					if (excelWorkBook == null) {
						excelWorkBook = new ExcelWorkBook(
								CharTools.javaScriptEscape(layerName), response);
						excelWorkBook.addHeader("����", 15);// poi����
						excelWorkBook.addHeader("x����", 15);// x����
						excelWorkBook.addHeader("y����", 15);// y����
						excelWorkBook.addHeader("�绰", 15);// �绰
						excelWorkBook.addHeader("��ַ", 25);// ��ַ
						excelWorkBook.addHeader("����", 35);// ����
						excelWorkBook.addHeader("�ݷþ���", 15);// �ݷþ���
						excelWorkBook.addHeader("λ������", 50);// λ������
						excelWorkBook.addHeader("��עʱ��", 15);// ��עʱ��
						excelWorkBook.addHeader("��ע��", 15);// ��עʱ��
						lastLayerId = layerId;
					}
					// ͬһͼ��
					if (lastLayerId.intValue() == layerId.intValue()) {
					} else {
						// ����sheet
						excelWorkBook.addWorkSheet(CharTools
								.javaScriptEscape(layerName));
						excelWorkBook.addHeader("����", 15);// poi����
						excelWorkBook.addHeader("x����", 15);// x����
						excelWorkBook.addHeader("y����", 15);// y����
						excelWorkBook.addHeader("�绰", 15);// �绰
						excelWorkBook.addHeader("��ַ", 25);// ��ַ
						excelWorkBook.addHeader("����", 35);// ����
						excelWorkBook.addHeader("�ݷþ���", 15);// �ݷþ���
						excelWorkBook.addHeader("λ������", 50);// λ������
						excelWorkBook.addHeader("��עʱ��", 15);// ��עʱ��
						excelWorkBook.addHeader("��ע��", 15);
						row = 0;
					}
					int col = 0;
					row += 1;
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(poiName));// poi����
					// if(poiDatas == null) continue;
					if (poiDatas == null || poiDatas.length() <= 1
							|| poiDatas.equals(","))
						continue;
					int index = poiDatas.indexOf(",");
					if (index == -1)
						continue;
					String x = poiDatas.substring(0, index);
					String y = poiDatas.substring(index + 1);
					double lx = Double.parseDouble(x);
					double ly = Double.parseDouble(y);
						// double[] xs = { lx };
						// double[] ys = { ly };
						com.sos.sosgps.api.DPoint dPoint = null;
						// String lngX = "";
						// String latY ="";
						try {
							dPoint = coordApi.decryptConvert(lx, ly);
							// lngX = dPoint[0].getEncryptX();
							// latY = dPoint[0].getEncryptY();
							if (locDesc.equals("")) {
								locDesc = coordCvtApi.getAddress(x, y);
							}
							lx = dPoint.x;
							ly = dPoint.y;
							logger.info("listPoi-encryptConvert: lx = " + lx + "; ly = " + ly);
						} catch (Exception ex) {
							logger.error("listPoi-encryptConvert error," + ex.getMessage());
						}
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(lx + ""));// x����
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(ly + ""));// y����
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(telephone));// �绰
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(address));// ��ַ
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(poiDesc));// ����
					excelWorkBook
							.addCell(col++, row, CharTools.killNullLong2String(
									visitDistance, "500"));// �ݷþ���

					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(locDesc));// λ������
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(cdate));// ��עʱ��
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(termName));
					lastLayerId = layerId;
				}
				if (excelWorkBook != null)
					excelWorkBook.write();
				return null;
			}
		}

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		// int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = layerAndPoiService.listPoiCreateByUserId(
					entCode, userId, startint, limitint, searchValue,
					startDate, endDate);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] object = iterator.next();
					Long poiId = ((BigDecimal) object[0]).longValue();
					Long poiType = ((BigDecimal) object[1]).longValue();
					String poiDatas = (String) object[2];
					String poiEncryptDatas = (String) object[3];
					String iconpath = (String) object[4];
					String poiName = (String) object[5];
					String locDesc = (String) object[6];
					String address = (String) object[7];
					String telephone = (String) object[8];
					Long visitDistance = ((BigDecimal) object[9]).longValue();
					String cdate = (String) object[10];
					String deviceId = (String) object[11];
					Long layerId = ((BigDecimal) object[12]).longValue();
					String layerName = (String) object[13];
					String termName = (String) object[14];
					String poiDesc = (String) object[15];
					if (poiDatas == null)
						continue;
					int index = poiDatas.indexOf(",");
					if (index == -1)
						continue;
					String x = poiDatas.substring(0, index);
					String y = poiDatas.substring(index + 1);
					// double lx = Double.parseDouble(x);
					// double ly = Double.parseDouble(y);
					locDesc = CharTools.javaScriptEscape(locDesc);
					if (locDesc.equals("")) {
						// double[] xs = { lx };
						// double[] ys = { ly };
						// com.sos.sosgps.api.DPoint[] dPoint = null;
						// String lngX = "";
						// String latY ="";
						try {
							// dPoint = coordApi.encryptConvert(xs, ys);
							// lngX = dPoint[0].getEncryptX();
							// latY = dPoint[0].getEncryptY();
							locDesc = coordCvtApi.getAddress(x, y);
							// lx = dPoint[0].x;
							// ly = dPoint[0].y;
						} catch (Exception ex) {
							System.out
									.println("queryLocByTime-encryptConvert error,"
											+ ex.getMessage());
						}
					}

					jsonSb.append("{");
					jsonSb.append("id:'"
							+ CharTools.killNullLong2String(poiId, "0") + "',");// id
					jsonSb.append("poiType:'"
							+ CharTools.killNullLong2String(poiType, "0")
							+ "',");// poi����0��1��2��
					jsonSb.append("poiDatas:'"
							+ CharTools.javaScriptEscape(poiDatas) + "',");// poi����
					jsonSb.append("poiEncryptDatas:'"
							+ CharTools.javaScriptEscape(poiEncryptDatas)
							+ "',");// poi��������
					jsonSb.append("iconpath:'"
							+ CharTools.javaScriptEscape(iconpath) + "',");// poiͼƬ��ַ
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(poiName) + "',");// poi����
					jsonSb.append("poiDesc:'"
							+ CharTools.javaScriptEscape(poiDesc) + "',");// poi����
					jsonSb.append("address:'"
							+ CharTools.javaScriptEscape(address) + "',");// poi��ַ
					jsonSb.append("telephone:'"
							+ CharTools.javaScriptEscape(telephone) + "',");// poi�绰
					jsonSb.append("visitDistance:'"
							+ CharTools.killNullLong2String(visitDistance,
									"500") + "',");// �ݷþ��룬��λ��
					jsonSb.append("layerId:'"
							+ CharTools.killNullLong2String(layerId, "") + "',");// ����ͼ��id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layerName) + "',");// ����ͼ������
					jsonSb.append("locDesc:'"
							+ CharTools.javaScriptEscape(locDesc) + "',");// λ������
					jsonSb.append("cDate:'" + CharTools.javaScriptEscape(cdate)
							+ "',");// ����ʱ��
					jsonSb.append("deviceId:'"
							+ CharTools.javaScriptEscape(deviceId) + "',");
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(termName) + "'");
					jsonSb.append("},");

				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sosδ���κ��ն˵���Ȥ���б�
	@SuppressWarnings("unchecked")
	public ActionForward listNotRefPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		// �Ƿ񵼳�excel
		String expExcel = request.getParameter("expExcel");// trueΪ����
		expExcel = CharTools.javaScriptEscape(expExcel);
		if (expExcel.equalsIgnoreCase("true")) {
			com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
			Page<TPoi> list = layerAndPoiService.listNotRefPoiCreateByUserId(
					entCode, userId, 1, 65536, searchValue);
			ExcelWorkBook excelWorkBook = null;
			int row = 0;
			Long lastLayerId = -1L;
			for (TPoi poi : list.getResult()) {
				Set<RefLayerPoi> refLayerPois = poi.getRefLayerPois();
				TLayers layer = refLayerPois.iterator().next().getTLayers();
				Long layerId = layer.getId();
				String layerName = layer.getLayerName();
				if (excelWorkBook == null) {
					excelWorkBook = new ExcelWorkBook(layerName, response);
					excelWorkBook.addHeader("����", 15);// poi����
					excelWorkBook.addHeader("x����", 15);// x����
					excelWorkBook.addHeader("y����", 15);// y����
					excelWorkBook.addHeader("�绰", 15);// �绰
					excelWorkBook.addHeader("��ַ", 25);// ��ַ
					excelWorkBook.addHeader("����", 35);// ����
					excelWorkBook.addHeader("�ݷþ���", 15);// �ݷþ���
					lastLayerId = layerId;
				}
				// ͬһͼ��
				if (lastLayerId.intValue() == layerId.intValue()) {
				} else {
					// ����sheet
					excelWorkBook.addWorkSheet(CharTools
							.javaScriptEscape(layerName));
					excelWorkBook.addHeader("����", 15);// poi����
					excelWorkBook.addHeader("x����", 15);// x����
					excelWorkBook.addHeader("y����", 15);// y����
					excelWorkBook.addHeader("�绰", 15);// �绰
					excelWorkBook.addHeader("��ַ", 25);// ��ַ
					excelWorkBook.addHeader("����", 35);// ����
					excelWorkBook.addHeader("�ݷþ���", 15);// �ݷþ���
					row = 0;
				}
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poi.getPoiName()));// poi����
				String poiDatas = poi.getPoiDatas();
				if (poiDatas == null)
					continue;
				int index = poiDatas.indexOf(",");
				if (index == -1)
					continue;
				String x = poiDatas.substring(0, index);
				String y = poiDatas.substring(index + 1);
				excelWorkBook
						.addCell(col++, row, CharTools.javaScriptEscape(x));// x����
				excelWorkBook
						.addCell(col++, row, CharTools.javaScriptEscape(y));// y����
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poi.getTelephone()));// �绰
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poi.getAddress()));// ��ַ
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poi.getPoiDesc()));// ����
				String locDesc = poi.getLocDesc();
				double lx = Double.parseDouble(x);
				double ly = Double.parseDouble(y);
				if (locDesc.equals("")) {
					double[] xs = { lx };
					double[] ys = { ly };
					com.sos.sosgps.api.DPoint[] dPoint = null;
					String lngX = "";
					String latY = "";
					try {
						dPoint = coordApi.encryptConvert(xs, ys);
						lngX = dPoint[0].getEncryptX();
						latY = dPoint[0].getEncryptY();
						lx = dPoint[0].x;
						ly = dPoint[0].y;
					} catch (Exception ex) {
						System.out
								.println("queryLocByTime-encryptConvert error,"
										+ ex.getMessage());
					}
					if (Double.parseDouble(x) > 0 && Double.parseDouble(y) > 0) {
						try {
							locDesc = CoordCvtAPI.getAddress(lngX, latY);
						} catch (Exception ex) {
							System.out.println("listPoi-getAddress error,"
									+ ex.getMessage());
						}
					}
				}

				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(lx + ""));// x����
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(ly + ""));// y����
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(poi.getTelephone()));// �绰
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(poi.getAddress()));// ��ַ
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(locDesc));// ����
				excelWorkBook.addCell(col++, row, CharTools
						.killNullLong2String(poi.getVisitDistance(), "500"));// �ݷþ���
				lastLayerId = layerId;
			}
			if (excelWorkBook != null)
				excelWorkBook.write();
			return null;
		}

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<TPoi> list = layerAndPoiService.listNotRefPoiCreateByUserId(
					entCode, userId, page, limitint, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator<TPoi> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					TPoi poi = iterator.next();
					Set<RefLayerPoi> refLayerPois = poi.getRefLayerPois();
					TLayers layer = refLayerPois.iterator().next().getTLayers();
					jsonSb.append("{");
					jsonSb.append("id:'" + poi.getId() + "',");// id
					jsonSb.append("poiType:'"
							+ CharTools.killNullLong2String(poi.getPoiType(),
									"0") + "',");// poi����0��1��2��
					jsonSb.append("poiDatas:'"
							+ CharTools.javaScriptEscape(poi.getPoiDatas())
							+ "',");// poi����
					jsonSb.append("poiEncryptDatas:'"
							+ CharTools.javaScriptEscape(poi
									.getPoiEncryptDatas()) + "',");// poi��������
					jsonSb.append("iconpath:'"
							+ CharTools.javaScriptEscape(poi.getIconpath())
							+ "',");// poiͼƬ��ַ
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(poi.getPoiName())
							+ "',");// poi����
					jsonSb.append("poiDesc:'"
							+ CharTools.javaScriptEscape(poi.getPoiDesc())
							+ "',");// poi����
					jsonSb.append("address:'"
							+ CharTools.javaScriptEscape(poi.getAddress())
							+ "',");// poi��ַ
					jsonSb.append("telephone:'"
							+ CharTools.javaScriptEscape(poi.getTelephone())
							+ "',");// poi�绰
					jsonSb.append("visitDistance:'"
							+ CharTools.killNullLong2String(
									poi.getVisitDistance(), "500") + "',");// �ݷþ��룬��λ��
					jsonSb.append("layerId:'"
							+ CharTools.killNullLong2String(layer.getId(), "")
							+ "',");// ����ͼ��id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layer.getLayerName())
							+ "',");// ����ͼ������
					jsonSb.append("cDate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(poi.getCdate())) + "'");// ����ʱ��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos��ѯpoi��Ϣ
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward queryPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String id = request.getParameter("id");// poi��id
		TPoi poi = layerAndPoiService.queryPoi(id);
		if (poi == null) {
			response.getWriter().write("{result:\"2\"}");// ��ѯʧ��
			return mapping.findForward(null);
		}
		Set<RefTermPoi> refTermPois = poi.getRefTermPois();
		String termDeviceIds = "";
		for (Iterator iterator = refTermPois.iterator(); iterator.hasNext();) {
			RefTermPoi refTermPoi = (RefTermPoi) iterator.next();
			String deviceId = refTermPoi.getTTerminal().getDeviceId();
			termDeviceIds += deviceId + ",";
		}
		if (termDeviceIds.length() > 0) {
			termDeviceIds = termDeviceIds.substring(0,
					termDeviceIds.length() - 1);
		}
		StringBuffer jsonSb = new StringBuffer();
		jsonSb.append("id:'" + poi.getId() + "',");// id
		jsonSb.append("deviceIds:'" + termDeviceIds + "'");// �����ն�deviceId
		response.getWriter().write("{" + jsonSb.toString() + "}");
		return mapping.findForward(null);
	}

	// sos������Ȥ��
	@SuppressWarnings("static-access")
	public ActionForward addPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String poiName = request.getParameter("poiName");// poi����
		String poiDesc = request.getParameter("poiDesc");// poi����
		String poiType = request.getParameter("poiType");// poi����0��1��2��
		String poiDatas = request.getParameter("poiDatas");// poi����
		String poiEncryptDatas = request.getParameter("poiEncryptDatas");// poi��������
		String telephone = request.getParameter("telephone");// �绰
		String address = request.getParameter("address");// ��ַ
		String iconpath = request.getParameter("iconpath");// ͼƬ·��
		String visitDistanceStr = request.getParameter("visitDistance");// ���ʾ��룬��λ�ף�Ĭ��500��
		String layerId = request.getParameter("layerId");// ͼ��id
		String deviceIds = request.getParameter("deviceIds");// ������ԱdeviceId�����","����
		String deviceId = request.getParameter("deviceId");
		String locDesc = "";

		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);

		poiDesc = CharTools.killNullString(poiDesc);
		poiDesc = java.net.URLDecoder.decode(poiDesc, "utf-8");
		poiDesc = CharTools.killNullString(poiDesc);

		poiType = CharTools.killNullString(poiType);
		poiDatas = CharTools.killNullString(poiDatas);
		poiEncryptDatas = CharTools.killNullString(poiEncryptDatas);
		visitDistanceStr = CharTools.killNullString(visitDistanceStr);
		poiName = CharTools.killNullString(poiName);// �����ַ�����
		poiDesc = CharTools.killNullString(poiDesc);// �����ַ�����
		Long visitDistance = CharTools.str2Long(visitDistanceStr, 500L);// Ĭ��500��
		String[] poiEncryptDatasArr = CharTools.split(poiDatas, ",");
		if (poiEncryptDatasArr.length == 2) {
			// CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			try {
				com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				double[] xs = { Float.parseFloat(poiEncryptDatasArr[0]) };
				double[] ys = { Float.parseFloat(poiEncryptDatasArr[1]) };
				com.sos.sosgps.api.DPoint[] dPoint = coordApizw.encryptConvert(
						xs, ys);
				String lngX = dPoint[0].getEncryptX();
				String latY = dPoint[0].getEncryptY();
				poiEncryptDatas = lngX + "," + latY;
				locDesc = coordCvtApi.getAddress(lngX, latY);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		telephone = CharTools.killNullString(telephone);
		telephone = java.net.URLDecoder.decode(telephone, "utf-8");
		telephone = CharTools.killNullString(telephone);
		address = CharTools.killNullString(address);
		address = java.net.URLDecoder.decode(address, "utf-8");
		address = CharTools.killNullString(address);
		iconpath = CharTools.killNullString(iconpath);
		String[] deviceIdss = CharTools.split(deviceIds, ",");
		Long lId = Long.parseLong(layerId);
		String visible = "1";// Ĭ�Ͽɼ�
		layerAndPoiService.addPoi(entCode, poiName, poiDesc, poiType, poiDatas,
				poiEncryptDatas, telephone, address, iconpath, visible,
				visitDistance, lId, deviceIdss, locDesc, deviceId);
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sos�޸���Ȥ��
	public ActionForward updatePoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String id = request.getParameter("id");// poi��id
		String poiName = request.getParameter("poiName");// poi����
		String poiDesc = request.getParameter("poiDesc");// poi����
		String poiType = request.getParameter("poiType");// poi����0��1��2��
		String poiDatas = request.getParameter("poiDatas");// poi����
		String poiEncryptDatas = request.getParameter("poiEncryptDatas");// poi��������
		String telephone = request.getParameter("telephone");// �绰
		String address = request.getParameter("address");// ��ַ
		String iconpath = request.getParameter("iconpath");// ͼƬ·��
		String visitDistanceStr = request.getParameter("visitDistance");// ���ʾ��룬��λ�ף�Ĭ��500��
		String layerId = request.getParameter("layerId");// ͼ��id

		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);

		String deviceIds = request.getParameter("deviceIds");// ������ԱdeviceId�����","����

		poiDesc = CharTools.killNullString(poiDesc);
		poiDesc = java.net.URLDecoder.decode(poiDesc, "utf-8");
		poiDesc = CharTools.killNullString(poiDesc);

		poiType = CharTools.killNullString(poiType);
		poiDatas = CharTools.killNullString(poiDatas);
		poiEncryptDatas = CharTools.killNullString(poiEncryptDatas);
		telephone = CharTools.killNullString(telephone);
		telephone = java.net.URLDecoder.decode(telephone, "utf-8");
		telephone = CharTools.killNullString(telephone);

		address = CharTools.killNullString(address);
		address = java.net.URLDecoder.decode(address, "utf-8");
		address = CharTools.killNullString(address);

		iconpath = CharTools.killNullString(iconpath);
		visitDistanceStr = CharTools.killNullString(visitDistanceStr);
		Long visitDistance = CharTools.str2Long(visitDistanceStr, 500L);// Ĭ��500��
		String[] deviceIdss = CharTools.split(deviceIds, ",");
		Long lId = Long.parseLong(layerId);
		String visible = "1";// Ĭ�Ͽɼ�
		Long poiId = Long.parseLong(id);
		layerAndPoiService.updatePoi(poiId, entCode, poiName, poiDesc, poiType,
				poiDatas, poiEncryptDatas, telephone, address, iconpath,
				visible, visitDistance, lId, deviceIdss);
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// add by magiejue 2010-12-7 �����޸�poi
	public ActionForward updatePoiBatch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}

		// ��request�л�ȡ����
		// List<Long> ids = new ArrayList();
		String layerId = request.getParameter("poiLayer");// ͼ��id
		String poiIds = request.getParameter("poiIds");// poi��ids
		String iconpath = request.getParameter("iconpath");// ͼƬ·��
		String visitDistanceStr = request.getParameter("range");// ���ʾ��룬��λ�ף�Ĭ��500��
		iconpath = CharTools.killNullString(iconpath);
		visitDistanceStr = CharTools.killNullString(visitDistanceStr);
		if (poiIds != null && poiIds != "") {
			/*
			 * String[] idString = poiIds.split(","); for (int i = 0; i <
			 * idString.length; i++) { ids.add(Long.valueOf(idString[i])); }
			 */
			boolean bool = layerAndPoiService.updatePoiBatch(poiIds, layerId,
					visitDistanceStr, iconpath);
			if (bool) {
				response.getWriter().write("{result:\"1\"}");// �ɹ�
			} else {
				response.getWriter().write("{result:\"3\"}");// ʧ��
			}
		} else {
			response.getWriter().write("{result:\"3\"}");// ʧ��
		}
		return mapping.findForward(null);
	}

	// sos�޸���Ȥ�����ԣ����޸���ͼ�㡢�ն˰󶨹�ϵ��
	public ActionForward updatePoiProperties(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String id = request.getParameter("id");// poi��id
		String poiName = request.getParameter("poiName");// poi����
		String poiDesc = request.getParameter("poiDesc");// poi����
		String poiType = request.getParameter("poiType");// poi����0��1��2��
		String poiDatas = request.getParameter("poiDatas");// poi����
		String poiEncryptDatas = request.getParameter("poiEncryptDatas");// poi��������
		String telephone = request.getParameter("telephone");// �绰
		String address = request.getParameter("address");// ��ַ
		String iconpath = request.getParameter("iconpath");// ͼƬ·��
		String visitDistanceStr = request.getParameter("visitDistance");// ���ʾ��룬��λ�ף�Ĭ��500��

		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);

		poiDesc = CharTools.killNullString(poiDesc);
		poiDesc = java.net.URLDecoder.decode(poiDesc, "utf-8");
		poiDesc = CharTools.killNullString(poiDesc);

		poiType = CharTools.killNullString(poiType);
		poiDatas = CharTools.killNullString(poiDatas);
		poiEncryptDatas = CharTools.killNullString(poiEncryptDatas);

		telephone = CharTools.killNullString(telephone);
		telephone = java.net.URLDecoder.decode(telephone, "utf-8");
		telephone = CharTools.killNullString(telephone);

		address = CharTools.killNullString(address);
		address = java.net.URLDecoder.decode(address, "utf-8");
		address = CharTools.killNullString(address);

		iconpath = CharTools.killNullString(iconpath);
		visitDistanceStr = CharTools.killNullString(visitDistanceStr);
		Long visitDistance = CharTools.str2Long(visitDistanceStr, 500L);// Ĭ��500��
		String visible = "1";// Ĭ�Ͽɼ�
		Long poiId = Long.parseLong(id);
		layerAndPoiService.updatePoi(poiId, entCode, poiName, poiDesc, poiType,
				poiDatas, poiEncryptDatas, telephone, address, iconpath,
				visible, visitDistance);
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sosɾ����Ȥ��
	public ActionForward deletePois(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String ids = request.getParameter("ids");// poi��id�����","����
		String[] poiIds = CharTools.split(ids, ",");
		layerAndPoiService.deletePois(poiIds);
		response.getWriter().write("{result:\"1\"}");
		return mapping.findForward(null);
	}

	// sos�ɼ�ͼ���б�
	@SuppressWarnings("unchecked")
	public ActionForward listVisibleLayer(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			int startint = Integer.parseInt(start);
			int limitint = Integer.parseInt(limit);
			int page = startint / limitint + 1;
			Long userId = userInfo.getUserId();
			Page<TLayers> list = layerAndPoiService.listVisibleByUserId(
					entCode, userId, page, limitint);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator<TLayers> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					TLayers layer = (TLayers) iterator.next();
					Set<RefUserLayer> refUserLayers = layer.getRefUserLayers();
					RefUserLayer refUserLayer = refUserLayers.iterator().next();
					jsonSb.append("{");
					jsonSb.append("id:'" + layer.getId() + "',");// id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layer.getLayerName())
							+ "',");// ͼ������
					jsonSb.append("mapLevel:'" + layer.getInfo1() + "',");// ͼ���ͼ����
					jsonSb.append("visible:'" + refUserLayer.getVisible() + "'");// �����Ƿ��¼����0��1��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos�ɼ�ͼ���Լ���Ȥ���б�
	@SuppressWarnings("unchecked")
	public ActionForward listVisibleLayerAndPoi(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Long userId = userInfo.getUserId();
			Page<TLayers> list = layerAndPoiService.listVisibleAndPoiByUserId(
					entCode, userId, page, limitint);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator<TLayers> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					TLayers layer = (TLayers) iterator.next();
					// Set<RefUserLayer> refUserLayers =
					// layer.getRefUserLayers();
					// RefUserLayer refUserLayer =
					// refUserLayers.iterator().next();
					jsonSb.append("{");
					jsonSb.append("id:'" + layer.getId() + "',");// id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layer.getLayerName())
							+ "',");// ͼ������
					jsonSb.append("mapLevel:'" + layer.getInfo1() + "',");// ͼ���ͼ����
					jsonSb.append("editable:'"
							+ (layer.getUserId().intValue() == userId
									.intValue() ? "true" : "false") + "',");// ͼ���Ƿ���޸ģ��û������ķ����޸ģ�
					// poi�б�
					jsonSb.append("pois:[");
					Set<RefLayerPoi> refLayerPois = layer.getRefLayerPois();
					for (Iterator<RefLayerPoi> iterator2 = refLayerPois
							.iterator(); iterator2.hasNext();) {
						jsonSb.append("{");
						RefLayerPoi refLayerPoi = iterator2.next();
						TPoi poi = refLayerPoi.getTPoi();
						jsonSb.append("id:'" + poi.getId() + "',");// id
						jsonSb.append("poiType:'"
								+ CharTools.killNullLong2String(
										poi.getPoiType(), "0") + "',");// poi����0��1��2��
						jsonSb.append("poiDatas:'"
								+ CharTools.javaScriptEscape(poi.getPoiDatas())
								+ "',");// poi����
						jsonSb.append("poiEncryptDatas:'"
								+ CharTools.javaScriptEscape(poi
										.getPoiEncryptDatas()) + "',");// poi��������
						jsonSb.append("iconpath:'"
								+ CharTools.javaScriptEscape(poi.getIconpath())
								+ "',");// poiͼƬ��ַ
						jsonSb.append("poiName:'"
								+ CharTools.javaScriptEscape(poi.getPoiName())
								+ "',");// poi����
						jsonSb.append("poiDesc:'"
								+ CharTools.javaScriptEscape(poi.getPoiDesc())
								+ "',");// poi����
						jsonSb.append("address:'"
								+ CharTools.javaScriptEscape(poi.getAddress())
								+ "',");// poi��ַ
						jsonSb.append("visitDistance:'"
								+ CharTools.killNullLong2String(
										poi.getVisitDistance(), "500") + "',");// �ݷþ��룬��λ��
						jsonSb.append("telephone:'"
								+ CharTools.javaScriptEscape(poi.getTelephone())
								+ "'");// poi�绰
						jsonSb.append("},");
					}
					// ȥ������","
					int lastCommaIndex = jsonSb.lastIndexOf(",");
					if (lastCommaIndex == jsonSb.length() - 1) {
						jsonSb.deleteCharAt(lastCommaIndex);
					}
					jsonSb.append("]");
					jsonSb.append("},");
				}
				// ȥ������","
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos�޸�ͼ��ɼ��ԣ��Ƿ��¼���أ�
	public ActionForward updateLayerVisible(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String layerIds = request.getParameter("layerIds");// ��Ҫ��¼���ص�layer��id,���","����
		String[] visibleRefUserLayerIds = CharTools.split(layerIds, ",");
		layerAndPoiService.updateLayerVisibleByUserId(entCode, userId,
				visibleRefUserLayerIds);
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sos�����ն�id�����poi(�����û��ɼ���ͼ��)
	public ActionForward queryPoiByDeviceId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");// �ն�id

		deviceId = CharTools.killNullString(deviceId);
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		deviceId = CharTools.killNullString(deviceId);

		Page<TPoi> list = layerAndPoiService.queryPoiByDeviceId(deviceId,
				userId);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			total = list.getTotalCount();
			for (TPoi poi : list.getResult()) {
				jsonSb.append("{");// id
				jsonSb.append("id:'" + poi.getId() + "',");// id
				jsonSb.append("poiType:'"
						+ CharTools.killNullLong2String(poi.getPoiType(), "0")
						+ "',");// poi����0��1��2��
				jsonSb.append("poiDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiDatas()) + "',");// poi����
				jsonSb.append("poiEncryptDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiEncryptDatas())
						+ "',");// poi��������
				jsonSb.append("iconpath:'"
						+ CharTools.javaScriptEscape(poi.getIconpath()) + "',");// poiͼƬ��ַ
				jsonSb.append("poiName:'"
						+ CharTools.javaScriptEscape(poi.getPoiName()) + "',");// poi����
				jsonSb.append("poiDesc:'"
						+ CharTools.javaScriptEscape(poi.getPoiDesc()) + "',");// poi����
				jsonSb.append("address:'"
						+ CharTools.javaScriptEscape(poi.getAddress()) + "',");// poi��ַ
				jsonSb.append("visitDistance:'"
						+ CharTools.killNullLong2String(poi.getVisitDistance(),
								"500") + "',");// �ݷþ��룬��λ��
				jsonSb.append("telephone:'"
						+ CharTools.javaScriptEscape(poi.getTelephone()) + "'");// poi�绰
				jsonSb.append("},");
			}
			// ȥ������","
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos�����ն�id�����poi(�����û��ɹ����ͼ��)
	public ActionForward queryManagePoiByDeviceId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");// �ն�id
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		deviceId = CharTools.killNullString(deviceId);
		Page<TPoi> list = layerAndPoiService.queryManagePoiByDeviceId(deviceId,
				userId);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			total = list.getTotalCount();
			for (TPoi poi : list.getResult()) {
				jsonSb.append("{");// id
				jsonSb.append("id:'" + poi.getId() + "',");// id
				jsonSb.append("poiType:'"
						+ CharTools.killNullLong2String(poi.getPoiType(), "0")
						+ "',");// poi����0��1��2��
				jsonSb.append("poiDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiDatas()) + "',");// poi����
				jsonSb.append("poiEncryptDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiEncryptDatas())
						+ "',");// poi��������
				jsonSb.append("iconpath:'"
						+ CharTools.javaScriptEscape(poi.getIconpath()) + "',");// poiͼƬ��ַ
				jsonSb.append("poiName:'"
						+ CharTools.javaScriptEscape(poi.getPoiName()) + "',");// poi����
				jsonSb.append("poiDesc:'"
						+ CharTools.javaScriptEscape(poi.getPoiDesc()) + "',");// poi����
				jsonSb.append("address:'"
						+ CharTools.javaScriptEscape(poi.getAddress()) + "',");// poi��ַ
				jsonSb.append("visitDistance:'"
						+ CharTools.killNullLong2String(poi.getVisitDistance(),
								"500") + "',");// �ݷþ��룬��λ��
				jsonSb.append("telephone:'"
						+ CharTools.javaScriptEscape(poi.getTelephone()) + "'");// poi�绰
				jsonSb.append("},");
			}
			// ȥ������","
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos�����޸���Ȥ��󶨹�ϵ
	public ActionForward updatePoiRefTerm(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String poiIds = request.getParameter("poiIds");// poi��id�����","����
		String deviceIds = request.getParameter("deviceIds");// ������ԱdeviceId�����","����
		String[] poiIdss = CharTools.split(poiIds, ",");
		String[] deviceIdss = CharTools.split(deviceIds, ",");
		layerAndPoiService.updateRefTerm(poiIdss, deviceIdss);
		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sosɾ���ն˵����а󶨹�ϵ
	public ActionForward deletePoiRefTerm(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:'9'}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String poiIds = request.getParameter("poiIds");// poi��id�����","����
		String deviceIds = request.getParameter("deviceIds");// ������ԱdeviceId�����","����
		String[] poiIdss = CharTools.split(poiIds, ",");
		String[] deviceIdss = CharTools.split(deviceIds, ",");

		// layerAndPoiService.deleteRefTerm(deviceIdss);
		layerAndPoiService.updateBinding(poiIdss, deviceIdss,
				LayerAndPoiService.reomve_binding);
		response.getWriter().write("{result:'1'}");// �ɹ�
		return mapping.findForward(null);
	}

	// sos����׷���޸���Ȥ��󶨹�ϵ
	public ActionForward updatePoiRefTermForAdd(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:'9'}");// δ��¼
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// ��request�л�ȡ����
		String poiIds = request.getParameter("poiIds");// poi��id�����","����
		String deviceIds = request.getParameter("deviceIds");// ������ԱdeviceId�����","����
		String[] poiIdss = CharTools.split(poiIds, ",");
		String[] deviceIdss = CharTools.split(deviceIds, ",");
		// ����׷�Ӱ�
		// layerAndPoiService.updateRefTermForAdd(poiIdss, deviceIdss);
		layerAndPoiService.updateBinding(poiIdss, deviceIdss,
				LayerAndPoiService.add_binding);
		response.getWriter().write("{result:'1'}");// �ɹ�
		return mapping.findForward(null);
	}

	// add by magiejue 2010-12-7��ѯ���б�ע��
	@SuppressWarnings("unchecked")
	public ActionForward allPois(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
													// HH:mm:ss
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		String poiLayer = request.getParameter("poiLayer") == null ? ""
				: request.getParameter("poiLayer");
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		List<TPoi> list = layerAndPoiService.listPoiCreateByUserId(entCode,
				userId, searchValue, poiLayer, startDate, endDate);
		if (list != null && list.size() > 0) {
			total = list.size();
			for (int i = 0; i < list.size(); i++) {
				TPoi poi = list.get(i);
				Set<RefLayerPoi> refLayerPois = poi.getRefLayerPois();
				TLayers layer = refLayerPois.iterator().next().getTLayers();
				jsonSb.append("{");
				jsonSb.append("id:'" + poi.getId() + "',");// id
				jsonSb.append("poiType:'"
						+ CharTools.killNullLong2String(poi.getPoiType(), "0")
						+ "',");// poi����0��1��2��
				jsonSb.append("poiDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiDatas()) + "',");// poi����
				jsonSb.append("poiEncryptDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiEncryptDatas())
						+ "',");// poi��������
				jsonSb.append("iconpath:'"
						+ CharTools.javaScriptEscape(poi.getIconpath()) + "',");// poiͼƬ��ַ
				jsonSb.append("poiName:'"
						+ CharTools.javaScriptEscape(poi.getPoiName()) + "',");// poi����
				jsonSb.append("poiDesc:'"
						+ CharTools.javaScriptEscape(poi.getPoiDesc()) + "',");// poi����
				jsonSb.append("address:'"
						+ CharTools.javaScriptEscape(poi.getAddress()) + "',");// poi��ַ
				jsonSb.append("telephone:'"
						+ CharTools.javaScriptEscape(poi.getTelephone()) + "',");// poi�绰
				jsonSb.append("visitDistance:'"
						+ CharTools.killNullLong2String(poi.getVisitDistance(),
								"500") + "',");// �ݷþ��룬��λ��
				jsonSb.append("layerId:'"
						+ CharTools.killNullLong2String(layer.getId(), "")
						+ "',");// ����ͼ��id
				jsonSb.append("layerName:'"
						+ CharTools.javaScriptEscape(layer.getLayerName())
						+ "',");// ����ͼ������
				jsonSb.append("cDate:'"
						+ CharTools.javaScriptEscape(DateUtility
								.dateTimeToStr(poi.getCdate())) + "'");// ����ʱ��
				jsonSb.append("},");
			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// add by zhaofeng 2011-10-24��ѯ���б�ע��---��ҳ
	@SuppressWarnings("unchecked")
	public ActionForward listAllPois(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		String st = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-MM-dd
													// HH:mm:ss
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		String poiLayer = request.getParameter("poiLayer") == null ? ""
				: request.getParameter("poiLayer");
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<TPoi> list = layerAndPoiService.listPoiCreateByUserId(entCode,
					userId, pageNo, pageSize, searchValue, poiLayer, startDate,
					endDate);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator<?> iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					TPoi poi = (TPoi) iterator.next();
					Set<RefLayerPoi> refLayerPois = poi.getRefLayerPois();
					TLayers layer = refLayerPois.iterator().next().getTLayers();
					jsonSb.append("{");
					jsonSb.append("id:'" + poi.getId() + "',");// id
					jsonSb.append("poiType:'"
							+ CharTools.killNullLong2String(poi.getPoiType(),
									"0") + "',");// poi����0��1��2��
					jsonSb.append("poiDatas:'"
							+ CharTools.javaScriptEscape(poi.getPoiDatas())
							+ "',");// poi����
					jsonSb.append("poiEncryptDatas:'"
							+ CharTools.javaScriptEscape(poi
									.getPoiEncryptDatas()) + "',");// poi��������
					jsonSb.append("iconpath:'"
							+ CharTools.javaScriptEscape(poi.getIconpath())
							+ "',");// poiͼƬ��ַ
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(poi.getPoiName())
							+ "',");// poi����
					jsonSb.append("poiDesc:'"
							+ CharTools.javaScriptEscape(poi.getPoiDesc())
							+ "',");// poi����
					jsonSb.append("address:'"
							+ CharTools.javaScriptEscape(poi.getAddress())
							+ "',");// poi��ַ
					jsonSb.append("telephone:'"
							+ CharTools.javaScriptEscape(poi.getTelephone())
							+ "',");// poi�绰
					jsonSb.append("visitDistance:'"
							+ CharTools.killNullLong2String(
									poi.getVisitDistance(), "500") + "',");// �ݷþ��룬��λ��
					jsonSb.append("layerId:'"
							+ CharTools.killNullLong2String(layer.getId(), "")
							+ "',");// ����ͼ��id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layer.getLayerName())
							+ "',");// ����ͼ������
					jsonSb.append("cDate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(poi.getCdate())) + "'");// ����ʱ��
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// �����Ƿ����ظ���ע�� add by zhaofeng 2011-10-22
	public ActionForward checkPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String tmpPoiName = request.getParameter("tmpPoiName");// ��ȡ��ע������
		tmpPoiName = java.net.URLDecoder.decode(tmpPoiName, "UTF-8");
		// StringBuffer jsonSb = new StringBuffer();
		// int total = 0;
		List<TPoi> list = layerAndPoiService.listPoiByPoiName(entCode,
				tmpPoiName);
		if (list != null && list.size() > 0) {
			response.getWriter().write("{result:\"2\"}");// �����ظ���ע��
			return mapping.findForward(null);
		} else {
			response.getWriter().write("{result:\"1\"}");// û���ظ���ע��
			return mapping.findForward(null);
		}
		// response.getWriter().write("{result:'1'}");// û���ظ���ע��

	}
}
