package com.sosgps.v21.targetTemplate.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.sos.helper.SpringHelper;
import com.sosgps.v21.signBill.service.SignBillService;
import com.sosgps.v21.targetTemplate.service.TargetTemplateService;
import com.sosgps.wzt.system.common.UserInfo;

public class TargetTemplateAction extends DispatchAction {

    private TargetTemplateService targetTemplateService =(TargetTemplateService)SpringHelper.getBean("targetTemplateService");

    /**
     * 获取区间维护数据
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = (HttpSession) request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        response.setContentType("text/json; charset=utf-8");
        if (userInfo == null) {
            response.getWriter().write("{result:\"-1\"}");// 未登录
            return mapping.findForward(null);
        }
        String entCode = userInfo.getEmpCode();
        //targetTemplateService.
       // response.getWriter().write(
        //        "{result:\"0\",data:" + TargetUtils.convertRateFromObjectToJson(kpiList) + "}");// 成功
        return mapping.findForward(null);
    }

}
