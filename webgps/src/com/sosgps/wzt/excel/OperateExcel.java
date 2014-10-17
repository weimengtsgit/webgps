package com.sosgps.wzt.excel;

import java.io.File;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
/**
 * 操作excel文件的类接口
 * @author zhumingzhe
 *@add by zhumingzhe 2010-09-15
 */
public interface OperateExcel {
	
	//读取Excel文件集合获得所有sheet表集合
	public List<Sheet> read(List<File> files);
	
	//指定路径下生成合并sheet集合的excel文件
	public List<File> write(String path,List<Sheet> sheets,Date startDate,Date endDate)throws Exception;
}
