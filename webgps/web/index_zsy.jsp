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
<!-- saved from url=(0022)http://218.200.48.189/ -->
<html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>企运通平台</title>
<link href="index_zsy/second.css" type="text/css" rel="stylesheet">
<script language="JavaScript" src="index_zsy/base64.js"></script>
<script language="JavaScript" src="js/common.js"></script>
<script language="JavaScript" src="indexjs.js"></script>
<script language="javascript" for="document" event="onkeydown">
  if(event.keyCode==13){
  	submitForm();
  }
</script>
<script language="JavaScript">
<!--
function correctPNG() 
{
	for(var i=0; i<document.images.length; i++)
	{
		var img = document.images[i]
		var imgName = img.src.toUpperCase()
		if (imgName.substring(imgName.length-3, imgName.length) == "PNG")
		{
			var imgID = (img.id) ? "id='" + img.id + "' " : ""
			var imgClass = (img.className) ? "class='" + img.className + "' " : ""
			var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' "
			var imgStyle = "display:inline-block;" + img.style.cssText 
			if (img.align == "left") imgStyle = "float:left;" + imgStyle
			if (img.align == "right") imgStyle = "float:right;" + imgStyle
			if (img.parentElement.href) imgStyle = "cursor:hand;" + imgStyle 
			var strNewHTML = "<span " + imgID + imgClass + imgTitle
			+ " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;" + imgStyle + ";"
			+ "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
			+ "(src=\'" + img.src + "\', sizingMethod='scale');\"></span>" 
			img.outerHTML = strNewHTML
			i = i-1
		}
	}
}
if(window.attachEvent){
	window.attachEvent("onload", correctPNG);
}else{
	

}

//WZTPLAT-63 网页回车触发事件
document.onkeydown = function(e){
    var e = e||window.event;
    if(e.keyCode == 13) {
        submitForm();
    }
}

//-->
</script>


</head>
<body style=" background:url(index_zsy/images/login_bg_top.jpg) repeat-x ;background-color:#8ec4de;">
<div class="login_body">
  <div class="login_top">
    <table width="624" border="0" cellpadding="0" cellspacing="0">
      <tbody>
	    <tr>
          <td>
          <!-- 
          <img src="../images/common/sun1.png" />
           -->
          </td>
        </tr>
        <tr>
          <td style=" padding-left:370px; padding-top:0px;"><img src="index_zsy/images/sun2.png" height="273px;"></td>
        </tr>
      </tbody>
	</table>
  </div>
  <div class="login_bottom">
    <table border="0" cellspacing="0" cellpadding="0">
      <tbody>
	    <tr>
          <td><img src="index_zsy/images/buildings1.png"></td>
          <td><img src="index_zsy/images/buildings2.png"></td>
          <td><img src="index_zsy/images/buildings3.png"></td>
          <td><img src="index_zsy/images/buildings4.png"></td>
        </tr>
      </tbody>
	</table>
  </div>

  <div class="numeral"><img src="index_zsy/images/numeral.png" style=" margin-left:5px;"></div>
  <div class="lo_bg"><img src="index_zsy/images/location.png"></div>

  <div class="location">
    <div class="logo"><img src="index_zsy/images/logoNew.png"></div>
    <form name="LoginForm" method="post" action="">
      <table width="251" height="148" border="0" cellpadding="0" cellspacing="0">
        <tbody>
		  <tr>
            <td width="73">企业代码：</td>
            <td colspan="2"><div class="input_div"><input value="${LoginForm.empCode!=null?LoginForm.empCode:entCode}" name="empCode" id="empCode" type="text" class="location_intup1"></div></td>
          </tr>
          <tr style="display:none">
            <td width="73">登录模式：</td>
            <td colspan="2">&nbsp;
			  <select name="loginType" onchange="changeMode()">
			    <option value="1">网站密码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
			    <option value="2">随机短信</option>
		      </select>
			</td>
          </tr>
          <tr>
            <td>
			  <div id="s_userName" style="display: ">用&nbsp;&nbsp;户&nbsp;&nbsp;名：</div>
			  <div id="s_phoneNumber" style="display: none;">手&nbsp;&nbsp;机&nbsp;&nbsp;号：</div>
			</td>
            <td colspan="2">
			  <div class="input_div"><input value="${LoginForm.userAccount!=null?LoginForm.userAccount:userName}" name="userAccount" id="userAccount" type="text" class="location_intup1"></div>
			</td>
          </tr>
          <tr>
            <td>
			  <div name="s_password" id="s_password" style="display:">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</div>
			  <div name="s_dynamicMessage" id="s_dynamicMessage" style="display: none;"><a href="javascript:fetchMessage()">点击获取</a>&nbsp;</div>
			</td>
            <td colspan="2">
			  <div class="input_div"><input type="password" name="password" id="password" class="location_intup1"></div>
			</td>
          </tr>
          <tr>
            <td>验&nbsp;&nbsp;证&nbsp;&nbsp;码：</td>
            <td width="85"><div class="checking"><input name="validateCode" id="validateCode" type="text" class="location_intup2"></div></td>
            <td width="93"><img src="<%=path%>/system/validateCode.do" width="60" height="20" align="middle" id="validateImg" onclick="refreshImg()" style="padding: 0px;margin: 0px;"></td>
          </tr>
          <tr>
  	        <td>&nbsp;</td>
            <td colspan="2"><font color="#FF0000">${error}</font></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td colspan="2"><img name="submitBut" id="submitBut" style="cursor:pointer;" type="image" onclick="submitForm()" src="index_zsy/images/login_system_0.jpg" onmouseover=" this.src=&#39;index_zsy/images/login_system_1.jpg&#39;" onmouseout=" this.src=&#39;index_zsy/images/login_system_0.jpg&#39;" alt="登录系统"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </div>
</div>

<div class="copyright">Copyright 2015 中国石油西北销售分公司版权所有</div>


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
<input type="hidden" name="cooperation" value="index_zsy">
</form>
</body></html>