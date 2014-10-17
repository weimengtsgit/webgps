package com.sosgps.wzt.fileUpload.util;

import java.util.List;

import com.sosgps.wzt.system.common.UserInfo;

/**
 * ���� Excel ������ ��������ģʽ
 * 
 * @author zhangwei
 * @version 1.0
 * @{zhangwei77 2008-04-15
 * 
 */
public class AppPareExcel {
	PareExcel pareExcel;
	String filePath;

	/**
	 * ����Excel���ݵ�����
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List pareExcel(UserInfo userInfo, String poiImg, String range) {
		this.pareExcel.init();
		return this.pareExcel.pareExcel(userInfo, poiImg, range);
	}

	// add ���������¼
	public List annualExamin(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.annualExamin(userInfo);
	}

	// ���ü�¼֧�����
	public List vehicleExpense(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.vehicleExpense(userInfo);
	}

	// ��·�Ѽ�¼���
	public List addToll(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addToll(userInfo);
	}

	// ����ά����¼���
	public List addVehiclesMaintenance(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addVehiclesMaintenance(userInfo);
	}

	// ���շ��ü�¼���
	public List addInsurance(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addInsurance(userInfo);
	}

	// ���ͼ�¼���
	public List addOiling(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addOiling(userInfo);
	}
	// ���ͽ�����
	public List addDispatchMoney(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addDispatchMoney(userInfo);
	}
	public PareExcel getPareExcel() {
		return pareExcel;
	}

	public void setPareExcel(PareExcel pareExcel) {
		this.pareExcel = pareExcel;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		pareExcel.setFilePath(filePath);
	}

	public static void main(String[] args) {
		// AppPareExcel server =new AppPareExcel();
		// server.setPareExcel(new TerminalPareExeclImp());
		// server.setFilePath("d://test.xls");
		// List list = server.pareExcel();
	}

}
