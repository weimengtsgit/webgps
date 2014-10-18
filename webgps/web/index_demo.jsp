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
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<META content= name=keywords>
<META content= name=description>
<link rel="stylesheet" type="text/css" href="indexcss.css" />
<META content="MSHTML 6.00.6000.16981" name=GENERATOR>
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>
<!-- 
<script language="JavaScript" src="tip.js"></script>
-->
<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>
<script>
function englishEdition(){
	window.location.href = 'index_en.jsp';
}
</script>
<style type="text/css">
<!--
.foot_1_31 {	FONT-SIZE: 12px; LEFT: 20px; FLOAT: left; WIDTH: 252px; COLOR: #1a1817; LINE-HEIGHT: 20px; POSITION: relative; TOP: 0px; HEIGHT: 6px
}
.foot_1_11 {	FONT-SIZE: 18px; LEFT: 60px; FLOAT: left; WIDTH: 220px; COLOR: #fff; POSITION: relative; TOP: -16px; HEIGHT: 16px
}
-->
</style>
</HEAD>

<BODY>
<form name="LoginForm" method="post" action="">
<TABLE cellSpacing=0 cellPadding=0 width=1024 align=center border=0>
  <TBODY>
  <TR>
    <TD style="HEIGHT: 102px" width=1024><IMG  src="images/index/LogIn_Titel_seagate.jpg"  border=0></TD>
  </TR>
  <TR>
    <TD style="BACKGROUND-IMAGE: url(images/index/LogIn_Center1.jpg); POSITION: relative; HEIGHT: 416px" width=1024>
	<table width="100%" height="372" border="0">
      <tr>
        <td width="2%" rowspan="12">&nbsp;</td>
        <td height="16">&nbsp;</td>
        <td width="32%" rowspan="12">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td rowspan="3"><SPAN  class=bt_1>&nbsp;&nbsp;&nbsp;&nbsp;车载监控平台</SPAN><br>
		<SPAN class=bt_2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;v1.0版本</SPAN></td>
        <td height="23" class="input4"><strong>企业代码：</strong></td>
        <td><input type="text" value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" class="input1" id="empCode"/></td>
      </tr>
      <tr>
        <td height="23" class="input4"><strong id="s_userName" style="display: ">用户名：</strong></td>
        <td><input type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="input1"  id="userAccount"/></td>
      </tr>
      <tr>
        <td height="23" class="input4"><strong name="s_password" id="s_password" style="display:">密码：</strong></td>
        <td><input type="password" name="password"  class="input1" id="password"/></td>
      </tr>
      <tr>
        <td width="38%" rowspan="3">&nbsp;</td>
        <td height="23" class="input4"><strong name="s_validatecode" id="s_validatecode" style="display:">验证码：</strong></td>
        <td><input type="text" name="validateCode"  class="input3" id="validateCode"/><img src="<%=path%>/system/validateCode.do" width="60" height="20" align="middle" id="validateImg" onClick="refreshImg()"></td>
      </tr>
      <tr>
        <td width="9%" height="21" align="center"><IMG style="CURSOR: pointer" onclick=submitForm() height=19  src="images/index/LogInButton.gif" width=55></td>
        <td width="19%" ><!-- <a href="#" onclick=englishEdition()>English</a> --></td>
      </tr>
      <tr>
        <td height="29" colspan="2" class="input4"><font color="#FF0000">${error}</font></td>
        </tr>
      <tr>
        <td height="28"><!--&nbsp;<font color="#FF0000"><strong><a target="_blank" href="http://www.cmmo.cn/portal.php?mod=topic&topicid=401">图搜天下定位服务平台，火热招募全国空白区域代理</a></strong></font>--></td>
        <td colspan="2" rowspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td height="29">&nbsp;</td>
      </tr>
      <tr>
        <td height="26">&nbsp;</td>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td height="83"><SCRIPT language=JavaScript>
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
        <td height="15"></td>
        <td colspan="2">&nbsp;</td>
      </tr>
    </table></TD>
  </TR>
  <TR>
    <TD  style="BACKGROUND-IMAGE: url(images/index/LogIn_Boot1.jpg); POSITION: relative; HEIGHT: 250px"  width=1024>&nbsp;</TD>
  </TR>
  </TBODY>
</TABLE>
</form>
<MAP id=Map name=Map><!--AREA shape=RECT target=_blank coords=215,3,269,18 href="http://www.sogou.com/docs/terms.htm"-->
<!--AREA shape=RECT target=_blank coords=275,11,289,27  href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202005041500024"-->
<AREA  shape=RECT target=_blank coords=114,3,207,16  href="#"></MAP>
<SCRIPT src="images/index/Md5.js" type=text/javascript></SCRIPT>
<SCRIPT src="images/index/login.js" type=text/javascript></SCRIPT>
<iframe id="targetFrame" name="targetFrame" height="0" style="display:none;"></iframe>
<form name="LoginForm2" method="post" action="">
<input type="hidden" name="empCode">
<input type="hidden" name="userAccount">
<input type="hidden" name="password">
<input type="hidden" name="loginType">
<input type="hidden" name="validateCode">
<input type="hidden" name="edition" value="zh_cn">
<input type="hidden" name="cooperation" value="index_demo">
</form>
</BODY>
</HTML>
