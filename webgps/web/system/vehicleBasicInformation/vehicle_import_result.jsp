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
		// 登录日志记录
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
TLoginLog tLoginLog = new TLoginLog(); // 登录日志bean
	String loginDesc = "";
	String loginError = "";
			//构造文件上传工具类
			FileUpLoad upload = new FileUpLoad(request);
			//上传文件类型
			String[] fileType ={"xls","xlsx"};
			//设置上传文件类型
			upload.setFileType(fileType);
			//上传后返回上传文件名称（在源文件基础上加入系统时间数）
			String fileName = "";
			
			try{
				//上传文件处理
				fileName = upload.process();
			}catch(Exception ex){
				//设置错误信息
				//request.setAttribute("errorInfo", "上传文件过程中出现错误");
				
				//response.setContentType("text/json; charset=utf-8");
				out.println("{success:true,msg:'上传文件过程中出现错误!'}");
				return;
				//request.setAttribute("errorInfo", "{success:false,msg:'上传文件过程中出现错误!'}");
				//跳转到错误页面
				
			}
			if(fileName == null){
				//设置错误信息
				//request.setAttribute("errorInfo", "文件类型错误");
				//response.setContentType("text/json; charset=utf-8");
				out.println("{success:true,msg:'导入文件类型错误,请选择excel文件!'}");
				return;
				//request.setAttribute("errorInfo", "{success:false,msg:'导入文件类型错误!'}");
				//跳转到错误页面
				//return mapping.findForward(null);	
				
			}
			String filePath = upload.getUploadPath();	
			//上传文件名称
			filePath = filePath+File.separator+fileName;
			loginDesc = "上传文件成功，上传地址："+filePath;
			request.setAttribute("filePath",filePath);
			SysLogger.sysLogger.info(loginDesc);
			//开始导入车辆年审记录
			AnnualExaminationService annualExaminationService = (AnnualExaminationService) SpringHelper
			.getBean("AnnualExaminationServiceImpl");	
			//开始导入费用记录支出
			VehicleExpenseService vehicleExpenseService=(VehicleExpenseService) SpringHelper
					.getBean("VehicleExpenseServiceImpl");	
			//开始过路费支出
			TollService tollServiceService=(TollService) SpringHelper
					.getBean("TollServiceImpl");	
			//车辆维护记录添加
			VehiclesMaintenanceService vehiclesMaintenanceService=(VehiclesMaintenanceService) SpringHelper
					.getBean("VehiclesMaintenanceServiceImpl");
			//保险费用记录添加
			InsuranceService insuranceService = (InsuranceService) SpringHelper.getBean("InsuranceServiceImpl");
			//加油记录添加
			OilingService oilingService = (OilingService) SpringHelper.getBean("OilingServiceImpl");
			//配送金额添加
			DispatchMoneyService dispatchMoneyService = (DispatchMoneyService) SpringHelper.getBean("DispatchMoneyServiceImpl");
			// excle 解析
			com.sosgps.wzt.fileUpload.util.AppPareExcel appPareExcel = new com.sosgps.wzt.fileUpload.util.AppPareExcel();
			// 设置bean
			appPareExcel.setPareExcel(new TPoiPareExeclImp());
			// 文件路径及名称
			appPareExcel.setFilePath(filePath);
		
			// 返回车辆年审记录集合
			List<TAnnualExamination> list = appPareExcel.annualExamin(userInfo);
			for(int x=0 ; x<list.size(); x++){
				try {
					annualExaminationService.save(list.get(x));
				}catch (Exception ex) {
					loginError = "系统错误";
					out.println("{success:true,msg:'导入车辆基本信息数据失败!'}");
					return;
				}
			}
			// 返回费用记录支出集合
			List<TVehicleExpense> vehicleExpense = appPareExcel.vehicleExpense(userInfo);
			for(int x=0 ; x<vehicleExpense.size(); x++){
				try {
					vehicleExpenseService.save(vehicleExpense.get(x));
				}catch (Exception ex) {
					loginError = "系统错误";
					out.println("{success:true,msg:'导入车辆基本信息数据失败!'}");
					return;
				}
			}
			// 返回过路费支出集合
			List<TToll> addToll = appPareExcel.addToll(userInfo);
			for(int x=0 ; x<addToll.size(); x++){
				try {
					tollServiceService.save(addToll.get(x));
				}catch (Exception ex) {
					loginError = "系统错误";
					out.println("{success:true,msg:'导入车辆基本信息数据失败!'}");
					return;
				}
			}
			// 车辆维护记录集合
			List<TVehiclesMaintenance> addVehiclesMaintenance = appPareExcel.addVehiclesMaintenance(userInfo);
			for(int x=0 ; x<addToll.size(); x++){
				try {
					vehiclesMaintenanceService.save(addVehiclesMaintenance.get(x));
				}catch (Exception ex) {
					loginError = "系统错误";
					out.println("{success:true,msg:'导入车辆基本信息数据失败!'}");
					return;
				}
			}
			// 保险费用集合
			List<TInsurance> addInsurance = appPareExcel.addInsurance(userInfo);
			for(int x=0 ; x<addInsurance.size(); x++){
				try {
					insuranceService.save(addInsurance.get(x));
				}catch (Exception ex) {
					loginError = "系统错误";
					out.println("{success:true,msg:'导入车辆基本信息数据失败!'}");
					return;
				}
			}
			// 加油记录集合
			List<TOiling> addOiling = appPareExcel.addOiling(userInfo);
			for(int x=0 ; x<addOiling.size(); x++){
				try {
					oilingService.save(addOiling.get(x));
				}catch (Exception ex) {
					loginError = "系统错误";
					out.println("{success:true,msg:'导入车辆基本信息数据失败!'}");
					return;
				}
			}
			// 配送金额集合
			List<TDispatchMoney> addDispatchMoney = appPareExcel.addDispatchMoney(userInfo);
			for(int x=0 ; x<addDispatchMoney.size(); x++){
				try {
					dispatchMoneyService.save(addDispatchMoney.get(x));
				}catch (Exception ex) {
					loginError = "系统错误";
					out.println("{success:true,msg:'导入车辆基本信息数据失败!'}");
					return;
				}
			}
			loginDesc = "导入车辆基本信息数据成功";
			//SysLogger.sysLogger.info(loginDesc);
			this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
			//response.setContentType("text/json; charset=utf-8");
			out.println("{success:true,msg:'导入车辆基本信息成功!'}");
			//return mapping.findForward(null);
%>
