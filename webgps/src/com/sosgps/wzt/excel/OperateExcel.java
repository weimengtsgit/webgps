package com.sosgps.wzt.excel;

import java.io.File;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
/**
 * ����excel�ļ�����ӿ�
 * @author zhumingzhe
 *@add by zhumingzhe 2010-09-15
 */
public interface OperateExcel {
	
	//��ȡExcel�ļ����ϻ������sheet����
	public List<Sheet> read(List<File> files);
	
	//ָ��·�������ɺϲ�sheet���ϵ�excel�ļ�
	public List<File> write(String path,List<Sheet> sheets,Date startDate,Date endDate)throws Exception;
}
