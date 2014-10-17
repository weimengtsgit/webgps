<%@ page language="java"pageEncoding="GBK"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.sosgps.wzt.util.*"%>
<% 
	String entCode = CookieHelper.getValue(request,"login_entCode");
	if(entCode == null) entCode = "";
	String userName = CookieHelper.getValue(request,"login_userName");
	if(userName == null) userName = "";
	request.setAttribute("entCode",entCode);
	request.setAttribute("userName",userName);
	String path = request.getContextPath();
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- saved from url=(0028)http://map.sogou.com/sosgps/ -->
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD><TITLE>GPS定位服务-手机定位（搜狐-图搜天下）</TITLE>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<META content=GPS定位,位置轨迹监控,手机定位,车辆定位,移动实时定位,地图定位轨迹 name=keywords>
<META content=搜狐图搜天下定位平台，提供手机、车辆的实时定位服务，由电子地图完美呈现，协助企业精细化管理销售、客服等外勤业务人员。 name=description>
<link rel="stylesheet" type="text/css" href="indexcss.css" />
<META content="MSHTML 6.00.6000.16981" name=GENERATOR>
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>
<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>
<script>
function englishEdition(){
	window.location.href = 'index.jsp';
}
</script>
</HEAD>

<BODY>
<form name="LoginForm" method="post" action="">
<TABLE cellSpacing=0 cellPadding=0 width=1024 align=center border=0>
  <TBODY>
  <TR>
    <TD style="HEIGHT: 102px" width=1024><IMG  src="images/index/LogIn_Titel.jpg" useMap=#Map2  border=0></TD>
  </TR>
  <TR>
    <TD style="BACKGROUND-IMAGE: url(images/index/LogIn_Center1.jpg); POSITION: relative; HEIGHT: 416px" width=1024>
	<table width="100%" height="416" border="0">
      <tr>
        <td width="2%" rowspan="11">&nbsp;</td>
        <td height="16">&nbsp;</td>
        <td width="25%" rowspan="11">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="23">&nbsp;</td>
        <td class="input4"><strong>Enterprise Code：</strong></td>
        <td><input type="text" value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" class="input1" id="empCode"/></td>
      </tr>
      <tr>
        <td height="23"><SPAN  class=bt_2>PositionServices  MapSearchWorld</SPAN></td>
        <td class="input4"><strong id="s_userName" style="display: ">Username：</strong></td>
        <td><input type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="input1"  id="userAccount"/></td>
      </tr>
      <tr>
        <td height="23"><SPAN class=bt_3>Map Search world position services platform V2.0</SPAN></td>
        <td class="input4"><strong name="s_password" id="s_password" style="display:">Password：</strong></td>
        <td><input type="password" name="password"  class="input1" id="password"/></td>
      </tr>
      <tr>
        <td width="45%" rowspan="3">
		 <SPAN style="FONT-SIZE: 12px; WIDTH: 340px; COLOR: #555346; TEXT-INDENT: 2em; LINE-HEIGHT: 20px; FONT-FAMILY: '宋体'">
		 &nbsp;&nbsp; Sohu.com Inc. and jointly launch the world map search, the platform provides users with real-
          time location information of mobile terminals,track inquiry services to help companies develop management,
          lower management costs, improve management efficiency.		 </SPAN>		</td>
        <td height="23" class="input4"><strong name="s_validatecode" id="s_validatecode" style="display:">Security Code：</strong></td>
        <td><input type="text" name="validateCode"  class="input3" id="validateCode"/><img src="<%=path%>/system/validateCode.do" width="60" height="20" align="middle" id="validateImg" onClick="refreshImg()"></td>
      </tr>
      <tr>
        <td width="12%" height="21" align="center"><IMG style="CURSOR: pointer" onclick=submitForm() height=19  src="images/index/login_button.gif" width=55></td>
        <td width="16%" ><a href="#" onclick=englishEdition()>中文(简体)</a></td>
      </tr>
      <tr>
        <td colspan="2" class="input4"><font color="#FF0000">${error}</font></td>
        </tr>
      <tr>
        <td height="40">&nbsp;</td>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td height="29"><SPAN style="FONT-SIZE: 12px; COLOR: #555346"><IMG height=14 src="images/index/icon-dw.gif" width=14> Contract Customers</SPAN></td>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td height="18"><DIV id=demo style="OVERFLOW: hidden; WIDTH: 310px; HEIGHT: 50px">
      <TABLE cellPadding=0 align=left border=0 cellspace="0">
        <TBODY>
        <TR>
          <TD id=demo1 vAlign=top>
            <TABLE cellSpacing=0 cellPadding=5 border=0>
              <TBODY>
              <TR>
                <TD><a href="http://www.bfxg-fas.com"><IMG class=img height=45  src="images/index/gps_scroll_1.gif"  width=100></a></TD>
                <TD><a href="http://www.1573gj.com"><IMG class=img height=45  src="images/index/gps_scroll_2.gif"  width=100></a></TD>
                <TD><a href="http://www.bjlacc.com"><IMG class=img height=45  src="images/index/gps_scroll_3.gif"   width=100></a></TD>
                <TD><a href="http://www.bau.com.cn/"><IMG class=img height=45  src="images/index/gps_scroll_4.gif"   width=100></a></TD>
                <TD><a href="http://www.sinopharmholding.com"><IMG class=img height=45  src="images/index/gps_scroll_5.gif"   width=100></a></TD>
                <TD><a href="http://www.jond.cn"><IMG class=img height=45   src="images/index/gps_scroll_6.gif"   width=100></a></TD>
                <TD><a href="http://www.huarunjie.com"><IMG class=img height=45   src="images/index/gps_scroll_7.gif"    width=100></a></TD>
                <TD><a href="http://www.jgyln.com"><IMG class=img height=45  src="images/index/gps_scroll_8.gif"  width=100></a></TD>
                <TD><a href="http://www.bjlsjt.com"><IMG class=img height=45   src="images/index/gps_scroll_9.gif"  width=100></a></TD>
                <TD><a href="http://www.dihon.com"><IMG class=img height=45  src="images/index/gps_scroll_10.gif" width=100></a></TD>
                <TD><a href="http://www.lzlj.com.cn"><IMG class=img height=45   src="images/index/gps_scroll_11.gif"  width=100></a></TD>
                <TD><a href="http://www.mengniu.com.cn"><IMG class=img height=45 src="images/index/gps_scroll_12.gif"   width=100></a></TD>
                <TD><a href="http://www.sdgqm.com"><IMG class=img height=45  src="images/index/gps_scroll_13.gif"  width=100></a></TD>
                <TD><a href="http://www.zhixianweiye.com"><IMG class=img height=45  src="images/index/gps_scroll_14.gif"  width=100></a></TD>
                <TD><a href="http://www.yili.com"><IMG class=img height=45   src="images/index/gps_scroll_15.gif"  width=100></a></TD>
                <TD><a href="http://www.bikai.com"><IMG class=img height=45  src="images/index/gps_scroll_16.gif"   width=100></a></TD>
                <TD><a href="http://www.pacificphar.com"><IMG class=img height=45    src="images/index/gps_scroll_18.gif"   width=100></a></TD>
                <TD><a href="http://yzsp.shop.linshang.com"><IMG class=img height=45   src="images/index/gps_scroll_19.gif"   width=100></a></TD>
                <TD><a href="http://loveborder.b2b.hc360.com"><IMG class=img height=45  src="images/index/gps_scroll_20.gif"  width=100></a></TD>
                <TD><a href="http://www.qiheyuan.com"><IMG class=img height=45   src="images/index/gps_scroll_22.gif"  width=100></a></TD>
                <TD><a href="http://www.bddzwy.com"><IMG class=img height=45  src="images/index/gps_scroll_23.gif"   width=100></a></TD>
                <TD><a href="http://www.cyt9.com"><IMG class=img height=45  src="images/index/gps_scroll_24.gif"  width=100></a></TD>
                <TD><a href="http://www.bxdyy.com/site/site_217307.htm"><IMG class=img height=45   src="images/index/gps_scroll_25.gif" width=100></a></TD>
                <TD><a href="http://www.taiji.com"><IMG class=img height=45  src="images/index/gps_scroll_26.gif" width=100></a></TD>
                <TD><a href="http://www.jimei.com.cn"><IMG class=img height=45   src="images/index/gps_scroll_27.gif" width=100></a></TD>
                <TD><a href="http://www.bjfood.com.cn"><IMG class=img height=45   src="images/index/gps_scroll_28.gif" width=100></a></TD>
                <TD><a href="http://www.challenge.com.cn/cy/my"><IMG class=img height=45  src="images/index/gps_scroll_29.gif" width=100></a></TD>
                <TD><a href="http://www.bjdafa.com"><IMG class=img height=45 src="images/index/gps_scroll_30.gif" width=100></a></TD>
                <TD><a href="http://www.ncpc.com.cn"><IMG class=img height=45 src="images/index/gps_scroll_31.gif" width=100></a></TD>
                <TD><a href="http://www.niulanshan.com.cn"><IMG class=img height=45  src="images/index/gps_scroll_32.gif" width=100></a></TD>
                <TD><a href="http://www.wondersun.com.cn"><IMG class=img height=45  src="images/index/gps_scroll_33.gif" width=100></a></TD>
                <TD><a href="http://www.lifan.com"><IMG class=img height=45  src="images/index/gps_scroll_34.gif"  width=100></a></TD>
                <TD><a href="http://www.huisun.com/companylist/hydf6.htm"><IMG class=img height=45 src="images/index/gps_scroll_35.gif" width=100></a></TD>
				<TD><IMG class=img height=45  src="images/index/gps_scroll_36.jpg"  width=100></TD>
				<TD><a href="http://www.sykee.com.cn/"><IMG class=img height=45  src="images/index/gps_scroll_37.jpg" width=100></TD>
                </TR></TBODY></TABLE></TD>
          <TD id=demo2 vAlign=top></TD></TR></TBODY></TABLE></DIV>
		        <SCRIPT language=JavaScript>
    var speed=20;
    demo2.innerHTML=demo1.innerHTML;    //克隆demo1为demo2
    
    function Marquee(){
        if(demo2.offsetWidth  <= demo.scrollLeft){    //当滚动至demo1与demo2交界时
            demo.scrollLeft = demo.scrollLeft - demo1.offsetWidth;        //demo跳到最顶端
        }
        else{
            demo.scrollLeft++;
        }
    }
    var MyMar=setInterval(Marquee,speed);    //设置定时器
    demo.onmouseover=function() {clearInterval(MyMar);}//鼠标移上时清除定时器达到滚动停止的目的
    demo.onmouseout=function() {MyMar=setInterval(Marquee,speed);}//鼠标移开时重设定时器
