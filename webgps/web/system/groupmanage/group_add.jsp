<%@ page contentType="text/html;charset=GBK"%>
<%
String path = request.getContextPath();
%>
<html>
<head>
  <title></title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-3.1.1/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/buttons.css"/>
 	<script type="text/javascript" src="<%=path%>/ext-3.1.1/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-all.js"></script>
    <script type="text/javascript" src="<%=path%>/ext-3.1.1/ext-lang-zh_CN.js"></script>
	<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '<%=path%>/images/s.gif';
	var path = '<%=path%>';
	</script>
	<script type="text/javascript" src="<%=path%>/system/groupmanage/group_add.js"></script>
</head>
<body>
    <!-- use class="x-hide-display" to prevent a brief flicker of the content -->
    <div id="west" class="x-hide-display">
    </div>
    <div id="center1" class="x-hide-display">
    </div>
</body>
</html>