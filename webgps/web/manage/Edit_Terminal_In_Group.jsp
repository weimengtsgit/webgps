<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.sosgps.wzt.orm.*,com.sosgps.wzt.system.common.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<% 
String entName = "企业名称";
Object obj = session.getAttribute("userInfo");

if (obj != null && obj instanceof UserInfo) {
	UserInfo userInfo = (UserInfo) obj;
	TEnt ent = userInfo.getEnt();
	entName = ent.getEntName();
}

 %>
  <head>
<title></title>

<!-- exTree js and css -->
<script type="text/javascript" language="javascript" src="../js/extree/xtree.js"></script>
<script type="text/javascript" language="javascript" src="../js/extree/xloadtree.js"></script>
<script type="text/javascript" language="javascript" src="../js/extree/xmlextras.js"></script>
<script type="text/javascript" language="javascript" src="../js/extree/map.js"></script>
<script type="text/javascript" language="javascript" src="../js/extree/checkboxTreeItem.js"></script>
<script type="text/javascript" language="javascript" src="../js/extree/checkboxXLoadTree.js"></script>
<script type="text/javascript" language="javascript" src="../js/extree/radioTreeItem.js"></script>
<script type="text/javascript" language="javascript" src="../js/extree/radioXLoadTree.js"></script>
<link type="text/css" rel="stylesheet" href="../css/extree/xtree.css" />
<script>
var entName = '<%=entName%>';
//初始化终端组树
webFXTreeConfig.usePersistence=false;
webFXTreeConfig.setImagePath("../images/extree/");
</script>

</head>
<body>

<div style="width:45%;position:absolute;top:0px;left:0px;">
<script>
var tree = new WebFXTree(entName,null,null,null,null,"g0");
function test(){
   var re = new Array();
   var objs = getCheckObjects(false);
   s = objs.length;
   for(var i=0;i<s;i++){
   		var obj = objs[i];
   		var realid = obj.id.substring(13);
   		if(realid.indexOf("g")!=-1){
   		}else if(realid.indexOf("c")!=-1){
   		}else{
   			re[re.length]="deviceid:"+realid+" name:"+obj.text+" typecode:"+obj.value;
   		}
   		//alert(realid);
   }
   alert(re);
   return re;
}
function testDesc(){
	var objs = getCheckObjects(false);
	for(var i=0; i<objs.length; i++){
		var obj = objs[i];
		//var node = getNodeById(obj.id);
		if(obj.realId!=null && obj.realId.indexOf("g")==-1){
			value = document.getElementById("desc").value;
			obj.setDesc(value);
		}
	}
}
</script>
<!-- input type="button" value="显示选中的终端节点信息" onclick="test()"><br>
节点描述:<input type="text" name="desc" id="desc" value="<font color='red'>test</font>">
<input type="button" value="设置" onclick="testDesc()"-->&nbsp;&nbsp;
<c:forEach var="termGroup" items="${userViewTermGroup}" varStatus="status">
	<script type="text/javascript" language="javascript">
		//alert("${termGroup.id} "+"${termGroup.parentId} "+"${termGroup.groupName}"+" ${termGroup.groupSort}");
		//根据父id查找父节点，如果没有则增加到根上
		parentNode = getNodeByRealId("g${termGroup.parentId}");
		if(parentNode==null){
			new WebFXLoadCheckBoxTreeItem("${termGroup.groupName}","g${termGroup.id}","<%= request.getContextPath() %>/manage/findTermByGroupId.do?actionMethod=findTermByGroupId&groupId=${termGroup.id}",tree,null,null,null,null,"g${termGroup.id}");
		}else{
			new WebFXLoadCheckBoxTreeItem("${termGroup.groupName}","g${termGroup.id}","<%= request.getContextPath() %>/manage/findTermByGroupId.do?actionMethod=findTermByGroupId&groupId=${termGroup.id}",parentNode,null,null,null,null,"g${termGroup.id}");
		}
	</script>
</c:forEach>
<div id="locate">
	<script>
		document.write(tree);
	</script>
</div>
</div>

<br>

<div style="width:45%;position:absolute;top:0px;right:0px;">
<script>
var radioTree = new WebFXTree(entName,null,null,null,null,"r0");

