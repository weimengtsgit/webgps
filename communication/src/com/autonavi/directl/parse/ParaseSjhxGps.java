package com.autonavi.directl.parse;

import com.autonavi.directl.Log;
import com.autonavi.directl.Tools;
import com.autonavi.lbsgateway.GBLTerminalList;
import com.mapabc.geom.CoordCvtAPI;
import com.mapabc.geom.DPoint;

public class ParaseSjhxGps extends ParseBase
{
  String deviceid = null;

  public void parseGPRS(String hexString)
  {
    Log.getInstance().outLog("接收到TestGps原始数据：" + hexString);
    try {
      byte[] cont = Tools.fromHexString(hexString);
      String scont = new String(cont);
      Log.getInstance().outLog("TestGps解析后数据：" + scont);
      if (scont.equalsIgnoreCase("$SJHXTEST,")) {
        byte[] repb = "$SJHXTEST,\r\n".getBytes();
        setReplyByte(repb);
        Log.getInstance().outLog(getDeviceSN() + " 车机和中心平台的联通测试回复指令：" + new String(repb));
      } else if (scont.indexOf("$SJHXL") != -1) {
        String devicd_id = scont.substring(6);
        devicd_id = parseDeviceId(devicd_id);
        setDeviceSN(devicd_id);
        Log.getInstance().outLog("车机GPRS链接维持报文.devicd_id = " + devicd_id);
      }
      else
      {
        String head = scont.substring(0, 5);
        scont = scont.substring(5, scont.length());
        String mark = scont.substring(0, 1);
        scont = scont.substring(1, scont.length());
        String[] sconts = scont.split("7E7E");
        String cmd = "";
        if (sconts.length >= 3)
        {
          String devicd_id = sconts[0];
          String state = sconts[1];

          String engineState = state.substring(0, 1);
          setAccStatus(engineState);
          devicd_id = parseDeviceId(devicd_id);
          setDeviceSN(devicd_id);
          String data = sconts[2];
          cmd = data.substring(0, 6);
          data = data.substring(6, data.length());
          String phnum = null;
          phnum = GBLTerminalList.getInstance().getSimcardNum(getDeviceSN());
          if ((phnum == null) || (hexString == null) || (phnum.trim().length() == 0) || (hexString.trim().length() == 0)) {
            Log.getInstance().outLog("系统中没有适配到指定的终端：device_id=" + getDeviceSN());
            return;
          }
          setPhnum(phnum);

          if (cmd.equalsIgnoreCase("020301")) {
            parsePosition(data);
          }
          else if ((!cmd.equalsIgnoreCase("020307")) && 
            (!cmd.equalsIgnoreCase("020308")) && 
            (!cmd.equalsIgnoreCase("020304")))
          {
            cmd.equalsIgnoreCase("02030B");
          }
        }

        Log.getInstance().outLog(getDeviceSN() + ",数据区长度：" + (head.length() + mark.length() + scont.length()) + 
          ",数据内容：" + head + mark + scont + 
          ",命令:" + cmd);
      }

    }
    catch (Exception e)
    {
      Log.getInstance().errorLog(getDeviceSN() + ",解析数据报错", e);
    }
  }

