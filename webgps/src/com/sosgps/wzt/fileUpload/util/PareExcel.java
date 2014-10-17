package com.sosgps.wzt.fileUpload.util;

import java.io.InputStream;
import java.util.List;
import com.sosgps.wzt.system.common.UserInfo;

/**
 * excel 解析接口
 * 
 * @author Administrator
 * 
 */
abstract class PareExcel {
	// 初始化变量
	jxl.Workbook workbook = null;
	InputStream is = null;
	String filePath;

	public abstract void init();

	@SuppressWarnings("rawtypes")
	public abstract List pareExcel(UserInfo userInfo, String poiImg,
			String range);

	// 车辆年审记录
	public abstract List annualExamin(UserInfo userInfo);

	// 费用记录支出添加
	public abstract List vehicleExpense(UserInfo userInfo);

	// 过路费记录添加
	public abstract List addToll(UserInfo userInfo);

	// 车辆维护记录添加
	public abstract List addVehiclesMaintenance(UserInfo userInfo);

	// 保险费用记录添加
	public abstract List addInsurance(UserInfo userInfo);

	// 加油记录添加
	public abstract List addOiling(UserInfo userInfo);

	// 配送金额添加
	public abstract List addDispatchMoney(UserInfo userInfo);

	/**
	 * 返回工作表中指定sheet
	 * 
	 * @param sheetNum
	 * @return
	 */
	public jxl.Sheet getSheet(int sheetNum) {
		jxl.Sheet rs = this.workbook.getSheet(sheetNum);
		return rs;
	}

	/**
	 * 获取Sheet表中所包含的总列数 返回列数
	 */
	public int getColumns(int sheetNum) {
		return getSheet(sheetNum).getColumns();
	}

	/**
	 * 获取Sheet表中所包含的总行数
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
