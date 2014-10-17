package com.sosgps.wzt.manage.terminalgroup.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.log.LogFactory;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.terminal.service.TerminalManageService;
import com.sosgps.wzt.manage.terminalgroup.service.TerminalGroupManageService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;

/**
 * @Title:终端组管理action类
 * @Description:
 * @Company: 
 * @author: 
 * @version 1.0
 * @date: 
 */
public class TerminalGroupManageAction extends DispatchWebActionSupport {
	/**
	 * 系统级终端组管理init
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initForSystem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跳转地址
		String actionUrl = request.getContextPath() + "/manage/Group_Edit.jsp";
		// 先去企业选择页面，将功能跳转地址传递过去
		ActionForward forward = new ActionForward();
		forward
				.setPath("/entManage.do?actionMethod=select&pageNo=1&vague=y&actionUrl="
						+ actionUrl);
		forward.setRedirect(true);
		return forward;
	}

	/**
	 * 系统级用户可见终端组管理init
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initUserTermGroupForSystem(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 跳转地址
		String actionUrl = request.getContextPath()
				+ "/manage/termGroupManage.do?actionMethod=listUser";
		// 先去企业选择页面，将功能跳转地址传递过去
		ActionForward forward = new ActionForward();
		forward
				.setPath("/entManage.do?actionMethod=select&pageNo=1&vague=y&actionUrl="
						+ actionUrl);
		forward.setRedirect(true);
		return forward;
	}

	/**
	 * 企业级终端组管理init
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initForEnt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entCode = help(request);// 企业代码
		if (entCode == null || entCode.equals("") || entCode.equals("")) {
			return mapping.findForward("error");
		}
		// 跳转地址
		ActionForward forward = new ActionForward();
		forward.setPath("/Group_Edit.jsp?entCode=" + entCode);
		forward.setRedirect(true);
		return forward;
	}

	/**
	 * 企业级用户可见终端组管理init
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initUserTermGroupForEnt(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 跳转地址
		ActionForward forward = new ActionForward();
		forward.setPath("/termGroupManage.do?actionMethod=listUser");
		forward.setRedirect(true);
		return forward;
	}

	/**
	 * 增加终端下属组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addTermGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entCode = help(request);// 企业代码
		if (entCode == null || entCode.equals("") || entCode.equals("")) {
			return mapping.findForward("error");
		}
		String termGroupName = request.getParameter("termGroupName");// 终端组名称
		String termGroupId = request.getParameter("termGroupId");// 终端组id
		String childrenGroupMaxId = request.getParameter("childrenGroupMaxId");// 子组最大顺序号
		// String groupSort = request.getParameter("groupSort");// 终端组顺序号
		// String upSortGroupId = request.getParameter("upSortGroupId");//
		// 上兄弟组顺序号
		// String downSortGroupId = request.getParameter("downSortGroupId");//
		// 下兄弟组顺序号
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		boolean re = terminalGroupManageService.add(entCode, termGroupName,
				termGroupId, childrenGroupMaxId);
		if (re) {
			request.setAttribute("addResult", "t");
		} else {
			request.setAttribute("addResult", "f");
		}
		return mapping.findForward("self");
	}

	/**
	 * 修改终端组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward editTermGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entCode = help(request);// 企业代码
		if (entCode == null || entCode.equals("")) {
			return mapping.findForward("error");
		}
		String termGroupId = request.getParameter("termGroupId");// 终端组id
		String termGroupName = request.getParameter("termGroupName");// 终端组名称
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		boolean re = terminalGroupManageService
				.edit(termGroupId, termGroupName);
		if (re) {
			request.setAttribute("editResult", "t");
		} else {
			request.setAttribute("editResult", "f");
		}
		return mapping.findForward("self");
	}

	/**
	 * 删除终端组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteTermGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String entCode = help(request);// 企业代码
		if (entCode == null || entCode.equals("")) {
			return mapping.findForward("error");
		}
		String termGroupId = request.getParameter("termGroupId");// 终端组id
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		boolean re = terminalGroupManageService.delete(termGroupId);
		if (re) {
			request.setAttribute("deleteResult", "t");
		} else {
			request.setAttribute("deleteResult", "f");
		}
		return mapping.findForward("self");
	}

	/**
	 * 获得用户可见终端组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward viewUserTermGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Object obj = request.getSession().getAttribute("userInfo");
		if (obj != null && obj instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) obj;
			Long userId = userInfo.getUserId();
			TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
					.getBean("TerminalGroupManageServiceImpl");
			List list = terminalGroupManageService.viewUserTermGroup(userId,
					userInfo.getEmpCode());
			request.setAttribute("userViewTermGroup", list);
		}
		return mapping.findForward("userViewTermGroupPage");
	}

	/**
	 * 获得企业终端组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward viewEntTermGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String entCode = request.getParameter("entCode");// 企业代码
		if (entCode == null || entCode.equals("")) {
			Object obj = request.getSession().getAttribute("userInfo");
			if (obj != null && obj instanceof UserInfo) {
				UserInfo userInfo = (UserInfo) obj;
				entCode = userInfo.getEmpCode();
			}
		}
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		List list = terminalGroupManageService.viewEntTermGroup(entCode);
		request.setAttribute("viewEntTermGroup", list);
		return mapping.findForward("viewEntTermGroupPage");
	}

	/**
	 * 系统级企业用户列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entCode = help(request);// 企业代码
		if (entCode == null || entCode.equals("")) {
			return mapping.findForward("error");
		}
		String pageNo = request.getParameter("pageNo");// 第几页
		String pageSize = request.getParameter("pageSize");// 每页行数
		String paramName = request.getParameter("paramName");// 过滤字段名称
		String paramValue = request.getParameter("paramValue");// 过滤字段值
		String vague = request.getParameter("vague");// 过滤字段是否模糊匹配
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		Page<TUser> re = terminalGroupManageService.listUser(entCode, pageNo,
				pageSize, paramName, paramValue, vague);
		request.setAttribute("userList", re);
		request.setAttribute("entCode", entCode);
		request.setAttribute("paramName", paramName);
		request.setAttribute("paramValue", paramValue);
		request.setAttribute("vague", vague);
		return mapping.findForward("list");
	}

	/**
	 * 获得用户可见终端组
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward userTermGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entCode = help(request);// 企业代码
		if (entCode == null || entCode.equals("")) {
			return mapping.findForward("error");
		}
		String userIdStr = request.getParameter("userId");// 用户id
		Long userId = Long.parseLong(userIdStr);
		String editResult = request.getParameter("editResult");
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		List entTermGroup = terminalGroupManageService
				.viewEntTermGroup(entCode);
		List list = terminalGroupManageService.viewUserTermGroup(userId,
				entCode);
		// List list = terminalGroupManageService.viewUserTermGroup(userId);
		request.setAttribute("entTermGroupList", entTermGroup);
		request.setAttribute("termGroupList", list);
		request.setAttribute("entCode", entCode);
		request.setAttribute("userId", userIdStr);
		request.setAttribute("editResult", editResult);
		return mapping.findForward("editUserTermGroup");
	}

	/**
	 * 用户可见终端组设置
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward userTermGroupSet(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String entCode = help(request);// 企业代码
		if (entCode == null || entCode.equals("") || entCode.equals("")) {
			return mapping.findForward("error");
		}
		String userIdStr = request.getParameter("userId");// 用户id
		Long userId = Long.parseLong(userIdStr);
		String groupIds = request.getParameter("groupIds");// 终端组id，以","号隔开
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		boolean re = terminalGroupManageService.userTermGroupSet(entCode,
				userId, groupIds);
		request.setAttribute("entCode", entCode);
		request.setAttribute("userId", userIdStr);
		request.setAttribute("editResult", re);

		ActionForward forward = new ActionForward();
		forward
				.setPath("/termGroupManage.do?actionMethod=userTermGroup&entCode="
						+ entCode + "&userId=" + userId + "&editResult=" + re);
		forward.setRedirect(true);
		return forward;
	}

	/**
	 * 初始化终端所属组修改
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward initTerminalInGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Object obj = request.getSession().getAttribute("userInfo");
		if (obj != null && obj instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) obj;
			Long userId = userInfo.getUserId();
			TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
					.getBean("TerminalGroupManageServiceImpl");
			// modify zhangwei 2009-04-22 增加企业代码查询条件
			List list = terminalGroupManageService.viewUserTermGroup(userId,
					userInfo.getEmpCode());
			// List list = terminalGroupManageService.viewUserTermGroup(userId);
			request.setAttribute("userViewTermGroup", list);
		}
		return mapping.findForward("editTerminalInGroup");
	}

	/**
	 * 终端组所属关系修改
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void editGroupInGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entCode = help(request);// 企业代码
		if (entCode == null || entCode.equals("") || entCode.equals("")) {
			return;
		}
		String groupId = request.getParameter("groupId");// 子组id
		String parentGroupId = request.getParameter("parentGroupId");// 父组id
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		boolean re = terminalGroupManageService.editGroupInGroup(groupId,
				parentGroupId);
		// 写入数据库操作日志
		TOptLog tOptLog = new TOptLog();
		// 获取登录用户信息
		UserInfo userInfo = this.getCurrentUser(request);
		tOptLog.setEmpCode(userInfo.getEmpCode());
		tOptLog.setUserName(userInfo.getUserAccount());
		tOptLog.setAccessIp(userInfo.getIp());
		tOptLog.setUserId(userInfo.getUserId());
		tOptLog.setFunFType(LogConstants.LOG_SOURCE_MANAGE);
		tOptLog.setFunCType(LogConstants.EDIT_GROUP_IN_GROUP);
		tOptLog.setOptDesc(Constants.EDIT_GROUP_IN_GROUP);
		if (re) {
			tOptLog.setResult(1L);
		} else {
			tOptLog.setResult(0L);
		}
		try {
			LogFactory.newLogInstance("optLogger").info(tOptLog);
		} catch (Exception e) {
			SysLogger.sysLogger.error(Constants.ERROR_OPT_LOG_SAVE, e);
			request.setAttribute("msg", "保存操作日志失败");
		}
		response.setContentType("text/xml;charset=utf-8");
		try {
			response.getWriter().write("<r>" + re + "</r>");
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.EDIT_GROUP_IN_GROUP + " "
							+ Constants.ERROR_RESPONSE_WRITE, e);
		}
	}

	/**
	 * 帮助方法，取得entCode
	 * 
	 * @param request
	 * @return
	 */
	public String help(HttpServletRequest request) {
		String entCode = null;// 企业代码
		Object obj = request.getSession().getAttribute("userInfo");
		if (obj != null && obj instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) obj;
			entCode = userInfo.getEmpCode();
			// 超级管理员
			if (entCode.equalsIgnoreCase("emproot")) {
				// 从request中获得参数
				// entCode = request.getParameter("entCode");// 企业代码
			}
		}
		return entCode;
	}

	/**
	 * 帮助方法，是否为系统超级管理员
	 * 
	 * @param request
	 * @return
	 */
	public boolean isAdmin(HttpServletRequest request) {
		boolean re = false;
		Object obj = request.getSession().getAttribute("userInfo");
		if (obj != null && obj instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) obj;
			String entCode = userInfo.getEmpCode();
			// 超级管理员
			if (entCode.equalsIgnoreCase("emproot")) {
				return true;
			}
		}
		return re;
	}


	// 终端树sos企业终端组
	@SuppressWarnings("rawtypes")
	public ActionForward groupListForEntTerminal(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从session中获取企业代码和用户id
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// service查询企业组
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		String node = request.getParameter("node");
		//System.out.println("组节点id node="+node);
		boolean flag = false;
		if (node == null){
			response.setCharacterEncoding("GBK");
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("");
			response.getWriter().flush();
			return mapping.findForward(null);
		}else if(node.equals("-100")||node.equals("-101")) {
			flag = true;
			node = "-1";
		}else{
			flag = false;
			String[] nodeArr = CharTools.split(node, "@");
			node = nodeArr[0];
		}
		
		//所有组信息
		List list = terminalGroupManageService.viewUserTermGroup(userId,entCode,node);
		String tree1="";
		if (list != null && list.size() > 0) {
			try{
					//如果flag = true,说明是根组访问
					String tree=searchRootGroupTerminal(list);

					
					if(flag){
						tree1 = "[{id: '-101',text: '"+userInfo.getEnt().getEntName()+"',iconCls: 'icon-group',leaf: false ,children : "+tree+"}]";
						
					}else{
						tree1 = tree;
						
					}
					
					
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		String leaf = searchTerminalByGroudId(Long.parseLong(node), userInfo.getEnt());
		
		
		if(leaf!=null&&leaf.length()>0){
			//System.out.println("leaf:"+leaf);
			if(tree1.length()>0){
				tree1 = tree1.substring(0, tree1.length()-1);
				tree1 +=",";
			}else{
				tree1 +="[";
			}
			tree1 +=leaf;
			tree1 +="]";
		}
		
		response.setCharacterEncoding("GBK");
		//System.out.println(tree1);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(tree1);
		response.getWriter().flush();
		return mapping.findForward(null);
	}
	
	/**
	 * 根据组id查找终端sos
	 * @param groupid
	 * @return
	 * @throws Exception
	 */
	public String searchTerminalByGroudId(Long groupid, TEnt tEnt)throws Exception {
		TerminalManageService terminalManageService = (TerminalManageService) SpringHelper
		.getBean("TerminalManageServiceImpl");
		String tree = terminalManageService.findByTermGroupId(groupid, tEnt);
		if(tree!=null&&tree.length()>=0){
			return tree;
		}else{
			return "";
		}

		
	}
	
	//遍历组,找出根组
	@SuppressWarnings("rawtypes")
	public String searchRootGroupTerminal(List list)throws Exception {

		// json
		StringBuffer treeStr = new StringBuffer();
		
		if (list != null && list.size() > 0) {
			Object[] records = (Object[]) list.toArray();
			treeStr.append("[");
			for(Object itemp : records){
				TTermGroup i = (TTermGroup) itemp;
				//如果组id=-1,说明此组为根组.
				//if(i.getParentId() == -1){
					//根组加入到树json对象中
					treeStr.append("{id: '" + i.getId()+"@"+CharTools.javaScriptEscape(i.getStartTime())+"@" +CharTools.javaScriptEscape(i.getEndTime())+"@"+i.getGroupStatus()+ "@"+i.getWeek()+"',");
					treeStr.append("text: '" + CharTools.javaScriptEscape(i.getGroupName()) + "',");
					treeStr.append("iconCls: 'icon-group',");
					treeStr.append("checked: false,");
					treeStr.append("leaf: false ");
					
					treeStr.append("},");
				//}
			}
			if (records.length > 0) {
				treeStr.deleteCharAt(treeStr.length() - 1);
			}
			treeStr.append("]");
		}
		return treeStr.toString();
	}
	
	
	
	// 企业终端组
	@SuppressWarnings("rawtypes")
	public ActionForward groupListForEnt(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从session中获取企业代码和用户id
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		//Long userId = userInfo.getUserId();
		// service查询企业组
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		//所有组信息
		List list = terminalGroupManageService.viewEntTermGroup(entCode);
		
		String node = request.getParameter("node");
		//System.out.println("组节点id node="+node);
		try{
			if (node.equals("-100")) {
				String tree=searchRootGroup(list,false,false);
				StringBuffer tree1 = new StringBuffer();
				tree1.append("[{id: '-101',text: '"+userInfo.getEnt().getEntName()+"',iconCls: 'icon-group',leaf: false ");
				if(tree!=null && tree.length() > 0){
					tree1.append(",children : "+tree);
				}
				tree1.append("}]");
				//String tree1 = "[{id: '-101',text: '"+userInfo.getEnt().getEntName()+"',iconCls: 'icon-group',leaf: false ,children : "+tree+"}]";
				response.setCharacterEncoding("GBK");
				//System.out.println(tree1.toString());
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write(tree1.toString());
				response.getWriter().flush();
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
		return mapping.findForward(null);
	}
	

	
	//遍历组,找出根组
	@SuppressWarnings("rawtypes")
	public String searchRootGroup(List list,boolean isCombox,boolean ischeckbox)throws Exception {
		// json
		StringBuffer treeStr = new StringBuffer();
		if (list != null && list.size() > 0) {
			Object[] records = (Object[]) list.toArray();
			treeStr.append("[");
			for(Object itemp : records){
				TTermGroup i = (TTermGroup) itemp;
				//如果组id=-1,说明此组为根组.
				if(i.getParentId() == -1){
					//根组加入到树json对象中
					treeStr.append("{id: '" + i.getId()+"@"+CharTools.javaScriptEscape(i.getStartTime())+"@" +CharTools.javaScriptEscape(i.getEndTime())+"@"+i.getGroupStatus()+ "@"+i.getWeek()+"',");
					
					treeStr.append("text: '" + CharTools.javaScriptEscape(i.getGroupName()) + "',");
					if(ischeckbox){
						treeStr.append("checked : false ,");
					}
					treeStr.append("iconCls: 'icon-group',");
					if(isCombox){
						treeStr.append("expanded : true ,");
					}
					
					treeStr.append("leaf: false ");
					
					String subgroup = group(records, i,isCombox ,ischeckbox);
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
	public String group(Object[] g, TTermGroup groupRecord,boolean isCombox,boolean ischeckbox) throws Exception {
		StringBuffer treeStr = new StringBuffer();
		for (Object itemp : g) {
			TTermGroup i = (TTermGroup) itemp;
			if ((i.getParentId()+"").equals(groupRecord.getId()+"")) {
				treeStr.append("{id: '" + i.getId()+"@"+CharTools.javaScriptEscape(i.getStartTime())+"@" +CharTools.javaScriptEscape(i.getEndTime())+"@"+i.getGroupStatus()+ "@"+i.getWeek()+"',");
				treeStr.append("text: '" + CharTools.javaScriptEscape(i.getGroupName()) + "',");
				if(ischeckbox){
					treeStr.append("checked : false ,");
				}
				treeStr.append("iconCls: 'icon-group',");
				if(isCombox){
					treeStr.append("expanded : true ,");
				}
				String subgroup = group(g, i ,isCombox,ischeckbox);
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
	
	// 用户可见终端组
	@SuppressWarnings("rawtypes")
	public ActionForward groupListForUser(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从session中获取企业代码和用户id
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// service查询此用户可见组
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		List list = terminalGroupManageService.viewUserTermGroup(userId,
				entCode);
		String node = request.getParameter("node");
		try{
			if (node.equals("-100")) {
				String tree=searchRootGroup(list,false,false);
				String tree1 = "[{id: '-101',text: '"+userInfo.getEnt().getEntName()+"',iconCls: 'icon-group',leaf: false ,children : "+tree+"}]";
				response.setCharacterEncoding("GBK");
				//System.out.println(tree1);
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write(tree1);
				response.getWriter().flush();
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return mapping.findForward(null);
	}


	// 企业终端组,给combox使用
	@SuppressWarnings("rawtypes")
	public ActionForward groupListForUserCombox(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从session中获取企业代码和用户id
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
//		String entCode=request.getParameter("entCode");
//		entCode=java.net.URLDecoder.decode(entCode, "utf-8");
//		String strId=request.getParameter("userId");
//		Long userId=Long.valueOf(strId);
		// service查询此用户可见组
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		List list = terminalGroupManageService.viewUserTermGroup(userId,
				entCode);
		String node = request.getParameter("node");
		try{
			if (node.equals("-100")) {
				String tree=searchRootGroup(list,true,false);
				//String tree1 = "[{id: '-101',text: '"+userInfo.getEnt().getEntName()+"',expanded : true ,iconCls: 'icon-group',leaf: false ,children : "+tree+"}]";
				
				response.setCharacterEncoding("GBK");
				//System.out.println(tree);
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write(tree);
				response.getWriter().flush();
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return mapping.findForward(null);
	}
	
	@SuppressWarnings("rawtypes")
	public ActionForward groupListForUserchecked(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从session中获取企业代码和用户id
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		//Long userId = userInfo.getUserId();
		// service查询此用户可见组
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		List list = terminalGroupManageService.viewEntTermGroup(entCode);
		String node = request.getParameter("node");
		try{
			if (node.equals("-100")) {
				String tree=searchRootGroup(list,true,true);
				//String tree1 = "[{id: '-101',text: '"+userInfo.getEnt().getEntName()+"',expanded : true ,iconCls: 'icon-group',leaf: false ,children : "+tree+"}]";
				
				response.setCharacterEncoding("GBK");
				//System.out.println(tree);
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write(tree);
				response.getWriter().flush();
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return mapping.findForward(null);
	}
	
	public ActionForward groupListForEntTerminalNoCheck(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 从session中获取企业代码和用户id
		UserInfo userInfo = this.getCurrentUser(request);
		String entCode = userInfo.getEmpCode();
		Long userId = userInfo.getUserId();
		// service查询企业组
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
				.getBean("TerminalGroupManageServiceImpl");
		String node = request.getParameter("node");
		//System.out.println("组节点id node="+node);
		boolean flag = false;
		if (node.equals("-100")||node.equals("-101")) {
			flag = true;
			node = "-1";
		}else{
			flag = false;
			String[] nodeArr = CharTools.split(node, "@");
			node = nodeArr[0];
		}
		
		//所有组信息
		List list = terminalGroupManageService.viewUserTermGroup(userId,entCode,node);
		String tree1="";
		if (list != null && list.size() > 0) {
			try{
					//如果flag = true,说明是根组访问
					String tree=searchRootGroupTerminalNoCheck(list);
					if(flag){
						tree1 = "[{id: '-101',text: '"+userInfo.getEnt().getEntName()+"',iconCls: 'icon-group',leaf: false ,children : "+tree+"}]";
						
					}else{
						tree1 = tree;
						
					}
			}catch(Exception e){
				System.out.println(e);
			}
		}
		String leaf = searchTerminalByGroudIdNoCheck(Long.parseLong(node));
		if(leaf!=null&&leaf.length()>0){
			//System.out.println("leaf:"+leaf);
			if(tree1.length()>0){
				tree1 = tree1.substring(0, tree1.length()-1);
				tree1 +=",";
			}else{
				tree1 +="[";
			}
			tree1 +=leaf;
			tree1 +="]";
		}
		response.setCharacterEncoding("GBK");
		//System.out.println(tree1);
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(tree1);
		response.getWriter().flush();
		return mapping.findForward(null);
	}
	
	public String searchRootGroupTerminalNoCheck(List list)throws Exception {
		// json
		StringBuffer treeStr = new StringBuffer();
		if (list != null && list.size() > 0) {
			Object[] records = (Object[]) list.toArray();
			treeStr.append("[");
			for(Object itemp : records){
				TTermGroup i = (TTermGroup) itemp;
				//如果组id=-1,说明此组为根组.
				//if(i.getParentId() == -1){
					//根组加入到树json对象中
					treeStr.append("{id: '" + i.getId()+"@"+CharTools.javaScriptEscape(i.getStartTime())+"@" +CharTools.javaScriptEscape(i.getEndTime())+"@"+i.getGroupStatus()+ "@"+i.getWeek()+"',");
					treeStr.append("text: '" + CharTools.javaScriptEscape(i.getGroupName()) + "',");
					treeStr.append("iconCls: 'icon-group',");
					//treeStr.append("checked: false,");
					treeStr.append("leaf: false ");
					treeStr.append("},");
				//}
			}
			if (records.length > 0) {
				treeStr.deleteCharAt(treeStr.length() - 1);
			}
			treeStr.append("]");
		}
		return treeStr.toString();
	}
	
	public String searchTerminalByGroudIdNoCheck(Long groupid)throws Exception {
		TerminalManageService terminalManageService = (TerminalManageService) SpringHelper.getBean("TerminalManageServiceImpl");
		String tree = terminalManageService.findByTermGroupIdNoCheck(groupid);
		if(tree!=null&&tree.length()>=0){
			return tree;
		}else{
			return "";
		}
	}
	
}
