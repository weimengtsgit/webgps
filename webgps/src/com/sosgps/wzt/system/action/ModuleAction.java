package com.sosgps.wzt.system.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

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
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TModuleGroup;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TRole;

import com.sosgps.wzt.system.bean.ModuleGradeBean;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.ModuleForm;
import com.sosgps.wzt.system.service.ModuleGroupService;
import com.sosgps.wzt.system.service.ModuleManagerService;
import com.sosgps.wzt.system.service.ModuleService;
import com.sosgps.wzt.system.service.impl.ModuleGroupServiceImpl;
import com.sosgps.wzt.tree.TreeNode;
import com.sosgps.wzt.util.CharTools;

/**
 * 权限功能管理
 * 
 * @author zhangwei
 * 
 */
public class ModuleAction extends DispatchWebActionSupport {
	private ModuleGroupService moduleGroupService = (ModuleGroupService) SpringHelper
			.getBean("moduleGroupService");
	private ModuleService moduleService = (ModuleService) SpringHelper
			.getBean("moduleService");;
	//TOptLog tOptLog = new TOptLog();

	/**
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
		ModuleForm moduleForm = (ModuleForm) form;
		// moduleGroupService = (ModuleGroupService)
		// SpringHelper.getBean("moduleGroupService");
		List parentList = moduleGroupService.queryModuleGroupByLevel(Long
				.valueOf("1"));
		
		List moduleGradeList = new ArrayList();
		ModuleGradeBean bean = new ModuleGradeBean();
		bean.setGid(1);
		bean.setGradeName("系统权限");
		moduleGradeList.add(bean);
		bean = new ModuleGradeBean();
		bean.setGid(2);
		bean.setGradeName("用户权限");
		moduleGradeList.add(bean);
		
		moduleForm.setModuleGradeList(moduleGradeList);
		moduleForm.setGroupList(parentList);
		return mapping.findForward("init");
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
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ModuleForm module = (ModuleForm) form;
		//String groupId = module.getGroupId();
		//String gradeId = module.getGradeId();//权限等级Id
		String modulename = request.getParameter("modulename");
		String modulecode = request.getParameter("modulecode");
		String modulepath = request.getParameter("modulepath");
		String moduledesc = request.getParameter("moduledesc");
		String parentid = request.getParameter("parentid");
		String gradeid = request.getParameter("gradeid");
		
		
		//TModuleGroup tModuleGroup = moduleGroupService.retrieveModuleGroup(new Long(groupId));
		//TModule tModule = module.getModule();
		TModule tModule = new TModule();
		tModule.setCreateDate(DateUtil.getDateTime());
		tModule.setModuleName(modulename);
		tModule.setModuleCode(modulecode);
		tModule.setModulePath(modulepath);
		tModule.setMoudleDesc(moduledesc);
		tModule.setParentid(Long.parseLong(parentid));
		tModule.setVisibleFlag((long)1);
		tModule.setEnableFlag((long)1);
		
		String createBy = "initName";
		String goPage = "createSuccess";
		TOptLog tOptLog = new TOptLog();
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		tModule.setCreateBy(createBy);// 登录 user 信息
		tModule.setUsageFlag("1");
		tModule.setModuleType("1");
		tModule.setModuleGrade(Long.parseLong(gradeid));
		
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());		
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
		try{
			moduleService.saveModule(tModule);
			tOptLog.setOptDesc("创建 '"+module.getModule().getModuleName()+ "' 权限成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		}catch(Exception ex){
			tOptLog.setOptDesc("创建 '"+module.getModule().getModuleName()+ "' 权限失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "创建 '"+module.getModule().getModuleName()+ "' 权限失败");
			goPage = "message";
		}
		return mapping.findForward(goPage);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleForm moduleForm = (ModuleForm) form;
		TModuleGroup moduleGroup = moduleGroupService.retrieveModuleGroup(Long
				.valueOf(moduleForm.getGroupId()));
		TModule module = moduleForm.getModule();
		String goPage = "createSuccess";
		TOptLog tOptLog = new TOptLog();
		UserInfo userInfo = this.getCurrentUser(request);
		
		// 获取登录用户信息
		String createBy = "initName";
		// 获取登录用户信息
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		
		module.setCreateBy(userInfo.getUserAccount());// 登录 user 信息
		module.setCreateDate(DateUtil.getDateTime());
		module.setModuleGrade(moduleForm.getModuleGrade());
		module.setUsageFlag("1");
		//module.setTModuleGroup(moduleGroup);
		module.setModuleType("1");
		
		// 日志记录
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());		
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
		try{
			moduleService.updateModule(module);
			tOptLog.setOptDesc("更新 "+moduleForm.getModule().getModuleName()+ " 权限成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		}catch(Exception ex){
			tOptLog.setOptDesc("更新 "+moduleForm.getModule().getModuleName()+ " 权限失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "更新 "+moduleForm.getModule().getModuleName()+ " 权限失败");
			goPage = "message";
		}
		return mapping.findForward(goPage);
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
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleForm moduleForm = (ModuleForm) form;
		Long id =Long.valueOf(moduleForm
				.getId());
		TModule module = moduleService.retrieveModule(id);
		moduleForm.setModule(module);
		moduleForm.setGrade(module.getModuleGrade()==1 ?"系统权限":"用户权限");
		moduleForm.setModuleGrade(module.getModuleGrade());
		return mapping.findForward("view");
	}

	/**
	 * 删除功能权限
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
		boolean ret = moduleService.deleteAll(longIds);
		String createBy = "initName";
		TOptLog tOptLog = new TOptLog();
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
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
		// 删除成功
		if (ret) {
			tOptLog.setOptDesc("删除权限成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			// 删除失败
		} else {
			tOptLog.setOptDesc("删除权限失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			request.setAttribute("msg", "删除权限失败");
			goPage = "message";
		}
		return mapping.findForward(goPage);
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
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModuleForm moduleForm = (ModuleForm) form;
		TModule module = moduleService.retrieveModule(Long.valueOf(moduleForm
				.getId()));
		moduleForm.setModule(module);
		//moduleForm.setGroupId(module.getTModuleGroup().getId().toString());
		moduleForm.setModuleGrade(module.getModuleGrade());
		
		moduleGroupService = (ModuleGroupService) SpringHelper
				.getBean("moduleGroupService");
		List parentList = moduleGroupService.queryModuleGroupByLevel(Long
				.valueOf("1"));
		
		List moduleGradeList = new ArrayList();
		ModuleGradeBean bean = new ModuleGradeBean();
		bean.setGid(1);
		bean.setGradeName("系统权限");
		moduleGradeList.add(bean);
		bean = new ModuleGradeBean();
		bean.setGid(2);
		bean.setGradeName("用户权限");
		moduleGradeList.add(bean);
		
		moduleForm.setModuleGradeList(moduleGradeList);
		moduleForm.setGroupList(parentList);
		return mapping.findForward("modify");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("query");
	}

	public ActionForward displayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("displayList");
	}
	//遍历组,找出根组
	public String searchRootGroup(List list)throws Exception {

		// json
		StringBuffer treeStr = new StringBuffer();
		
		if (list != null && list.size() > 0) {
			Object[] records = (Object[]) list.toArray();
			treeStr.append("[");
			for(Object itemp : records){
				TModule i = (TModule)itemp;
				//如果组id=-1,说明此组为根组.
				if(i.getParentid() == -1){
					//根组加入到树json对象中
					treeStr.append("{id: '" + i.getId()+"@"+CharTools.javaScriptEscape(i.getModuleCode())+"@" +CharTools.javaScriptEscape(i.getModulePath())+"@"+i.getMoudleDesc()+ "',");
					treeStr.append("text: '" + CharTools.javaScriptEscape(i.getModuleName()) + "',");
					treeStr.append("iconCls: 'icon-group',");
					treeStr.append("leaf: false ");
					String subgroup = group(records, i);
					if (subgroup.length() > 0) {
						treeStr.append(",children : [" + subgroup);
						treeStr.append("]");
					}
					treeStr.append("},");
				}
			}
			if (records.length > 0) {
				treeStr.deleteCharAt(treeStr.length() - 1);
			}
			treeStr.append("]");
		}
		return treeStr.toString();
	}
	//查询子组
	public String group(Object[] g, TModule moduleRecord) throws Exception {
		StringBuffer treeStr = new StringBuffer();
		for (Object itemp : g) {
			TModule i = (TModule)itemp;
			if ((i.getParentid()+"").equals(moduleRecord.getId()+"")) {
				treeStr.append("{id: '" + i.getId()+"@"+CharTools.javaScriptEscape(i.getModuleCode())+"@" +CharTools.javaScriptEscape(i.getModulePath())+"@"+i.getMoudleDesc()+ "',");
				treeStr.append("text: '" + CharTools.javaScriptEscape(i.getModuleName()) + "',");
				treeStr.append("iconCls: 'icon-group',");
				String subgroup = group(g, i);
				if (subgroup.length() > 0) {
					treeStr.append("children : [" + subgroup);
					treeStr.append("],");
					treeStr.append("leaf: false ");
				} else {
					treeStr.append("leaf: true ");
				}
				treeStr.append("},");
			}
		}
		if (treeStr.length() > 0) {
			treeStr.deleteCharAt(treeStr.length() - 1);
		}
		return treeStr.toString();
	}
	// sos功能模块列表
	public ActionForward listModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		ModuleManagerService moduleManagerService=(ModuleManagerService)SpringHelper.getBean("moduleManagerService");
		TRole tRole=userInfo.getUser().getRole();
		Long roleID=tRole.getId();
		TreeNode treeNode=moduleManagerService.getTreeNodeByRole(roleID);
		request.setAttribute("moduleTreeNode", treeNode);
		String checked = (String)request.getParameter("checked");
		checked = CharTools.killNullString(checked);
		StringBuffer treeSb = new StringBuffer();
		TModule TModule = treeNode.getTModule();
		Long id = TModule.getId();
		
		if(id == -1){
			treeSb.append("[{id: '-101',");
			treeSb.append("text: '" + CharTools.javaScriptEscape(userInfo.getEnt().getEntName()) + "',");
			//if(checked.equals("true")){
				//treeSb.append("checked: false,");
			//}
			String child = getModuleTree(treeNode.getChildrenNodes(),checked);
			if(child!=null&&child.length()>0){
				treeSb.append("children:[");
				treeSb.append(child);
				treeSb.append("],");
			}
			//treeSb.append("iconCls: 'icon-group',");
			//treeSb.append("checked: false,");
			treeSb.append("leaf: false ");
			treeSb.append("}]");
		}
		
		System.out.println(treeSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(treeSb.toString());
		response.getWriter().flush();

		return mapping.findForward(null);
	}
	
	public String getModuleTree(ArrayList<TreeNode> al,String checked){
		StringBuffer treeSb = new StringBuffer();
		Iterator<TreeNode> i = al.iterator();
		
		while(i.hasNext()){
			TreeNode tmptreeNode = i.next();
			TModule TModule = tmptreeNode.getTModule();
			Long id = TModule.getId();
			String ModuleName = TModule.getModuleName();
			String ModuleCode = TModule.getModuleCode();
			String ModulePath = TModule.getModulePath();
			String MoudleDesc = TModule.getMoudleDesc();
//			ModuleName = CharTools.javaScriptEscape(ModuleName).trim();
//			ModuleCode = CharTools.javaScriptEscape(ModuleCode).trim();
//			ModulePath = CharTools.javaScriptEscape(ModulePath).trim();
//			MoudleDesc = CharTools.javaScriptEscape(MoudleDesc).trim();
			//ModuleCode="";
			//ModulePath="";
			//MoudleDesc="";
			
			treeSb.append("{id: \"" + id + "@#"+CharTools.javaScriptEscape(ModuleName)+ "@#"+CharTools.javaScriptEscape(ModuleCode)+ "@#"+CharTools.javaScriptEscape(ModulePath)+ "@#"+CharTools.javaScriptEscape(MoudleDesc)+"\",");
			treeSb.append("text: '" + CharTools.javaScriptEscape(TModule.getModuleName()) + "',");
			if(checked.equals("true")){
				treeSb.append("checked: false,");
			}
			String child = getModuleTree(tmptreeNode.getChildrenNodes(),checked);
			//System.out.println("child:"+child);
			if(child!=null&&child.length()>0){
				treeSb.append("children:[");
				treeSb.append(child);
				treeSb.append("],");
				treeSb.append("leaf: false ");
			}else{
				treeSb.append("leaf: true ");
			}
			
			treeSb.append("},");
			
			
		}
		if (treeSb.length() > 0) {
			treeSb.deleteCharAt(treeSb.length() - 1);
		}
		System.out.println("treeSb.toString():"+treeSb.toString());
		return treeSb.toString();
	}
	// sos新增功能模块
	public ActionForward addModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//从request中获取参数
		String parentModuleId = request.getParameter("parentModuleId");//父模块id
		String moduleName = request.getParameter("moduleName");//模块名称
		String moduleCode = request.getParameter("moduleCode");//模块编码
		String modulePath = request.getParameter("modulePath");//模块路径
		String moduleDesc = request.getParameter("moduleDesc");//模块描述
		String gradeid = request.getParameter("gradeId");//模块级别
		parentModuleId = java.net.URLDecoder.decode(parentModuleId, "utf-8");
		
		moduleName = CharTools.killNullString(moduleName);
		moduleCode = CharTools.killNullString(moduleCode);
		modulePath = CharTools.killNullString(modulePath);
		moduleDesc = CharTools.killNullString(moduleDesc);
		
		moduleName = java.net.URLDecoder.decode(moduleName, "utf-8");
		moduleCode = java.net.URLDecoder.decode(moduleCode, "utf-8");
		modulePath = java.net.URLDecoder.decode(modulePath, "utf-8");
		moduleDesc = java.net.URLDecoder.decode(moduleDesc, "utf-8");
		gradeid = java.net.URLDecoder.decode(gradeid, "utf-8");
		moduleName = CharTools.killNullString(moduleName);
		moduleCode = CharTools.killNullString(moduleCode);
		modulePath = CharTools.killNullString(modulePath);
		moduleDesc = CharTools.killNullString(moduleDesc);
		
		
		Long parent_module_id = Long.parseLong(parentModuleId);
		
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		
		// 查询模块编码是否存在
		TModule m = moduleService.findByModuleCode(moduleCode);
		if(m != null){
			response.setCharacterEncoding("GBK");
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{result:\"3\"}");//已存在此模块编码
			response.getWriter().flush();
			return mapping.findForward(null);
		}else{
			TModule tModule = new TModule();
			tModule.setModuleName(moduleName);
			tModule.setModuleNameEn(moduleName);
			tModule.setModuleCode(moduleCode);
			tModule.setModulePath(modulePath);
			tModule.setMoudleDesc(moduleDesc);
			tModule.setCreateBy(createBy);// 登录 user 信息
			tModule.setCreateDate(DateUtil.getDateTime());
			tModule.setUsageFlag("1");
			tModule.setModuleType("1");
			tModule.setParentid(parent_module_id);
			tModule.setVisibleFlag((long)1);
			tModule.setEnableFlag((long)1);
			tModule.setModuleGrade(Long.parseLong(gradeid));
			tModule.setSortedIndex(1L);
			
			// 日志记录
			TOptLog tOptLog = new TOptLog();
			tOptLog.setEmpCode(userInfo.getEmpCode());
			tOptLog.setUserName(userInfo.getUserAccount());
			tOptLog.setAccessIp(userInfo.getIp());		
			tOptLog.setUserId(userInfo.getUserId());
			tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
			tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
			try{
				moduleService.saveModule(tModule);
				tOptLog.setOptDesc("创建 "+moduleName+ " 权限成功");
				tOptLog.setResult(new Long(1));
				LogFactory.newLogInstance("optLogger").info(tOptLog);
				response.setCharacterEncoding("GBK");
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write("{result:\"1\"}");//成功
				response.getWriter().flush();
				return mapping.findForward(null);
			}catch(Exception ex){
				System.out.println(ex.getMessage());
				tOptLog.setOptDesc("创建 "+moduleName+ " 权限失败");
				tOptLog.setResult(new Long(0));
				LogFactory.newLogInstance("optLogger").info(tOptLog);
				
				response.setCharacterEncoding("GBK");
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write("{result:\"2\"}");//失败
				response.getWriter().flush();
				return mapping.findForward(null);
			}
		}
	}
	// sos修改功能模块
	public ActionForward updateModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//从request中获取参数
		String moduleId = request.getParameter("moduleId");//模块id
//		String parentModuleId = request.getParameter("parentModuleId");//父模块id
		String moduleName = request.getParameter("moduleName");//模块名称
		String moduleCode = request.getParameter("moduleCode");//模块编码
		String modulePath = request.getParameter("modulePath");//模块路径
		String moduleDesc = request.getParameter("moduleDesc");//模块描述
		String gradeid = request.getParameter("gradeId");//模块级别
		
		moduleName = java.net.URLDecoder.decode(moduleName, "utf-8");
		moduleCode = java.net.URLDecoder.decode(moduleCode, "utf-8");
		modulePath = java.net.URLDecoder.decode(modulePath, "utf-8");
		moduleDesc = java.net.URLDecoder.decode(moduleDesc, "utf-8");
		gradeid = java.net.URLDecoder.decode(gradeid, "utf-8");
		
		moduleName = CharTools.killNullString(moduleName);
		moduleCode = CharTools.killNullString(moduleCode);
		modulePath = CharTools.killNullString(modulePath);
		moduleDesc = CharTools.killNullString(moduleDesc);
		gradeid = CharTools.killNullString(gradeid);
		
//		Long parent_module_id = Long.parseLong(parentModuleId);
		Long module_id = Long.parseLong(moduleId);
		
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		
		// 日志记录
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());		
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
		
		// 获取未更新前模块信息
		TModule _module = moduleService.retrieveModule(module_id);
		_module.setModuleName(moduleName);
		// 模块编码唯一,需要修改
		if(!_module.getModuleCode().equals(moduleCode)){
			// 查询模块编码是否存在
			TModule m = moduleService.findByModuleCode(moduleCode);
			if(m != null){
				response.getWriter().write("{result:\"3\"}");//已存在此模块编码
				return mapping.findForward(null);
			}else{
				_module.setModuleCode(moduleCode);
			}
		}
		_module.setModulePath(modulePath);
		_module.setMoudleDesc(moduleDesc);
		_module.setModuleGrade(CharTools.str2Long(gradeid, 2L));//不传默认为2
		try{
			moduleService.updateModule(_module);
			tOptLog.setOptDesc("更新 '"+moduleName+ "' 权限成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"1\"}");//成功
		}catch(Exception ex){
			tOptLog.setOptDesc("更新 "+moduleName+ " 权限失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"2\"}");//失败
		}
		return mapping.findForward(null);
	}
	// sos删除功能模块
	public ActionForward deleteModules(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//从request中获取参数
		String moduleIds = request.getParameter("moduleIds");//模块id，多个用","隔开
		StringTokenizer st = new StringTokenizer(moduleIds, ",");
		Long[] longIds = new Long[st.countTokens()];
		for (int x = 0; st.hasMoreElements();x ++) {
			longIds[x] = Long.valueOf(st.nextToken());
		}
		
		UserInfo userInfo = this.getCurrentUser(request);
		// 获取登录用户信息
		String createBy = "initName";
		if (userInfo != null) {
			createBy = userInfo.getUserAccount();
		}
		// 日志信息
		TOptLog tOptLog = new TOptLog();
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());		
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.MANAGER_F_CODE);
		tOptLog.setFunCType(LogConstants.MANAGER_C_MODULE_CODE);
		
		// 删除功能模块
		boolean ret = moduleService.deleteAll(longIds);
		if(ret){
			tOptLog.setOptDesc("删除权限成功");
			tOptLog.setResult(new Long(1));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"1\"}");//成功
		}else{
			tOptLog.setOptDesc("删除权限失败");
			tOptLog.setResult(new Long(0));
			LogFactory.newLogInstance("optLogger").info(tOptLog);
			response.getWriter().write("{result:\"2\"}");//失败
		}
		return mapping.findForward(null);
	}
	// sos所有功能模块列表
	public ActionForward listAllModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		String checked = (String)request.getParameter("checked");
		checked = CharTools.javaScriptEscape(checked);
		
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		ModuleManagerService moduleManagerService=(ModuleManagerService)SpringHelper.getBean("moduleManagerService");
		List<TModule> modules=moduleManagerService.queryAllModuleList();
		StringBuffer treeSb = new StringBuffer();
		
		treeSb.append("[{id: '-101',");
		treeSb.append("text: '" + userInfo.getEnt().getEntName() + "',");
		StringBuffer childSb = new StringBuffer();
		for(TModule module : modules){
			if(module.getParentid() == -1){
				
				Long id = module.getId();
				String ModuleName = module.getModuleName();
				String ModuleCode = module.getModuleCode();
				String ModulePath = module.getModulePath();
				String MoudleDesc = module.getMoudleDesc();
				String ModuleGrade = module.getModuleGrade()+"";
//				ModuleName = CharTools.javaScriptEscape(ModuleName).trim();
//				ModuleCode = CharTools.javaScriptEscape(ModuleCode).trim();
//				ModulePath = CharTools.javaScriptEscape(ModulePath).trim();
//				MoudleDesc = CharTools.javaScriptEscape(MoudleDesc).trim();
//				ModuleGrade = CharTools.javaScriptEscape(ModuleGrade).trim();
				childSb.append("{id: \"" + id + "@#"+CharTools.javaScriptEscape(ModuleName)+ "@#"+CharTools.javaScriptEscape(ModuleCode)+ "@#"+CharTools.javaScriptEscape(ModulePath)+ "@#"+CharTools.javaScriptEscape(MoudleDesc)+ "@#"+ModuleGrade+"\",");
				childSb.append("text: '" + CharTools.javaScriptEscape(module.getModuleName()) + "',");
				if(checked.equals("true")){
					childSb.append("checked: false,");
				}
				String child = getAllModuleTree(modules,id+"",checked);
				
				if(child!=null&&child.length()>0){
					childSb.append("children:[");
					childSb.append(child);
					childSb.append("],");
					childSb.append("leaf: false ");
				}else{
					childSb.append("leaf: true ");
				}
				childSb.append("},");
			}
		}

		if(childSb.length()>0){
			treeSb.append("children:[");
			treeSb.append(childSb.deleteCharAt(childSb.length()-1));
			treeSb.append("],");
		}
		treeSb.append("leaf: false ");
		treeSb.append("}]");
		System.out.println(treeSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(treeSb.toString());
		response.getWriter().flush();
		
		return mapping.findForward(null);
	}
	
	public String getAllModuleTree(List<TModule> modules,String parentId,String checked){
		StringBuffer treeSb = new StringBuffer();
		for(TModule module : modules){
			//System.out.println(parentId+":"+module.getParentid()+";"+(parentId == module.getParentid()));
			
			if(parentId.equals(module.getParentid()+"")){
				Long id = module.getId();
				String ModuleName = module.getModuleName();
				String ModuleCode = module.getModuleCode();
				String ModulePath = module.getModulePath();
				String MoudleDesc = module.getMoudleDesc();
				String ModuleGrade = module.getModuleGrade()+"";
//				ModuleName = CharTools.javaScriptEscape(ModuleName).trim();
//				ModuleCode = CharTools.javaScriptEscape(ModuleCode).trim();
//				ModulePath = CharTools.javaScriptEscape(ModulePath).trim();
//				MoudleDesc = CharTools.javaScriptEscape(MoudleDesc).trim();
//				ModuleGrade = CharTools.javaScriptEscape(ModuleGrade).trim();
				
				treeSb.append("{id: \"" + id + "@#"+CharTools.javaScriptEscape(ModuleName)+ "@#"+CharTools.javaScriptEscape(ModuleCode)+ "@#"+CharTools.javaScriptEscape(ModulePath)+ "@#"+CharTools.javaScriptEscape(MoudleDesc)+"@#"+ModuleGrade+"\",");
				treeSb.append("text: '" + CharTools.javaScriptEscape(module.getModuleName()) + "',");
				if(checked.equals("true")){
					treeSb.append("checked: false,");
				}
				String child = getAllModuleTree(modules,id+"",checked);
				//System.out.println("child:"+child);
				if(child!=null&&child.length()>0){
					treeSb.append("children:[");
					treeSb.append(child);
					treeSb.append("],");
					treeSb.append("leaf: false ");
				}else{
					treeSb.append("leaf: true ");
				}
				
				treeSb.append("},");
			}
		}
		if (treeSb.length() > 0) {
			treeSb.deleteCharAt(treeSb.length() - 1);
		}
		System.out.println("treeSb.toString():"+treeSb.toString());
		return treeSb.toString();
	}
	
	// sos用户角色功能模块列表
	public ActionForward listUserModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		String checked = (String)request.getParameter("checked");
		checked = CharTools.javaScriptEscape(checked);
		
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		ModuleManagerService moduleManagerService=(ModuleManagerService)SpringHelper.getBean("moduleManagerService");
		TRole tRole=userInfo.getUser().getRole();
		Long roleID=tRole.getId();
		Page<TModule> modulesPage=moduleManagerService.queryRoleModulesById(roleID);
		StringBuffer treeSb = new StringBuffer();
		
		treeSb.append("[{id: '-101',");
		treeSb.append("text: '" + userInfo.getEnt().getEntName() + "',");
		StringBuffer childSb = new StringBuffer();
		if(modulesPage!=null && modulesPage.getResult()!=null)
			for(TModule module : modulesPage.getResult()){
				if(module.getParentid() == -1){
					
					Long id = module.getId();
					String ModuleName = module.getModuleName();
					String ModuleCode = module.getModuleCode();
					String ModulePath = module.getModulePath();
					String MoudleDesc = module.getMoudleDesc();
					String ModuleGrade = module.getModuleGrade()+"";
//					ModuleName = CharTools.javaScriptEscape(ModuleName).trim();
//					ModuleCode = CharTools.javaScriptEscape(ModuleCode).trim();
//					ModulePath = CharTools.javaScriptEscape(ModulePath).trim();
//					MoudleDesc = CharTools.javaScriptEscape(MoudleDesc).trim();
//					ModuleGrade = CharTools.javaScriptEscape(ModuleGrade).trim();
					childSb.append("{id: \"" + id + "@#"+CharTools.javaScriptEscape(ModuleName)+ "@#"+CharTools.javaScriptEscape(ModuleCode)+ "@#"+CharTools.javaScriptEscape(ModulePath)+ "@#"+CharTools.javaScriptEscape(MoudleDesc)+ "@#"+ModuleGrade+"\",");
					childSb.append("text: '" + CharTools.javaScriptEscape(module.getModuleName()) + "',");
					if(checked.equals("true")){
						childSb.append("checked: false,");
					}
					String child = getAllModuleTree(modulesPage.getResult(),id+"",checked);
					
					if(child!=null&&child.length()>0){
						childSb.append("children:[");
						childSb.append(child);
						childSb.append("],");
						childSb.append("leaf: false ");
					}else{
						childSb.append("leaf: true ");
					}
					childSb.append("},");
				}
			}

		if(childSb.length()>0){
			treeSb.append("children:[");
			treeSb.append(childSb.deleteCharAt(childSb.length()-1));
			treeSb.append("],");
		}
		treeSb.append("leaf: false ");
		treeSb.append("}]");
		System.out.println(treeSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(treeSb.toString());
		response.getWriter().flush();
		
		return mapping.findForward(null);
	}
	
	// sos按权限级别查用户角色功能模块列表
	public ActionForward listUserModuleByModuleGrade(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String checked = (String)request.getParameter("checked");
		String moduleGradeStr = (String)request.getParameter("moduleGrade");
		checked = CharTools.javaScriptEscape(checked);
		long moduleGrade = CharTools.str2Long(moduleGradeStr, 0L);
		
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		ModuleManagerService moduleManagerService=(ModuleManagerService)SpringHelper.getBean("moduleManagerService");
		TRole tRole=userInfo.getUser().getRole();
		Long roleID=tRole.getId();
		Page<TModule> modulesPage=moduleManagerService.queryRoleModulesByIdAndModuleGrade(roleID, moduleGrade);
		StringBuffer treeSb = new StringBuffer();
		
		treeSb.append("[{id: '-101',");
		treeSb.append("text: '" + userInfo.getEnt().getEntName() + "',");
		StringBuffer childSb = new StringBuffer();
		if(modulesPage!=null && modulesPage.getResult()!=null)
			for(TModule module : modulesPage.getResult()){
				if(module.getParentid() == -1){
					
					Long id = module.getId();
					String ModuleName = module.getModuleName();
					String ModuleCode = module.getModuleCode();
					String ModulePath = module.getModulePath();
					String MoudleDesc = module.getMoudleDesc();
//					ModuleName = CharTools.javaScriptEscape(ModuleName).trim();
//					ModuleCode = CharTools.javaScriptEscape(ModuleCode).trim();
//					ModulePath = CharTools.javaScriptEscape(ModulePath).trim();
//					MoudleDesc = CharTools.javaScriptEscape(MoudleDesc).trim();
					childSb.append("{id: \"" + id + "@#"+CharTools.javaScriptEscape(ModuleName)+ "@#"+CharTools.javaScriptEscape(ModuleCode)+ "@#"+CharTools.javaScriptEscape(ModulePath)+ "@#"+MoudleDesc+"\",");
					childSb.append("text: '" + CharTools.javaScriptEscape(module.getModuleName()) + "',");
					if(checked.equals("true")){
						childSb.append("checked: false,");
					}
					String child = getAllModuleTree(modulesPage.getResult(),id+"",checked);
					
					if(child!=null&&child.length()>0){
						childSb.append("children:[");
						childSb.append(child);
						childSb.append("],");
						childSb.append("leaf: false ");
					}else{
						childSb.append("leaf: true ");
					}
					childSb.append("},");
				}
			}
		
		if(childSb.length()>0){
			treeSb.append("children:[");
			treeSb.append(childSb.deleteCharAt(childSb.length()-1));
			treeSb.append("],");
		}
		treeSb.append("leaf: false ");
		treeSb.append("}]");
		System.out.println(treeSb.toString());
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(treeSb.toString());
		response.getWriter().flush();
		
		return mapping.findForward(null);
	}
}
