/**
 * 
 */
package com.sosgps.wzt.system.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.system.form.LoginForm;
import com.sosgps.wzt.system.service.ShortMessageService;
import com.sosgps.wzt.system.service.UserService;

/**
 * @author xiaojun.luan
 * 
 */
public class ShortMessageAction extends DispatchWebActionSupport {

	private ShortMessageService shortMessageService = (ShortMessageService) SpringHelper
			.getBean("shortMessageService");
	private UserService userService = (UserService) SpringHelper
	.getBean("userService");

	public ActionForward sendDynamicPassword(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LoginForm loginForm = (LoginForm) form;
		String returnPage="SendMessageAlert";
		List list=userService.hasAccountByPhoneNum(loginForm.getUserAccount(), loginForm.getEmpCode());
		if(list==null || list.size()==0){
			request.setAttribute("error", "ϵͳδ�󶨴��ֻ��ţ�����ϵ����Ա!");
			return mapping.findForward(returnPage);
		}
		boolean flag = shortMessageService.sendDynamicPassword(loginForm
				.getEmpCode(), loginForm.getUserAccount(),
				ShortMessageService.DYNAMIC_MESSAGE_LENGTH,
				ShortMessageService.LOGIN_TYPE);
		if (flag) {
			request.setAttribute("error", "������������ѳɹ����ͣ���ע�����!");
		} else {
			request.setAttribute("error", "����������뷢��ʧ�ܣ������ԣ�");
		}
		return mapping.findForward(returnPage);
	}

	public ActionForward changePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserInfo userInfo = this.getCurrentUser(request);
		if (userInfo != null) {
			String phoneNum = userInfo.getUser().getUserContact();
			if (phoneNum != null && phoneNum.length() > 0) {
				boolean flag = shortMessageService.sendDynamicPassword(userInfo
						.getEmpCode(), phoneNum,
						ShortMessageService.MODIFY_PASSWORD_LENGTH,
						ShortMessageService.MODIFY_PASSWORD_TYPE);
				request.setAttribute("error", "������֤�����ѳɹ����ͣ���ע����գ�");
			}else{
				request.setAttribute("error", "���˻�δ���ֻ����룬�������Ա��ϵ��");
			}
		}else{
			request.setAttribute("error", "�˻��ѳ�ʱ�������µ�½��");
		}
		return mapping.findForward("SendMessageAlert");
	}
}
