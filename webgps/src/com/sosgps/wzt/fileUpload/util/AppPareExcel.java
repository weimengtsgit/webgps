package com.sosgps.wzt.fileUpload.util;

import java.util.List;

import com.sosgps.wzt.system.common.UserInfo;

/**
 * 调用 Excel 服务类 借助策略模式
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
	 * 读出Excel数据到集合
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List pareExcel(UserInfo userInfo, String poiImg, String range) {
		this.pareExcel.init();
		return this.pareExcel.pareExcel(userInfo, poiImg, range);
	}

	// add 车辆年审记录
	public List annualExamin(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.annualExamin(userInfo);
	}

	// 费用记录支出添加
	public List vehicleExpense(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.vehicleExpense(userInfo);
	}

	// 过路费记录添加
	public List addToll(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addToll(userInfo);
	}

	// 车辆维护记录添加
	public List addVehiclesMaintenance(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addVehiclesMaintenance(userInfo);
	}

	// 保险费用记录添加
	public List addInsurance(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addInsurance(userInfo);
	}

	// 加油记录添加
	public List addOiling(UserInfo userInfo) {
		this.pareExcel.init();
		return this.pareExcel.addOiling(userInfo);
	}
	// 配送金额添加
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
