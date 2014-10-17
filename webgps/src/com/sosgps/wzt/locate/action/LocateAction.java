package com.sosgps.wzt.locate.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.sos.sosgps.api.CoordAPI;
import com.sosgps.wzt.locate.bean.Position;
import com.sosgps.wzt.locate.service.LocateService;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.terminalgroup.service.TerminalGroupManageService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TLocrecord;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.terminal.service.TerminalService;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.Config;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.DriftFilter;
import com.sosgps.wzt.util.GeoUtils;
import com.sosgps.wzt.util.GeoUtils.GaussSphere;
import com.sosgps.wzt.util.PointBean;

public class LocateAction extends DispatchWebActionSupport {
	LocateService tLoccateService = (LocateService) SpringHelper
			.getBean("tLoccateService");
	TerminalService tTargetObjectService = (TerminalService) SpringHelper
			.getBean("tTargetObjectService");

	/**
	 * ת���ʼҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Object obj = request.getSession().getAttribute("userInfo");
		if (obj != null && obj instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) obj;
			String entCode = userInfo.getEmpCode();
			// �����ն���
			TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
					.getBean("TerminalGroupManageServiceImpl");
			List list = terminalGroupManageService.viewEntTermGroup(entCode);
			request.setAttribute("viewEntTermGroup", list);
		}
		return mapping.findForward("init");
	}

	public ActionForward viewLastLoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			Object obj = request.getSession().getAttribute("userInfo");
			String deviceIds = request.getParameter("deviceIds");
			String locs = this.tLoccateService.findLastLoc(deviceIds);
			if (locs != null) {
				// System.out.println("Last Locate XML is: " + locs);
				response.setContentType("text/xml;charset=GBK");
				response.getWriter().write(locs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ѯ�ն����λ��
	 */
	public ActionForward queryClosestLocator(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserInfo userInfo = (UserInfo) request.getSession(true).getAttribute(
				"userInfo");
		try {

			String entCode = userInfo.getEnt().getEntCode();
			String gpsDeviceIds = request.getParameter("gpsDeviceIds");
			String lbsDeviceIds = request.getParameter("lbsDeviceIds");
			// System.out.println("EntCode =====: " + entCode);
			// System.out.println("GpsDeviceIds =====: " + gpsDeviceIds);
			// System.out.println("LbsDeviceIds =====: " + lbsDeviceIds);
			String locs = tLoccateService.queryClosestLocSelectedTerm(entCode,
					gpsDeviceIds, lbsDeviceIds, userInfo.getUserAccount());
			if (locs != null) {
				System.out.println("Closest Locate XML is: " + locs);
				response.setContentType("text/xml;charset=GBK");
				response.getWriter().write(locs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public ActionForward positionDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// UserInfo userInfo = (UserInfo)
		// request.getSession(true).getAttribute("userInfo");
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		String positionDesc = tLoccateService.positionDesc(x, y);
		response.setContentType("text/xml;charset=UTF-8");
		response.getWriter().write("<p>" + positionDesc + "</p>");

		return null;
	}

	/**
	 * add by zhaofeng 2011215
	 */

	// sos�����ʱ���λ������
	public ActionForward queryLocByTime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��request�л�ò���
		String gpsDeviceIds = request.getParameter("gpsDeviceIds");// GPS�ն�deviceid�������","����
		String lbsDeviceIds = request.getParameter("lbsDeviceIds");// �ֻ��ն�deviceid�������","����
		String now = request.getParameter("now");// true�뵱ǰʱ�����
		String time = request.getParameter("time");// ʱ�䣬��ʽyyyy-mm-dd HH:MM

		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		CoordCvtAPI coordCvtApi = new CoordCvtAPI();
		com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
		int carGreyInterval = userInfo.getEnt().getCarGreyInterval() == null ? -10 : -(userInfo.getEnt().getCarGreyInterval().intValue()/60);
		int persionGreyInterval = userInfo.getEnt().getPersionGreyInterval() == null ? -15 : -(userInfo.getEnt().getPersionGreyInterval().intValue()/60);
		List locs = null;
		// json
		StringBuffer jsonSb = new StringBuffer();
		if (now != null && now.equalsIgnoreCase("true")) {// ��ǰʱ��
			// gps
			locs = tLoccateService.queryLocByTime(gpsDeviceIds);
			jsonSb.append("[");

			if (locs != null) {
				for (Iterator iterator = locs.iterator(); iterator.hasNext();) {
					TLastLocrecord tloc = (TLastLocrecord) iterator.next();
					double[] xs = { tloc.getLongitude() };
					double[] ys = { tloc.getLatitude() };
					// DPoint[] dPoint = coordCvtApi.encryptConvert(xs, ys);
					com.sos.sosgps.api.DPoint[] dPoint = null;
					String lngX = "";
					String latY = "";
					double x = 0;
					double y = 0;
					try {
						dPoint = coordApi.encryptConvert(xs, ys);
						lngX = dPoint[0].getEncryptX();
						latY = dPoint[0].getEncryptY();
						x = dPoint[0].x;
						y = dPoint[0].y;
					} catch (Exception ex) {
						System.out
								.println("queryLocByTime-encryptConvert error,"
										+ ex.getMessage());
					}

					// ȡ��λ������
					String posDesc = "";
					/*
					 * if (x > 0 && y > 0) { try{ posDesc =
					 * coordCvtApi.getAddress(lngX, latY); }catch(Exception ex){
					 * System.out.println("queryLocByTime-getAddress
					 * error,"+ex.getMessage()); } }
					 */
					// �ж�ʱ�䣬����24Сʱ��acc״̬������24Сʱ��acc״̬��Ч��24Сʱ��������
					int status;// 1��2��3�ֱ��Ӧ24Сʱ��acc״̬������24Сʱ��acc״̬��Ч��24Сʱ��������
					Date gpstime = tloc.getGpstime();
					Calendar c = Calendar.getInstance();
					//24Сʱ���
					//c.add(Calendar.HOUR_OF_DAY, -24);
					//10���ӱ��
					//c.add(Calendar.MINUTE, -10);
					c.add(Calendar.MINUTE, carGreyInterval);
					
					if (gpstime.after(c.getTime())) {// 24Сʱ����GPS����
						// �ж�24Сʱ��acc״̬
						if (tloc.getAccStatus() != null
								&& tloc.getAccStatus().equals("1")) {
							status = 1;// acc״̬����
						} else {
							status = 2;// acc״̬��Ч
						}
					} else {// 24Сʱ��������
						status = 3;// acc״̬��Ч
					}
					jsonSb.append("{");
					jsonSb.append("id:'" + tloc.getId() + "',");// id
					jsonSb.append("deviceId:'" + tloc.getDeviceId() + "',");// id
					jsonSb.append("type:'1',");// type:0LBS1GPS
					jsonSb.append("x:'" + tloc.getLongitude() + "',");// x
					jsonSb.append("y:'" + tloc.getLatitude() + "',");// y
					jsonSb.append("jmx:'" + CharTools.killNullString(lngX)
							+ "',");// jmx
					jsonSb.append("jmy:'" + CharTools.killNullString(latY)
							+ "',");// jmy
					jsonSb.append("status:'" + status + "',");// status
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(tloc.getSpeed(),
									"0") + "',");// speed
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "',");// position desc
					jsonSb.append("time:'"
							+ CharTools.killNullString(DateUtility
									.dateTimeToStr(tloc.getGpstime())) + "',");// time
					jsonSb.append("lastTime:'"
							+ CharTools.killNullString(DateUtility
									.dateTimeToStr(tloc.getLastTime())) + "',");// lastTime
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(tloc
									.getTemperature(), "") + "',");// temperature
					//add by yuanchao @2014-01-17
					jsonSb.append("direction:'"
							+ CharTools.killNullFloat2String(tloc
									.getDirection(), "") + "'");// direction
					jsonSb.append("},");
				}
			}
			locs = null;
			// �ֻ�
			locs = tLoccateService.queryLocByTime(lbsDeviceIds);
			if (locs != null) {
				for (Iterator iterator = locs.iterator(); iterator.hasNext();) {
					TLastLocrecord tloc = (TLastLocrecord) iterator.next();
					double[] xs = { tloc.getLongitude() };
					double[] ys = { tloc.getLatitude() };
					// DPoint[] dPoint = coordCvtApi.encryptConvert(xs, ys);
					com.sos.sosgps.api.DPoint[] dPoint = null;
					String lngX = "";
					String latY = "";
					double x = 0;
					double y = 0;
					try {
						dPoint = coordApi.encryptConvert(xs, ys);
						lngX = dPoint[0].getEncryptX();
						latY = dPoint[0].getEncryptY();
						x = dPoint[0].x;
						y = dPoint[0].y;
					} catch (Exception ex) {
						System.out
								.println("queryLocByTime-encryptConvert error,"
										+ ex.getMessage());
					}
					// ȡ��λ������
					String posDesc = "";
					/*
					 * if (x > 0 && y > 0) { posDesc =
					 * coordCvtApi.getAddress(lngX, latY); }
					 */
					// �ж�ʱ�䣬����1Сʱ������ЧGPS���ݣ�1Сʱ������ЧGPS���ݣ�1Сʱ��������
					int status;// 1��2��3�ֱ��Ӧ1Сʱ������Ч���ݡ�1Сʱ������Ч���ݡ�1Сʱ��������
					Date gpstime = tloc.getGpstime();

					/*
					 * Calendar c = Calendar.getInstance();
					 * c.add(Calendar.HOUR_OF_DAY, -1); if
					 * (gpstime.after(c.getTime())) {// 1Сʱ������ЧGPS���� status = 1; }
					 * else {// 1Сʱ������Ч���� // �ж�1Сʱ���Ƿ�����Ч���� if
					 * (tloc.getLastTime().after(c.getTime())) { status = 2;//
					 * ����Ч���� } else { status = 3;// ����Ч���� } }
					 */

					Calendar c = Calendar.getInstance();
					c.add(Calendar.HOUR_OF_DAY, -1);
					if (gpstime.after(c.getTime())) {
						// 1Сʱ����GPS���ݸ���
						Calendar cc = Calendar.getInstance();
						//cc.add(Calendar.MINUTE, -15);
						cc.add(Calendar.MINUTE, persionGreyInterval);
						
						if (gpstime.after(cc.getTime())) {
							status = 1;// 15����������ЧGPS����Ϊ���
						} else {
							status = 2;// 15����������Ч����Ϊ����
						}
					} else {
						// 1Сʱ����GPS���ݸ���
						if (tloc.getLastTime().after(c.getTime())) {
							status = 2;// 1Сʱ��������Ϊ����
						} else {
							status = 3;// 1Сʱ��������Ϊ�ҵ�
						}
					}

					jsonSb.append("{");
					jsonSb.append("id:'" + tloc.getId() + "',");// id
					jsonSb.append("deviceId:'" + tloc.getDeviceId() + "',");// id
					jsonSb.append("type:'0',");// type:0LBS1GPS
					jsonSb.append("x:'" + tloc.getLongitude() + "',");// x
					jsonSb.append("y:'" + tloc.getLatitude() + "',");// y
					jsonSb.append("jmx:'" + CharTools.killNullString(lngX)
							+ "',");// jmx
					jsonSb.append("jmy:'" + CharTools.killNullString(latY)
							+ "',");// jmy
					jsonSb.append("status:'" + status + "',");// status
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(tloc.getSpeed(),
									"0") + "',");// speed
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "',");// position desc
					jsonSb.append("time:'"
							+ CharTools.killNullString(DateUtility
									.dateTimeToStr(tloc.getGpstime())) + "',");// time
					jsonSb.append("lastTime:'"
							+ CharTools.killNullString(DateUtility
									.dateTimeToStr(tloc.getLastTime())) + "',");// lastTime
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(tloc
									.getTemperature(), "") + "'");// temperature
					jsonSb.append("},");
				}
			}
			if (jsonSb.length() > 1) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
			jsonSb.append("]");
		} else {// ��ѡʱ��
			// gps
			locs = tLoccateService.queryLocByTime(gpsDeviceIds, time);
			jsonSb.append("[");
			// CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			if (locs != null) {
				for (Iterator iterator = locs.iterator(); iterator.hasNext();) {
					TLocrecord tloc = (TLocrecord) iterator.next();
					// λ������
					String posDesc = "";
					if (tloc.getLongitude() > 0 && tloc.getLatitude() > 0) {
						if (tloc.getLocDesc() == null
								|| tloc.getLocDesc().equals("")) {
							if (tloc.getJmx() == null
									|| tloc.getJmx().equals("")) {
								double[] xs = { tloc.getLongitude() };
								double[] ys = { tloc.getLatitude() };
								// DPoint[] dPoint = coordCvtApi.encryptConvert(
								// xs, ys);
								// String lngX = dPoint[0].getEncryptX();
								// String latY = dPoint[0].getEncryptY();
								// double x = dPoint[0].x;
								// double y = dPoint[0].y;

								com.sos.sosgps.api.DPoint[] dPoint = null;
								String lngX = "";
								String latY = "";
								double x = 0;
								double y = 0;
								try {
									dPoint = coordApi.encryptConvert(xs, ys);
									lngX = dPoint[0].getEncryptX();
									latY = dPoint[0].getEncryptY();
									x = dPoint[0].x;
									y = dPoint[0].y;
								} catch (Exception ex) {
									System.out
											.println("queryLocByTime-encryptConvert error,"
													+ ex.getMessage());
								}

								tloc.setJmx(lngX);
								tloc.setJmy(latY);
							}
							// ȡ��λ������
							// posDesc = coordCvtApi.getAddress(tloc.getJmx(),
							// tloc.getJmy());
						} else {
							posDesc = tloc.getLocDesc();
						}
					}
					// �ж�ʱ�䣬����1Сʱ������ЧGPS���ݣ�1Сʱ������ЧGPS���ݣ�1Сʱ��������
					int status = 1;// 1��2��3�ֱ��Ӧ1Сʱ������Ч���ݡ�1Сʱ������Ч���ݡ�1Сʱ��������
					jsonSb.append("{");
					jsonSb.append("id:'" + tloc.getId() + "',");// id
					jsonSb.append("deviceId:'" + tloc.getDeviceId() + "',");// id
					jsonSb.append("type:'1',");// type:0LBS1GPS
					jsonSb.append("x:'" + tloc.getLongitude() + "',");// x
					jsonSb.append("y:'" + tloc.getLatitude() + "',");// y
					jsonSb.append("jmx:'"
							+ CharTools.killNullString(tloc.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.killNullString(tloc.getJmy()) + "',");// jmy
					jsonSb.append("status:'" + status + "',");// status
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(tloc.getSpeed(),
									"0") + "',");// speed
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "',");// position desc
					jsonSb.append("time:'"
							+ CharTools.killNullString(DateUtility
									.dateTimeToStr(tloc.getGpstime())) + "',");// time
					jsonSb.append("lastTime:'" + "null" + "',");// �������ѡʱ��ʹ���ǰ̨ҳ�治��ʾ
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(tloc
									.getTemperature(), "") + "'");// temperature
					jsonSb.append("},");
				}
			}
			locs = null;
			// �ֻ�
			locs = tLoccateService.queryLocByTime(lbsDeviceIds, time);
			if (locs != null) {
				for (Iterator iterator = locs.iterator(); iterator.hasNext();) {
					TLocrecord tloc = (TLocrecord) iterator.next();
					// λ������
					String posDesc = "";
					if (tloc.getLongitude() > 0 && tloc.getLatitude() > 0) {
						if (tloc.getLocDesc() == null
								|| tloc.getLocDesc().equals("")) {
							if (tloc.getJmx() == null
									|| tloc.getJmx().equals("")) {
								double[] xs = { tloc.getLongitude() };
								double[] ys = { tloc.getLatitude() };
								// DPoint[] dPoint = coordCvtApi.encryptConvert(
								// xs, ys);
								// String lngX = dPoint[0].getEncryptX();
								// String latY = dPoint[0].getEncryptY();
								// double x = dPoint[0].x;
								// double y = dPoint[0].y;

								com.sos.sosgps.api.DPoint[] dPoint = null;
								String lngX = "";
								String latY = "";
								double x = 0;
								double y = 0;
								try {
									dPoint = coordApi.encryptConvert(xs, ys);
									lngX = dPoint[0].getEncryptX();
									latY = dPoint[0].getEncryptY();
									x = dPoint[0].x;
									y = dPoint[0].y;
								} catch (Exception ex) {
									System.out
											.println("queryLocByTime-encryptConvert error,"
													+ ex.getMessage());
								}

								tloc.setJmx(lngX);
								tloc.setJmy(latY);
							}
							// ȡ��λ������
							// posDesc = coordCvtApi.getAddress(tloc.getJmx(),
							// tloc.getJmy());
						} else {
							posDesc = tloc.getLocDesc();
						}
					}
					// �ж�ʱ�䣬����1Сʱ������ЧGPS���ݣ�1Сʱ������ЧGPS���ݣ�1Сʱ��������
					int status = 1;// 1��2��3�ֱ��Ӧ1Сʱ������Ч���ݡ�1Сʱ������Ч���ݡ�1Сʱ��������
					// Date gpstime = tloc.getGpstime();
					// Calendar c = Calendar.getInstance();
					// c.add(Calendar.HOUR_OF_DAY, -1);
					// if(gpstime.after(c.getTime())){// 1Сʱ������ЧGPS����
					// status = 1;
					// }else{// 1Сʱ������ЧGPS����
					// // �ж��Ƿ�����Ч����
					// if(tloc.getSignalFlag()!=null &&
					// tloc.getSignalFlag().equals("1")){
					// status = 2;// ����Ч����
					// }else{
					// status = 3;// ����Ч����
					// }
					// }
					jsonSb.append("{");
					jsonSb.append("id:'" + tloc.getId() + "',");// id
					jsonSb.append("deviceId:'" + tloc.getDeviceId() + "',");// id
					jsonSb.append("type:'0',");// type:0LBS1GPS
					jsonSb.append("x:'" + tloc.getLongitude() + "',");// x
					jsonSb.append("y:'" + tloc.getLatitude() + "',");// y
					jsonSb.append("jmx:'"
							+ CharTools.killNullString(tloc.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.killNullString(tloc.getJmy()) + "',");// jmy
					jsonSb.append("status:'" + status + "',");// status
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(tloc.getSpeed(),
									"0") + "',");// speed
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "',");// position desc
					jsonSb.append("time:'"
							+ CharTools.killNullString(DateUtility
									.dateTimeToStr(tloc.getGpstime())) + "',");// time
					jsonSb.append("lastTime:'" + "null" + "',");// �������ѡʱ��ʹ���ǰ̨ҳ�治��ʾ
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(tloc
									.getTemperature(), "") + "'");// temperature
					jsonSb.append("},");
				}
			}
			if (jsonSb.length() > 1) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
			jsonSb.append("]");
		}

		// System.out.println(jsonSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(jsonSb.toString());

		// ��־��¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.TRACK_REPLAY);
		tOptLog.setFunCType(LogConstants.TRACK_REPLAY_REALTIEMLOC);
		tOptLog.setOptDesc("��ʱ��λ���ݲ�ѯ�ɹ�");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}

	// sos�켣�ط�
	public ActionForward track(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��request�л�ò���
		String deviceId = request.getParameter("deviceId");// �ն�deviceid
		String startDate = request.getParameter("startDate");// ��ʼ���ڣ���ʽyyyy-mm-dd
		String endDate = request.getParameter("endDate");// �������ڣ���ʽyyyy-mm-dd
		String startTime = request.getParameter("startTime");// ��ʼʱ�䣬��ʽHH:MM
		String endTime = request.getParameter("endTime");// ����ʱ�䣬��ʽHH:MM
		String filterStar = request.getParameter("filterStar");// �������� add by
		// liuhongxiao
		// 2011-12-02
		UserInfo userInfo = this.getCurrentUser(request);
		if (deviceId == null || startDate == null || endDate == null
				|| startTime == null || endTime == null || filterStar == null
				|| userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("");
			// ��־��¼
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.TRACK_REPLAY);
			tOptLog.setFunCType(LogConstants.TRACK_REPLAY_QUERY);
			tOptLog.setOptDesc("�켣��ѯʧ��");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			return mapping.findForward(null);
		}
		// String entCode = userInfo.getEmpCode();
		// json
		StringBuffer jsonSb = new StringBuffer();
		jsonSb.append("[");
		TTerminal terminal = tTargetObjectService
				.getTTargetObjectById(deviceId);// ����ն���Ϣadd 10.11.23
		List<TLocrecord> locs = tLoccateService.queryLocsByTime(terminal,
				startDate, endDate, startTime, endTime);

		if (locs != null) {
			try {
				CoordCvtAPI coordCvtApi = new CoordCvtAPI();
				CoordAPI coordApi = new CoordAPI();
				// add by liuhongxiao 2012-04-26
				String filterSpeed = Config.getInstance().getString(
						"config.properties", "filterSpeed");
				int maxSpeed = CharTools.str2Integer(filterSpeed, 200);
				DriftFilter df = new DriftFilter(30 * 60, maxSpeed, 1d);
				List<PointBean> pointList = df.deal(locs);

				PointBean prev = null;
				long filterStarL = Long.parseLong(filterStar);
				for (int i = 0; i < pointList.size(); i++) {
					try {
						PointBean current = (PointBean) pointList.get(i);
						if (current.getLocateType().equals("0")
								&& current.getStatlliteNum() < filterStarL) {
							continue;
						}
						double distance = 0;
						if (prev == null) {
							prev = current;
						} else {
							// gpsʹ���������������ֻ��ն�ʹ�ü����������
							if (current.getLocateType().equals("0")) {// �ֻ�
								distance = GeoUtils.DistanceOfTwoPoints(prev
										.getLongitude(), prev.getLatitude(),
										current.getLongitude(), current
												.getLatitude(),
										GaussSphere.WGS84) / 1000;
							} else {// gps
								if (current.getDistance() == null
										|| current.getDistance() == 0
										|| prev.getDistance() == null
										|| prev.getDistance() == 0) {
									distance = GeoUtils.DistanceOfTwoPoints(
											prev.getLongitude(), prev
													.getLatitude(), current
													.getLongitude(), current
													.getLatitude(),
											GaussSphere.WGS84) / 1000;
								} else {
									distance = current.getDistance()
											- prev.getDistance();
								}
							}
							prev = current;
						}
						String prevAccStatus = null;
						List<TLocrecord> locList = current.getLocrecordList();
						for (int j = 0; j < locList.size(); j++) {
							TLocrecord loc = locList.get(j);
							String accStatus = loc.getAccStatus();
							float speed = loc.getSpeed() == null ? 0 : loc
									.getSpeed();
							// ��accStatus Ϊ��ʱ �ж��ٶ��Ƿ����0 ������0 ʱ acc״̬Ϊ�� ����Ϊ�ر�
							if (accStatus == null) {
								if (speed > 0) {
									accStatus = "1";
								} else {
									accStatus = "0";
								}
							}
							if (j == 0) {
								prevAccStatus = accStatus;
							} else if (accStatus.equals(prevAccStatus)) {
								continue;
							} else {
								prevAccStatus = accStatus;
								distance = 0d;
							}
							// λ������
							String posDesc = "";
							String jmx = loc.getJmx();
							boolean flag = false;
							if (jmx != null && jmx.length() > 0) {
								String tmpjmx = jmx.substring(0, 3);
								if (tmpjmx.equalsIgnoreCase("IFI")) {
									flag = true;
								}
							}
							if (loc.getLocDesc() == null
									|| loc.getLocDesc().equals("") || flag || loc.getJmx() == null
											|| loc.getJmx().equals("")) {

									double[] xs = { loc.getLongitude() };
									double[] ys = { loc.getLatitude() };

									com.sos.sosgps.api.DPoint[] dPoint = null;
									String lngX = "";
									String latY = "";
									try {
										dPoint = coordApi
												.encryptConvert(xs, ys);
										lngX = dPoint[0].getEncryptX();
										latY = dPoint[0].getEncryptY();
									} catch (Exception ex) {
										System.out
												.println("track-encryptConvert error,"
														+ ex.getMessage());
									}

									loc.setJmx(lngX);
									loc.setJmy(latY);
							} else {
								posDesc = loc.getLocDesc();
							}
							// end add
							jsonSb.append("{");
							jsonSb.append("id:'" + loc.getId() + "',");// id
							jsonSb.append("deviceId:'" + loc.getDeviceId()
									+ "',");// id
							// jsonSb.append("type:'0',");//type:0LBS1GPS
							jsonSb.append("x:'" + loc.getLongitude() + "',");// x
							jsonSb.append("y:'" + loc.getLatitude() + "',");// y

							jsonSb
									.append("poiName:'" + loc.getPoiName()
											+ "',");// poiName

							java.text.DecimalFormat myformat = new java.text.DecimalFormat(
									"0.00");
							// System.out.println(myformat.format(distance));
							jsonSb.append("distance:'"
									+ myformat.format(distance) + "',");// ����һ������룬��λkm

							// add magiejue 2010-11-23
							jsonSb.append("termName:'" + terminal.getTermName()
									+ "',");// �ն���
							jsonSb.append("vNumber:'"
									+ terminal.getVehicleNumber() + "',");// ���ƺ�
							jsonSb.append("simCard:'" + terminal.getSimcard()
									+ "',");// sim����
							jsonSb.append("locType:'"
									+ terminal.getLocateType() + "',");// ��Ϣ��¼���ͣ����ػ��ֻ���
							jsonSb.append("direction:'" + loc.getDirection()
									+ "',");// ����

							jsonSb.append("jmx:'"
									+ CharTools.killNullString(loc.getJmx())
									+ "',");// jmx
							jsonSb.append("jmy:'"
									+ CharTools.killNullString(loc.getJmy())
									+ "',");// jmy
							jsonSb.append("deflectionx:'"
									+ coordCvtApi.decrypt(loc.getJmx()) + "',");// ƫתx����
							jsonSb.append("deflectiony:'"
									+ coordCvtApi.decrypt(loc.getJmy()) + "',");// ƫתy����
							jsonSb.append("speed:'"
									+ CharTools
											.killNullFloat2String(speed, "0")
									+ "',");// speed
							jsonSb.append("pd:'"
									+ CharTools.javaScriptEscape(CharTools
											.replayStr(posDesc)) + "',");// position
							jsonSb.append("time:'"
									+ CharTools.killNullString(DateUtility
											.dateTimeToStr(loc.getGpstime()))
									+ "',");// time
							// add ��Դ 2010-07-31 �����г�״̬ acc
							jsonSb.append("accStatus:'"
									+ CharTools.javaScriptEscape(accStatus)
									+ "',");// accStatus
							jsonSb.append("temperature:'"
									+ CharTools.killNullFloat2String(loc
											.getTemperature(), "") + "'");// temperature
							// end
							jsonSb.append("},");
						}
					} catch (Exception e) {
					}
				}
				if (jsonSb.length() > 1) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jsonSb.append("]");
		// System.out.println(jsonSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(jsonSb.toString());

		// ��־��¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.TRACK_REPLAY);
		tOptLog.setFunCType(LogConstants.TRACK_REPLAY_QUERY);
		tOptLog.setOptDesc("�켣��ѯ�ɹ�");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		return mapping.findForward(null);
	}

	// sos�����ֻ���λ
	public ActionForward locTelecom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ��request�л�ò���
		String deviceIds = request.getParameter("deviceIds");// �����ֻ��ն�deviceid�������","����

		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		List locs = null;
		// json
		StringBuffer jsonSb = new StringBuffer();
		jsonSb.append("[");
		String[] tokens = null;
		Object o = request.getSession().getAttribute("telecomToken");
		if (o != null) {
			tokens = (String[]) o;
		}
		if (tokens == null) {
			// ���token
			tokens = tLoccateService.telecomGetUnauthorizedToken();
			request.getSession().setAttribute("telecomToken", tokens);
		}
		if (tokens == null || tokens.length != 5) {
		} else {
			String oauth_verifier = tokens[2];
			String oauth_token_access = tokens[3];
			String oauth_token_secret_access = tokens[4];
			locs = tLoccateService.locTelecom(oauth_token_access,
					oauth_verifier, oauth_token_secret_access, deviceIds);
			CoordCvtAPI coordCvtApi = new CoordCvtAPI();
			com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
			if (locs != null) {
				for (Iterator iterator = locs.iterator(); iterator.hasNext();) {
					Position tloc = (Position) iterator.next();
					String lngX = null;// ����x����
					String latY = null;// ����y����
					String posDesc = null;// λ������
					String errorDesc = tloc.getErrorDesc();// ��������
					if (tloc.getCoordX() > 0 && tloc.getCoordY() > 0) {
						double[] xs = { tloc.getCoordX() };
						double[] ys = { tloc.getCoordY() };
						// DPoint[] dPoint = coordCvtApi.encryptConvert(xs, ys);
						// lngX = dPoint[0].getEncryptX();
						// latY = dPoint[0].getEncryptY();
						// double x = dPoint[0].x;
						// double y = dPoint[0].y;
						// if (x > 0 && y > 0) {
						// posDesc = coordCvtApi.getAddress(lngX, latY);
						// }

						com.sos.sosgps.api.DPoint[] dPoint = null;
						lngX = "";
						latY = "";
						double x = 0;
						double y = 0;
						try {
							dPoint = coordApi.encryptConvert(xs, ys);
							lngX = dPoint[0].getEncryptX();
							latY = dPoint[0].getEncryptY();
							x = dPoint[0].x;
							y = dPoint[0].y;
						} catch (Exception ex) {
							System.out
									.println("queryLocByTime-encryptConvert error,"
											+ ex.getMessage());
						}

						// ȡ��λ������

						if (x > 0 && y > 0) {
							try {
								posDesc = coordCvtApi.getAddress(lngX, latY);
							} catch (Exception ex) {
								System.out
										.println("queryLocByTime-getAddress error,"
												+ ex.getMessage());
							}
						}

					}
					jsonSb.append("{");
					jsonSb.append("deviceId:'" + tloc.getSimcard() + "',");// simcard
					jsonSb.append("type:'1',");// type:0LBS1GPS
					jsonSb.append("x:'" + tloc.getCoordX() + "',");// x
					jsonSb.append("y:'" + tloc.getCoordY() + "',");// y
					jsonSb.append("jmx:'" + CharTools.killNullString(lngX)
							+ "',");// jmx
					jsonSb.append("jmy:'" + CharTools.killNullString(latY)
							+ "',");// jmy
					jsonSb.append("errorDesc:'"
							+ CharTools.javaScriptEscape(errorDesc) + "',");// ��������
					jsonSb.append("pd:'" + CharTools.javaScriptEscape(posDesc)
							+ "'");// position desc
					jsonSb.append("},");
				}
				if (jsonSb.length() > 1) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
			} else {
				// ���»��token
				tokens = tLoccateService.telecomGetUnauthorizedToken();
				request.getSession().setAttribute("telecomToken", tokens);
			}
		}
		jsonSb.append("]");
		// System.out.println(jsonSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(jsonSb.toString());

		return mapping.findForward(null);
	}

	/**
	 * add by zhaofeng 2011215
	 */
	public ActionForward locDesc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// UserInfo userInfo = (UserInfo)
		// request.getSession(true).getAttribute("userInfo");
		// �Է������첽�����ȡ��γ��
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		// ���ݾ�γ�Ȼ�ȡλ������
		String positionDesc = tLoccateService.positionDesc(x, y);
		// System.out.println("positionDesc"+positionDesc);
		response.setContentType("text/xml;charset=UTF-8");
		response.getWriter().write(positionDesc.toString());
		return null;
	}

	// �켣�طų�����Ϣ��ҳ
	public ActionForward listTrack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer jsonSb = new StringBuffer();
		// ��request�л�ò���
		String deviceId = request.getParameter("deviceId");// �ն�deviceid
		String startTime = request.getParameter("startTime");// ��ʼʱ�䣬��ʽyyyy-mm-dd
		// HH:MM
		String endTime = request.getParameter("endTime");// ����ʱ�䣬��ʽyyyy-mm-dd
		// HH:MM
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		int total = 0;
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			jsonSb.append("[]");
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write(
					"{total:" + total + ",data:" + jsonSb.toString() + "}");
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		// Page locs = null;

		// json
		if (deviceId == null) {
			return mapping.findForward(null);
		}
		TTerminal tt = tTargetObjectService.getTTargetObjectById(deviceId);// ����ն���Ϣadd
		// 10.11.23
		Page<TLocrecord> locs = tLoccateService.listQueryLocsByTime(deviceId,
				startTime + ":00", endTime + ":00", page, limitint);
		CoordCvtAPI coordCvtApi = new CoordCvtAPI();
		com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();

		if (locs != null) {
			// �жϵ�һ�����Ƿ���Ư��
			double lastX = 0;
			double lastY = 0;
			Date lastGtime = null;
			double lastLc = 0;
			double lastlngX = -1;
			double lastLatY = -1;
			String lastPosDesc = "";

			for (int xx = 0; xx < locs.getResult().size(); xx++) {
				TLocrecord tloc = (TLocrecord) locs.getResult().get(xx);
				// ������̲�
				double x = tloc.getLongitude();
				double y = tloc.getLatitude();
				Date gtime = tloc.getGpstime();
				double distance = 0;
				boolean tmp = false;
				// �ο��ٽ�4��������Աȣ������ٽ�4�������ƫ�� ����Ϊ�õ�ΪƯ�Ƶ� ɾ���õ�
				for (int yy = xx + 1; yy < xx + 4; yy++) {
					if (yy >= locs.getResult().size() - 1) {
						break;
					}
					TLocrecord tlocr = (TLocrecord) locs.getResult().get(yy);
					if (tlocr.getLongitude() > 0 && tlocr.getLatitude() > 0) {
						String filterSpeed = Config.getInstance().getString(
								"config.properties", "filterSpeed");
						int speedmax = CharTools.str2Integer(filterSpeed, 200);// Ĭ��200
						// ȥ��Ư��
						distance = GeoUtils.DistanceOfTwoPoints(tlocr
								.getLongitude(), tlocr.getLatitude(), x, y,
								GaussSphere.WGS84);
						distance /= 1000;// ��λ�����km
						if (distance == 0) {
							continue;
						}
						int betweenTime = DateUtility.betweenSecond(tlocr
								.getGpstime(), gtime);// ʱ����λ��
						if (betweenTime == 0) {
							continue;
						}
						double realSpeed = (distance / betweenTime) * 60 * 60;
						// double realSpeed = distance / 1000 * 60 * 60
						// / betweenTime;// ����/Сʱ
						if (realSpeed > speedmax) {

							// tmp = true;
							// break;
							locs.getResult().remove(xx);
						}
						// if(tmp){
						// locs.remove(xx);
						// }
					}
				}
			}
			jsonSb.append("[");
			total = locs.getTotalCount();
			for (Iterator iterator = locs.getResult().iterator(); iterator
					.hasNext();) {
				try {
					TLocrecord tloc = (TLocrecord) iterator.next();
					// ������̲�
					double x = tloc.getLongitude();
					double y = tloc.getLatitude();
					Date gtime = tloc.getGpstime();
					// // ��������һ�������
					double distance = 0;
					if (lastX > 0 && lastY > 0 && x > 0 && y > 0) {
						// gpsʹ���������������ֻ��ն�ʹ�ü����������
						if (tloc.getLocateType() != null
								&& tloc.getLocateType().equals("0")) {// �ֻ�
							distance = GeoUtils.DistanceOfTwoPoints(lastX,
									lastY, x, y, GaussSphere.WGS84);
							distance /= 1000;// ��λ�����km
						} else {// gps
							if (tloc.getDistance() == null) {
								distance = GeoUtils.DistanceOfTwoPoints(lastX,
										lastY, x, y, GaussSphere.WGS84);
								distance /= 1000;// ��λ�����km
							} else if (tloc.getDistance() == 0) {
								distance = GeoUtils.DistanceOfTwoPoints(lastX,
										lastY, x, y, GaussSphere.WGS84);
								distance /= 1000;// ��λ�����km
							} else if (lastLc == 0) {
								distance = GeoUtils.DistanceOfTwoPoints(lastX,
										lastY, x, y, GaussSphere.WGS84);
								distance /= 1000;// ��λ�����km
							} else {
								distance = tloc.getDistance() - lastLc;
							}
						}
					}

					lastX = x;
					lastY = y;
					lastGtime = gtime;
					lastLc = tloc.getDistance() == null ? 0 : tloc
							.getDistance();

					// λ������
					String posDesc = "";
					if (tloc.getLongitude() > 0 && tloc.getLatitude() > 0) {
						if (tloc.getLocDesc() == null
								|| tloc.getLocDesc().equals("")) {
							if (tloc.getJmx() == null
									|| tloc.getJmx().equals("")) {
								double[] xs = { tloc.getLongitude() };
								double[] ys = { tloc.getLatitude() };
								com.sos.sosgps.api.DPoint[] dPoint = null;
								String lngX = "";
								String latY = "";
								try {
									dPoint = coordApi.encryptConvert(xs, ys);
									lngX = dPoint[0].getEncryptX();
									latY = dPoint[0].getEncryptY();
								} catch (Exception ex) {
									System.out
											.println("track-encryptConvert error,"
													+ ex.getMessage());
								}

								tloc.setJmx(lngX);
								tloc.setJmy(latY);
							}
							lastlngX = tloc.getLongitude();
							lastLatY = tloc.getLatitude();
						} else {
							posDesc = tloc.getLocDesc();
						}
					}
					lastPosDesc = posDesc;
					// add ��Դ 2010-07-31 �����г�״̬ acc ��Ϊ��ʱ Ĭ�� Ϊ�г�״̬
					float speed = tloc.getSpeed() == null ? 0 : tloc.getSpeed();
					String accStatus = tloc.getAccStatus();
					// ��accStatus Ϊ��ʱ �ж��ٶ��Ƿ����0 ������0 ʱ acc״̬Ϊ�� ����Ϊ�ر�
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
					// jsonSb.append("type:'0',");//type:0LBS1GPS
					jsonSb.append("x:'" + tloc.getLongitude() + "',");// x
					jsonSb.append("y:'" + tloc.getLatitude() + "',");// y

					jsonSb.append("poiName:'" + tloc.getPoiName() + "',");// poiName

					java.text.DecimalFormat myformat = new java.text.DecimalFormat(
							"0.00");
					// System.out.println(myformat.format(distance));
					jsonSb.append("distance:'" + myformat.format(distance)
							+ "',");// ����һ������룬��λkm

					// add magiejue 2010-11-23
					jsonSb.append("termName:'" + tt.getTermName() + "',");// �ն���
					jsonSb.append("vNumber:'" + tt.getVehicleNumber() + "',");// ���ƺ�
					jsonSb.append("simCard:'" + tt.getSimcard() + "',");// sim����
					jsonSb.append("locType:'" + tt.getLocateType() + "',");// ��Ϣ��¼���ͣ����ػ��ֻ���
					jsonSb.append("direction:'" + tloc.getDirection() + "',");// ����

					jsonSb.append("jmx:'"
							+ CharTools.javaScriptEscape(tloc.getJmx()) + "',");// jmx
					jsonSb.append("jmy:'"
							+ CharTools.javaScriptEscape(tloc.getJmy()) + "',");// jmy
					jsonSb.append("deflectionx:'"
							+ coordCvtApi.decrypt(tloc.getJmx()) + "',");// ƫתx����
					jsonSb.append("deflectiony:'"
							+ coordCvtApi.decrypt(tloc.getJmy()) + "',");// ƫתy����
					jsonSb.append("speed:'"
							+ CharTools.killNullFloat2String(tloc.getSpeed(),
									"0") + "',");// speed
					jsonSb.append("pd:'"
							+ CharTools.javaScriptEscape(CharTools
									.replayStr(lastPosDesc)) + "',");// position
					// desc
					// �滻 '
					// Ϊ ��
					jsonSb.append("time:'"
							+ CharTools.javaScriptEscape(DateUtility
									.dateTimeToStr(tloc.getGpstime())) + "',");// time
					// add ��Դ 2010-07-31 �����г�״̬ acc
					jsonSb.append("accStatus:'"
							+ CharTools.javaScriptEscape(accStatus) + "',");// accStatus
					jsonSb.append("temperature:'"
							+ CharTools.killNullFloat2String(tloc
									.getTemperature(), "") + "'");// temperature
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
		response.setContentType("text/json; charset=utf-8");
		// response.getWriter().write(jsonSb.toString());
		// System.out.println("-----------"+jsonSb.toString());
		response.getWriter().write(
				"{total:" + total + ",data:" + jsonSb.toString() + "}");
		// ��־��¼
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.TRACK_REPLAY);
		tOptLog.setFunCType(LogConstants.TRACK_REPLAY_QUERY);
		tOptLog.setOptDesc("�켣��ѯ�ɹ�");
		tOptLog.setResult(new Long(1));
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward(null);
	}

	public static void main(String args[]) {
		//ƫת
		String lngX = null;// ����x����118.60911,28.715832
		String latY = null;// ����y����
		com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
		CoordCvtAPI coordCvtApi = new CoordCvtAPI();
		com.sos.sosgps.api.DPoint[] dPoint = null;
		double[] xs = { 122.00935 };
		double[] ys = { 40.7106 };
		lngX = "";
		latY = "";
		double x = 0;
		double y = 0;
		try {
			dPoint = coordApi.encryptConvert(xs, ys);
			lngX = dPoint[0].getEncryptX();
			latY = dPoint[0].getEncryptY();
			x = dPoint[0].x;
			y = dPoint[0].y;
			System.out.println(x+","+y+";"+lngX+","+latY);
		} catch (Exception ex) {
			System.out.println("queryLocByTime-encryptConvert error,"
					+ ex.getMessage());
		}
		// ȡ��λ������
		if (x > 0 && y > 0) {
			try {
				String posDesc = coordCvtApi.getAddress(lngX, latY);
				System.out.println(posDesc);
			} catch (Exception ex) {
				System.out.println("queryLocByTime-getAddress error,"
						+ ex.getMessage());
			}
		}
		//��ƫת
		/*com.sos.sosgps.api.CoordAPI coordApi = new com.sos.sosgps.api.CoordAPI();
		com.sos.sosgps.api.DPoint dPoint = null;
		double x = 0;
		double y = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("116.424766,40.004634;");
		String[] xyArr = sb.toString().split(";");
		for(int i = 0;i < xyArr.length;i++){
			String[] xys = xyArr[i].split(",");
			if(xys.length == 2){
				try {
					x = Double.parseDouble(xys[0]);
					y = Double.parseDouble(xys[1]);
					dPoint = coordApi.decryptConvert(x, y);
					x = dPoint.x;
					y = dPoint.y;
					System.out.println(x+","+y);
				} catch (Exception ex) {
					System.out.println("queryLocByTime-encryptConvert error,"
							+ ex.getMessage());
				}
			}
		}*/
		

	}

    // sos�켣�ط�
    public ActionForward track2(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // ��request�л�ò���
        String deviceId = request.getParameter("deviceId");// �ն�deviceid
        String startDate = request.getParameter("startDate");// ��ʼ���ڣ���ʽyyyy-mm-dd
        String endDate = request.getParameter("endDate");// �������ڣ���ʽyyyy-mm-dd
        String startTime = request.getParameter("startTime");// ��ʼʱ�䣬��ʽHH:MM
        String endTime = request.getParameter("endTime");// ����ʱ�䣬��ʽHH:MM
        String filterStar = request.getParameter("filterStar");// �������� add by
        // liuhongxiao
        // 2011-12-02
        UserInfo userInfo = this.getCurrentUser(request);
        if (deviceId == null || startDate == null || endDate == null
                || startTime == null || endTime == null || filterStar == null
                || userInfo == null) {
            response.setContentType("text/json; charset=utf-8");
            response.getWriter().write("");
            // ��־��¼
            TOptLog tOptLog = new TOptLog();
            tOptLog.setEmpCode(userInfo.getEmpCode());
            tOptLog.setUserName(userInfo.getUserAccount());
            tOptLog.setAccessIp(userInfo.getIp());
            tOptLog.setUserId(userInfo.getUserId());
            tOptLog.setFunFType(LogConstants.TRACK_REPLAY);
            tOptLog.setFunCType(LogConstants.TRACK_REPLAY_QUERY);
            tOptLog.setOptDesc("�켣��ѯʧ��");
            tOptLog.setResult(new Long(1));
            LogFactory.newLogInstance("optLogger").info(tOptLog);
            return mapping.findForward(null);
        }
        // String entCode = userInfo.getEmpCode();
        // json
        StringBuffer jsonSb = new StringBuffer();
        jsonSb.append("[");
        TTerminal terminal = tTargetObjectService
                .getTTargetObjectById(deviceId);// ����ն���Ϣadd 10.11.23
        List<Object[]> locs = tLoccateService.queryLocsByTime2(terminal,
                startDate, endDate, startTime, endTime);
        List<TLocrecord> locs1 = new ArrayList<TLocrecord>();
        if (locs != null) {
            try {
                for (Iterator iterator = locs.iterator(); iterator.hasNext();) {
                    Object[] tloc = (Object[]) iterator.next();
                    TLocrecord tLocrecord = new TLocrecord();
                    float latitude = (BigDecimal)tloc[0] == null ? 0 : ((BigDecimal)tloc[0]).floatValue();
                    float longitude = (BigDecimal)tloc[1] == null ? 0 : ((BigDecimal)tloc[1]).floatValue();
                    String gpstime = (String)tloc[2];
                    float distance = (BigDecimal)tloc[3] == null ? 0 : ((BigDecimal)tloc[3]).floatValue();
                    long statlliteNum = (BigDecimal)tloc[4] == null ? 0 : ((BigDecimal)tloc[4]).longValue();
                    String locateType = (String)tloc[5] == null ? "0" : (String)tloc[5];
                    tLocrecord.setLatitude(latitude);
                    tLocrecord.setLongitude(longitude);
                    tLocrecord.setGpstime(DateUtility.strToDateTime(gpstime));
                    tLocrecord.setDistance(distance);
                    tLocrecord.setStatlliteNum(statlliteNum);
                    tLocrecord.setLocateType(locateType);
                    locs1.add(tLocrecord);
                }
                CoordCvtAPI coordCvtApi = new CoordCvtAPI();
                CoordAPI coordApi = new CoordAPI();
                // add by liuhongxiao 2012-04-26
                String filterSpeed = Config.getInstance().getString(
                        "config.properties", "filterSpeed");
                int maxSpeed = CharTools.str2Integer(filterSpeed, 200);
                DriftFilter df = new DriftFilter(30 * 60, maxSpeed, 1d);
                List<PointBean> pointList = df.deal(locs1);

                PointBean prev = null;
                long filterStarL = Long.parseLong(filterStar);
                for (int i = 0; i < pointList.size(); i++) {
                    try {
                        PointBean current = (PointBean) pointList.get(i);
                        if (current.getLocateType().equals("0")
                                && current.getStatlliteNum() < filterStarL) {
                            continue;
                        }
                        double distance = 0;
                        if (prev == null) {
                            prev = current;
                        } else {
                            // gpsʹ���������������ֻ��ն�ʹ�ü����������
                            if (current.getLocateType().equals("0")) {// �ֻ�
                                distance = GeoUtils.DistanceOfTwoPoints(prev
                                        .getLongitude(), prev.getLatitude(),
                                        current.getLongitude(), current
                                                .getLatitude(),
                                        GaussSphere.WGS84) / 1000;
                            } else {// gps
                                if (current.getDistance() == null
                                        || current.getDistance() == 0
                                        || prev.getDistance() == null
                                        || prev.getDistance() == 0) {
                                    distance = GeoUtils.DistanceOfTwoPoints(
                                            prev.getLongitude(), prev
                                                    .getLatitude(), current
                                                    .getLongitude(), current
                                                    .getLatitude(),
                                            GaussSphere.WGS84) / 1000;
                                } else {
                                    distance = current.getDistance()
                                            - prev.getDistance();
                                }
                            }
                            prev = current;
                        }
                        String prevAccStatus = null;
                        List<TLocrecord> locList = current.getLocrecordList();
                        for (int j = 0; j < locList.size(); j++) {
                            TLocrecord loc = locList.get(j);
                            String accStatus = loc.getAccStatus();
                            float speed = loc.getSpeed() == null ? 0 : loc
                                    .getSpeed();
                            // ��accStatus Ϊ��ʱ �ж��ٶ��Ƿ����0 ������0 ʱ acc״̬Ϊ�� ����Ϊ�ر�
                            if (accStatus == null) {
                                if (speed > 0) {
                                    accStatus = "1";
                                } else {
                                    accStatus = "0";
                                }
                            }
                            if (j == 0) {
                                prevAccStatus = accStatus;
                            } else if (accStatus.equals(prevAccStatus)) {
                                continue;
                            } else {
                                prevAccStatus = accStatus;
                                distance = 0d;
                            }
                            // λ������
                            String posDesc = "";
                            String jmx = loc.getJmx();
                            boolean flag = false;
                            if (jmx != null && jmx.length() > 0) {
                                String tmpjmx = jmx.substring(0, 3);
                                if (tmpjmx.equalsIgnoreCase("IFI")) {
                                    flag = true;
                                }
                            }
                            if (loc.getLocDesc() == null
                                    || loc.getLocDesc().equals("") || flag || loc.getJmx() == null
                                            || loc.getJmx().equals("")) {

                                    double[] xs = { loc.getLongitude() };
                                    double[] ys = { loc.getLatitude() };

                                    com.sos.sosgps.api.DPoint[] dPoint = null;
                                    String lngX = "";
                                    String latY = "";
                                    try {
                                        dPoint = coordApi
                                                .encryptConvert(xs, ys);
                                        lngX = dPoint[0].getEncryptX();
                                        latY = dPoint[0].getEncryptY();
                                    } catch (Exception ex) {
                                        System.out
                                                .println("track-encryptConvert error,"
                                                        + ex.getMessage());
                                    }

                                    loc.setJmx(lngX);
                                    loc.setJmy(latY);
                            } else {
                                posDesc = loc.getLocDesc();
                            }
                            // end add
                            jsonSb.append("{");
                            jsonSb.append("id:'" + loc.getId() + "',");// id
                            jsonSb.append("deviceId:'" + loc.getDeviceId()
                                    + "',");// id
                            // jsonSb.append("type:'0',");//type:0LBS1GPS
                            jsonSb.append("x:'" + loc.getLongitude() + "',");// x
                            jsonSb.append("y:'" + loc.getLatitude() + "',");// y

                            jsonSb
                                    .append("poiName:'" + loc.getPoiName()
                                            + "',");// poiName

                            java.text.DecimalFormat myformat = new java.text.DecimalFormat(
                                    "0.00");
                            // System.out.println(myformat.format(distance));
                            jsonSb.append("distance:'"
                                    + myformat.format(distance) + "',");// ����һ������룬��λkm

                            // add magiejue 2010-11-23
                            jsonSb.append("termName:'" + terminal.getTermName()
                                    + "',");// �ն���
                            jsonSb.append("vNumber:'"
                                    + terminal.getVehicleNumber() + "',");// ���ƺ�
                            jsonSb.append("simCard:'" + terminal.getSimcard()
                                    + "',");// sim����
                            jsonSb.append("locType:'"
                                    + terminal.getLocateType() + "',");// ��Ϣ��¼���ͣ����ػ��ֻ���
                            jsonSb.append("direction:'" + loc.getDirection()
                                    + "',");// ����

                            jsonSb.append("jmx:'"
                                    + CharTools.killNullString(loc.getJmx())
                                    + "',");// jmx
                            jsonSb.append("jmy:'"
                                    + CharTools.killNullString(loc.getJmy())
                                    + "',");// jmy
                            jsonSb.append("deflectionx:'"
                                    + coordCvtApi.decrypt(loc.getJmx()) + "',");// ƫתx����
                            jsonSb.append("deflectiony:'"
                                    + coordCvtApi.decrypt(loc.getJmy()) + "',");// ƫתy����
                            jsonSb.append("speed:'"
                                    + CharTools
                                            .killNullFloat2String(speed, "0")
                                    + "',");// speed
                            jsonSb.append("pd:'"
                                    + CharTools.javaScriptEscape(CharTools
                                            .replayStr(posDesc)) + "',");// position
                            jsonSb.append("time:'"
                                    + CharTools.killNullString(DateUtility
                                            .dateTimeToStr(loc.getGpstime()))
                                    + "',");// time
                            // add ��Դ 2010-07-31 �����г�״̬ acc
                            jsonSb.append("accStatus:'"
                                    + CharTools.javaScriptEscape(accStatus)
                                    + "',");// accStatus
                            jsonSb.append("temperature:'"
                                    + CharTools.killNullFloat2String(loc
                                            .getTemperature(), "") + "'");// temperature
                            // end
                            jsonSb.append("},");
                        }
                    } catch (Exception e) {
                    }
                }
                if (jsonSb.length() > 1) {
                    jsonSb.deleteCharAt(jsonSb.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        jsonSb.append("]");
        // System.out.println(jsonSb.toString());
        response.setContentType("text/json; charset=utf-8");
        response.getWriter().write(jsonSb.toString());

        // ��־��¼
        TOptLog tOptLog = new TOptLog();
        tOptLog.setEmpCode(userInfo.getEmpCode());
        tOptLog.setUserName(userInfo.getUserAccount());
        tOptLog.setAccessIp(userInfo.getIp());
        tOptLog.setUserId(userInfo.getUserId());
        tOptLog.setFunFType(LogConstants.TRACK_REPLAY);
        tOptLog.setFunCType(LogConstants.TRACK_REPLAY_QUERY);
        tOptLog.setOptDesc("�켣��ѯ�ɹ�");
        tOptLog.setResult(new Long(1));
        LogFactory.newLogInstance("optLogger").info(tOptLog);

        return mapping.findForward(null);
    }
    

    /**
     * �����λ��Ϣ����
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward lastLocrecordList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String deviceIds = request.getParameter("deviceIds");
        deviceIds = CharTools.splitAndAdd(deviceIds);
        String name = request.getParameter("name");
        name = CharTools.killNullString(name);
        name = java.net.URLDecoder.decode(name, "utf-8");
        name = CharTools.killNullString(name);
        String locateType = request.getParameter("locateType");
        int gpstime = request.getParameter("gpstime") == null ? 0 : Integer.parseInt(request.getParameter("gpstime"));
        int inputtime = request.getParameter("inputtime") == null ? 0 : Integer.parseInt(request.getParameter("inputtime"));
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        UserInfo userInfo = this.getCurrentUser(request);
        String entCode = userInfo.getEmpCode();
        
        String expExcel = request.getParameter("expExcel");// trueΪ����
        expExcel = CharTools.killNullString(expExcel);

        if (expExcel.equalsIgnoreCase("true")) {
            tLoccateService.lastLocrecordListExcel(response, entCode, deviceIds, name, locateType, gpstime, inputtime);
            return null;
        }
        if (deviceIds == null || deviceIds.equals("")) {
            response.setContentType("text/json; charset=utf-8");
            response.getWriter().write("{total:0,data:[]}");
            return null;
        }
        int startint = Integer.parseInt(start);
        int limitint = Integer.parseInt(limit);

        String json = tLoccateService.lastLocrecordList(entCode, deviceIds, name, locateType, gpstime, inputtime, startint, limitint);
        response.setContentType("text/json; charset=utf-8");
        response.getWriter().write(json);
//
//        // ��־��¼
//        TOptLog tOptLog = new TOptLog();
//        tOptLog.setEmpCode(userInfo.getEmpCode());
//        tOptLog.setUserName(userInfo.getUserAccount());
//        tOptLog.setAccessIp(userInfo.getIp());
//        tOptLog.setUserId(userInfo.getUserId());
//        tOptLog.setFunFType(LogConstants.TRACK_REPLAY);
//        tOptLog.setFunCType(LogConstants.TRACK_REPLAY_REALTIEMLOC);
//        tOptLog.setOptDesc("��ʱ��λ���ݲ�ѯ�ɹ�");
//        tOptLog.setResult(new Long(1));
//        LogFactory.newLogInstance("optLogger").info(tOptLog);
        return mapping.findForward(null);
    }

}
