package com.sosgps.wzt.fileUpload.util;

import java.io.InputStream;
import java.util.List;
import com.sosgps.wzt.system.common.UserInfo;

/**
 * excel �����ӿ�
 * 
 * @author Administrator
 * 
 */
abstract class PareExcel {
	// ��ʼ������
	jxl.Workbook workbook = null;
	InputStream is = null;
	String filePath;

	public abstract void init();

	@SuppressWarnings("rawtypes")
	public abstract List pareExcel(UserInfo userInfo, String poiImg,
			String range);

	// ���������¼
	public abstract List annualExamin(UserInfo userInfo);

	// ���ü�¼֧�����
	public abstract List vehicleExpense(UserInfo userInfo);

	// ��·�Ѽ�¼���
	public abstract List addToll(UserInfo userInfo);

	// ����ά����¼���
	public abstract List addVehiclesMaintenance(UserInfo userInfo);

	// ���շ��ü�¼���
	public abstract List addInsurance(UserInfo userInfo);

	// ���ͼ�¼���
	public abstract List addOiling(UserInfo userInfo);

	// ���ͽ�����
	public abstract List addDispatchMoney(UserInfo userInfo);

	/**
	 * ���ع�������ָ��sheet
	 * 
	 * @param sheetNum
	 * @return
	 */
	public jxl.Sheet getSheet(int sheetNum) {
		jxl.Sheet rs = this.workbook.getSheet(sheetNum);
		return rs;
	}

	/**
	 * ��ȡSheet������������������ ��������
	 */
	public int getColumns(int sheetNum) {
		return getSheet(sheetNum).getColumns();
	}

	/**
	 * ��ȡSheet������������������
	 * 
	 * @param sheetNum
	 * @return
	 */
	public int getRows(int sheetNum) {
		return getSheet(sheetNum).getRows();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
