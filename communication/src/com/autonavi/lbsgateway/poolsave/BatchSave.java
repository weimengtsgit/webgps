package com.autonavi.lbsgateway.poolsave;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;

import cn.net.sosgps.memcache.Memcache;
import cn.net.sosgps.memcache.bean.Ent;
import cn.net.sosgps.memcache.bean.Terminal;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.directl.bean.TEnt;
import com.autonavi.directl.bean.TLocrecord;
import com.autonavi.directl.bean.TTerminal;
import com.autonavi.directl.dbutil.DbOperation;
import com.autonavi.directl.parse.ParseBase;
import com.sjw.util.CharacterUtil;
import com.sjw.util.CheckedResult;
import com.sjw.util.Constants;
import com.sjw.util.HttpResponseEntity;
import com.sjw.util.HttpUtil;
import com.sos.sosgps.api.CoordAPI;
import com.sos.sosgps.api.DPoint;


public class BatchSave {
	private java.util.ArrayList datalist;
    CoordAPI coordApizw = new CoordAPI();

	private java.sql.Connection conn;

	public BatchSave() {
	}

	public synchronized void setDataList(ArrayList li) {
		this.datalist = li;
	}
	
	public ArrayList<ParseBase> sortList(ArrayList<ParseBase> al){
		ArrayList<ParseBase> resList = null;
		if (al != null && al.size() >0){
			Collections.sort(al, new Comparator(){

				public int compare(Object o1, Object o2) {
					// TODO Auto-generated method stub
					ParseBase parse1 = (ParseBase) o1;
					ParseBase parse2 = (ParseBase) o2;
					
					return 0;
				}});
		}
		return resList;
	}

