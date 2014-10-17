package com.sosgps.wzt.excel.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * �ļ�������
 * @author 
 * @add by zhumingzhe 2010-09-15
 */
public class FileUtil {
	
	protected static final String EXL=".xls";//excel�ļ���׺
	protected static final String EXLX=".xlsx";//excel�ļ���׺
	/**
	 * �ж��ǲ���Excel�ļ�
	 * @param file 
	 * @return
	 */
	public static boolean isExcel(File file){
		int lastPoint=file.getName().lastIndexOf('.');
		String suffix=file.getName().substring(lastPoint);
		if(suffix.equals(EXL)||suffix.equals(EXLX)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * zip���ļ���
	 * @param file
	 * @return
	 */
	public static String zipName(File file){
		int lastPoint=file.getName().lastIndexOf('.');
		return file.getName().substring(0,lastPoint);
	}
	/**
	 * �����������zipѹ���ļ�
	 * @param path ������ļ��Ĵ��·��
	 * @param files Ҫ������ļ�����
	 * @return ����ѹ���ļ�·��
	 * @throws Exception
	 */
	public static String zip(String path,List<File> files) throws Exception{
			String zipPath=path+zipName(files.get(0))+".zip";
			new File(zipPath);
			//fileIsExists(path, new File(zipPath));
			//�����ļ������
			FileOutputStream fo=new FileOutputStream(zipPath);
			//ʹ����������
            CheckedOutputStream cos = new CheckedOutputStream(fo, new CRC32());
			//����ѹ�������
			ZipOutputStream zos=new ZipOutputStream(new BufferedOutputStream(cos));
			//���������������ʽ
			zos.setEncoding("GBK");
			for(File file:files){
				//BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
				FileInputStream bf=new FileInputStream(file);
				int c;
				zos.putNextEntry(new ZipEntry(file.getName()));
				while((c=bf.read())!=-1){
					zos.write(c);
				}
					bf.close();
			}
			zos.close();
			return zipPath;
		}
	/**
	 * ������ʱ���ɵ��ļ�
	 * @param path��ʱ�ļ�·��
	 */
	public static void clean(String path){
		File file=new File(path);
		if(file.isDirectory()){
			File[] files=file.listFiles();
			for(int i=0;i<files.length;i++){
				files[i].delete();
			}
		}
	}
	/**
	 * ���Ŀ¼�������ļ�
	 * @param pathes�ļ�·������
	 * @return �����ļ�����
	 */
	public static List<File> getAllFiles(List pathes){
		List<File> files=new ArrayList();
		if(pathes!=null&&pathes.size()>0){
			for(int i=0;i<pathes.size();i++){
				File file=new File(pathes.get(i).toString());
				files.add(file);
			}
		}
		return files;
	}
	
}
