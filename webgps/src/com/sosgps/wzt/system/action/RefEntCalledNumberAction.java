/**
 * 
 */
package com.sosgps.wzt.system.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;

import com.sosgps.wzt.orm.RefEntCallednumber;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.system.form.RefEntCalledNumberForm;
import com.sosgps.wzt.system.model.TEntValue;
import com.sosgps.wzt.system.service.EnterpManageService;
import com.sosgps.wzt.system.service.RefEntCalledNumberService;
import com.sosgps.wzt.system.service.TEntService;

/**
 * @author xiaojun.luan
 *
 */
public class RefEntCalledNumberAction extends DispatchWebActionSupport {
	private Log log=LogFactory.getLog(RefEntCalledNumberAction.class);
	
	private RefEntCalledNumberService refEntCalledNumberService=(RefEntCalledNumberService)SpringHelper.getBean("refEntCalledNumberService");
	
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("view ∫ÙΩ–∫≈¬Î");
		RefEntCalledNumberForm refEntCalledNumberForm=(RefEntCalledNumberForm)form;
		String entCode=request.getParameter("entCode");
		if(entCode==null || entCode.length()==0){
			entCode=this.getCurrentUser(request).getEmpCode();
		}
		List list=refEntCalledNumberService.findByEntCode(entCode);
		request.setAttribute("refEntCalledNumberList", list);
		return mapping.findForward("view");
	}
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("init ∫ÙΩ–∫≈¬Î");
		RefEntCalledNumberForm refEntCalledNumberForm=(RefEntCalledNumberForm)form;
		refEntCalledNumberForm.setMethod("create");
		refEntCalledNumberForm.setButtonName("‘ˆº”");
		String goPage="init";
		String entCode=request.getParameter("entCode");
		if(entCode==null || entCode.length()==0){
			entCode=this.getCurrentUser(request).getEmpCode();
		}
		
		refEntCalledNumberForm.setEntCode(entCode);

		TEntService entService=(TEntService)SpringHelper.getBean("tEntService");
		TEnt tEnt=entService.findByEntCode(entCode);
		
		request.setAttribute("tEnt", tEnt);
		return mapping.findForward(goPage);
	}	
	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("…æ≥˝—°÷– ∫ÙΩ–∫≈¬Î");
		RefEntCalledNumberForm refEntCalledNumberForm=(RefEntCalledNumberForm)form;
		String entCode=refEntCalledNumberForm.getEntCode();

		String[] ids=request.getParameterValues("ids");

		refEntCalledNumberService.deleteAll(ids);
		request.setAttribute("entCode", entCode);
		return mapping.findForward("show");
	}	
	
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("create ∫ÙΩ–∫≈¬Î");
		RefEntCalledNumberForm refEntCalledNumberForm=(RefEntCalledNumberForm)form;
		String entCode=refEntCalledNumberForm.getEntCode();
		String calledNumber=refEntCalledNumberForm.getCalledNumber();
		String numberName=refEntCalledNumberForm.getNumberName();
		
		TEntService entService=(TEntService)SpringHelper.getBean("tEntService");
		TEnt tEnt=entService.findByEntCode(entCode);
		
		RefEntCallednumber verRefEntCalledNumber=refEntCalledNumberService.findByCallednumber(calledNumber);
		if(verRefEntCalledNumber!=null){
			log.info("'"+calledNumber+"'¥À∫≈¬Î“—¥Ê‘⁄");
			refEntCalledNumberForm.setButtonName("‘ˆº”");
			request.setAttribute("entCode", entCode);
			request.setAttribute("msg", "'"+calledNumber+"'¥À∫≈¬Î“—¥Ê‘⁄");
			return mapping.findForward("init");
		}
		RefEntCallednumber refEntCalledNumber=new RefEntCallednumber();
		refEntCalledNumber.setCalledNumber(calledNumber);
		refEntCalledNumber.setInputdate(new Date());
		refEntCalledNumber.setNumberName(numberName);
		refEntCalledNumber.setTEnt(tEnt);
		refEntCalledNumberService.save(refEntCalledNumber);
		request.setAttribute("entCode", entCode);
		return mapping.findForward("show");
	}	
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("modify ∫ÙΩ–∫≈¬Î");
		RefEntCalledNumberForm refEntCalledNumberForm=(RefEntCalledNumberForm)form;
		refEntCalledNumberForm.setMethod("update");
		refEntCalledNumberForm.setButtonName("±£¥Ê");
		String id=request.getParameter("id");
		String calledNumber=refEntCalledNumberForm.getCalledNumber();
		String numberName=refEntCalledNumberForm.getNumberName();
		RefEntCallednumber refEntCallednumber= refEntCalledNumberService.findById(Long.valueOf(id));
		request.setAttribute("tEnt",refEntCallednumber.getTEnt());
		request.setAttribute("refEntCallednumber", refEntCallednumber);
		return mapping.findForward("modify");
	}
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("update ∫ÙΩ–∫≈¬Î");
		RefEntCalledNumberForm refEntCalledNumberForm=(RefEntCalledNumberForm)form;
		String id=request.getParameter("id");
		String entCode=refEntCalledNumberForm.getEntCode();
		String calledNumber=refEntCalledNumberForm.getCalledNumber();
		String numberName=refEntCalledNumberForm.getNumberName();
		
		RefEntCallednumber verRefEntCallednumber=refEntCalledNumberService.findByCallednumber(calledNumber);
		if(verRefEntCallednumber!=null){
			if(!verRefEntCallednumber.getId().toString().equals(id)){
				log.info("'"+calledNumber+"'¥À∫≈¬Î“—¥Ê‘⁄");
				refEntCalledNumberForm.setButtonName("±£¥Ê");
				request.setAttribute("entCode", entCode);
				request.setAttribute("msg", "'"+calledNumber+"'¥À∫≈¬Î“—¥Ê‘⁄");
				RefEntCallednumber returnRefEntCallednumber=new RefEntCallednumber();
				returnRefEntCallednumber.setId(Long.valueOf(id));
				returnRefEntCallednumber.setCalledNumber(calledNumber);
				returnRefEntCallednumber.setNumberName(numberName);
				request.setAttribute("refEntCallednumber", returnRefEntCallednumber);
		
				return mapping.findForward("init");	
			}
		}
		
		RefEntCallednumber refEntCallednumber= refEntCalledNumberService.findById(Long.valueOf(id));
		refEntCallednumber.setCalledNumber(calledNumber);
		refEntCallednumber.setNumberName(numberName);
		refEntCalledNumberService.update(refEntCallednumber);
		request.setAttribute("entCode", entCode);
		return mapping.findForward("show");
	}
	public ActionForward displayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return mapping.findForward("");
	}
}
