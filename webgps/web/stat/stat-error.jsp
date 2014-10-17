<%@ page contentType="text/html;charset=GBK"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title></title>
<link rel="stylesheet" type="text/css" href="../ext-3.1.1/resources/css/ext-all.css" />
<script type="text/javascript" src="../ext-3.1.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="../ext-3.1.1/ext-all.js"></script>
<script type="text/javascript">
Ext.onReady(function(){
	Ext.Msg.alert("操作提示","包含不存在的文件！",function(btn,text){
		if (btn == 'ok')
			history.go(-1);
		});
});
</script>
</head>
<body>
</body>
</html>