	public synchronized void batchSave() {
		long t1 = System.currentTimeMillis();
		if (this.datalist.size() <= 0) {
			return;
		}
		ArrayList gpsdatalist = new ArrayList();
		// if (datalist.size() > 0) {
		// // ת����JMS
		// GPSForwardToJMS toJms = new GPSForwardToJMS();
		// toJms.sendMessage("abcd", datalist);
		// }
		for (int i = 0; i < this.datalist.size(); i++) {
			ParseBase tmppb = (ParseBase) this.datalist.get(i);
			// Log.getInstance().outLog("��¼ʱ�䣺"+tmppb.getTime());
			GpsData tmpgpsdata = this.getGpsDataFromParseBase(tmppb);
			if (tmpgpsdata != null) {
				gpsdatalist.add(tmpgpsdata);
			}
		}
		long t2 = System.currentTimeMillis();
		if (gpsdatalist.size() <= 0) {
			// DbOperation.release(null, null, null, conn);
			// com.mapabc.db.DBConnectionManager.close(conn, null, null);
			return;
		}

		conn = DbOperation.getConnection();// com.mapabc.db.DBConnectionManager.getInstance().getConnection();

		String insertPro = "{call PROC_ADD_LOCRECORD_LOCDESC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
//		String callProc = "{call PROC_ADD_Last_LOC(?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement pstmt = null;
		CallableStatement cstmt = null;

		try {
		    //conn.setAutoCommit(false);
			/**
			cstmt = conn.prepareCall(callProc);

			try {
				String count = null;
				int icount = 200;
				try {
					count = Config.getInstance().getString("lastSaveCount").trim();
					icount = Integer.parseInt(count == null ? "200" : count);
				} catch (Exception e) {
					icount = 200;
				}
				int remain = 0;
				for (int i = 0; i < gpsdatalist.size(); i++) {
					GpsData tmp = (GpsData) gpsdatalist.get(i);
					// Log.getInstance().outLog("���λ�ã�"+tmp.toString());
					// �������λ�ñ�
					cstmt.setFloat(1, tmp.X);
					cstmt.setFloat(2, tmp.Y);
					cstmt.setFloat(3, tmp.S);
					cstmt.setFloat(4, tmp.H);
					cstmt.setFloat(5, tmp.V);
					cstmt.setFloat(6, tmp.LC);
					cstmt.setTimestamp(7, tmp.gpsTime);// ����ʱ�������
					cstmt.setString(8, tmp.DEVICE_ID);
					cstmt.setString(9, tmp.locateType);// ���ͣ�GPS
					cstmt.setFloat(10, tmp.temperature);
					cstmt.setString(11, tmp.accStatus);
					cstmt.addBatch();
 					
					if ((i+1) % icount == 0) {
						cstmt.executeBatch();
						conn.commit();
						cstmt.clearBatch();
 					}else if (i == gpsdatalist.size()-1){
 						cstmt.executeBatch();
 						conn.commit();
						cstmt.clearBatch();
 					}
				}
				
			} catch (Exception e) {
				Log.getInstance().errorLog("�������λ�ñ��쳣", e);
			}
			*/
			pstmt = conn.prepareCall(insertPro);// conn.prepareStatement(insertSql);
			// ���͸����򱨾�����
			String requestEntity = "";
			for (int j = 0; j < gpsdatalist.size(); j++) {
				GpsData tmp = (GpsData) gpsdatalist.get(j);
				// Log.getInstance().outLog("�켣��"+tmp.toString());
                String deviceId = tmp.DEVICE_ID;
				/*Terminal termCached = Constants.TERMINAL_CACHE.get(tmp.DEVICE_ID);
                CheckedResult<Terminal> result = isTermminalUseful(termCached, tmp.DEVICE_ID);
                termCached = result.getObj();
                if (!result.isResult()) {
                    //terminal not exist or expired
                    continue;
                }*/
                double lng = tmp.X;
                double lat = tmp.Y;
                //get jmx jmy desc
                TLocrecord locrecord = new TLocrecord();
                getJmxyDesc(lng, lat, locrecord, lng + "", lat + "");
                Log.getInstance().outLog("deviceId = "+deviceId+"; lng = "+lng+"; lat = "+lat+"; desc = " + locrecord.getLocDesc());
                
				pstmt.setString(1, tmp.DEVICE_ID);
				pstmt.setFloat(2, tmp.X);
				pstmt.setFloat(3, tmp.Y);
				pstmt.setFloat(4, tmp.S);
				pstmt.setFloat(5, tmp.H);
				pstmt.setFloat(6, tmp.V);
				pstmt.setFloat(7, tmp.LC);
				pstmt.setTimestamp(8, tmp.gpsTime);// ����ʱ�������
				pstmt.setString(9, tmp.locateType);// ���ͣ�GPS
				if (tmp.temperature != null) {
					pstmt.setFloat(10, tmp.temperature.floatValue());
				}else {
					pstmt.setNull(10, Types.FLOAT);
				}
				pstmt.setString(11, tmp.accStatus);
				pstmt.setString(12, tmp.Extend1);
                pstmt.setString(13, locrecord.getJmx());
                pstmt.setString(14, locrecord.getJmy());
                pstmt.setString(15, locrecord.getLocDesc());
                if (tmp.humidity != null) {
                  pstmt.setFloat(16, tmp.humidity.floatValue());
                }else {
                  pstmt.setNull(16, Types.FLOAT);
                }
				pstmt.addBatch();

				if (tmp.X > 0) {
					// ������";"����,��ʽdeviceId,x,y,speed,height,direction,statlliteNum,alarmTime
					requestEntity += tmp.DEVICE_ID + "," + tmp.X + ","
					+ tmp.Y + ",0,0,0,0," + tmp.gpsTime + ";";
				}
			}
			pstmt.executeBatch();
			conn.commit();
			
			if (requestEntity.equals("")) {
				return;
			}
			if (requestEntity.length() > 0) {
				requestEntity = requestEntity.substring(0,
						requestEntity.length() - 1);
			}
			Log.getInstance().outLog("send to areaalarm server:datas=" + requestEntity);
			String timeoutStr = Config.getInstance().getString("timeoutStr");
			String areaAlarmServerUrl = Config.getInstance().getString("areaAlarmServerUrl");
			int timeout = CharacterUtil.str2Int(timeoutStr, 30) * 1000;// Ĭ��30��
			HttpResponseEntity res = HttpUtil.sendPost(areaAlarmServerUrl, timeout,
					new String[] { "datas", requestEntity });
		} catch (SQLException ex) {
			ex.printStackTrace();
			Log.getInstance().outLog(
					" batch save:" + gpsdatalist.size() + " fail!"
							+ ex.getMessage());
			Log.getInstance().errorLog(
					" batch save" + gpsdatalist.size() + " fail!", ex);
		} finally {

			try {

				if (cstmt != null) {
					cstmt.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				Log.getInstance().errorLog(e.getMessage(), e);
			}

			// DbOperation.release(stmt, rs, pstmt, conn);

			this.datalist = null;
		}
	}

	public static String getEncrptCoord(double n) {
		try {
			// return com.mapabc.encrptor.EncrptorFactory.getInstance().
			// getStrFormCoordinate(n, 0);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}

	}

	public synchronized GpsData getGpsDataFromParseBase(ParseBase pb) {
		GpsData gpsdata = null;
		try {
			gpsdata = new GpsData();
			long t1 = System.currentTimeMillis();
			float tmpx = 0;
			float tmpy = 0;
			try {
				tmpx = Float.parseFloat(pb.getCoordX() == null ? "0" : pb
						.getCoordX());
				tmpy = Float.parseFloat(pb.getCoordY() == null ? "0" : pb
						.getCoordY());
			} catch (Exception ex) {
				tmpx = 0;
				tmpy = 0;
			}

			if (tmpx > 0 && tmpy > 0) {

				gpsdata.X = tmpx;
				gpsdata.Y = tmpy;

			} else {
				gpsdata.X = 0; // ��������ƫת�ľ���
				gpsdata.Y = 0; // ��������ƫת��γ��
				return null;// 0���겻���
			}

			long t2 = System.currentTimeMillis();
			// Log.getInstance().outLog("coordConversion cost:" + (t2 - t1) +
			// "miSec");
			gpsdata.SIMCARD = pb.getPhnum();
			gpsdata.DEVICE_ID = pb.getDeviceSN();
			// gpsdata.TIME = pb.getTime();

			SimpleDateFormat simpleDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String strDate = simpleDate.format(new Date());
			Calendar curCal = Calendar.getInstance();

			if (pb.getTime() == null || pb.getTime().trim().length() == 0) {
				// ϵͳʱ��
				gpsdata.TIME = strDate;
				pb.setTime(strDate);
			} else {
				Pattern pattern1 = Pattern
						.compile("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01]) (0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})$"); // ƥ��'YYYY-MM-DD
				// HH24:MI:SS'��ʽ
				Matcher matcher1 = pattern1.matcher(pb.getTime());
				boolean flag1 = matcher1.matches();
				if (flag1) {

					Date gpstime = Tools.formatStrToDate(pb.getTime(),
							"yyyy-MM-dd HH:mm:ss");
					Calendar gpsCal = Calendar.getInstance();
					gpsCal.setTime(gpstime);
					gpsCal.add(Calendar.MINUTE, 20);

					int com1 = curCal.compareTo(gpsCal); // GPSʱ��ȵ�ǰʱ��С��30����

					gpsCal.add(Calendar.MINUTE, -40); // GPSʱ��ȵ�ǰʱ�����30����
					int com2 = curCal.compareTo(gpsCal);

					 if (com1 > 0 || com2 < 0) {//���GPSʱ���뵱ǰʱ��ǰ������5���ӣ������ϵͳʱ��
						 // GPS����ʱ�����Ϊ���������ڣ���2090��2080��2001�Ⱥܴ���С������
						 gpsdata.TIME = strDate;
					 	 pb.setTime(strDate);
					 	 Log.getInstance().outLog("�ź������ݣ�"+gpsdata.toString());
					 	 //return null;//���ؿգ��������ڲ��ܴ�����ݵ������ź�������
					 } else {
						 gpsdata.TIME = pb.getTime();
					 }
					//gpsdata.TIME = pb.getTime();
				} else {// ���GPSʱ���뵱ǰʱ��ǰ������5���ӣ������ϵͳʱ��
					gpsdata.TIME = strDate;
				}
			}

			Date date = simpleDate.parse(gpsdata.TIME);
			gpsdata.gpsTime = new java.sql.Timestamp(date.getTime());

			gpsdata.TX = tmpx; // δ��������ƫת�ľ���
			gpsdata.TY = tmpy; // δ��������ƫת��γ��
			gpsdata.S = Float.parseFloat(pb.getSpeed() == null ? "0" : pb
					.getSpeed());
			if (null == pb.getDirection()) {
				pb.setDirection("0");
			}
			gpsdata.V = Float.parseFloat(pb.getDirection());
			if (pb.getLC() != null && pb.getLC() != "")
				gpsdata.LC = Float.parseFloat(pb.getLC());
			gpsdata.PICURL = pb.getPicURL();
			gpsdata.ALARMDESC = pb.getAlarmDesc();
			if (pb.getAltitude() != null)
				gpsdata.H = Float.parseFloat(pb.getAltitude());
			if (pb.getSatellites() != null && pb.getSatellites() != "")
				gpsdata.C = Integer.parseInt(pb.getSatellites());
			gpsdata.F = pb.getStatus();
			//gpsdata.temperature = pb.getTemperature()==null?0f:Float.parseFloat(pb.getTemperature());
			gpsdata.temperature = pb.getTemperature()==null?null:Float.parseFloat(pb.getTemperature());
			gpsdata.humidity = pb.getHumidity()==null?null:Float.parseFloat(pb.getHumidity());
			gpsdata.accStatus = pb.getAccStatus();
			
			// gpsdata.targetID = this.getTargetObjectIdBySim(pb.getPhnum());
			gpsdata.locateType = "1";
			//������
			if (pb.getExtend1() != null && pb.getExtend1() != "")
				gpsdata.Extend1 = pb.getExtend1();
			
		} catch (Exception ex2) {
			gpsdata = null;
			ex2.printStackTrace();
			Log.getInstance().errorLog("GpsDataSave.class ���ִ���.", ex2);
		}
		// GBLTerminalList.getInstance().updateLocInfo(pb.getPhnum(), pb);
		// this.updateOrSaveLastLoc(pb);
		return gpsdata;
	}


    public CheckedResult<Terminal> isTermminalUseful(Terminal termCached, String deviceId){
        CheckedResult<Terminal> termResult = new CheckedResult<Terminal>();
        try {
            if (termCached == null) {
                TTerminal terminal = DbOperation.findTerminalByDeviceId(deviceId);
                if (terminal != null) {
                    termCached = new Terminal();
                    BeanUtils.copyProperties(terminal, termCached);
                    Constants.TERMINAL_CACHE.set(deviceId, termCached,
                            1 * Memcache.EXPIRE_DAY);// �ն�ʱ�䵽��,oracle_job���޸��ն˱�ʶΪ�����ڡ�,��˻���10��
                } else {
//                    logger.info("�ն�:" + deviceId + "ȡDB���ݿ�,˵���Ѿ���ɾ��,����������!");
                    termResult.setResult(false);
                    return termResult;
                }
            }
            // �ն˲����ڻ��ն˵���
            if (termCached == null || termCached.getDeviceId() == null || termCached.getExpirationFlag() != 0) {
//                logger.info("�ն�:" + deviceId + " �����ڻ��ն˵���");
                termResult.setResult(false);
                return termResult;
            }
            /** �ж���ҵ* */
            String entCode = termCached.getEntCode();
            Ent entCached = Constants.ENT_CACHE.get(entCode);
            CheckedResult<Ent> result = isEntUseful(entCached, entCode);
            if (!result.isResult()) {
                //ent not exist or expired
                termResult.setResult(false);
                return termResult;
            }
        } catch (Exception e) {
            Log.getInstance().errorLog("isTermminalUseful : "+e.getMessage(), e);
            termResult.setResult(false);
            return termResult;
        }
        termResult.setObj(termCached);
        termResult.setResult(true);
        return termResult;
    }

    public CheckedResult<Ent> isEntUseful(Ent entCached, String entCode) {
        CheckedResult<Ent> result = new CheckedResult<Ent>();
        try {
            if (entCached == null) {
                TEnt ent = DbOperation.findEntByentCode(entCode);
                if (ent != null) {
                    Ent aEnt = new Ent();
                    BeanUtils.copyProperties(ent, aEnt);
                    Constants.ENT_CACHE.set(entCode, aEnt,
                            1 * Memcache.EXPIRE_DAY);// ��ҵʱ�䵽��,oracle_job���޸���ҵ��ʶΪ�����ڡ�,��˻���10��
                }else {
//                    logger.info("��ҵ:" + entCode + "ȡDB���ݿ�,˵���Ѿ���ɾ��,����������!");
                    result.setResult(false);
                    return result;
                }
            }
            // ��ҵ�����ڻ���ҵ����
            if (entCached == null || entCached.getEntCode() == null || !entCached.getEntStatus().equals("1")) {
//                logger.info("��ҵ:" + entCode + " �����ڻ���ҵ����");
                result.setResult(false);
                return result;
            }
        } catch (Exception e) {
            Log.getInstance().errorLog("isEntUseful : "+e.getMessage(), e);
            result.setResult(false);
            return result;
        }
        result.setResult(true);
        return result;
    }
    
    @SuppressWarnings("static-access")
    public void getJmxyDesc(double lng, double lat, TLocrecord locrecord, String slng, String slat){
        String jmx = null, jmy = null, desc = null;
        try {
            /** ʵʱ�ӱ���λ�÷���ȡ��γ�ȵ�ƫת���ܺ�λ������,����retry���λ���* */
            if (lng != 0d && lat != 0d) {
                String pKey = slng + "_" + slat;
                cn.net.sosgps.memcache.bean.Position pCached = null;//Constants.POSITION_CACHE.get(pKey);
                if (pCached == null) {
                    double xs[] = { lng };
                    double ys[] = { lat };
                    //for (int i = 0; i < RETRY; i++) {
                        try {
                            DPoint[] dps = null;
                            dps = coordApizw.encryptConvert(xs, ys);
                            jmx = dps[0].encryptX;
                            jmy = dps[0].encryptY;
                            desc = coordApizw.getAddress(jmx, jmy);
                            //desc = coordApizw.getAddress(coordApizw.encrypt(dps[0].x),coordApizw.encrypt(dps[0].y));
                        } catch (Exception e) {
                            Log.getInstance().errorLog("get desc err: "+e.getMessage(), e);
                        }
                    //}
                    // ����memcache����
                    if (desc != null && desc.length() > 0) {
                        //logger.info("set lng_lat and desc to memcache. lng = "+lng+"; lat = "+lat);
                        pCached = new cn.net.sosgps.memcache.bean.Position();
                        pCached.setLongitude(lng);
                        pCached.setLatitude(lat);
                        pCached.setJmx(jmx);
                        pCached.setJmy(jmy);
                        pCached.setLocDesc(desc);
                        //Constants.POSITION_CACHE.set(pKey, pCached, 2 * Memcache.EXPIRE_HOUR);// ��Ŀǰ30000�ն˵�������,����4��Сʱ
                    }
                    //logger.info("lng_lat not in memcache, get desc from locationservice. lng = "+lng+"; lat = "+lat +"; jmx = "+jmx+"; jmy = "+jmy+"; desc = " + desc);
                }else {
                    desc = pCached.getLocDesc();
                    jmx = pCached.getJmx();
                    jmy = pCached.getJmy();
                    //logger.info("lng_lat and desc in memcache, get desc from memcache. lng = "+lng+"; lat = "+lat+"; " + desc);
                }
                locrecord.setJmx(jmx);
                locrecord.setJmy(jmy);
                locrecord.setLocDesc(desc);
            }
        } catch (Exception e) {
            Log.getInstance().errorLog(e.getMessage(), e);
        }
    }
    
}
