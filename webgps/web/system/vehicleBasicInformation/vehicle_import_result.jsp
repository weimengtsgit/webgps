<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%@ page import="com.sosgps.wzt.annualExamination.service.AnnualExaminationService" %>
<%@ page import="com.sosgps.wzt.vehicleExpense.service.VehicleExpenseService" %>
<%@ page import="com.sosgps.wzt.toll.service.TollService" %>
<%@ page import="com.sosgps.wzt.vehiclesMaintenance.service.VehiclesMaintenanceService" %>
<%@ page import="com.sosgps.wzt.insurance.service.InsuranceService" %>
<%@ page import="com.sosgps.wzt.oiling.service.OilingService" %>
<%@ page import="com.sosgps.wzt.dispatchmoney.service.DispatchMoneyService" %>
<%@ page import="com.sosgps.wzt.fileUpload.util.TPoiPareExeclImp" %>
<%@ page import="com.sosgps.wzt.fileUpload.util.FileUpLoad" %>
<%@ page import="java.io.File" %>
<%@ page import="com.sosgps.wzt.orm.TLoginLog" %>
<%@ page import="com.sosgps.wzt.orm.TVehicleExpense" %>
<%@ page import="com.sosgps.wzt.orm.TToll" %>
<%@ page import="com.sosgps.wzt.orm.TVehiclesMaintenance" %>
<%@ page import="com.sosgps.wzt.orm.TInsurance" %>
<%@ page import="com.sosgps.wzt.orm.TOiling" %>
<%@ page import="com.sosgps.wzt.orm.TDispatchMoney" %>
<%@ page import="com.sosgps.wzt.log.SysLogger" %>
<%@ page import="com.sosgps.wzt.orm.TAnnualExamination" %>
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
			//��ʼ���복�������¼
			AnnualExaminationService annualExaminationService = (AnnualExaminationService) SpringHelper
			.getBean("AnnualExaminationServiceImpl");	
			//��ʼ������ü�¼֧��
			VehicleExpenseService vehicleExpenseService=(VehicleExpenseService) SpringHelper
					.getBean("VehicleExpenseServiceImpl");	
			//��ʼ��·��֧��
			TollService tollServiceService=(TollService) SpringHelper
					.getBean("TollServiceImpl");	
			//����ά����¼���
			VehiclesMaintenanceService vehiclesMaintenanceService=(VehiclesMaintenanceService) SpringHelper
					.getBean("VehiclesMaintenanceServiceImpl");
			//���շ��ü�¼���
			InsuranceService insuranceService = (InsuranceService) SpringHelper.getBean("InsuranceServiceImpl");
			//���ͼ�¼���
			OilingService oilingService = (OilingService) SpringHelper.getBean("OilingServiceImpl");
			//���ͽ�����
			DispatchMoneyService dispatchMoneyService = (DispatchMoneyService) SpringHelper.getBean("DispatchMoneyServiceImpl");
			// excle ����
			com.sosgps.wzt.fileUpload.util.AppPareExcel appPareExcel = new com.sosgps.wzt.fileUpload.util.AppPareExcel();
			// ����bean
			appPareExcel.setPareExcel(new TPoiPareExeclImp());
			// �ļ�·��������
			appPareExcel.setFilePath(filePath);
		
			// ���س��������¼����
			List<TAnnualExamination> list = appPareExcel.annualExamin(userInfo);
			for(int x=0 ; x<list.size(); x++){
				try {
					annualExaminationService.save(list.get(x));
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					out.println("{success:true,msg:'���복��������Ϣ����ʧ��!'}");
					return;
				}
			}
			// ���ط��ü�¼֧������
			List<TVehicleExpense> vehicleExpense = appPareExcel.vehicleExpense(userInfo);
			for(int x=0 ; x<vehicleExpense.size(); x++){
				try {
					vehicleExpenseService.save(vehicleExpense.get(x));
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					out.println("{success:true,msg:'���복��������Ϣ����ʧ��!'}");
					return;
				}
			}
			// ���ع�·��֧������
			List<TToll> addToll = appPareExcel.addToll(userInfo);
			for(int x=0 ; x<addToll.size(); x++){
				try {
					tollServiceService.save(addToll.get(x));
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					out.println("{success:true,msg:'���복��������Ϣ����ʧ��!'}");
					return;
				}
			}
			// ����ά����¼����
			List<TVehiclesMaintenance> addVehiclesMaintenance = appPareExcel.addVehiclesMaintenance(userInfo);
			for(int x=0 ; x<addToll.size(); x++){
				try {
					vehiclesMaintenanceService.save(addVehiclesMaintenance.get(x));
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					out.println("{success:true,msg:'���복��������Ϣ����ʧ��!'}");
					return;
				}
			}
			// ���շ��ü���
			List<TInsurance> addInsurance = appPareExcel.addInsurance(userInfo);
			for(int x=0 ; x<addInsurance.size(); x++){
				try {
					insuranceService.save(addInsurance.get(x));
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					out.println("{success:true,msg:'���복��������Ϣ����ʧ��!'}");
					return;
				}
			}
			// ���ͼ�¼����
			List<TOiling> addOiling = appPareExcel.addOiling(userInfo);
			for(int x=0 ; x<addOiling.size(); x++){
				try {
					oilingService.save(addOiling.get(x));
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					out.println("{success:true,msg:'���복��������Ϣ����ʧ��!'}");
					return;
				}
			}
			// ���ͽ���
			List<TDispatchMoney> addDispatchMoney = appPareExcel.addDispatchMoney(userInfo);
			for(int x=0 ; x<addDispatchMoney.size(); x++){
				try {
					dispatchMoneyService.save(addDispatchMoney.get(x));
				}catch (Exception ex) {
					loginError = "ϵͳ����";
					out.println("{success:true,msg:'���복��������Ϣ����ʧ��!'}");
					return;
				}
			}
			loginDesc = "���복��������Ϣ���ݳɹ�";
			//SysLogger.sysLogger.info(loginDesc);
			this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
			//response.setContentType("text/json; charset=utf-8");
			out.println("{success:true,msg:'���복��������Ϣ�ɹ�!'}");
			//return mapping.findForward(null);
%>
