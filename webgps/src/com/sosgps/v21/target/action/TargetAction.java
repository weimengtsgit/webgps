package com.sosgps.v21.target.action;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.sos.helper.SpringHelper;

import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.model.TargetTemplate;
import com.sosgps.v21.target.ColumeNumberException;
import com.sosgps.v21.target.ParseDataException;
import com.sosgps.v21.target.form.TargetForm;
import com.sosgps.v21.target.service.TargetService;
import com.sosgps.v21.util.DateUtils;
import com.sosgps.v21.util.TargetUtils;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.terminal.service.TerminalService;

public class TargetAction extends DispatchAction {

	private TargetService targetService = (TargetService) SpringHelper
			.getBean("targetService");

	/**
	 * ��ȡ����ά������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getRate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		List<Kpi> kpiList = targetService.getKpi(entCode, "1,2,3,4,5");
		response.getWriter().write(
				"{result:\"0\",data:["
						+ TargetUtils.convertRateFromObjectToJson(kpiList)
						+ "]}");// �ɹ�
		return mapping.findForward(null);
	}

	/**
	 * �޸�����ά������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateRate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String data = request.getParameter("data");// ����ά������

		try {
			List<Kpi> kpiList = TargetUtils.convertRateFromStringToObject(
					entCode, data);
			List<Kpi> kpiOldList = targetService.getKpi(entCode, "1,2,3,4,5");
			// ���
			if (kpiOldList == null || kpiOldList.size() < 1)
				targetService.addKpi(kpiList, userInfo.getUser());
			else
				targetService.updateKpi(kpiList, userInfo.getUser());
			response.getWriter().write("{result:\"0\"}");// �ɹ�
		} catch (ParseDataException e) {
			response.getWriter().write(
					"{result:\"-3\",desc:\"" + e.getMessage() + "\"}");// ���������쳣
		} catch (Exception e) {
			response.getWriter().write(
					"{result:\"-2\",desc:\"" + e.getMessage() + "\"}");// �����쳣
		}
		return mapping.findForward(null);
	}

	/**
	 * ��ȡģ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getTemplateType(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/html; charset=gbk");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		List<Kpi> kpiList = targetService.getKpi(entCode, 0);
		if (kpiList == null || kpiList.size() < 1) {
			response.getWriter().write("{success:false,info:\"ģ�����Ͳ�����\"}");// ģ�����Ͳ�����
			return mapping.findForward(null);
		}
		response.getWriter().write(
				"{success:true,info:" + kpiList.get(0).getValue() + "}");// �ɹ�
		return mapping.findForward(null);
	}

	/**
	 * ��ѯָ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/html; charset=gbk");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String vehicleNumber = request.getParameter("vehicleNumber");// Ա������
		vehicleNumber = URLDecoder.decode(vehicleNumber, "utf-8");
		Integer year = Integer.valueOf(request.getParameter("year"));// ���
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer limit = Integer.valueOf(request.getParameter("limit"));
		List<Kpi> kpiList = targetService.getKpi(entCode, 0);
		Integer type = null;// ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
		if (kpiList == null || kpiList.size() < 1) {
			response.getWriter().write("{success:false,info:\"ģ�����Ͳ�����\"}");// ģ�����Ͳ�����
			return mapping.findForward(null);
		}
		type = Integer.valueOf(kpiList.get(0).getValue());

		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);// ��ȡʱ�������б�,�����ṩ
		int count = targetService.getTargetCount(entCode, vehicleNumber, type,
				year);
		if (count > 0) {
			List<TargetTemplate> targetList = targetService.getTarget(entCode,
					vehicleNumber, type, year, start, limit);// Ŀ������
			String json = TargetUtils.convertTargetFromObjectToJson(dateList,
					targetList);
			response.getWriter().write(
					"{total:" + count + ",data:[" + json + "]}");
		} else {
			response.getWriter().write("{total:0,data:[]}");
		}

		return mapping.findForward(null);
	}
	
	/**
	 * ��ѯ��˾��ָ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author wangzhen
	 */
	public ActionForward getEntTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/html; charset=gbk");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Integer year = Integer.valueOf(request.getParameter("year"));// ���
		Integer start = Integer.valueOf(request.getParameter("start"));//��ҳ
		Integer limit = Integer.valueOf(request.getParameter("limit"));//��ҳ
		