</SCRIPT>		  </td>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td height="67"></td>
        <td colspan="2">&nbsp;</td>
      </tr>
    </table></TD>
  </TR>
  <TR>
    <TD  style="BACKGROUND-IMAGE: url(images/index/LogIn_Boot1.jpg); POSITION: relative; HEIGHT: 250px"  width=1024>
	<table width="100%" height="232" border="0">
      <tr>
        <td width="3%" height="31">&nbsp;</td>
        <td width="5%" rowspan="2"><IMG  height=37 src="images/index/icon1.jpg" width=35></td>
        <td width="27%"><SPAN class=foot_1_1>Positioning Time</SPAN></td>
        <td width="5%" rowspan="2"><IMG  height=37 src="images/index/icon2.jpg" width=35></td>
        <td width="26%"><SPAN class=foot_1_1>Trajectory Queries</SPAN></td>
        <td width="5%" rowspan="2"><IMG   height=37 src="images/index/icon3.jpg" width=35></td>
        <td width="26%"><SPAN class=foot_1_1>Attendance Report</SPAN></td>
        <td width="3%">&nbsp;</td>
      </tr>
      <tr>
        <td height="28">&nbsp;</td>
        <td><SPAN class=foot_1_2>The current positioning of the subordinates at any location</SPAN></td>
        <td><SPAN class=foot_1_2>Can query the historical activities of employees track</SPAN></td>
        <td><SPAN class=foot_1_2>Also view and download staff attendance reports</SPAN></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="98">&nbsp;</td>
		<td colspan="2"><SPAN class=foot_1_3>Map given by the location of punctuation locate employees, move the mouse to 
		that point, the system gives the employees name, position description of the time and current location 
		information.</SPAN>		</td>
        <td colspan="2"><SPAN class=foot_1_3>Just set the time, you can check an employee's activities during this time the 
			direction and trajectory, the system employee is given the name of each point, positioning time and then 
			the description of the location.</SPAN>		</td>
        <td colspan="2"><SPAN class=foot_1_3>Set time period, view the attendance of all employees can also view details of 
			an employee in a period of time attendance list, including all the time and location of punctuation 
			description of the positioning information, support, export Excel tables to facilitate the corporate 
			staff.</SPAN>		</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="56">&nbsp;</td>
        <td colspan="6">
			<TABLE cellSpacing=0 cellPadding=0 width=450 align=center border=0>
			<TBODY>
				<TR>
				  <TD width=40></TD>
				  <TD width=20>&copy;</TD>
				  <TD width=121><A>2009 SOGOU.COM</A></TD>
				  <TD width=112><A  href="http://www.miibeian.gov.cn/">京ICP备 09042169号-1</A></TD>
				  <TD width=117><A  href="http://www.sogou.com/docs/terms.htm">Disclaimer</A></TD>
				  <TD width=40></TD>
				 </TR>
			 </TBODY>
			 </TABLE>		  </td>
        <td>&nbsp;</td>
      </tr>
    </table></TD>
  </TR>
  </TBODY>
