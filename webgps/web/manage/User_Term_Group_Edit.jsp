<%@ page contentType="text/html;charset=UTF-8"%>
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
<title>修改用户可见终端组</title>

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
<link rel="stylesheet" href="../css/sap.css" type="text/css">
<link rel="StyleSheet" href="../css/dtree.css" type="text/css" />
<%
}else{	
%>
<link rel="StyleSheet" href="../t11thngame/css/main.css" type="text/css" />
<%
}
%>
<script>
var entName = '<%=entName%>';
//初始化终端组树
webFXTreeConfig.usePersistence=false;
webFXTreeConfig.cascadeCheck=false;
webFXTreeConfig.setImagePath("../images/extree/");

</script>
</head>

<body>
<div>
<script>
var checkboxTree = new WebFXTree(entName,null,null,null,null,"r-1");
checkboxTree.setRootType("checkbox", "r-1");
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

function save(){
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
   document.forms[0].groupIds.value = re;
   document.forms[0].submit();
}
</script>

<c:forEach var="termGroup" items="${entTermGroupList}" varStatus="status">
	<script type="text/javascript" language="javascript">
		//alert("${termGroup.id} "+"${termGroup.parentId} "+"${termGroup.groupName}"+" ${termGroup.groupSort}");
		//根据父id查找父节点，如果没有则增加到根上
		parentNode1 = getNodeByRealId("c${termGroup.parentId}");
		if(parentNode1==null){
			node = new WebFXCheckBoxTreeItem("${termGroup.groupName}","c${termGroup.id}",checkboxTree,null,null,null,null,"c${termGroup.id}");
		}else{
			node = new WebFXCheckBoxTreeItem("${termGroup.groupName}","c${termGroup.id}",parentNode1,null,null,null,null,"c${termGroup.id}");
		}
		node.setFolder(true);//设置为组
	</script>
</c:forEach>

<table width=100% border=0 cellspacing=0 cellpadding=0 align=center height=30>
  <tr>
	<td align=right>
      <table width="100%" border="0" cellspacing="0">
        <tr class="top">
          <td>
			<span class="spanbutton_normal" title="" style="cursor:hand;" onmouseover="this.className='spanbutton_over'" onmousedown="this.className='spanbutton_down'" onmouseup="this.className='spanbutton_up'" onmouseout="this.className='spanbutton_normal'"><a href="javascript:window.close()"><img src="../images/reload.gif" align="adsmiddle" border="0">关闭</a></span>
          </td>
        </tr>
      </table>
	</td>
  </tr>
</table>
<table width=100% border=0 cellspacing=0 cellpadding=2 align=center>
	<tr>
		<td class=baobiao_title>请输入或修改企业信息</td>
	</tr>
	
</table>
<br>
	<script>
		document.write(checkboxTree);
		checkboxTree.expandAll();
		webFXTreeConfig.cascadeCheck=false;
		webFXTreeConfig.cascadeCheckUp=true;
	</script>
</div>
<c:forEach var="termGroup" items="${termGroupList}" varStatus="status">
	<script>
		node = getNodeByRealId("c${termGroup.id}");
		if(node !=null){
			node.setChecked(true);
		}
	</script>
</c:forEach>
<input type="button" value="确定" onclick="javascript:save()">
<form action="<%= request.getContextPath() %>/manage/termGroupManage.do?actionMethod=userTermGroupSet" method="post">
	<input type="hidden" name="entCode" value="${entCode}">
	<input type="hidden" name="userId" value="${userId}">
	<input type="hidden" name="groupIds">
</form>

<c:if test="${editResult=='true'}">
	<script>
		alert("修改成功");
	</script>
</c:if>
</body>