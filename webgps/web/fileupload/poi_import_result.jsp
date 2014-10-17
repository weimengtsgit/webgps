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
		String layerId = request.getParameter("layerID");// 图层id
		String poiImg = request.getParameter("poiImg");// 标注点样式
		String range = request.getParameter("range");// 范围
		
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
			
			
			//开始导入
			LayerAndPoiService layerAndPoiService = (LayerAndPoiService) SpringHelper
			.getBean("LayerAndPoiServiceImpl");		
			
			// excle 解析
			com.sosgps.wzt.fileUpload.util.AppPareExcel appPareExcel = new com.sosgps.wzt.fileUpload.util.AppPareExcel();
			// 设置bean
			appPareExcel.setPareExcel(new TPoiPareExeclImp());
			// 文件路径及名称
			appPareExcel.setFilePath(filePath);
			// 返回集合
			List<TPoi> list = appPareExcel.pareExcel(userInfo,poiImg,range);
			String[] deviceIds = {};
			for(int x=0 ; x<list.size(); x++){
				try {
				layerAndPoiService.addPoi(list.get(x),Long.parseLong(layerId),deviceIds);
				}catch (Exception ex) {
					loginError = "系统错误";
					//request.setAttribute("errorInfo", loginError);
					//response.setContentType("text/json; charset=utf-8");
					out.println("{success:true,msg:'导入标注失败!'}");
					return;
					//SysLogger.sysLogger.error("导入POI数据错误", ex);
					//return mapping.findForward(null);	
					
				}
			}
			loginDesc = "导入POI数据成功";
			//SysLogger.sysLogger.info(loginDesc);
			this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
			
			
			//response.setContentType("text/json; charset=utf-8");
			out.println("{success:true,msg:'导入标注成功!'}");
			//return mapping.findForward(null);
%>
