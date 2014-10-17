package com.sosgps.wzt.fileUpload.action;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sos.helper.SpringHelper;
import org.sos.web.action.DispatchWebActionSupport;
import com.sosgps.wzt.fileUpload.util.FileUpLoad;
import com.sosgps.wzt.fileUpload.util.TPoiPareExeclImp;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.RefLayerPoi;
import com.sosgps.wzt.orm.TLayers;
import com.sosgps.wzt.orm.TLoginLog;
import com.sosgps.wzt.orm.TPoi;
import com.sosgps.wzt.poi.service.LayerAndPoiService;
import com.sosgps.wzt.system.common.UserInfo;

public class FileUploadAction extends DispatchWebActionSupport  implements Serializable {

	TLoginLog tLoginLog = new TLoginLog(); // ��¼��־bean
	String loginDesc = "";
	String loginError = "";
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		return mapping.findForward("init");		
	}
	public ActionForward fileUpload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		UserInfo userInfo = this.getCurrentUser(request);
		String layerId = request.getParameter("layerID");// ͼ��id
			//�����ļ��ϴ�������
			FileUpLoad upload = new FileUpLoad(request);
			//�ϴ��ļ�����
			String[] fileType ={"xls","xlsx"};
			//�����ϴ��ļ�����
			upload.setFileType(fileType);
			//�ϴ��󷵻��ϴ��ļ����ƣ���Դ�ļ������ϼ���ϵͳʱ������
			String fileName = "";
			
			try{
				//�ϴ��ļ�����
				fileName = upload.process();
			}catch(Exception ex){
				//���ô�����Ϣ
				//request.setAttribute("errorInfo", "�ϴ��ļ������г��ִ���");
				
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write("{success:false,msg:'�ϴ��ļ������г��ִ���!'}");
				//request.setAttribute("errorInfo", "{success:false,msg:'�ϴ��ļ������г��ִ���!'}");
				//��ת������ҳ��
				return mapping.findForward(null);	
				
			
			}
			if(fileName == null){
				//���ô�����Ϣ
				//request.setAttribute("errorInfo", "�ļ����ʹ���");
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write("{success:false,msg:'�����ļ����ʹ���!'}");
				//request.setAttribute("errorInfo", "{success:false,msg:'�����ļ����ʹ���!'}");
				//��ת������ҳ��
				return mapping.findForward(null);	
				
			}
			
			String filePath = upload.getUploadPath();	
			//�ϴ��ļ�����
			filePath = filePath+File.separator+fileName;
			loginDesc = "�ϴ��ļ��ɹ����ϴ���ַ��"+filePath;
			request.setAttribute("filePath",filePath);
			SysLogger.sysLogger.info(loginDesc);
			
			//��ʼ����
			LayerAndPoiService layerAndPoiService = (LayerAndPoiService) SpringHelper
			.getBean("LayerAndPoiServiceImpl");		
			
			// excle ����
			com.sosgps.wzt.fileUpload.util.AppPareExcel appPareExcel = new com.sosgps.wzt.fileUpload.util.AppPareExcel();
			// ����bean
			appPareExcel.setPareExcel(new TPoiPareExeclImp());
			// �ļ�·��������
			appPareExcel.setFilePath(filePath);
			// ���ؼ���
			List<TPoi> list = appPareExcel.pareExcel(userInfo,"","");
			String[] deviceIds = {};
			for(int x=0 ; x<list.size(); x++){
				try {
				layerAndPoiService.addPoi(list.get(x),Long.parseLong(layerId),deviceIds);
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					//request.setAttribute("errorInfo", loginError);
					response.setContentType("text/json; charset=utf-8");
					response.getWriter().write("{success:false,msg:'�����עʧ��!'}");
					//SysLogger.sysLogger.error("����POI���ݴ���", ex);
					return mapping.findForward(null);	
					
				}
			}
			loginDesc = "����POI���ݳɹ�";
			//SysLogger.sysLogger.info(loginDesc);
			this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{success:true,msg:'�����ע�ɹ�!'}");
			return mapping.findForward(null);
			//this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
			//��תָ��ҳ��
			//return mapping.findForward("init");		
	}
	//�����ϴ��ļ���poi�㵼������
	public ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		UserInfo userInfo = this.getCurrentUser(request);
		LayerAndPoiService layerAndPoiService = (LayerAndPoiService) SpringHelper
		.getBean("LayerAndPoiServiceImpl");		
		String layerId = request.getParameter("layerID");// �ն�����
		String filePath = request.getParameter("filePath"); // �ļ�·��
		// excle ����
		com.sosgps.wzt.fileUpload.util.AppPareExcel appPareExcel = new com.sosgps.wzt.fileUpload.util.AppPareExcel();
		// ����bean
		appPareExcel.setPareExcel(new TPoiPareExeclImp());
		// �ļ�·��������
		appPareExcel.setFilePath(filePath);
		// ���ؼ���
		List<TPoi> list = appPareExcel.pareExcel(userInfo,"","");
		String[] deviceIds = {};
		for(int x=0 ; x<list.size(); x++){
			try {
			layerAndPoiService.addPoi(list.get(x),Long.parseLong(layerId),deviceIds);
			}catch (Exception ex) {
				loginError = "ϵͳ����";
				request.setAttribute("errorInfo", loginError);
				//SysLogger.sysLogger.error("����POI���ݴ���", ex);
				return mapping.findForward("");
				
			}
		}
		loginDesc = "����POI���ݳɹ�";
		//SysLogger.sysLogger.info(loginDesc);
		this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
		return mapping.findForward("init");	

	}
	private void doLoginLog(TLoginLog tLoginLog, UserInfo userInfo,
			String loginDesc, int result) {
		// ��¼��־��¼
		tLoginLog.setUserId(userInfo.getUserId());
		tLoginLog.setUserName(userInfo.getUserAccount());
		tLoginLog.setAccessIp(userInfo.getIp());
		tLoginLog.setEmpCode(userInfo.getEmpCode());
		tLoginLog.setResult(new Long(result));
		tLoginLog.setLoginDesc(loginDesc);
	}
	

}
