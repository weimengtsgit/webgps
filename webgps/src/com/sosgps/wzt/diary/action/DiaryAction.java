package com.sosgps.wzt.diary.action;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;
import com.mapabc.geom.CoordCvtAPI;
import com.sosgps.wzt.diary.service.DiaryService;
import com.sosgps.wzt.locate.service.LocateService;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiary;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.terminal.service.impl.TerminalServiceImpl;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.Config;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.GeoUtils;
import com.sosgps.wzt.util.GeoUtils.GaussSphere;

public class DiaryAction extends DispatchWebActionSupport {
	private DiaryService diaryService = (DiaryService) SpringHelper.getBean("DiaryServiceImpl");
	private TerminalServiceImpl tTargetObjectService = (TerminalServiceImpl) SpringHelper.getBean("tTargetObjectService");
	LocateService tLoccateService = (LocateService) SpringHelper.getBean("tLoccateService");
	//private DiaryMarkService diaryMarkService = (DiaryMarkService) SpringHelper.getBean("DiaryMarkServiceImpl");
	
	public ActionForward listDiaryByDeviceIds(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		if(start == null || limit == null || st == null || et == null){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		String deviceIds = request.getParameter("deviceIds");
		deviceIds = CharTools.splitAndAdd(deviceIds);
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
			Page<Object[]> list = diaryService.listDiaryByDeviceIds(entCode, pageNo, pageSize, startTime, endTime, searchValue, deviceIds);
			if (list != null && list.getResult() != null && list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator.hasNext();) {
					Object[] objects = iterator.next();
					Long id = (Long) objects[0];
					String termName = (String) objects[1];
					String title = (String) objects[2];
					String content = (String) objects[3];
					String remark = (String) objects[4];
					Date createDate = (Date) objects[5];
					Date modifyDate = (Date) objects[6];
					Date diaryDate = (Date) objects[9];
					Date remarkDate = (Date) objects[10];
					String deviceId = (String) objects[11];
					termName = CharTools.javaScriptEscape(termName);
					title = CharTools.javaScriptEscape(title);
					content = CharTools.javaScriptEscape(content);
					remark = CharTools.javaScriptEscape(remark);
					String createDates = CharTools.javaScriptEscape(DateUtility.dateTimeToStr(createDate));
					String modifyDates = CharTools.javaScriptEscape(DateUtility.dateTimeToStr(modifyDate));
					String diaryDates = CharTools.javaScriptEscape(DateUtility.dateToStr(diaryDate));
					String remarkDates = CharTools.javaScriptEscape(DateUtility.dateToStr(remarkDate));
					jsonSb.append("{");
					jsonSb.append("id:'" + id + "',");
					jsonSb.append("termName:'" + termName + "',");
					jsonSb.append("title:'" + title + "',");
					jsonSb.append("content:'" + content + "',");
					jsonSb.append("remark:'" + remark + "',");
					jsonSb.append("createDate:'" + createDates + "',");
					jsonSb.append("modifyDate:'" + modifyDates + "',");
					jsonSb.append("entCode:'" + entCode + "',");
					jsonSb.append("userId:'" + userId + "',");
					jsonSb.append("diaryDate:'" + diaryDates + "',");
					jsonSb.append("remarkDate:'" + remarkDates + "',");
					jsonSb.append("deviceId:'" + deviceId + "'");
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		//System.out.println("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		response.getWriter().write("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
	    // 日志记录
	    TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.DIARY_SET);
		tOptLog.setFunCType(LogConstants.DIARY_SET_QUERY_USER);
		tOptLog.setOptDesc("查询登录业务员日志成功");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}
	
	public ActionForward listDiaryByUsers(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		//Long userId = userInfo.getUserId();
		// 从request中获取参数
		String start = request.getParameter("start") == null ? "0" : request.getParameter("start");
		String limit = request.getParameter("limit") == null ? "15" : request.getParameter("limit");
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);
		
		String termNames = request.getParameter("deviceIds");
		termNames = CharTools.killNullString(termNames);
		termNames = java.net.URLDecoder.decode(termNames, "utf-8");
		termNames = CharTools.killNullString(termNames);
		termNames = CharTools.splitAndAdd(termNames);
		int startint = Integer.parseInt(start);
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null && limit.length() > 0) {
			Page<Object[]> list = diaryService.listDiaryByDeviceIds(entCode, pageNo, pageSize, startTime, endTime, searchValue, termNames);
			if (list != null && list.getResult() != null && list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator<Object[]> iterator = list.getResult().iterator(); iterator.hasNext();) {
					Object[] objects = iterator.next();
					Long id = (Long) objects[0];
					String termName = (String) objects[1];
					String title = (String) objects[2];
					String content = (String) objects[3];
					String remark = (String) objects[4];
					Date createDate = (Date) objects[5];
					Date modifyDate = (Date) objects[6];
					String entCode_ = (String) objects[7];
					Long userId_ = (Long) objects[8];
					Date diaryDate = (Date) objects[9];
					Date remarkDate = (Date) objects[10];
					termName = CharTools.javaScriptEscape(termName);
					title = CharTools.javaScriptEscape(title);
					content = CharTools.javaScriptEscape(content);
					remark = CharTools.javaScriptEscape(remark);
					String createDates = CharTools.javaScriptEscape(DateUtility.dateTimeToStr(createDate));
					String modifyDates = CharTools.javaScriptEscape(DateUtility.dateTimeToStr(modifyDate));
					String diaryDates = CharTools.javaScriptEscape(DateUtility.dateToStr(diaryDate));
					String remarkDates = CharTools.javaScriptEscape(DateUtility.dateToStr(remarkDate));
					jsonSb.append("{");
					jsonSb.append("id:'" + id + "',");
					jsonSb.append("termName:'" + termName + "',");
					jsonSb.append("title:'" + title + "',");
					jsonSb.append("content:'" + content + "',");
					jsonSb.append("remark:'" + remark + "',");
					jsonSb.append("createDate:'" + createDates + "',");
					jsonSb.append("modifyDate:'" + modifyDates + "',");
					jsonSb.append("entCode:'" + entCode_ + "',");
					jsonSb.append("userId:'" + userId_ + "',");
					jsonSb.append("diaryDate:'" + diaryDates + "',");
					jsonSb.append("remarkDate:'" + remarkDates + "'");
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
		}
		response.setContentType("text/json; charset=utf-8");
		//System.out.println("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		response.getWriter().write("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
	    // 日志记录
	    TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.DIARY_SET);
		tOptLog.setFunCType(LogConstants.DIARY_SET_QUERY_USER);
		tOptLog.setOptDesc("查询登录业务员日志成功");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}
	
	public ActionForward addDiary(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String st = request.getParameter("startTime");// 开始时间，格式yyyy-MM-dd
		String et = request.getParameter("endTime");// 结束时间，格式yyyy-MM-dd
		String deviceId = request.getParameter("deviceId");
		String termName = request.getParameter("termName");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		if(deviceId == null || deviceId.length()<=0 || st == null || st.length()<=0 || et == null || et.length()<=0){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		Date startTime = DateUtility.strToDateTime(st);
		Date endTime = DateUtility.strToDateTime(et);
		Page<Object[]> list = diaryService.listDiaryByDeviceId(entCode, userId, 1, 65535, startTime, endTime, "", deviceId);
		if (list != null && list.getResult() != null && list.getResult().size() > 0) {
			response.getWriter().write("{result:\"3\"}");//已存在这天的日志
			return mapping.findForward(null);
		}
		String lng = request.getParameter("lng");
		String lat = request.getParameter("lat");
		String[] lngs = lng.split(",");
		String[] lats = lat.split(",");
	
	
		termName = CharTools.killNullString(termName);
		title = CharTools.killNullString(title);
		content = CharTools.killNullString(content);
		
		termName = java.net.URLDecoder.decode(termName, "utf-8");
		title = java.net.URLDecoder.decode(title, "utf-8");
		content = java.net.URLDecoder.decode(content, "utf-8");
		
		
	
		termName = CharTools.killNullString(termName);
		title = CharTools.killNullString(title);
		content = CharTools.killNullString(content);
		
		boolean flag = diaryService.saveTDiary(userInfo, lngs, lats, startTime, 
				deviceId, entCode, userId, termName, title, content);
		if(flag){
			response.getWriter().write("{result:\"1\"}");//成功
		}else{
			response.getWriter().write("{result:\"2\"}");//失败
		}
		return mapping.findForward(null);
	}
	
	public ActionForward updateDiary(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String deviceId = request.getParameter("deviceId");
		String termName = request.getParameter("termName");
		if(deviceId == null || id == null){
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String lng = request.getParameter("lng");
		String lat = request.getParameter("lat");
		String[] lngs = lng.split(",");
		String[] lats = lat.split(",");
		
		title = CharTools.killNullString(title);
		content = CharTools.killNullString(content);
		
		title = java.net.URLDecoder.decode(title, "utf-8");
		content = java.net.URLDecoder.decode(content, "utf-8"); 
		
		title = CharTools.killNullString(title);
		content = CharTools.killNullString(content);
		
		boolean f = diaryService.updateTDiary(id, userInfo, lngs, lats, 
				deviceId, entCode, userId, termName, title, content);
		if(f){
			response.getWriter().write("{result:\"1\"}");// 操作成功
			return mapping.findForward(null);
		}else{
			response.getWriter().write("{result:\"2\"}");// 操作失败
			return mapping.findForward(null);
		}
	}

	public ActionForward remarkDiary(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String id = request.getParameter("id");
		String remark = request.getParameter("remark");
		remark = CharTools.killNullString(remark);
		remark = java.net.URLDecoder.decode(remark, "utf-8");
		remark = CharTools.killNullString(remark);
		TDiary tDiary = diaryService.findById(Long.parseLong(id));
		//修改
		tDiary.setRemark(remark);
		tDiary.setRemarkDate(new Date());
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.DIARY_SET);
		tOptLog.setFunCType(LogConstants.DIARY_SET_REMARK_USER);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("批注业务员日志成功! ");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		boolean f = diaryService.update(tDiary);
		if(f){
			response.getWriter().write("{result:\"1\"}");// 操作成功
			return mapping.findForward(null);
		}else{
			response.getWriter().write("{result:\"2\"}");// 操作失败
			return mapping.findForward(null);
		}
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public ActionForward queryTrack(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String termName = request.getParameter("termName");
		if(termName == null || termName.length() <= 0){
			termName = userInfo.getUser().getUserName();
		}
		
		termName = CharTools.killNullString(termName);
		termName = java.net.URLDecoder.decode(termName, "utf-8");
		termName = CharTools.killNullString(termName);
		String startTime = request.getParameter("startTime");
		String[] startTimes = startTime.split(" ");
		startTime = startTimes[0];
		List<Object> list = tTargetObjectService.findTerminalByTermName(termName, entCode, true);
		if (list != null && list.size() > 0) {
			TTerminal tTerminal = (TTerminal)list.get(0);
			String deviceId = tTerminal.getDeviceId();
			String startTime_ = startTime+" "+tTerminal.getStartTime()+":00";
			String endTime_ = startTime+" "+tTerminal.getEndTime()+":00";
			String vehicleNumber = tTerminal.getVehicleNumber();
			String simcard = tTerminal.getSimcard();
			String locateType = tTerminal.getLocateType();
			StringBuffer jsonSb = new StringBuffer();
			List<TLocrecord> locs = tLoccateService.queryLocsByTime(deviceId, startTime_, endTime_);
			jsonSb.append("[");
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
			if (locs != null) {
				double lastX = 0;
				double lastY = 0;
				Date lastGtime = null;
				double lastLc = 0;
				//double lastlngX = -1;
				//double lastLatY = -1;
				String lastPosDesc = "";
				for (Iterator<TLocrecord> iterator = locs.iterator(); iterator.hasNext();) {
					try {
						TLocrecord tloc = (TLocrecord) iterator.next();
						// 计算里程差
						double x = tloc.getLongitude();
						double y = tloc.getLatitude();
						Date gtime = tloc.getGpstime();
						// 计算与上一个点距离
						double distance = 0;
						if (lastX > 0 && lastY > 0 && x > 0 && y > 0) {
							String trackFilter = Config.getInstance().getString("config.properties", "trackFilter");
							if (trackFilter != null && trackFilter.equalsIgnoreCase("true")) {
								String filterSpeed = Config.getInstance().getString("config.properties", "filterSpeed");
								int speedmax = CharTools.str2Integer(filterSpeed, 200);// 默认200
								// 去除漂移
								distance = GeoUtils.DistanceOfTwoPoints(lastX, lastY, x, y, GaussSphere.WGS84);
								distance /= 1000;// 单位换算成km
								if (distance == 0) {
									continue;
								}
								int betweenTime = DateUtility.betweenSecond(gtime, lastGtime);// 时间差，单位秒
								if (betweenTime == 0) {
									continue;
								}
								double realSpeed = (distance / betweenTime)*60*60;
								if (realSpeed > speedmax) {
									lastX = x;
									lastY = y;
									continue;
								}
							}
							// gps使用里程数据相减，手机终端使用计算两点距离
							if (tloc.getLocateType() != null && tloc.getLocateType().equals("0")) {// 手机
								distance = GeoUtils.DistanceOfTwoPoints(lastX, lastY, x, y, GaussSphere.WGS84);
								distance /= 1000;// 单位换算成km
							} else {// gps
								if(tloc.getDistance() ==null ){
									distance = GeoUtils.DistanceOfTwoPoints(lastX, lastY, x, y, GaussSphere.WGS84);
									distance /= 1000;// 单位换算成km
								}
								else if(tloc.getDistance() ==0 ){
									distance = GeoUtils.DistanceOfTwoPoints(lastX, lastY, x, y, GaussSphere.WGS84);
									distance /= 1000;// 单位换算成km
								}
								else if(lastLc == 0){
									distance = GeoUtils.DistanceOfTwoPoints(lastX, lastY, x, y, GaussSphere.WGS84);
									distance /= 1000;// 单位换算成km
								}
								else{
									distance = tloc.getDistance() - lastLc;
								}
							}
						}
						lastX = x;
						lastY = y;
						lastGtime = gtime;
						lastLc = tloc.getDistance() == null ? 0 : tloc.getDistance();
						// 位置描述
						String posDesc = "";
						if (tloc.getLongitude() > 0 && tloc.getLatitude() > 0) {
							if (tloc.getLocDesc() == null || tloc.getLocDesc().equals("")) {
								if (tloc.getJmx() == null || tloc.getJmx().equals("")) {
									double[] xs = { tloc.getLongitude() };
									double[] ys = { tloc.getLatitude() };
									com.sos.sosgps.api.DPoint[] dPoint = null;
									String lngX = "";
									String latY ="";
									try{
										dPoint = coordApi.encryptConvert(xs, ys);
										 lngX = dPoint[0].getEncryptX();
										 latY = dPoint[0].getEncryptY();
									}catch(Exception ex){
										System.out.println("track-encryptConvert error,"+ex.getMessage());
									}
									tloc.setJmx(lngX);
									tloc.setJmy(latY);
								}
								//if (lastlngX != tloc.getLongitude() && lastLatY != tloc.getLatitude()) {
									// 取得位置描述
									//posDesc = coordCvtApi.getAddress(tloc.getJmx(), tloc.getJmy());

								//}
								//lastlngX = tloc.getLongitude();
								//lastLatY = tloc.getLatitude();
							} else {
								posDesc = tloc.getLocDesc();
							}
						}
						lastPosDesc = posDesc;
						// add 刘源 2010-07-31 增加行车状态 acc 当为空时 默认 为行车状态
						float speed = tloc.getSpeed() == null ? 0 : tloc.getSpeed();
						String accStatus = tloc.getAccStatus();
						// 当accStatus 为空时 判断速度是否大于0 当大于0 时 acc状态为开 否则为关闭
						if (accStatus == null) {
							if (speed > 0) {
								accStatus = "1";
							} else {
								accStatus = "0";
							}
						}
						// end add
						jsonSb.append("{");
						jsonSb.append("id:'" + tloc.getId() + "',");// id
						jsonSb.append("deviceId:'" + tloc.getDeviceId() + "',");// id
						jsonSb.append("x:'" + tloc.getLongitude() + "',");// x
						jsonSb.append("y:'" + tloc.getLatitude() + "',");// y
						jsonSb.append("poiName:'" + tloc.getPoiName() + "',");// poiName
						java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.00");
						jsonSb.append("distance:'" + myformat.format(distance) + "',");// 与上一个点距离，单位km
						//add magiejue 2010-11-23
						jsonSb.append("termName:'" + termName + "',");//终端名
						jsonSb.append("vNumber:'" + vehicleNumber + "',");//车牌号
						jsonSb.append("simCard:'" + simcard + "',");//sim卡号
						jsonSb.append("locType:'" + locateType + "',");// 信息记录类型（车载或手机）
						jsonSb.append("direction:'" + tloc.getDirection() + "',");//方向
						jsonSb.append("jmx:'" + CharTools.javaScriptEscape(tloc.getJmx()) + "',");// jmx
						jsonSb.append("jmy:'" + CharTools.javaScriptEscape(tloc.getJmy()) + "',");// jmy
						jsonSb.append("deflectionx:'" + coordCvtApi.decrypt(tloc.getJmx()) + "',");// 偏转x坐标
						jsonSb.append("deflectiony:'" + coordCvtApi.decrypt(tloc.getJmy()) + "',");// 偏转y坐标
						jsonSb.append("speed:'" + CharTools.killNullFloat2String(tloc.getSpeed(), "0") + "',");// speed
						jsonSb.append("pd:'" + CharTools.javaScriptEscape(CharTools .replayStr(lastPosDesc)) + "',");// position desc 替换 ' 为 ‘
						jsonSb.append("time:'" + CharTools.javaScriptEscape(DateUtility.dateTimeToStr(tloc.getGpstime())) + "',");// time
						// add 刘源 2010-07-31 增加行车状态 acc
						jsonSb.append("accStatus:'" + CharTools.javaScriptEscape(accStatus) + "',");// accStatus
						jsonSb.append("temperature:'" + CharTools.killNullFloat2String( tloc.getTemperature(), "") + "'");// temperature
						// end
						jsonSb.append("},");
					} catch (Exception e) {
					}
				}
				if (jsonSb.length() > 1) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			}
			jsonSb.append("]");
			 //System.out.println(jsonSb.toString());
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write(jsonSb.toString());
			// 日志记录
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.TRACK_REPLAY);
			tOptLog.setFunCType(LogConstants.TRACK_REPLAY_QUERY);
			tOptLog.setOptDesc("轨迹查询成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			return mapping.findForward(null);
		}else{
			response.getWriter().write("{result:\"3\"}");// 未查到业务员对应的终端
			return mapping.findForward(null);
		}
		
	}
}
