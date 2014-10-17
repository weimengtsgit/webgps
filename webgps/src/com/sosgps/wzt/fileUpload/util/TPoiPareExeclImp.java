package com.sosgps.wzt.fileUpload.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.Workbook;

import org.apache.log4j.Logger;

import com.mapabc.geom.CoordCvtAPI;
import com.sosgps.wzt.orm.TAnnualExamination;
import com.sosgps.wzt.orm.TDispatchMoney;
import com.sosgps.wzt.orm.TInsurance;
import com.sosgps.wzt.orm.TOiling;
import com.sosgps.wzt.orm.TPoi;
import com.sosgps.wzt.orm.TToll;
import com.sosgps.wzt.orm.TVehicleExpense;
import com.sosgps.wzt.orm.TVehiclesMaintenance;
import com.sosgps.wzt.system.common.UserInfo;
import com.sosgps.wzt.util.DateUtility;

/**
 * 解析批量增加终端excel
 * 
 * @author zhangwei
 * @version 1.0
 * 
 */
public class TPoiPareExeclImp extends PareExcel {
	private Logger log = Logger.getLogger(TPoiPareExeclImp.class);

	public TPoiPareExeclImp() {

	}

	public TPoiPareExeclImp(String filePath) {
		this.filePath = filePath;
		this.init();
	}

	public void init() {
		try {
			// 构建Workbook对象, 只读Workbook对象
			// 直接从本地文件创建Workbook
			// 从输入流创建Workbook
			is = new FileInputStream(filePath);
			workbook = Workbook.getWorkbook(is);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析excle格式文件 返回List集合
	 */
	public List pareExcel(UserInfo userInfo, String poiImg, String range) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		String lonlat = "";
		String longitude = "";// 经度
		String latitude = "";// 维度
		String poiEncryptDatas = ""; // 加密坐标串
		com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();

		com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();

		for (int rows = 1; rows < this.getRows(0); rows++) {
			TPoi bean = new TPoi();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();

				// System.out.println("cell value:"+cellValue);
				// poi 名称
				if (cols == 0) {
					bean.setPoiName(cellValue);
					continue;
				}
				// 经度
				if (cols == 1) {
					longitude = cellValue;
					continue;
				}
				// 维度
				if (cols == 2) {
					latitude = cellValue;

					continue;
				}
				// 电话
				if (cols == 3) {
					bean.setTelephone(cellValue);
					continue;
				}
				// 地址
				if (cols == 4) {
					bean.setAddress(cellValue);
					continue;
				}
				// 描述
				if (cols == 5) {
					bean.setPoiDesc(cellValue);
					continue;
				}
				// 范围

				if (cols == 6) {
					bean.setVisitDistance(Long.parseLong(cellValue));
					continue;
				}
			}
			log.info("定时任务-查找没有偏转及获取描述的数据并获取位置描述及偏转数据-开始");

			double x = Float.parseFloat(longitude);
			double y = Float.parseFloat(latitude);
			double xs[] = { x };
			double ys[] = { y };
			// com.mapabc.geom.DPoint[] dps = null;
			com.sos.sosgps.api.DPoint[] dps = null;
			String lngX = "";
			String latY = "";
			String locDesc = "";
			try {
				// dps = coordAPI.encryptConvert(xs, ys);
				dps = coordApizw.encryptConvert(xs, ys);
				lngX = dps[0].getEncryptX();
				latY = dps[0].getEncryptY();
				locDesc = coordAPI.getAddress(lngX, latY);
				// 偏转坐标
				lonlat = dps[0].x + "," + dps[0].y;
				// 偏转加密坐标
				// poiEncryptDatas
				// =coordAPI.encrypt(dps[0].x)+","+coordAPI.encrypt(dps[0].y);
				poiEncryptDatas = coordApizw.encrypt(dps[0].x) + ","
						+ coordAPI.encrypt(dps[0].y);

			} catch (Exception e) {
				log.error("POI导入-偏转加密error! x=" + x + " y=" + y);
			}
			bean.setLocDesc(locDesc);
			// 创建者
			bean.setCreator(userInfo.getUserAccount());
			// 所属企业
			bean.setEntcode(userInfo.getEmpCode());
			// 默认创建显示图片
			if (poiImg == null || poiImg.length() <= 0) {
				bean.setIconpath("poi0.gif");
			} else {
				bean.setIconpath(poiImg);
			}
			// 偏转明码坐标串
			bean.setPoiDatas(lonlat);
			// 加密坐标串
			bean.setPoiEncryptDatas(poiEncryptDatas);
			// poi 点类型 默认为 点
			bean.setPoiType(0l);
			// 默认可见
			bean.setVisible("1");

			list.add(bean);
		}
		return list;
	}

