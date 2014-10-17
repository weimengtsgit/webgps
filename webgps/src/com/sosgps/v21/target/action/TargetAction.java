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
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = (HttpSession) request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		response.setContentType("text/json; charset=utf-8");
		if (userInfo == null) {
			response.getWriter().write("{result:\"-1\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		List<Kpi> kpiList = targetService.getKpi(entCode, "1,2,3,4,5");
		response.getWriter().write(
				"{result:\"0\",data:["
						+ TargetUtils.convertRateFromObjectToJson(kpiList)
						+ "]}");// 成功
		return mapping.findForward(null);
	}

	/**
	 * 修改区间维护数据
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
			response.getWriter().write("{result:\"-1\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String data = request.getParameter("data");// 区间维护数据

		try {
			List<Kpi> kpiList = TargetUtils.convertRateFromStringToObject(
					entCode, data);
			List<Kpi> kpiOldList = targetService.getKpi(entCode, "1,2,3,4,5");
			// 入库
			if (kpiOldList == null || kpiOldList.size() < 1)
				targetService.addKpi(kpiList, userInfo.getUser());
			else
				targetService.updateKpi(kpiList, userInfo.getUser());
			response.getWriter().write("{result:\"0\"}");// 成功
		} catch (ParseDataException e) {
			response.getWriter().write(
					"{result:\"-3\",desc:\"" + e.getMessage() + "\"}");// 解析数据异常
		} catch (Exception e) {
			response.getWriter().write(
					"{result:\"-2\",desc:\"" + e.getMessage() + "\"}");// 其他异常
		}
		return mapping.findForward(null);
	}

	/**
	 * 获取模板类型
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
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		List<Kpi> kpiList = targetService.getKpi(entCode, 0);
		if (kpiList == null || kpiList.size() < 1) {
			response.getWriter().write("{success:false,info:\"模板类型不存在\"}");// 模板类型不存在
			return mapping.findForward(null);
		}
		response.getWriter().write(
				"{success:true,info:" + kpiList.get(0).getValue() + "}");// 成功
		return mapping.findForward(null);
	}

	/**
	 * 查询指标数据
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
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		String vehicleNumber = request.getParameter("vehicleNumber");// 员工姓名
		vehicleNumber = URLDecoder.decode(vehicleNumber, "utf-8");
		Integer year = Integer.valueOf(request.getParameter("year"));// 年份
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer limit = Integer.valueOf(request.getParameter("limit"));
		List<Kpi> kpiList = targetService.getKpi(entCode, 0);
		Integer type = null;// 模板类型,0-周模板,1-旬模板,2-月模板
		if (kpiList == null || kpiList.size() < 1) {
			response.getWriter().write("{success:false,info:\"模板类型不存在\"}");// 模板类型不存在
			return mapping.findForward(null);
		}
		type = Integer.valueOf(kpiList.get(0).getValue());

		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);// 获取时间粒度列表,王超提供
		int count = targetService.getTargetCount(entCode, vehicleNumber, type,
				year);
		if (count > 0) {
			List<TargetTemplate> targetList = targetService.getTarget(entCode,
					vehicleNumber, type, year, start, limit);// 目标数据
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
	 * 查询公司级指标数据
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
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		Integer year = Integer.valueOf(request.getParameter("year"));// 年份
		Integer start = Integer.valueOf(request.getParameter("start"));//分页
		Integer limit = Integer.valueOf(request.getParameter("limit"));//分页
		
		//获取模板类型
		List<Kpi> kpiList = targetService.getKpi(entCode, 0);
		Integer type = null;// 模板类型,0-周模板,1-旬模板,2-月模板
		if (kpiList == null || kpiList.size() < 1) {
			response.getWriter().write("{success:false,info:\"模板类型不存在\"}");// 模板类型不存在
			return mapping.findForward(null);
		}
		type = Integer.valueOf(kpiList.get(0).getValue());
		
		// 获取时间粒度列表,王超提供
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);
		
		//count 分页记录用
		int count = targetService.getEntTargetCount(entCode, type,year);
		if (count > 0) {
			//获取企业级的数据
			List tempList = targetService.getEntTarget(entCode,
					 type, year, start, limit);
			//将企业级的数据进行JSON封装，用于页面显示
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
	 * 修改指标数据
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
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String data = request.getParameter("data");// 目标数据

		try {
			List<TargetTemplate> targetList = TargetUtils
					.convertTargetFromStringToObject(data);

			// 入库
			targetService.updateTarget(targetList, userInfo.getUser());
			response.getWriter().write("{success:true,info:\"保存成功\"}");// 成功
		} catch (ParseDataException e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// 解析数据异常
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// 其他异常
		}

		return mapping.findForward(null);
	}
	
	/**
	 * 修改公司指标数据:
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
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}

		String entCode = userInfo.getEmpCode(); //企业代码
		List<Kpi> kpiList = targetService.getKpi(entCode, 0);
		Integer type = null;// 模板类型,0-周模板,1-旬模板,2-月模板
		if (kpiList == null || kpiList.size() < 1) {
			response.getWriter().write("{success:false,info:\"模板类型不存在\"}");// 模板类型不存在
			return mapping.findForward(null);
		}
		type = Integer.valueOf(kpiList.get(0).getValue());//模板类型
		
		String data = request.getParameter("data");// 目标数据

		try {
			//将目标数据解析成对象
			List<TargetTemplate> targetList = TargetUtils
					.convertEntTargetFromStringToObject(data,type,entCode);
			
			if(targetList.size() == 0) {
				response.getWriter().write("{success:true,info:\"没有要更新的数据\"}");
			} else {
				TerminalService terminalService = (TerminalService) SpringHelper
						.getBean("tTargetObjectService");
				//根据企业获取所有的终端：
				List<TTerminal> tterminals = terminalService.findAllTerminal(entCode);
				//企业获取所有的终端总数：
				int count = tterminals.size();
				//封装需要将企业指标平均分配给每个员工的指标
				List<TargetTemplate> tts = TargetUtils.getTargetTemplate(tterminals);
				
				//企业指标平均分配
				for(TargetTemplate t : targetList) {
					//对新的指标进行进一步的完善
					List<TargetTemplate> fullTargets = TargetUtils.fillTarget(entCode,type,t.getYear(),t.getTargetOn(),tts);
					List<TargetTemplate> avgTargets = TargetUtils.avgEntTarget(fullTargets,count,t);
					// 入库
					targetService.updateEntTarget(avgTargets,userInfo.getUser());
				}
			}
			
			response.getWriter().write("{success:true,info:\"保存成功\"}");// 成功
		} catch (ParseDataException e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// 解析数据异常
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// 其他异常
		}

		return mapping.findForward(null);
	}

	/**
	 * 下载模板
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
			request.setAttribute("error", "用户未登录!");
			return mapping.findForward("error");
		}
		String entCode = userInfo.getEmpCode();
		Integer type = Integer.valueOf(request.getParameter("type"));// 模板类型,0-周模板,1-旬模板,2-月模板
		Integer year = Integer.valueOf(request.getParameter("year"));// 年份
		List<TTerminal> terminalList = targetService
				.getTerminalAndGroup(entCode);// 终端ID,员工姓名,手机号码,所属部门
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);// 获取时间粒度列表,王超提供
		Map<String, TargetTemplate> targetMap = targetService.getTarget(
				entCode, type, year);// 历史数据
		Workbook excel = TargetUtils.convertTargetFromObjectToExcel(
				terminalList, dateList, targetMap, type, XSSFWorkbook.class);

		// 下载
		response.setHeader("Content-Disposition",
				"attachment; filename=template.xlsx");
		ServletOutputStream os = response.getOutputStream();
		excel.write(os);
		os.close();
		return mapping.findForward(null);
	}
	
	/**
	 * 指标公司级别模板下载
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
			request.setAttribute("error", "用户未登录!");
			return mapping.findForward("error");
		}
		String entCode = userInfo.getEmpCode();
		Integer type = Integer.valueOf(request.getParameter("type"));// 模板类型,0-周模板,1-旬模板,2-月模板
		Integer year = Integer.valueOf(request.getParameter("year"));// 年份
		
		//模板下载涉及数据
		Map<String,Object[]> targetData = targetService.getEntTarget(entCode,type, year);
				 
		// 获取时间粒度列表,王超提供
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);
		
		//获取下载的excel 
		Workbook excel = TargetUtils.convertEntTargetFromObjectToExcel(
				targetData, dateList, type, XSSFWorkbook.class);

		// 下载
		response.setHeader("Content-Disposition",
				"attachment; filename=template.xlsx");
		ServletOutputStream os = response.getOutputStream();
		excel.write(os);
		os.close();
		return mapping.findForward(null);
	}

	/**
	 * 导入目标数据
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
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}

		String entCode = userInfo.getEmpCode();
		TargetForm targetForm = (TargetForm) form;
		Integer type = Integer.valueOf(request.getParameter("type"));// 模板类型,0-周模板,1-旬模板,2-月模板
		Integer year = Integer.valueOf(request.getParameter("year"));// 年份
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);// 获取时间粒度列表,超提供
		FormFile userfile = targetForm.getTargetFile(); // 目标数据文件
		String fileName = userfile.getFileName();
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			response.getWriter().write(
					"{success:false,info:\"文件格式错误，请选择Excel文件导入\"}");// 文件格式错误
			return mapping.findForward(null);
		}
		Workbook wb = null;
		try {
			TerminalService terminalService = (TerminalService) SpringHelper
					.getBean("tTargetObjectService");
			//根据企业获取所有的终端：
			List<TTerminal> tterminals = terminalService.findAllTerminal(entCode);
			//企业获取所有的终端总数：
			int count = tterminals.size();
			
			//企业账户刚申请，但没有添加终端
			if(0 == count) {
				response.getWriter().write(
						"{success:false,info:\"您还未添加终端，请先在终端管理中添加终端\"}");// 文件格式错误
				return mapping.findForward(null);
			}
			if(fileName.endsWith(".xls")) {
				 wb = new HSSFWorkbook(userfile.getInputStream());
			} else {
			     wb = new XSSFWorkbook(userfile.getInputStream());
			}
			//在解析之前首先检查Excel文件
			TargetUtils.checkUploadExcelFile(wb,dateList,type,year,entCode);
			
			List<TargetTemplate> targetList = TargetUtils
					.convertTargetFromExcelToObject(wb, dateList, type, year);
			userfile.destroy();
            
			//addby wangzhen 如果Excel中第一个sheet为空，则给用户提示
			if(targetList.size() == 0) {
            	response.getWriter().write(
    					"{success:false,info:\"数据必须在导入的Excel的第一个sheet中\"}");
    			return mapping.findForward(null);
            }
			// 入库
			targetService.importTarget(entCode, type, year, targetList,
					userInfo.getUser());
			response.getWriter().write("{success:true,info:\"保存成功\"}");// 成功
		} catch (ParseDataException e) {
			if (e instanceof ColumeNumberException)
				response.getWriter().write(
						"{success:false,info:\"" + e.getMessage() + "\"}");// 导入文件的列数与模版不一致
			else
				response.getWriter().write(
						"{success:false,info:\"" + e.getMessage() + "\"}");// 解析excel异常
		} catch (Exception e) {
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// 其他异常
		}
		return mapping.findForward(null);
	}
	
	/**
	 * 将公司级别的Excel文件上传进行解析入库
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
			response.getWriter().write("{success:false,info:\"用户未登录!\"}");// 未登录
			return mapping.findForward(null);
		}
		String entCode = userInfo.getEmpCode();
		
		TargetForm targetForm = (TargetForm) form;
		Integer type = Integer.valueOf(request.getParameter("type"));// 模板类型,0-周模板,1-旬模板,2-月模板
		Integer year = Integer.valueOf(request.getParameter("year"));// 年份
		
		List<Map<String, Object>> dateList = DateUtils.getTemplate(type, year);// 获取时间粒度列表,超提供
		FormFile userfile = targetForm.getTargetFile(); // 目标数据文件
		String fileName = userfile.getFileName();
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			response.getWriter().write(
					"{success:false,info:\"文件格式错误，请选择Excel文件导入\"}");// 文件格式错误
			return mapping.findForward(null);
		}
		
		Workbook wb = null;
		try {
			TerminalService terminalService = (TerminalService) SpringHelper
					.getBean("tTargetObjectService");
			//根据企业获取所有的终端：
			List<TTerminal> tterminals = terminalService.findAllTerminal(entCode);
			//企业获取所有的终端总数：
			int count = tterminals.size();
			
			//企业账户刚申请，但没有添加终端
			if(0 == count) {
				response.getWriter().write(
						"{success:false,info:\"您还未添加终端，请先在终端管理中添加终端\"}");// 文件格式错误
				return mapping.findForward(null);
			}
			
			if(fileName.endsWith(".xls")) {
				 wb = new HSSFWorkbook(userfile.getInputStream());
			} else {
			    wb = new XSSFWorkbook(userfile.getInputStream());
			}
			/**start*/
			//解析
			List<TargetTemplate> targetList = TargetUtils
					.convertEntTargetFromExcelToObject(wb, dateList, type, year,entCode);
			
			//addby wangzhen 如果Excel中第一个sheet为空，则给用户提示
			if(targetList.size() == 0) {
            	response.getWriter().write(
    					"{success:false,info:\"数据必须在导入的Excel的第一个sheet中\"}");
    			return mapping.findForward(null);
            }
			//上传的文件删掉
			userfile.destroy();
			
			//封装需要将企业指标平均分配给每个员工的指标
			List<TargetTemplate> tts = TargetUtils.getTargetTemplate(tterminals);
			//企业指标平均分配
			for(TargetTemplate t : targetList) {
				//对新的指标进行进一步的完善
				List<TargetTemplate> fullTargets = TargetUtils.fillTarget(entCode,type,t.getYear(),t.getTargetOn(),tts);
				List<TargetTemplate> avgTargets = TargetUtils.avgEntTarget(fullTargets,count,t);
				// 入库
				targetService.importTarget(entCode, type, year, avgTargets,userInfo.getUser());
			}
			/**end*/
			response.getWriter().write("{success:true,info:\"保存成功\"}");// 成功
		} catch (ParseDataException e) {
			if (e instanceof ColumeNumberException)
				response.getWriter().write(
						"{success:false,info:\"" + e.getMessage() + "\"}");// 导入文件的列数与模版不一致
			else
				response.getWriter().write(
						"{success:false,info:\"" + e.getMessage() + "\"}");// 解析excel异常
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					"{success:false,info:\"" + e.getMessage() + "\"}");// 其他异常
		}
		return mapping.findForward(null);
	}
}
