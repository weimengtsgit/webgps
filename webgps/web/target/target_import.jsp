<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}

String targetTemplateType = user.getTargetTemplateType();
int targetTemplateType_ = targetTemplateType.equals("") ? 2 : Integer.valueOf(targetTemplateType);
if(targetTemplateType_ == 0){
	targetTemplateType = "周";
}else if(targetTemplateType_ == 1){
	targetTemplateType = "旬";
}else if(targetTemplateType_ == 2){
	targetTemplateType = "月";
}else{
	targetTemplateType = "周";
}

%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK" />
    <title></title>
    <!-- ** CSS ** -->
    <!-- base library -->
    <link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
    <!-- page specific -->
    <link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/examples/shared/examples.css" />
    <link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/examples/ux/fileuploadfield/css/fileuploadfield.css"/>

    <style type=text/css>
        .upload-icon {
            background: url('<%=path%>/ext-3.1.1/examples/shared/icons/fam/image_add.png') no-repeat 0 0 !important;
        }
        #fi-button-msg {
            border: 2px solid #ccc;
            padding: 5px 10px;
            background: #eee;
            margin: 5px;
            float: left;
        }
    </style>
    <!-- ExtJS library: base/adapter -->
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <!-- ExtJS library: all widgets -->
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
    <!-- overrides to base library -->
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/examples/ux/fileuploadfield/FileUploadField.js"></script>
	<script type="text/javascript">
		var typeDesc = '<%=targetTemplateType%>';
		var typeValue = '<%=targetTemplateType_%>';
		Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
		var path = '<%=path%>';
	</script>
    <!-- page specific -->
    <script type="text/javascript" src="<%=path%>/target/target_import.js"></script>
</head>
<body>
	<div id="fi-form"></div>
	<form name="excelform"  method="post" action="" target="_self"></form>
</body>
</html>
