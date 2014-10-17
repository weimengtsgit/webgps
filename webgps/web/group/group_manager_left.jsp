<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.sosgps.wzt.orm.TTermGroup" %>
<%@ page import="com.sosgps.wzt.orm.*,com.sosgps.wzt.system.common.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
//List tTargetGroupList=(List)request.getAttribute("tTargetGroupList");
String entName = "企业名称";
String entCode = "";
Object obj = session.getAttribute("userInfo");

if (obj != null && obj instanceof UserInfo) {
	UserInfo userInfo = (UserInfo) obj;
	TEnt ent = userInfo.getEnt();
	entName = ent.getEntName();
	entCode = ent.getEntCode();
}
%>
<html>
<head>
<title>组部门管理</title>
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
<%
	if(session.getAttribute("t11thngame")==null){
%>
<%
}else{	
%>
<link rel="StyleSheet" href="../t11thngame/css/main.css" type="text/css" />
<%
}
%><script type="text/javascript" language="JavaScript">
var entName = '<%=entName%>';
//初始化终端组树
webFXTreeConfig.usePersistence=false;
webFXTreeConfig.setImagePath("../images/extree/");
function selGroupObj(groupId,groupName){
	parent.group_right.document.getElementById("groupName").value=groupName;
	parent.group_right.document.getElementById("groupId").value=groupId;
	parent.group_right.document.getElementById("groupName").disabled=false;
	parent.group_right.document.getElementById("modify").disabled=false;
	parent.group_right.document.getElementById("del").disabled=false;
	parent.group_right.document.getElementById("childGroupName").disabled=false;
	parent.group_right.document.getElementById("add").disabled=false;
}

function selTopGroup(groupId,groupName){
	parent.group_right.document.getElementById("groupName").value=groupName;
	parent.group_right.document.getElementById("groupId").value=groupId;
	parent.group_right.document.getElementById("childGroupName").disabled=false;
	parent.group_right.document.getElementById("add").disabled=false;
	parent.group_right.document.getElementById("groupName").disabled=true;
	parent.group_right.document.getElementById("modify").disabled=true;
	parent.group_right.document.getElementById("del").disabled=true;
}

