package com.autonavi.directl.dbutil.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.pic.Picture;
import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.directl.dbutil.DbUtil;
import com.autonavi.directl.dbutil.service.DBService;
import com.autonavi.directl.parse.ParseBase;
import com.sos.sosgps.api.CoordAPI;
import com.sos.sosgps.api.DPoint;

public class DBServiceImpl implements DBService {

	// insert into shandong_sms_wait(wait_id,longid,desmobile,content)
	// values(shandong_seq_sms_id.nextval,'106511111111','134'26457580,'test')

	public boolean insertAlarmNotify(Connection conn, String longid,
			String user, String usermobile, String cont) {
		// TODO �Զ����ɷ������
		boolean flag = false;

		String sql = "insert into shandong_sms_wait(wait_id,longid,desmobile,content,report) values(shandong_seq_sms_id.nextval,?,?,?,3)";
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, longid);
			pst.setString(2, usermobile);
			pst.setString(3, cont);
			pst.executeUpdate();
			flag = true;
			Log.getInstance().outLog(
					"֪ͨ����Ա" + user + " " + usermobile + ":" + cont);
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			flag = false;
			Log.getInstance().errorLog("���汨��֪ͨ����ʧ��", e);
		} finally {
			DbOperation.release(null, null, pst, conn);
		}

		return flag;
	}

	// ��ѯ�ܼ������ն˵��û�
	public String[] getUserSimBinded(Connection con, String deviceId) {
		// TODO �Զ����ɷ������
		String sql = "select tu.user_contact phone,tt.term_name tname,tu.user_account uname   from  ref_term_group ttg ";
		sql += "inner join ref_user_tgroup rug on ttg.group_id=rug.group_id ";
		sql += "inner join t_user tu on tu.id=rug.user_id ";
		sql += "inner join t_terminal tt on tt.device_id=ttg.device_id ";
		sql += "where is_area_alarm=1 and tt.device_id=" + deviceId;
		Statement stm = null;
		ResultSet rs = null;
		String[] ret = null;

		try {

			stm = con.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				ret = new String[3];
				ret[0] = rs.getString("phone");// ==null?"":rs.getString("phone");//��½�û��ֻ�����
				ret[1] = rs.getString("tname");// ==null?"":rs.getString("tname");//�ն�����
				ret[2] = rs.getString("uname");// ==null?"":rs.getString("uname");//��½�û���
			}

		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			Log.getInstance().errorLog("��ѯ�ն˵Ŀɼ��û��쳣", e);
		} finally {
			DbOperation.release(stm, rs, null, con);
		}

		return ret;
	}

	/**
	 * ���������к�ָ��״̬(��ͯ�ֻ��ն�,��ǿ�ն�)
	 * 
	 * @param deviceId:�ն˺���
	 * @param state:״̬
	 *            0 �ɹ���1������ 2��ʧ��
	 * @param cmdId:ָ���ʶ
	 * @return
	 */
	public void updateInstructionsState(String deviceId, String state,
			String cmdId) {
		ArrayList list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = DbUtil.getConnection();
		// instr(instruction,?)=1��ָ���а���ָ�� �����ʶ�� ���ն�״̬���и���
		String sql = "update t_structions set state=" + state + " where id=";
		String querySql = "select id from t_structions where device_id=? and instr(instruction,?)<>0 and state=1 order by createdate desc";
		Statement stm = null;

		try {

			long id = 0;
		 
			pst = conn.prepareStatement(querySql);
			pst.setString(1, deviceId);
			pst.setString(2, cmdId);

			rs = pst.executeQuery();
			if (rs.next()) {
				id = rs.getLong("id");
				sql += id;
				stm = conn.createStatement();
				stm.executeUpdate(sql);
				conn.commit();
				Log.getInstance().outLog(
						"������" + deviceId + "ָ��" + cmdId + "״̬��");
			}

		} catch (SQLException e) {
			Log.getInstance().outLog("����ָ��״̬ʧ��" + e.getMessage());
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			DbOperation.release(stm, rs, pst, conn);
		}

	}

	// ͼƬ������Ϣ���
	public boolean insertPicInfo(Picture p) {
		boolean flag = false;
		Connection conn = DbOperation.getConnection();
		PreparedStatement pst = null;

		String sql = "insert into t_muti_medium (id,device_id,longitude,lantitude,image,upload_time,img_type) ";
		sql += "values(SEQ_MULTI_MEDIUM.NEXTVAL,?,?,?,?,sysdate,?)";
		InputStream is = p.getImgcontent();
		try {

			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setString(1, p.getDeviceId());

			pst.setFloat(2, p.getX());
			pst.setFloat(3, p.getY());
			pst.setBinaryStream(4, is, is.available());
			// pst.setTimestamp(5, p.getTimeStamp());
			pst.setString(5, p.getType() == null ? "0" : p.getType());
			pst.execute();
			conn.commit();
			conn.setAutoCommit(true);
			flag = true;
		} catch (SQLException ex) {
			flag = false;
			Log.getInstance().errorLog("����ͼƬ��Ϣ�쳣��" + ex.getMessage(), ex);
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			// �ͷ���Դ
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex2) {
				}
			}

			DbOperation.release(null, null, pst, conn);
		}

		return flag;

	}

	public static void main(String[] args) {
		DBServiceImpl dbl = new DBServiceImpl();
		Connection conn = DbUtil.getDirectConnection();
		dbl.insertAlarmNotify(conn, "10658264", "", "13698607474",
				"#TH100,000000,RC,S,10658264,13698607474*");
	}

	/**
	 * @param pb:������Ϣ
	 * @param type:1������
	 *            2���� 3����
	 */
	public boolean saveActiveAlarm(ParseBase pb, String type) {
		// TODO Auto-generated method stub
		String callActiveAlarm = "{call PROC_ACTIVE_ALARM_INFO(?,?,?,?,?,?,?,?,?)}";
		String callSpeedAlarm = "{call PROC_SPEED_ALARM_INFO(?,?,?,?,?,?,?,?,?)}";
		String callAreaAlarm = "{call PROC_AREA_ALARM_INFO(?,?,?,?,?,?,?,?,?,?)}";
		String mx = "";
		String my = "";
		boolean flag = false;

		Connection conn = DbUtil.getConnection();
		CallableStatement cstm = null;

		double[] xx = new double[1];// ԭʼ����
		double[] yy = new double[1];// ԭʼγ��
		xx[0] = Float.parseFloat(pb.getCoordX() == null ? "0" : pb.getCoordX());
		yy[0] = Float.parseFloat(pb.getCoordY() == null ? "0" : pb.getCoordY());
		com.sos.sosgps.api.CoordAPI coordAPI = new CoordAPI();
		com.sos.sosgps.api.DPoint dpoint = new DPoint();
		com.sos.sosgps.api.DPoint[] pts = null;
		try {
			// ����ƫתԭʼ����
			pts = coordAPI.encryptConvert(xx, yy);
			mx = pts[0].getEncryptX();
			my = pts[0].getEncryptY();
			if (type != null && !type.equals("")) {
				if (type.equals("1")) {
					cstm = conn.prepareCall(callSpeedAlarm);
				} else if (type.equals("2")) {
					String longid = Config.getInstance().getString("smslongid")
							.trim();// �����ط�����
					cstm = conn.prepareCall(callAreaAlarm);
					cstm.setString(10, longid);
				} else if (type.equals("3")) {
					cstm = conn.prepareCall(callActiveAlarm);
				}

				cstm.setString(1, pb.getDeviceSN());
				cstm.setFloat(2, Float.parseFloat(pb.getCoordX() == null ? "0"
						: pb.getCoordX()));
				cstm.setFloat(3, Float.parseFloat(pb.getCoordY() == null ? "0"
						: pb.getCoordY()));
				cstm.setString(4, mx);
				cstm.setString(5, my);
				cstm.setFloat(6, Float.parseFloat(pb.getSpeed() == null ? "0"
						: pb.getSpeed()));
				cstm.setFloat(7, Float
						.parseFloat(pb.getDirection() == null ? "0" : pb
								.getDirection()));
				cstm.setFloat(8, Float.parseFloat(pb.getLC() == null ? "0" : pb
						.getLC()));
				cstm.setTimestamp(9, new Timestamp(new Date().getTime()));
				cstm.execute();
				conn.commit();
			}
			flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.getInstance().errorLog("���汨����Ϣ�쳣", e);
			flag = false;
		} finally {

			try {

				if (cstm != null)
					cstm.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbOperation.release(null, null, null, conn);

		}

		return flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.autonavi.directl.dbutil.service.DBService#saveMessage(java.lang.String,
	 *      java.lang.String)
	 */
	public void saveMessage(String deviceid, String cont) {
		// TODO Auto-generated method stub
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = null;
		String sql = "insert into t_message(id,sender,content) values(seq_message.nextval,?,?)";
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, deviceid);
			pst.setString(2, cont);
			pst.execute();
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.getInstance().errorLog("������Ϣ�쳣", e);
		} finally {
			DbOperation.release(null, null, pst, con);
		}

	}

	public synchronized void saveActiveAlarm(ParseBase base) throws Exception {
		String callActiveAlarm = "{call PROC_ACTIVE_ALARM_INFO(?,?,?,?,?,?,?,?,?)}";

		double[] xx = new double[1];// ԭʼ����
		double[] yy = new double[1];// ԭʼγ��
		xx[0] = Float.parseFloat(base.getCoordX());
		yy[0] = Float.parseFloat(base.getCoordY());
		CoordAPI coordAPI = new CoordAPI();
		DPoint dpoint = new DPoint();

		String mx = "";
		String my = "";
		DPoint[] pts = null;
		try {
			// ����ƫתԭʼ����
			pts = coordAPI.encryptConvert(xx, yy);
			mx = pts[0].getEncryptX();
			my = pts[0].getEncryptY();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection conn = DbOperation.getNodeConnection();// .getConnection();
		CallableStatement cstm = null;
		try {
			cstm = conn.prepareCall(callActiveAlarm);
			cstm.setString(1, base.getDeviceSN());
			cstm.setFloat(2, Float.parseFloat(base.getCoordX() == null ? "0"
					: base.getCoordX()));
			cstm.setFloat(3, Float.parseFloat(base.getCoordY() == null ? "0"
					: base.getCoordY()));
			cstm.setString(4, mx);
			cstm.setString(5, my);
			cstm.setFloat(6, Float.parseFloat(base.getSpeed() == null ? "0"
					: base.getSpeed()));
			cstm.setFloat(7, Float.parseFloat(base.getDirection() == null ? "0"
					: base.getDirection()));
			cstm.setFloat(8, Float.parseFloat(base.getLC() == null ? "0" : base
					.getLC()));
			cstm.setTimestamp(9, base.getTimeStamp());
			cstm.execute();
			cstm.close();
			cstm = null;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.getInstance().errorLog("���������쳣", e);
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public synchronized void saveAreaAlarm(ParseBase base) throws Exception {

		String callAreaAlarm = "{call PROC_AREA_ALARM_INFO(?,?,?,?,?,?,?,?,?,?)}";

		double[] xx = new double[1];// ԭʼ����
		double[] yy = new double[1];// ԭʼγ��
		xx[0] = Float.parseFloat(base.getCoordX());
		yy[0] = Float.parseFloat(base.getCoordY());
		CoordAPI coordAPI = new CoordAPI();
		DPoint dpoint = new DPoint();

		String mx = "";
		String my = "";
		DPoint[] pts = null;
		try {
			// ����ƫתԭʼ����
			pts = coordAPI.encryptConvert(xx, yy);
			mx = pts[0].getEncryptX();
			my = pts[0].getEncryptY();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection conn = DbOperation.getNodeConnection();// DbOperation.getConnection();
		CallableStatement cstm = null;
		try {

			String longid = Config.getInstance().getString("smslongid");// �����ط�����
			cstm = conn.prepareCall(callAreaAlarm);
			cstm.setString(1, base.getDeviceSN());
			cstm.setFloat(2, Float.parseFloat(base.getCoordX() == null ? "0"
					: base.getCoordX()));
			cstm.setFloat(3, Float.parseFloat(base.getCoordY() == null ? "0"
					: base.getCoordY()));
			cstm.setString(4, mx);
			cstm.setString(5, my);
			cstm.setFloat(6, Float.parseFloat(base.getSpeed() == null ? "0"
					: base.getSpeed()));
			cstm.setFloat(7, Float.parseFloat(base.getDirection() == null ? "0"
					: base.getDirection()));
			cstm.setFloat(8, Float.parseFloat(base.getLC() == null ? "0" : base
					.getLC()));
			cstm.setTimestamp(9, base.getTimeStamp());
			cstm.setString(10, longid);
			cstm.execute();
			cstm.close();
			cstm = null;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.getInstance().errorLog("���򱨾��쳣", e);
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public synchronized void saveLineAlarm(ParseBase base) throws Exception {
		double[] xx = new double[1];// ԭʼ����
		double[] yy = new double[1];// ԭʼγ��
		xx[0] = Float.parseFloat(base.getCoordX());
		yy[0] = Float.parseFloat(base.getCoordY());
		CoordAPI coordAPI = new CoordAPI();
		DPoint dpoint = new DPoint();

		String mx = "";
		String my = "";
		DPoint[] pts = null;
		try {
			// ����ƫתԭʼ����
			pts = coordAPI.encryptConvert(xx, yy);
			mx = pts[0].getEncryptX();
			my = pts[0].getEncryptY();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String callLineAlarm = "{call PROC_LINE_ALARM_INFO(?,?,?,?,?,?,?,?,?)}";
		Connection conn = DbOperation.getNodeConnection();// DbOperation.getConnection();
		CallableStatement cstm = null;
		try {
			cstm = conn.prepareCall(callLineAlarm);
			cstm.setString(1, base.getDeviceSN());
			cstm.setFloat(2, Float.parseFloat(base.getCoordX() == null ? "0"
					: base.getCoordX()));
			cstm.setFloat(3, Float.parseFloat(base.getCoordY() == null ? "0"
					: base.getCoordY()));
			cstm.setString(4, mx);
			cstm.setString(5, my);
			cstm.setFloat(6, Float.parseFloat(base.getSpeed() == null ? "0"
					: base.getSpeed()));
			cstm.setFloat(7, Float.parseFloat(base.getDirection() == null ? "0"
					: base.getDirection()));
			cstm.setFloat(8, Float.parseFloat(base.getLC() == null ? "0" : base
					.getLC()));

			cstm.setTimestamp(9, base.getTimeStamp());
			cstm.execute();
			conn.commit();
			cstm.close();
			cstm = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.getInstance().errorLog("����ƫ�������쳣", e);
		} finally {
			try {
				if (cstm != null) {
					cstm.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbOperation.release(null, null, null, conn);
		}

	}

	// �������汨����Ϣ
	public synchronized void saveMoreAlarm(ArrayList<ParseBase> baseList) {
		String callAlarm = "{call PROC_ALARM_INFO(?,?,?,?,?,?,?,?,?,?,?)}";
		String smslongcode = null;
		smslongcode = Config.getInstance().getString("smslongid");// �����ط�����
        CoordAPI coordAPI = new CoordAPI();
		Connection conn =  DbOperation.getConnection();// DbOperation.getNodeConnection();
		CallableStatement cstm = null;
		try {
			cstm = conn.prepareCall(callAlarm);
            float x = 0;
            float y = 0;
            String mx = "";
            String my = "";
			for (int j = 0; j < baseList.size(); j++) {
				ParseBase base = baseList.get(j);
	            try {
	                x = Float.parseFloat(base.getCoordX());
	                y = Float.parseFloat(base.getCoordY());
	            } catch (Exception e) {
	                x = 0;
	                y = 0;
	            }
	            if(x == 0 || y == 0){
                    continue;
	            }
		        double[] xx = {x};
		        double[] yy = {y};
		        try {
	                // ����ƫתԭʼ����
                    DPoint[] dps = null;
                    dps = coordAPI.encryptConvert(xx, yy);
                    mx = dps[0].encryptX;
                    my = dps[0].encryptY;
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	                Log.getInstance().errorLog("�������汨��========="+e.getMessage(), e);
	                continue;
	            }

				cstm.setString(1, base.getDeviceSN());
				cstm.setFloat(2, Float
						.parseFloat(base.getCoordX() == null ? "0" : base
								.getCoordX()));
				cstm.setFloat(3, Float
						.parseFloat(base.getCoordY() == null ? "0" : base
								.getCoordY()));
				cstm.setString(4, mx);
				cstm.setString(5, my);
				cstm.setFloat(6, Float.parseFloat(base.getSpeed() == null ? "0"
						: base.getSpeed()));
				cstm.setFloat(7, Float
						.parseFloat(base.getDirection() == null ? "0" : base
								.getDirection()));
				cstm.setFloat(8, Float.parseFloat(base.getLC() == null ? "0"
						: base.getLC()));
				cstm.setTimestamp(9,  new Timestamp(System.currentTimeMillis()));
				cstm.setString(10, base.getAlarmType());
				cstm.setString(11, smslongcode);
				cstm.addBatch();
			}
			cstm.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.getInstance().errorLog("�������汨���쳣", e);
			e.printStackTrace();
		} finally {
			try {
				if (cstm != null)
					cstm.close();
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public synchronized void saveSpeedAlarm(ParseBase base) throws Exception {

		String callSpeedAlarm = "{call PROC_SPEED_ALARM_INFO(?,?,?,?,?,?,?,?,?)}";

		double[] xx = new double[1];// ԭʼ����
		double[] yy = new double[1];// ԭʼγ��
		xx[0] = Float.parseFloat(base.getCoordX());
		yy[0] = Float.parseFloat(base.getCoordY());
		CoordAPI coordAPI = new CoordAPI();
		DPoint dpoint = new DPoint();

		String mx = "";
		String my = "";
		DPoint[] pts = null;
		try {
			// ����ƫתԭʼ����
			pts = coordAPI.encryptConvert(xx, yy);
			mx = pts[0].getEncryptX();
			my = pts[0].getEncryptY();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection conn = DbOperation.getNodeConnection();// DbOperation.getConnection();
		CallableStatement cstm = null;
		try {

			cstm = conn.prepareCall(callSpeedAlarm);
			cstm.setString(1, base.getDeviceSN());
			cstm.setFloat(2, Float.parseFloat(base.getCoordX() == null ? "0"
					: base.getCoordX()));
			cstm.setFloat(3, Float.parseFloat(base.getCoordY() == null ? "0"
					: base.getCoordY()));
			cstm.setString(4, mx);
			cstm.setString(5, my);
			cstm.setFloat(6, Float.parseFloat(base.getSpeed() == null ? "0"
					: base.getSpeed()));
			cstm.setFloat(7, Float.parseFloat(base.getDirection() == null ? "0"
					: base.getDirection()));
			cstm.setFloat(8, Float.parseFloat(base.getLC() == null ? "0" : base
					.getLC()));

			cstm.setTimestamp(9, base.getTimeStamp());
			cstm.execute();
			cstm.close();
			cstm = null;
			conn.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.getInstance().errorLog("���ٱ����쳣", e);
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public synchronized void saveImagePack(Picture p) {

		if (p == null)
			return;

		Connection conn = DbOperation.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;

		String insql = "insert into t_muti_medium (id,device_id,longitude,lantitude,image,upload_time,img_type) ";
		insql += "values(SEQ_MULTI_MEDIUM.NEXTVAL,?,?,?,?,sysdate,?)";

		String usql = "update t_muti_medium set image=? where device_id=?";

		String sql = "select * from t_muti_medium where device_id=?";
		byte[] retB = null;

		try {
 			pst = conn.prepareStatement(sql);
			pst.setString(1, p.getDeviceId());
			rs = pst.executeQuery();
 
			if (rs.next()) {
				
				InputStream readIs  = rs.getBinaryStream("image");
				retB = new byte[readIs.available()];
				readIs.read(retB);
				
				InputStream is = null;
 				if (retB != null && p.getImgBytes() != null && retB.length > 0
						&& p.getImgBytes().length > 0) {

					ByteBuffer buf = ByteBuffer.allocate(retB.length
							+ p.getImgBytes().length);
					buf.put(retB).put(p.getImgBytes());
					is = new ByteArrayInputStream(buf.array());
					pst.close();
					pst = null;
					pst = conn.prepareStatement(usql);
					pst.setBinaryStream(1, is, is.available());
					pst.setString(2, p.getDeviceId());
					pst.executeUpdate();
					conn.commit();
 				}

			} else {
				InputStream is = new ByteArrayInputStream(p.getImgBytes());
				pst = conn.prepareStatement(insql);
				pst.setString(1, p.getDeviceId());
				pst.setFloat(2, p.getX());
				pst.setFloat(3, p.getY());
				pst.setBinaryStream(4, is, is.available());
				pst.setString(5, p.getType() == null ? "0" : p.getType());
				pst.execute();
				conn.commit();
			}

		} catch (SQLException ex) {

			Log.getInstance().errorLog("����ͼƬ���ݰ���" + ex.getMessage(), ex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.getInstance().errorLog("����ͼƬ���ݰ���" + e.getMessage(), e);
		} finally {

			DbOperation.release(null, rs, pst, conn);
		}

	}

	public void updateNoCmdIdInstructionsState(String deviceId, String state,
			String cmdId) {
		ArrayList list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = DbUtil.getConnection();
		// instr(instruction,?)=1��ָ���а���ָ�� �����ʶ�� ���ն�״̬���и���
		String sql = "update t_structions set state='" + state
				+ "' where device_id='" + deviceId + "' and state='1'";
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate(sql);
			conn.commit();
			Log.getInstance().outLog("������" + deviceId + "ָ��" + cmdId + "״̬��");
		} catch (SQLException e) {
			Log.getInstance().outLog("����ָ��״̬ʧ��" + e.getMessage());
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			DbOperation.release(stm, rs, pst, conn);
		}
	}
}
