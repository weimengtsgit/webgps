package com.sosgps.wzt.system.action;

import java.util.ArrayList;
import java.util.Date;
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
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TModuleGroup;
import com.sosgps.wzt.orm.TOptLog;

import com.sosgps.wzt.system.bean.ModuleGradeBean;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.ModuleNewForm;
import com.sosgps.wzt.system.service.ModuleManagerService;
import com.sosgps.wzt.tree.MapabcTree;

/**
 * Ȩ�޹��ܹ���
 * 
 * @author xiaojun.luan
 * 
 */
public class ModuleManagerAction extends DispatchWebActionSupport {
	private ModuleManagerService moduleManagerService = (ModuleManagerService) SpringHelper
			.getBean("moduleManagerService");

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MapabcTree sosgpsTree = moduleManagerService.getModuleMapabcTree();
		request.setAttribute("MapabcTree", sosgpsTree);
		return mapping.findForward("view");
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ModuleNewForm moduleForm = (ModuleNewForm) form;

		String groupId = moduleForm.getGroupId();
		String gradeId = moduleForm.getGradeId();// Ȩ�޵ȼ�Id
		TModule tModule = moduleForm.getModule();

		String createBy = "initName";
		String goPage = "success";
		TOptLog tOptLog = new TOptLog();
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}

		int sortedIndex = moduleManagerService
				.findNextSortedIndexByParentID(new Long(moduleForm.getGroupId()));
		tModule.setModuleName(moduleForm.getModuleName());
		tModule.setMoudleDesc(moduleForm.getModuleDesc());
		tModule.setModuleCode(moduleForm.getModuleCode());
		// tModule.setModuleType(moduleForm.getModuleType());
		tModule.setModulePath(moduleForm.getModulePath());
		tModule.setCreateDate(DateUtil.getDateTime());
		tModule.setCreateBy(createBy);// ��¼ user ��Ϣ
		tModule.setUsageFlag("1");
		tModule.setModuleType("1");
		tModule.setLeafFlag(new Long(0));
		tModule.setSortedIndex(new Long(sortedIndex));
		tModule.setModuleGrade(new Long(moduleForm.getModuleGrade()));
		tModule.setParentid(new Long(moduleForm.getGroupId()));
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
		try {
			moduleManagerService.saveModule(tModule);
			tOptLog.setOptDesc("���� '" + moduleForm.getModule().getModuleName()
					+ "' Ȩ�޳ɹ�");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		} catch (Exception ex) {
			ex.printStackTrace();
			tOptLog.setOptDesc("���� '" + moduleForm.getModule().getModuleName()
					+ "' Ȩ��ʧ��");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "���� '"
					+ moduleForm.getModule().getModuleName() + "' Ȩ��ʧ��");
			// goPage = "view";
		}
		return mapping.findForward(goPage);
	}

	/**
	 * ɾ������Ȩ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] sids = request.getParameterValues("ids");
		String goPage = "displayList";
		Long[] longIds = new Long[sids.length];
		for (int x = 0; x < sids.length; x++) {
			longIds[x] = Long.valueOf(sids[x]);
		}
		boolean ret = moduleManagerService.deleteAll(longIds);
		String createBy = "initName";
		TOptLog tOptLog = new TOptLog();
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
		// ɾ���ɹ�
		if (ret) {
			tOptLog.setOptDesc("ɾ��Ȩ�޳ɹ�");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			// ɾ��ʧ��
		} else {
			tOptLog.setOptDesc("ɾ��Ȩ��ʧ��");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "ɾ��Ȩ��ʧ��");
			goPage = "message";
		}
		return mapping.findForward(goPage);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleNewForm moduleForm = (ModuleNewForm) form;

		String groupId = moduleForm.getGroupId();
		TModule module = moduleManagerService.retrieveModule(new Long(groupId));
		this.moduleManagerService.deleteModule(module);
		return mapping.findForward("success");
	}

	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleNewForm moduleForm = (ModuleNewForm) form;

		Long id = new Long(moduleForm.getGroupId());

		TModule oldModule = moduleManagerService.retrieveModule(id);
		Long parentID = oldModule.getParentid();

		String gradeId = moduleForm.getGradeId();// Ȩ�޵ȼ�Id
		TModule tModule = moduleForm.getModule();

		String createBy = "initName";
		String goPage = "success";
		TOptLog tOptLog = new TOptLog();
		// ��ȡ��¼�û���Ϣ
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		tModule.setModuleName(moduleForm.getModuleName());
		tModule.setMoudleDesc(moduleForm.getModuleDesc());
		tModule.setModuleCode(moduleForm.getModuleCode());
		// tModule.setModuleType(moduleForm.getModuleType());
		tModule.setModulePath(moduleForm.getModulePath());
		tModule.setCreateDate(DateUtil.getDateTime());
		tModule.setCreateBy(createBy);// ��¼ user ��Ϣ
		tModule.setUsageFlag("1");
		tModule.setModuleType("1");
		tModule.setLeafFlag(new Long(0));
		tModule.setModuleGrade(new Long(moduleForm.getModuleGrade()));
		tModule.setParentid(parentID);
		tModule.setSortedIndex(oldModule.getSortedIndex());
		tModule.setId(id);
		// ��־��¼
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
		try {
			moduleManagerService.updateModule(tModule);
			tOptLog.setOptDesc("�޸� '" + moduleForm.getModule().getModuleName()
					+ "' Ȩ�޳ɹ�");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		} catch (Exception ex) {
			ex.printStackTrace();
			tOptLog.setOptDesc("�޸� '" + moduleForm.getModule().getModuleName()
					+ "' Ȩ��ʧ��");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "�޸� '"
					+ moduleForm.getModule().getModuleName() + "' Ȩ��ʧ��");
			// goPage = "view";
		}
		return mapping.findForward(goPage);
	}

	public ActionForward moveUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleNewForm moduleForm = (ModuleNewForm) form;
		
		Long id = new Long(moduleForm.getGroupId());
		
		moduleManagerService.moveUpSameParent(id);
		
		return mapping.findForward("success");

	}
	public ActionForward moveDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleNewForm moduleForm = (ModuleNewForm) form;
		
		Long id = new Long(moduleForm.getGroupId());
		
		moduleManagerService.moveDownSameParent(id);
		
		return mapping.findForward("success");

	}

}
