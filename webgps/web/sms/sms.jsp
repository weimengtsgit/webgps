<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	//response.sendRedirect(path+"/error.jsp");
	//return;
}
String edition = (String)request.getSession().getAttribute("edition");
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>

<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
<script type="text/javascript" src="<%=path%>/internation/<%=edition%>/lang_sms.js"></script>

    <link rel="stylesheet" type="text/css" href="feed-viewer.css" />
<style   type="text/css">   
  <!--   
  a:link   {   
  text-decoration:   none;   
  }   
  a:visited   {   
  text-decoration:   none;   
  }   
  a:hover   {   
  text-decoration:   underline;   
  }   
  -->   
</style>
  
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/examples/state/SessionProvider.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/examples/ux/TabCloseMenu.js"></script>
<script type="text/javascript" src="<%=path%>/js/DateTool.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
var path = '<%=path%>';
</script>
<script type="text/javascript" src="<%=path%>/sms/sms.js"></script>

</head>
<body>
<div id="header"><div style="float:right;margin:5px;" class="x-small-editor"></div></div>

<!-- Template used for Feed Items -->
<div id="preview-tpl" style="display:none;">
    <div class="post-data">
        <span class="post-date">{pubDate:date("M j, Y, g:i a")}</span>
        <h3 class="post-title">{title}</h3>
        <h4 class="post-author">by {author:defaultValue("Unknown")}</h4>
    </div>
    <div class="post-body">{content:this.getBody}</div>
</div>
<!-- add by magiejue 2010-12-13 获得所有发送接收短信的请求表单 -->
<form name="excelformRec"  method="post" action="" target="_self"></form>
<form name="excelformSen"  method="post" action="" target="_self"></form>

</body>
</html>