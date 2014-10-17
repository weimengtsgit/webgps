package com.sosgps.wzt.system.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.commons.util.DateUtil;

import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.orm.TModuleGroup;
import com.sosgps.wzt.orm.TOptLog;

import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.ModuleGroupForm;
import com.sosgps.wzt.system.service.ModuleGroupService;
import com.sosgps.wzt.system.service.impl.ModuleGroupServiceImpl;

/**
 * @Title:权限组管理
 * @Description:权限组管理，涉及增、删、改、查功能操作
 * @Copyright: Copyright (c) 2007
 * @Company: Mapabc
 * @author: zhangwei
 * @version 1.0
 * @date: 2008-8-20 下午02:41:11
 */
public class ModuleGroupAction extends DispatchWebActionSupport {
	private ModuleGroupService moduleGroupService = (ModuleGroupService) SpringHelper
			.getBean("moduleGroupService");;
	TOptLog tOptLog = new TOptLog();

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleGroupForm groupForm = (ModuleGroupForm) form;
		List parentList = moduleGroupService.queryModuleGroupByLevel(Long
				.valueOf("0"));
		groupForm.setParentList(parentList);
		return mapping.findForward("init");
	}

	/**
	 * 创建权限组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleGroupForm groupForm = (ModuleGroupForm) form;
		TModuleGroup tModuleGroup = groupForm.getModuleGroup();
		TModuleGroup moduleGroup = new TModuleGroup();
		String goPage = "createSuccess";
		moduleGroup.setGroupName(tModuleGroup.getGroupName());
		moduleGroup.setGroupDesc(tModuleGroup.getGroupDesc());
		moduleGroup.setParentId(tModuleGroup.getParentId());
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		moduleGroup.setCreateBy(createBy);// 登录 user 信息
		// moduleGroup.setCreateBy("aa");
		moduleGroup.setCreateDate(DateUtil.getDateTime());
		moduleGroup.setGroupLevel(new Long(1));
		moduleGroup.setUsageFlag("1");

		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULEGROUP_CODE);
		try {
			moduleGroupService.saveModuleGroup(moduleGroup);
			tOptLog
					.setOptDesc("创建 '" + tModuleGroup.getGroupName()
							+ "' 权限组成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		} catch (Exception ex) {
			tOptLog
					.setOptDesc("创建 '" + tModuleGroup.getGroupName()
							+ "' 权限组失败");
			tOptLog.setResult(new Long(0));
			request.setAttribute("msg", "创建 '" + tModuleGroup.getGroupName()
					+ "' 权限组失败");
			goPage = "message";
		}

		return mapping.findForward(goPage);
	}

	/**
	 * 修改权限组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleGroupForm groupForm = (ModuleGroupForm) form;
		String id = groupForm.getId();
		TModuleGroup moduleGroup = moduleGroupService.retrieveModuleGroup(Long
				.valueOf(id));

		List parentList = moduleGroupService.queryModuleGroupByLevel(Long
				.valueOf("0"));
		groupForm.setModuleGroup(moduleGroup);
		groupForm.setParentList(parentList);
		return mapping.findForward("modify");
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleGroupForm groupForm = (ModuleGroupForm) form;
		TModuleGroup group = groupForm.getModuleGroup();
		String goPage = "createSuccess";
		UserInfo userInfo = this.getCurrentUser(request);
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULEGROUP_CODE);

		try {
			tOptLog.setOptDesc("更新 '"
					+ groupForm.getModuleGroup().getGroupName() + "' 权限组成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			moduleGroupService.updateModuleGroup(group);
		} catch (Exception ex) {
			tOptLog.setOptDesc("更新 '"
					+ groupForm.getModuleGroup().getGroupName() + "' 权限组失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			goPage = "message";
			request.setAttribute("msg", "更新 '"
					+ groupForm.getModuleGroup().getGroupName() + "' 权限组失败");
		}

		return mapping.findForward(goPage);
	}

	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] sids = request.getParameterValues("ids");
		Long[] longIds = new Long[sids.length];
		String goPage = "displayList";
		for (int x = 0; x < sids.length; x++) {
			longIds[x] = Long.valueOf(sids[x]);
		}
		boolean ret = moduleGroupService.deleteAll(longIds);
		// 获取登录用户信息
		String createBy = "initName";
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULEGROUP_CODE);

		// 删除成功
		if (ret) {
			tOptLog.setOptDesc("删除权限组成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);

			// 删除失败
		} else {
			tOptLog.setOptDesc("删除权限组失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "删除权限组失败");
			goPage = "message";
		}
		return mapping.findForward(goPage);
	}

	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleGroupForm groupForm = (ModuleGroupForm) form;
		String id = groupForm.getId();
		TModuleGroup moduleGroup = moduleGroupService.retrieveModuleGroup(Long
				.valueOf(id));
		TModuleGroup parentModuleGroup = moduleGroupService
				.retrieveModuleGroup(moduleGroup.getParentId());
		groupForm.setParentModuleGroup(parentModuleGroup);
		groupForm.setModuleGroup(moduleGroup);
		return mapping.findForward("view");
	}

	public ActionForward displayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("displayList");
	}

}
