/**
 * 
 */
package com.sosgps.wzt.terminal.action;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import com.sosgps.wzt.excel.ExcelWorkBook;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.terminalgroup.service.TerminalGroupManageService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ModuleParamConfig;
import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.TEntTermtype;
import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TVehicleMsg;
import com.sosgps.wzt.orm.TermParamConfig;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.terminal.bean.TTerminalBean;
import com.sosgps.wzt.terminal.form.TerminalForm;
import com.sosgps.wzt.terminal.service.TerminalService;
import com.sosgps.wzt.terminal.service.impl.TerminalServiceImpl;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

/**
 * @author shiguang.zhou
 */
public class TerminalAction extends DispatchWebActionSupport {

	public ActionForward terminal_manager(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("terminal_manager");

	}

	/**
	 * 组/部门树结构
	 */
	public ActionForward terminalGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		// add zhangwei 增加 企业code
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();

		List tTargetGroupList = tTargetObjectService.getTTargetGroup(empCode);
		// List tTargetGroupList = tTargetObjectService.getTTargetGroup();
		request.setAttribute("tTargetGroupList", tTargetGroupList);
		return mapping.findForward("tTargetGroup");

	}

	/**
	 * 根据选择的组/部门，显示终端列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward changeGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long groupId = Long.valueOf(request.getParameter("groupId"));
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		List groupTTargetObjectList = tTargetObjectService
				.getGroupTTargetObject(groupId);
		List unGroupTTargetObjectList = tTargetObjectService
				.getUnGroupTTargetObject(groupId);
		request.setAttribute("groupTTargetObjectList", groupTTargetObjectList);
		request.setAttribute("unGroupTTargetObjectList",
				unGroupTTargetObjectList);
		request.setAttribute("groupId", groupId);
		return mapping.findForward("tTargetObject");

	}

	/**
	 * 跳转到终端列表页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tTargetObjectList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("tTargetObjectList");

	}

	/**
	 * 跳转到终端增加页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tTargetObjectAdd(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");

		// add zhangwei 增加 企业code
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		List tTargetGroupList = tTargetObjectService.getTTargetGroup(empCode);
		// List tTargetGroupList = tTargetObjectService.getTTargetGroup();
		request.setAttribute("tTargetGroupList", tTargetGroupList);

		List ttypelist = tTargetObjectService.getAllTermType();
		request.setAttribute("typelist", ttypelist);

		// List car_type_list = this.carTypeService.findAll();
		// request.setAttribute("cartypelist", car_type_list);

		return mapping.findForward("tTargetObjectAdd");

	}

	/**
	 * 终端增加入库
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tTargetObjectAddDb(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TerminalForm tTargetObjectForm = (TerminalForm) form;
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");

		boolean result = tTargetObjectService.save(tTargetObjectForm);

		// add zhangwei 增加 企业code
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		List tTargetGroupList = tTargetObjectService.getTTargetGroup(empCode);
		// List tTargetGroupList = tTargetObjectService.getTTargetGroup();

		request.setAttribute("tTargetGroupList", tTargetGroupList);
		List ttypelist = tTargetObjectService.getAllTermType();
		request.setAttribute("typelist", ttypelist);

		// 日志实例
		TOptLog tOptLog = new TOptLog();

		// 非正常登录
		if (userInfo == null) {
			request.setAttribute("msg", "非正常登录！");

			return mapping.findForward("message");
		}
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_TERMINAL);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("");

		if (result) {
			tOptLog.setResult(1L);
			tOptLog.setOptDesc("添加终端成功! ");
			request.setAttribute("msg", "添加终端成功!");
		} else {

			tOptLog.setResult(0L);
			tOptLog.setOptDesc("添加终端失败! ");
			request.setAttribute("msg", "添加终端失败!");
		}
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward("tTargetObjectList");

	}

	/**
	 * 删除TTargetObject
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delTTargetObject(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TerminalForm tTargetObjectForm = (TerminalForm) form;
		String[] sids = request.getParameterValues("ids");
		// 先取消采样
		// SimpleManageService simpleManageService = (SimpleManageService)
		// SpringHelper
		// .getBean("SimpleManageServiceImpl");
		// if (simpleManageService != null) {
		// ResponseBean re = simpleManageService.cancelSimcardSimple(sids);
		// if (re != null) {
		// if (re.getErrorCode() != 0) {// 取消失败
		// }
		// }
		// }
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		UserInfo userInfo = this.getCurrentUser(request);
		// 非正常登录
		if (userInfo == null) {
			request.setAttribute("msg", "非正常登录！");

			return mapping.findForward("message");
		}
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_TERMINAL);
		tOptLog.setResult(1L);

		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		tTargetObjectService.delTTargetObjectByIds(sids);
		request.setAttribute("msg", "删除终端成功!");

		tOptLog.setOptDesc("删除终端成功!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		return mapping.findForward("tTargetObjectList");

	}

	/**
	 * 编辑
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward viewTTargetObject(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String gpssn = request.getParameter("id");
		if (null == gpssn)
			gpssn = (String) request.getAttribute("gpssn");

		UserInfo userInfo = this.getCurrentUser(request);

		// 非正常登录
		if (userInfo == null) {
			request.setAttribute("msg", "非正常登录！");

			return mapping.findForward(null);
		}

		// Long id = Long.valueOf((String) gpssn);
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		TTerminal tTargetObject = tTargetObjectService
				.getTTargetObjectById(gpssn);

		// add zhangwei 增加 企业code
		String empCode = userInfo.getEmpCode();
		List tTargetGroupList = tTargetObjectService.getTTargetGroup(empCode);
		// List tTargetGroupList = tTargetObjectService.getTTargetGroup();
		// List tTargetGroupList = tTargetObjectService.getTTargetGroup();
		request.setAttribute("tTargetGroupList", tTargetGroupList);

		List ttypelist = tTargetObjectService.getAllTermType();
		request.setAttribute("typelist", ttypelist);

		// List car_type_list = this.carTypeService.findAll();
		// request.setAttribute("cartypelist", car_type_list);

		String code = null;

		TEntTermtype termtype = tTargetObject.getTEntTermtype();
		if (termtype != null)
			code = termtype.getTypeCode();

		TEntTermtype type = null;
		if (code != null)
			tTargetObjectService.getTermTypeByCode(code);

		RefTermGroup rgroup = tTargetObjectService.getTermGroup(tTargetObject);
		TTermGroup tgroup = null;
		if (rgroup != null)
			tgroup = rgroup.getTTermGroup();

		request.setAttribute("termgroup", tgroup);
		request.setAttribute("termtype", type);
		request.setAttribute("typeCode", code);
		request.setAttribute("tTargetObject", tTargetObject);
		return mapping.findForward("tTargetObjectView");

	}

	/**
	 * 修改终端信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tTargetObjectMod(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserInfo userInfo = this.getCurrentUser(request);

		// 非正常登录
		if (userInfo == null) {
			request.setAttribute("msg", "非正常登录！");

			return mapping.findForward(null);
		}

		TerminalForm tTargetObjectForm = (TerminalForm) form;
		tTargetObjectForm.setEntCode(userInfo.getEmpCode());

		String old_parent_id = request.getParameter("oldGroupId");

		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");

		boolean flag = tTargetObjectService.update(tTargetObjectForm,
				old_parent_id);
		request.setAttribute("gpssn", tTargetObjectForm.getDeviceId());

		String empCode = userInfo.getEmpCode();
		List tTargetGroupList = tTargetObjectService.getTTargetGroup(empCode);

		// List tTargetGroupList = tTargetObjectService.getTTargetGroup();
		request.setAttribute("tTargetGroupList", tTargetGroupList);

		List ttypelist = tTargetObjectService.getAllTermType();
		request.setAttribute("typelist", ttypelist);

		// List car_type_list = this.carTypeService.findAll();
		// request.setAttribute("cartypelist", car_type_list);
		// 日志实例
		TOptLog tOptLog = new TOptLog();

		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_TERMINAL);
		tOptLog.setResult(1L);

		if (flag) {
			request.setAttribute("msg", "修改终端信息成功!");
			tOptLog.setOptDesc("修改终端信息成功!");
		} else {
			request.setAttribute("msg", "修改终端失败");
			tOptLog.setOptDesc("修改终端信息失败!");
		}
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		return mapping.findForward("viewTTargetObject");

	}

	// 检测是否存在序列号
	public ActionForward checkTerminalId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String param = request.getParameter("deviceId");
		if (param == null)
			return null;
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");

		boolean flag = tTargetObjectService.findTermById(param);
		if (flag)
			response.getWriter().write("GPSSN_EXIST");

		return null;

	}

	// 检查是否已经存在SIMCARD
	public ActionForward checkTerminalSim(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String param = request.getParameter("simcard");
		if (param == null)
			return null;
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");

		boolean flag = tTargetObjectService.findTermBySim(param);
		if (flag)
			response.getWriter().write("SIM_EXIST");

		return null;

	}

	// 模糊查询终端名
	public ActionForward getTerminals(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String termName = request.getParameter("termName");
		if (termName == null) {
			return null;
		}
		String termName1 = new String(termName);
		// termName=new String(termName1.getBytes("ISO-8859-1"),"UTF-8");
		// termName=StringUtil.unescape(termName);
		termName = java.net.URLDecoder.decode(termName, "UTF-8");
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		response.setContentType("text/xml;charset=GBK");

		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo == null) {
			return null;
		}
		String entCode = userInfo.getEmpCode();

		boolean isVague = true;
		List list = tTargetObjectService.findTerminalByTermName("%" + termName
				+ "%", entCode, isVague);
		String termjson = "{termNames:[";
		for (int i = 0; i < list.size(); i++) {
			TTerminal tTerminal = (TTerminal) list.get(i);
			String tmName = tTerminal.getTermName();
			if (i == 0) {
				termjson += "{tname:'" + tmName + "'}";
			} else {
				termjson += ",{tname:'" + tmName + "'}";
			}
		}
		termjson += "]}";
		// System.out.println(termjson);
		response.getWriter().write(termjson);
		return null;
	}

	// 得到终端信息
	public ActionForward getTerminalById(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String param = request.getParameter("deviceId");
		if (param == null)
			return null;
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		response.setContentType("text/xml;charset=GBK");

		TTerminal term = tTargetObjectService.findTerminal(param);

		if (term != null) {
			String icon = term.getImgUrl();
			String tmName = term.getTermName();
			String termjson = "{\"tname\":\"" + tmName + "\",\"icon\":\""
					+ icon + "\"}";
			response.getWriter().write(termjson);
		}
		return null;
	}

	public ActionForward getTermInfoByDeviceIds(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String params = request.getParameter("deviceIds");
		if (params == null)
			return null;
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		response.setContentType("text/xml;charset=GBK");
		StringBuffer termjson = new StringBuffer();
		String[] deviceIds = CharTools.split(params, ",");
		termjson.append("[");
		for (int i = 0; i < deviceIds.length; i++) {
			String tmpDeviceId = deviceIds[i];
			TTerminal term = tTargetObjectService.findTerminal(tmpDeviceId);
			if (term != null) {
				String icon = term.getImgUrl();
				String tmName = term.getTermName();
				String tmId = term.getDeviceId();
				if (i == (deviceIds.length - 1)) {
					termjson.append("{\"deviceId\":\"" + tmId
							+ "\",\"tname\":\"" + tmName + "\",\"icon\":\""
							+ icon + "\"}");
				} else {
					termjson.append("{\"deviceId\":\"" + tmId
							+ "\",\"tname\":\"" + tmName + "\",\"icon\":\""
							+ icon + "\"},");
				}
			}
		}
		termjson.append("]");
		response.getWriter().write(termjson.toString());
		return null;
	}

	// sos企业终端列表

	public ActionForward listTerminal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = URLDecoder.decode(searchValue, "UTF-8");

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;

		UserInfo userInfo = getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		Long userId = userInfo.getUserId();
		Page list = tTargetObjectService.listTerminal(entCode, userId, page,
				limitint, searchValue);

		StringBuffer jsonSb = new StringBuffer();
		String total = "";
		if ((list != null) && (list.getResult() != null)) {
			total = "{total:" + list.getTotalCount() + ",data:[";

			Iterator i = list.getResult().iterator();
			while (i.hasNext()) {
				Object[] userObj = (Object[]) i.next();
				TTerminal terminal = (TTerminal) userObj[0];
				TTermGroup group = (TTermGroup) userObj[1];
				TEntTermtype tp = (TEntTermtype) userObj[2];
				TVehicleMsg tv = (TVehicleMsg) userObj[3];
				long id = 0L;
				String model = "";
				String vehicleNum = "";
				Date saleDate = null;
				String saleMethods = "";
				String dealer = "";
				String installers = "";
				long contractValue = 0L;
				long loanAmount = 0L;
				Date repaymentPeriod = null;
				String claimAct = "";
				Date crtDate = null;
				if (tv != null) {
					id = tv.getId().longValue();
					model = tv.getModel();
					vehicleNum = tv.getVehicleNum();
					saleDate = tv.getSaleDate();
					saleMethods = tv.getSaleMethods();
					dealer = tv.getDealer();
					installers = tv.getInstallers();
					contractValue = tv.getContractValue().longValue();
					loanAmount = tv.getLoanAmount().longValue();
					repaymentPeriod = tv.getRepaymentPeriod();
					claimAct = tv.getClaimAct();
					crtDate = tv.getCrtDate();
				}

				jsonSb.append("{");
				jsonSb.append("id:'" + terminal.getDeviceId() + "',");
				jsonSb.append("simcard:'"
						+ CharTools.javaScriptEscape(terminal.getSimcard())
						+ "',");
				jsonSb.append("deviceId:'"
						+ CharTools.javaScriptEscape(terminal.getDeviceId())
						+ "',");
				jsonSb.append("termName:'"
						+ CharTools.javaScriptEscape(terminal.getTermName())
						+ "',");
				jsonSb.append("groupName:'"
						+ CharTools.javaScriptEscape((group != null) ? group
								.getGroupName() : "") + "',");
				jsonSb.append("groupId:'"
						+ CharTools.killNullString(new StringBuilder().append(
								(group != null) ? group.getId() : "")
								.toString()) + "',");
				jsonSb.append("province:'"
						+ CharTools.javaScriptEscape(terminal.getProvince())
						+ "',");
				jsonSb.append("city:'"
						+ CharTools.javaScriptEscape(terminal.getCity()) + "',");
				jsonSb.append("startTime:'"
						+ CharTools.killNullString(terminal.getStartTime())
						+ "',");
				jsonSb.append("endTime:'"
						+ CharTools.killNullString(terminal.getEndTime())
						+ "',");
				jsonSb.append("typeCode:'"
						+ CharTools.killNullString(terminal.getTEntTermtype()
								.getTypeCode()) + "',");
				jsonSb.append("typeName:'"
						+ CharTools.javaScriptEscape(tp.getTypeName()) + "',");
				jsonSb.append("getherInterval:'"
						+ CharTools.killNullLong2String(Long.valueOf(terminal
								.getGetherInterval().longValue() / 60L), "0")
						+ "',");
				jsonSb.append("termDesc:'"
						+ CharTools.javaScriptEscape(terminal.getTermdesc())
						+ "',");
				jsonSb.append("locateType:'"
						+ CharTools.javaScriptEscape(terminal.getLocateType())
						+ "',");
				jsonSb.append("vehicleNumber:'"
						+ CharTools.javaScriptEscape(terminal
								.getVehicleNumber()) + "',");
				jsonSb.append("vehicleType:'"
						+ CharTools.javaScriptEscape(terminal.getVehicleType())
						+ "',");
				jsonSb.append("driverNumber:'"
						+ CharTools.javaScriptEscape(terminal.getDriverNumber())
						+ "',");
				jsonSb.append("oilSpeedLimit:'"
						+ CharTools.killNullLong2String(
								terminal.getOilSpeedLimit(), "0") + "',");
				jsonSb.append("holdAlarmFlag:'"
						+ CharTools.killNullString(terminal.getHoldAlarmFlag())
						+ "',");
				jsonSb.append("speedAlarmLimit:'"
						+ CharTools.killNullLong2String(
								terminal.getSpeedAlarmLimit(), "0") + "',");
				jsonSb.append("speedAlarmLast:'"
						+ CharTools.killNullLong2String(
								terminal.getSpeedAlarmLast(), "0") + "',");
				String imgUrl = terminal.getImgUrl();
				if ((imgUrl == null)
						&& (CharTools.killNullString(terminal.getLocateType())
								.equals("0")))
					imgUrl = "persion";
				else if ((imgUrl == null)
						&& (CharTools.killNullString(terminal.getLocateType())
								.equals("1"))) {
					imgUrl = "car";
				}
				jsonSb.append("imgUrl:'" + imgUrl + "',");
				jsonSb.append("week:'"
						+ CharTools.killNullLong2String(terminal.getWeek(),
								"127") + "',");

				jsonSb.append("vehicleMsgId:'" + id + "',");
				jsonSb.append("model:'" + CharTools.javaScriptEscape(model)
						+ "',");
				jsonSb.append("vehicleNum:'"
						+ CharTools.javaScriptEscape(vehicleNum) + "',");
				jsonSb.append("saleDate:'"
						+ CharTools.killNullString(DateUtility
								.dateToStr(saleDate)) + "',");
				jsonSb.append("saleMethods:'"
						+ CharTools.javaScriptEscape(saleMethods) + "',");
				jsonSb.append("dealer:'" + CharTools.javaScriptEscape(dealer)
						+ "',");
				jsonSb.append("installers:'"
						+ CharTools.javaScriptEscape(installers) + "',");
				jsonSb.append("contractValue:'"
						+ CharTools.killNullLong2String(
								Long.valueOf(contractValue), "0") + "',");
				jsonSb.append("loanAmount:'"
						+ CharTools.killNullLong2String(
								Long.valueOf(loanAmount), "0") + "',");
				jsonSb.append("repaymentPeriod:'"
						+ CharTools.killNullString(DateUtility
								.dateToStr(repaymentPeriod)) + "',");
				jsonSb.append("claimAct:'"
						+ CharTools.javaScriptEscape(claimAct) + "',");
				jsonSb.append("crtDate:'"
						+ CharTools.killNullString(DateUtility
								.dateToStr(crtDate)) + "'");
				jsonSb.append("},");
			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
			jsonSb.append("]}");
		}

		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(total + jsonSb.toString());
		return mapping.findForward(null);
	}

	// sos新增终端
	public ActionForward addTerminal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 企业code
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		// 从request中获取参数
		String deviceId = request.getParameter("deviceId");
		String typeCode = request.getParameter("typeCode");
		String termName = request.getParameter("termName");
		String simcard = request.getParameter("simcard");
		String groupId = request.getParameter("groupId");
		String locateType = request.getParameter("locateType");
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String getherInterval = request.getParameter("getherInterval");

		getherInterval = (Integer.parseInt(getherInterval) * 60) + "";

		String termDesc = request.getParameter("termDesc");

		String vehicleNumber = request.getParameter("vehicleNumber");
		String vehicleType = request.getParameter("vehicleType");
		String driverNumber = request.getParameter("driverNumber");
		String oilSpeedLimit = request.getParameter("oilSpeedLimit");
		String speedAlarmLimit = request.getParameter("speedAlarmLimit");
		String speedAlarmLast = request.getParameter("speedAlarmLast");
		String holdAlarmFlag = request.getParameter("holdAlarmFlag");
		String imgUrl = request.getParameter("imgUrl");
		String week = request.getParameter("week");

		String model = request.getParameter("model");
		String vehicleNum = request.getParameter("vehicleNum");
		String saleDate = request.getParameter("saleDate");
		String dealer = request.getParameter("dealer");
		String installers = request.getParameter("installers");
		String saleMethods = request.getParameter("saleMethods");
		String contractValue = request.getParameter("contractValue");
		String loanAmount = request.getParameter("loanAmount");
		String repaymentPeriod = request.getParameter("repaymentPeriod");
		String claimAct = request.getParameter("claimAct");

		model = CharTools.killNullString(model);
		vehicleNum = CharTools.killNullString(vehicleNum);
		saleDate = CharTools.killNullString(saleDate);
		dealer = CharTools.killNullString(dealer);
		installers = CharTools.killNullString(installers);
		saleMethods = CharTools.killNullString(saleMethods);
		contractValue = CharTools.killNullString(contractValue);
		loanAmount = CharTools.killNullString(loanAmount);
		repaymentPeriod = CharTools.killNullString(repaymentPeriod);
		claimAct = CharTools.killNullString(claimAct);

		deviceId = CharTools.killNullString(deviceId);
		typeCode = CharTools.killNullString(typeCode);
		termName = CharTools.killNullString(termName);
		simcard = CharTools.killNullString(simcard);
		groupId = CharTools.killNullString(groupId);
		locateType = CharTools.killNullString(locateType);
		province = CharTools.killNullString(province);
		city = CharTools.killNullString(city);
		startTime = CharTools.killNullString(startTime);
		endTime = CharTools.killNullString(endTime);
		getherInterval = CharTools.killNullString(getherInterval);
		termDesc = CharTools.killNullString(termDesc);
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleType = CharTools.killNullString(vehicleType);
		driverNumber = CharTools.killNullString(driverNumber);
		oilSpeedLimit = CharTools.killNullString(oilSpeedLimit);
		speedAlarmLimit = CharTools.killNullString(speedAlarmLimit);
		speedAlarmLast = CharTools.killNullString(speedAlarmLast);
		holdAlarmFlag = CharTools.killNullString(holdAlarmFlag);
		imgUrl = CharTools.killNullString(imgUrl);

		deviceId = URLDecoder.decode(deviceId, "utf-8");
		typeCode = URLDecoder.decode(typeCode, "utf-8");
		termName = URLDecoder.decode(termName, "utf-8");
		simcard = URLDecoder.decode(simcard, "utf-8");
		groupId = URLDecoder.decode(groupId, "utf-8");
		locateType = URLDecoder.decode(locateType, "utf-8");
		province = URLDecoder.decode(province, "utf-8");
		city = URLDecoder.decode(city, "utf-8");
		startTime = URLDecoder.decode(startTime, "utf-8");
		endTime = URLDecoder.decode(endTime, "utf-8");
		getherInterval = URLDecoder.decode(getherInterval, "utf-8");
		termDesc = URLDecoder.decode(termDesc, "utf-8");
		vehicleNumber = URLDecoder.decode(vehicleNumber, "utf-8");
		vehicleType = URLDecoder.decode(vehicleType, "utf-8");
		driverNumber = URLDecoder.decode(driverNumber, "utf-8");
		oilSpeedLimit = URLDecoder.decode(oilSpeedLimit, "utf-8");
		speedAlarmLimit = URLDecoder.decode(speedAlarmLimit, "utf-8");
		speedAlarmLast = URLDecoder.decode(speedAlarmLast, "utf-8");
		holdAlarmFlag = URLDecoder.decode(holdAlarmFlag, "utf-8");
		imgUrl = URLDecoder.decode(imgUrl, "utf-8");

		model = URLDecoder.decode(model, "utf-8");
		vehicleNum = URLDecoder.decode(vehicleNum, "utf-8");

		dealer = URLDecoder.decode(dealer, "utf-8");
		installers = URLDecoder.decode(installers, "utf-8");
		saleMethods = URLDecoder.decode(saleMethods, "utf-8");
		contractValue = URLDecoder.decode(contractValue, "utf-8");
		loanAmount = URLDecoder.decode(loanAmount, "utf-8");

		claimAct = URLDecoder.decode(claimAct, "utf-8");

		TerminalService terminalService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");

		// 查找是否已存在
		boolean b = terminalService.findTermById(deviceId);
		if (b) {
			response.getWriter().write("{result:\"3\"}");// 已存在此终端
			return mapping.findForward(null);
		}
		// 查询企业终端数是否达到上限
		Long maxUserNum = userInfo.getEnt().getMaxUserNum();
		if (maxUserNum != null && maxUserNum.longValue() > 0) {
			Long termNum = terminalService.findTermNum(empCode);
			if (termNum.longValue() >= maxUserNum) {
				response.getWriter().write("{result:\"4\"}");// 终端数达到上限
				return mapping.findForward(null);
			}
		}

		TTerminalBean terminalBean = new TTerminalBean();
		terminalBean.setEntCode(empCode);
		terminalBean.setDeviceId(deviceId);
		terminalBean.setTypeCode(typeCode);
		terminalBean.setTermName(termName);
		terminalBean.setTermDesc(termDesc);
		terminalBean.setGroupId(Long.valueOf(Long.parseLong(groupId)));
		terminalBean.setLocateType(locateType);
		terminalBean.setSimcard(simcard);
		terminalBean.setDriverNumber(driverNumber);
		terminalBean.setProvince(province);
		terminalBean.setCity(city);
		terminalBean.setStartTime(startTime);
		terminalBean.setEndTime(endTime);
		terminalBean.setGetherInterval(Long.valueOf(Long
				.parseLong(getherInterval)));
		terminalBean.setVehicleNumber(vehicleNumber);
		terminalBean.setVehicleType(vehicleType);
		terminalBean.setOilSpeedLimit(Long.valueOf(Long
				.parseLong(oilSpeedLimit)));
		terminalBean.setSpeedAlarmLimit(Long.valueOf(Long
				.parseLong(speedAlarmLimit)));
		terminalBean.setSpeedAlarmLast(Long.valueOf(Long
				.parseLong(speedAlarmLast)));
		terminalBean.setHoldAlarmFlag(holdAlarmFlag);
		terminalBean.setImgUrl(imgUrl);
		terminalBean.setWeek(Long.valueOf(Long.parseLong(week)));
		terminalBean.setIsAllocate("1");

		terminalService.saveTTerminal(terminalBean);

		if (locateType.equals("1")) {
			TVehicleMsg tVehicleMsg = new TVehicleMsg();
			tVehicleMsg.setModel(model);
			tVehicleMsg.setVehicleNum(vehicleNum);
			tVehicleMsg.setSaleDate(DateUtility.strToDate(saleDate));
			tVehicleMsg.setDealer(dealer);
			tVehicleMsg.setInstallers(installers);
			tVehicleMsg.setSaleMethods(saleMethods);
			tVehicleMsg.setContractValue(CharTools.str2Long(contractValue,
					Long.valueOf(0L)));
			tVehicleMsg.setLoanAmount(CharTools.str2Long(loanAmount,
					Long.valueOf(0L)));
			tVehicleMsg.setRepaymentPeriod(DateUtility
					.strToDate(repaymentPeriod));
			tVehicleMsg.setClaimAct(claimAct);
			tVehicleMsg.setDeviceId(deviceId);
			tVehicleMsg.setEntCode(empCode);
			tVehicleMsg.setCrtDate(new Date());
			terminalService.saveVehicleMsg(tVehicleMsg);
		}

		// add by wangzhen 2012-11-29
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<ModuleParamConfig> mcs = terminalService.getAllModule();
			StringBuffer contentDef0 = new StringBuffer();
			contentDef0
					.append("<modeConfig><mobileParamInterval>240</mobileParamInterval>");
			contentDef0.append("<termExpirationTime>"
					+ sm.format(terminalBean.getSetExpirationTime())
					+ "</termExpirationTime>");
			contentDef0
					.append("<logFlag>1</logFlag><logUploadTime>240</logUploadTime><netSmsFlag>1</netSmsFlag><uninstallSmsFlag>1</uninstallSmsFlag><signMode>0</signMode></modeConfig>");
			StringBuffer contentDef1 = new StringBuffer();
			contentDef1.append("<modeConfig><locMode>1</locMode>");
			contentDef1.append("<week>" + terminalBean.getWeek() + "</week>");
			contentDef1.append("<workTime>" + terminalBean.getStartTime()
					+ ":00-" + terminalBean.getEndTime() + ":00</workTime>");
			contentDef1.append("<locInterval>"
					+ terminalBean.getGetherInterval() + "</locInterval>");
			contentDef1
					.append("<gpsEnable>1</gpsEnable><searchTime>60</searchTime><networkEnable>0</networkEnable><cellEnable>0</cellEnable>"
							+ "<gpsDeviationFilter>200</gpsDeviationFilter><networkDeviationFilter>500</networkDeviationFilter><cellDeviationFilter>1000</cellDeviationFilter>"
							+ "<repeatDate>1</repeatDate><repeatCheckInterval>300</repeatCheckInterval></modeConfig>");

			StringBuffer contentDef2 = new StringBuffer();
			contentDef2.append("<modeConfig><locMode>0</locMode>");
			contentDef2.append("<week>127</week>");
			contentDef2.append("<workTime>00:00:00-23:59:59</workTime>");
			contentDef2.append("<locInterval>300</locInterval>");
			contentDef2
					.append("<gpsEnable>1</gpsEnable><searchTime>60</searchTime><networkEnable>0</networkEnable><cellEnable>0</cellEnable>"
							+ "<gpsDeviationFilter>200</gpsDeviationFilter><networkDeviationFilter>500</networkDeviationFilter><cellDeviationFilter>1000</cellDeviationFilter>"
							+ "<repeatDate>1</repeatDate><repeatCheckInterval>300</repeatCheckInterval></modeConfig>");
			Date currentDate = new Date();
			if (mcs == null || mcs.size() <= 0) {
				// 增加全局默认配置
				for (int i = 0; i <= 2; i++) {
					TermParamConfig termPar0 = new TermParamConfig();
					if (i == 0) {
						termPar0.setContent(contentDef0.toString());
					} else if (i == 1) {
						termPar0.setContent(contentDef1.toString());
					} else if (i == 2) {
						termPar0.setContent(contentDef2.toString());
					}
					termPar0.setCreateOn(currentDate);
					termPar0.setCreateBy(termName);
					termPar0.setDeviceId(terminalBean.getDeviceId());
					termPar0.setEntCode(terminalBean.getEntCode());
					termPar0.setType(i);
					termPar0.setLastUpdateOn(currentDate);
					termPar0.setLastUpdateBy(termName);
					terminalService.saveTermParamConfig(termPar0);
				}
			} else {
				ModuleParamConfig mc0 = null;
				ModuleParamConfig mc1 = null;
				ModuleParamConfig mc2 = null;
				for (ModuleParamConfig mc : mcs) {
					if (mc.getType() == 0) {
						mc0 = mc;
					}
					if (mc.getType() == 1) {
						mc1 = mc;
					}
					if (mc.getType() == 2) {
						mc2 = mc;
					}
				}
				TermParamConfig termPar0 = new TermParamConfig();
				termPar0.setCreateOn(currentDate);
				termPar0.setCreateBy(termName);
				termPar0.setDeviceId(terminalBean.getDeviceId());
				termPar0.setEntCode(terminalBean.getEntCode());
				termPar0.setType(0);
				termPar0.setLastUpdateOn(currentDate);
				termPar0.setLastUpdateBy(termName);

				if (mc0 == null) {
					termPar0.setContent(contentDef0.toString());
					terminalService.saveTermParamConfig(termPar0);
				} else {
					String content = mc0.getContent();
					content = dealContentFromType0(content, terminalBean);
					termPar0.setContent(content);
					terminalService.saveTermParamConfig(termPar0);
				}
				TermParamConfig termPar1 = new TermParamConfig();
				termPar1.setCreateOn(currentDate);
				termPar1.setCreateBy(termName);
				termPar1.setDeviceId(terminalBean.getDeviceId());
				termPar1.setEntCode(terminalBean.getEntCode());
				termPar1.setType(1);
				termPar1.setLastUpdateOn(currentDate);
				termPar1.setLastUpdateBy(termName);
				if (mc1 == null) {
					termPar1.setContent(contentDef1.toString());
					terminalService.saveTermParamConfig(termPar1);
				} else {
					String content = mc1.getContent();
					content = dealContent(content, terminalBean);
					termPar1.setContent(content);
					terminalService.saveTermParamConfig(termPar1);
				}
				TermParamConfig termPar2 = new TermParamConfig();
				termPar2.setCreateOn(currentDate);
				termPar2.setCreateBy(termName);
				termPar2.setDeviceId(terminalBean.getDeviceId());
				termPar2.setEntCode(terminalBean.getEntCode());
				termPar2.setType(1);
				termPar2.setLastUpdateOn(currentDate);
				termPar2.setLastUpdateBy(termName);
				if (mc2 == null) {
					termPar2.setContent(contentDef2.toString());
					terminalService.saveTermParamConfig(termPar2);
				} else {
					String content = mc2.getContent();
					content = dealContent(content, terminalBean);
					termPar2.setContent(content);
					terminalService.saveTermParamConfig(termPar2);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_TERMINAL);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("添加终端成功!(名称:" + termName + ";手机号:" + simcard
				+ ";车牌号:" + vehicleNumber + ";序列号:" + deviceId + ");");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	/**
	 * 处理模块配置类型为全局的终端到期时间
	 * 
	 * @param content
	 * @param terminal
	 * @return
	 */
	private String dealContentFromType0(String content, TTerminalBean tb) {
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		// 终端到期时间
		String termEndTimeStart = "<termExpirationTime>";
		String termEndTimeEnd = "</termExpirationTime>";
		String termEndTimeRepValue = content.substring(
				content.indexOf(termEndTimeStart),
				content.indexOf(termEndTimeEnd) + termEndTimeEnd.length());
		Date termEndTime = tb.getSetExpirationTime();
		String termEndTimeStr = "";
		if (termEndTime != null) {
			termEndTimeStr = sm.format(termEndTime);
		}
		String newtermEndTimeRepValue = "<termExpirationTime>" + termEndTimeStr
				+ "</termExpirationTime>";
		content = replace(content, termEndTimeRepValue, newtermEndTimeRepValue);
		return content;
	}

	// sos修改终端

	public ActionForward updateTerminal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = getCurrentUser(request);
		String empCode = userInfo.getEmpCode();

		String deviceId = request.getParameter("deviceId");
		String typeCode = request.getParameter("typeCode");
		String termName = request.getParameter("termName");
		String simcard = request.getParameter("simcard");
		String groupId = request.getParameter("groupId");
		String locateType = request.getParameter("locateType");
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String getherInterval = request.getParameter("getherInterval");

		getherInterval = (Integer.parseInt(getherInterval) * 60) + "";

		String termDesc = request.getParameter("termDesc");

		String vehicleNumber = request.getParameter("vehicleNumber");
		String vehicleType = request.getParameter("vehicleType");
		String driverNumber = request.getParameter("driverNumber");
		String oilSpeedLimit = request.getParameter("oilSpeedLimit");
		String speedAlarmLimit = request.getParameter("speedAlarmLimit");
		String speedAlarmLast = request.getParameter("speedAlarmLast");
		String holdAlarmFlag = request.getParameter("holdAlarmFlag");
		String imgUrl = request.getParameter("imgUrl");
		String week = request.getParameter("week");

		String model = request.getParameter("model");
		String vehicleNum = request.getParameter("vehicleNum");
		String saleDate = request.getParameter("saleDate");
		String dealer = request.getParameter("dealer");
		String installers = request.getParameter("installers");
		String saleMethods = request.getParameter("saleMethods");
		String contractValue = request.getParameter("contractValue");
		String loanAmount = request.getParameter("loanAmount");
		String repaymentPeriod = request.getParameter("repaymentPeriod");
		String claimAct = request.getParameter("claimAct");
		String vehicleMsgId = request.getParameter("vehicleMsgId");

		model = CharTools.killNullString(model);
		vehicleNum = CharTools.killNullString(vehicleNum);
		saleDate = CharTools.killNullString(saleDate);
		dealer = CharTools.killNullString(dealer);
		installers = CharTools.killNullString(installers);
		saleMethods = CharTools.killNullString(saleMethods);
		contractValue = CharTools.killNullString(contractValue);
		loanAmount = CharTools.killNullString(loanAmount);
		repaymentPeriod = CharTools.killNullString(repaymentPeriod);
		claimAct = CharTools.killNullString(claimAct);

		deviceId = CharTools.killNullString(deviceId);
		typeCode = CharTools.killNullString(typeCode);
		termName = CharTools.killNullString(termName);
		simcard = CharTools.killNullString(simcard);
		groupId = CharTools.killNullString(groupId);
		locateType = CharTools.killNullString(locateType);
		province = CharTools.killNullString(province);
		city = CharTools.killNullString(city);
		startTime = CharTools.killNullString(startTime);
		endTime = CharTools.killNullString(endTime);
		getherInterval = CharTools.killNullString(getherInterval);
		termDesc = CharTools.killNullString(termDesc);
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleType = CharTools.killNullString(vehicleType);
		driverNumber = CharTools.killNullString(driverNumber);
		oilSpeedLimit = CharTools.killNullString(oilSpeedLimit);
		speedAlarmLimit = CharTools.killNullString(speedAlarmLimit);
		speedAlarmLast = CharTools.killNullString(speedAlarmLast);
		holdAlarmFlag = CharTools.killNullString(holdAlarmFlag);
		imgUrl = CharTools.killNullString(imgUrl);

		deviceId = URLDecoder.decode(deviceId, "utf-8");
		typeCode = URLDecoder.decode(typeCode, "utf-8");
		termName = URLDecoder.decode(termName, "utf-8");
		simcard = URLDecoder.decode(simcard, "utf-8");
		groupId = URLDecoder.decode(groupId, "utf-8");
		locateType = URLDecoder.decode(locateType, "utf-8");
		province = URLDecoder.decode(province, "utf-8");
		city = URLDecoder.decode(city, "utf-8");
		startTime = URLDecoder.decode(startTime, "utf-8");
		endTime = URLDecoder.decode(endTime, "utf-8");
		getherInterval = URLDecoder.decode(getherInterval, "utf-8");
		termDesc = URLDecoder.decode(termDesc, "utf-8");
		vehicleNumber = URLDecoder.decode(vehicleNumber, "utf-8");
		vehicleType = URLDecoder.decode(vehicleType, "utf-8");
		driverNumber = URLDecoder.decode(driverNumber, "utf-8");
		oilSpeedLimit = URLDecoder.decode(oilSpeedLimit, "utf-8");
		speedAlarmLimit = URLDecoder.decode(speedAlarmLimit, "utf-8");
		speedAlarmLast = URLDecoder.decode(speedAlarmLast, "utf-8");
		holdAlarmFlag = URLDecoder.decode(holdAlarmFlag, "utf-8");
		imgUrl = URLDecoder.decode(imgUrl, "utf-8");

		model = URLDecoder.decode(model, "utf-8");
		vehicleNum = URLDecoder.decode(vehicleNum, "utf-8");

		dealer = URLDecoder.decode(dealer, "utf-8");
		installers = URLDecoder.decode(installers, "utf-8");
		saleMethods = URLDecoder.decode(saleMethods, "utf-8");
		contractValue = URLDecoder.decode(contractValue, "utf-8");
		loanAmount = URLDecoder.decode(loanAmount, "utf-8");

		claimAct = URLDecoder.decode(claimAct, "utf-8");

		TerminalService terminalService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");

		TTerminalBean terminalBean = new TTerminalBean();
		terminalBean.setEntCode(empCode);
		terminalBean.setDeviceId(deviceId);
		terminalBean.setTypeCode(typeCode);
		terminalBean.setTermName(termName);
		terminalBean.setTermDesc(termDesc);
		terminalBean.setGroupId(Long.valueOf(Long.parseLong(groupId)));
		terminalBean.setLocateType(locateType);
		terminalBean.setSimcard(simcard);
		terminalBean.setDriverNumber(driverNumber);
		terminalBean.setProvince(province);
		terminalBean.setCity(city);
		terminalBean.setStartTime(startTime);
		terminalBean.setEndTime(endTime);
		terminalBean.setGetherInterval(Long.valueOf(Long
				.parseLong(getherInterval)));
		terminalBean.setVehicleNumber(vehicleNumber);
		terminalBean.setVehicleType(vehicleType);
		terminalBean.setOilSpeedLimit(Long.valueOf(Long
				.parseLong(oilSpeedLimit)));
		terminalBean.setSpeedAlarmLimit(Long.valueOf(Long
				.parseLong(speedAlarmLimit)));
		terminalBean.setSpeedAlarmLast(Long.valueOf(Long
				.parseLong(speedAlarmLast)));
		terminalBean.setHoldAlarmFlag(holdAlarmFlag);
		terminalBean.setImgUrl(imgUrl);
		terminalBean.setWeek(Long.valueOf(Long.parseLong(week)));
		terminalBean.setIsAllocate("1");

		terminalService.updateTTerminal(terminalBean);
		// logger.info("locateType = " + locateType + ";vehicleMsgId = " +
		// vehicleMsgId);
		if ((locateType.equals("1")) && (vehicleMsgId != null)
				&& (!vehicleMsgId.equals("0"))) {
			TVehicleMsg tVehicleMsg = new TVehicleMsg();
			tVehicleMsg.setModel(model);
			tVehicleMsg.setVehicleNum(vehicleNum);
			tVehicleMsg.setSaleDate(DateUtility.strToDate(saleDate));
			tVehicleMsg.setDealer(dealer);
			tVehicleMsg.setInstallers(installers);
			tVehicleMsg.setSaleMethods(saleMethods);
			tVehicleMsg.setContractValue(CharTools.str2Long(contractValue,
					Long.valueOf(0L)));
			tVehicleMsg.setLoanAmount(CharTools.str2Long(loanAmount,
					Long.valueOf(0L)));
			tVehicleMsg.setRepaymentPeriod(DateUtility
					.strToDate(repaymentPeriod));
			tVehicleMsg.setClaimAct(claimAct);
			tVehicleMsg.setDeviceId(deviceId);
			tVehicleMsg.setEntCode(empCode);
			tVehicleMsg.setId(Long.valueOf(Long.parseLong(vehicleMsgId)));
			terminalService.updateVehicleMsg(tVehicleMsg);
		} else if ((locateType.equals("1"))
				&& (((vehicleMsgId == null) || (vehicleMsgId.equals("0"))))) {
			// logger.info("locateType == 1; vehicleMsgId == null || vehicleMsgId.equals(0)");

			TVehicleMsg tVehicleMsg = new TVehicleMsg();
			tVehicleMsg.setModel(model);
			tVehicleMsg.setVehicleNum(vehicleNum);
			tVehicleMsg.setSaleDate(DateUtility.strToDate(saleDate));
			tVehicleMsg.setDealer(dealer);
			tVehicleMsg.setInstallers(installers);
			tVehicleMsg.setSaleMethods(saleMethods);
			tVehicleMsg.setContractValue(CharTools.str2Long(contractValue,
					Long.valueOf(0L)));
			tVehicleMsg.setLoanAmount(CharTools.str2Long(loanAmount,
					Long.valueOf(0L)));
			tVehicleMsg.setRepaymentPeriod(DateUtility
					.strToDate(repaymentPeriod));
			tVehicleMsg.setClaimAct(claimAct);
			tVehicleMsg.setDeviceId(deviceId);
			tVehicleMsg.setEntCode(empCode);
			tVehicleMsg.setCrtDate(new Date());
			terminalService.saveVehicleMsg(tVehicleMsg);
		}

		// 日志记录
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_TERMINAL);
		tOptLog.setResult(1L);
		// tOptLog.setOptDesc("修改终端成功.原数据(名称:"+termName_+";手机号:"+simcard_+";车牌号:"+vehicleNumber_+";序列号:"+deviceId_+");"
		// +
		// "修改后数据(名称:"+termName+";手机号:"+simcard+";车牌号:"+vehicleNumber+";序列号:"+deviceId+");");
