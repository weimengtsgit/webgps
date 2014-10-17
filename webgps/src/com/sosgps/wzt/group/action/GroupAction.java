/**
 * 
 */
package com.sosgps.wzt.group.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.group.form.TGroupCtlForm;
import com.sosgps.wzt.group.service.GroupService;
import com.sosgps.wzt.group.service.impl.GroupServiceImpl;
import com.sosgps.wzt.manage.terminalgroup.service.TerminalGroupManageService;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.CharTools;

/**
 * @author shiguang.zhou
 *
 */
public class GroupAction extends DispatchAction {

	public ActionForward groupManager(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return mapping.findForward("groupManager");
	}
	/**
	 * 获得组结构
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getTTargetGroup(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		TerminalGroupManageService terminalGroupManageService = (TerminalGroupManageService) SpringHelper
		.getBean("TerminalGroupManageServiceImpl");
		UserInfo userInfo = this.getCurrentUser(request);
		String empCode = userInfo.getEmpCode();
		List tTargetGroupList=terminalGroupManageService.viewEntTermGroup(empCode);
		request.setAttribute("tTargetGroupList", tTargetGroupList);
		return mapping.findForward("tTargetGroup");
	}
	/**
	 * 增，删，改组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tTargetGroupCtl(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		/*TGroupCtlForm tTargetGroupCtlForm=(TGroupCtlForm)form;
		String groupName=tTargetGroupCtlForm.getGroupName();
		Long groupId=tTargetGroupCtlForm.getGroupId();
		 
		String ctl=tTargetGroupCtlForm.getCtl();
		String childGroupName=tTargetGroupCtlForm.getChildGroupName();*/
		
		String childGroupName=request.getParameter("groupname");
		childGroupName = CharTools.killNullString(childGroupName);
		childGroupName = java.net.URLDecoder.decode(childGroupName, "utf-8");
		childGroupName = CharTools.killNullString(childGroupName);
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		String groupstatus=request.getParameter("groupstatus");
		String groupId= request.getParameter("groupId");
		
		String ctl=request.getParameter("ctl");
		String parentid=request.getParameter("parentid");
		String week=request.getParameter("week");
		
		GroupService tTargetGroupService=(GroupServiceImpl)SpringHelper.getBean("tTargetGroupService");
		String resultStr = "{success:false,result:2}";
		//修改组/部门名称
		if("modify".equals(ctl)){
			//groupId为此组id,而不是父组id
			boolean result=tTargetGroupService.modTGNameById(Long.parseLong(groupId), childGroupName,starttime,endtime , groupstatus, Long.parseLong(week));
			if(result){
				//request.setAttribute("result", "修改成功!");
				resultStr = "{success:true,result:1}";
			}else{
				//request.setAttribute("result", "修改失败!");
				resultStr = "{success:false,result:2}";
			}
		}
		//删除组/部门
		if("del".equals(ctl)){
			//判断如果有终端在此组下，不允许删除
			List ref = tTargetGroupService.findRefTermGroupById(Long.parseLong(groupId));
			if(ref!=null && ref.size()>0){
				resultStr = "{success:false,result:3}";//有终端在此组下，不允许删除
			}else{
				boolean result=tTargetGroupService.delTGById(Long.parseLong(groupId));
				if(result){
					//request.setAttribute("result", "删除成功!");
					resultStr = "{success:true,result:1}";
				}else{
					//request.setAttribute("result", "删除失败!");
					resultStr = "{success:false,result:2}";
				}
			}
		}
		
		//增加组/部门
		if("add".equals(ctl)){
			//modify zhangwei 增加获取登录人所属企业
			//String empcode = "empRoot";
			HttpSession session = (HttpSession) request.getSession();
			UserInfo user = (UserInfo) session.getAttribute("userInfo");
			String empcode = user.getEmpCode();//
			//end modify
			
			TEnt ent = tTargetGroupService.getEnt(empcode);
			
			boolean result=tTargetGroupService.addChildTG(Long.parseLong(parentid),childGroupName,ent,starttime,endtime,user.getUserId(), Long.parseLong(week));
			if(result){
				//request.setAttribute("result", "增加成功!");
				resultStr = "{success:true,result:1}";
			}else{
				//request.setAttribute("result", "增加失败!");
				resultStr = "{success:true,result:2}";
			}
		}
		
		response.setContentType("text/json; charset=utf-8");
		response.getWriter().write(resultStr);
		return mapping.findForward(null);
	}
	
	public ActionForward init(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return mapping.findForward("init");
	}
	/**
	 * 获取登录用户信息
	 * @param request
	 * @return
	 */
	public UserInfo getCurrentUser(HttpServletRequest request) {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		return user;
	}
}