function test2(){
	var re = "";
   var obj = getCheckObject();
   if(obj!=null){
   	var realid = obj.id.substring(13);
   	if(realid.indexOf("r")!=-1){
   		re = realid.substring(1);
   	}
   }
   alert(re);
   return re;
}
</script>
<!-- input type="button" value="显示radio选中的节点" onclick="test2()"-->&nbsp;&nbsp;
<c:forEach var="termGroup" items="${userViewTermGroup}" varStatus="status">
	<script type="text/javascript" language="javascript">
		//alert("${termGroup.id} "+"${termGroup.parentId} "+"${termGroup.groupName}"+" ${termGroup.groupSort}");
		//根据父id查找父节点，如果没有则增加到根上
		parentNode2 = getNodeByRealId("r${termGroup.parentId}");
		if(parentNode2==null){
			new WebFXRadioTreeItem("${termGroup.groupName}","r${termGroup.id}",radioTree,null,null,null,null,"r${termGroup.id}");
		}else{
			new WebFXRadioTreeItem("${termGroup.groupName}","r${termGroup.id}",parentNode2,null,null,null,null,"r${termGroup.id}");
		}
	</script>
</c:forEach>
<script>
	document.write(radioTree);
</script>
</div>

<div style="width:100%;height:100%;position:absolute;bottom:0px;text-align:center;">
<input type="button" value="确定" onclick="moveTerminalGroup()">
</div>
<script>
/*************** help ***************/
//取得应用路径
function getAppPath(){
	url = window.location.href;
	pathname = window.location.pathname;
	pathname = pathname.substr(1,pathname.length);
	i = pathname.indexOf("/");
	pathname = pathname.substr(0,i);
	re = window.location.protocol + "//" + window.location.host;
	re += "/"+pathname;
	return re;
}
//移动终端至终端组
function moveTerminalGroup(){
	var deviceIds = "";
    var objs = getCheckObjects(false);
    s = objs.length;
    for(var i=0;i<s;i++){
   		var obj = objs[i];
   		var realid = obj.id.substring(13);
   		if(realid.indexOf("g")!=-1){
   		}else if(realid.indexOf("c")!=-1){
   		}else{
   			deviceIds+=realid+",";
   		}
    }
    if(deviceIds.length>0){
    	deviceIds = deviceIds.substring(0,deviceIds.length-1);
    }
    //alert(deviceIds);
	if(deviceIds==""){
		alert("请选择要移动的终端");
		return;
	}
	var groupId = "";
	var obj2 = getCheckObject();
    if(obj2!=null){
   		var realid2 = obj2.id.substring(13);
   		if(realid2.indexOf("r")!=-1){
   			groupId = realid2.substring(1);
   		}
   	}
    //alert(groupId);
    if(groupId==""){
		alert("请选择移至终端组");
		return;
	}
	var xmlHttp = XmlHttp.create();
	//alert(xmlHttp);
	xmlHttp.open("GET", getAppPath()+"/terminalManage.do?actionMethod=terminalInGroup&deviceIds="+deviceIds+"&groupId="+groupId, true);	// async
	xmlHttp.onreadystatechange = function () {
		if (xmlHttp.readyState == 4) {
			//alert(xmlHttp.responseText);
			tig_parseRespXy(deviceIds,groupId,xmlHttp.responseXML);
		}
	};
	// call in new thread to allow ui to update
	window.setTimeout(function () {
		//xmlHttp.setrequestheader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("caller=13800000000&called="+called+"&content="+content);
		xmlHttp.send(null);
	}, 10);
}
//解析
function tig_parseRespXy(deviceIds,groupId,oXmlDoc){
	if(oXmlDoc==null){
		alert("返回为空，操作失败");
		return;
	}
	if(oXmlDoc.getElementsByTagName("r")!=null && oXmlDoc.getElementsByTagName("r").length==1){
		var r = oXmlDoc.getElementsByTagName("r")[0].firstChild.nodeValue;
		if(r=="true"){
			//操作成功
			var groupNode = getNodeByRealId("g"+groupId);
			if(groupNode!=null){
				var ds = deviceIds.split(",");
				var s = ds.length;
    			for(var i=0;i<s;i++){
	   				var d = ds[i];
   					var node = getNodeByRealId(d);
   					if(node!=null){
   						if(groupNode.loaded==true){
   							node.setChecked(false);
   							node.move(groupNode);
   						}else{
   							node.setChecked(false);
   							node.remove();
   							groupNode.expand();
   						}
   					}
    			}
    			alert("操作成功");
    		}
		}else{
			alert("操作失败");
			return;
		}
	}
}
</script>
</body>