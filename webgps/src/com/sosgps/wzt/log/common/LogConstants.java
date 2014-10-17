package com.sosgps.wzt.log.common;

/**
 * <p>
 * Title: LogConstants
 * </p>
 * <p>
 * Description: ��־����
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: ͼ�˿Ƽ�
 * </p>
 * 
 * @author λ��ͨ��Ŀ�� ����
 * @version 1.0
 */
public class LogConstants {
	/* �û���ɫȨ�ޱ��� */
	public static final String MANAGER_F_CODE = "F-005";// �û���ɫȨ��ģ��
	public static final String MANAGER_C_MODULEGROUP_CODE = "F-005-001";// Ȩ����ģ��
	public static final String MANAGER_C_MODULE_CODE = "F-005-002";// Ȩ��ģ��
	public static final String MANAGER_C_ROLE_CODE = "F-005-003";// ��ɫģ��
	public static final String MANAGER_C_USER_CODE = "F-005-004";// �û�ģ��
	/* ��־������� */
	public static final String LOG_F_CODE = "F-006";// ��־����
	public static final String LOG_C_LOGIN_CODE = "F-006-001";// ��¼��־
	public static final String LOG_C_OPT_CODE = "F-006-002";// ������־

	public static final String LOCATE_SERVICE_F_CODE = "";

	/* ��Դ����ģ����־ */
	public static final String LOG_SOURCE_MANAGE = "F-002";// ��Դ����
	public static final String LOG_SOURCE_MANAGE_ENT = "F-002-001";// ��ҵ����
	public static final String LOG_SOURCE_MANAGE_TERMINAL = "F-002-002";// �ն˹���
	public static final String LOG_SOURCE_MANAGE_TERMINAL_GROUP = "F-002-003";// �ն������
	public static final String LOG_SOURCE_MANAGE_POI = "F-002-004";// POI����
	public static final String LOG_SOURCE_MANAGE_SAMPLE = "F-002-005";// �������ù���
	public static final String LOG_SOURCE_MANAGE_ALARM = "F-002-006";// �������ù�
	public static final String LOG_SOURCE_SPEED_ALARM = "F-002-007";// ���ٱ�������
	public static final String LOG_SOURCE_AREA_ALARM = "F-002-008";// ���򱨾�����
	public static final String LOG_SOURCE_INTERVAL_FRE = "F-002-009";// GPS�ϱ�Ƶ������
	public static final String LOG_SOURCE_TAKE_PHOTL = "F-002-010";// ������Ƭָ��
	public static final String LOG_SOURCE_OIL_ELEC = "F-002-011";// �͵����ָ��
	public static final String LOG_SOURCE_MESSAGE = "F-002-012";// ������Ϣָ��
	public static final String LOG_SOURCE_STOP_ALARM = "F-002-013";// ֹͣ������Ϣָ��
	public static final String LOG_SOURCE_ALARM_PARAM = "F-002-014";// ����������Ϣָ��
	public static final String LOG_SOURCE_CAR_TYPE = "F-002-015";// ����������Ϣָ��
	public static final String LOG_SOURCE_STRUCTIONS = "F-002-016";// ָ�����
	public static final String LOG_SOURCE_LINE_ALARM = "F-002-017";// ·�߱�������
	
	public static final String LOG_SOURCE_AREA_ADD = "F-002-018";// ��������
	public static final String LOG_SOURCE_AREA_UPDATE = "F-002-019";// �޸�����
	public static final String LOG_SOURCE_AREA_DELETE = "F-002-020";// ɾ������
	public static final String LOG_SOURCE_AREA_TERM_DELETE = "F-002-021";// ɾ���ն���������
	public static final String LOG_SOURCE_AREA_TERM_UPDATE = "F-002-022";// �ն���������
	
	
	public static final String EDIT_TERMINAL_IN_GROUP = "F-002-020";// �ն������ն���
	public static final String EDIT_GROUP_IN_GROUP = "F-002-021";// �ն���������ϵ����
	/*���Ÿ�֪*/
	public static final String LOG_SMS_NOTIFI_PL_MODIFY = "F-002-022";// �����޸Ķ��Ÿ�֪
	public static final String LOG_SMS_NOTIFI_MODIFY = "F-002-023";// �޸Ķ��Ÿ�֪
	public static final String LOG_SMS_NOTIFI_PL_DELETE = "F-002-024";// ����ɾ�����Ÿ�֪
	public static final String LOG_SMS_NOTIFI_DELETE = "F-002-025";// ɾ�����Ÿ�֪
	
