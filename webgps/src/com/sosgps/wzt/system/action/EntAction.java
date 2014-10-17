/**
 * 
 */
package com.sosgps.wzt.system.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.v21.email.service.EmailService;
import com.sosgps.v21.model.Email;
import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.target.service.TargetService;
import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.SmsAccounts;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.poi.service.LayerAndPoiService;
import com.sosgps.wzt.sms.service.SmsService;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.dao.RefModuleRoleDao;
import com.sosgps.wzt.system.form.EntForm;
import com.sosgps.wzt.system.service.RoleService;
import com.sosgps.wzt.system.service.TEntService;
import com.sosgps.wzt.system.service.UserService;
import com.sosgps.wzt.util.CharTools;
import com.sosgps.wzt.util.DateUtility;

/**
 * @author xiaojun.luan
 * 
 */
public class EntAction extends DispatchWebActionSupport {
	private Log log = org.apache.commons.logging.LogFactory
			.getLog(EntAction.class);

	private TEntService tEntService = (TEntService) SpringHelper
			.getBean("tEntService");
	private RoleService roleService = (RoleService) SpringHelper
			.getBean("roleService");
	private UserService userService = (UserService) SpringHelper
			.getBean("userService");
	private SmsService smsService = (SmsService) SpringHelper
			.getBean("SmsServiceImpl");
	private TargetService targetService = (TargetService) SpringHelper
			.getBean("targetService");
	private EmailService emailService = (EmailService) SpringHelper
			.getBean("EmailService");

	public ActionForward displayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("displayList企业");
		List list = tEntService.findAll();
		request.setAttribute("entList", list);
		return mapping.findForward("list");
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EntForm entForm = (EntForm) form;
		String entCode = entForm.getEntCode();

		log.info("updae企业'" + entCode + "'");

