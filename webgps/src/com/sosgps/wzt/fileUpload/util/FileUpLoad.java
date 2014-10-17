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
 * �ļ��ϴ����� Ŀǰ����Ϊ xls ��ʽ ����չ
 * 
 * @author zhangwei
 * @version 1.0
 * @{zhangwei77@mapabc.com} 2008-04-16
 */
public class FileUpLoad {

	
	private String uploadPath;

	private String uploadDir = "upload";// �ϴ��ļ���Ŀ¼ 
	
	private String tempDir = "tmp"; // ��ʱ�ļ�Ŀ¼

	private int maxMemorySize = 1024;

	private int maxRequestSize = 1000000;

	private boolean writeToFile = true; //�Ƿ�д���ļ�

	private String[] fileType = { "xls" };//�ϴ��ļ���ʽ Ĭ��Ϊ excecl ��ʽ

	private HttpServletRequest request;

	/**
	 * 
	 * @param request form����
	 */
	public FileUpLoad(HttpServletRequest request) {
		this.request = request;

		this.init(this.request);
	}

	/**
	 * �ж�Ŀ¼�Ƿ���ڲ�����Ŀ¼
	 * 
	 */
	public void init(HttpServletRequest request) {

		// ��ȡ·��
		//String realPath = request.getRealPath("//");  windows ϵͳ��
		String realPath = request.getRealPath("/"); //linux ϵͳ��
		//this.outLogger.info("�ϴ�realPath:"+realPath);
		
		this.uploadPath = realPath + this.uploadDir;
		
		//this.outLogger.info("uploadPath��"+uploadPath);
		this.tempDir = realPath + this.tempDir;
		try{
		// �ж�Ŀ¼�Ƿ����
		if (!new File(uploadPath).isDirectory()) {
			new File(uploadPath).mkdirs();
		}
		// �ж�Ŀ¼�Ƿ����
		if (!new File(tempDir).isDirectory()) {
			new File(tempDir).mkdirs();
		}
		}catch(Exception ex){
			ex.printStackTrace();
	
			return;
		}

	}
	/**
	 * ʵ��ָ����ʽ�����ϴ�
	 * @return String �ϴ������ļ�����
	 * @throws Exception �������׳��쳣
	 */
	public String process() throws Exception{
		String ret = null;//���سɹ������ϴ��ļ�����
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
//									fileName.length());                        //windows ��
					fileName = fileName
					.substring(
							fileName.lastIndexOf("\\") + 1,
							fileName.length());                                //linux ��

					fileName = this.reName(fileName);
					// �ļ����ʹ���
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
	 * �ϴ����ݸ�ʽ�Ƿ����
	 * @param fileName
	 * @return boolean ���� true ������ false
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
	 * ���ϴ��������ļ���������
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
