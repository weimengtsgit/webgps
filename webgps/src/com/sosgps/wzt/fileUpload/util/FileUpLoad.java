package com.sosgps.wzt.fileUpload.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.log4j.Logger;

/**
 * 文件上传工具 目前限制为 xls 格式 可扩展
 * 
 * @author zhangwei
 * @version 1.0
 * @{zhangwei77@mapabc.com} 2008-04-16
 */
public class FileUpLoad {

	
	private String uploadPath;

	private String uploadDir = "upload";// 上传文件的目录 
	
	private String tempDir = "tmp"; // 临时文件目录

	private int maxMemorySize = 1024;

	private int maxRequestSize = 1000000;

	private boolean writeToFile = true; //是否写入文件

	private String[] fileType = { "xls" };//上传文件格式 默认为 excecl 格式

	private HttpServletRequest request;

	/**
	 * 
	 * @param request form请求
	 */
	public FileUpLoad(HttpServletRequest request) {
		this.request = request;

		this.init(this.request);
	}

	/**
	 * 判断目录是否存在并创建目录
	 * 
	 */
	public void init(HttpServletRequest request) {

		// 获取路径
		//String realPath = request.getRealPath("//");  windows 系统下
		String realPath = request.getRealPath("/"); //linux 系统下
		//this.outLogger.info("上传realPath:"+realPath);
		
		this.uploadPath = realPath + this.uploadDir;
		
		//this.outLogger.info("uploadPath："+uploadPath);
		this.tempDir = realPath + this.tempDir;
		try{
		// 判断目录是否存在
		if (!new File(uploadPath).isDirectory()) {
			new File(uploadPath).mkdirs();
		}
		// 判断目录是否存在
		if (!new File(tempDir).isDirectory()) {
			new File(tempDir).mkdirs();
		}
		}catch(Exception ex){
			ex.printStackTrace();
	
			return;
		}

	}
	/**
	 * 实现指定格式数据上传
	 * @return String 上传数据文件名称
	 * @throws Exception 错误发生抛出异常
	 */
	public String process() throws Exception{
		String ret = null;//返回成功数据上传文件名称
		try {
			// Check that we have a file upload request
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Set factory constraints
			factory.setSizeThreshold(maxMemorySize);
			factory.setRepository(new File(tempDir));

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Set overall request size constraint
			upload.setSizeMax(maxRequestSize);

			// Parse the request
			List /* FileItem */items = upload.parseRequest(request);

			String fileName = "";
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (!item.isFormField()) {
					fileName = item.getName();
//					fileName = fileName
//							.substring(
//									fileName.lastIndexOf(File.separator) + 1,
//									fileName.length());                        //windows 下
					fileName = fileName
					.substring(
							fileName.lastIndexOf("\\") + 1,
							fileName.length());                                //linux 下

					fileName = this.reName(fileName);
					// 文件类型错误
					if (!this.isVailed(fileName)) {
						return ret = null;
					}
					// Process a file upload
					if (writeToFile && !fileName.equals("")) {
						File uploadedFile = new File(uploadPath, fileName);
						item.write(uploadedFile);
					}
				}
			}
			ret = fileName;
		} catch (Exception e) {
			throw e;			
		}
		return ret;
	}
	/**
	 * 上传数据格式是否符合
	 * @param fileName
	 * @return boolean 符合 true 不符合 false
	 */
	public boolean isVailed(String fileName) {
		boolean ret = false;
		String tmp = fileName.substring(fileName.lastIndexOf(".") + 1);
		for (int size = 0; size < this.fileType.length; size++) {
			if (tmp.equals(fileType[size])) {
				ret = true;
			}
			
		}
		return ret;
	}

	/**
	 * 将上传上来的文件重新命名
	 * 
	 * @param fileName
	 * @return
	 */
	public String reName(String fileName) {
		String ret = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String strCurrentTime = df.format(new Date());
		ret = "["+strCurrentTime+"]" + fileName;
		return ret;
	}

	public String processUploadedFile(FileItem item) {
		String fileName = "";
		if (!item.isFormField()) {
			fileName = item.getName();
		}
		return fileName;
	}

	public int getMaxMemorySize() {
		return maxMemorySize;
	}

	public void setMaxMemorySize(int maxMemorySize) {
		this.maxMemorySize = maxMemorySize;
	}

	public int getMaxRequestSize() {
		return maxRequestSize;
	}

	public void setMaxRequestSize(int maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
	}

	public String getTempPath() {
		return tempDir;
	}

	public void setTempPath(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public boolean isWriteToFile() {
		return writeToFile;
	}

	public void setWriteToFile(boolean writeToFile) {
		this.writeToFile = writeToFile;
	}

	public static void main(String[] args) {
		// FileUpLoad fileUpLoad = new FileUpLoad();
		// fileUpLoad.init();
	}

	public String[] getFileType() {
		return fileType;
	}

	public void setFileType(String[] fileType) {
		this.fileType = fileType;
	}
}