	/**
	 * 车辆年审记录
	 */
	public List annualExamin(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TAnnualExamination tAnnualExamination = new TAnnualExamination();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId 终端名称
				if (cols == 0) {
					tAnnualExamination.setDeviceId(cellValue);
					continue;
				}
				// 年审日期
				if (cols == 1) {
					tAnnualExamination.setExaminationDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// 年审项目
				if (cols == 2) {
					tAnnualExamination.setProject(cellValue);
					continue;
				}
				// 年审情况
				if (cols == 3) {
					tAnnualExamination.setCondition(cellValue);
					continue;
				}
				// 年审金额
				if (cols == 4) {
						tAnnualExamination.setExpenses(Long.parseLong(cellValue));
				}
				// 经手人
				if (cols == 5) {
					tAnnualExamination.setHandler(cellValue);
					continue;
				}
				// 到期日期

				if (cols == 6) {
					tAnnualExamination.setExpireDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// 备注
				if (cols == 7) {
					tAnnualExamination.setDemo(cellValue);
					continue;
				}
			}
			
			log.info("开始解析车辆年审记录批量添加Excel");
			// 默认可见
			tAnnualExamination.setEmpCode(userInfo.getEmpCode());
			list.add(tAnnualExamination);
		}
		return list;
	}

	public static void main(String[] args) {
		TPoiPareExeclImp TerminalPareExeclImp = new TPoiPareExeclImp(
				"d://test.xls");
		// TerminalPareExeclImp.pareExcel();
	}