	//add by 2012-12-17 ��������
	
	public static final String LOG_SOURCE_RestoreOilPower= "F-002-026"; //�ָ��͵�
	public static final String LOG_SOURCE_LiftPanic = "F-002-027"; //����پ�
	public static final String LOG_SOURCE_LiftSpeeding = "F-002-028"; //�������
	public static final String LOG_SOURCE_LiftArea = "F-002-029"; //�������
	public static final String LOG_SOURCE_OffOilPower = "F-002-030";

	/* ͳ�Ʋ�ѯģ����־ F-003 */
	public static final String LOG_STAT = "F-003";// ͳ�Ʋ�ѯ
	public static final String LOG_STAT_EMP_LOC = "F-003-001";// ��ҵ��λͳ��
	public static final String LOG_STAT_PEAK_LOC = "F-003-002";// ��λ��ֵͳ��
	public static final String LOG_STAT_USER_LOC = "F-003-003";// ��Ա��λͳ��
	public static final String LOG_STAT_EMP = "F-003-004"; // ��ҵͳ��
	public static final String LOG_STAT_FUNCTION = "F-003-005"; // ����ͳ��
	public static final String LOG_STAT_ALARM = "F-003-006"; // ����ͳ��
	public static final String LOG_STAT_REPORTDETAIL ="F-003-007"; //��Աλ����ϸͳ��
	public static final String LOG_STAT_SALSEMAN = "F-003-008";//ҵ��Ա���ڱ���ͳ��
	public static final String LOG_STAT_VISIT = "F-003-009";//�ͻ����ݷô���ͳ��
	public static final String LOG_STAT_CUSTOM = "F-003-010";//ҵ��Ա����ͳ��
	public static final String LOG_STAT_CAR = "F-003-011";//��λ��Ϣͳ��
	public static final String LOG_STAT_CARINFO = "F-003-012";//������Ϣͳ��
	public static final String LOG_STAT_CARARRIVAL = "F-003-013";//����������Ϣͳ��
	public static final String LOG_STAT_AREAALARM = "F-003-014"; // ���򱨾�ͳ��
	public static final String LOG_STAT_SPEEDALARM = "F-003-015"; // ���ٱ���ͳ��
	public static final String LOG_STAT_HOLDALARM = "F-003-016"; // ��������ͳ��
	public static final String LOG_STAT_DISTANCE = "F-003-017"; // ���ͳ��
	public static final String LOG_STAT_HOURDISTANCE = "F-003-018"; // ��ʱ���ͳ��
	public static final String LOG_STAT_TOTALDISTANCE = "F-003-019"; // �����ͳ��
	public static final String LOG_STAT_SENSOR = "F-003-020"; // �Ŵ���Ϣ
	public static final String LOG_STAT_ATTENDANCE_RECORD = "F-003-021"; // ����ͳ��
	public static final String LOG_STAT_STRUCTIONS_RECORD = "F-003-022"; // ָ����Ϣͳ��
	public static final String LOG_STAT_SignBillDetail = "F-003-023"; // ǩ������ϸ
	public static final String LOG_STAT_CashDetail = "F-003-024"; // �ؿ����ϸ
	public static final String LOG_STAT_CostDetail = "F-003-025"; // ���ö���ϸ
	public static final String LOG_STAT_VisitReport = "F-003-026"; // ��ѯ�ͻ��ݷ���ϸ��¼
	public static final String LOG_STAT_VisitRank = "F-003-027"; // ��ѯ�ͻ��ݷô�����
	public static final String LOG_STAT_CusVisit_v2_1 = "F-003-028"; // �ͻ����ݷô���ͳ��
	public static final String LOG_STAT_CusVisit_Detail_v2_1 = "F-003-029"; // �ͻ����ݷ�ͳ����ϸ��Ϣ
	public static final String LOG_STAT_Visit_v2_1 = "F-003-030"; // ҵ��Ա����ͳ��
	public static final String LOG_STAT_Visit_Detail_v2_1 = "F-003-031"; // ҵ��Ա������ϸ��Ϣ
	
