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
 * �������������ն�excel
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
			// ����Workbook����, ֻ��Workbook����
			// ֱ�Ӵӱ����ļ�����Workbook
			// ������������Workbook
			is = new FileInputStream(filePath);
			workbook = Workbook.getWorkbook(is);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ����excle��ʽ�ļ� ����List����
	 */
	public List pareExcel(UserInfo userInfo, String poiImg, String range) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		String lonlat = "";
		String longitude = "";// ����
		String latitude = "";// ά��
		String poiEncryptDatas = ""; // �������괮
		com.mapabc.geom.CoordCvtAPI coordAPI = new CoordCvtAPI();

		com.sos.sosgps.api.CoordAPI coordApizw = new com.sos.sosgps.api.CoordAPI();

		for (int rows = 1; rows < this.getRows(0); rows++) {
			TPoi bean = new TPoi();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();

				// System.out.println("cell value:"+cellValue);
				// poi ����
				if (cols == 0) {
					bean.setPoiName(cellValue);
					continue;
				}
				// ����
				if (cols == 1) {
					longitude = cellValue;
					continue;
				}
				// ά��
				if (cols == 2) {
					latitude = cellValue;

					continue;
				}
				// �绰
				if (cols == 3) {
					bean.setTelephone(cellValue);
					continue;
				}
				// ��ַ
				if (cols == 4) {
					bean.setAddress(cellValue);
					continue;
				}
				// ����
				if (cols == 5) {
					bean.setPoiDesc(cellValue);
					continue;
				}
				// ��Χ

				if (cols == 6) {
					bean.setVisitDistance(Long.parseLong(cellValue));
					continue;
				}
			}
			log.info("��ʱ����-����û��ƫת����ȡ���������ݲ���ȡλ��������ƫת����-��ʼ");

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
				// ƫת����
				lonlat = dps[0].x + "," + dps[0].y;
				// ƫת��������
				// poiEncryptDatas
				// =coordAPI.encrypt(dps[0].x)+","+coordAPI.encrypt(dps[0].y);
				poiEncryptDatas = coordApizw.encrypt(dps[0].x) + ","
						+ coordAPI.encrypt(dps[0].y);

			} catch (Exception e) {
				log.error("POI����-ƫת����error! x=" + x + " y=" + y);
			}
			bean.setLocDesc(locDesc);
			// ������
			bean.setCreator(userInfo.getUserAccount());
			// ������ҵ
			bean.setEntcode(userInfo.getEmpCode());
			// Ĭ�ϴ�����ʾͼƬ
			if (poiImg == null || poiImg.length() <= 0) {
				bean.setIconpath("poi0.gif");
			} else {
				bean.setIconpath(poiImg);
			}
			// ƫת�������괮
			bean.setPoiDatas(lonlat);
			// �������괮
			bean.setPoiEncryptDatas(poiEncryptDatas);
			// poi ������ Ĭ��Ϊ ��
			bean.setPoiType(0l);
			// Ĭ�Ͽɼ�
			bean.setVisible("1");

			list.add(bean);
		}
		return list;
	}

	/**
	 * ���������¼
	 */
	public List annualExamin(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TAnnualExamination tAnnualExamination = new TAnnualExamination();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId �ն�����
				if (cols == 0) {
					tAnnualExamination.setDeviceId(cellValue);
					continue;
				}
				// ��������
				if (cols == 1) {
					tAnnualExamination.setExaminationDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// ������Ŀ
				if (cols == 2) {
					tAnnualExamination.setProject(cellValue);
					continue;
				}
				// �������
				if (cols == 3) {
					tAnnualExamination.setCondition(cellValue);
					continue;
				}
				// ������
				if (cols == 4) {
						tAnnualExamination.setExpenses(Long.parseLong(cellValue));
				}
				// ������
				if (cols == 5) {
					tAnnualExamination.setHandler(cellValue);
					continue;
				}
				// ��������

				if (cols == 6) {
					tAnnualExamination.setExpireDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// ��ע
				if (cols == 7) {
					tAnnualExamination.setDemo(cellValue);
					continue;
				}
			}
			
			log.info("��ʼ�������������¼�������Excel");
			// Ĭ�Ͽɼ�
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
	 * ���ü�¼֧�����
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
				// �����۾ɷ���
				if (cols == 8) {
						tVehicleExpense.setVehicleDepreciation(Long.parseLong(cellValue));
						continue;
				}
				// ��Ա����
				if (cols == 9) {
						tVehicleExpense.setPersonalExpenses(Long.parseLong(cellValue));
						continue;
				}
				// ����̯������
				if (cols == 10) {
						tVehicleExpense.setInsurance(Long.parseLong(cellValue));
						continue;
				}
				// ά�ޱ�������
				if (cols == 11) {
						tVehicleExpense.setMaintenance(Long.parseLong(cellValue));
						continue;
				}
				// ��·���ŷ���
				if (cols == 12) {
						tVehicleExpense.setToll(Long.parseLong(cellValue));
						continue;
				}
				// �������������
				if (cols == 13) {
						tVehicleExpense.setAnnualCheck(Long.parseLong(cellValue));
						continue;
				}
				// ����
				if (cols == 14) {
					tVehicleExpense.setCreateDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
			}
			log.info("��ʼ�������ü�¼֧������������Excel");
			// Ĭ�Ͽɼ�
			tVehicleExpense.setEmpCode(userInfo.getEmpCode());
			list.add(tVehicleExpense);
		}
		return list;
	}

	/**
	 * ��·�Ѽ�¼���
	 */
	public List addToll(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TToll tToll = new TToll();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId �ն�����
				if (cols == 0) {
					tToll.setDeviceId(cellValue);
					continue;
				}
				// �ɷ�����
				if (cols == 15) {
					tToll.setPayDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// �ɷѵص�
				if (cols == 16) {
					tToll.setPayPlace(cellValue);
					continue;
				}
				// �ɷѽ��
				if (cols == 17) {
						tToll.setExpenses(Long.parseLong(cellValue));
						continue;
				}
				// ������
				if (cols == 18) {
					tToll.setHandler(cellValue);
					continue;
				}
				// ��ע
				if (cols == 19) {
					tToll.setDemo(cellValue);
					continue;
				}
			}
			log.info("��ʼ���� ��·�Ѽ�¼����������Excel");
			// Ĭ�Ͽɼ�
			tToll.setEmpCode(userInfo.getEmpCode());
			list.add(tToll);
		}
		return list;
	}


	/**
	 * ����ά����¼���
	 */
	public List addVehiclesMaintenance(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TVehiclesMaintenance tVehiclesMaintenance = new TVehiclesMaintenance();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId �ն�����
				if (cols == 0) {
					tVehiclesMaintenance.setDeviceId(cellValue);
					continue;
				}
				// ά������
				if (cols == 20) {
					tVehiclesMaintenance.setMaintenanceDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// ά������
				if (cols == 21) {
						tVehiclesMaintenance.setExpenses(Long.parseLong(cellValue));
						continue;
				}
				// ά�����
				if (cols == 22) {
					tVehiclesMaintenance.setCondition(cellValue);
					continue;
				}
				// ������
				if (cols == 23) {
					tVehiclesMaintenance.setHandler(cellValue);
					continue;
				}
				//��������
				if (cols == 24) {
					tVehiclesMaintenance.setExpireDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// ��ע
				if (cols == 25) {
					tVehiclesMaintenance.setDemo(cellValue);
					continue;
				}
			}
			log.info("��ʼ��������ά����¼����������Excel");
			// Ĭ�Ͽɼ�
			tVehiclesMaintenance.setEmpCode(userInfo.getEmpCode());
			list.add(tVehiclesMaintenance);
		}
		return list;
	}


	/**
	 * ���շ��ü�¼���
	 */
	public List addInsurance(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TInsurance tInsurance = new TInsurance();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId �ն�����
				if (cols == 0) {
					tInsurance.setDeviceId(cellValue);
					continue;
				}
				// ���յ���
				if (cols == 26) {
					tInsurance.setInsuranceNo(cellValue);
					continue;
				}
				//��������
				if (cols == 27) {
					tInsurance.setInsuranceName(cellValue);
					continue;
				}
				// ���չ�˾
				if (cols == 28) {
					tInsurance.setInsuranceCo(cellValue);
					continue;
				}
				//Ͷ������
				if (cols == 29) {
					tInsurance.setInsuranceDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				//��������
				if (cols == 30) {
					tInsurance.setExpireDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// ����
				if (cols == 31) {
						tInsurance.setSumInsured(Long.parseLong(cellValue));
						continue;
				}
				// ����
				if (cols == 32) {
						tInsurance.setPremium(Long.parseLong(cellValue));
						continue;
				}
				// ������
				if (cols == 33) {
					tInsurance.setHandler(cellValue);
					continue;
				}
			}
			log.info("��ʼ�������շ��ü�¼����������Excel");
			// Ĭ�Ͽɼ�
			tInsurance.setEmpCode(userInfo.getEmpCode());
			list.add(tInsurance);
		}
		return list;
	}

	/**
	 * ���ͼ�¼���
	 */
	public List addOiling(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TOiling tOiling = new TOiling();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId �ն�����
				if (cols == 0) {
					tOiling.setDeviceId(cellValue);
					continue;
				}
				// ������������
				if (cols == 34) {
						tOiling.setOilLiter(Long.parseLong(cellValue));
						continue;
				}
				//���ͽ��
				if (cols == 35) {
						tOiling.setOilCost(Long.parseLong(cellValue));
						continue;
				}
				// ��������
				if (cols == 36) {
					tOiling.setCreateDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
			}
			log.info("��ʼ�������ͼ�¼����������Excel");
			// Ĭ�Ͽɼ�
			tOiling.setEmpCode(userInfo.getEmpCode());
			list.add(tOiling);
		}
		return list;
	}
	/**
	 * ���ͽ��
	 */
	public List addDispatchMoney(UserInfo userInfo) {
		List list = new ArrayList();
		jxl.Sheet rs = this.workbook.getSheet(0);
		for (int rows = 1; rows < this.getRows(0); rows++) {
			TDispatchMoney tDispatchMoney = new TDispatchMoney();
			for (int cols = 0; cols < this.getColumns(0); cols++) {
				jxl.Cell cell = rs.getCell(cols, rows);
				String cellValue = cell.getContents();
				// deviceId �ն�����
				if (cols == 0) {
					tDispatchMoney.setDeviceId(cellValue);
					continue;
				}
				// ��������
				if (cols == 37) {
					tDispatchMoney.setDispatchDate(DateUtility
							.strToDate(cellValue, "yyyy/MM/dd"));
					continue;
				}
				// ��������
				if (cols == 38) {
					tDispatchMoney.setDispatchAmount(Long.parseLong(cellValue));
					continue;
				}
				// ������
				if (cols == 39) {
					tDispatchMoney.setFrmhandler(cellValue);
						continue;
				}
				// ��ע
				if (cols == 40) {
					tDispatchMoney.setFrmdemo(cellValue);
					continue;
				}
			}
			log.info("��ʼ���� ��·�Ѽ�¼����������Excel");
			// Ĭ�Ͽɼ�
			tDispatchMoney.setUserId(userInfo.getUserId());
			tDispatchMoney.setSaveDate(new Date());
			list.add(tDispatchMoney);
		}
		return list;
	}
}
