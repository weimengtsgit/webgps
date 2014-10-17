package com.sosgps.wzt.excel.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.sosgps.wzt.excel.OperateExcel;
import com.sosgps.wzt.excel.common.FileUtil;
import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.stat.service.impl.AlarmStatServiceImpl;
import com.sosgps.wzt.stat.service.impl.VisitStatServiceImpl;
/**
 * 操作excel文件的实现类
 * @author zhumingzhe 
 * @add by zhumingzhe 2010-09-15
 */
public class OperateExcelImpl extends FileUtil implements OperateExcel {
	private static final Logger logger = Logger
	.getLogger(AlarmStatServiceImpl.class);
	/**
	 * 读取Excel文件集合获得所有sheet表集合
	 * param 要读取的文件集合
	 * return sheet表集合
	 */
	public List<Sheet> read(List<File> files) {
		List<Sheet> shs=new ArrayList();
		if(files.size()>0){
			for(int i=0;i<files.size();i++){
				//判断文件类型是否合法
				if(FileUtil.isExcel(files.get(i))){
					try {
						InputStream inputStream=new FileInputStream(files.get(i));
						Workbook book=Workbook.getWorkbook(inputStream);
						Sheet[] sheets=book.getSheets();
						for(int j=0;j<sheets.length;j++){
							shs.add(sheets[j]);
						}
					} catch (BiffException e) {
						e.printStackTrace();
					} catch (IOException e) {
						logger.error("文件不存在", e);
					}
				}else{
					logger.error("文件格式不正确");
				}
			}
		}
		return shs;
	}
	/*
	 * 操作所有sheet集合指定路径的excel文件
	 * @see com.sosgps.wzt.excel.operateExcelDao#write(java.lang.String, java.util.List)
	 */
	public List<File> write(String path,List<Sheet> sheets,Date startDate,Date endDate) throws Exception {
		List<File> files=new ArrayList<File>();
		String sd=DateUtil.convertUTC(startDate.toString());
		String ed=DateUtil.convertUTC(endDate.toString());
		if(sheets!=null&&sheets.size()>0){
			Set<String> names=new LinkedHashSet<String>();
			for(int i=0;i<sheets.size();i++){
				//去除重复的文件名确保合并同名文件
				names.add(sheets.get(i).getName());
			}
			//迭代set集合
			Iterator it=names.iterator();
			//生成文件名规则
			File file=new File(path+sd+"-"+ed+EXL);
			//创建工作空间
			WritableWorkbook wb=Workbook.createWorkbook(file);
			int index=1;
			while(it.hasNext()){
				String name=it.next().toString();
				//创建工作表
				WritableSheet st=wb.createSheet(name, index);
				index+=1;
				int rows=0;
				boolean first=true;
				int title=0;//用于跳过相同名称sheet的标题行
				for(int n=0;n<sheets.size();n++){
					if(name.equals(sheets.get(n).getName())){
						if(!first){
							//rows+=sheets.get(n).getRows();
							rows=st.getRows();
							title=1;
						}
						first=false;
						int columns=sheets.get(n).getColumns();//获得列
						for(int h=0;h<columns;h++){
							//设置列宽，位置描述列40宽度
							if(h==3)
								st.setColumnView(h, 40);
							else
								st.setColumnView(h, 20);
							//添加单元格
							for(int g=title;g<sheets.get(n).getRows();g++){
								Label lb=new Label(h,g+rows-title,sheets.get(n).getCell(h, g).getContents());
								st.addCell(lb);
							}
						}
					}
				}
			}
			wb.write();
			wb.close();
			files.add(file);
		}
		return files;
	}
}
