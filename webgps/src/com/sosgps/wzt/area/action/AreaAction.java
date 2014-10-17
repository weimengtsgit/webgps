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

	// sos用户列表
	public ActionForward listArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo user = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (user == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = user.getEmpCode();
		// 从request中获取参数
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
					jsonSb.append("areaName:'" + area.getName() + "',");// 区域名称
					jsonSb.append("areaPoints:'" + area.getXy() + "',");// 区域坐标
					jsonSb.append("areaType:'" + area.getAreaType() + "'");// 区域类型1多边形2矩形3圆形
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

	// sos新增区域
	public ActionForward addArea(ActionMapping mapping, ActionForm form,
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
		String name = request.getParameter("name");// 区域名称
		String areaPoints = request.getParameter("areaPoints");// 区域坐标
		String areaType = request.getParameter("areaType");// 区域类型1多边形2矩形3圆形

		name = CharTools.killNullString(name);
		name = java.net.URLDecoder.decode(name, "utf-8");
		name = CharTools.killNullString(name);
		
		areaPoints = CharTools.killNullString(areaPoints);
		areaType = CharTools.killNullString(areaType);

		areaService.addArea(entCode, name, areaPoints, areaType);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_ADD);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("新增区域成功!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos修改区域
	public ActionForward updateArea(ActionMapping mapping, ActionForm form,
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
		String id = request.getParameter("id");// 区域id
		String name = request.getParameter("name");// 区域名称
		String areaPoints = request.getParameter("areaPoints");// 区域坐标
		String areaType = request.getParameter("areaType");// 区域类型1多边形2矩形3圆形

		name = CharTools.killNullString(name);
		name = java.net.URLDecoder.decode(name, "utf-8");
		name = CharTools.killNullString(name);
		areaPoints = CharTools.killNullString(areaPoints);
		areaType = CharTools.killNullString(areaType);

		Long areaId = Long.parseLong(id);
		areaService.updateArea(areaId, name, areaPoints, areaType);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("修改区域成功!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos删除区域
	public ActionForward deleteAreas(ActionMapping mapping, ActionForm form,
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
		String ids = request.getParameter("ids");// 区域id，多个","隔开
		String[] areaIds = CharTools.split(ids, ",");
		areaService.deleteAreas(areaIds);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("删除区域成功!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}
	//
	public ActionForward deleteRefTermAreasByDeviceIdAndAreaIds(ActionMapping mapping,
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
		String deviceId = request.getParameter("deviceId");// 终端id，多个用","隔开
		String areaIds = request.getParameter("areaIds"); //区域id

		String[] areaIdss = CharTools.split(areaIds, ",");
		areaService.deleteRefTermAreasByDeviceIdsAndAreaIds(deviceId, areaIdss);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_TERM_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("删除终端设置区域成功!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}
	
	// sos删除终端设置区域
	public ActionForward deleteRefTermAreas(ActionMapping mapping,
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
		String deviceIds = request.getParameter("deviceIds");// 终端id，多个用","隔开

		String[] deviceIdss = CharTools.split(deviceIds, ",");
		areaService.deleteRefTermAreas(deviceIdss);
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_TERM_DELETE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("删除终端设置区域成功!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos终端设置区域
	public ActionForward refTermArea(ActionMapping mapping, ActionForm form,
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
		String areaIds = request.getParameter("areaIds");// 区域id，多个用","隔开
		String deviceIds = request.getParameter("deviceIds");// 终端id，多个用","隔开
		String startTime = request.getParameter("startTime");// 开始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String alarmType = request.getParameter("alarmType");// 报警类型0出区域1进区域

		areaIds = CharTools.killNullString(areaIds);
		deviceIds = CharTools.killNullString(deviceIds);
		startTime = CharTools.killNullString(startTime);
		endTime = CharTools.killNullString(endTime);
		alarmType = CharTools.killNullString(alarmType);

		String[] deviceIdss = CharTools.split(deviceIds, ",");
		String[] areaIdss = CharTools.split(areaIds, ",");
		areaService.saveRefTermArea(deviceIdss, areaIdss, alarmType, startTime,
				endTime);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_AREA_TERM_UPDATE);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("终端设置区域成功!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos终端设置区域列表
	public ActionForward listRefTermArea(ActionMapping mapping,
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

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		// 出区域进区域全部
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
					jsonSb.append("areaName:'" + area.getName() + "',");// 区域名称
					jsonSb.append("areaPoints:'" + area.getXy() + "',");// 区域坐标
					jsonSb.append("areaType:'" + area.getAreaType() + "',");// 区域类型1多边形2矩形3圆形
					jsonSb.append("vehicleNumber:'"
							+ terminal.getVehicleNumber() + "',");// 车牌号码
					jsonSb.append("locateType:'" + terminal.getLocateType()
							+ "',");// 终端类型
					jsonSb.append("deviceId:'" + terminal.getDeviceId() + "',");// 终端id
					jsonSb.append("alarmType:'" + ref.getAlarmType() + "',");// 报警类型0出区域1进区域
					jsonSb.append("startTime:'" + ref.getStartTime() + "',");// 开始时间
					jsonSb.append("endTime:'" + ref.getEndTime() + "'");// 结束时间
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
	
	// sos根据终端id查关联报警区域
	public ActionForward queryAreaByDeviceId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");//终端id
		
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
				jsonSb.append("areaName:'" + area.getName() + "',");// 区域名称
				jsonSb.append("areaPoints:'" + area.getXy() + "',");// 区域坐标
				Set<RefTermAreaalarm> refs = area.getRefTermAreaalarms();
				RefTermAreaalarm ref = refs.iterator().next();
				jsonSb.append("alarmType:'" + ref.getAlarmType() + "',");// 报警类型0出区域1进区域
				jsonSb.append("areaType:'" + area.getAreaType() + "'");// 区域类型1多边形2矩形3圆形
				jsonSb.append("},");
			}
			// 去掉最后的","
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
