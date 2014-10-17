/**
 * 
 */
package com.autonavi.directl.device.xwrj;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.mapabc.geom.DPoint;
import com.autonavi.directl.CRC16;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.idirectl.AlarmAdapter;
import com.autonavi.directl.idirectl.TerminalParam;
 

/**
 * @author asia-auto ������ݱ�����
 */
public class XWRJAlarm extends AlarmAdapter {

	private String head = "7e00";

	private String simcard = "";

	private String head1 = "";

	public XWRJAlarm() {
	}

	/**
	 * 
	 */
	public XWRJAlarm(TerminalParam param) {
		this.terminalParam = param;
		simcard = param.getSimCard();
		head1 = "2626" + simcard + "020";
	}

	/**
	 * ���ö�����ش�
	 */
	public String setWarpAlarm(String seq, String distance) {

		String ret = "";
		String cmdsn = "";// �������к�
		cmdsn = Tools.convertToHex(seq, 8);

		String cmd = cmdsn + "020200" + Tools.convertToHex(distance, 4)
				+ "000001";
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = head1 + cmd1;
		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		// this.setByteArrayCmd(Tools.fromHexString(ret));
		//
		// try {
		// ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		// } catch (UnsupportedEncodingException e) {
		// // TODO �Զ����� catch ��
		// e.printStackTrace();
		// }
		return ret;
	}

	/**
	 * ���ٱ���
	 * 
	 */
	public String setSpeedAlarm(String seq, String speedType, String speed,
			String time) {
		String ret = "";
		String cmdsn = "";// �������к�
		cmdsn = Tools.convertToHex(seq, 8);

		if (speedType.equals("1")) {
			String t = time;// speed.substring(0, speed.indexOf(","));
			String spd = speed;// speed.substring(speed.indexOf(",") + 1);
			// Log.getInstance().outXWRJ//Loger("��������ʱ�䣺" + t + " �ٶ�ֵ��" + spd);
			String tmpSpeed = "000";

			try {
				double dSpeed = Double.parseDouble(spd);
				if (dSpeed > 999) {
					dSpeed = 999;
				}
				double hSpeed = dSpeed / 1.852;
				byte iSpeed = (byte) hSpeed;
				byte bt = Byte.valueOf(t);
				byte[] param = new byte[2];
				param[0] = iSpeed;
				param[1] = bt;

				String tmpSpeed1 = "" + iSpeed;
				tmpSpeed = Tools.convertToHex(tmpSpeed1, 2);
				String tt = Tools.convertToHex(t, 2);

				String cmd = cmdsn + "010e" + tmpSpeed + tt;
				String cmd1 = Tools.compressHexData(cmd);
				String cmd2 = head1 + cmd1;
				String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
				ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8)
						+ cmd3;

				this.setByteArrayCmd(Tools.fromHexString(ret));
				// Log.getInstance().outXWRJ//Loger(" ��������ٶ�ָ��:" + ret);

			} catch (java.lang.NumberFormatException ex) {
				ex.printStackTrace();
			}

		} else {
			String cmd = cmdsn + "0407";
			String cmd1 = Tools.compressHexData(cmd);
			String cmd2 = head1 + cmd1;
			String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
			ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
			// this.setByteArrayCmd(Tools.fromHexString(ret));
			// Log.getInstance().outXWRJ//Loger(" ȡ�����ٱ���ָ��:" + ret);
		}

