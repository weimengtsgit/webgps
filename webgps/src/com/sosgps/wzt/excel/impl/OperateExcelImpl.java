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
 * ����excel�ļ���ʵ����
 * @author zhumingzhe 
 * @add by zhumingzhe 2010-09-15
 */
public class OperateExcelImpl extends FileUtil implements OperateExcel {
	private static final Logger logger = Logger
	.getLogger(AlarmStatServiceImpl.class);
	/**
	 * ��ȡExcel�ļ����ϻ������sheet����
	 * param Ҫ��ȡ���ļ�����
	 * return sheet����
	 */
	public List<Sheet> read(List<File> files) {
		List<Sheet> shs=new ArrayList();
		if(files.size()>0){
			for(int i=0;i<files.size();i++){
				//�ж��ļ������Ƿ�Ϸ�
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
						logger.error("�ļ�������", e);
					}
				}else{
					logger.error("�ļ���ʽ����ȷ");
				}
			}
		}
		return shs;
	}
	/*
	 * ��������sheet����ָ��·����excel�ļ�
	 * @see com.sosgps.wzt.excel.operateExcelDao#write(java.lang.String, java.util.List)
	 */
	public List<File> write(String path,List<Sheet> sheets,Date startDate,Date endDate) throws Exception {
		List<File> files=new ArrayList<File>();
		String sd=DateUtil.convertUTC(startDate.toString());
		String ed=DateUtil.convertUTC(endDate.toString());
		if(sheets!=null&&sheets.size()>0){
			Set<String> names=new LinkedHashSet<String>();
			for(int i=0;i<sheets.size();i++){
				//ȥ���ظ����ļ���ȷ���ϲ�ͬ���ļ�
				names.add(sheets.get(i).getName());
			}
			//����set����
			Iterator it=names.iterator();
			//�����ļ�������
			File file=new File(path+sd+"-"+ed+EXL);
			//���������ռ�
			WritableWorkbook wb=Workbook.createWorkbook(file);
			int index=1;
			while(it.hasNext()){
				String name=it.next().toString();
				//����������
				WritableSheet st=wb.createSheet(name, index);
				index+=1;
				int rows=0;
				boolean first=true;
				int title=0;//����������ͬ����sheet�ı�����
				for(int n=0;n<sheets.size();n++){
					if(name.equals(sheets.get(n).getName())){
						if(!first){
							//rows+=sheets.get(n).getRows();
							rows=st.getRows();
							title=1;
						}
						first=false;
						int columns=sheets.get(n).getColumns();//�����
						for(int h=0;h<columns;h++){
							//�����п�λ��������40���
							if(h==3)
								st.setColumnView(h, 40);
							else
								st.setColumnView(h, 20);
							//��ӵ�Ԫ��
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
