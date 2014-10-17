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

	TLoginLog tLoginLog = new TLoginLog(); // 登录日志bean
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
		String layerId = request.getParameter("layerID");// 图层id
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
				
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write("{success:false,msg:'上传文件过程中出现错误!'}");
				//request.setAttribute("errorInfo", "{success:false,msg:'上传文件过程中出现错误!'}");
				//跳转到错误页面
				return mapping.findForward(null);	
				
			
			}
			if(fileName == null){
				//设置错误信息
				//request.setAttribute("errorInfo", "文件类型错误");
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write("{success:false,msg:'导入文件类型错误!'}");
				//request.setAttribute("errorInfo", "{success:false,msg:'导入文件类型错误!'}");
				//跳转到错误页面
				return mapping.findForward(null);	
				
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
			List<TPoi> list = appPareExcel.pareExcel(userInfo,"","");
			String[] deviceIds = {};
			for(int x=0 ; x<list.size(); x++){
				try {
				layerAndPoiService.addPoi(list.get(x),Long.parseLong(layerId),deviceIds);
				}catch (Exception ex) {
					loginError = "系统错误";
					//request.setAttribute("errorInfo", loginError);
					response.setContentType("text/json; charset=utf-8");
					response.getWriter().write("{success:false,msg:'导入标注失败!'}");
					//SysLogger.sysLogger.error("导入POI数据错误", ex);
					return mapping.findForward(null);	
					
				}
			}
			loginDesc = "导入POI数据成功";
			//SysLogger.sysLogger.info(loginDesc);
			this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write("{success:true,msg:'导入标注成功!'}");
			return mapping.findForward(null);
			//this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
			//跳转指定页面
			//return mapping.findForward("init");		
	}
	//依据上传文件将poi点导入数据
	public ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		UserInfo userInfo = this.getCurrentUser(request);
		LayerAndPoiService layerAndPoiService = (LayerAndPoiService) SpringHelper
		.getBean("LayerAndPoiServiceImpl");		
		String layerId = request.getParameter("layerID");// 终端类型
		String filePath = request.getParameter("filePath"); // 文件路径
		// excle 解析
		com.sosgps.wzt.fileUpload.util.AppPareExcel appPareExcel = new com.sosgps.wzt.fileUpload.util.AppPareExcel();
		// 设置bean
		appPareExcel.setPareExcel(new TPoiPareExeclImp());
		// 文件路径及名称
		appPareExcel.setFilePath(filePath);
		// 返回集合
		List<TPoi> list = appPareExcel.pareExcel(userInfo,"","");
		String[] deviceIds = {};
		for(int x=0 ; x<list.size(); x++){
			try {
			layerAndPoiService.addPoi(list.get(x),Long.parseLong(layerId),deviceIds);
			}catch (Exception ex) {
				loginError = "系统错误";
				request.setAttribute("errorInfo", loginError);
				//SysLogger.sysLogger.error("导入POI数据错误", ex);
				return mapping.findForward("");
				
			}
		}
		loginDesc = "导入POI数据成功";
		//SysLogger.sysLogger.info(loginDesc);
		this.doLoginLog(tLoginLog, userInfo, loginDesc, 1);
		return mapping.findForward("init");	

	}
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
	

}