		TEnt tEnt = tEntService.findByEntCode(entCode);
		// tEnt.setEntCode(entForm.getEntCode());
		tEnt.setEntName(entForm.getEntName());
		tEnt.setAreaCode(entForm.getAreaCode());
		tEnt.setBusinessId(entForm.getBusinessId());
		tEnt.setEntCrtDate(new Date());
		tEnt.setEntStatus("1");
		tEnt.setEntType(Long.valueOf(entForm.getEntType()));
		tEnt.setFeeType(0l);
		tEntService.update(tEnt);
		return mapping.findForward("showList");
	}

	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("view企业");
		String entCode = request.getParameter("entCode");
		TEnt tEnt = tEntService.findByEntCode(entCode);

		request.setAttribute("tEnt", tEnt);
		return mapping.findForward("view");
	}

	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("view企业");
		EntForm entForm = (EntForm) form;
		entForm.setMethod("update");
		entForm.setButtonName("保存");

		String entCode = request.getParameter("entCode");
		TEnt tEnt = tEntService.findByEntCode(entCode);

		request.setAttribute("tEnt", tEnt);
		return mapping.findForward("init");
	}

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("创建企业");
		EntForm entForm = (EntForm) form;
		String entCode = entForm.getEntCode();
		String entName = entForm.getEntName();
		String businessId = entForm.getBusinessId();
		String areaCode = entForm.getAreaCode();
		Long entType = Long.valueOf(entForm.getEntType());
		String otherInfo = "";
		String adminUser = entForm.getAdminUser();
		String password = entForm.getPassword();

		Long maxUserNum = null;
		try {
			maxUserNum = Long.valueOf(entForm.getMaxUserNum());
		} catch (Exception e) {
			maxUserNum = 10l;
		}

		// BossUserRoleService bossUserRoleService = (BossUserRoleService)
		// SpringHelper
		// .getBean("bossUserRoleService");
		// String returnMsg=bossUserRoleService.createEntInfo(entCode, entName,
		// businessId, areaCode, entType, otherInfo, adminUser, password,
		// maxUserNum);
		// if(returnMsg.equals("0000")){//创建成功
		// return mapping.findForward("showList");
		// }else if(returnMsg.equals("2002")){
		// String errorInfo = "企业代码'" + entCode + "'已经存在！";
		// log.error("增加企业错误，" + errorInfo);
		// request.setAttribute("msg", errorInfo);
		// }else{
		// String errorInfo = returnMsg;
		// log.error("增加企业错误，" + errorInfo);
		// request.setAttribute("msg", errorInfo);
		// }
		entForm.setMethod("create");
		entForm.setButtonName("增加");
		return mapping.findForward("init");
	}

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("init企业");
		EntForm entForm = (EntForm) form;
		entForm.setMethod("create");
		entForm.setButtonName("增加");
		String goPage = "init";
		return mapping.findForward(goPage);
	}

	// sos视野控制
	public ActionForward setVeiw(ActionMapping mapping, ActionForm form,
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
		String mapZoom = request.getParameter("mapZoom");// 地图级别
		String centerX = request.getParameter("centerX");// x坐标
		String centerY = request.getParameter("centerY");// y坐标

		mapZoom = CharTools.javaScriptEscape(mapZoom);
		centerX = CharTools.javaScriptEscape(centerX);
		centerY = CharTools.javaScriptEscape(centerY);

		TEnt tEnt = userInfo.getEnt();
		// com.mapabc.geom.DPoint[] dPoint=null;
		com.sos.sosgps.api.DPoint[] dPoint = null;
		// com.sos.sosgps.api.CoordAPI coordAPI = new
		// com.sos.sosgps.api.CoordAPI();
		com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();
		Long mapZoomL = 3L;
		try {
			mapZoomL = Long.parseLong(mapZoom);
			double xs[] = { Double.parseDouble(centerX) };
			double ys[] = { Double.parseDouble(centerY) };
			// dPoint = coordAPI.encryptConvert(xs, ys);
			dPoint = coordApizw.encryptConvert(xs, ys);
		} catch (Exception e) {
			response.getWriter().write("{result:\"2\"}");// 失败
			return null;
		}
		tEnt.setMapZoom(mapZoomL);
		tEnt.setCenterX(dPoint[0].getEncryptX());
		tEnt.setCenterY(dPoint[0].getEncryptY());
		tEntService.update(tEnt);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	// sos新增企业
	public ActionForward addEnt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String empEntCode = userInfo.getEmpCode();
		// 从request中获取参数
		String entCode = request.getParameter("entCode");// 企业代码
		String entName = request.getParameter("entName");// 企业名称
		// String centerX = request.getParameter("centerX");// 默认视野x坐标
		// String centerY = request.getParameter("centerY");// 默认视野y坐标
		// String mapZoom = request.getParameter("mapZoom");// 默认视野zoom
		String maxUserNum = request.getParameter("maxUserNum");// 最大用户数
		String smsAccount = request.getParameter("smsAccount");// 短信帐号
		String smsPwd = request.getParameter("smsPwd");// 短信密码
		String controlPassword = request.getParameter("controlPassword");// 断油断电密码
		String userName = request.getParameter("userName");// 用户名
		String userAccount = request.getParameter("userAccount");// 登录帐号
		String password = request.getParameter("password");// 密码
		String endDate = request.getParameter("endTime");// 服务到期时间
		String smsType = request.getParameter("smsType");// 短信通道 add by
		// liuhongxiao
		// 2011-12-05
		String visitTjStatus = request.getParameter("visitTjStatus");// 拜访统计
		// add
		// by
		// liuhongxiao
		// 2011-12-23
		String reportStatus = request.getParameter("reportStatus");// 报表日志 add
		// by
		// liuhongxiao
		// 2011-12-23
		String templateType = request.getParameter("templateType");// 目标维护模板类型
		// add by
		// weimeng
		// 2012-8-2
		String email1 = request.getParameter("email1");// 邮箱1 add by weimeng
		// 2012-8-2
		String email2 = request.getParameter("email2");// 邮箱2 add by weimeng

		String persionGreyInterval = request.getParameter("persionGreyInterval");
		String carGreyInterval = request.getParameter("carGreyInterval");
		
		// 2012-8-2
		Date endTime = DateUtility.strToDate(endDate);
		entCode = CharTools.killNullString(entCode);
		entCode = java.net.URLDecoder.decode(entCode, "utf-8");
		entCode = CharTools.killNullString(entCode);

		entName = CharTools.killNullString(entName);
		entName = java.net.URLDecoder.decode(entName, "utf-8");
		entName = CharTools.killNullString(entName);

		// centerX = CharTools.javaScriptEscape(centerX);
		// centerY = CharTools.javaScriptEscape(centerY);
		// mapZoom = CharTools.javaScriptEscape(mapZoom);
		maxUserNum = CharTools.killNullString(maxUserNum);
		maxUserNum = java.net.URLDecoder.decode(maxUserNum, "utf-8");
		maxUserNum = CharTools.killNullString(maxUserNum);

		smsAccount = CharTools.killNullString(smsAccount);
		smsAccount = java.net.URLDecoder.decode(smsAccount, "utf-8");
		smsAccount = CharTools.killNullString(smsAccount);

		smsPwd = CharTools.killNullString(smsPwd);
		smsPwd = java.net.URLDecoder.decode(smsPwd, "utf-8");
		smsPwd = CharTools.killNullString(smsPwd);

		userName = CharTools.killNullString(userName);
		userName = java.net.URLDecoder.decode(userName, "utf-8");
		userName = CharTools.killNullString(userName);

		userAccount = CharTools.killNullString(userAccount);
		userAccount = java.net.URLDecoder.decode(userAccount, "utf-8");
		userAccount = CharTools.killNullString(userAccount);

		password = CharTools.killNullString(password);
		password = java.net.URLDecoder.decode(password, "utf-8");
		password = CharTools.killNullString(password);

		controlPassword = CharTools.killNullString(controlPassword);
		controlPassword = java.net.URLDecoder.decode(password, "utf-8");
		controlPassword = CharTools.killNullString(controlPassword);

		templateType = CharTools.killNullString(templateType);
		templateType = java.net.URLDecoder.decode(templateType, "utf-8");
		templateType = CharTools.killNullString(templateType);

		email1 = CharTools.killNullString(email1);
		email1 = java.net.URLDecoder.decode(email1, "utf-8");

		email2 = CharTools.killNullString(email2);
		email2 = java.net.URLDecoder.decode(email2, "utf-8");

		TEnt ent = tEntService.findByEntCode(entCode);
		if (ent != null) {
			response.getWriter().write("{result:\"3\"}");// 已存在企业代码
			log.info("新增企业-已存在企业代码!");
			return null;
		}
		// 查找模板角色
		String roleCode = "defaultEntAdminRole";
		TRole tempRole = roleService.findRoleAndModulesByRoleCode(empEntCode,
				roleCode);
		if (tempRole == null) {
			response.getWriter().write("{result:\"4\"}");// 模板角色不存在
			log.info("新增企业-模板角色不存在!");
			return null;
		}

		// 创建企业
		TEnt tEnt = new TEnt();
		tEnt.setEntCode(entCode);
		//add by wangzhen 算出企业的hashCode ，添加进去 start
		long index = entCode.hashCode() % 100;
		index = index > 0 ? index : -index;
		tEnt.setHashCode(index);
		//add by wangzhen 算出企业的hashCode ，添加进去 end
		tEnt.setEntName(entName);
		tEnt.setEntStatus("1");// 使用状态 0:不可用 1:可用
		Date now = Calendar.getInstance().getTime();
		tEnt.setOpenTime(now);
		tEnt.setEntCrtDate(now);
		// if(centerX.equals(""))
		String centerX = "NMNFNTORGRUDKMN";// 默认
		// if(centerY.equals(""))
		String centerY = "PSHLKSQTGMNHOMN";// 默认
		tEnt.setCenterX(centerX);
		tEnt.setCenterY(centerY);
		String mapZoom = "6";
		tEnt.setMapZoom(CharTools.str2Long(mapZoom, 6L));// 默认
		tEnt.setFeeType(1L);// 默认
		tEnt.setEntType(1L);// 企业用户
		// if(!maxUserNum.equals(""))
		tEnt.setMaxUserNum(CharTools.str2Long(maxUserNum, null));// 最大用户数
		tEnt.setSmsAccount(smsAccount);
		tEnt.setSmsPwd(smsPwd);
		tEnt.setEndTime(endTime);
		tEnt.setSmsType(CharTools.str2Long(smsType, 1L));
		tEnt.setVisitTjStatus(CharTools.str2Long(visitTjStatus, 0L));
		tEnt.setReportStatus(CharTools.str2Long(reportStatus, 0L));
		tEnt.setPersionGreyInterval(CharTools.str2Long(persionGreyInterval, 1440L)*60);
		tEnt.setCarGreyInterval(CharTools.str2Long(carGreyInterval, 15L)*60);
		tEnt.setVersion("2.0");
		tEntService.save(tEnt);
		log.info("新增企业-创建企业成功!");

		// 创建企业管理员角色
		TRole role = new TRole();
		role.setCreateBy(userInfo.getUserAccount());
		role.setCreateDate(DateUtil.getDateTime());
		role.setEmpCode(entCode);
		role.setRoleName("企业管理员");
		role.setRoleCode("admin_" + entCode);
		role.setRoleDesc("企业管理员");
		role.setUsageFlag("1");
		roleService.saveRole(role);
		log.info("新增企业-创建企业管理员角色成功!");
		// 创建角色与权限关系
		Set<RefModuleRole> refs = tempRole.getRefModuleRoles();
		if (refs != null) {
			RefModuleRoleDao refModuleRoleDao = (RefModuleRoleDao) SpringHelper
					.getBean("refModuleRoleDao");
			for (RefModuleRole refModuleRole : refs) {
				TModule module = refModuleRole.getTModule();
				RefModuleRole transientInstance = new RefModuleRole();
				transientInstance.setTRole(role);
				transientInstance.setTModule(module);
				refModuleRoleDao.save(transientInstance);
			}
			log.info("新增企业-创建角色与权限关系成功!");
		}

		// 创建用户
		TUser user = new TUser();
		user.setUserAccount(userAccount);
		user.setUserPassword(password);
		user.setUserName(userName);
		user.setUsageFlag("1");
		user.setCreateBy(userInfo.getUserAccount());
		user.setCreateDate(DateUtil.getDateTime());
		user.setRole(role);
		user.setEmpCode(entCode);// 所属公司企业代码
		user.setControlPassword(controlPassword);// 断油断电密码
		userService.saveUser(user, role);
		log.info("新增企业-创建用户成功!");

		// 创建默认图层
		LayerAndPoiService layerAndPoiService = (LayerAndPoiService) SpringHelper
				.getBean("LayerAndPoiServiceImpl");
		Long userId = user.getId();
		String layerName = "默认图层";
		String visible = "1";
		String mapLevel = "16";
		String[] userIds = {};
		layerAndPoiService.addLayer(entCode, layerName, "默认图层", userId,
				visible, mapLevel, userIds);
		log.info("新增企业-创建默认图层成功!");

		// 判断是否为国都短信通道，如果为国都短信通道，再判断有没有短信账号，如果没有，添加短信账号
		// add by liuhongxiao 2011-12-23
		if ("3".equals(smsType)) {
			List<SmsAccounts> smsAccountsList = smsService
					.findByEntCode(entCode);
			if (smsAccountsList == null || smsAccountsList.size() < 1) {
				SmsAccounts smsAccounts = new SmsAccounts();
				smsAccounts.setEntCode(entCode);
				smsAccounts.setSmsAvailable(0);
				smsAccounts.setSms_sent_count(0);
				smsAccounts.setSms_total(0);
				smsAccounts.setCreateDate(new Date());
				smsService.save(smsAccounts);
				log.info("新增企业-创建国都短信通道成功!");
			}
		}
		// 创建目标模板
		Kpi kpi_ = new Kpi();
		kpi_.setEntCode(entCode);
		kpi_.setType(0);
		kpi_.setValue(templateType);
		kpi_.setKey(0 + "_" + templateType);
		kpi_.setStates(0);
		long currentTime = new Date().getTime();
		kpi_.setCreateOn(currentTime);
		kpi_.setCreateBy(userInfo.getUser().getUserName());
		List<Kpi> kpiList_ = new ArrayList<Kpi>();
		kpiList_.add(kpi_);
		targetService.addKpi(kpiList_, userInfo.getUser());
		log.info("新增企业-创建企业目标模板成功!");

		// 设置邮箱
		List<Email> emailList = new ArrayList<Email>();
		int templateTypeI = Integer.valueOf(templateType);
		this.addEmail(emailList, email1, entCode, templateTypeI, user.getId(),
				userInfo.getUser().getUserName(), currentTime);
		this.addEmail(emailList, email2, entCode, templateTypeI, user.getId(),
				userInfo.getUser().getUserName(), currentTime);

		emailService.saveEmails(emailList);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_ENT);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("新增企业成功！");
		LogFactory.newLogInstance("optLogger").info(tOptLog);
		log.info("新增企业成功!");

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	// sos企业列表
	public ActionForward listEnt(ActionMapping mapping, ActionForm form,
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
		int pageSize = Integer.parseInt(limit);
		int pageNo = startint / pageSize + 1;
		// System.out.println(start+";"+limit+";page = "+page);
		StringBuffer jsonSb = new StringBuffer();
		int total = 0;
		if (start != null && start.length() > 0 && limit != null
				&& limit.length() > 0) {
			Page<TEnt> list = tEntService.listEnt3(pageNo, pageSize,
					searchValue);
			if (list != null && list.getResult() != null
					&& list.getResult().size() > 0) {
				total = list.getTotalCount();
				for (Iterator iterator = list.getResult().iterator(); iterator
						.hasNext();) {
					TEnt ent = (TEnt) iterator.next();
					// 过滤初始企业
					if (ent.getEntCode().equals("empRoot"))
						continue;

					List<Kpi> kpis = targetService.getKpi(CharTools
							.javaScriptEscape(ent.getEntCode()), 0);
					String templateType = "";
					if (kpis != null && kpis.size() >= 1) {
						Kpi kpi_ = kpis.get(0);
						templateType = kpi_.getValue();
					}
					jsonSb.append("{");
					jsonSb.append("entCode:'"
							+ CharTools.javaScriptEscape(ent.getEntCode())
							+ "',");// entCode
					jsonSb.append("entName:'"
							+ CharTools.javaScriptEscape(ent.getEntName())
							+ "',");// 企业名称
					jsonSb.append("maxUserNum:'"
							+ CharTools.killNullLong2String(
									ent.getMaxUserNum(), "") + "',");// 最大用户数
					jsonSb.append("smsAccount:'"
							+ CharTools.javaScriptEscape(ent.getSmsAccount())
							+ "',");// 短信帐号
					jsonSb.append("smsPwd:'"
							+ CharTools.javaScriptEscape(ent.getSmsPwd())
							+ "',");// 短信密码
					jsonSb.append("endTime:'"
							+ DateUtility.dateToStr(ent.getEndTime()) + "',");// 服务到期时间
					jsonSb.append("entStatus:'"
							+ CharTools.javaScriptEscape(ent.getEntStatus())
							+ "',");// 企业状态
					jsonSb.append("smsType:'"
							+ CharTools.javaScriptEscape(String.valueOf(ent
									.getSmsType())) + "',");// 短信通道 add by
					// liuhongxiao
					// 2011-12-05
					jsonSb.append("visitTjStatus:'"
							+ CharTools.javaScriptEscape(String.valueOf(ent
									.getVisitTjStatus())) + "',");// 拜访统计 add
					// by
					// liuhongxiao
					// 2011-12-23
					jsonSb.append("reportStatus:'"
							+ CharTools.javaScriptEscape(String.valueOf(ent
									.getReportStatus())) + "',");// 报表日志 add
					
					jsonSb.append("persionGreyInterval:'"
							+ CharTools.javaScriptEscape(String.valueOf(ent.getPersionGreyInterval()/60)) + "',");
					jsonSb.append("carGreyInterval:'"
							+ CharTools.javaScriptEscape(String.valueOf(ent.getCarGreyInterval()/60)) + "',");
					// by
					// liuhongxiao
					// 2011-12-23

					List<Email> emailList = emailService.getEmails(CharTools
							.javaScriptEscape(ent.getEntCode()));
					int i = 1;
					for (Email email : emailList) {
						if (email.getSendFrequency() == 0) {
							continue;
						}
						jsonSb.append("email" + i + ":'" + email.getEmail()
								+ "," + email.getContactName() + ","
								+ email.getPosition() + "',");
						i++;
					}

					for (; i < 3; i++) {
						jsonSb.append("email" + i + ":'',");
					}
					jsonSb.append("templateType:'"
							+ CharTools.javaScriptEscape(templateType) + "'");// 目标模板值
					// add
					// by
					// weimeng
					// 2012-8-7
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

	// sos删除企业
	public ActionForward deleteEnts(ActionMapping mapping, ActionForm form,
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
		String entCodesStr = request.getParameter("entCodes");// 企业代码，多个","隔开
		entCodesStr = java.net.URLDecoder.decode(entCodesStr, "utf-8");
		entCodesStr = CharTools.killNullString(entCodesStr);

		String[] entCodes = CharTools.split(entCodesStr, ",");
		tEntService.deleteEnts(entCodes);

		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_ENT);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("删除企业成功！");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	// sos修改企业
	public ActionForward updateEnt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"9\"}");// 未登录
			return mapping.findForward(null);
		}
		String empEntCode = userInfo.getEmpCode();
		// 从request中获取参数
		String entCode = request.getParameter("entCode");// 企业代码
		String entName = request.getParameter("entName");// 企业名称
		String maxUserNum = request.getParameter("maxUserNum");// 最大用户数
		String smsAccount = request.getParameter("smsAccount");// 短信帐号
		String smsPwd = request.getParameter("smsPwd");// 短信密码
		String endDate = request.getParameter("endTime");// 服务到期时间
		String entStatus = request.getParameter("entStatus");// 状态
		String smsType = request.getParameter("smsType");// 短信通道 add by
		// liuhongxiao
		// 2011-12-05
		String visitTjStatus = request.getParameter("visitTjStatus");// 拜访统计
		// add
		// by
		// liuhongxiao
		// 2011-12-23
		String reportStatus = request.getParameter("reportStatus");// 报表日志 add
		// by
		// liuhongxiao
		// 2011-12-23
		String templateType = request.getParameter("templateType");// 目标维护模板类型
		// add by
		// weimeng
		// 2012-8-2
		String email1 = request.getParameter("email1");// 邮箱1 add by weimeng
		String email2 = request.getParameter("email2");// 邮箱2 add by weimeng
		String persionGreyInterval = request.getParameter("persionGreyInterval");
		String carGreyInterval = request.getParameter("carGreyInterval");
		
		Date endTime = DateUtility.strToDate(endDate);

		entCode = CharTools.killNullString(entCode);
		entCode = java.net.URLDecoder.decode(entCode, "utf-8");
		entCode = CharTools.killNullString(entCode);

		entName = CharTools.killNullString(entName);
		entName = java.net.URLDecoder.decode(entName, "utf-8");
		entName = CharTools.killNullString(entName);

		maxUserNum = CharTools.killNullString(maxUserNum);
		maxUserNum = java.net.URLDecoder.decode(maxUserNum, "utf-8");
		maxUserNum = CharTools.killNullString(maxUserNum);

		smsAccount = CharTools.killNullString(smsAccount);
		smsAccount = java.net.URLDecoder.decode(smsAccount, "utf-8");
		smsAccount = CharTools.killNullString(smsAccount);

		smsPwd = CharTools.killNullString(smsPwd);
		smsPwd = java.net.URLDecoder.decode(smsPwd, "utf-8");
		smsPwd = CharTools.killNullString(smsPwd);

		templateType = CharTools.killNullString(templateType);
		templateType = java.net.URLDecoder.decode(templateType, "utf-8");
		templateType = CharTools.killNullString(templateType);

		email1 = CharTools.killNullString(email1);
		email1 = java.net.URLDecoder.decode(email1, "utf-8");

		email2 = CharTools.killNullString(email2);
		email2 = java.net.URLDecoder.decode(email2, "utf-8");

		TEnt ent = tEntService.findByEntCode(entCode);
		if (ent == null) {
			response.getWriter().write("{result:\"3\"}");// 不存在企业代码
			return null;
		}

		ent.setEntName(entName);
		// if(!maxUserNum.equals(""))
		ent.setMaxUserNum(CharTools.str2Long(maxUserNum, null));// 最大用户数
		ent.setSmsAccount(smsAccount);
		ent.setSmsPwd(smsPwd);
		Date now = new Date();
		ent.setEntStatus(entStatus);
		if (endTime != null && endTime.before(now)) {
			ent.setEntStatus("2");
		}/*
			 * else if(endTime!=null&&endTime.after(now)){
			 * ent.setEntStatus("1"); }
			 */
		ent.setEndTime(endTime);
		ent.setSmsType(CharTools.str2Long(smsType, 1L));
		ent.setVisitTjStatus(CharTools.str2Long(visitTjStatus, 0L));
		ent.setReportStatus(CharTools.str2Long(reportStatus, 0L));
		ent.setPersionGreyInterval(CharTools.str2Long(persionGreyInterval, 1440L)*60);
		ent.setCarGreyInterval(CharTools.str2Long(carGreyInterval, 15L)*60);
		ent.setVersion("2.0");
		tEntService.update(ent);

		// 判断是否为国都短信通道，如果为国都短信通道，再判断有没有短信账号，如果没有，添加短信账号
		// add by liuhongxiao 2011-12-23
		if ("3".equals(smsType)) {
			List<SmsAccounts> smsAccountsList = smsService
					.findByEntCode(entCode);
			if (smsAccountsList == null || smsAccountsList.size() < 1) {
				SmsAccounts smsAccounts = new SmsAccounts();
				smsAccounts.setEntCode(entCode);
				smsAccounts.setSmsAvailable(0);
				smsAccounts.setSms_sent_count(0);
				smsAccounts.setSms_total(0);
				smsAccounts.setCreateDate(new Date());
				smsService.save(smsAccounts);
			}
		}
		if (templateType != null && templateType.length() > 0) {
			List<Kpi> kpiList_ = targetService.getKpi(entCode, 0);
			if (kpiList_ == null || kpiList_.size() < 1) {
				// 创建目标模板
				Kpi kpi_ = new Kpi();
				kpi_.setEntCode(entCode);
				kpi_.setType(0);
				kpi_.setValue(templateType);
				kpi_.setKey(0 + "_" + templateType);
				kpi_.setStates(0);
				long currentTime = new Date().getTime();
				kpi_.setCreateOn(currentTime);
				kpi_.setCreateBy(userInfo.getUser().getUserName());
				kpiList_ = new ArrayList<Kpi>();
				kpiList_.add(kpi_);
				targetService.addKpi(kpiList_, userInfo.getUser());
				log.info("新增企业-创建企业目标模板成功!");
			} else {
				Kpi kpi_ = kpiList_.get(0);
				kpi_.setValue(templateType);
				kpi_.setKey(0 + "_" + templateType);
				// kpi_.setStates(0);
				targetService.updateKpi(kpiList_, userInfo.getUser());
				userInfo.setTargetTemplateType(templateType);
				log.info("修改企业-修改企业目标模板成功!");
			}
		}

		// 设置邮箱
		Page<TUser> userPage = userService.listUserAdmin(entCode, 0, 1, "");
		List<TUser> userList = userPage.getResult();
		if (userList != null && userList.size() > 0) {
			Long userId = userList.get(0).getId();
			long currentTime = new Date().getTime();
			List<Email> emailList = new ArrayList<Email>();
			int templateTypeI = Integer.valueOf(templateType);
			this.addEmail(emailList, email1, entCode, templateTypeI, userId,
					userInfo.getUser().getUserName(), currentTime);
			this.addEmail(emailList, email2, entCode, templateTypeI, userId,
					userInfo.getUser().getUserName(), currentTime);
			emailService.removeEmails(entCode);
			emailService.saveEmails(emailList);
		}
		// 日志实例
		TOptLog tOptLog = new TOptLog();
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.LOG_SOURCE_MANAGE_ENT);
		tOptLog.setResult(1L);
		tOptLog.setOptDesc("修改企业成功！");
		LogFactory.newLogInstance("optLogger").info(tOptLog);

		response.getWriter().write("{result:\"1\"}");// 成功
		return null;
	}

	public SmsService getSmsService() {
		return smsService;
	}

	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	/**
	 * 设置邮件发送策略
	 * 
	 * @param emailList
	 * @param emailStr
	 * @param entCode
	 * @param templateType
	 * @param userId
	 * @param userName
	 * @param timestamp
	 */
	private void addEmail(List<Email> emailList, String emailStr,
			String entCode, int templateType, Long userId, String userName,
			long timestamp) {
		if (emailStr != null && emailStr.length() > 0) {
			String[] emailinfos = emailStr.split(",");
			Email email = new Email();
			email.setEntCode(entCode);
			email.setEmail(emailinfos[0]);
			email.setContactName(emailinfos[1]);
			email.setPosition(emailinfos[2]);
			email.setSendTime(0);
			email.setSendFrequency(templateType + 1);
			if (templateType == 0) {// 周模板
				email.setSendWeekday(1);
			} else if (templateType == 1) {// 旬模板
				email.setSendXun(0);
			} else {
				email.setSendDay(1);
			}
			email.setUserId(userId);
			email.setCreateon(timestamp);
			email.setCreateby(userName);
			email.setDeleteFlag(0L);
			emailList.add(email);

			// 按天发送的邮件策略
			email = new Email();
			email.setEntCode(entCode);
			email.setEmail(emailinfos[0]);
			email.setContactName(emailinfos[1]);
			email.setPosition(emailinfos[2]);
			email.setSendTime(0);
			email.setSendFrequency(0);
			email.setUserId(userId);
			email.setCreateon(timestamp);
			email.setCreateby(userName);
			email.setDeleteFlag(0L);
			emailList.add(email);
		}
	}
	

    public ActionForward queryMmsAccount(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UserInfo user = this.getCurrentUser(request);
        response.setContentType("text/json; charset=utf-8");
        if (user == null) {
            response.getWriter().write("{result:\"9\"}");// 未登录
            return mapping.findForward(null);
        }
        String entCode = user.getEmpCode();
        TEnt tEnt = tEntService.findByEntCode(entCode);
        StringBuffer json = new StringBuffer();
        if(tEnt != null && tEnt.getMmsAccount() != null && tEnt.getMmsPwd() != null){
            json.append("{result: \"0\", mmsAccount: \""+tEnt.getMmsAccount()+"\", mmsPwd: \""+tEnt.getMmsPwd()+"\"}");
        }else{
            json.append("{result: \"1\"}");
        }
        response.getWriter().write(json.toString());
        return mapping.findForward(null);
    }
}
