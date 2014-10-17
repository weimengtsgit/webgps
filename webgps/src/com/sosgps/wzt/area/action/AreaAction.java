/**
 * 
 */
package com.sosgps.wzt.area.action;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.area.service.AreaService;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefTermAreaalarm;
import com.sosgps.wzt.orm.TArea;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;

/**
 * @author shiguang.zhou
 * 
 */
public class AreaAction extends DispatchWebActionSupport {
	private AreaService areaService = (AreaService) SpringHelper
			.getBean("AreaServiceImpl");

	// sos�û��б�
	public ActionForward listArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo user = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (user == null) {
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = user.getEmpCode();
		// ��request�л�ȡ����
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "utf-8");
		searchValue = CharTools.killNullString(searchValue);

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<TArea> list = areaService.listArea(entCode, page, limitint,
					searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					TArea area = (TArea) iterator.next();
					jsonSb.append("{");
					jsonSb.append("id:'" + area.getId() + "',");// id
					jsonSb.append("areaName:'" + area.getName() + "',");// ��������
					jsonSb.append("areaPoints:'" + area.getXy() + "',");// ��������
					jsonSb.append("areaType:'" + area.getAreaType() + "'");// ��������1�����2����3Բ��
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

	// sos��������
	public ActionForward addArea(ActionMapping mapping, ActionForm form,
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
		String name = request.getParameter("name");// ��������
		String areaPoints = request.getParameter("areaPoints");// ��������
		String areaType = request.getParameter("areaType");// ��������1�����2����3Բ��

		name = CharTools.killNullString(name);
		name = java.net.URLDecoder.decode(name, "utf-8");
		name = CharTools.killNullString(name);
		
		areaPoints = CharTools.killNullString(areaPoints);
		areaType = CharTools.killNullString(areaType);

		areaService.addArea(entCode, name, areaPoints, areaType);

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("��������ɹ�!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sos�޸�����
	public ActionForward updateArea(ActionMapping mapping, ActionForm form,
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
		String id = request.getParameter("id");// ����id
		String name = request.getParameter("name");// ��������
		String areaPoints = request.getParameter("areaPoints");// ��������
		String areaType = request.getParameter("areaType");// ��������1�����2����3Բ��

		name = CharTools.killNullString(name);
		name = java.net.URLDecoder.decode(name, "utf-8");
		name = CharTools.killNullString(name);
		areaPoints = CharTools.killNullString(areaPoints);
		areaType = CharTools.killNullString(areaType);

		Long areaId = Long.parseLong(id);
		areaService.updateArea(areaId, name, areaPoints, areaType);

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("�޸�����ɹ�!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sosɾ������
	public ActionForward deleteAreas(ActionMapping mapping, ActionForm form,
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
		String ids = request.getParameter("ids");// ����id�����","����
		String[] areaIds = CharTools.split(ids, ",");
		areaService.deleteAreas(areaIds);

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("ɾ������ɹ�!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}
	//
	public ActionForward deleteRefTermAreasByDeviceIdAndAreaIds(ActionMapping mapping,
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
		String deviceId = request.getParameter("deviceId");// �ն�id�������","����
		String areaIds = request.getParameter("areaIds"); //����id

		String[] areaIdss = CharTools.split(areaIds, ",");
		areaService.deleteRefTermAreasByDeviceIdsAndAreaIds(deviceId, areaIdss);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_TERM_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("ɾ���ն���������ɹ�!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}
	
	// sosɾ���ն���������
	public ActionForward deleteRefTermAreas(ActionMapping mapping,
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
		String deviceIds = request.getParameter("deviceIds");// �ն�id�������","����

		String[] deviceIdss = CharTools.split(deviceIds, ",");
		areaService.deleteRefTermAreas(deviceIdss);
		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_TERM_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("ɾ���ն���������ɹ�!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sos�ն���������
	public ActionForward refTermArea(ActionMapping mapping, ActionForm form,
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
		String areaIds = request.getParameter("areaIds");// ����id�������","����
		String deviceIds = request.getParameter("deviceIds");// �ն�id�������","����
		String startTime = request.getParameter("startTime");// ��ʼʱ��
		String endTime = request.getParameter("endTime");// ����ʱ��
		String alarmType = request.getParameter("alarmType");// ��������0������1������

		areaIds = CharTools.killNullString(areaIds);
		deviceIds = CharTools.killNullString(deviceIds);
		startTime = CharTools.killNullString(startTime);
		endTime = CharTools.killNullString(endTime);
		alarmType = CharTools.killNullString(alarmType);

		String[] deviceIdss = CharTools.split(deviceIds, ",");
		String[] areaIdss = CharTools.split(areaIds, ",");
		areaService.saveRefTermArea(deviceIdss, areaIdss, alarmType, startTime,
				endTime);

		// ��־ʵ��
		TOptLog tOptLog = new TOptLog();
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_TERM_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("�ն���������ɹ�!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// �ɹ�
		return mapping.findForward(null);
	}

	// sos�ն����������б�
	public ActionForward listRefTermArea(ActionMapping mapping,
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

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		// �����������ȫ��
		String alarmType = request.getParameter("alarmType");
		alarmType = CharTools.killNullString(alarmType);

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<Object[]> list = areaService.listRefTermArea(entCode,
					alarmType, page, limitint, searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					Object[] object = (Object[]) iterator.next();
					TArea area = (TArea) object[0];
					TTerminal terminal = (TTerminal) object[1];
					RefTermAreaalarm ref = (RefTermAreaalarm) object[2];
					jsonSb.append("{");
					jsonSb.append("id:'" + area.getId() + "',");// id
					jsonSb.append("areaName:'" + area.getName() + "',");// ��������
					jsonSb.append("areaPoints:'" + area.getXy() + "',");// ��������
					jsonSb.append("areaType:'" + area.getAreaType() + "',");// ��������1�����2����3Բ��
					jsonSb.append("vehicleNumber:'"
							+ terminal.getVehicleNumber() + "',");// ���ƺ���
					jsonSb.append("locateType:'" + terminal.getLocateType()
							+ "',");// �ն�����
					jsonSb.append("deviceId:'" + terminal.getDeviceId() + "',");// �ն�id
					jsonSb.append("alarmType:'" + ref.getAlarmType() + "',");// ��������0������1������
					jsonSb.append("startTime:'" + ref.getStartTime() + "',");// ��ʼʱ��
					jsonSb.append("endTime:'" + ref.getEndTime() + "'");// ����ʱ��
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
	
	// sos�����ն�id�������������
	public ActionForward queryAreaByDeviceId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"9\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// ��request�л�ȡ����
		String deviceId = request.getParameter("deviceId");//�ն�id
		
		deviceId = CharTools.killNullString(deviceId);
		
		Page<TArea> list = areaService.queryAreaByDeviceId(deviceId);
		
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (list != null && list.getResult() != null
				&& list.getResult().size() > 0) {
			total = list.getTotalCount();
			for(TArea area : list.getResult()){
				jsonSb.append("{");
				jsonSb.append("id:'" + area.getId() + "',");// id
				jsonSb.append("areaName:'" + area.getName() + "',");// ��������
				jsonSb.append("areaPoints:'" + area.getXy() + "',");// ��������
				Set<RefTermAreaalarm> refs = area.getRefTermAreaalarms();
				RefTermAreaalarm ref = refs.iterator().next();
				jsonSb.append("alarmType:'" + ref.getAlarmType() + "',");// ��������0������1������
				jsonSb.append("areaType:'" + area.getAreaType() + "'");// ��������1�����2����3Բ��
				jsonSb.append("},");
			}
			// ȥ������","
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
		}
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		//System.out.println("{total:" + total + ",data:[" + jsonSb.toString() + "]}");
		return mapping.findForward(null);
	}
}