	//add by 2012-12-17 zss 
	public static final String LOG_STAT_SpeedExcel = "F-003-036"; // ���ٱ���ͳ��
	public static final String LOG_STAT_RemoveAllAlarm = "F-003-037"; //�������
	/*add by 2012-12-17 zss ���۱���*/
	public static final String LOG_STAT_SalesReported  = "F-003-032";// ��ѯ�����ϱ� 
	public static final String LOG_STAT_PromotionaRreported = "F-003-033"; // ��ѯ�����ϱ�
	public static final String LOG_STAT_SalesExcel  = "F-003-034";//  ���������ϱ� 
	public static final String LOG_STAT_PromotionaExcel = "F-003-035"; // ���������ϱ�
	
	/*add by 2012-12-18 zss ���� */
	public static final String LOG_STAT_Attendance = "F-003-038"; // ������ϸ
	public static final String LOG_STAT_AttendanceExcel = "F-003-039"; //������˾����������
	public static final String LOG_STAT_AttendanceShowCompany = "F-003-040"; // չʾ��˾���Ŀ�������
	public static final String LOG_STAT_AttendanceShowStaff = "F-003-041"; // չʾ��Ա��������
	public static final String LOG_STAT_AttendanceStaffExcel = "F-003-042"; // ������Ա���ڻ��ܱ�������
	public static final String LOG_STAT_AttendanceReconstructedExcel = "F-003-043"; // �ع�������Ա���ڻ��ܱ�������
	public static final String LOG_STAT_AttendanceModifyl = "F-003-044"; //��Ա���ڱ����޸� 
	
	/*add by 2012-12-18 zss ����*/
	public static final String LOG_STAT_TravelListExcel = "F-003-045"; //����������Ϣ���� 
	public static final String LOG_STAT_TravelList = "F-003-046"; //��ѯ������Ϣ��ϸ 
	public static final String LOG_STAT_TravelVerify = "F-003-047"; //������� 
	
	/*add by 2012-12-18 zss ҵ��Ա*/
	public static final String LOG_STAT_VisitListExcel = "F-003-048"; //�����ݷÿ��ڱ�Excel
	public static final String LOG_STAT_VisitExcel = "F-003-049"; //����ǩ��ǩ����¼
	public static final String LOG_STAT_VisitRanking = "F-003-050"; //��ѯ�ͻ��ݷô������б�
	public static final String LOG_STAT_VisitRankingExcel = "F-003-051"; //�����ͻ��ݷô������б�Excel
	public static final String LOG_STAT_VisitGraph = "F-003-052"; //ȡ��ҵ��Ա��������ͼ
	public static final String LOG_STAT_VisitHistorical  = "F-003-053"; //ȡ��ҵ��Ա������ʷ����ͼ
	public static final String LOG_STAT_VisitedGraph = "F-003-054"; //ȡ�ÿͻ����ݷ�����ͼ
	public static final String LOG_STAT_VisitedHistorical  = "F-003-055"; //ȡ�ÿͻ����ݷ���ʷ����ͼ

	public static final String LOG_STAT_VisitDetail  = "F-003-056"; //��ѯҵ��Ա������ϸ��¼
	public static final String LOG_STAT_VisitedStatistics  = "F-003-057"; //�ͻ����ݷ�ͳ��
	public static final String LOG_STAT_VisitStatistics  = "F-003-058"; //��ѯҵ��Ա����ͳ�Ʊ�  
	public static final String LOG_STAT_VisitStatisticsDetail  = "F-003-059"; //��ѯҵ��Ա������ϸ��
	public static final String LOG_STAT_VisitedDetail  = "F-003-060"; //��ѯ��ϸ�ı��ݷÿͻ���ϢLOG_STAT_VisitedDashboard
	public static final String LOG_STAT_VisitedDashboard  = "F-003-061"; //�ͻ����ݷ��Ǳ���
	public static final String LOG_STAT_VisitDashboard  = "F-003-062"; //Ա�����ô�����Ǳ���

	/*add by 2012-12-18 zss �ֻ����ģ��*/
	public static final String LOG_STAT_MobileList  = "F-003-063"; //Ա�����ô�����Ǳ���
	
	/*add by 2012-12-18 zss  ǩ������*/
	public static final String LOG_STAT_SigningGraph  = "F-003-064"; //ȡ��ǩ��������ͼ
	public static final String LOG_STAT_SigningHistoryGraph  = "F-003-065"; //ȡ��ǩ������ʷ����ͼ
	public static final String LOG_STAT_SigningDetail  = "F-003-066"; //ǩ������ϸ�����ѯ
	public static final String LOG_STAT_SigningExcel  = "F-003-067"; //ǩ������ϸ������
	
	public static final String LOG_STAT_SigningAudit  = "F-003-068"; //ȡ��ǩ������ʷ����ͼ
	public static final String LOG_STAT_SigningDashboard  = "F-003-069"; //ȡ��ǩ��������ͼ
	
	/*add by 2012-12-18 zss  �ͻ�*/
	public static final String LOG_STAT_VisitedByDetail  = "F-003-070"; //��ѯĳ���ͻ����ݷ���ϸ��Ϣ
	public static final String LOG_STAT_VisitCustomerDetail  = "F-003-071"; //�鿴�ݷô�����ϸͳ��(ĳ��ҵ��Ա�ݷÿͻ���ϸ)
	
	/*add by 2012-12-18 zss  �ͻ�*/
	public static final String LOG_STAT_AttendanceDetail  = "F-003-072"; //�鿴ҵ��Ա���ڱ�����ϸͳ��
	public static final String LOG_STAT_VisitNumber  = "F-003-073"; //��ѯ�ݷõص���
	
	
	/* ������ϸ */
	public static final String LOG_Exp = "F-004";// ��������
	public static final String LOG_Exp_SignBillDetail = "F-004-001"; // ����ǩ������ϸ
	public static final String LOG_Exp_CashDetail = "F-004-002"; // �����ؿ����ϸ
	public static final String LOG_Exp_CostDetail = "F-004-003"; // �������ö���ϸ
	public static final String LOG_Exp_VisitReport = "F-004-004"; // �����ͻ��ݷ���ϸ��¼
	public static final String LOG_Exp_VisitRank = "F-004-005"; // �����ͻ��ݷô�����
	
	/* ��˲��� */
	public static final String LOG_Approved = "F-007";// ��˲���
	public static final String LOG_Approved_SignBill = "F-007-001"; // ���ǩ��
	public static final String LOG_Approved_Cash = "F-007-002"; // ��˻ؿ�
	public static final String LOG_Approved_Cost = "F-007-003"; // ��˷���
	public static final String LOG_Approved_Visit = "F-007-004"; // ��˿ͻ��ݷ�

	/* �Ǳ��� */
	public static final String LOG_Gauge = "F-011";// �Ǳ���
	public static final String LOG_Gauge_SignBill = "F-011-001"; // ǩ��
	public static final String LOG_Gauge_Cash = "F-011-002"; // �ؿ�
	public static final String LOG_Gauge_Cost = "F-011-003"; // ����
	public static final String LOG_Gauge_Visit = "F-011-004"; // Ա�����ô����
	public static final String LOG_Gauge_CusVisit = "F-011-005"; // �ͻ��ݷø�����
	