	/**
	 * 费用记录支出添加
	 */
	public List vehicleExpense(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TVehicleExpense tVehicleExpense = new TVehicleExpense();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				if (cols == 0) {
					tVehicleExpense.setDeviceId(cellValue);
					continue;
				}
				// 车辆折旧费用
				if (cols == 8) {
						tVehicleExpense.setVehicleDepreciation(Long.parseLong(cellValue));
						continue;
				}
				// 人员费用
				if (cols == 9) {
						tVehicleExpense.setPersonalExpenses(Long.parseLong(cellValue));
						continue;
				}
				// 保险摊销费用
				if (cols == 10) {
						tVehicleExpense.setInsurance(Long.parseLong(cellValue));
						continue;
				}
				// 维修保养费用
				if (cols == 11) {
						tVehicleExpense.setMaintenance(Long.parseLong(cellValue));
						continue;
				}
				// 过路过桥费用
				if (cols == 12) {
						tVehicleExpense.setToll(Long.parseLong(cellValue));
						continue;
				}
				// 年检以其他费用
				if (cols == 13) {
						tVehicleExpense.setAnnualCheck(Long.parseLong(cellValue));
						continue;
				}
				// 日期
				if (cols == 14) {
					tVehicleExpense.setCreateDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
			}
			log.info("开始解析费用记录支出添加批量添加Excel");
			// 默认可见
			tVehicleExpense.setEmpCode(userInfo.getEmpCode());
			list.add(tVehicleExpense);
		}
		return list;
	}

	/**
	 * 过路费记录添加
	 */
	public List addToll(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TToll tToll = new TToll();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId 终端名称
				if (cols == 0) {
					tToll.setDeviceId(cellValue);
					continue;
				}
				// 缴费日期
				if (cols == 15) {
					tToll.setPayDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// 缴费地点
				if (cols == 16) {
					tToll.setPayPlace(cellValue);
					continue;
				}
				// 缴费金额
				if (cols == 17) {
						tToll.setExpenses(Long.parseLong(cellValue));
						continue;
				}
				// 经手人
				if (cols == 18) {
					tToll.setHandler(cellValue);
					continue;
				}
				// 备注
				if (cols == 19) {
					tToll.setDemo(cellValue);
					continue;
				}
			}
			log.info("开始解析 过路费记录添加批量添加Excel");
			// 默认可见
			tToll.setEmpCode(userInfo.getEmpCode());
			list.add(tToll);
		}
		return list;
	}


	/**
	 * 车辆维护记录添加
	 */
	public List addVehiclesMaintenance(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TVehiclesMaintenance tVehiclesMaintenance = new TVehiclesMaintenance();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId 终端名称
				if (cols == 0) {
					tVehiclesMaintenance.setDeviceId(cellValue);
					continue;
				}
				// 维护日期
				if (cols == 20) {
					tVehiclesMaintenance.setMaintenanceDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// 维护费用
				if (cols == 21) {
						tVehiclesMaintenance.setExpenses(Long.parseLong(cellValue));
						continue;
				}
				// 维护情况
				if (cols == 22) {
					tVehiclesMaintenance.setCondition(cellValue);
					continue;
				}
				// 经手人
				if (cols == 23) {
					tVehiclesMaintenance.setHandler(cellValue);
					continue;
				}
				//到期日期
				if (cols == 24) {
					tVehiclesMaintenance.setExpireDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// 备注
				if (cols == 25) {
					tVehiclesMaintenance.setDemo(cellValue);
					continue;
				}
			}
			log.info("开始解析车辆维护记录添加批量添加Excel");
			// 默认可见
			tVehiclesMaintenance.setEmpCode(userInfo.getEmpCode());
			list.add(tVehiclesMaintenance);
		}
		return list;
	}


	/**
	 * 保险费用记录添加
	 */
	public List addInsurance(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TInsurance tInsurance = new TInsurance();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId 终端名称
				if (cols == 0) {
					tInsurance.setDeviceId(cellValue);
					continue;
				}
				// 保险单号
				if (cols == 26) {
					tInsurance.setInsuranceNo(cellValue);
					continue;
				}
				//保险名称
				if (cols == 27) {
					tInsurance.setInsuranceName(cellValue);
					continue;
				}
				// 保险公司
				if (cols == 28) {
					tInsurance.setInsuranceCo(cellValue);
					continue;
				}
				//投保日期
				if (cols == 29) {
					tInsurance.setInsuranceDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				//到期日期
				if (cols == 30) {
					tInsurance.setExpireDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// 保额
				if (cols == 31) {
						tInsurance.setSumInsured(Long.parseLong(cellValue));
						continue;
				}
				// 保费
				if (cols == 32) {
						tInsurance.setPremium(Long.parseLong(cellValue));
						continue;
				}
				// 经手人
				if (cols == 33) {
					tInsurance.setHandler(cellValue);
					continue;
				}
			}
			log.info("开始解析保险费用记录添加批量添加Excel");
			// 默认可见
			tInsurance.setEmpCode(userInfo.getEmpCode());
			list.add(tInsurance);
		}
		return list;
	}

	/**
	 * 加油记录添加
	 */
	public List addOiling(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TOiling tOiling = new TOiling();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId 终端名称
				if (cols == 0) {
					tOiling.setDeviceId(cellValue);
					continue;
				}
				// 加油量（升）
				if (cols == 34) {
						tOiling.setOilLiter(Long.parseLong(cellValue));
						continue;
				}
				//加油金额
				if (cols == 35) {
						tOiling.setOilCost(Long.parseLong(cellValue));
						continue;
				}
				// 加油日期
				if (cols == 36) {
					tOiling.setCreateDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
			}
			log.info("开始解析加油记录添加批量添加Excel");
			// 默认可见
			tOiling.setEmpCode(userInfo.getEmpCode());
			list.add(tOiling);
		}
		return list;
	}
	/**
	 * 配送金额
	 */
	public List addDispatchMoney(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TDispatchMoney tDispatchMoney = new TDispatchMoney();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId 终端名称
				if (cols == 0) {
					tDispatchMoney.setDeviceId(cellValue);
					continue;
				}
				// 配送日期
				if (cols == 37) {
					tDispatchMoney.setDispatchDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// 配送数量
				if (cols == 38) {
					tDispatchMoney.setDispatchAmount(Long.parseLong(cellValue));
					continue;
				}
				// 经手人
				if (cols == 39) {
					tDispatchMoney.setFrmhandler(cellValue);
						continue;
				}
				// 备注
				if (cols == 40) {
					tDispatchMoney.setFrmdemo(cellValue);
					continue;
				}
			}
			log.info("开始解析 过路费记录添加批量添加Excel");
			// 默认可见
			tDispatchMoney.setUserId(userInfo.getUserId());
			tDispatchMoney.setSaveDate(new Date());
			list.add(tDispatchMoney);
		}
		return list;
	}
}
