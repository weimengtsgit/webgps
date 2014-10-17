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
 * 文件操作类
 * @author 
 * @add by zhumingzhe 2010-09-15
 */
public class FileUtil {
	
	protected static final String EXL=".xls";//excel文件后缀
	protected static final String EXLX=".xlsx";//excel文件后缀
	/**
	 * 判断是不是Excel文件
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
	 * zip的文件名
	 * @param file
	 * @return
	 */
	public static String zipName(File file){
		int lastPoint=file.getName().lastIndexOf('.');
		return file.getName().substring(0,lastPoint);
	}
	/**
	 * 打包方法生成zip压缩文件
	 * @param path 打包完文件的存放路径
	 * @param files 要打包的文件集合
	 * @return 返回压缩文件路径
	 * @throws Exception
	 */
	public static String zip(String path,List<File> files) throws Exception{
			String zipPath=path+zipName(files.get(0))+".zip";
			new File(zipPath);
			//fileIsExists(path, new File(zipPath));
			//定义文件输出流
			FileOutputStream fo=new FileOutputStream(zipPath);
			//使用输出流检查
            CheckedOutputStream cos = new CheckedOutputStream(fo, new CRC32());
			//定义压缩输出流
			ZipOutputStream zos=new ZipOutputStream(new BufferedOutputStream(cos));
			//设置输入流编码格式
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
	 * 清理临时生成的文件
	 * @param path临时文件路径
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
	 * 获得目录下所有文件
	 * @param pathes文件路径集合
	 * @return 返回文件集合
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
