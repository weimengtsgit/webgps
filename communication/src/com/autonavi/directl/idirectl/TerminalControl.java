package com.autonavi.directl.idirectl;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:�ն˿��Ƴ�����
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.mapabc.com
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public abstract class TerminalControl extends BaseDictate {
	public TerminalControl() {
	}

	/**
	 * ��������
	 */
	public abstract String reset(String seq);

	/**
	 * �ر�
	 */
	public abstract String turnOff(String seq,String v);

	/**
	 * �������� state:0����Ͽ�����,1���������
	 */
	public abstract String setOilState(String seq,String state);

	/**
	 * ���ص��� state:0����Ͽ�����,1����򿪵���
	 */
	public abstract String setEletricityState(String seq,String state);

	/**
	 * ң��Ϩ�� type:0����Ϩ��1��һ���ٶ�Ϩ�� state:0Ϩ��1ȡ��Ϩ��
	 */
	public abstract String setFlameout(String seq,String type, String state, String speed);

	/**
	 * ͨ���趨 type:0ͨ��ģʽ��1����ģʽ state:0�����Ⲧ��1��ֹ�Ⲧ
	 */
	public abstract String setCallSetting(String seq,String type, String state);

	/**
	 * ���뿪�� pwd :����
	 */
	public abstract String setOpenWithPwd(String seq,String pwd);

	/**
	 * �������� state:0������������,1�������������
	 */
	public abstract String setDoorState(String seq,String state);

	/**
	 * ���ü���
	 */
	public abstract String setListening(String seq,String state, String tel);

	/**
	 * �����ն˹���״̬
	 */
	public abstract String setWorkState(String seq,String v);

	/**
	 * ����������Ϣ
	 */
	public abstract String sentPubMsg(String seq,String msg);

	/**
	 * ��������
	 * 
	 * @param v���Ƿ��Ƿ�Χ����
	 * @param xy
	 * @param XY
	 * @param msg
	 * @return
	 */
	public abstract String quickRevertShortMessage(String seq,String ptype, EarthCoord xy,
			EarthCoord XY, String msg);

	/**
	 * �绰����
	 * 
	 * @param v
	 * @param xy
	 * @param XY
	 * @param tel
	 * @param msg
	 * @return
	 */
	public abstract String quickRevertCalling(String seq,String ptype, EarthCoord xy,
			EarthCoord XY, String tel, String msg);

	/**
	 * ���ŵ���
	 */
	public abstract String shortMessageAttemper(String seq,String ptype, EarthCoord xy,
			EarthCoord XY, String msg);

	/**
	 * �绰����
	 */
	public abstract String telphoneAttemper(String seq,String ptype, EarthCoord xy,
			EarthCoord XY, String tel, String msg);

	/**
	 * ���ֻش�
	 */
	public abstract String replyNum(String seq,String ptype, EarthCoord xy, EarthCoord XY,
			String msg);

	/**
	 * Ҫ���ն˽���GPRSģʽ
	 */
	public abstract String gointoGPRS(String seq,String v, String pTime);

	/**
	 * ĳ��Χ����
	 */
	public abstract String rangeCalling(String seq,EarthCoord xy, EarthCoord XY);

	/**
	 * ���ö�ʱ����ʡ��ģʽ
	 */
	public abstract String startSaveModeByTime(String seq,String time);

	/**
	 * �ն˻ز�
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String callBack(String seq,String tel);

	/**
	 * ���ն˸���������Ϣ
	 * 
	 * @param affix:�ն˸�������,�磺��ʾƽ����Ϊ04
	 * @param protocol��Э��
	 * @param msg�����͵���Ϣ
	 * @return
	 */
	public abstract String sentAffixMsg(String seq,String affix, String protocol,
			String msg);

	/**
	 * �������ӿ���
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String testConnectSwitch(String seq,String testState);

	/**
	 * ���ֵ�����Ϣ msg��������Ϣ
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String SendMessage(String seq,String msg);

	/**
	 * �·�����Ϣ��
	 * 
	 * @param type
	 *            String --- ��Ϣ����
	 * @param msg
	 *            String --- ��Ϣ
	 * @return String
	 */
	public abstract String sendMessageByType(String seq,String type, String msg);

	/**
	 * ȡ��������Ϣ yl��1ȡ��ҽ�ƣ�0��Ч��jt��1ȡ����ͨ���� 0��Ч dh��1ȡ��������0 ��Ч
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String CancelHelp(String seq,String yl, String jt, String dh);

	/**
	 * �Լ� type �Լ����� 1���Լ� 2����״̬���� 3 ��������״̬���� 4 ǿ�д����� 5 gps�ϵ縴λ 6 gps������ 7
	 * gps������1 8 gps������2 9 gps������ 10 gps������λ 11 gps��Դ�ر� 12 gps��Դ��
	 * 
	 * @param tel
	 * @return
	 */
	public abstract String SelfCheck(String seq,String type);

	/**
	 * ·���·�
	 * 
	 * @param points
	 *            String
	 * @return String
	 */
	public abstract String sendRoute(String seq,String points);

	/**
	 * Ŀ�ĵ��·�
	 * 
	 * @param point
	 *            String
	 * @return String
	 */
	public abstract String sendDest(String seq,String point);

	/**
	 * �ظ���Ϣ
	 * 
	 * @return String
	 */
	public abstract String restoreInfor(String seq,String simcard);

	/**
	 * ��ͨ�����Ϣ����ʽ
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public abstract String commonCmdFormat(String seq,String content);

	/**
	 * �г���Ϣ ��Ҫ˾���ظ�����Ϣ�����԰�ȷ�ϻ�ȡ����
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public abstract String callCarInfor(String seq,String content);

	/**
	 * ����ָ�
	 * 
	 * @param centerNum
	 *            String
	 * @return String
	 */
	public abstract String pwdResume(String seq,String centerNum);

	/**
	 * �����Ϣ ��������ʾ����Ϣ��
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public abstract String sentMsgAloneToLED(String seq,String content);

	/**
	 * �������/�޸���Ϣ �����������Ϣ�ı���)
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	public abstract String batchSentMsgToLED(String seq,String content);

	/**
	 * ɾ����Ϣ
	 * 
	 * @param inforNums
	 *            String --- ��Ϣ����ַ���
	 * @return String
	 */
	public abstract String deleteInfor(String seq,String inforNums);

	/**
	 * �����ʾ������
	 * 
	 * @return String
	 */
	public abstract String clearLEDContent();

	/**
	 * ������ݷ��ͣ�������--->�նˣ�
	 * 
	 * @param content
	 *            String
	 */
	public abstract String sendExternalDate(String seq,String content);

	/**
	 * �ն����չ��ܣ������ǰ��н����ŵ�Э�飩
	 * 
	 * @param size
	 *            ��Ƭ��С
	 * @param action
	 *            ����
	 * @param no
	 *            ������
	 * @return
	 */
	public abstract String takePictures(String seq,String size, String action, String no);
	/**
	 * ��ȡ�ն�ͼƬ
	 * @param seq�����к�
	 * @param trackNo���ն�����ͨ����
	 * @param picId��ͼƬID
	 * @return
	 */
	public abstract String getPicture(String seq, String trackNo, String picId);

	/**
	 * ���Ͷϵ� ���Ϻ���̫Э�飩
	 * 
	 * @param state --
	 *            ״̬
	 * @param param --
	 *            ����
	 * @return
	 */
	public String stopOilAndElec(String seq,String state, String param) {
		return "";
	}

	// �����������ƣ�������ݣ�
	public abstract String setOilAndLock(String seq,String state, String param);

	// ����������,��·��������ݣ�
	public abstract String setCameraControl(String seq,String type, String action,
			String times, String interval, String zhiliang, String liangdu,
			String duibdu, String baohd, String huidu);
	
	public abstract String camera(String seq);

}
