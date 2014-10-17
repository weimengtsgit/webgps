<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%@ page import="com.sosgps.wzt.poi.service.LayerAndPoiService" %>
<%@ page import="com.sosgps.wzt.fileUpload.util.TPoiPareExeclImp" %>
<%@ page import="com.sosgps.wzt.fileUpload.util.FileUpLoad" %>
<%@ page import="java.io.File" %>
<%@ page import="com.sosgps.wzt.orm.TLoginLog" %>
<%@ page import="com.sosgps.wzt.log.SysLogger" %>
<%@ page import="com.sosgps.wzt.orm.TPoi" %>
<%@ page import="java.util.List" %>
<%@ page import="org.sos.helper.SpringHelper" %>
<%!
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
%>
<%
String path = request.getContextPath();
UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
if(userInfo==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
TLoginLog tLoginLog = new TLoginLog(); // ��¼��־bean
	String loginDesc = "";
	String loginError = "";
		String layerId = request.getParameter("layerID");// ͼ��id
		String poiImg = request.getParameter("poiImg");// ��ע����ʽ
		String range = request.getParameter("range");// ��Χ
		
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
				
				//response.setContentType("text/json; charset=utf-8");
				out.println("{success:true,msg:'�ϴ��ļ������г��ִ���!'}");
				return;
				//request.setAttribute("errorInfo", "{success:false,msg:'�ϴ��ļ������г��ִ���!'}");
				//��ת������ҳ��
				
			}
			if(fileName == null){
				//���ô�����Ϣ
				//request.setAttribute("errorInfo", "�ļ����ʹ���");
				//response.setContentType("text/json; charset=utf-8");
				out.println("{success:true,msg:'�����ļ����ʹ���,��ѡ��excel�ļ�!'}");
				return;
				//request.setAttribute("errorInfo", "{success:false,msg:'�����ļ����ʹ���!'}");
				//��ת������ҳ��
				//return mapping.findForward(null);	
				
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
			List<TPoi> list = appPareExcel.pareExcel(userInfo,poiImg,range);
			String[] deviceIds = {};
			for(int x=0 ; x<list.size(); x++){
				try {
				layerAndPoiService.addPoi(list.get(x),Long.parseLong(layerId),deviceIds);
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					//request.setAttribute("errorInfo", loginError);
					//response.setContentType("text/json; charset=utf-8");
					out.println("{success:true,msg:'�����עʧ��!'}");
					return;
					//SysLogger.sysLogger.error("����POI���ݴ���", ex);
					//return mapping.findForward(null);	
					
				}
			}
			loginDesc = "����POI���ݳɹ�";
			//SysLogger.sysLogger.info(loginDesc);
			this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
			
			
			//response.setContentType("text/json; charset=utf-8");
			out.println("{success:true,msg:'�����ע�ɹ�!'}");
			//return mapping.findForward(null);
%>