		// try {
		// ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		// } catch (UnsupportedEncodingException e) {
		// // TODO �Զ����� catch ��
		// e.printStackTrace();
		// }
		return ret;
	}

	/**
	 * ��������򱨾�����
	 * 
	 * @param seq��ָ�����к�
	 * @param alarmType������
	 *            0�� 1�� 2ȡ��
	 * @param rangeId��������
	 * @param startDate:������Ч�Ŀ�ʼʱ��
	 * @param endDate:������Ч�Ľ���ʱ��
	 * @param points����������
	 * @return
	 */
	public String setRangeAlarm(String seq, String alarmType, String rangeId,
			String startDate, String endDate, com.mapabc.geom.DPoint[] points) {
		String ret = "";
		String cmdsn = "";// �������к�
		cmdsn = Tools.convertToHex(seq, 8);

		String cmd = cmdsn;
		String precmd = "";
		String contidition = "ffffffff";// Ĭ�ϱ���״̬�֣��κ�״̬�´������򱨾��������ϱ���
		String type = Tools.convertToHex(alarmType, 2);// ��������
		String num = Tools.convertToHex(rangeId, 2); // ������
		String pointNum = Tools.convertToHex(points.length + "", 4); // �켣����
		String stime = startDate.replaceAll("[-:\\s]", "").substring(2);// ��ʼʱ��
																		// yyMMddhhmmss
		String etime = endDate.replaceAll("[-:\\s]", "").substring(2);// ��������
																		// yyMMddhhmmss
		String xy = "";// ��γ�ȴ�
		for (int i = 0; i < points.length; i++) {
			DPoint dp = points[i];
			xy += this.getFormatXY(String.valueOf(dp.x))
					+ this.getFormatXY(String.valueOf(dp.y));
		}
		if (alarmType.equals("0") || alarmType.equals("1")) {// �����

			precmd = "013d" + num + type + pointNum + xy + stime + etime;

		} else if (alarmType.equals("2")) {
			precmd = "0405";// ȡ�����򱨾�
		}
		String cmd1 = Tools.compressHexData(cmd + precmd);
		String cmd2 = head1 + cmd1;// "78DA6360606064616104000020000B";

		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		 
		return ret;
	}

	/**
	 * 
	 * @param v
	 *            0 :�رձ���
	 * @return String
	 * @todo Implement this com.autonavi.directl.idirectl.Alarm method
	 */
	public String stopAlarm(String v) {
		String ret = "";
		String cmd = "00000001040401";
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = head1 + cmd1;// "78DA6360606064616104000020000B";

		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		// Log.getInstance().outXWRJ//Loger("ֹͣ����ָ��:" + ret);

		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * �����¶ȱ���
	 * 
	 * @param up�������¶�
	 * @param down�������¶�
	 * @return
	 */
	public String setTemperatureAlarm(String up, String down) {
		String ret = "";
		int upt = Integer.parseInt(up);
		int downt = Integer.parseInt(down);
		if (upt > 327)
			upt = 327;
		if (downt < -327)
			downt = -327;

		String upts = Tools.int2Hexstring(upt, 4);
		String downts = Tools.int2Hexstring(downt, 4);

		String cmd = "00000000012f" + downts + upts;
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = head1 + cmd1;// "78DA6360606064616104000020000B";

		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		// Log.getInstance().outXWRJ//Loger("�¶ȷ�Χ����ָ��:" + ret);

		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;

	}

	// �������򱨾�
	public String setRangeAlarmById(String alarmType, String rangeId, String x,
			String y, String X, String Y) {

		String ret = "";
		String cmd = "00000000";
		String precmd = "";
		if (alarmType.equals("0")) {
			precmd = "010f0000" + Tools.convertToHex("4", 4)
					+ Tools.convertToHex("4", 4) + getFormatXY(x)
					+ getFormatXY(y) + getFormatXY(X) + getFormatXY(Y);
		} else if (alarmType.equals("1")) {
			precmd = "010f00" + Tools.convertToHex("1", 2)
					+ Tools.convertToHex("4", 4) + getFormatXY(x)
					+ getFormatXY(y) + getFormatXY(X) + getFormatXY(Y);
			;
		} else if (alarmType.equals("2")) {
			precmd = "010f00" + Tools.convertToHex("2", 2) + getFormatXY(x)
					+ getFormatXY(y) + getFormatXY(X) + getFormatXY(Y);
			;
		} else if (alarmType.equals("3")) {
			precmd = "0405";// ȡ�����򱨾�
		}
		String cmd1 = Tools.compressHexData(cmd + precmd);
		String cmd2 = head1 + cmd1;// "78DA6360606064616104000020000B";

		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		ret = head + Tools.convertToHex(cmd3.length() / 2 + "", 8) + cmd3;
		this.setByteArrayCmd(Tools.fromHexString(ret));
		// Log.getInstance().outXWRJ//Loger("���򱨾�����ָ��:" + ret);

		try {
			ret = new String(Tools.fromHexString(ret), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	private String getFormatXY(String xy) {
		String ret = "";
		String du = xy.substring(0, xy.lastIndexOf("."));// ��
		String fen = "0" + xy.substring(xy.lastIndexOf("."));// С����
		double df = Double.parseDouble(fen) * 60;// ��
		int zf = (int) df; // ������
		String dot = String.valueOf(df).substring(
				String.valueOf(df).lastIndexOf(".") + 1);// ��С������
		String sb = dot.substring(0, 2);// С����ʮ���ٷ�λ
		String qw = dot.substring(2, 4);// С����ǧ�����λ
		ret = Tools.convertToHex(du, 2) + Tools.int2Hexstring(zf, 2)
				+ Tools.convertToHex(sb, 2) + Tools.convertToHex(qw, 2);

		return ret;
	}

	public static void main(String[] args) {
		XWRJAlarm x = new XWRJAlarm();
		String cmd = "000000000407";
		String cmd1 = Tools.compressHexData(cmd);
		String cmd2 = x.head1 + cmd1;
		String cmd3 = cmd2 + CRC16.CRC16(Tools.fromHexString(cmd2));
		String ret = x.head + Tools.convertToHex(cmd3.length() / 2 + "", 8)
				+ cmd3;

		x.getFormatXY("116.394021");
		System.out.println(ret);
	}

}