//		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	/**
	 * 处理配置文件
	 * 
	 * @param content
	 * @return
	 */
	private String dealContent(String content, TTerminalBean tb) {
		// 上下班时间 、工作日期、采集间隔

		String workTimeStart = "<workTime>";
		String workTimeEnd = "</workTime>";
		String locIntervalStart = "<locInterval>";
		String locIntervalEnd = "</locInterval>";
		String weekStart = "<week>";
		String weekEnd = "</week>";

		String workTimeRepValue = content.substring(
				content.indexOf(workTimeStart), content.indexOf(workTimeEnd)
						+ workTimeEnd.length());
		String locIntervalRepValue = content.substring(
				content.indexOf(locIntervalStart),
				content.indexOf(locIntervalEnd) + locIntervalEnd.length());
		String weekRepValue = content.substring(content.indexOf(weekStart),
				content.indexOf(weekEnd) + weekEnd.length());

		String newWorkTimeRepValue = "<workTime>" + tb.getStartTime() + ":00-"
				+ tb.getEndTime() + ":00</workTime>";
		String newLocInterval = "<locInterval>" + tb.getGetherInterval()
				+ "</locInterval>";
		String newWeek = "<week>" + tb.getWeek() + "</week>";
		content = replace(content, workTimeRepValue, newWorkTimeRepValue);
		content = replace(content, locIntervalRepValue, newLocInterval);
		content = replace(content, weekRepValue, newWeek);

		return content;
	}

	/**
	 * 替换字符串
	 * 
	 * @param str
	 * @param oldSubStr
	 * @param newSubStr
	 * @return
	 */
	private static String replace(String str, String oldSubStr, String newSubStr) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		int j = 0;
		int len = oldSubStr.length();
		while ((i = str.indexOf(oldSubStr, j)) != -1) {
			sb.append(str.substring(j, i));
			sb.append(newSubStr);
			j = i + len;
		}
		sb.append(str.substring(j, str.length()));
		return sb.toString();
	}

	// sos删除终端
	public ActionForward deleteTerminals(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TerminalForm tTargetObjectForm = (TerminalForm) form;

		String ids = request.getParameter("ids");
		String[] deviceIds = CharTools.split(ids, ",");
		String vehicleMsgIds = request.getParameter("vehicleMsgIds");
		String[] vehicleMsgIdArr = CharTools.split(vehicleMsgIds, ",");

		UserInfo userInfo = getCurrentUser(request);

		TerminalService terminalService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		for (int i = 0; i < deviceIds.length; ++i) {
			String deviceId = deviceIds[i];
			TTerminalBean terminalBean = new TTerminalBean();
			terminalBean.setDeviceId(deviceId);
			terminalService.deleteTTerminal(terminalBean);
		}

		for (int i = 0; i < vehicleMsgIdArr.length; ++i) {
			String vehicleMsgId = vehicleMsgIdArr[i];
			TVehicleMsg tVehicleMsg = new TVehicleMsg();
			tVehicleMsg.setId(Long.valueOf(Long.parseLong(vehicleMsgId)));
			terminalService.deleteVehicleMsg(tVehicleMsg);
		}

		TOptLog tOptLog = new TOptLog();

		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType("F-002");
		tOptLog.setFunCType("F-002-002");
		tOptLog.setResult(Long.valueOf(1L));
		tOptLog.setOptDesc("删除终端成功!");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");
		return mapping.findForward(null);
	}

	// sos获取所有终端类型

	public ActionForward getAllTermType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		Long userId = userInfo.getUserId();
		List list = tTargetObjectService.getAllTermType();

		StringBuffer jsonSb = new StringBuffer();

		jsonSb.append("['GP_TEST_GPRS','宇弘机械协议'],");
		jsonSb.append("['GP_SHOUHANG_GPRS','宇弘车载协议']");
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write("[" + jsonSb.toString() + "]");
		return mapping.findForward(null);
	}

	public ActionForward oiling(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer jsonSb = new StringBuffer();
		jsonSb.append("{total:3,data:[");
		jsonSb.append("{");
		jsonSb.append("id:'1',");
		jsonSb.append("simcard:'42.5',");
		jsonSb.append("deviceId:'300',");
		jsonSb.append("termName:'2011-4-1'");
		jsonSb.append("},");
		jsonSb.append("{");
		jsonSb.append("id:'2',");
		jsonSb.append("simcard:'70.6',");
		jsonSb.append("deviceId:'500',");
		jsonSb.append("termName:'2011-4-2'");
		jsonSb.append("},");
		jsonSb.append("{");
		jsonSb.append("id:'3',");
		jsonSb.append("simcard:'84.7',");
		jsonSb.append("deviceId:'600',");
		jsonSb.append("termName:'2011-4-3'");
		jsonSb.append("}");
		jsonSb.append("]}");

		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(jsonSb.toString());
		return mapping.findForward(null);
	}

	public ActionForward expense(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer jsonSb = new StringBuffer();
		jsonSb.append("{total:3,data:[");
		jsonSb.append("{");
		jsonSb.append("id:'1',");
		jsonSb.append("simcard:'9000',");
		jsonSb.append("deviceId:'2000',");
		jsonSb.append("termName:'150',");
		jsonSb.append("termName1:'500',");
		jsonSb.append("termName2:'200',");
		jsonSb.append("termName3:'200'");
		jsonSb.append("},");
		jsonSb.append("{");
		jsonSb.append("id:'2',");
		jsonSb.append("simcard:'10000',");
		jsonSb.append("deviceId:'2000',");
		jsonSb.append("termName:'150',");
		jsonSb.append("termName1:'500',");
		jsonSb.append("termName2:'200',");
		jsonSb.append("termName3:'200'");
		jsonSb.append("},");
		jsonSb.append("{");
		jsonSb.append("id:'3',");
		jsonSb.append("simcard:'11000',");
		jsonSb.append("deviceId:'2000',");
		jsonSb.append("termName:'150',");
		jsonSb.append("termName1:'500',");
		jsonSb.append("termName2:'200',");
		jsonSb.append("termName3:'200'");
		jsonSb.append("}");
		jsonSb.append("]}");

		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(jsonSb.toString());
		return mapping.findForward(null);
	}

	// sos新增终端
	public ActionForward addTerminalSecond(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 企业code
		UserInfo userInfo = this.getCurrentUser(request);
		// String empCode = userInfo.getEmpCode();
		// 从request中获取参数
		String empCode = request.getParameter("entCode");// 企业代码
		String userId = request.getParameter("userId");
		String deviceId = request.getParameter("deviceId");// 终端序列号
		String typeCode = request.getParameter("typeCode");// 终端类型码
		String termName = request.getParameter("termName");// 终端名称
		String simcard = request.getParameter("simcard");// 手机号

		// sxh
		String imsi = request.getParameter("imsi");
		String groupId = request.getParameter("groupId");// 组id
		String locateType = request.getParameter("locateType");// 终端类型0:LBS1:GPS
		String province = request.getParameter("province");// 省
		String city = request.getParameter("city");// 市
		String startTime = request.getParameter("startTime");// 上班时间
		String endTime = request.getParameter("endTime");// 下班时间
		String terminalEndtime = request.getParameter("terminalEndtime");// 终端到期时间
		String getherInterval = request.getParameter("getherInterval");// 采集间隔
		// if(locateType.equals("0")){
		// getherInterval = Integer.parseInt(getherInterval)*60+"";
		// }
		String termDesc = request.getParameter("termDesc");// 备注
		// gps终端参数
		String vehicleNumber = request.getParameter("vehicleNumber");// 车牌号
		String vehicleType = request.getParameter("vehicleType");// 车辆类型
		String driverNumber = request.getParameter("driverNumber");// 车主电话
		String oilSpeedLimit = request.getParameter("oilSpeedLimit");// 断油断电速度阀值
		String speedAlarmLimit = request.getParameter("speedAlarmLimit");// 速度限值
		String speedAlarmLast = request.getParameter("speedAlarmLast");// 持续时间
		String holdAlarmFlag = request.getParameter("holdAlarmFlag");// 劫警开关0关1开
		String imgUrl = request.getParameter("imgUrl");// 定位图标
		String week = request.getParameter("week");// 周工作日期
		String carTypeInfo = request.getParameter("carTypeInfo");// 周工作日期

		deviceId = CharTools.killNullString(deviceId);
		typeCode = CharTools.killNullString(typeCode);
		termName = CharTools.killNullString(termName);
		simcard = CharTools.killNullString(simcard);
		imsi = CharTools.killNullString(imsi);
		groupId = CharTools.killNullString(groupId);
		locateType = CharTools.killNullString(locateType);
		province = CharTools.killNullString(province);
		city = CharTools.killNullString(city);
		startTime = CharTools.killNullString(startTime);
		endTime = CharTools.killNullString(endTime);
		getherInterval = CharTools.killNullString(getherInterval);
		termDesc = CharTools.killNullString(termDesc);
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleType = CharTools.killNullString(vehicleType);
		driverNumber = CharTools.killNullString(driverNumber);
		oilSpeedLimit = CharTools.killNullString(oilSpeedLimit);
		speedAlarmLimit = CharTools.killNullString(speedAlarmLimit);
		speedAlarmLast = CharTools.killNullString(speedAlarmLast);
		holdAlarmFlag = CharTools.killNullString(holdAlarmFlag);
		imgUrl = CharTools.killNullString(imgUrl);
		carTypeInfo = CharTools.killNullString(carTypeInfo);
		// Date tendTime=DateUtility.strToDate(terminalEndtime);//终端到期时间
		Date tendTime = DateUtility.add(new Date(), 1);

		empCode = java.net.URLDecoder.decode(empCode, "utf-8");
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		typeCode = java.net.URLDecoder.decode(typeCode, "utf-8");
		termName = java.net.URLDecoder.decode(termName, "utf-8");
		simcard = java.net.URLDecoder.decode(simcard, "utf-8");
		imsi = java.net.URLDecoder.decode(imsi, "utf-8");
		groupId = java.net.URLDecoder.decode(groupId, "utf-8");
		locateType = java.net.URLDecoder.decode(locateType, "utf-8");
		province = java.net.URLDecoder.decode(province, "utf-8");
		city = java.net.URLDecoder.decode(city, "utf-8");
		startTime = java.net.URLDecoder.decode(startTime, "utf-8");
		endTime = java.net.URLDecoder.decode(endTime, "utf-8");
		getherInterval = java.net.URLDecoder.decode(getherInterval, "utf-8");
		termDesc = java.net.URLDecoder.decode(termDesc, "utf-8");
		vehicleNumber = java.net.URLDecoder.decode(vehicleNumber, "utf-8");
		vehicleType = java.net.URLDecoder.decode(vehicleType, "utf-8");
		driverNumber = java.net.URLDecoder.decode(driverNumber, "utf-8");
		oilSpeedLimit = java.net.URLDecoder.decode(oilSpeedLimit, "utf-8");
		speedAlarmLimit = java.net.URLDecoder.decode(speedAlarmLimit, "utf-8");
		speedAlarmLast = java.net.URLDecoder.decode(speedAlarmLast, "utf-8");
		holdAlarmFlag = java.net.URLDecoder.decode(holdAlarmFlag, "utf-8");
		imgUrl = java.net.URLDecoder.decode(imgUrl, "utf-8");

		// entCode = CharTools.javaScriptEscape(entCode);
		deviceId = CharTools.killNullString(deviceId);
		typeCode = CharTools.killNullString(typeCode);
		termName = CharTools.killNullString(termName);
		simcard = CharTools.killNullString(simcard);
		imsi = CharTools.killNullString(imsi);
		groupId = CharTools.killNullString(groupId);
		locateType = CharTools.killNullString(locateType);
		province = CharTools.killNullString(province);
		city = CharTools.killNullString(city);
		startTime = CharTools.killNullString(startTime);
		endTime = CharTools.killNullString(endTime);
		getherInterval = CharTools.killNullString(getherInterval);
		termDesc = CharTools.killNullString(termDesc);
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleType = CharTools.killNullString(vehicleType);
		driverNumber = CharTools.killNullString(driverNumber);
		oilSpeedLimit = CharTools.killNullString(oilSpeedLimit);
		speedAlarmLimit = CharTools.killNullString(speedAlarmLimit);
		speedAlarmLast = CharTools.killNullString(speedAlarmLast);
		holdAlarmFlag = CharTools.killNullString(holdAlarmFlag);
		imgUrl = CharTools.killNullString(imgUrl);
		carTypeInfo = CharTools.killNullString(carTypeInfo);

		TerminalService terminalService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");

		// 查找是否已存在
		boolean b = terminalService.findTermById(deviceId);
		if (b) {
			response.getWriter().write("{result:\"3\"}");// 已存在此终端
			return mapping.findForward(null);
		}
		// 查询企业终端数是否达到上限
		// Long maxUserNum = userInfo.getEnt().getMaxUserNum();
		String strMax = request.getParameter("maxUserNum");
		Long maxUserNum = null;
		if (strMax == null || strMax.equals("null")) {
			maxUserNum = null;
		} else if (strMax.length() > 0) {
			maxUserNum = Long.valueOf(strMax);
		}

		if (maxUserNum != null && maxUserNum.longValue() > 0) {
			Long termNum = terminalService.findTermNum(empCode);
			if (termNum.longValue() >= maxUserNum) {
				response.getWriter().write("{result:\"4\"}");// 终端数达到上限
				return mapping.findForward(null);
			}
		}

		// 保存
		TTerminalBean terminalBean = new TTerminalBean();
		terminalBean.setEntCode(empCode);
		terminalBean.setDeviceId(deviceId);
		terminalBean.setTypeCode(typeCode);
		terminalBean.setTermName(termName);
		terminalBean.setTermDesc(termDesc);
		terminalBean.setGroupId(Long.parseLong(groupId));
		terminalBean.setLocateType(locateType);
		terminalBean.setSimcard(simcard);
		terminalBean.setImsi(imsi);
		terminalBean.setDriverNumber(driverNumber);
		terminalBean.setProvince(province);
		terminalBean.setCity(city);
		terminalBean.setStartTime(startTime);
		terminalBean.setEndTime(endTime);
		terminalBean.setGetherInterval(Long.parseLong(getherInterval));
		terminalBean.setVehicleNumber(vehicleNumber);
		terminalBean.setVehicleType(vehicleType);
		terminalBean.setOilSpeedLimit(Long.parseLong(oilSpeedLimit));
		terminalBean.setSpeedAlarmLimit(Long.parseLong(speedAlarmLimit));
		terminalBean.setSpeedAlarmLast(Long.parseLong(speedAlarmLast));
		terminalBean.setHoldAlarmFlag(holdAlarmFlag);
		terminalBean.setImgUrl(imgUrl);
		terminalBean.setWeek(Long.parseLong(week));
		terminalBean.setIsAllocate("1");
		terminalBean.setSetExpirationTime(tendTime);// 终端到期时间
		if (!carTypeInfo.equals("")) {
			terminalBean.setCarTypeInfo(Long.parseLong(carTypeInfo));
		}
		// save
		terminalService.saveTTerminal(terminalBean);
		// add by wangzhen 2012-11-29
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<ModuleParamConfig> mcs = terminalService.getAllModule();
			StringBuffer contentDef0 = new StringBuffer();
			contentDef0
					.append("<modeConfig><mobileParamInterval>240</mobileParamInterval>");
			contentDef0.append("<termExpirationTime>"
					+ sm.format(terminalBean.getSetExpirationTime())
					+ "</termExpirationTime>");
			// logFlag 新需求 新增终端默认日志开关关闭 0是关闭，1是开启
			contentDef0
					.append("<logFlag>0</logFlag><logUploadTime>240</logUploadTime><netSmsFlag>1</netSmsFlag><uninstallSmsFlag>1</uninstallSmsFlag><signMode>0</signMode></modeConfig>");
			StringBuffer contentDef1 = new StringBuffer();
			contentDef1.append("<modeConfig><locMode>1</locMode>");
			contentDef1.append("<week>" + terminalBean.getWeek() + "</week>");
			contentDef1.append("<workTime>" + terminalBean.getStartTime()
					+ ":00-" + terminalBean.getEndTime() + ":00</workTime>");
			contentDef1.append("<locInterval>"
					+ terminalBean.getGetherInterval() + "</locInterval>");
			contentDef1
					.append("<gpsEnable>1</gpsEnable><searchTime>60</searchTime><networkEnable>0</networkEnable><cellEnable>0</cellEnable>"
							+ "<gpsDeviationFilter>200</gpsDeviationFilter><networkDeviationFilter>500</networkDeviationFilter><cellDeviationFilter>1000</cellDeviationFilter>"
							+ "<repeatDate>1</repeatDate><repeatCheckInterval>300</repeatCheckInterval></modeConfig>");

			StringBuffer contentDef2 = new StringBuffer();
			contentDef2.append("<modeConfig><locMode>0</locMode>");
			contentDef2.append("<week>127</week>");
			contentDef2.append("<workTime>00:00:00-23:59:59</workTime>");
			contentDef2.append("<locInterval>300</locInterval>");
			contentDef2
					.append("<gpsEnable>1</gpsEnable><searchTime>15</searchTime><networkEnable>1</networkEnable><cellEnable>1</cellEnable>"
							+ "<gpsDeviationFilter>200</gpsDeviationFilter><networkDeviationFilter>500</networkDeviationFilter><cellDeviationFilter>1000</cellDeviationFilter>"
							+ "<repeatDate>1</repeatDate><repeatCheckInterval>300</repeatCheckInterval></modeConfig>");
			Date currentDate = new Date();
			if (mcs == null || mcs.size() <= 0) {
				// 增加全局默认配置
				for (int i = 0; i <= 2; i++) {
					TermParamConfig termPar0 = new TermParamConfig();
					if (i == 0) {
						termPar0.setContent(contentDef0.toString());
					} else if (i == 1) {
						termPar0.setContent(contentDef1.toString());
					} else if (i == 2) {
						termPar0.setContent(contentDef2.toString());
					}
					termPar0.setCreateOn(currentDate);
					termPar0.setCreateBy(termName);
					termPar0.setDeviceId(terminalBean.getDeviceId());
					termPar0.setEntCode(terminalBean.getEntCode());
					termPar0.setType(i);
					termPar0.setLastUpdateOn(currentDate);
					termPar0.setLastUpdateBy(termName);
					terminalService.saveTermParamConfig(termPar0);
				}
			} else {
				ModuleParamConfig mc0 = null;
				ModuleParamConfig mc1 = null;
				ModuleParamConfig mc2 = null;
				for (ModuleParamConfig mc : mcs) {
					if (mc.getType() == 0) {
						mc0 = mc;
					}
					if (mc.getType() == 1) {
						mc1 = mc;
					}
					if (mc.getType() == 2) {
						mc2 = mc;
					}
				}
				TermParamConfig termPar0 = new TermParamConfig();
				termPar0.setCreateOn(currentDate);
				termPar0.setCreateBy(termName);
				termPar0.setDeviceId(terminalBean.getDeviceId());
				termPar0.setEntCode(terminalBean.getEntCode());
				termPar0.setType(0);
				termPar0.setLastUpdateOn(currentDate);
				termPar0.setLastUpdateBy(termName);

				if (mc0 == null) {
					termPar0.setContent(contentDef0.toString());
					terminalService.saveTermParamConfig(termPar0);
				} else {
					String content = mc0.getContent();
					content = dealContentFromType0(content, terminalBean);
					termPar0.setContent(content);
					terminalService.saveTermParamConfig(termPar0);
				}
				TermParamConfig termPar1 = new TermParamConfig();
				termPar1.setCreateOn(currentDate);
				termPar1.setCreateBy(termName);
				termPar1.setDeviceId(terminalBean.getDeviceId());
				termPar1.setEntCode(terminalBean.getEntCode());
				termPar1.setType(1);
				termPar1.setLastUpdateOn(currentDate);
				termPar1.setLastUpdateBy(termName);
				if (mc1 == null) {
					termPar1.setContent(contentDef1.toString());
					terminalService.saveTermParamConfig(termPar1);
				} else {
					String content = mc1.getContent();
					content = dealContent(content, terminalBean);
					termPar1.setContent(content);
					terminalService.saveTermParamConfig(termPar1);
				}
				TermParamConfig termPar2 = new TermParamConfig();
				termPar2.setCreateOn(currentDate);
				termPar2.setCreateBy(termName);
				termPar2.setDeviceId(terminalBean.getDeviceId());
				termPar2.setEntCode(terminalBean.getEntCode());
				termPar2.setType(1);
				termPar2.setLastUpdateOn(currentDate);
				termPar2.setLastUpdateBy(termName);
				if (mc2 == null) {
					termPar2.setContent(contentDef2.toString());
					terminalService.saveTermParamConfig(termPar2);
				} else {
					String content = mc2.getContent();
					content = dealContent(content, terminalBean);
					termPar2.setContent(content);
					terminalService.saveTermParamConfig(termPar2);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(empCode);
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(Long.valueOf(userId));
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_TERMINAL);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("添加终端成功!(名称:" + termName + ";手机号:" + simcard
				+ ";车牌号:" + vehicleNumber + ";序列号:" + deviceId + ");");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos修改终端
	public ActionForward updateTerminalSecond(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserInfo userInfo = this.getCurrentUser(request);
		// String empCode = userInfo.getEmpCode();
		// 从request中获取参数
		String empCode = request.getParameter("entCode");// 企业代码
		String userId = request.getParameter("userId");
		String deviceId = request.getParameter("deviceId");// 终端序列号
		String typeCode = request.getParameter("typeCode");// 终端类型码
		String termName = request.getParameter("termName");// 终端名称
		String simcard = request.getParameter("simcard");// 手机号
		// sxh
		String imsi = request.getParameter("imsi");// 手机号
		String groupId = request.getParameter("groupId");// 组id
		String locateType = request.getParameter("locateType");// 终端类型0:LBS1:GPS
		String province = request.getParameter("province");// 省
		String city = request.getParameter("city");// 市
		String startTime = request.getParameter("startTime");// 上班时间
		String endTime = request.getParameter("endTime");// 下班时间
		// String terminalEndtime =
		// request.getParameter("terminalEndtime");//终端到期时间
		String getherInterval = request.getParameter("getherInterval");// 采集间隔
		// if(locateType.equals("0")){
		// getherInterval = Integer.parseInt(getherInterval)*60+"";
		// }
		String termDesc = request.getParameter("termDesc");// 备注
		// gps终端参数
		String vehicleNumber = request.getParameter("vehicleNumber");// 车牌号
		String vehicleType = request.getParameter("vehicleType");// 车辆类型
		String driverNumber = request.getParameter("driverNumber");// 车主电话
		String oilSpeedLimit = request.getParameter("oilSpeedLimit");// 断油断电速度阀值
		String speedAlarmLimit = request.getParameter("speedAlarmLimit");// 速度限值
		String speedAlarmLast = request.getParameter("speedAlarmLast");// 持续时间
		String holdAlarmFlag = request.getParameter("holdAlarmFlag");// 劫警开关0关1开
		String imgUrl = request.getParameter("imgUrl");// 定位图标
		String week = request.getParameter("week");
		String carTypeInfo = request.getParameter("carTypeInfo");

		deviceId = CharTools.killNullString(deviceId);
		typeCode = CharTools.killNullString(typeCode);
		termName = CharTools.killNullString(termName);
		simcard = CharTools.killNullString(simcard);
		imsi = CharTools.killNullString(imsi);
		groupId = CharTools.killNullString(groupId);
		locateType = CharTools.killNullString(locateType);
		province = CharTools.killNullString(province);
		city = CharTools.killNullString(city);
		startTime = CharTools.killNullString(startTime);
		endTime = CharTools.killNullString(endTime);
		getherInterval = CharTools.killNullString(getherInterval);
		termDesc = CharTools.killNullString(termDesc);
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleType = CharTools.killNullString(vehicleType);
		driverNumber = CharTools.killNullString(driverNumber);
		oilSpeedLimit = CharTools.killNullString(oilSpeedLimit);
		speedAlarmLimit = CharTools.killNullString(speedAlarmLimit);
		speedAlarmLast = CharTools.killNullString(speedAlarmLast);
		holdAlarmFlag = CharTools.killNullString(holdAlarmFlag);
		imgUrl = CharTools.killNullString(imgUrl);
		carTypeInfo = CharTools.killNullString(carTypeInfo);
		// Date tendTime=DateUtility.strToDate(terminalEndtime);//修改终端到期时间
		// Date tendTime = DateUtility.add(new Date(), 1);

		empCode = java.net.URLDecoder.decode(empCode, "utf-8");
		deviceId = java.net.URLDecoder.decode(deviceId, "utf-8");
		typeCode = java.net.URLDecoder.decode(typeCode, "utf-8");
		termName = java.net.URLDecoder.decode(termName, "utf-8");
		simcard = java.net.URLDecoder.decode(simcard, "utf-8");
		imsi = java.net.URLDecoder.decode(imsi, "utf-8");
		groupId = java.net.URLDecoder.decode(groupId, "utf-8");
		locateType = java.net.URLDecoder.decode(locateType, "utf-8");
		province = java.net.URLDecoder.decode(province, "utf-8");
		city = java.net.URLDecoder.decode(city, "utf-8");
		startTime = java.net.URLDecoder.decode(startTime, "utf-8");
		endTime = java.net.URLDecoder.decode(endTime, "utf-8");
		getherInterval = java.net.URLDecoder.decode(getherInterval, "utf-8");
		termDesc = java.net.URLDecoder.decode(termDesc, "utf-8");
		vehicleNumber = java.net.URLDecoder.decode(vehicleNumber, "utf-8");
		vehicleType = java.net.URLDecoder.decode(vehicleType, "utf-8");
		driverNumber = java.net.URLDecoder.decode(driverNumber, "utf-8");
		oilSpeedLimit = java.net.URLDecoder.decode(oilSpeedLimit, "utf-8");
		speedAlarmLimit = java.net.URLDecoder.decode(speedAlarmLimit, "utf-8");
		speedAlarmLast = java.net.URLDecoder.decode(speedAlarmLast, "utf-8");
		holdAlarmFlag = java.net.URLDecoder.decode(holdAlarmFlag, "utf-8");
		imgUrl = java.net.URLDecoder.decode(imgUrl, "utf-8");

		// entCode = CharTools.javaScriptEscape(entCode);
		deviceId = CharTools.killNullString(deviceId);
		typeCode = CharTools.killNullString(typeCode);
		termName = CharTools.killNullString(termName);
		simcard = CharTools.killNullString(simcard);
		imsi = CharTools.killNullString(imsi);
		groupId = CharTools.killNullString(groupId);
		locateType = CharTools.killNullString(locateType);
		province = CharTools.killNullString(province);
		city = CharTools.killNullString(city);
		startTime = CharTools.killNullString(startTime);
		endTime = CharTools.killNullString(endTime);
		getherInterval = CharTools.killNullString(getherInterval);
		termDesc = CharTools.killNullString(termDesc);
		vehicleNumber = CharTools.killNullString(vehicleNumber);
		vehicleType = CharTools.killNullString(vehicleType);
		driverNumber = CharTools.killNullString(driverNumber);
		oilSpeedLimit = CharTools.killNullString(oilSpeedLimit);
		speedAlarmLimit = CharTools.killNullString(speedAlarmLimit);
		speedAlarmLast = CharTools.killNullString(speedAlarmLast);
		holdAlarmFlag = CharTools.killNullString(holdAlarmFlag);
		imgUrl = CharTools.killNullString(imgUrl);
		carTypeInfo = CharTools.killNullString(carTypeInfo);

		TerminalService terminalService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		List<TTerminal> list = terminalService.findTerminal(deviceId, empCode,
				false);
		TTerminal tTerminal = list.get(0);
		String termName_ = tTerminal.getTermName();
		String simcard_ = tTerminal.getSimcard();
		String vehicleNumber_ = tTerminal.getVehicleNumber();
		String deviceId_ = tTerminal.getDeviceId();

		TTerminalBean terminalBean = new TTerminalBean();
		terminalBean.setEntCode(empCode);
		terminalBean.setDeviceId(deviceId);
		terminalBean.setTypeCode(typeCode);
		terminalBean.setTermName(termName);
		terminalBean.setTermDesc(termDesc);
		terminalBean.setGroupId(Long.parseLong(groupId));
		terminalBean.setLocateType(locateType);
		terminalBean.setSimcard(simcard);
		terminalBean.setImsi(imsi);
		terminalBean.setDriverNumber(driverNumber);
		terminalBean.setProvince(province);
		terminalBean.setCity(city);
		terminalBean.setStartTime(startTime);
		terminalBean.setEndTime(endTime);
		terminalBean.setGetherInterval(Long.parseLong(getherInterval));
		terminalBean.setVehicleNumber(vehicleNumber);
		terminalBean.setVehicleType(vehicleType);
		terminalBean.setOilSpeedLimit(Long.parseLong(oilSpeedLimit));
		terminalBean.setSpeedAlarmLimit(Long.parseLong(speedAlarmLimit));
		terminalBean.setSpeedAlarmLast(Long.parseLong(speedAlarmLast));
		terminalBean.setHoldAlarmFlag(holdAlarmFlag);
		terminalBean.setImgUrl(imgUrl);
		terminalBean.setWeek(Long.parseLong(week));
		terminalBean.setIsAllocate("1");
		// terminalBean.setSetExpirationTime(tendTime);//终端到期时间
		if (!carTypeInfo.equals("")) {
			terminalBean.setCarTypeInfo(Long.parseLong(carTypeInfo));
		}

		// update
		try {
			terminalService.updateTTerminal(terminalBean);
			// modify by wangzhen 2012-11-28 修改终端上下班、工作日期和采集间隔的同时，修改其对应的配置文件
			TermParamConfig termPc = terminalService
					.getTermParamConfigByDeviceIdAndType(
							terminalBean.getDeviceId(), 1);
			if (termPc != null) {
				String content = termPc.getContent();
				content = dealContent(content, terminalBean);
				termPc.setContent(content);
				termPc.setLastUpdateOn(new Date());
				termPc.setLastUpdateBy(terminalBean.getTermName());
				terminalService.updateTermConfig(termPc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("{result:\"0\"}");// 失败
		}

		// 日志记录
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(empCode);
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(Long.valueOf(userId));
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_TERMINAL);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("修改终端成功.原数据(名称:" + termName_ + ";手机号:" + simcard_
				+ ";车牌号:" + vehicleNumber_ + ";序列号:" + deviceId_ + ");"
				+ "修改后数据(名称:" + termName + ";手机号:" + simcard + ";车牌号:"
				+ vehicleNumber + ";序列号:" + deviceId + ");");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return mapping.findForward(null);
	}

	// sos企业终端列表
	public ActionForward listTerminalSecond(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从request中获取参数
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String searchValue = request.getParameter("searchValue");

		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);

		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;

		// 从session中获取企业代码
		UserInfo userInfo = this.getCurrentUser(request);
		// String entCode = userInfo.getEmpCode();
		String entCode = request.getParameter("entCode");
		entCode = java.net.URLDecoder.decode(entCode, "utf-8");
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		// Long userId = userInfo.getUserId();
		String strId = request.getParameter("userId");
		Long userId = Long.valueOf(strId);
		Page list = tTargetObjectService.listTerminal(entCode, userId, page,
				limitint, searchValue);
		// json
		StringBuffer jsonSb = new StringBuffer();
		String total = "";
		if (list != null && list.getResult() != null) {
			total = "{total:" + list.getTotalCount() + ",data:[";
			// jsonSb.append("{total:"+gridList.getCountTotal()+",data:[");
			Iterator i = list.getResult().iterator();
			while (i.hasNext()) {
				Object[] userObj = (Object[]) i.next();
				TTerminal terminal = (TTerminal) userObj[0];// 终端
				TTermGroup group = (TTermGroup) userObj[1];// 组
				TEntTermtype tp = (TEntTermtype) userObj[2];// 终端类型
				jsonSb.append("{");
				jsonSb.append("id:'"
						+ CharTools.javaScriptEscape(terminal.getDeviceId())
						+ "',");// id也是终端序列号，终端唯一标识
				jsonSb.append("simcard:'"
						+ CharTools.javaScriptEscape(terminal.getSimcard())
						+ "',");// 电话号码
				jsonSb.append("deviceId:'"
						+ CharTools.javaScriptEscape(terminal.getDeviceId())
						+ "',");// 电话号码
				jsonSb.append("termName:'"
						+ CharTools.javaScriptEscape(terminal.getTermName())
						+ "',");// 终端名称
				jsonSb.append("termEndtime:'"
						+ CharTools.javaScriptEscape(DateUtility
								.dateTimeToStr(terminal.getExpirationTime()))
						+ "',");// 终端到期时间
				jsonSb.append("groupName:'"
						+ CharTools.javaScriptEscape(group != null ? group
								.getGroupName() : "") + "',");// 组名称
				jsonSb.append("groupId:'"
						+ CharTools.javaScriptEscape((group != null ? group
								.getId() : "") + "") + "',");// 组id
				jsonSb.append("province:'"
						+ CharTools.javaScriptEscape(terminal.getProvince())
						+ "',");// 省
				jsonSb.append("city:'"
						+ CharTools.javaScriptEscape(terminal.getCity()) + "',");// 市

				jsonSb.append("startTime:'"
						+ CharTools.javaScriptEscape(terminal.getStartTime())
						+ "',");// 市
				jsonSb.append("endTime:'"
						+ CharTools.javaScriptEscape(terminal.getEndTime())
						+ "',");// 市

				// if(terminal.getLocateType().equals("0")){
				// jsonSb.append("typeCode:'"+CharTools.javaScriptEscape("P-LENOVO")+"',");//市
				// }else if(terminal.getLocateType().equals("1")){
				// jsonSb.append("typeCode:'"+CharTools.javaScriptEscape("GP-SZHQ-GPRS")+"',");//市
				// }
				jsonSb.append("typeCode:'"
						+ CharTools.javaScriptEscape(terminal.getTEntTermtype()
								.getTypeCode()) + "',");// 市
				jsonSb.append("typeName:'"
						+ CharTools.javaScriptEscape(tp.getTypeName()) + "',");// 终端名称
				jsonSb.append("getherInterval:'"
						+ CharTools.killNullLong2String(
								terminal.getGetherInterval(), "0") + "',");// 市

				jsonSb.append("termDesc:'"
						+ CharTools.javaScriptEscape(terminal.getTermdesc())
						+ "',");// 市
				// String locateType =
				// CharTools.javaScriptEscape(terminal.getLocateType());
				jsonSb.append("locateType:'"
						+ CharTools.javaScriptEscape(terminal.getLocateType())
						+ "',");// 终端类型0:人LBS 1:车GPS

				jsonSb.append("vehicleNumber:'"
						+ CharTools.javaScriptEscape(terminal
								.getVehicleNumber()) + "',");// 市

				jsonSb.append("vehicleType:'"
						+ CharTools.javaScriptEscape(terminal.getVehicleType())
						+ "',");// 市
				jsonSb.append("driverNumber:'"
						+ CharTools.javaScriptEscape(terminal.getDriverNumber())
						+ "',");// 市
				jsonSb.append("oilSpeedLimit:'"
						+ CharTools.killNullLong2String(
								terminal.getOilSpeedLimit(), "0") + "',");// 市
				jsonSb.append("holdAlarmFlag:'"
						+ CharTools.javaScriptEscape(terminal
								.getHoldAlarmFlag()) + "',");// 市
				jsonSb.append("speedAlarmLimit:'"
						+ CharTools.killNullLong2String(
								terminal.getSpeedAlarmLimit(), "0") + "',");// 市
				jsonSb.append("speedAlarmLast:'"
						+ CharTools.killNullLong2String(
								terminal.getSpeedAlarmLast(), "0") + "',");// 状态0:在用
																			// 1:停止使用
				jsonSb.append("imsi:'"
						+ CharTools.javaScriptEscape(terminal.getImsi()) + "',");// 手机卡唯一序列号
				String imgUrl = terminal.getImgUrl();
				if (imgUrl == null
						&& CharTools.javaScriptEscape(terminal.getLocateType())
								.equals("0")) {
					imgUrl = "persion";
				} else if (imgUrl == null
						&& CharTools.javaScriptEscape(terminal.getLocateType())
								.equals("1")) {
					imgUrl = "car";
				}
				jsonSb.append("imgUrl:'" + imgUrl + "',");// 定位图标
				jsonSb.append("week:'"
						+ CharTools.killNullLong2String(terminal.getWeek(),
								"127") + "',");
				jsonSb.append("carTypeInfo:'"
						+ CharTools.killNullLong2String(
								terminal.getCarTypeInfoId(), "") + "'");

				jsonSb.append("},");
			}
			if (jsonSb.length() > 0) {
				jsonSb.deleteCharAt(jsonSb.length() - 1);
			}
			jsonSb.append("]}");
			// System.out.println(jsonSb.toString());
		}
		// System.out.println(total+jsonSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(total + jsonSb.toString());
		return mapping.findForward(null);
	}

	// 终端树sos企业终端组
	public ActionForward queryTerminalTree(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从session中获取企业代码和用户id
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		entCode = java.net.URLDecoder.decode(entCode, "utf-8");
		Long userId = userInfo.getUserId();
		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		searchValue = CharTools.killNullString(searchValue);
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		String tree = tTargetObjectService.queryTerminalTree(entCode, userId,
				searchValue);
		String tree1 = "";
		if (tree != null && tree.length() > 0) {
			tree1 = "[{id: '-101',text: '" + userInfo.getEnt().getEntName()
					+ "',iconCls: 'icon-group',leaf: false ,children : ["
					+ tree + "]}]";
		}/*
		 * else{ tree1 = "[{id: '-101',text: '"+userInfo.getEnt().getEntName()+
		 * "',iconCls: 'icon-group',leaf: false}]"; }
		 */
		response.setCharacterEncoding("GBK");
		// System.out.println(tree1);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(tree1);
		response.getWriter().flush();
		return mapping.findForward(null);
	}

	public ActionForward listTerminalByDeviceId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String searchValue = request.getParameter("searchValue");
		searchValue = CharTools.killNullString(searchValue);
		String deviceIdsStr = request.getParameter("deviceIds");
		deviceIdsStr = CharTools.killNullString(deviceIdsStr);
		String deviceIds = CharTools.splitAndAdd(deviceIdsStr);

		searchValue = URLDecoder.decode(searchValue, "UTF-8");

		UserInfo userInfo = getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		TerminalService tTargetObjectService = (TerminalServiceImpl) SpringHelper
				.getBean("tTargetObjectService");
		Long userId = userInfo.getUserId();
		String expExcel = request.getParameter("expExcel");
		expExcel = CharTools.killNullString(expExcel);

		if (expExcel.equalsIgnoreCase("true")) {
			Page<Object[]> list = tTargetObjectService.listTerminalByDeviceId(
					entCode, userId, 1, 65535, searchValue, deviceIds);
			ExcelWorkBook excelWorkBook = new ExcelWorkBook(response);
			excelWorkBook.addHeader("SIM卡号", 15);
			excelWorkBook.addHeader("终端序列号", 15);
			excelWorkBook.addHeader("终端设备号", 15);
			excelWorkBook.addHeader("所属组名称", 15);
			excelWorkBook.addHeader("省", 15);
			excelWorkBook.addHeader("市", 15);
			excelWorkBook.addHeader("上班时间", 15);
			excelWorkBook.addHeader("下班时间", 15);

			excelWorkBook.addHeader("采集间隔", 15);
			excelWorkBook.addHeader("员工备注", 15);
			excelWorkBook.addHeader("终端类型", 15);
			excelWorkBook.addHeader("GPS厂商", 15);
			excelWorkBook.addHeader("客户姓名", 15);
			excelWorkBook.addHeader("车辆型号", 15);
			excelWorkBook.addHeader("客户手机", 15);
			excelWorkBook.addHeader("机械型号", 15);
			excelWorkBook.addHeader("机号(车架号)", 15);
			excelWorkBook.addHeader("销售日期(提机日)", 15);
			excelWorkBook.addHeader("销售方式", 15);
			excelWorkBook.addHeader("经销商", 15);
			excelWorkBook.addHeader("安装人员", 15);
			excelWorkBook.addHeader("合同金额(元)", 15);
			excelWorkBook.addHeader("贷款金额(元)", 15);
			excelWorkBook.addHeader("还款期限", 15);
			excelWorkBook.addHeader("债权担当", 15);

			int row = 0;
			for (Object[] objects : list.getResult()) {
				TTerminal terminal = (TTerminal) objects[0];
				TTermGroup group = (TTermGroup) objects[1];
				TEntTermtype tp = (TEntTermtype) objects[2];
				TVehicleMsg tv = (TVehicleMsg) objects[3];
				long id = 0L;
				String model = "";
				String vehicleNum = "";
				Date saleDate = null;
				String saleMethods = "";
				String dealer = "";
				String installers = "";
				long contractValue = 0L;
				long loanAmount = 0L;
				Date repaymentPeriod = null;
				String claimAct = "";
				if (tv != null) {
					id = tv.getId().longValue();
					model = tv.getModel();
					vehicleNum = tv.getVehicleNum();
					saleDate = tv.getSaleDate();
					saleMethods = tv.getSaleMethods();
					dealer = tv.getDealer();
					installers = tv.getInstallers();
					contractValue = tv.getContractValue().longValue();
					loanAmount = tv.getLoanAmount().longValue();
					repaymentPeriod = tv.getRepaymentPeriod();
					claimAct = tv.getClaimAct();
				}

				int col = 0;
				++row;
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(terminal.getSimcard()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(terminal.getDeviceId()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(terminal.getTermName()));
				excelWorkBook.addCell(col++, row, CharTools
						.javaScriptEscape((group != null) ? group
								.getGroupName() : ""));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(terminal.getProvince()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(terminal.getCity()));
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(terminal.getStartTime()));
				excelWorkBook.addCell(col++, row,
						CharTools.killNullString(terminal.getEndTime()));

				excelWorkBook.addCell(col++, row, CharTools
						.killNullLong2String(Long.valueOf(terminal
								.getGetherInterval().longValue() / 60L), "0"));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(terminal.getTermdesc()));
				if (terminal.getLocateType().equals("0"))
					excelWorkBook.addCell(col++, row, "人员");
				else {
					excelWorkBook.addCell(col++, row, "车辆");
				}
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(tp.getTypeName()));
				excelWorkBook
						.addCell(col++, row, CharTools
								.javaScriptEscape(terminal.getVehicleNumber()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(terminal.getVehicleType()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(terminal.getDriverNumber()));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(model));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(vehicleNum));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullString(DateUtility.dateToStr(saleDate)));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(saleMethods));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(dealer));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(installers));
				excelWorkBook.addCell(
						col++,
						row,
						CharTools.killNullLong2String(
								Long.valueOf(contractValue), "0"));
				excelWorkBook.addCell(col++, row, CharTools
						.killNullLong2String(Long.valueOf(loanAmount), "0"));
				excelWorkBook
						.addCell(col++, row, CharTools
								.killNullString(DateUtility
										.dateToStr(repaymentPeriod)));
				excelWorkBook.addCell(col++, row,
						CharTools.javaScriptEscape(claimAct));
			}

			excelWorkBook.write();
			return null;
		}

		StringBuffer jsonSb = new StringBuffer();
		String total = "";
		int startint = Integer.parseInt(start);
		int limitint = Integer.parseInt(limit);
		int page = startint / limitint + 1;
		if ((start != null) && (start.length() > 0) && (limit != null)
				&& (limit.length() > 0)) {
			Page list = tTargetObjectService.listTerminalByDeviceId(entCode,
					userId, page, limitint, searchValue, deviceIds);
			if ((list != null) && (list.getResult() != null)) {
				total = "{total:" + list.getTotalCount() + ",data:[";

				Iterator i = list.getResult().iterator();
				while (i.hasNext()) {
					Object[] userObj = (Object[]) i.next();
					TTerminal terminal = (TTerminal) userObj[0];
					TTermGroup group = (TTermGroup) userObj[1];
					TEntTermtype tp = (TEntTermtype) userObj[2];
					TVehicleMsg tv = (TVehicleMsg) userObj[3];
					long id = 0L;
					String model = "";
					String vehicleNum = "";
					Date saleDate = null;
					String saleMethods = "";
					String dealer = "";
					String installers = "";
					long contractValue = 0L;
					long loanAmount = 0L;
					Date repaymentPeriod = null;
					String claimAct = "";
					Date crtDate = null;
					if (tv != null) {
						id = tv.getId().longValue();
						model = tv.getModel();
						vehicleNum = tv.getVehicleNum();
						saleDate = tv.getSaleDate();
						saleMethods = tv.getSaleMethods();
						dealer = tv.getDealer();
						installers = tv.getInstallers();
						contractValue = tv.getContractValue().longValue();
						loanAmount = tv.getLoanAmount().longValue();
						repaymentPeriod = tv.getRepaymentPeriod();
						claimAct = tv.getClaimAct();
						crtDate = tv.getCrtDate();
					}

					jsonSb.append("{");
					jsonSb.append("id:'" + terminal.getDeviceId() + "',");
					jsonSb.append("simcard:'"
							+ CharTools.javaScriptEscape(terminal.getSimcard())
							+ "',");
					jsonSb.append("deviceId:'"
							+ CharTools.javaScriptEscape(terminal.getDeviceId())
							+ "',");
					jsonSb.append("termName:'"
							+ CharTools.javaScriptEscape(terminal.getTermName())
							+ "',");
					jsonSb.append("groupName:'"
							+ CharTools
									.javaScriptEscape((group != null) ? group
											.getGroupName() : "") + "',");
					jsonSb.append("groupId:'"
							+ CharTools.killNullString(new StringBuilder()
									.append((group != null) ? group.getId()
											: "").toString()) + "',");
					jsonSb.append("province:'"
							+ CharTools.javaScriptEscape(terminal.getProvince())
							+ "',");
					jsonSb.append("city:'"
							+ CharTools.javaScriptEscape(terminal.getCity())
							+ "',");
					jsonSb.append("startTime:'"
							+ CharTools.killNullString(terminal.getStartTime())
							+ "',");
					jsonSb.append("endTime:'"
							+ CharTools.killNullString(terminal.getEndTime())
							+ "',");
					jsonSb.append("typeCode:'"
							+ CharTools.killNullString(terminal
									.getTEntTermtype().getTypeCode()) + "',");
					jsonSb.append("typeName:'"
							+ CharTools.javaScriptEscape(tp.getTypeName())
							+ "',");
					jsonSb.append("getherInterval:'"
							+ CharTools.killNullLong2String(Long
									.valueOf(terminal.getGetherInterval()
											.longValue() / 60L), "0") + "',");
					jsonSb.append("termDesc:'"
							+ CharTools.javaScriptEscape(terminal.getTermdesc())
							+ "',");
					jsonSb.append("locateType:'"
							+ CharTools.javaScriptEscape(terminal
									.getLocateType()) + "',");
					jsonSb.append("vehicleNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleNumber()) + "',");
					jsonSb.append("vehicleType:'"
							+ CharTools.javaScriptEscape(terminal
									.getVehicleType()) + "',");
					jsonSb.append("driverNumber:'"
							+ CharTools.javaScriptEscape(terminal
									.getDriverNumber()) + "',");
					jsonSb.append("oilSpeedLimit:'"
							+ CharTools.killNullLong2String(
									terminal.getOilSpeedLimit(), "0") + "',");
					jsonSb.append("holdAlarmFlag:'"
							+ CharTools.killNullString(terminal
									.getHoldAlarmFlag()) + "',");
					jsonSb.append("speedAlarmLimit:'"
							+ CharTools.killNullLong2String(
									terminal.getSpeedAlarmLimit(), "0") + "',");
					jsonSb.append("speedAlarmLast:'"
							+ CharTools.killNullLong2String(
									terminal.getSpeedAlarmLast(), "0") + "',");
					String imgUrl = terminal.getImgUrl();
					if ((imgUrl == null)
							&& (CharTools.killNullString(terminal
									.getLocateType()).equals("0")))
						imgUrl = "persion";
					else if ((imgUrl == null)
							&& (CharTools.killNullString(terminal
									.getLocateType()).equals("1"))) {
						imgUrl = "car";
					}
					jsonSb.append("imgUrl:'" + imgUrl + "',");
					jsonSb.append("week:'"
							+ CharTools.killNullLong2String(terminal.getWeek(),
									"127") + "',");

					jsonSb.append("vehicleMsgId:'" + id + "',");
					jsonSb.append("model:'" + CharTools.javaScriptEscape(model)
							+ "',");
					jsonSb.append("vehicleNum:'"
							+ CharTools.javaScriptEscape(vehicleNum) + "',");
					jsonSb.append("saleDate:'"
							+ CharTools.killNullString(DateUtility
									.dateToStr(saleDate)) + "',");
					jsonSb.append("saleMethods:'"
							+ CharTools.javaScriptEscape(saleMethods) + "',");
					jsonSb.append("dealer:'"
							+ CharTools.javaScriptEscape(dealer) + "',");
					jsonSb.append("installers:'"
							+ CharTools.javaScriptEscape(installers) + "',");
					jsonSb.append("contractValue:'"
							+ CharTools.killNullLong2String(
									Long.valueOf(contractValue), "0") + "',");
					jsonSb.append("loanAmount:'"
							+ CharTools.killNullLong2String(
									Long.valueOf(loanAmount), "0") + "',");
					jsonSb.append("repaymentPeriod:'"
							+ CharTools.killNullString(DateUtility
									.dateToStr(repaymentPeriod)) + "',");
					jsonSb.append("claimAct:'"
							+ CharTools.javaScriptEscape(claimAct) + "',");
					jsonSb.append("crtDate:'"
							+ CharTools.killNullString(DateUtility
									.dateToStr(crtDate)) + "'");
					jsonSb.append("},");
				}
				if (jsonSb.length() > 0) {
					jsonSb.deleteCharAt(jsonSb.length() - 1);
				}
				jsonSb.append("]}");
			}

		}

		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(total + jsonSb.toString());
		return mapping.findForward(null);
	}
}