	/* �ն˲��� */
	public static final String TERMINAL_PARAMTER = "F-008";
	public static final String TERMINAL_PARAMTER_QUERY = "F-008-01";// ������ѯ
	/* �������� */
	public static final String SIMPLE_SET = "F-009";// ��������
	public static final String SIMPLE_SET_ADD = "F-009-001";// ������������
	public static final String SIMPLE_SET_MODIFY = "F-009-002";// �޸Ĳ�������
	public static final String SIMPLE_SET_DELETE = "F-009-003";// ɾ����������
	public static final String SIMPLE_SET_TERMINAL = "F-009-004";// �ն����ò���
	public static final String SIMPLE_SET_CANCEL = "F-009-005";// �ն�ȡ������
	/* �������� */
	public static final String ATTENDANCE_SET = "F-010";// ��������
	public static final String ATTENDANCE_SET_ADD = "F-010-001";// �������ڹ���
	public static final String ATTENDANCE_SET_MODIFY = "F-010-002";// �޸Ŀ��ڹ���
	public static final String ATTENDANCE_SET_DELETE = "F-010-003";// ɾ�����ڹ���
	public static final String ATTENDANCE_SET_TERMINAL = "F-010-004";// �ն��������ڹ���
	public static final String ATTENDANCE_SET_CANCEL = "F-010-005";// �ն�ȡ�����ڹ���
	public static final String ATTENDANCE_SET_CANCEL_ALL = "F-010-006";// �ն�ȡ�����п��ڹ���

	
	/* �ֻ����� */
	public static final String LBSATT_SET = "F-012";
	public static final String LBSATT_SET_INSERTDB = "F-012-001"; //����
	
	/*ͼ�����*/
	public static final String POIMANAGE_SET = "F-013"; 
	public static final String POIMANAGE_SET_UPDATELAYERS = "F-013-001";//����ͼ����ʾ����
	
	/*����ͳ��*/
	public static final String SPEEDMANAGE_SET="F-014";
	public static final String SPEEDMANAGE_SET_showDayList = "F-014-001";
	public static final String SPEEDMANAGE_SET_showMonthList = "F-014-002";
	public static final String SPEEDMANAGE_SET_showYearList = "F-014-003";
	
	/*��ʱ����*/
	public static final String TIMER_SET="F-015";
	public static final String TIMER_SET_GETLOCDESC="F-015-001";
	
	/*�켣���*/
	public static final String TRACK_REPLAY="F-016";
	public static final String TRACK_REPLAY_QUERY="F-016-001";//�켣��ѯ
	public static final String TRACK_REPLAY_REALTIEMLOC="F-016-002";//��ʱ��λ
	
	/*����*/
	public static final String SMS_SET="F-017";
	public static final String SMS_SET_SEND="F-017-001";//���ŷ���
	public static final String SMS_SET_QUERY="F-017-002";//�ѷ��Ͷ��Ų�ѯ
	public static final String SMS_SET_RECEIVE="F-017-003";//��ѯ���н��ն��Ų�ѯ
	
	//add by 2012-12-17 zss
	public static final String SMS_SET_UNREAD="F-017-004";//��ѯδ������
	public static final String SMS_SET_UNREADEXCEL="F-017-005";//����δ������
	public static final String SMS_SET_UNREADUPDATE="F-017-006";//�޸�δ������Ϊ�Ѷ�
	public static final String SMS_SET_RECEIVEEXCEL="F-017-007";//�������н��ն���
	public static final String SMS_SET_QUERYEXCEL="F-017-008";//�����ѷ��Ͷ���
	

	/*ҵ��Ա��־*/
	public static final String DIARY_SET="F-018";
	public static final String DIARY_SET_QUERY_USER="F-018-001";//��ѯ��¼ҵ��Ա��־
	public static final String DIARY_SET_ADD_USER="F-018-002";//���ҵ��Ա��־
	public static final String DIARY_SET_UPDATE_USER="F-018-003";//�޸�ҵ��Ա��־
	public static final String DIARY_SET_REMARK_USER="F-018-004";//��עҵ��Ա��־
	
	/*���ͼ�¼*/
	public static final String OILING_SET = "F-019";
	public static final String OILING_SET_ADD = "F-019-001";    //���
	public static final String OILING_SET_UPDATE = "F-019-002"; //�޸�
	public static final String OILING_SET_DELETE = "F-019-003"; //ɾ��
	
