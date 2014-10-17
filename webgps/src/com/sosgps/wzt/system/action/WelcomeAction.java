package com.sosgps.wzt.system.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.manage.terminalgroup.service.TerminalGroupManageService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.WelcomeForm;
import com.sosgps.wzt.system.service.ModuleManagerService;
import com.sosgps.wzt.system.util.CheckRightUtil;
import com.sosgps.wzt.util.CharTools;

public class WelcomeAction extends DispatchWebActionSupport {

	public static Logger log = Logger.getLogger(WelcomeAction.class);

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward welcome(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		List list = terminalGroupManageService.viewUserTermGroup(userInfo
				.getUserId(), userInfo.getEmpCode());
		request.setAttribute("userViewTermGroup", list);
		ModuleManagerService moduleManagerService = (ModuleManagerService) SpringHelper
				.getBean("moduleManagerService");
		TRole tRole = userInfo.getUser().getRole();
		Long roleID = tRole.getId();
		Page<TModule> tModule = moduleManagerService
				.queryRoleModulesById(roleID);
		String edition = (String) request.getSession().getAttribute("edition");
		edition = CharTools.killNullString(edition, "");
		String path = request.getContextPath();
		ArrayList<String> panelal = new ArrayList<String>();
		ArrayList<String> actionsal = new ArrayList<String>();
		StringBuffer htmlSb = new StringBuffer();
		Hashtable<String, String> mode3_map = new Hashtable<String, String>();
		Hashtable<String, String> mode4_loc = new Hashtable<String, String>();
		Hashtable<String, String> mode5_report = new Hashtable<String, String>();
		Hashtable<String, String> flatMap = new Hashtable<String, String>();

		List<TModule> tModuleList = tModule.getResult();
		Iterator<TModule> i = tModuleList.iterator();
		while (i.hasNext()) {
			TModule module = (TModule) i.next();
			String tmpParentid = module.getParentid() + "";
			// 根权限
			if (tmpParentid.equals("-1") && (module.getModuleGrade() == 2)) {
				// System.out.println("根权限:"+module.getModuleName()+";id:"+module.getId());
				String ModuleCode = module.getModuleCode();
				String ModuleName = module.getModuleName();
				if (edition.equals("en")) {
					ModuleName = module.getModuleNameEn();
				}
				String panel = "new Ext.Panel({frame:true,title: '"
						+ ModuleName + "',collapsible:true,contentEl:'"
						+ ModuleCode + "',titleCollapse: true});";
				panelal.add(panel);
				htmlSb.append("<ul id=\"" + ModuleCode
						+ "\" class=\"x-hidden\">");
				String tmp = iteratorPageModules(tModuleList, module.getId()
						+ "", "-1", ModuleCode, actionsal, path, edition);
				htmlSb.append(tmp);
				htmlSb.append("</ul>");

			} else if (module.getModuleGrade() == 3) {
				mode3_map.put(module.getModuleCode(), module.getModuleName());
			} else if (module.getModuleGrade() == 4) {
				mode4_loc.put(module.getModuleCode(), module.getModuleName());
			} else if (module.getModuleGrade() == 5) {
				mode5_report
						.put(module.getModuleCode(), module.getModuleName());
			} else if (module.getModuleGrade() == 7L) {
				flatMap.put(module.getModuleCode(), module.getModuleName());
			}
		}

		request.getSession().setAttribute("module_panelal", panelal);
		request.getSession().setAttribute("module_actionsal", actionsal);
		request.getSession().setAttribute("module_htmlSb", htmlSb.toString());
		request.getSession().setAttribute("mode3_map", mode3_map);
		request.getSession().setAttribute("mode4_loc", mode4_loc);
		request.getSession().setAttribute("mode5_report", mode5_report);
		request.getSession().setAttribute("flat_map", flatMap);

		return mapping.findForward("success");
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public String iteratorPageModules(List<TModule> tModuleList,
			String parentid, String parentparentid, String parentModuleCode,
			ArrayList actionsal, String path, String edition) {
		Iterator i = tModuleList.iterator();
		StringBuffer tmphtmlSb = new StringBuffer();
		while (i.hasNext()) {
			TModule module = (TModule) i.next();
			if (module.getModuleGrade() == 2) {
				String tmpParentId = module.getParentid() + "";
				// System.out.println(tmpParentId+";"+parentid+":"+tmpParentId.equals(parentid));
				if (tmpParentId.equals(parentid)) {
					String ModuleCode = module.getModuleCode();
					String ModuleName = module.getModuleName();
					if (edition.equals("en")) {
						ModuleName = module.getModuleNameEn();
					}
					// String ModuleDesc = module.getMoudleDesc();
					String ModuleURL = module.getModulePath();
					// System.out.println("parentparentid:"+parentparentid);
					String tmp = iteratorPageModules(tModuleList, module
							.getId()
							+ "", module.getParentid() + "", ModuleCode,
							actionsal, path, edition);

					if (parentparentid.equals("-1")) {

						if (tmp != null && tmp.length() > 0) {
							String action = "actions." + ModuleCode
									+ "_a = function(){clickFolder('"
									+ ModuleCode + "_img','@" + ModuleCode
									+ "@');}";
							// System.out.println(action);
							actionsal.add(action);
							tmphtmlSb.append("<li>");
							tmphtmlSb
									.append("<img id=\""
											+ ModuleCode
											+ "_img\" src=\""
											+ path
											+ "/images/s.gif\" class=\"icon-folder\"/>");
							tmphtmlSb.append("<a id=\"" + ModuleCode
									+ "_a\" href=\"#\">" + ModuleName + "</a>");
							tmphtmlSb.append("</li>");
						} else {
							String action = "actions."
									+ ModuleCode
									+ "_a = function(){createTab('actions_tab','"
									+ ModuleName + "','" + path + ModuleURL
									+ "','ifr_" + ModuleCode
									+ "',center1);setNavigation('" + ModuleName
									+ "');}";
							// System.out.println("2:"+action);
							actionsal.add(action);
							tmphtmlSb.append("<li id=\"@" + parentModuleCode
									+ "@" + ModuleCode
									+ "\" style=\"display:none;\">");
							tmphtmlSb
									.append("<img src=\""
											+ path
											+ "/images/s.gif\" class=\"icon-elbow\"/><img src=\""
											+ path
											+ "/images/s.gif\" class=\"icon-leaf\"/>");
							tmphtmlSb.append("<a id=\"" + ModuleCode
									+ "_a\" href=\"#\">" + ModuleName + "</a>");
							tmphtmlSb.append("</li>");
						}
					} else {
						String action = "actions." + ModuleCode
								+ "_a = function(){createTab('actions_tab','"
								+ ModuleName + "','" + path + ModuleURL
								+ "','ifr_" + ModuleCode
								+ "',center1);setNavigation('" + ModuleName
								+ "');}";
						// System.out.println("2:"+action);
						actionsal.add(action);
						tmphtmlSb.append("<li id=\"@" + parentModuleCode + "@"
								+ ModuleCode + "\" style=\"display:none;\">");
						tmphtmlSb
								.append("<img src=\""
										+ path
										+ "/images/s.gif\" class=\"icon-elbow\"/><img src=\""
										+ path
										+ "/images/s.gif\" class=\"icon-leaf\"/>");
						tmphtmlSb.append("<a id=\"" + ModuleCode
								+ "_a\" href=\"#\">" + ModuleName + "</a>");
						tmphtmlSb.append("</li>");
					}
					// System.out.println("tmphtml:"+tmphtml);
					tmphtmlSb.append(tmp);
				}
			}

		}
		return tmphtmlSb.toString();
	}

	@SuppressWarnings("rawtypes")
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserInfo userInfo = this.getCurrentUser(request);
		CheckRightUtil checkRightUtil = new CheckRightUtil();
		List topGroupList = checkRightUtil.queryTopModuleGroup(userInfo
				.getUserId());
		WelcomeForm welcomeForm = (WelcomeForm) form;
		welcomeForm.setTopGroupList(topGroupList);
		session.setAttribute("topGroupList", topGroupList);
		String parentId = welcomeForm.getTopGroupId();
		if (parentId == null) {
			parentId = "-1";
		}
		List subGroupList = null;

		session.setAttribute("topGroupId_", parentId);

		subGroupList = checkRightUtil.queryChildModuleGroup(Long
				.valueOf(parentId), userInfo.getUserId());
		session.setAttribute("subGroupList", subGroupList);
		return mapping.findForward("init");
	}

}
