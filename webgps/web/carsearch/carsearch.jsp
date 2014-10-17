<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ page import="com.sosgps.wzt.system.common.UserInfo" %>
<%
String path = request.getContextPath();
UserInfo user = (UserInfo) request.getSession().getAttribute("userInfo");
if(user==null){
	response.sendRedirect(path+"/error.jsp");
	return;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<link href="<%=path%>/css/supermap.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript" src="<%=path%>/js/mapSearch.js"></script>
<title>地图搜索</title>
</head>
<body>
<!-- start -->
<div class="supermap_bg" id="supermap_bg" style="display:;"></div>

<!--搜索div start-->
<div class="supermap_list" style="display:;color:#375b96;" id="listdiv">
<ul class="supermap_map_cont">
<li class="supermap_cur" onclick="showLS()" id="li_ls">本地搜索</li>
<li class="" onclick="showRS()" id="li_rs">驾车搜索</li>
</ul>

<!--地图搜索start-->
<div class="supermap_map_input" style="display:block;" id="list_ls"><form action="javascript:LS_localSearch_submit();">　在&nbsp;&nbsp;<input name="local_cityname" id="local_cityname" value="北京" type="text" class="s_input1" /><img src="../images/pane/supermap_changecity.gif" onclick="javascript:citylist_show('40px','56px')"/><br />查找&nbsp;&nbsp;<input name="local_keyword" id="local_keyword" type="text" value="" class="supermap_input3" /><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;例如：<a href="javascript:setValue('银行','local_keyword');">银行</a>&nbsp;&nbsp;<a href="javascript:setValue('加油站','local_keyword');">加油站</a><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="地图搜索" tabindex="23" style="cursor:pointer;" /></form>
</div>

<div class="supermap_map_list" style="display:none;" id="M_search_list">
<div class="supermap_listtop" id="LS_resultList"></div>
<div class="supermap_page" id="LS_resultList_page"></div>
<div style="width:100%; height:0; line-height:0; overflow:hidden; clear:both;"></div>
</div>
<!--地图搜索end-->



<!--驾车线路start-->
<div class="supermap_route_input" style="display:none;" id="list_rs"><form action="javascript:QN_driveLine_submit()">从&nbsp;&nbsp;<input name="QN_driveLine_citynameS" id="QN_driveLine_citynameS" type="text" class="supermap_input5" value="北京"/><img src="../images/pane/supermap_changecity.gif" onclick="javascript:citylist_show('28px','57px')" />&nbsp;&nbsp;<input name="QN_driveLine_startname" id="QN_driveLine_startname" type="text" class="supermap_input4" value="请输入起点" onBlur="javascript:if(QN_driveLine_startname.value==''){QN_driveLine_startname.value='请输入起点';}" onfocus="javascript:if(QN_driveLine_startname.value=='请输入起点'){QN_driveLine_startname.value='';}"/><br />到&nbsp;&nbsp;<input name="QN_driveLine_citynameE" id="QN_driveLine_citynameE" type="text" class="supermap_input5" value="济南"/><img src="../images/pane/supermap_changecity.gif" onclick="javascript:citylist_show('28px','81px')" />&nbsp;&nbsp;<input name="QN_driveLine_endname" id="QN_driveLine_endname" type="text" class="supermap_input4" value="请输入终点" onBlur="javascript:if(QN_driveLine_endname.value==''){QN_driveLine_endname.value='请输入终点';}" onfocus="javascript:if(QN_driveLine_endname.value=='请输入终点'){QN_driveLine_endname.value=''}"/><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="驾车线路" style="cursor:pointer;"/>&nbsp;&nbsp;</form>
</div>

<!--选择起点终点start-->
<div class="supermap_choose" id="M_drivechooseDIV" style="display:none">
	<div class="supermap_start" id="M_start_end_Drivecontent"></div>
		<div class="supermap_start1">
			<span style=" font-size:14px;">请选择起点：</span><br />
				<ul class="supermap_startarea" id="M_Drivestartlist_rs"></ul>
				<span style=" font-size:14px;">请选择终点：</span><br />
				<ul class="supermap_startarea" id="M_Drivestartlist_re"></ul>
		</div>
	<div class="supermap_start2"><input type="button" value="查 询" onclick="QN_driveLine_search()" style="cursor:pointer;"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="返 回" onclick="QN_driveChange_chooseBack()" style="cursor:pointer;"/></div>
</div>


<div class="supermap_route_list" style="display:none"  id="M_drive_list">
<div class="supermap_listtop" id="QN_driveresultList">
</div>
<div style=" width:100%; height:0; clear:both; line-height:0; overflow:hidden;"></div>
</div>
<!--驾车线路end-->
<!--热点城市start-->
<div id="M_citysmall" class="supermap_self_hot_div" Style="display:none">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
	<td colspan="4" align="left"><div class="supermap_citytitle"><span style="float:left;color:#FF9900;"><strong>[热门城市]</strong>&nbsp;&nbsp;<a href="javascript:cityall_show()">所有城市</a></span><div onclick="javascript:citylist_hide()" class="supermap_city_close" onmouseOver="this.className='supermap_city_close1'" onmouseOut="this.className='supermap_city_close'"></div></div></td>
    </tr>
  <tr>
	<td height="19" align="center"><a class="supermap_yellow" href="javascript:setcityvalue('北京')">北京</a></td>
    <td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('上海')">上海</a></td>
    <td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('广州')">广州</a></td>
    <td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('深圳')">深圳</a></td>
  </tr>
  <tr>
  	<td height="19" align="center"><a class="supermap_yellow" href="javascript:setcityvalue('杭州')">杭州</a></td>
    <td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('苏州')">苏州</a></td>
    <td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('成都')">成都</a></td>
    <td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('济南')">济南</a></td>
  </tr>
  <tr>
  	<td height="19" align="center"><a class="supermap_yellow" href="javascript:setcityvalue('郑州')">郑州</a></td>
	<td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('青岛')">青岛</a></td>
    <td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('沈阳')">沈阳</a></td>
    <td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('天津')">天津</a></td>
  </tr>
  <tr>
    <td height="19" align="center"><a class="supermap_yellow" href="javascript:setcityvalue('重庆')">重庆</a></td>
	<td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('武汉')">武汉</a></td>
	<td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('大连')">大连</a></td>
	<td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('长沙')">长沙</a></td>
  </tr>
  <tr>
    <td height="19" align="center"><a class="supermap_yellow" href="javascript:setcityvalue('南京')">南京</a></td>
	<td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('西安')">西安</a></td>
	<td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('包头')">包头</a></td>
	<td align="center"><a class="supermap_yellow" href="javascript:setcityvalue('福州')">福州</a></td>
  </tr>
</table>
</div>
<!--热点城市end-->
<!--所有城市start-->
<div id="M_cityall" class="supermap_self_all_div" style="display:none">
<div class="supermap_citytitle"><span style="float:left;color:#FF9900;"><a href="javascript:cityall_hide()">热门城市</a>&nbsp;&nbsp;<strong>[所有城市]</strong></span><div class="supermap_city_close" onclick="javascript:citylist_hide()" onmouseOver="this.className='supermap_city_close1'" onmouseOut="this.className='supermap_city_close'"><a href=""></a></div></div>
<div class="supermap_onbyone">
<table width="162" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>A</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('安庆')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>anqing </td><td align="right">安庆</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('鞍山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>anshan </td><td align="right">鞍山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('安阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>anyang </td><td align="right">安阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('安康')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>ankang </td><td align="right">安康</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('安顺')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>anshun </td><td align="right">安顺</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('阿坝藏族羌族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>abeizizhizhou </td><td align="right">阿坝藏族羌族自治州</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('阿拉善盟')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>alashanmeng </td><td align="right">阿拉善盟</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('阿拉善盟')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>alidiqu </td><td align="right">阿里地区</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('阿克苏地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>akesu </td><td align="right">阿克苏地区</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('阿勒泰地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>aletai </td><td align="right">阿勒泰地区</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('澳门')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>aomen </td><td align="right">澳门</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>B</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('北京')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>beijing </td><td align="right">北京</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('保定')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baoding </td><td align="right">保定</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宝鸡')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baoji </td><td align="right">宝鸡</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('本溪')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>benxi </td><td align="right">本溪</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('包头')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baotou </td><td align="right">包头</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('蚌埠')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>bengbu </td><td align="right">蚌埠</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('北海')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>beihai </td><td align="right">北海</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('亳州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>haozhou </td><td align="right">亳州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('巴中')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>bazhong </td><td align="right">巴中</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('毕节地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>bijie </td><td align="right">毕节地区</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('滨州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>bingzhou </td><td align="right">滨州</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('白城')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baicheng </td><td align="right">白城</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('白山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baishan </td><td align="right">白山</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('白银')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baiyin </td><td align="right">白银</td>
</tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('博尔塔拉蒙古自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>boertala </td><td align="right">博尔塔拉蒙古自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('百色')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baise<td align="right">百色</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('白沙县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baishaxian </td><td align="right">白沙县</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('保亭县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baotingxian </td><td align="right">保亭县</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('巴彦淖尔盟')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>bayanzuoermeng </td><td align="right">巴彦淖尔盟</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('巴音郭楞州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>bayinguolengzhou </td><td align="right">巴音郭楞州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('保山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>baoshan </td><td align="right">保山</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>C</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('成都')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chengdu </td><td align="right">成都</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('重庆')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chongqing </td><td align="right">重庆</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('长春')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>changchun </td><td align="right">长春</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('长沙')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>changsha </td><td align="right">长沙</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('巢湖')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chaohu </td><td align="right">巢湖</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('潮州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chaozhou </td><td align="right">潮州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('常德')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>changde </td><td align="right">常德</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('承德')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chengde </td><td align="right">承德</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('常州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>changzhou </td><td align="right">常州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('池州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chizhou </td><td align="right">池州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('滁州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chuzhou </td><td align="right">滁州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('郴州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>binzhou </td><td align="right">郴州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('赤峰')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chifeng </td><td align="right">赤峰</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('长治')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>changzhi </td><td align="right">长治</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('沧州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>cangzhou </td><td align="right">沧州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('朝阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chaoyang </td><td align="right">朝阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('崇左')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chongzuo </td><td align="right">崇左</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('楚雄彝族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chuxiongyizu </td><td align="right">楚雄彝族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('昌吉回族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>changjihuizu </td><td align="right">昌吉回族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('昌江县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>changjiangxian </td><td align="right">昌江县</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('澄迈县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>chengmaixian </td><td align="right">澄迈县</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('昌都地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>changdu </td><td align="right">昌都地区</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>D</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('大连')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dalian </td><td align="right">大连</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('东莞')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dongguan </td><td align="right">东莞</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('大庆')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>daqing </td><td align="right">大庆</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('丹东')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dandong </td><td align="right">丹东</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('德州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dezhou </td><td align="right">德州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('德阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>deyang </td><td align="right">德阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('东营')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dongying </td><td align="right">东营</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('大同')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>datong </td><td align="right">大同</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('定西')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dingxi </td><td align="right">定西</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('达州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dazhou </td><td align="right">达州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('大兴安岭地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>daxinganling </td><td align="right">大兴安岭地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('大理白族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dalibaizu </td><td align="right">大理白族自治州</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('定安县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dinganxian </td><td align="right">定安县</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('东方')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dongfang </td><td align="right">东方</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('东沙群岛')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dongshaqundao </td><td align="right">东沙群岛</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('儋州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>danzhou </td><td align="right">儋州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('德宏州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>dehongzhou </td><td align="right">德宏州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('迪庆州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>diqingzhou </td><td align="right">迪庆州</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>E</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('鄂尔多斯')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>eerduosi </td><td align="right">鄂尔多斯</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('鄂州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>ezhou </td><td align="right">鄂州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('恩施土家族苗族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>enshi </td><td align="right">恩施土家族苗族自治州</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>F</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('福州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>fuzhou </td><td align="right">福州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('佛山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>fushan </td><td align="right">佛山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('抚顺')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>fushan </td><td align="right">抚顺</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('凤凰')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>fenghuang </td><td align="right">凤凰</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('防城港')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>fangchenggang </td><td align="right">防城港</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('阜阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>fuyang </td><td align="right">阜阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('阜新')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>fuxin </td><td align="right">阜新</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>G</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('广州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>guangzhou </td><td align="right">广州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('贵阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>guiyang </td><td align="right">贵阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('桂林')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>guiyang </td><td align="right">桂林</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('赣州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>ganzhou </td><td align="right">赣州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('贵港')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>guigang </td><td align="right">贵港</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('广安')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>guangan </td><td align="right">广安</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('广元')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>guangyuan </td><td align="right">广元</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('甘孜藏族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>ganzizangzu </td><td align="right">甘孜藏族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('甘南州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>gannanzhou </td><td align="right">甘南州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('固原')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>guyuan </td><td align="right">固原</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('果洛州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>guoluozhou </td><td align="right">果洛州</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>H</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('衡阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hengyang </td><td align="right">衡阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('黄山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huangshan </td><td align="right">黄山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('合肥')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hefei </td><td align="right">合肥</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('邯郸')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>handan </td><td align="right">邯郸</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('惠州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huizhou </td><td align="right">惠州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('杭州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hangzhou </td><td align="right">杭州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('海口')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>haikou </td><td align="right">海口</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('湖州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huzhou </td><td align="right">湖州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('黄石')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huangshi </td><td align="right">黄石</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('葫芦岛')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huludao </td><td align="right">葫芦岛</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('哈尔滨')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>haerbin </td><td align="right">哈尔滨</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('呼和浩特')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huhehaote </td><td align="right">呼和浩特</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('淮北')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huaibei </td><td align="right">淮北</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('淮南')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huainan </td><td align="right">淮南</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('淮安')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huaian </td><td align="right">淮安</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('河源')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>heyuan </td><td align="right">河源</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('河池')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hechi </td><td align="right">河池</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('贺州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hezhou </td><td align="right">贺州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('黑河')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>heihe </td><td align="right">黑河</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('黄冈')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huanggang </td><td align="right">黄冈</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('鹤壁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hebi </td><td align="right">鹤壁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('衡水')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hengshui </td><td align="right">衡水</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('菏泽')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>heze </td><td align="right">菏泽</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('怀化')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huaihua </td><td align="right">怀化</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('鹤岗')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hegang </td><td align="right">鹤岗</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('呼伦贝尔')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hulunbeier </td><td align="right">呼伦贝尔</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('汉中')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hanzhong </td><td align="right">汉中</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('海东地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>haidong </td><td align="right">海东地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('海北藏族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>haibei </td><td align="right">海北藏族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('海南藏族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hainan </td><td align="right">海南藏族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('红河哈尼族彝族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>honghe </td><td align="right">红河哈尼族彝族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('黄南州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>huangnanzhou </td><td align="right">黄南州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('哈密地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hamidiqu </td><td align="right">哈密地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('和田地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>hetiandiqu </td><td align="right">和田地区</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>J</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('焦作')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jiaozuo </td><td align="right">焦作</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('荆州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jingzhou </td><td align="right">荆州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('荆门')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jingmen </td><td align="right">荆门</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('吉林')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jilin </td><td align="right">吉林</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('江门')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jiangmen </td><td align="right">江门</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('揭阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jieyang </td><td align="right">揭阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('金华')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jinhua </td><td align="right">金华</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('金昌')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jinchang </td><td align="right">金昌</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('嘉兴')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jiaxing </td><td align="right">嘉兴</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('九江')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jiujiang </td><td align="right">九江</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('锦州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jinzhou </td><td align="right">锦州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('济南')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jinan </td><td align="right">济南</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('佳木斯')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jiamusi </td><td align="right">佳木斯</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('景德镇')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jingdezhen </td><td align="right">景德镇</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('济宁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jining </td><td align="right">济宁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('济源')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jiyuan </td><td align="right">济源</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('吉安')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jian </td><td align="right">吉安</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('晋城')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jincheng </td><td align="right">晋城</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('晋中')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jinzhong </td><td align="right">晋中</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('酒泉')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jiuquan </td><td align="right">酒泉</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('鸡西')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jixi </td><td align="right">鸡西</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('嘉峪关')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>jiayuguan </td><td align="right">嘉峪关</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>K</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('昆明')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>kunming </td><td align="right">昆明</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('克拉玛依')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>kelamayi </td><td align="right">克拉玛依</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('开封')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>kaifeng </td><td align="right">开封</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('喀什地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>kashi </td><td align="right">喀什地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('克孜勒苏州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>kezilesu </td><td align="right">克孜勒苏州</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>L</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('廊坊')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>langfang </td><td align="right">廊坊</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('乐山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>leshan </td><td align="right">乐山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('柳州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>liuzhou </td><td align="right">柳州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('洛阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>luoyang </td><td align="right">洛阳</td>
</tr><tr align='left' class="supermap_item" onmouseDown="setcityvalue('泸州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>luzhou </td><td align="right">泸州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('泸州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>liaoyang </td><td align="right">辽阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('辽源')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>liaoyuan </td><td align="right">辽源</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('陇南')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>longnan </td><td align="right">陇南</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('连云港')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lianyungang </td><td align="right">连云港</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('聊城')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>liaocheng </td><td align="right">聊城</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('临汾')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>linfen </td><td align="right">临汾</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('临沂')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>linxi </td><td align="right">临沂</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('丽水')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lishui </td><td align="right">丽水</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('六安')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>liuan </td><td align="right">六安</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('兰州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lanzhou </td><td align="right">兰州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('吕梁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lvliang </td><td align="right">吕梁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('娄底')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>loudi </td><td align="right">娄底</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('拉萨')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lasa </td><td align="right">拉萨</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('来宾')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>laibin </td><td align="right">来宾</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('漯河')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>leihe </td><td align="right">漯河</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('莱芜')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>laiwu </td><td align="right">莱芜</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('龙岩')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>longyan </td><td align="right">龙岩</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('临夏回族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>linxiahuizu </td><td align="right">临夏回族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('凉山彝族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>liangshanyizu </td><td align="right">凉山彝族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('六盘水')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>liupanshui </td><td align="right">六盘水</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('乐东黎族自治县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>ledonglizu </td><td align="right">乐东黎族自治县</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('临高县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lingaoxian </td><td align="right">临高县</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('陵水黎族自治县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lingshuilizu </td><td align="right">陵水黎族自治县</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('林芝地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>linzhi </td><td align="right">林芝地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('丽江')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lijiang </td><td align="right">丽江</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('临沧地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>lincang </td><td align="right">临沧地区</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>M</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('茂名')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>maoming </td><td align="right">茂名</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('梅州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>meizhou </td><td align="right">梅州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('眉山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>meishan </td><td align="right">眉山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('绵阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>mianyang </td><td align="right">绵阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('马鞍山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>maanshan </td><td align="right">马鞍山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('牡丹江')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>mudanjiang </td><td align="right">牡丹江</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>N</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('南京')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nanjing </td><td align="right">南京</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('南通')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nantong </td><td align="right">南通</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宁德')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>ningde </td><td align="right">宁德</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宁波')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>ningbo </td><td align="right">宁波</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('南宁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nanning </td><td align="right">南宁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('南昌')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nanchang </td><td align="right">南昌</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('内江')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>neijiang </td><td align="right">内江</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('南充')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nanchong </td><td align="right">南充</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('南平')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nanping </td><td align="right">南平</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('南阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nanyang </td><td align="right">南阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('南沙群岛')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nanshaqundao </td><td align="right">南沙群岛</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('那曲地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>naqu </td><td align="right">那曲地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('怒江州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>nujiangzhou </td><td align="right">怒江州</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>P</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('莆田')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>putian </td><td align="right">莆田</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('盘锦')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>panjin </td><td align="right">盘锦</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('平顶山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>pingdingshan </td><td align="right">平顶山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('平凉')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>pingjing </td><td align="right">平凉</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('濮阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>puyang </td><td align="right">濮阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('攀枝花')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>panzhihua </td><td align="right">攀枝花</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('萍乡')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>pingxiang </td><td align="right">萍乡</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('普洱')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>puer </td><td align="right">普洱</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>Q</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('青岛')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qingdao </td><td align="right">青岛</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('泉州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>quanzhou </td><td align="right">泉州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('秦皇岛')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qinhuangdao </td><td align="right">秦皇岛</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('齐齐哈尔')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qiqihaer </td><td align="right">齐齐哈尔</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('七台河')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qitaihe </td><td align="right">七台河</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('庆阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qingyang </td><td align="right">庆阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('曲靖')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qujing </td><td align="right">曲靖</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('清远')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qingyuan </td><td align="right">清远</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('潜江')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qianjiang </td><td align="right">潜江</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('钦州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qinzhou </td><td align="right">钦州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('黔东南苗族侗族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qiandongnan </td><td align="right">黔东南苗族侗族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('黔南布依族苗族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qiannan </td><td align="right">黔南布依族苗族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('黔西南州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qianxinan </td><td align="right">黔西南州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('琼海')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qionghai </td><td align="right">琼海</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('琼中黎族苗族自治县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>qiongzhong </td><td align="right">琼中黎族苗族自治县</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>R</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('日照')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>rizhao </td><td align="right">日照</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('日喀则地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>rikaze </td><td align="right">日喀则地区</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>S</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('上海')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shanghai </td><td align="right">上海</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('韶关')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shaoguan </td><td align="right">韶关</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('汕头')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shantou </td><td align="right">汕头</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('汕尾')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shanwei </td><td align="right">汕尾</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('深圳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shenzhen </td><td align="right">深圳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('三亚')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>sanya </td><td align="right">三亚</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('四平')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>siping </td><td align="right">四平</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('苏州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>suzhou </td><td align="right">苏州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('十堰')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shiyan </td><td align="right">十堰</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('沈阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shenyang </td><td align="right">沈阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('绍兴')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shaoxing </td><td align="right">绍兴</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('邵阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shaoyang </td><td align="right">邵阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('石家庄')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shijiazhuang </td><td align="right">石家庄</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('绥化')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>suihua </td><td align="right">绥化</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('随州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>suizhou </td><td align="right">随州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('三明')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>sanming </td><td align="right">三明</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('三门峡')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>sanmenxia </td><td align="right">三门峡</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('上饶')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shangrao </td><td align="right">上饶</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('双鸭山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shuangyashan </td><td align="right">双鸭山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('商丘')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shangqiu </td><td align="right">商丘</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('商洛')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shangluo </td><td align="right">商洛</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宿州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>suzhou </td><td align="right">宿州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宿迁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>suqian </td><td align="right">宿迁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('山南地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shanan </td><td align="right">山南地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('朔州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shuozhou </td><td align="right">朔州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('松原')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>songyuan </td><td align="right">松原</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('石嘴山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shizuishan </td><td align="right">石嘴山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('石河子')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shihezi </td><td align="right">石河子</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('遂宁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>suining </td><td align="right">遂宁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('神农架林区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>shennongjia </td><td align="right">神农架林区</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>T</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('天津')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tianjin </td><td align="right">天津</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('太原')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>taiyuan </td><td align="right">太原</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('唐山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tangshan </td><td align="right">唐山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('台州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>taizhou </td><td align="right">台州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('泰州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>taizhou </td><td align="right">泰州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('泰安')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>taian </td><td align="right">泰安</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('吐鲁番地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tulufan </td><td align="right">吐鲁番地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('塔城地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tacheng </td><td align="right">塔城地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('天水')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tianshui </td><td align="right">天水</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('天门')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tianmen </td><td align="right">天门</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('铁岭')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tieling </td><td align="right">铁岭</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('通化')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tonghua </td><td align="right">通化</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('通辽')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tongliao </td><td align="right">通辽</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('铜仁地区')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tongren </td><td align="right">铜仁地区</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('铜陵')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tongling </td><td align="right">铜陵</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('铜川')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tongchuan </td><td align="right">铜川</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('屯昌县')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>tunchangxian </td><td align="right">屯昌县</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>W</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('芜湖')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wuhu </td><td align="right">芜湖</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('无锡')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wuxi </td><td align="right">无锡</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('温州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wenzhou </td><td align="right">温州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('武汉')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wuhan </td><td align="right">武汉</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('潍坊')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>weifang </td><td align="right">潍坊</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('威海')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>weihai </td><td align="right">威海</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('乌兰察布')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wulanchabu </td><td align="right">乌兰察布</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('乌海')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wuhai </td><td align="right">乌海</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('吴忠')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wuzhong </td><td align="right">吴忠</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('梧州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wuzhou </td><td align="right">梧州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('乌鲁木齐')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wulumuqi </td><td align="right">乌鲁木齐</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('武威')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wuwei </td><td align="right">武威</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('渭南')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>weinan </td><td align="right">渭南</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('万宁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wanning </td><td align="right">万宁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('文昌')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wenchang </td><td align="right">文昌</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('五指山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wuzhishan </td><td align="right">五指山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('文山州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>wenshanzhou </td><td align="right">文山州</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>X</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('厦门')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xiamen </td><td align="right">厦门</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('咸阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xianyang </td><td align="right">咸阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('西安')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xian </td><td align="right">西安</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('湘潭')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xiangtan </td><td align="right">湘潭</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('襄樊')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xiangfan </td><td align="right">襄樊</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('许昌')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xuchang </td><td align="right">许昌</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('新乡')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xinxiang </td><td align="right">新乡</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('徐州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xuzhou </td><td align="right">徐州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('仙桃')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xiantao </td><td align="right">仙桃</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('信阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xinyang </td><td align="right">信阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('孝感')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xiaogan </td><td align="right">孝感</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('忻州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xinzhou </td><td align="right">忻州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('新余')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xinyu </td><td align="right">新余</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('西宁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xining </td><td align="right">西宁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('咸宁')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xianning </td><td align="right">咸宁</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宣城')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xuancheng </td><td align="right">宣城</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('邢台')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xingtai </td><td align="right">邢台</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('兴安盟')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xinanmeng </td><td align="right">兴安盟</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('西双版纳傣族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xishuangbanna </td><td align="right">西双版纳傣族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('湘西土家族苗族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xiangxi </td><td align="right">湘西土家族苗族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('西沙群岛')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xishaqundao </td><td align="right">西沙群岛</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('锡林郭勒盟')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xilinguolinguole </td><td align="right">锡林郭勒盟</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('香港')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>xianggang </td><td align="right">香港</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>Y</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宜宾')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yibin </td><td align="right">宜宾</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('营口')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yingkou </td><td align="right">营口</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('阳江')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yangjiang </td><td align="right">阳江</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('延安')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yanan </td><td align="right">延安</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('岳阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yueyang </td><td align="right">岳阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('玉溪')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yuxi </td><td align="right">玉溪</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宜昌')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yichang </td><td align="right">宜昌</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('盐城')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yancheng </td><td align="right">盐城</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('烟台')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yantai </td><td align="right">烟台</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('扬州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yangzhou </td><td align="right">扬州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('伊春')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yichun </td><td align="right">伊春</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('榆林')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yulin </td><td align="right">榆林</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('玉林')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yulin </td><td align="right">玉林</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('益阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yiyang </td><td align="right">益阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('云浮')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yunfu </td><td align="right">云浮</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('运城')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yuncheng </td><td align="right">运城</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('宜春')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yichun </td><td align="right">宜春</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('鹰潭')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yingtan </td><td align="right">鹰潭</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('永州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yongzhou </td><td align="right">永州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('银川')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yinchuan </td><td align="right">银川</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('阳泉')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yangquan </td><td align="right">阳泉</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('雅安')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yaan </td><td align="right">雅安</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('伊犁哈萨克自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yilihasake </td><td align="right">伊犁哈萨克自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('延边朝鲜族自治州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yanbianchaoxianzu </td><td align="right">延边朝鲜族自治州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('玉树州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>yushuzhou </td><td align="right">玉树州</td></tr>
<tr><td colspan="2" style='border-bottom:1px #7f9db9 dashed;font-size:12px;color:#7f9db9'><b>Z</b></td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('漳州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhangzhou </td><td align="right">漳州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('珠海')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhuhai </td><td align="right">珠海</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('湛江')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhanjiang </td><td align="right">湛江</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('肇庆')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhaoqing </td><td align="right">肇庆</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('中山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhongshan</td><td align="right">中山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('株洲')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhuzhou </td><td align="right">株洲</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('郑州')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhengzhou </td><td align="right">郑州</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('舟山')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhoushan </td><td align="right">舟山</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('镇江')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhenjiang </td><td align="right">镇江</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('自贡')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zigong </td><td align="right">自贡</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('淄博')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zibo </td><td align="right">淄博</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('资阳')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>ziyang </td><td align="right">资阳</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('张家界')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhangjiajie </td><td align="right">张家界</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('张家口')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhangjiakou </td><td align="right">张家口</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('遵义')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zunyi </td><td align="right">遵义</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('驻马店')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhumadian </td><td align="right">驻马店</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('中卫')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhongwei </td><td align="right">中卫</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('周口')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhoukou </td><td align="right">周口</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('张掖')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhangye </td><td align="right">张掖</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('昭通')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhaotong </td><td align="right">昭通</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('枣庄')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zaozhuang </td><td align="right">枣庄</td></tr>
<tr align='left' class="supermap_item" onmouseDown="setcityvalue('中沙群岛的岛礁及其海域')" onmouseOver="this.className='supermap_item1'" onmouseOut="this.className='supermap_item'"><td>zhongshaqundao </td><td align="right">中沙群岛的岛礁及其海域</td></tr>
</table>
</div>
</div>
<!--所有城市end-->
</div>
<!--搜索div end-->

</body>
</html>