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
 * @Title:poi展现层action
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-10 下午03:30:32
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
			response.getWriter().write("{result:'9'}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
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

	// sos图层列表
	@SuppressWarnings("rawtypes")
	public ActionForward listLayer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
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
							+ "',");// 图层名称
					jsonSb.append("layerDesc:'"
							+ CharTools.javaScriptEscape(layer.getLayerDesc())
							+ "',");// 图层描述
					jsonSb.append("mapLevel:'" + layer.getInfo1() + "',");// 图层地图级别
					jsonSb.append("visible:'" + layer.getVisible() + "'");// 是否可见0不可见1可见
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
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
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
							+ "'");// 图层名称
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

	// sos查询图层信息
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward queryLayer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String id = request.getParameter("id");// 图层id

		TLayers layer = layerAndPoiService.queryLayer(id);
		if (layer == null) {
			response.getWriter().write("{result:\"2\"}");// 查询失败
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
		jsonSb.append("visibleUserIds:'" + visibleUserIds + "'");// 可见此图层的用户id列表，不包含此用户本身
		// System.out.println("{" + jsonSb.toString() + "}");
		response.getWriter().write("{" + jsonSb.toString() + "}");
		return mapping.findForward(null);
	}

	// sos新增图层
	public ActionForward addLayer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String layerName = request.getParameter("layerName");// 图层名称
		String layerDesc = request.getParameter("layerDesc");// 图层描述
		String visible = request.getParameter("visible");// 是否可见0不可见1可见
		String mapLevel = request.getParameter("mapLevel");// 图层地图级别
		String userIdss = request.getParameter("userIdss");// 可见用户id，多个","隔开
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

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos修改图层
	public ActionForward updateLayer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String id = request.getParameter("id");// 图层id
		String layerName = request.getParameter("layerName");// 图层名称
		String layerDesc = request.getParameter("layerDesc");// 图层描述
		String visible = request.getParameter("visible");// 是否可见0不可见1可见
		String mapLevel = request.getParameter("mapLevel");// 图层地图级别
		String userIdss = request.getParameter("userIdss");// 可见用户id，多个","隔开
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

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos删除图层
	public ActionForward deleteLayers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String ids = request.getParameter("ids");// 图层id，多个","隔开
		String[] layerIds = CharTools.split(ids, ",");
		int tPoi=layerAndPoiService.checkPoi(ids);
        if(tPoi!=0){
            response.getWriter().write("{result:\"2\"}");
            return mapping.findForward(null);
        }
		layerAndPoiService.deleteLayers(layerIds);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// 查询符合条件的标注点
	public ActionForward listMatchingPois(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:'9'}");// 未登录
			return mapping.findForward(null);
		}

		String entCode = userInfo.getEmpCode();
		// Long userId = userInfo.getUserId();

		/*-------------------------从request中获取参数------------------------*/
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String searchValue = request.getParameter("searchValue");
		String poiDescription = request.getParameter("poiDescription");
		String locDescription = request.getParameter("locDescription");
		String layer = request.getParameter("layer");
		String bindingStatus = request.getParameter("bindingStatus");
		String terminalName = request.getParameter("terminalName");
		String terminalGroupIds = request.getParameter("terminalGroupIds");
		// 开始时间，格式yyyy-MM-dd HH:mm:ss
		String st = request.getParameter("startTime");
		System.out.println(st);
		// 开始时间，格式yyyy-MM-dd HH:mm:ss
		String et = request.getParameter("endTime");
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出

		System.out.println(start + "  " + limit + "  " + searchValue + "  "
				+ poiDescription + "  " + layer + "  " + bindingStatus + "  "
				+ terminalName);

		/*-------------------------对参数进行处理------------------------*/
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

		// 是否导出Excel
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
						excelWorkBook.addHeader("标注名称", 15);// poi名称
						excelWorkBook.addHeader("x坐标", 15);// x坐标
						excelWorkBook.addHeader("y坐标", 15);// y坐标
						excelWorkBook.addHeader("电话", 15);// 电话
						excelWorkBook.addHeader("地址", 25);// 地址
						excelWorkBook.addHeader("描述", 35);// 描述
						excelWorkBook.addHeader("拜访距离", 15);// 拜访距离
						excelWorkBook.addHeader("位置描述", 50);// 位置描述
						excelWorkBook.addHeader("标注时间", 15);// 标注时间
						excelWorkBook.addHeader("标注人", 15);// 标注时间
					}
					// 不是同一图层
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

						excelWorkBook.addHeader("标注名称", 15);// poi名称
						excelWorkBook.addHeader("x坐标", 15);// x坐标
						excelWorkBook.addHeader("y坐标", 15);// y坐标
						excelWorkBook.addHeader("电话", 15);// 电话
						excelWorkBook.addHeader("地址", 25);// 地址
						excelWorkBook.addHeader("描述", 35);// 描述
						excelWorkBook.addHeader("拜访距离", 15);// 拜访距离
						excelWorkBook.addHeader("位置描述", 50);// 位置描述
						excelWorkBook.addHeader("标注时间", 15);// 标注时间
						excelWorkBook.addHeader("标注人", 15);
						row = 0;
					}
					int col = 0;
					row += 1;
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(poiName));// poi名称
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
							CharTools.javaScriptEscape(lx + ""));// x坐标
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(ly + ""));// y坐标
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(telephone));// 电话
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(address));// 地址
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(poiDesc));// 描述
					excelWorkBook
							.addCell(col++, row, CharTools.killNullLong2String(
									visitDistance, "500"));// 拜访距离

					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(locDesc));// 位置描述
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(cdate));// 标注时间
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
						+ CharTools.killNullLong2String(poiType, "0") + "',");// poi类型0点1线2面
				jsonSb.append("poiDatas:'"
						+ CharTools.javaScriptEscape(poiDatas) + "',");// poi坐标
				jsonSb.append("poiEncryptDatas:'"
						+ CharTools.javaScriptEscape(poiEncryptDatas) + "',");// poi加密坐标
				jsonSb.append("iconpath:'"
						+ CharTools.javaScriptEscape(iconpath) + "',");// poi图片地址
				jsonSb.append("poiName:'" + CharTools.javaScriptEscape(poiName)
						+ "',");// poi名称
				jsonSb.append("poiDesc:'" + CharTools.javaScriptEscape(poiDesc)
						+ "',");// poi描述
				jsonSb.append("address:'" + CharTools.javaScriptEscape(address)
						+ "',");// poi地址
				jsonSb.append("telephone:'"
						+ CharTools.javaScriptEscape(telephone) + "',");// poi电话
				jsonSb.append("visitDistance:'"
						+ CharTools.killNullLong2String(visitDistance, "500")
						+ "',");// 拜访距离，单位米
				jsonSb.append("layerId:'"
						+ CharTools.killNullLong2String(layerId, "") + "',");// 所属图层id
				jsonSb.append("layerName:'"
						+ CharTools.javaScriptEscape(layerName) + "',");// 所属图层名称
				jsonSb.append("locDesc:'" + CharTools.javaScriptEscape(locDesc)
						+ "',");// 位置描述
				jsonSb.append("cDate:'" + CharTools.javaScriptEscape(cdate)
						+ "',");// 创建时间
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

	// sos兴趣点列表
	@SuppressWarnings({ "static-access" })
	public ActionForward listPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
		CoordCvtAPI coordCvtApi = new CoordCvtAPI();
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
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
						excelWorkBook.addHeader("名称", 15);// poi名称
						excelWorkBook.addHeader("x坐标", 15);// x坐标
						excelWorkBook.addHeader("y坐标", 15);// y坐标
						excelWorkBook.addHeader("电话", 15);// 电话
						excelWorkBook.addHeader("地址", 25);// 地址
						excelWorkBook.addHeader("描述", 35);// 描述
						excelWorkBook.addHeader("拜访距离", 15);// 拜访距离
						excelWorkBook.addHeader("位置描述", 50);// 位置描述
						excelWorkBook.addHeader("标注时间", 15);// 标注时间
						excelWorkBook.addHeader("标注人", 15);// 标注时间
						lastLayerId = layerId;
					}
					// 同一图层
					if (lastLayerId.intValue() == layerId.intValue()) {
					} else {
						// 增加sheet
						excelWorkBook.addWorkSheet(CharTools
								.javaScriptEscape(layerName));
						excelWorkBook.addHeader("名称", 15);// poi名称
						excelWorkBook.addHeader("x坐标", 15);// x坐标
						excelWorkBook.addHeader("y坐标", 15);// y坐标
						excelWorkBook.addHeader("电话", 15);// 电话
						excelWorkBook.addHeader("地址", 25);// 地址
						excelWorkBook.addHeader("描述", 35);// 描述
						excelWorkBook.addHeader("拜访距离", 15);// 拜访距离
						excelWorkBook.addHeader("位置描述", 50);// 位置描述
						excelWorkBook.addHeader("标注时间", 15);// 标注时间
						excelWorkBook.addHeader("标注人", 15);
						row = 0;
					}
					int col = 0;
					row += 1;
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(poiName));// poi名称
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
							CharTools.javaScriptEscape(lx + ""));// x坐标
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(ly + ""));// y坐标
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(telephone));// 电话
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(address));// 地址
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(poiDesc));// 描述
					excelWorkBook
							.addCell(col++, row, CharTools.killNullLong2String(
									visitDistance, "500"));// 拜访距离

					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(locDesc));// 位置描述
					excelWorkBook.addCell(col++, row,
							CharTools.javaScriptEscape(cdate));// 标注时间
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
							+ "',");// poi类型0点1线2面
					jsonSb.append("poiDatas:'"
							+ CharTools.javaScriptEscape(poiDatas) + "',");// poi坐标
					jsonSb.append("poiEncryptDatas:'"
							+ CharTools.javaScriptEscape(poiEncryptDatas)
							+ "',");// poi加密坐标
					jsonSb.append("iconpath:'"
							+ CharTools.javaScriptEscape(iconpath) + "',");// poi图片地址
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(poiName) + "',");// poi名称
					jsonSb.append("poiDesc:'"
							+ CharTools.javaScriptEscape(poiDesc) + "',");// poi描述
					jsonSb.append("address:'"
							+ CharTools.javaScriptEscape(address) + "',");// poi地址
					jsonSb.append("telephone:'"
							+ CharTools.javaScriptEscape(telephone) + "',");// poi电话
					jsonSb.append("visitDistance:'"
							+ CharTools.killNullLong2String(visitDistance,
									"500") + "',");// 拜访距离，单位米
					jsonSb.append("layerId:'"
							+ CharTools.killNullLong2String(layerId, "") + "',");// 所属图层id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layerName) + "',");// 所属图层名称
					jsonSb.append("locDesc:'"
							+ CharTools.javaScriptEscape(locDesc) + "',");// 位置描述
					jsonSb.append("cDate:'" + CharTools.javaScriptEscape(cdate)
							+ "',");// 创建时间
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

	// sos未绑定任何终端的兴趣点列表
	@SuppressWarnings("unchecked")
	public ActionForward listNotRefPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		// 是否导出excel
		String expExcel = request.getParameter("expExcel");// true为导出
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
					excelWorkBook.addHeader("名称", 15);// poi名称
					excelWorkBook.addHeader("x坐标", 15);// x坐标
					excelWorkBook.addHeader("y坐标", 15);// y坐标
					excelWorkBook.addHeader("电话", 15);// 电话
					excelWorkBook.addHeader("地址", 25);// 地址
					excelWorkBook.addHeader("描述", 35);// 描述
					excelWorkBook.addHeader("拜访距离", 15);// 拜访距离
					lastLayerId = layerId;
				}
				// 同一图层
				if (lastLayerId.intValue() == layerId.intValue()) {
				} else {
					// 增加sheet
					excelWorkBook.addWorkSheet(CharTools
							.javaScriptEscape(layerName));
					excelWorkBook.addHeader("名称", 15);// poi名称
					excelWorkBook.addHeader("x坐标", 15);// x坐标
					excelWorkBook.addHeader("y坐标", 15);// y坐标
					excelWorkBook.addHeader("电话", 15);// 电话
					excelWorkBook.addHeader("地址", 25);// 地址
					excelWorkBook.addHeader("描述", 35);// 描述
					excelWorkBook.addHeader("拜访距离", 15);// 拜访距离
					row = 0;
				}
				int col = 0;
				row += 1;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poi.getPoiName()));// poi名称
				String poiDatas = poi.getPoiDatas();
				if (poiDatas == null)
					continue;
				int index = poiDatas.indexOf(",");
				if (index == -1)
					continue;
				String x = poiDatas.substring(0, index);
				String y = poiDatas.substring(index + 1);
				excelWorkBook
						.addCell(col++, row, CharTools.javaScriptEscape(x));// x坐标
				excelWorkBook
						.addCell(col++, row, CharTools.javaScriptEscape(y));// y坐标
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poi.getTelephone()));// 电话
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poi.getAddress()));// 地址
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(poi.getPoiDesc()));// 描述
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
						CharTools.killNullString(lx + ""));// x坐标
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(ly + ""));// y坐标
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(poi.getTelephone()));// 电话
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(poi.getAddress()));// 地址
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(locDesc));// 描述
				excelWorkBook.addCell(col++, row, CharTools
						.killNullLong2String(poi.getVisitDistance(), "500"));// 拜访距离
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
									"0") + "',");// poi类型0点1线2面
					jsonSb.append("poiDatas:'"
							+ CharTools.javaScriptEscape(poi.getPoiDatas())
							+ "',");// poi坐标
					jsonSb.append("poiEncryptDatas:'"
							+ CharTools.javaScriptEscape(poi
									.getPoiEncryptDatas()) + "',");// poi加密坐标
					jsonSb.append("iconpath:'"
							+ CharTools.javaScriptEscape(poi.getIconpath())
							+ "',");// poi图片地址
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(poi.getPoiName())
							+ "',");// poi名称
					jsonSb.append("poiDesc:'"
							+ CharTools.javaScriptEscape(poi.getPoiDesc())
							+ "',");// poi描述
					jsonSb.append("address:'"
							+ CharTools.javaScriptEscape(poi.getAddress())
							+ "',");// poi地址
					jsonSb.append("telephone:'"
							+ CharTools.javaScriptEscape(poi.getTelephone())
							+ "',");// poi电话
					jsonSb.append("visitDistance:'"
							+ CharTools.killNullLong2String(
									poi.getVisitDistance(), "500") + "',");// 拜访距离，单位米
					jsonSb.append("layerId:'"
							+ CharTools.killNullLong2String(layer.getId(), "")
							+ "',");// 所属图层id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layer.getLayerName())
							+ "',");// 所属图层名称
					jsonSb.append("cDate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(poi.getCdate())) + "'");// 创建时间
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

	// sos查询poi信息
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward queryPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String id = request.getParameter("id");// poi的id
		TPoi poi = layerAndPoiService.queryPoi(id);
		if (poi == null) {
			response.getWriter().write("{result:\"2\"}");// 查询失败
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
		jsonSb.append("deviceIds:'" + termDeviceIds + "'");// 管理终端deviceId
		response.getWriter().write("{" + jsonSb.toString() + "}");
		return mapping.findForward(null);
	}

	// sos新增兴趣点
	@SuppressWarnings("static-access")
	public ActionForward addPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String poiName = request.getParameter("poiName");// poi名称
		String poiDesc = request.getParameter("poiDesc");// poi描述
		String poiType = request.getParameter("poiType");// poi类型0点1线2面
		String poiDatas = request.getParameter("poiDatas");// poi坐标
		String poiEncryptDatas = request.getParameter("poiEncryptDatas");// poi加密坐标
		String telephone = request.getParameter("telephone");// 电话
		String address = request.getParameter("address");// 地址
		String iconpath = request.getParameter("iconpath");// 图片路径
		String visitDistanceStr = request.getParameter("visitDistance");// 访问距离，单位米，默认500米
		String layerId = request.getParameter("layerId");// 图层id
		String deviceIds = request.getParameter("deviceIds");// 关联人员deviceId，多个","隔开
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
		poiName = CharTools.killNullString(poiName);// 特殊字符处理
		poiDesc = CharTools.killNullString(poiDesc);// 特殊字符处理
		Long visitDistance = CharTools.str2Long(visitDistanceStr, 500L);// 默认500米
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
		String visible = "1";// 默认可见
		layerAndPoiService.addPoi(entCode, poiName, poiDesc, poiType, poiDatas,
				poiEncryptDatas, telephone, address, iconpath, visible,
				visitDistance, lId, deviceIdss, locDesc, deviceId);
		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos修改兴趣点
	public ActionForward updatePoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String id = request.getParameter("id");// poi的id
		String poiName = request.getParameter("poiName");// poi名称
		String poiDesc = request.getParameter("poiDesc");// poi描述
		String poiType = request.getParameter("poiType");// poi类型0点1线2面
		String poiDatas = request.getParameter("poiDatas");// poi坐标
		String poiEncryptDatas = request.getParameter("poiEncryptDatas");// poi加密坐标
		String telephone = request.getParameter("telephone");// 电话
		String address = request.getParameter("address");// 地址
		String iconpath = request.getParameter("iconpath");// 图片路径
		String visitDistanceStr = request.getParameter("visitDistance");// 访问距离，单位米，默认500米
		String layerId = request.getParameter("layerId");// 图层id

		poiName = CharTools.killNullString(poiName);
		poiName = java.net.URLDecoder.decode(poiName, "utf-8");
		poiName = CharTools.killNullString(poiName);

		String deviceIds = request.getParameter("deviceIds");// 关联人员deviceId，多个","隔开

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
		Long visitDistance = CharTools.str2Long(visitDistanceStr, 500L);// 默认500米
		String[] deviceIdss = CharTools.split(deviceIds, ",");
		Long lId = Long.parseLong(layerId);
		String visible = "1";// 默认可见
		Long poiId = Long.parseLong(id);
		layerAndPoiService.updatePoi(poiId, entCode, poiName, poiDesc, poiType,
				poiDatas, poiEncryptDatas, telephone, address, iconpath,
				visible, visitDistance, lId, deviceIdss);
		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// add by magiejue 2010-12-7 批量修改poi
	public ActionForward updatePoiBatch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}

		// 从request中获取参数
		// List<Long> ids = new ArrayList();
		String layerId = request.getParameter("poiLayer");// 图层id
		String poiIds = request.getParameter("poiIds");// poi的ids
		String iconpath = request.getParameter("iconpath");// 图片路径
		String visitDistanceStr = request.getParameter("range");// 访问距离，单位米，默认500米
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
				response.getWriter().write("{result:\"1\"}");// 成功
			} else {
				response.getWriter().write("{result:\"3\"}");// 失败
			}
		} else {
			response.getWriter().write("{result:\"3\"}");// 失败
		}
		return mapping.findForward(null);
	}

	// sos修改兴趣点属性（不修改与图层、终端绑定关系）
	public ActionForward updatePoiProperties(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String id = request.getParameter("id");// poi的id
		String poiName = request.getParameter("poiName");// poi名称
		String poiDesc = request.getParameter("poiDesc");// poi描述
		String poiType = request.getParameter("poiType");// poi类型0点1线2面
		String poiDatas = request.getParameter("poiDatas");// poi坐标
		String poiEncryptDatas = request.getParameter("poiEncryptDatas");// poi加密坐标
		String telephone = request.getParameter("telephone");// 电话
		String address = request.getParameter("address");// 地址
		String iconpath = request.getParameter("iconpath");// 图片路径
		String visitDistanceStr = request.getParameter("visitDistance");// 访问距离，单位米，默认500米

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
		Long visitDistance = CharTools.str2Long(visitDistanceStr, 500L);// 默认500米
		String visible = "1";// 默认可见
		Long poiId = Long.parseLong(id);
		layerAndPoiService.updatePoi(poiId, entCode, poiName, poiDesc, poiType,
				poiDatas, poiEncryptDatas, telephone, address, iconpath,
				visible, visitDistance);
		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos删除兴趣点
	public ActionForward deletePois(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String ids = request.getParameter("ids");// poi的id，多个","隔开
		String[] poiIds = CharTools.split(ids, ",");
		layerAndPoiService.deletePois(poiIds);
		response.getWriter().write("{result:\"1\"}");
		return mapping.findForward(null);
	}

	// sos可见图层列表
	@SuppressWarnings("unchecked")
	public ActionForward listVisibleLayer(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
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
							+ "',");// 图层名称
					jsonSb.append("mapLevel:'" + layer.getInfo1() + "',");// 图层地图级别
					jsonSb.append("visible:'" + refUserLayer.getVisible() + "'");// 设置是否登录加载0否1是
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

	// sos可见图层以及兴趣点列表
	@SuppressWarnings("unchecked")
	public ActionForward listVisibleLayerAndPoi(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// 从request中获取参数
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
							+ "',");// 图层名称
					jsonSb.append("mapLevel:'" + layer.getInfo1() + "',");// 图层地图级别
					jsonSb.append("editable:'"
							+ (layer.getUserId().intValue() == userId
									.intValue() ? "true" : "false") + "',");// 图层是否可修改（用户创建的方可修改）
					// poi列表
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
										poi.getPoiType(), "0") + "',");// poi类型0点1线2面
						jsonSb.append("poiDatas:'"
								+ CharTools.javaScriptEscape(poi.getPoiDatas())
								+ "',");// poi坐标
						jsonSb.append("poiEncryptDatas:'"
								+ CharTools.javaScriptEscape(poi
										.getPoiEncryptDatas()) + "',");// poi加密坐标
						jsonSb.append("iconpath:'"
								+ CharTools.javaScriptEscape(poi.getIconpath())
								+ "',");// poi图片地址
						jsonSb.append("poiName:'"
								+ CharTools.javaScriptEscape(poi.getPoiName())
								+ "',");// poi名称
						jsonSb.append("poiDesc:'"
								+ CharTools.javaScriptEscape(poi.getPoiDesc())
								+ "',");// poi描述
						jsonSb.append("address:'"
								+ CharTools.javaScriptEscape(poi.getAddress())
								+ "',");// poi地址
						jsonSb.append("visitDistance:'"
								+ CharTools.killNullLong2String(
										poi.getVisitDistance(), "500") + "',");// 拜访距离，单位米
						jsonSb.append("telephone:'"
								+ CharTools.javaScriptEscape(poi.getTelephone())
								+ "'");// poi电话
						jsonSb.append("},");
					}
					// 去掉最后的","
					int lastCommaIndex = jsonSb.lastIndexOf(",");
					if (lastCommaIndex == jsonSb.length() - 1) {
						jsonSb.deleteCharAt(lastCommaIndex);
					}
					jsonSb.append("]");
					jsonSb.append("},");
				}
				// 去掉最后的","
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos修改图层可见性（是否登录加载）
	public ActionForward updateLayerVisible(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String layerIds = request.getParameter("layerIds");// 需要登录加载的layer的id,多个","隔开
		String[] visibleRefUserLayerIds = CharTools.split(layerIds, ",");
		layerAndPoiService.updateLayerVisibleByUserId(entCode, userId,
				visibleRefUserLayerIds);
		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos根据终端id查关联poi(过滤用户可见的图层)
	public ActionForward queryPoiByDeviceId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");// 终端id

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
						+ "',");// poi类型0点1线2面
				jsonSb.append("poiDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiDatas()) + "',");// poi坐标
				jsonSb.append("poiEncryptDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiEncryptDatas())
						+ "',");// poi加密坐标
				jsonSb.append("iconpath:'"
						+ CharTools.javaScriptEscape(poi.getIconpath()) + "',");// poi图片地址
				jsonSb.append("poiName:'"
						+ CharTools.javaScriptEscape(poi.getPoiName()) + "',");// poi名称
				jsonSb.append("poiDesc:'"
						+ CharTools.javaScriptEscape(poi.getPoiDesc()) + "',");// poi描述
				jsonSb.append("address:'"
						+ CharTools.javaScriptEscape(poi.getAddress()) + "',");// poi地址
				jsonSb.append("visitDistance:'"
						+ CharTools.killNullLong2String(poi.getVisitDistance(),
								"500") + "',");// 拜访距离，单位米
				jsonSb.append("telephone:'"
						+ CharTools.javaScriptEscape(poi.getTelephone()) + "'");// poi电话
				jsonSb.append("},");
			}
			// 去掉最后的","
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos根据终端id查关联poi(过滤用户可管理的图层)
	public ActionForward queryManagePoiByDeviceId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");// 终端id
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
						+ "',");// poi类型0点1线2面
				jsonSb.append("poiDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiDatas()) + "',");// poi坐标
				jsonSb.append("poiEncryptDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiEncryptDatas())
						+ "',");// poi加密坐标
				jsonSb.append("iconpath:'"
						+ CharTools.javaScriptEscape(poi.getIconpath()) + "',");// poi图片地址
				jsonSb.append("poiName:'"
						+ CharTools.javaScriptEscape(poi.getPoiName()) + "',");// poi名称
				jsonSb.append("poiDesc:'"
						+ CharTools.javaScriptEscape(poi.getPoiDesc()) + "',");// poi描述
				jsonSb.append("address:'"
						+ CharTools.javaScriptEscape(poi.getAddress()) + "',");// poi地址
				jsonSb.append("visitDistance:'"
						+ CharTools.killNullLong2String(poi.getVisitDistance(),
								"500") + "',");// 拜访距离，单位米
				jsonSb.append("telephone:'"
						+ CharTools.javaScriptEscape(poi.getTelephone()) + "'");// poi电话
				jsonSb.append("},");
			}
			// 去掉最后的","
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(
				"{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}

	// sos批量修改兴趣点绑定关系
	public ActionForward updatePoiRefTerm(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String poiIds = request.getParameter("poiIds");// poi的id，多个","隔开
		String deviceIds = request.getParameter("deviceIds");// 关联人员deviceId，多个","隔开
		String[] poiIdss = CharTools.split(poiIds, ",");
		String[] deviceIdss = CharTools.split(deviceIds, ",");
		layerAndPoiService.updateRefTerm(poiIdss, deviceIdss);
		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos删除终端的所有绑定关系
	public ActionForward deletePoiRefTerm(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:'9'}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String poiIds = request.getParameter("poiIds");// poi的id，多个","隔开
		String deviceIds = request.getParameter("deviceIds");// 关联人员deviceId，多个","隔开
		String[] poiIdss = CharTools.split(poiIds, ",");
		String[] deviceIdss = CharTools.split(deviceIds, ",");

		// layerAndPoiService.deleteRefTerm(deviceIdss);
		layerAndPoiService.updateBinding(poiIdss, deviceIdss,
				LayerAndPoiService.reomve_binding);
		response.getWriter().write("{result:'1'}");// 成功
		return mapping.findForward(null);
	}

	// sos批量追加修改兴趣点绑定关系
	public ActionForward updatePoiRefTermForAdd(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:'9'}");// 未登录
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// 从request中获取参数
		String poiIds = request.getParameter("poiIds");// poi的id，多个","隔开
		String deviceIds = request.getParameter("deviceIds");// 关联人员deviceId，多个","隔开
		String[] poiIdss = CharTools.split(poiIds, ",");
		String[] deviceIdss = CharTools.split(deviceIds, ",");
		// 调用追加绑定
		// layerAndPoiService.updateRefTermForAdd(poiIdss, deviceIdss);
		layerAndPoiService.updateBinding(poiIdss, deviceIdss,
				LayerAndPoiService.add_binding);
		response.getWriter().write("{result:'1'}");// 成功
		return mapping.findForward(null);
	}

	// add by magiejue 2010-12-7查询所有标注点
	@SuppressWarnings("unchecked")
	public ActionForward allPois(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
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
						+ "',");// poi类型0点1线2面
				jsonSb.append("poiDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiDatas()) + "',");// poi坐标
				jsonSb.append("poiEncryptDatas:'"
						+ CharTools.javaScriptEscape(poi.getPoiEncryptDatas())
						+ "',");// poi加密坐标
				jsonSb.append("iconpath:'"
						+ CharTools.javaScriptEscape(poi.getIconpath()) + "',");// poi图片地址
				jsonSb.append("poiName:'"
						+ CharTools.javaScriptEscape(poi.getPoiName()) + "',");// poi名称
				jsonSb.append("poiDesc:'"
						+ CharTools.javaScriptEscape(poi.getPoiDesc()) + "',");// poi描述
				jsonSb.append("address:'"
						+ CharTools.javaScriptEscape(poi.getAddress()) + "',");// poi地址
				jsonSb.append("telephone:'"
						+ CharTools.javaScriptEscape(poi.getTelephone()) + "',");// poi电话
				jsonSb.append("visitDistance:'"
						+ CharTools.killNullLong2String(poi.getVisitDistance(),
								"500") + "',");// 拜访距离，单位米
				jsonSb.append("layerId:'"
						+ CharTools.killNullLong2String(layer.getId(), "")
						+ "',");// 所属图层id
				jsonSb.append("layerName:'"
						+ CharTools.javaScriptEscape(layer.getLayerName())
						+ "',");// 所属图层名称
				jsonSb.append("cDate:'"
						+ CharTools.javaScriptEscape(DateUtility
								.dateTimeToStr(poi.getCdate())) + "'");// 创建时间
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

	// add by zhaofeng 2011-10-24查询所有标注点---分页
	@SuppressWarnings("unchecked")
	public ActionForward listAllPois(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
														// HH:mm:ss
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
													// HH:mm:ss
		Date startDate = DateUtility.strToDateTime(st);
		Date endDate = DateUtility.strToDateTime(et);
		String poiLayer = request.getParameter("poiLayer") == null ? ""
				: request.getParameter("poiLayer");
		// 从request中获取参数
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
									"0") + "',");// poi类型0点1线2面
					jsonSb.append("poiDatas:'"
							+ CharTools.javaScriptEscape(poi.getPoiDatas())
							+ "',");// poi坐标
					jsonSb.append("poiEncryptDatas:'"
							+ CharTools.javaScriptEscape(poi
									.getPoiEncryptDatas()) + "',");// poi加密坐标
					jsonSb.append("iconpath:'"
							+ CharTools.javaScriptEscape(poi.getIconpath())
							+ "',");// poi图片地址
					jsonSb.append("poiName:'"
							+ CharTools.javaScriptEscape(poi.getPoiName())
							+ "',");// poi名称
					jsonSb.append("poiDesc:'"
							+ CharTools.javaScriptEscape(poi.getPoiDesc())
							+ "',");// poi描述
					jsonSb.append("address:'"
							+ CharTools.javaScriptEscape(poi.getAddress())
							+ "',");// poi地址
					jsonSb.append("telephone:'"
							+ CharTools.javaScriptEscape(poi.getTelephone())
							+ "',");// poi电话
					jsonSb.append("visitDistance:'"
							+ CharTools.killNullLong2String(
									poi.getVisitDistance(), "500") + "',");// 拜访距离，单位米
					jsonSb.append("layerId:'"
							+ CharTools.killNullLong2String(layer.getId(), "")
							+ "',");// 所属图层id
					jsonSb.append("layerName:'"
							+ CharTools.javaScriptEscape(layer.getLayerName())
							+ "',");// 所属图层名称
					jsonSb.append("cDate:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(poi.getCdate())) + "'");// 创建时间
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

	// 检验是否有重复标注点 add by zhaofeng 2011-10-22
	public ActionForward checkPoi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String tmpPoiName = request.getParameter("tmpPoiName");// 获取标注点名称
		tmpPoiName = java.net.URLDecoder.decode(tmpPoiName, "UTF-8");
		// StringBuffer jsonSb = new StringBuffer();
		// int total = 0;
		List<TPoi> list = layerAndPoiService.listPoiByPoiName(entCode,
				tmpPoiName);
		if (list != null && list.size() > 0) {
			response.getWriter().write("{result:\"2\"}");// 存在重复标注点
			return mapping.findForward(null);
		} else {
			response.getWriter().write("{result:\"1\"}");// 没有重复标注点
			return mapping.findForward(null);
		}
		// response.getWriter().write("{result:'1'}");// 没有重复标注点

	}
}