	/*�������ü�¼*/
	public static final String VEHICLE_EXPENSE_SET = "F-020";
	public static final String VEHICLE_EXPENSE_SET_ADD = "F-020-001";    //���
	public static final String VEHICLE_EXPENSE_SET_UPDATE = "F-020-002"; //�޸�
	public static final String VEHICLE_EXPENSE_SET_DELETE = "F-020-003"; //ɾ��
	
	/*��������*/
	public static final String CAR_TYPE_INFO_SET = "F-021";
	public static final String CAR_TYPE_INFO_SET_ADD = "F-021-001";    //���
	public static final String CAR_TYPE_INFO_SET_UPDATE = "F-021-002"; //�޸�
	public static final String CAR_TYPE_INFO_SET_DELETE = "F-021-003"; //ɾ��

	/*ҵ��Ա��־��ע*/
	public static final String DIARY_MARK_SET="F-022";
	public static final String DIARY_MARK_SET_QUERY_USER="F-022-001";//��ѯ��¼ҵ��Ա��־
	public static final String DIARY_MARK_SET_ADD_USER="F-022-002";//���ҵ��Ա��־
	public static final String DIARY_MARK_SET_UPDATE_USER="F-022-003";//�޸�ҵ��Ա��־
	public static final String DIARY_MARK_SET_REMARK_USER="F-022-004";//��עҵ��Ա��־
	
	/*�������ռ�¼��ע*/
	public static final String INSURANCE_SET="F-023";
	public static final String INSURANCE_SET_QUERY="F-023-001";//��ѯ����
	public static final String INSURANCE_SET_ADD="F-023-002";//��ӱ���
	public static final String INSURANCE_SET_UPDATE="F-023-003";//�޸ı���
	public static final String INSURANCE_SET_DELETE="F-023-004";//ɾ������
	
	/*���������¼��ע*/
	public static final String ANNUAL_EXAMINATION_SET="F-024";
	public static final String ANNUAL_EXAMINATION_SET_QUERY="F-024-001";//��ѯ�����¼
	public static final String ANNUAL_EXAMINATION_SET_ADD="F-024-002";//��������¼
	public static final String ANNUAL_EXAMINATION_SET_UPDATE="F-024-003";//�޸������¼
	public static final String ANNUAL_EXAMINATION_SET_DELETE="F-024-004";//ɾ�������¼

	/*����ά����¼��ע*/
	public static final String VEHICLES_MAINTENANCE_SET="F-025";
	public static final String VEHICLES_MAINTENANCE_SET_QUERY="F-025-001";//��ѯ����ά����¼
	public static final String VEHICLES_MAINTENANCE_SET_ADD="F-025-002";//��ӳ���ά����¼
	public static final String VEHICLES_MAINTENANCE_SET_UPDATE="F-025-003";//�޸ĳ���ά����¼
	public static final String VEHICLES_MAINTENANCE_SET_DELETE="F-025-004";//ɾ������ά����¼

	/*��·���ż�¼��ע*/
	public static final String Toll_SET="F-026";
	public static final String Toll_SET_QUERY="F-026-001";//��ѯ��·���ż�¼
	public static final String Toll_SET_ADD="F-026-002";//��ӹ�·���Ż���¼
	public static final String Toll_SET_UPDATE="F-026-003";//�޸Ĺ�·���ż�¼
	public static final String Toll_SET_DELETE="F-026-004";//ɾ����·���ż�¼
	
	/*���������¼��ע*/
	public static final String DriverLicense_SET="F-027";
	public static final String DriverLicense_SET_QUERY="F-027-001";//��ѯ���������¼
	public static final String DriverLicense_SET_ADD="F-027-002";//��Ӽ��������¼
	public static final String DriverLicense_SET_UPDATE="F-027-003";//�޸ļ��������¼
	public static final String DriverLicense_SET_DELETE="F-027-004";//ɾ�����������¼
	
}