</TABLE>
</form>
<MAP id=Map name=Map><AREA shape=RECT target=_blank coords=215,3,269,18 href="http://www.sogou.com/docs/terms.htm">
<AREA shape=RECT target=_blank coords=275,11,289,27  href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202005041500024">
<AREA  shape=RECT target=_blank coords=114,3,207,16  href="http://www.miibeian.gov.cn/"></MAP>
<MAP id=Map2 name=Map2>
<AREA   shape=RECT target=_blank coords=838,35,902,70   href="http://www.sohu.com/">
<AREA shape=RECT target=_blank coords=53,17,265,79  href="http://map.sogou.com/">
<AREA shape=RECT target=_blank alt=图搜天下 coords=905,33,972,68 href="http://www.sosgps.cn/">
</MAP>
<SCRIPT src="images/index/Md5.js" type=text/javascript></SCRIPT>
<SCRIPT src="images/index/login.js" type=text/javascript></SCRIPT>
<iframe id="targetFrame" name="targetFrame" height="0" style="display:none;"></iframe>
<form name="LoginForm2" method="post" action="">
<input type="hidden" name="empCode">
<input type="hidden" name="userAccount">
<input type="hidden" name="password">
<input type="hidden" name="loginType">
<input type="hidden" name="validateCode">
<input type="hidden" name="edition" value="en">
</form>
</BODY>
</HTML>