</script>
</head>
<body bgcolor=#f3f3f3>
<div>
<script>
var radioTree = new WebFXTree(entName,"javascript:test()",null,null,null,"r-1");
function test(){
	var obj = getCheckObject();
    if(obj!=null){
    	getNodeByRealId(obj.id.substring(13)).setChecked(false);
    }
	var groupId = "-1";//组id
	var groupName = "";//组名称
	var groupSort = "";//顺序
	var upSortId = 0;//上面的兄弟组顺序号
	var downSortId = Number.MAX_VALUE;//下面的兄弟组顺序号
	var childrenGroupMaxId = 0;//子组最大顺序号
   		groupName = radioTree.text;
   		//子组顺序号
   		size = radioTree.childNodes.length;
   		for(var i=0;i < size;i ++){
   			sort = radioTree.childNodes[i].expend1;
   			if(sort > childrenGroupMaxId){
   				childrenGroupMaxId = sort;
   			}
   		}

    //alert(groupId+" "+groupName+" "+groupSort+" "+upSortId+" "+downSortId+" "+childrenGroupMaxId);
    parent.group_right.document.getElementById("groupName").value=groupName;
	parent.group_right.document.getElementById("groupId").value=groupId;
	parent.group_right.document.getElementById("childGroupName").disabled=false;
	parent.group_right.document.getElementById("add").disabled=false;
	parent.group_right.document.getElementById("groupName").disabled=true;
	parent.group_right.document.getElementById("modify").disabled=true;
	parent.group_right.document.getElementById("del").disabled=true;
    
	
    //parent.group_right.document.getElementById("groupName").innerHTML=groupName;
    //parent.group_right.document.getElementById("termGroupId").value=groupId;
    //parent.group_right.document.getElementById("groupSort").value=groupSort;
    //parent.group_right.document.getElementById("childrenGroupMaxId").value=childrenGroupMaxId;
    //parent.group_right.document.getElementById("upGroupSortId").value=upSortId;
    //parent.group_right.document.getElementById("downGroupSortId").value=downSortId;
    //parent.group_right.document.getElementById("groupEditDiv").style.display="";
    //parent.group_right.document.getElementById("editOrDelete").style.display="none";
    //return groupId;
}
function test2(){
	var groupId = "";//组id
	var groupName = "";//组名称
	var parentGroupId = "";//父组id
	var groupSort = "";//顺序
	var upSortId = 0;//上面的兄弟组顺序号
	var downSortId = Number.MAX_VALUE;//下面的兄弟组顺序号
	var childrenGroupMaxId = 0;//子组最大顺序号
    var obj = getCheckObject();
    if(obj!=null){
   		var realid = obj.id.substring(13);
   		groupName = obj.text;
   		groupSort = obj.expend1;
   		parentGroupId = obj.parentNode.id.substring(14);
   		if(realid.indexOf("r")!=-1){
   			groupId = realid.substring(1);
   		}
   		//兄弟组顺序号
   		brothers = obj.parentNode.childNodes;
   		size = brothers.length;
   		for(var i=0;i < size;i ++){
   			id = brothers[i].id.substring(14);
   			sort = brothers[i].expend1;
   			if(id == groupId)continue;
   			if(sort < groupSort){
   				if(sort > upSortId){
   					upSortId = sort;
   				}
   			}else{
   				if(sort < downSortId){
   					downSortId = sort;
   				}
   			}
   		}
   		if(upSortId == 0){
   			upSortId = groupSort;
   		}
   		if(downSortId == Number.MAX_VALUE){
   			downSortId = groupSort;
   		}
   		//子组顺序号
   		size = obj.childNodes.length;
   		for(var i=0;i < size;i ++){
   			sort = obj.childNodes[i].expend1;
   			if(sort > childrenGroupMaxId){
   				childrenGroupMaxId = sort;
   			}
   		}
    }
    // alert(groupId+" "+groupName+" "+parentGroupId+" "+groupSort+" "+upSortId+" "+downSortId+" "+childrenGroupMaxId);
    parent.group_right.document.getElementById("groupName").value=groupName;
	parent.group_right.document.getElementById("groupId").value=groupId;
	parent.group_right.document.getElementById("groupName").disabled=false;
	parent.group_right.document.getElementById("modify").disabled=false;
	parent.group_right.document.getElementById("del").disabled=false;
	parent.group_right.document.getElementById("childGroupName").disabled=false;
	parent.group_right.document.getElementById("add").disabled=false;
    
    //parent.group_right.document.getElementById("groupName").innerHTML=groupName;
    //parent.group_right.document.getElementById("termGroupId").value=groupId;
    //parent.group_right.document.getElementById("parentGroupId").value=parentGroupId;
    //parent.group_right.document.getElementById("groupSort").value=groupSort;
    //parent.group_right.document.getElementById("childrenGroupMaxId").value=childrenGroupMaxId;
    //parent.group_right.document.getElementById("upGroupSortId").value=upSortId;
    //parent.group_right.document.getElementById("downGroupSortId").value=downSortId;
    //parent.group_right.document.getElementById("groupEditDiv").style.display="";
    //parent.group_right.document.getElementById("editOrDelete").style.display="";
    //return groupId;
}
</script>
<c:forEach var="termGroup" items="${tTargetGroupList}" varStatus="status">
	<script type="text/javascript" language="javascript">
		
		//alert("${termGroup.id} "+"${termGroup.parentId} "+"${termGroup.groupName}"+" ${termGroup.groupSort}");
		//根据父id查找父节点，如果没有则增加到根上
		parentNode2 = getNodeByRealId("r${termGroup.parentId}");
		if(parentNode2==null){
			node = new WebFXRadioTreeItem("${termGroup.groupName}","r${termGroup.id}",radioTree,null,null,null,null,"r${termGroup.id}",null,"${termGroup.groupSort}");
			node.onchange=test2;
		}else{
			node = new WebFXRadioTreeItem("${termGroup.groupName}","r${termGroup.id}",parentNode2,null,null,null,null,"r${termGroup.id}",null,"${termGroup.groupSort}");
			node.onchange=test2;
		}
		node.setFolder(true);
	</script>
</c:forEach>
<script>
	document.write(radioTree);
</script>
</div>
</body>
</html>
