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
<META content=�� name=description>
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
    <TD style="HEIGHT: 102px" width=1024><IMG  src="" useMap=#Map2  border=0></TD>
  </TR>
  <TR>
    <TD style="BACKGROUND-IMAGE: url(); POSITION: relative; HEIGHT: 416px" width=1024>
	<table width="100%" height="372" border="0">
      <tr>
        <td width="2%" rowspan="12">&nbsp;</td>
        <td height="16">&nbsp;</td>
        <td width="32%" rowspan="12">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td rowspan="3"><SPAN  class=bt_1>&nbsp;&nbsp;&nbsp;</SPAN><br>
		<SPAN class=bt_2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</SPAN></td>
        <td height="23" class="input4"><strong>��ҵ���룺</strong></td>
        <td><input type="text" value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" class="input1" id="empCode"/></td>
      </tr>
      <tr>
        <td height="23" class="input4"><strong id="s_userName" style="display: ">�û�����</strong></td>
        <td><input type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="input1"  id="userAccount"/></td>
      </tr>
      <tr>
        <td height="23" class="input4"><strong name="s_password" id="s_password" style="display:">���룺</strong></td>
        <td><input type="password" name="password"  class="input1" id="password"/></td>
      </tr>
      <tr>
        <td width="38%" rowspan="3">
		 <SPAN style="FONT-SIZE: 12px; WIDTH: 340px; COLOR: #555346; TEXT-INDENT: 2em; LINE-HEIGHT: 20px; FONT-FAMILY: '����'">
		 </SPAN>	</td>
        <td height="23" class="input4"><strong name="s_validatecode" id="s_validatecode" style="display:">��֤�룺</strong></td>
        <td><input type="text" name="validateCode"  class="input3" id="validateCode"/><img src="<%=path%>/system/validateCode.do" width="60" height="20" align="middle" id="validateImg" onClick="refreshImg()"></td>
      </tr>
      <tr>
        <td width="9%" height="21" align="center"><IMG style="CURSOR: pointer" onclick=submitForm() height=19  src="images/index/LogInButton.gif" width=55></td>
        <td width="19%" ><a href="#" onclick=englishEdition()>English</a></td>
      </tr>
      <tr>
        <td height="29" colspan="2" class="input4"><font color="#FF0000">${error}</font></td>
        </tr>
      <tr>
        <td height="28"></td>
        <td colspan="2" rowspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td height="29">&nbsp;<font color="#FF0000"><strong></strong></font></td>
      </tr>
      <tr>
        <td height="26"><SPAN style="FONT-SIZE: 12px; COLOR: #555346"><IMG height=14 src="images/index/icon-dw.gif" width=14> ǩԼ�ͻ�</SPAN></td>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td height="83"><DIV id=demo style="OVERFLOW: hidden; WIDTH: 310px; HEIGHT: 50px">
      <TABLE cellPadding=0 align=left border=0 cellspace="0">
        <TBODY>
        <TR>
          <TD id=demo1 vAlign=top>
            <TABLE cellSpacing=0 cellPadding=5 border=0>
              <TBODY>
              <TR>
                </TR></TBODY></TABLE></TD>
          <TD id=demo2 vAlign=top></TD></TR></TBODY></TABLE></DIV>
		        <SCRIPT language=JavaScript>

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
    <TD  style="BACKGROUND-IMAGE: url(images/index/LogIn_Boot1.jpg); POSITION: relative; HEIGHT: 250px"  width=1024>
	<table width="100%" height="205" border="0">
      <tr>
        <td width="3%" height="31">&nbsp;</td>
        <td width="5%" rowspan="2"><IMG  height=37 src="images/index/icon1.jpg" width=35></td>
        <td width="27%"><span class="foot_1_1">��ʱ��λ</span></td>
        <td width="5%" rowspan="2"><IMG  height=37 src="images/index/icon2.jpg" width=35></td>
        <td width="26%"><SPAN class=foot_1_1>�켣��ѯ</SPAN></td>
        <td width="5%" rowspan="2"><IMG   height=37 src="images/index/icon3.jpg" width=35></td>
        <td width="26%"><SPAN class=foot_1_1>���ڱ���</SPAN></td>
        <td width="3%">&nbsp;</td>
      </tr>
      <tr>
        <td height="15">&nbsp;</td>
        <td><SPAN class=foot_1_2>��ʱ��λ����Ա���ĵ�ǰ����λ��</SPAN></td>
        <td><SPAN class=foot_1_2>���Բ�ѯԱ������ʷ��켣</SPAN></td>
        <td><SPAN class=foot_1_2>ͬʱ�鿴������Ա���Ŀ��ڱ���</SPAN></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="88">&nbsp;</td>
		<td colspan="2"><span class="foot_1_31">��ͼ��������λԱ������λ�õı�㣬����ƶ����õ㣬ϵͳ����Ա�����ơ���λʱ�估��ǰ����λ��������Ϣ��</span></td>
        <td colspan="2"><span class="foot_1_31">ֻ���趨ʱ��Σ����ɲ�ѯĳ��Ա���ڴ�ʱ����ڵĻ����͹켣��ϵͳͬʱ����ÿ�����Ա�����ơ���λʱ�估��ʱ����λ�õ�������Ϣ��</span></td>
        <td colspan="2"><span class="foot_1_31">�趨ʱ��Σ��鿴����Ա���Ŀ��������Ҳ���Բ鿴ĳ��Ա��ĳʱ����ڵ���ϸ�����б����а������б��Ķ�λʱ�估λ��������Ϣ��֧��Excel��񵼳���������ҵ����Ա����</span></td>
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
				  <TD width=121><A></A></TD>
				  <TD width=112></A></TD>
				  <TD width=117><!--A  href="http://www.sogou.com/docs/terms.htm">Disclaimer</A--></TD>
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
<MAP id=Map name=Map><!--AREA shape=RECT target=_blank coords=215,3,269,18 href="http://www.sogou.com/docs/terms.htm"-->
<!--AREA shape=RECT target=_blank coords=275,11,289,27  href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202005041500024"-->
<AREA  shape=RECT target=_blank coords=114,3,207,16  href="http://www.miibeian.gov.cn/"></MAP>
<MAP id=Map2 name=Map2>
<AREA   shape=RECT target=_blank coords=838,35,902,70   href="http://www.ct10000.com/">
<AREA shape=RECT target=_blank coords=53,17,265,79  href="http://www.sosgps.cn/">
<AREA shape=RECT target=_blank alt= coords=905,33,972,68 href="http://www.sosgps.cn/">
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
<input type="hidden" name="edition" value="zh_cn">
</form>
</BODY>
</HTML>