  private String locateDesc() throws Exception
  {
    CoordCvtAPI api = new CoordCvtAPI();

    String addr = null;
    DPoint[] point = null;
    try {
      point = api.encryptConvert(new double[] { Double.parseDouble(
        getCoordX()) }, new double[] { 
        Double.parseDouble(getCoordY()) });
      addr = CoordCvtAPI.getAddress(point[0].encryptX, point[0].encryptY);
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return addr;
  }

  private void parsePosition(String gpsdata)
  {
    try {
      String gpsStat = gpsdata.substring(0, 2);
      if (gpsStat.equals("00"))
        setSatellites("0");
      else {
        setSatellites("5");
      }
      String lat = gpsdata.substring(2, 10);
      String lng = gpsdata.substring(10, 20);
      String latdu = lat.substring(0, 2);
      String latfen = lat.substring(2, lat.length());
      String lngdu = lng.substring(0, 4);
      String lngfen = lng.substring(4, lng.length());
      String x = Integer.parseInt(lngdu) + Double.parseDouble(lngfen) / 10000.0D / 60.0D + "";
      String y = Integer.parseInt(latdu) + Double.parseDouble(latfen) / 10000.0D / 60.0D + "";
      if (gpsdata.length() >= 24)
      {
        String speed = gpsdata.substring(20, 24);
        setSpeed(speed);
      }
      if (gpsdata.length() >= 28)
      {
        String direction = gpsdata.substring(24, 28);
        setDirection(direction);
      }
      String dateStr = "";
      if (gpsdata.length() >= 40)
      {
        String gpstime = gpsdata.substring(28, 40);
        String year = gpstime.substring(0, 2);
        String month = gpstime.substring(2, 4);
        String day = gpstime.substring(4, 6);
        String hour = gpstime.substring(6, 8);
        String minute = gpstime.substring(8, 10);
        String second = gpstime.substring(10, 12);
        dateStr = "20" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
      }

      if (gpsdata.length() >= 42)
      {
        String signalIntensity = gpsdata.substring(40, 42);
        setSignalFlag(signalIntensity);
      }
      if (gpsdata.length() >= 45)
      {
        String voltage = gpsdata.substring(42, 45);
        setProgramVersion(voltage);
      }

      if (gpsdata.length() >= 48)
      {
        String bat = gpsdata.substring(45, 48);
        setBat(bat);
      }

      if (gpsdata.length() >= 49)
      {
        String sdq2scstate = gpsdata.substring(48, 49);
        setVarExt1(sdq2scstate);
      }

      if (gpsdata.length() >= 50)
      {
        String sdq2scres = gpsdata.substring(49, 50);
        setVarExt2(sdq2scres);
      }

      if (gpsdata.length() >= 51)
      {
        String sdq1scstate = gpsdata.substring(50, 51);
        setVarExt3(sdq1scstate);
      }

      if (gpsdata.length() >= 52)
      {
        String sdq1scres = gpsdata.substring(51, 52);
        setVarExt4(sdq1scres);
      }
      if (gpsdata.length() >= 53)
      {
        String yssc = gpsdata.substring(52, 53);
        setVarExt5(yssc);
      }
      if (gpsdata.length() >= 54)
      {
        String wkstate = gpsdata.substring(53, 54);
        setVarExt6(wkstate);
      }
      if (gpsdata.length() >= 55)
      {
        String tsstate = gpsdata.substring(54, 55);
        setVarExt7(tsstate);
      }
      if (gpsdata.length() >= 56)
      {
        String ljstate = gpsdata.substring(55, 56);
        setVarExt8(ljstate);
      }
      if (gpsdata.length() >= 57)
      {
        String cystate = gpsdata.substring(56, 57);
        setVarExt9(cystate);
      }
      if (gpsdata.length() >= 59)
      {
        String len = gpsdata.substring(57, 59);
        if (!len.equals("0"))
        {
          try
          {
            String datavalid = gpsdata.substring(59, 60);
            setVarExt10(datavalid);

            if (gpsdata.length() >= 62)
            {
              String handdata = gpsdata.substring(60, 62);
              setVarExt11(handdata);
            }
            if (gpsdata.length() >= 64)
            {
              String startdata = gpsdata.substring(62, 64);
              setVarExt12(startdata);
            }
            if (gpsdata.length() >= 68)
            {
              String dianyadata = gpsdata.substring(64, 68);
              setVarExt13(dianyadata);
            }
            if (gpsdata.length() >= 70)
            {
              String shuiwendata = gpsdata.substring(68, 70);
              setVarExt14(shuiwendata);
            }
            if (gpsdata.length() >= 72)
            {
              String youweidata = gpsdata.substring(70, 72);
              setVarExt15(youweidata);
            }
            if (gpsdata.length() >= 74)
            {
              String youwendata = gpsdata.substring(72, 74);
              setVarExt16(youwendata);
            }
            if (gpsdata.length() >= 76)
            {
              String youyadata = gpsdata.substring(74, 76);
              setVarExt17(youyadata);
            }
            if (gpsdata.length() >= 84)
            {
              String worktimedata = gpsdata.substring(76, 84);
              try {
                worktimedata = Integer.parseInt(worktimedata) / 10 +"";
              } catch (Exception e) {
                Log.getInstance().outLog("转换工作小时报错: " + e.getStackTrace());
              }
              setVarExt18(worktimedata);
            }
            if (gpsdata.length() >= 88)
            {
              String zhuansudata = gpsdata.substring(84, 88);
              setVarExt19(zhuansudata);
            }
            if (gpsdata.length() >= 90)
            {
              String baojingdata = gpsdata.substring(88, 90);
              setVarExt20(baojingdata);
            }
            if (gpsdata.length() >= 92)
            {
              String jiaoyandata = gpsdata.substring(90, 92);
              setVarExt21(jiaoyandata);
            }
            if (gpsdata.length() >= 94)
            {
              String enddata = gpsdata.substring(92, 94);
              setVarExt22(enddata);
            }
          } catch (Exception e) {
            Log.getInstance().outLog("解析采集数据长度出错: " + e.getStackTrace());
          }
        }
      }
      setCoordX(x);
      setCoordY(y);

      sentPost(true);
      Log.getInstance().outLog(
        getDeviceSN() + " 已定位GPS数据：x=" + getCoordX() + 
        ",y=" + getCoordY() + ",speed=" + 
        getSpeed() + ",dirction=" + 
        getDirection() + ",lc=" + getLC() + 
        ",temperature=" + getTemperature() + 
        ",gpstime=" + dateStr + 
        ",signalFlag=" + getSignalFlag() + 
        ",programVersion=" + getProgramVersion() + 
        ",lastDistance=" + getLastDistance() + 
        ",bat=" + getBat() + 
        ",state=" + getState() + 
        ",stateShell=" + getStateShell() + 
        ",stateAnt=" + getStateAnt() + 
        ",stateWire=" + getStateWire() + 
        ",relay1=" + getRelay1() + 
        ",reasonRelay1 = " + getReasonRelay1() + 
        ",sdq2scstate = " + getVarExt1() + 
        ",sdq2scres = " + getVarExt2() + 
        ",sdq1scstate = " + getVarExt3() + 
        ",sdq1scres = " + getVarExt4() + 
        ",yssc = " + getVarExt5() + 
        ",wkstate = " + getVarExt6() + 
        ",tsstate = " + getVarExt7() + 
        ",ljstate = " + getVarExt8() + 
        ",cystate = " + getVarExt9() + 
        ",datavalid = " + getVarExt10() + 
        ",handdata = " + getVarExt11() + 
        ",startdata = " + getVarExt12() + 
        ",dianyadata = " + getVarExt13() + 
        ",shuiwendata = " + getVarExt14() + 
        ",youweidata = " + getVarExt15() + 
        ",youwendata = " + getVarExt16() + 
        ",youyadata = " + getVarExt17() + 
        ",worktimedata = " + getVarExt18() + 
        ",zhuansudata = " + getVarExt19() + 
        ",baojingdata = " + getVarExt20() + 
        ",jiaoyandata = " + getVarExt21() + 
        ",enddata = " + getVarExt22());
    }
    catch (Exception e)
    {
      Log.getInstance().errorLog(getDeviceSN() + ",解析位置数据报错", e);
    }
  }

  public void parseSMS(String phnum, String content)
  {
  }

  public String parseDeviceId(String deviceId)
  {
    int count = deviceId.length() / 2;
    StringBuffer sb = new StringBuffer();
    for (int i = count; i > 0; --i) {
      int j = i * 2;
      String a1 = deviceId.substring(j - 2, j);
      sb.append(a1);
    }
    return sb.toString();
  }

  public static void main(String[] args)
  {
    String hex = "40534a48582c323731333131433237453745313032303745374530323033303138303339353132363437303131363235343538383030303430303030313231323139313633303239333131323333393330303030313030303031373030303030303030303030303030303030303030303030303030303030303030303030";
    ParaseSjhxGps sh = new ParaseSjhxGps();
    sh.parseGPRS(hex);
  }
}