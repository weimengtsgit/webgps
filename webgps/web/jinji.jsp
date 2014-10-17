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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>哈尔滨劲楫信息技术有限公司</title>
<LINK rel=stylesheet type=text/css href="index_jinji/css/thickbox.css">
<link href="index_jinji/css/jinji.css" rel="stylesheet" type="text/css" />
<STYLE type="text/css">
<!--
body,div,a{font:menu}
.article { 
BORDER-BOTTOM: black 1px solid; width:500px; BORDER-LEFT: black 1px solid; BORDER-RIGHT: black 1px solid; BORDER-TOP: black 1px solid; FILTER: revealTrans(transition=23,duration=0.5) blendTrans(duration=0.5); POSITION: absolute; VISIBILITY: hidden; background-color: #FFCC00; padding-top: 3px; padding-right: 3px; padding-bottom: 3px; padding-left: 3px; float:left; left:640px; top:120px; clear:both;} 
-->
</STYLE>
<SCRIPT language=JavaScript1.2> 
<!-- 
function Show(divid) { 
divid.filters.revealTrans.apply(); 
divid.style.visibility = "visible"; 
divid.filters.revealTrans.play(); 
} 
function Hide(divid) { 
divid.filters.revealTrans.apply(); 
divid.style.visibility = "hidden"; 
divid.filters.revealTrans.play(); 
} 
//--> 
</script> 
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>
<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }

</script>
</head>
<body>
<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="46" align="center" valign="top" background="index_jinji/images/index_01.jpg"></td>
  </tr>
  <tr>
    <td height="599" align="center" valign="top"><table width="1003" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="71" align="center" valign="top"><table width="1003" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="431" height="71" align="center" valign="top" background="index_jinji/images/index_02_01.jpg"></td>
            <td width="572" align="center" valign="top"><table width="572" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center" valign="top"><a class=thickbox  onMouseOver=Show(aaa) onMouseOut=Hide(aaa) ><img src="index_jinji/images/index_02_02_01.jpg" width="194" height="71" border="0" /></a></td>
                <td align="center" valign="top"><a class=thickbox  onMouseOver=Show(bbb) onMouseOut=Hide(bbb) ><img src="index_jinji/images/index_02_02_02.jpg" width="189" height="71"  border="0"/></a></td>
                <td align="center" valign="top"><a class=thickbox onMouseOver=Show(ccc) onMouseOut=Hide(ccc)  ><img src="index_jinji/images/index_02_02_03.jpg" width="189" height="71"  border="0"/></a></td>
              </tr>
            </table></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="495" align="center" valign="top" background="index_jinji/images/index_03.jpg"><table width="1003" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="600" height="495" align="left" valign="top" background="index_jinji/images/jj001_01.jpg"></td>
            <td width="403" align="left" valign="top" background="index_jinji/images/jj001_02.jpg"><table width="403" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="252" align="left" valign="top"></td>
              </tr>
              <tr>
                <td height="139" align="left" valign="top">
                <form name="LoginForm" method="post" action="">
                    <table width="270" height="140" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="95" align="right" valign="middle" class="z2">企业代码：</td>
                        <td width="10">&nbsp;</td>
                         <td width="165" align="left" valign="middle">
                         <input type="text" value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" class="bd1" id="empCode"/>
                         </td>
                      </tr>
                      <tr>
                        <td width="95" align="right" valign="middle" class="z2">用户名：</td>
                        <td width="10">&nbsp;</td>
                        <td width="165" align="left" valign="middle">
                        <input type="text" value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" class="bd1"  id="userAccount"/></td>
                      </tr>
                      <tr>
                        <td width="95" align="right" valign="middle" class="z2">密　码：</td>
                        <td width="10">&nbsp;</td>
                         <td width="165" align="left" valign="middle">
                         <input type="password" name="password"  class="bd1" id="password"/></td>
                      </tr>
                      <tr>
                        <td width="95" align="right" valign="middle" class="z2">验证码：</td>
                        <td width="10">&nbsp;</td>
                        <td width="165" align="left" valign="middle"><table width="158" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="50" align="left"><input type="text" name="validateCode"  class="bd2" id="validateCode"/></td>
                            <td width="59" align="right"><img src="<%=path%>/system/validateCode.do"  width="59" height="18"  id="validateImg" onClick="refreshImg()" /></td>
                          </tr>
                        </table></td>
                      </tr>
                      <tr>
                        <td width="95">&nbsp;</td>
                        <td width="10">&nbsp;</td>
                        <td width="165" align="left" valign="bottom"><label>
                          <IMG style="CURSOR: pointer" onclick=submitForm()   src="index_jinji/images/ann_dl.jpg" />
                        </label></td>
                      </tr>
                      <tr><td >&nbsp;</td><td >&nbsp;</td><td > <font color="#FF0000">${error}</font></td></tr>
                    </table>
                </form></td>
              </tr>
            </table></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="33" align="center" valign="middle" background="index_jinji/images/index_04.jpg" class="z1">关于我们 | 联系方式 | 在线留言 | 人才招聘 | 友情链接</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="102" align="center" valign="middle" background="index_jinji/images/index_05.jpg" class="z1">哈尔滨劲楫信息技术有限公司<br />
      电话：0451-87282552   网址：http://www.spaddle.cn<br />
    服务信箱：service@spaddle.cn </td>
  </tr>
</table>
 <div id="aaa" class="article">针对企业外勤、营销人员的定位。可通过管理平台，实现外勤、营销人员的签到签退、位置查询、轨迹回放、及客户拜访情况的统计汇总。是企业提升外勤、销售团队的管理利器！</div>
<div id="bbb" class="article">适用于物流、配送、市政服务、校车、及各类公私车辆的位置、行驶线路查询管理。并提供行驶超速、超范围警报，及必要情况下的远程断油、断电等安全管理功能。</div>
<div id="ccc" class="article">针对老人、小孩的位置信息查询、活动轨迹查询。对于独居老人及学生，即可以通过实时的位置查询，了解到他们的位置和安全状态，进行积极的预防，一旦亲人有紧急情况需要帮助，也可透过位置查询，及时的赶至亲人身边。</div>
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
<input type="hidden" name="cooperation" value="jinji">
</form>
</body>
</html>