		//��ȡģ������
		List<Kpi> kpiList = targetService.getKpi(entCode, 0);
		Integer type = null;// ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
		if (kpiList == null || kpiList.size() < 1) {
			response.getWriter().write("{success:false,info:\"ģ�����Ͳ�����\"}");// ģ�����Ͳ�����
			return mapping.findForward(null);
		}
		type = Integer.valueOf(kpiList.get(0).getValue());
		
		// ��ȡʱ�������б�,�����ṩ
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);
		
		//count ��ҳ��¼��
		int count = targetService.getEntTargetCount(entCode, type,year);
		if (count > 0) {
			//��ȡ��ҵ��������
			List tempList = targetService.getEntTarget(entCode,
					 type, year, start, limit);
			//����ҵ�������ݽ���JSON��װ������ҳ����ʾ
			String json = TargetUtils.convertEntTargetFromObjectToJson(dateList,
					tempList,year);
			response.getWriter().write(
					"{total:" + count + ",data:[" + json + "]}");
		} else {
			response.getWriter().write("{total:0,data:[]}");
		}

		return mapping.findForward(null);
	}
    
	

	/**
	 * �޸�ָ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/html; charset=gbk");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}
		String data = request.getParameter("data");// Ŀ������

		try {
			List<TargetTemplate> targetList = TargetUtils
					.convertTargetFromStringToObject(data);

			// ���
			targetService.updateTarget(targetList, userInfo.getUser());
			response.getWriter().write("{success:true,info:\"����ɹ�\"}");// �ɹ�
		} catch (ParseDataException e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// ���������쳣
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// �����쳣
		}

		return mapping.findForward(null);
	}
	
	/**
	 * �޸Ĺ�˾ָ������:
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author wangzhen
	 */
	public ActionForward updateEntTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/html; charset=gbk");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}

		String entCode = userInfo.getEmpCode(); //��ҵ����
		List<Kpi> kpiList = targetService.getKpi(entCode, 0);
		Integer type = null;// ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
		if (kpiList == null || kpiList.size() < 1) {
			response.getWriter().write("{success:false,info:\"ģ�����Ͳ�����\"}");// ģ�����Ͳ�����
			return mapping.findForward(null);
		}
		type = Integer.valueOf(kpiList.get(0).getValue());//ģ������
		
		String data = request.getParameter("data");// Ŀ������

		try {
			//��Ŀ�����ݽ����ɶ���
			List<TargetTemplate> targetList = TargetUtils
					.convertEntTargetFromStringToObject(data,type,entCode);
			
			if(targetList.size() == 0) {
				response.getWriter().write("{success:true,info:\"û��Ҫ���µ�����\"}");
			} else {
				TerminalService terminalService = (TerminalService) SpringHelper
						.getBean("tTargetObjectService");
				//������ҵ��ȡ���е��նˣ�
				List<TTerminal> tterminals = terminalService.findAllTerminal(entCode);
				//��ҵ��ȡ���е��ն�������
				int count = tterminals.size();
				//��װ��Ҫ����ҵָ��ƽ�������ÿ��Ա����ָ��
				List<TargetTemplate> tts = TargetUtils.getTargetTemplate(tterminals);
				
				//��ҵָ��ƽ������
				for(TargetTemplate t : targetList) {
					//���µ�ָ����н�һ��������
					List<TargetTemplate> fullTargets = TargetUtils.fillTarget(entCode,type,t.getYear(),t.getTargetOn(),tts);
					List<TargetTemplate> avgTargets = TargetUtils.avgEntTarget(fullTargets,count,t);
					// ���
					targetService.updateEntTarget(avgTargets,userInfo.getUser());
				}
			}
			
			response.getWriter().write("{success:true,info:\"����ɹ�\"}");// �ɹ�
		} catch (ParseDataException e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// ���������쳣
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// �����쳣
		}

		return mapping.findForward(null);
	}

	/**
	 * ����ģ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward downloadTemplate(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			request.setAttribute("error", "�û�δ��¼!");
			return mapping.findForward("error");
		}
		String entCode = userInfo.getEmpCode();
		Integer type = Integer.valueOf(request.getParameter("type"));// ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
		Integer year = Integer.valueOf(request.getParameter("year"));// ���
		List<TTerminal> terminalList = targetService
				.getTerminalAndGroup(entCode);// �ն�ID,Ա������,�ֻ�����,��������
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);// ��ȡʱ�������б�,�����ṩ
		Map<String, TargetTemplate> targetMap = targetService.getTarget(
				entCode, type, year);// ��ʷ����
		Workbook excel = TargetUtils.convertTargetFromObjectToExcel(
				terminalList, dateList, targetMap, type, XSSFWorkbook.class);

		// ����
		response.setHeader("Content-Disposition",
				"attachment; filename=template.xlsx");
		ServletOutputStream os = response.getOutputStream();
		excel.write(os);
		os.close();
		return mapping.findForward(null);
	}
	
	/**
	 * ָ�깫˾����ģ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author wangzhen
	 */
	public ActionForward downloadEntTemplate(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			request.setAttribute("error", "�û�δ��¼!");
			return mapping.findForward("error");
		}
		String entCode = userInfo.getEmpCode();
		Integer type = Integer.valueOf(request.getParameter("type"));// ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
		Integer year = Integer.valueOf(request.getParameter("year"));// ���
		
		//ģ�������漰����
		Map<String,Object[]> targetData = targetService.getEntTarget(entCode,type, year);
				 
		// ��ȡʱ�������б�,�����ṩ
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);
		
		//��ȡ���ص�excel 
		Workbook excel = TargetUtils.convertEntTargetFromObjectToExcel(
				targetData, dateList, type, XSSFWorkbook.class);

		// ����
		response.setHeader("Content-Disposition",
				"attachment; filename=template.xlsx");
		ServletOutputStream os = response.getOutputStream();
		excel.write(os);
		os.close();
		return mapping.findForward(null);
	}

	/**
	 * ����Ŀ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward uploadTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/html; charset=gbk");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}

		String entCode = userInfo.getEmpCode();
		TargetForm targetForm = (TargetForm) form;
		Integer type = Integer.valueOf(request.getParameter("type"));// ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
		Integer year = Integer.valueOf(request.getParameter("year"));// ���
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);// ��ȡʱ�������б�,���ṩ
		FormFile userfile = targetForm.getTargetFile(); // Ŀ�������ļ�
		String fileName = userfile.getFileName();
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			response.getWriter().write(
					"{success:false,info:\"�ļ���ʽ������ѡ��Excel�ļ�����\"}");// �ļ���ʽ����
			return mapping.findForward(null);
		}
		Workbook wb = null;
		try {
			TerminalService terminalService = (TerminalService) SpringHelper
					.getBean("tTargetObjectService");
			//������ҵ��ȡ���е��նˣ�
			List<TTerminal> tterminals = terminalService.findAllTerminal(entCode);
			//��ҵ��ȡ���е��ն�������
			int count = tterminals.size();
			
			//��ҵ�˻������룬��û������ն�
			if(0 == count) {
				response.getWriter().write(
						"{success:false,info:\"����δ����նˣ��������ն˹���������ն�\"}");// �ļ���ʽ����
				return mapping.findForward(null);
			}
			if(fileName.endsWith(".xls")) {
				 wb = new HSSFWorkbook(userfile.getInputStream());
			} else {
			     wb = new XSSFWorkbook(userfile.getInputStream());
			}
			//�ڽ���֮ǰ���ȼ��Excel�ļ�
			TargetUtils.checkUploadExcelFile(wb,dateList,type,year,entCode);
			
			List<TargetTemplate> targetList = TargetUtils
					.convertTargetFromExcelToObject(wb, dateList, type, year);
			userfile.destroy();
            
			//addby wangzhen ���Excel�е�һ��sheetΪ�գ�����û���ʾ
			if(targetList.size() == 0) {
            	response.getWriter().write(
    					"{success:false,info:\"���ݱ����ڵ����Excel�ĵ�һ��sheet��\"}");
    			return mapping.findForward(null);
            }
			// ���
			targetService.importTarget(entCode, type, year, targetList,
					userInfo.getUser());
			response.getWriter().write("{success:true,info:\"����ɹ�\"}");// �ɹ�
		} catch (ParseDataException e) {
			if (e instanceof ColumeNumberException)
				response.getWriter().write(
						"{success:false,info:\"" + e.getMessage() + "\"}");// �����ļ���������ģ�治һ��
			else
				response.getWriter().write(
						"{success:false,info:\"" + e.getMessage() + "\"}");// ����excel�쳣
		} catch (Exception e) {
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// �����쳣
		}
		return mapping.findForward(null);
	}
	
	/**
	 * ����˾�����Excel�ļ��ϴ����н������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author wangzhen
	 */
	public ActionForward uploadEntTarget(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/html; charset=gbk");
		if (userInfo == null) {
			response.getWriter().write("{success:false,info:\"�û�δ��¼!\"}");// δ��¼
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		
		TargetForm targetForm = (TargetForm) form;
		Integer type = Integer.valueOf(request.getParameter("type"));// ģ������,0-��ģ��,1-Ѯģ��,2-��ģ��
		Integer year = Integer.valueOf(request.getParameter("year"));// ���
		
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);// ��ȡʱ�������б�,���ṩ
		FormFile userfile = targetForm.getTargetFile(); // Ŀ�������ļ�
		String fileName = userfile.getFileName();
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			response.getWriter().write(
					"{success:false,info:\"�ļ���ʽ������ѡ��Excel�ļ�����\"}");// �ļ���ʽ����
			return mapping.findForward(null);
		}
		
		Workbook wb = null;
		try {
			TerminalService terminalService = (TerminalService) SpringHelper
					.getBean("tTargetObjectService");
			//������ҵ��ȡ���е��նˣ�
			List<TTerminal> tterminals = terminalService.findAllTerminal(entCode);
			//��ҵ��ȡ���е��ն�������
			int count = tterminals.size();
			
			//��ҵ�˻������룬��û������ն�
			if(0 == count) {
				response.getWriter().write(
						"{success:false,info:\"����δ����նˣ��������ն˹���������ն�\"}");// �ļ���ʽ����
				return mapping.findForward(null);
			}
			
			if(fileName.endsWith(".xls")) {
				 wb = new HSSFWorkbook(userfile.getInputStream());
			} else {
			    wb = new XSSFWorkbook(userfile.getInputStream());
			}
			/**start*/
			//����
			List<TargetTemplate> targetList = TargetUtils
					.convertEntTargetFromExcelToObject(wb, dateList, type, year,entCode);
			
			//addby wangzhen ���Excel�е�һ��sheetΪ�գ�����û���ʾ
			if(targetList.size() == 0) {
            	response.getWriter().write(
    					"{success:false,info:\"���ݱ����ڵ����Excel�ĵ�һ��sheet��\"}");
    			return mapping.findForward(null);
            }
			//�ϴ����ļ�ɾ��
			userfile.destroy();
			
			//��װ��Ҫ����ҵָ��ƽ�������ÿ��Ա����ָ��
			List<TargetTemplate> tts = TargetUtils.getTargetTemplate(tterminals);
			//��ҵָ��ƽ������
			for(TargetTemplate t : targetList) {
				//���µ�ָ����н�һ��������
				List<TargetTemplate> fullTargets = TargetUtils.fillTarget(entCode,type,t.getYear(),t.getTargetOn(),tts);
				List<TargetTemplate> avgTargets = TargetUtils.avgEntTarget(fullTargets,count,t);
				// ���
				targetService.importTarget(entCode, type, year, avgTargets,userInfo.getUser());
			}
			/**end*/
			response.getWriter().write("{success:true,info:\"����ɹ�\"}");// �ɹ�
		} catch (ParseDataException e) {
			if (e instanceof ColumeNumberException)
				response.getWriter().write(
						"{success:false,info:\"" + e.getMessage() + "\"}");// �����ļ���������ģ�治һ��
			else
				response.getWriter().write(
						"{success:false,info:\"" + e.getMessage() + "\"}");// ����excel�쳣
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// �����쳣
		}
		return mapping.findForward(null);
	}
}
