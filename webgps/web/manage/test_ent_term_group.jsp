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

<div>
<script>
var tree = new WebFXTree(entName,null,null,null,null,"g0");
function test(){
	var re = new Array();
   var objs = getCheckObjects(false);
   s = objs.length;
   for(var i=0;i<s;i++){
   		var obj = objs[i];
   		var realid = objs[i].id.substring(13);
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
</script>
<input type="button" value="显示选中的节点" onclick="test()">&nbsp;&nbsp;
<c:forEach var="termGroup" items="${viewEntTermGroup}" varStatus="status">
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

<div>
<script>
var radioxloadTree = new WebFXTree(entName,null,null,null,null,"x0");
function test3(){
	var re = "";
   var obj = getCheckObject();
   if(obj!=null){
   	var realid = obj.id.substring(13);
   	if(realid.indexOf("x")!=-1){
   	}else if(realid.indexOf("r")!=-1){
   	}else{
   		re = "deviceid:"+realid+" name:"+obj.text+" typecode:"+obj.value;
   	}
   }
   alert(re);
   return re;
}
</script>
<input type="button" value="显示radio选中的节点" onclick="test3()">&nbsp;&nbsp;
<c:forEach var="termGroup" items="${viewEntTermGroup}" varStatus="status">
	<script type="text/javascript" language="javascript">
		//alert("${termGroup.id} "+"${termGroup.parentId} "+"${termGroup.groupName}"+" ${termGroup.groupSort}");
		//根据父id查找父节点，如果没有则增加到根上
		parentNode3 = getNodeByRealId("x${termGroup.parentId}");
		if(parentNode3==null){
			new WebFXLoadRadioTreeItem("${termGroup.groupName}","x${termGroup.id}","<%= request.getContextPath() %>/manage/findTermByGroupId.do?actionMethod=findTermByGroupId&groupId=${termGroup.id}",radioxloadTree,null,null,null,null,"x${termGroup.id}");
		}else{
			new WebFXLoadRadioTreeItem("${termGroup.groupName}","x${termGroup.id}","<%= request.getContextPath() %>/manage/findTermByGroupId.do?actionMethod=findTermByGroupId&groupId=${termGroup.id}",parentNode3,null,null,null,null,"x${termGroup.id}");
		}
	</script>
</c:forEach>
<script>
	document.write(radioxloadTree);
</script>
</div>

<br>

<div>
<script>
var checkboxTree = new WebFXTree(entName,null,null,null,null,"c0");
function test1(){
	var re = new Array();
   var objs = getCheckObjects(false);
   s = objs.length;
   for(var i=0;i<s;i++){
		var realid = objs[i].id.substring(13);
   		if(realid.indexOf("g")!=-1){
   		}else if(realid.indexOf("c")!=-1){
   			re[re.length]=realid.substring(1);
   		}else{
   		}
   }
   alert(re);
   return re;
}
</script>
<input type="button" value="显示选中的节点" onclick="test1()">&nbsp;&nbsp;
<c:forEach var="termGroup" items="${viewEntTermGroup}" varStatus="status">
	<script type="text/javascript" language="javascript">
		//alert("${termGroup.id} "+"${termGroup.parentId} "+"${termGroup.groupName}"+" ${termGroup.groupSort}");
		//根据父id查找父节点，如果没有则增加到根上
		parentNode1 = getNodeByRealId("c${termGroup.parentId}");
		if(parentNode1==null){
			new WebFXCheckBoxTreeItem("${termGroup.groupName}","c${termGroup.id}",checkboxTree,null,null,null,null,"c${termGroup.id}");
		}else{
			new WebFXCheckBoxTreeItem("${termGroup.groupName}","c${termGroup.id}",parentNode1,null,null,null,null,"c${termGroup.id}");
		}
	</script>
</c:forEach>
	<script>
		document.write(checkboxTree);
	</script>
</div>

<br>

<div>
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
<input type="button" value="显示radio选中的节点" onclick="test2()">&nbsp;&nbsp;
<c:forEach var="termGroup" items="${viewEntTermGroup}" varStatus="status">
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
	
	
</body>