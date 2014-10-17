function $() {
	var elements = new Array();
	for ( var i = 0; i < arguments.length; i++) {
		var element = arguments[i];
		if (typeof element == 'string')
			element = document.getElementById(element);
		if (arguments.length == 1)
			return element;
		elements.push(element);
	}
	return elements;
}
function list_show() {
	var imgsrc = document.getElementById('topshow').src;
	imgsrc = imgsrc.split("to_");
	if (imgsrc[1] == "bottom1.jpg") {
		$("topshow").src = imgsrc[0] + "to_top1.jpg";
		$("topshow").onmouseout = function() {
			this.src = '../images/pane/to_top.jpg';
		};
		$("topshow").onmouseover = function() {
			this.src = '../images/pane/to_top1.jpg';
		};
		$("listdiv").style.display = "block";
		$("supermap_bg").style.display = "block";
	} else {
		$("topshow").src = imgsrc[0] + "to_bottom1.jpg";
		$("topshow").onmouseout = function() {
			this.src = '../images/pane/to_bottom.jpg';
		};
		$("topshow").onmouseover = function() {
			this.src = '../images/pane/to_bottom1.jpg';
		};
		$("listdiv").style.display = "none";
		$("supermap_bg").style.display = "none";
	}
}
function showLS() {
	// QN_removeAllOverlays();
	citylist_hide();
	$('li_ls').className = "supermap_cur";
	$('li_rs').className = "";
	$('list_ls').style.display = "block";
	$('list_rs').style.display = "none";
	$('list_rs').style.display = "none";
	$('M_drivechooseDIV').style.display = "none";
	$('M_drive_list').style.display = "none";
}
function showRS() {
	// QN_removeAllOverlays();
	citylist_hide();
	$('li_rs').className = "supermap_cur";
	$('li_ls').className = "";
	$('list_rs').style.display = "block";
	$('list_ls').style.display = "none";
	$('M_drivechooseDIV').style.display = "none";
	$('M_search_list').style.display = "none";
}
function citylist_show(m_left, m_top) {
	citylist_hide();
	$('M_citysmall').style.left = m_left;
	$('M_citysmall').style.top = m_top;
	$('M_cityall').style.left = m_left;
	$('M_cityall').style.top = m_top;
	$('M_citysmall').style.display = "block";
}
function citylist_hide() {
	$('M_citysmall').style.display = "none";
	$('M_cityall').style.display = "none";
}
function cityall_show() {
	$('M_citysmall').style.display = "none";
	$('M_cityall').style.display = "block";
}
function cityall_hide() {
	$('M_citysmall').style.display = "block";
	$('M_cityall').style.display = "none";
}
function setValue(v, d) {
	$(d).value = v;
}
function setcityvalue(c) {
	// ��ͼ 40px56px
	// �ݳ�1 28px57px
	// �ݳ�2 28px81px
	var temp = $('M_citysmall').style.left + $('M_citysmall').style.top;
	switch (temp) {
	case "40px56px":// ��ͼ 40px103px
		$('local_cityname').value = c;
		break;
	case "28px57px":// �ݳ�1 28px103px
		$('QN_driveLine_citynameS').value = c;
		break;
	case "28px81px":// �ݳ�2 28px129px
		$('QN_driveLine_citynameE').value = c;
		break;
	default:
		return;
	}
	citylist_hide();
}
/* �������� */
function LSearch() {
	this.search_t = "s";
	this.cityname = "";
	this.keyword = "";
	this.center = "";
	this.number = 10;
	this.batch = 1;
	this.range = 3000;
	this.page_now = 1;
	this.page_allnum = "";
}
var LS = new LSearch();
var sis, sp;
function LS_localSearch_submit() {
	LS.cityname = Trim($("local_cityname").value);
	LS.keyword = Trim($("local_keyword").value);
	var che = "";
	var i = 1;
	if (LS.cityname == "") {
		che += i + "����ѡ����Ҫ��ѯ�ĳ���\n";
		i = i + 1;
	}
	if (LS.keyword == "" || LS.keyword == "����ؼ��֣����У�����վ��") {
		che += i + "��������ؼ���";
		i = i + 1;
	}
	if (i == 1) {
		LS_local_search();
	} else {
		alert(che);
	}
}
function LS_local_search() {
	LS.page_now = 1;
	sis = new parent.map.MLocalSearch();
	sp = new parent.map.MLocalSearchOptions();
	sis.setCallbackFunction(LS_localSearch_CallBack);
	sp.recordsPerPage = LS.number;
	sp.pageNum = LS.batch;
	keyword = encodeURI(LS.keyword);
	cityname = encodeURI(LS.cityname);
	
	sis.poiSearchByKeywords(LS.keyword, cityname, sp);
}
function LS_localSearch_CallBack(data) {
	try {
		var Mmarker = new Array();
		var rs = data;
		switch (rs.message) {
		case 'ok':
			var allcount = rs.count;
			if (allcount == 0) {
				$('M_search_list').style.display = "block";
				$("LS_resultList").innerHTML = "<div class=\"supermap_wrongpic\"></div><div class=\"supermap_wrongtip\"><strong>���飺</strong><br />1.��ȷ�������ִ�ƴд��ȷ��<br />2.���Բ�ͬ�Ĺؼ��֡�<br />3.���Ը����Ĺؼ��֡�</div><div class=\"supermap_wronglink\"><br /></div>";
			} else {
				QN_removeAllOverlays();
				var TipContent = "", result_content = "";
				for ( var i = 0; i < rs.poilist.length; i++) {
					var ars = rs.poilist[i];
					var poiName = ars.name;
					var poiId = ars.pguid;
					var poiX = ars.x;
					var poiY = ars.y;
					var poiAddress = (!ars.address ? "" : ars.address);
					var poiTel = (!ars.tel ? "����" : ars.tel);
					var poiAddress_link = (poiAddress == "" ? "����" : poiAddress);
					var poiTel_link = (poiTel == "" ? "����" : poiTel);
					TipContent = "<br>��ַ��" + poiAddress_link + "<br>";
					TipContent += "�绰��" + poiTel_link + " <br>";
					if ('undefined' != typeof parent.map.mapObj) {
						;
						var markerOption = new parent.map.MMarkerOptions();
						if (ars.match == "10") {
							markerOption.imageUrl = "http://www.mapabc.com/images/num/"
									+ (i + 1) + "_10.gif?070701";
						} else {
							markerOption.imageUrl = "http://www.mapabc.com/images/num/"
									+ (i + 1) + ".gif?070701";
						}
						markerOption.isDraggable = false;// �Ƿ�����϶�
						// markerOption.imageAlign=5;//����ͼƬê�������ͼƬ��λ��
						var tipOption = new parent.map.MTipOptions();
						tipOption.title = (i + 1) + ". " + poiName;
						tipOption.content = TipContent;// tip����
						tipOption.hasShadow = true;
						tipOption.borderStyle.thickness = 2;
						tipOption.borderStyle.color = 0xff230b;
						tipOption.borderStyle.alpha = 1;
						tipOption.titleFontStyle.name = "Arial";
						tipOption.titleFontStyle.size = 12;
						tipOption.titleFontStyle.color = 0x000000;
						tipOption.titleFontStyle.bold = true;
						tipOption.contentFontStyle.name = "Arial";
						tipOption.contentFontStyle.size = 13;
						tipOption.contentFontStyle.color = 0x000000;
						tipOption.contentFontStyle.bold = false;
						tipOption.fillStyle.color = 0xFFFFFF; // ���ɫ
						tipOption.fillStyle.alpha = 1;
						tipOption.titleFillStyle.color = 0xff230b;
						tipOption.titleFillStyle.alpha = 1;

						markerOption.tipOption = tipOption;
						markerOption.canShowTip = true;
						markerOption.hasShadow = true;
						var ll = new parent.map.MLngLat(poiX, poiY);
						Mmarker[i] = new parent.map.MMarker(ll, markerOption);
						Mmarker[i].id = (i + 1);
					}
					var poiMatch = "";
					if (ars.match == "10") {
						poiMatch = "<img src=\"http://www.mapabc.com/qnmap/images/supermap/supermap_num"
								+ (i + 1) + ".gif\" title=\"��ȷλ��\"/>";
					} else {
						poiMatch = "<img src=\"http://www.mapabc.com/qnmap/images/supermap/supermap_num_m"
								+ (i + 1) + ".gif\" title=\"����λ��\"/>";
					}

					result_content += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-bottom:1px dotted #ccc;\"  onclick=\"parent.map.mapObj.setCenter(new parent.map.MLngLat('"
							+ poiX
							+ "','"
							+ poiY
							+ "'));parent.map.mapObj.openOverlayTip('"
							+ (i + 1)
							+ "');\" id=\"div_"
							+ (i + 1)
							+ "\" onmouseover=\"this.style.backgroundColor='#eeeeee'\" onmouseout=\"this.style.backgroundColor='#ffffff'\"><tr><td width=\"17%\" align=\"center\" valign=\"top\">"
							+ poiMatch
							+ "</td><td width=\"83%\"><strong>"
							+ poiName
							+ "</strong></td></tr><tr><td>&nbsp;</td><td>��ַ��"
							+ poiAddress_link
							+ "</td></tr><tr><td>&nbsp;</td><td>�绰��"
							+ poiTel_link + "</td></tr></table>";
				}
				//parent.map.mapObj.addOverlays(Mmarker, true);
				for(var i=1;i<Mmarker.length;i++){
					parent.map.mapObj.addOverlay(Mmarker[i], false);
				}
				parent.map.mapObj.addOverlay(Mmarker[0], true);
				
				LS.page_allnum = Math.floor((allcount + LS.number - 1)
						/ LS.number);// ҳ��
				$('M_search_list').style.display = "block";
				$('LS_resultList').innerHTML = "<div class=\"supermap_where\">�� <strong>"
						+ LS.cityname
						+ "</strong> ���� <strong>"
						+ LS.keyword
						+ "</strong></div>" + result_content;

				LS_ShowoPage("��ҳ", "��ҳ", "<font color='red'>", "</font>",
						"[<font color='red'>", "</font>]", allcount, LS.number,
						4, LS.page_now, "LS_resultList_page");
			}

			break;
		default:
			$('M_search_list').style.display = "block";
			QN_resultError("LS_resultList");
		}
	} catch (e) {
	}
}
function active_div(n) {
	try {
		for ( var i = 1; i < 11; ++i) {
			document.getElementById("div_" + i).onmouseout = function() {
				this.style.backgroundColor = '#ffffff';
			};
			document.getElementById("div_" + i).style.backgroundColor = "#ffffff";
		}
	} catch (e) {
	}
	try {
		if (n <= 10) {
			document.getElementById("div_" + n).onmouseout = "";
			document.getElementById("div_" + n).style.backgroundColor = "#eeeeee";
		}
	} catch (e) {
	}
}
function LS_ShowoPage(PrevFont, NextFont, PageNumFont1, PageNumFont2,
		PageNumFont3, PageNumFont4, RecCount, RecPerPage, PageNum, CurrPage,
		DivId) {
	if (RecCount % RecPerPage == 0) {
		PageCount = RecCount / RecPerPage;
	} else {
		PageCount = (parseInt(RecCount / RecPerPage) + 1);
	}
	if (PageCount <= 1) {
		PageCount = 1;
	}

	prevpage = parseInt(CurrPage - 1);
	if (prevpage < 1) {
		prevpage = 1;
	}
	nextpage = parseInt(CurrPage + 1);
	if (nextpage > PageCount) {
		nextpage = PageCount;
	}

	if (CurrPage <= 1 && PageCount == 1) {

		CurrPage = 1;
		PrevPageUrl = "&nbsp;" + PrevFont + "&nbsp;";
		NextPageUrl = "&nbsp;" + NextFont;
	} else if (CurrPage <= 1) {
		CurrPage = 1;
		PrevPageUrl = "&nbsp;" + PrevFont + "&nbsp;";
		NextPageUrl = "&nbsp;<A href=\"javascript:nextPageNum();\">" + NextFont
				+ "</A>";
	} else if (CurrPage >= PageCount) {
		CurrPage = PageCount;
		PrevPageUrl = "&nbsp;<A href=\"javascript:lastPageNum();\">" + PrevFont
				+ "</A>&nbsp;";
		NextPageUrl = "&nbsp;" + NextFont;
	} else {
		CurrPage = CurrPage;
		PrevPageUrl = "&nbsp;<A href=\"javascript:lastPageNum();\">" + PrevFont
				+ "</A>&nbsp;";
		NextPageUrl = "&nbsp;<A href=\"javascript:nextPageNum();\">" + NextFont
				+ "</A>";
	}
	PageStart = CurrPage - PageNum <= 1 ? 1
			: (CurrPage - PageNum - (CurrPage > PageCount - PageNum ? PageNum
					+ CurrPage - PageCount : 0));

	PageEnd = CurrPage + PageNum >= PageCount ? PageCount
			: (CurrPage + PageNum + (CurrPage - 1 < PageNum ? PageNum
					- CurrPage + 1 : 0));
	if (PageStart < 1)
		PageStart = 1;
	if (PageNum * 2 + 1 > PageCount)
		PageEnd = PageCount;
	$(DivId).innerHTML = PrevPageUrl;// alert(PageEnd);
	for (i = PageStart; i <= PageEnd; i++) {//
		if (i == CurrPage) {
			$(DivId).innerHTML += "&nbsp;" + PageNumFont3 + i + PageNumFont4
					+ "&nbsp;";
		} else {
			$(DivId).innerHTML += "&nbsp;<A href=\"javascript:pageNum(" + i
					+ ");\">" + PageNumFont1 + i + PageNumFont2 + "</A>&nbsp;";
		}
	}
	$(DivId).innerHTML += NextPageUrl;
}
function nextPageNum() {
	if (LS.page_now < LS.page_allnum) {
		pageNum(LS.page_now + 1);
	}
}
function lastPageNum() {
	if (LS.page_now > 0) {
		pageNum(LS.page_now - 1);
	}
}

function pageNum(num) {// ��ҳ
	LS.page_now = num;
	sp.pageNum = num;
	if (LS.search_t == "a") {
		sis.poiSearchByKeywords(LS.keyword, encodeURI(LS.cityname), sp);
	}
	if (LS.search_t == "s") {
		sis.poiSearchByKeywords(LS.keyword, encodeURI(LS.cityname), sp);
	}
}
/* �ݳ� */
function RSearch() {
	this.driveSType = "rs";
	this.route_way = "0";
	this.point_id = "";
	this.start_x = "";
	this.start_y = "";
	this.start_name = "";
	this.start_address = "";
	this.start_tel = "";
	this.start_pid = "";
	this.start_citycode = "";
	this.start_cityname = "";
	this.start_detailLink = "";
	this.end_x = "";
	this.end_y = "";
	this.end_name = "";
	this.end_address = "";
	this.end_tel = "";
	this.end_pid = "";
	this.end_citycode = "";
	this.end_cityname = "";
	this.end_detailLink = "";
	this.route_segment;
}
var RS = new RSearch();
function QN_driveLine_submit() {
	RS.start_cityname = $('QN_driveLine_citynameS').value;
	RS.end_cityname = $('QN_driveLine_citynameE').value;
	RS.start_name = $('QN_driveLine_startname').value;
	RS.end_name = $('QN_driveLine_endname').value;
	var che = "";
	var i = 1;
	if (RS.start_name == "���������" || RS.start_name == "") {
		che += i + "�����������\n";
		i = i + 1;
	}
	if (RS.end_name == "" || RS.end_name == "�������յ�") {
		che += i + "���������յ�\n";
		i = i + 1;
	}
	if (RS.start_cityname == "") {
		che += i + "����ѡ��������\n";
		i = i + 1;
	}
	if (RS.end_cityname == "") {
		che += i + "����ѡ���յ����\n";
		i = i + 1;
	}
	if (i == 1) {
		RS.driveSType = "rs";
		RS_driveLine_Startsearch(RS.start_cityname, RS.start_name);
	} else {
		alert(che);
	}
}
function RS_driveLine_Startsearch(c, s) {
	QN_removeAllOverlays();
	sis = new parent.map.MLocalSearch();
	sp = new parent.map.MLocalSearchOptions();
	sis.setCallbackFunction(QN_driveLineStartsearch_CallBack);
	sp.recordsPerPage = 10;
	sp.pageNum = 1;
	//s = encodeURI(s);
	c = encodeURI(c);
	//alert(s);
	//alert(c);
	sis.poiSearchByKeywords(s, c, sp);
}
function QN_driveLineStartsearch_CallBack(data) {
	if (data.message == "ok") {
		if (data.count != 0) {
			var resultContent = "";
			for ( var i = 0; i < data.poilist.length; i++) {
				var poi_detaillink = "";
				if (data.poilist[i].srctype == "basepoi") {
					poi_detaillink = "http://www.mapabc.com/detail/"
							+ Trim(data.poilist[0].citycode) + "/"
							+ data.poilist[0].pguid + ".html";
				}
				if (data.poilist[i].srctype == "enpoi") {
					poi_detaillink = data.poilist[i].dn;
				}
				resultContent += "<li id=\""
						+ RS.driveSType
						+ "_"
						+ i
						+ "\" style=\"cursor:pointer\" onclick=\"QN_driveLine_addpoi('0','"
						+ data.poilist[i].pguid + "','" + data.poilist[i].x
						+ "','" + data.poilist[i].y + "','"
						+ data.poilist[i].citycode + "',' "
						+ data.poilist[i].type + "','"
						+ data.poilist[i].address + "','" + data.poilist[i].tel
						+ "','" + data.poilist[i].name + "','" + RS.driveSType
						+ "_" + i + "','" + data.poilist.length + "','"
						+ RS.driveSType + "','" + poi_detaillink + "');\">"
						+ data.poilist[i].name + "</li>";
			}
			$('M_drive_list').style.display = "none";
			$('M_drivechooseDIV').style.display = "block";
			if (RS.driveSType == "rs") {
				$('M_start_end_Drivecontent').innerHTML = "<strong>��㣺</strong>"
						+ RS.start_name + "<br />";
			}
			if (RS.driveSType == "re") {
				$('M_start_end_Drivecontent').innerHTML = $('M_start_end_Drivecontent').innerHTML
						+ "<strong>�յ㣺</strong>" + RS.end_name;
			}
			$('M_Drivestartlist_' + RS.driveSType).innerHTML = resultContent;
			$("" + RS.driveSType + "_0").className = "supermap_itemarea";
			QN_driveLine_addpoi('1', data.poilist[0].pguid, data.poilist[0].x,
					data.poilist[0].y, Trim(data.poilist[0].citycode),
					data.poilist[0].type, data.poilist[0].address,
					data.poilist[0].tel, data.poilist[0].name, RS.driveSType
							+ "_0", data.poilist.length, RS.driveSType,
					"http://www.mapabc.com/detail/"
							+ Trim(data.poilist[0].citycode) + "/"
							+ data.poilist[0].pguid + ".html");
		} else {
			$('M_drive_list').style.display = "none";
			$('M_drivechooseDIV').style.display = "block";
			if (RS.driveSType == "rs") {
				$('M_start_end_Drivecontent').innerHTML = "<strong>��㣺</strong>"
						+ RS.start_name + "<br />";
			}
			if (RS.driveSType == "re") {
				$('M_start_end_Drivecontent').innerHTML = $('M_start_end_Drivecontent').innerHTML
						+ "<strong>�յ㣺</strong>" + RS.end_name;
			}
			$('M_Drivestartlist_' + RS.driveSType).innerHTML = "���޽����";
		}
		QN_driveLine_Endsearch();
	} else {
		$('M_drive_list').style.display = "none";
		$('M_drivechooseDIV').style.display = "block";
		if (RS.driveSType == "rs") {
			$('M_start_end_Drivecontent').innerHTML = "<strong>��㣺</strong>"
					+ RS.start_name + "<br />";
		}
		if (RS.driveSType == "re") {
			$('M_start_end_Drivecontent').innerHTML = $('M_start_end_Drivecontent').innerHTML
					+ "<strong>�յ㣺</strong>" + RS.end_name;
		}
		$('M_Drivestartlist_' + RS.driveSType).innerHTML = "�������쳣�������³��ԣ�";
	}
}
function QN_driveLine_Endsearch() {
	if (RS.driveSType != "re") {
		RS.driveSType = "re";
		RS_driveLine_Startsearch(RS.end_cityname, RS.end_name);
	} else {
		// parent.map.mapObj.addOverlays(points,true) ;
		points = new Array();
	}
}
function QN_driveChange_chooseBack() {
	$('M_drivechooseDIV').style.display = "none";
}
var qnroute_sis;
var startPoint ;
var endPoint ;
function QN_driveLine_search() {
	if (RS.start_x == "" || RS.start_x == "" || RS.end_x == ""
			|| RS.end_y == "") {
		alert("�������յ����޽�����޷���ѯ��������ѡ�������յ㣡");
	} else {
		sis = new parent.map.MRoutSearch();
		qnroute_sis = new parent.map.MRoutSearchOptions();
		qnroute_sis.routeType = RS.route_way;
		sis.setCallbackFunction(QN_driveLineSearch_CallBack);
		startPoint = new parent.map.MLngLat(RS.start_x, RS.start_y);
		endPoint = new parent.map.MLngLat(RS.end_x, RS.end_y);
		sis.routSearchByStartXYAndEndXY("drive", startPoint, endPoint, "all",
				qnroute_sis);
	}
}
function QN_driveLineSearch_CallBack(data) {
	if (data.message == "ok") {
		var route_count = data.count;// ���ؼݳ�·��(·������),����Ƿ�Ϊ"0",�����"0"�޷��ؽ��.
		if (route_count == 0) {
			$('M_drivechooseDIV').style.display = "none";
			$('M_drive_list').style.display = "block";
			$('QN_driveresultList').innerHTML = "<div class=\"supermap_wrongpic\"></div><div class=\"supermap_wrongtip\"><strong>���飺</strong><br />1.��ȷ�������ִ�ƴд��ȷ��<br />2.���Բ�ͬ�Ĺؼ��֡�<br />3.���Ը����Ĺؼ��֡�</div><div class=\"supermap_wronglink\"><br /><a href=\"javascript:qn_menu('4')\">���²�ѯ</a></div>";
		} else {
			// ��������
			var route_sms_text = "";
			route_sms_text += "��㣺" + RS.start_name;
			var route_text = "";
			var road_length = 0;
			var route_content = new Array();
			RS.route_segment = new Array();
			for ( var i = 0; i < route_count; i++) {
				RS.route_segment[i] = data.segmengList[i].coor;// ÿһ��·�ߵ�XY
				road_length += parseInt(data.segmengList[i].roadLength);// ÿһ��·�ߵ����

				if (i == 0) {
					route_text += "<tr id=\"tr_"
							+ i
							+ "\" onclick=\"QN_driveLineDrawFoldline('"
							+ i
							+ "','"
							+ route_count
							+ "')\"><td colspan=\"2\" class=\"supermap_tbbg\" onmouseover=\"this.className='supermap_tbbg1'\" onmouseout=\"this.className='supermap_tbbg'\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"73%\">&nbsp;&nbsp;"
							+ (i + 1)
							+ "����<strong>"
							+ data.segmengList[i].roadName
							+ "</strong>��<strong>"
							+ data.segmengList[i].direction
							+ "</strong>��ʻ</td><td width=\"27%\" align=\"center\">"
							+ QN_getdistance(data.segmengList[i].roadLength)
							+ "</td><td>&nbsp;</td></tr></table></td></tr>";
					route_sms_text += (i + 1) + "����"
							+ data.segmengList[i].roadName + "��"
							+ data.segmengList[i].direction + "��ʻ"
							+ QN_getdistance(data.segmengList[i].roadLength);
				} else {
					route_text += "<tr id=\"tr_"
							+ i
							+ "\" onclick=\"QN_driveLineDrawFoldline('"
							+ i
							+ "','"
							+ route_count
							+ "')\"><td colspan=\"2\" class=\"supermap_tbbg\" onmouseover=\"this.className='supermap_tbbg1'\" onmouseout=\"this.className='supermap_tbbg'\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td width=\"73%\">&nbsp;&nbsp;"
							+ (i + 1)
							+ "��"
							+ data.segmengList[i - 1].action
							+ "<strong>����"
							+ data.segmengList[i].roadName
							+ "</strong>��<strong>"
							+ data.segmengList[i].direction
							+ "</strong>��ʻ</td><td width=\"27%\" align=\"center\">"
							+ QN_getdistance(data.segmengList[i].roadLength)
							+ "</td><td>&nbsp;</td></tr></table></td></tr>";
					route_sms_text += (i + 1) + "��"
							+ data.segmengList[i - 1].action + "����"
							+ data.segmengList[i].roadName + "��"
							+ data.segmengList[i].direction + "��ʻ"
							+ QN_getdistance(data.segmengList[i].roadLength);
				}
			}
			route_sms_text += "�����յ㣺" + RS.end_name;
			route_content
					.push("<strong>����㣺</strong>"
							+ RS.start_name
							+ "<br /><strong>���յ㣺</strong>"
							+ RS.end_name
							+ "<br /><input name=\"QN_routeModeSearch\" id=\"QN_routeModeSearch\" type=\"radio\" value=\"0\" class=\"supermap_one1\" onclick=\"QN_selectMode_route('0')\"/>Ĭ��ģʽ&nbsp;&nbsp;<input name=\"QN_routeModeSearch\" id=\"QN_routeModeSearch\" type=\"radio\" value=\"1\" class=\"supermap_one1\" onclick=\"QN_selectMode_route('1')\"/>��������&nbsp;&nbsp;<input name=\"QN_routeModeSearch\" id=\"QN_routeModeSearch\" type=\"radio\" value=\"2\" class=\"supermap_one1\" onclick=\"QN_selectMode_route('2')\"/>��������<div class=\"supermap_route_title\">�ݳ�·��</div><table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"0\"><tr><td width=\"73%\" bgcolor=\"#e1e1e1\">��·��</td><td width=\"27%\" align=\"center\" bgcolor=\"#e1e1e1\">���</td></tr><tr>    <td colspan=\"2\" class=\"supermap_tbbg\" onmouseover=\"this.className='supermap_tbbg1'\" onmouseout=\"this.className='supermap_tbbg'\">&nbsp;&nbsp;��㣺"
							+ RS.start_name
							+ "</td></tr>"
							+ route_text
							+ "<td colspan=\"2\" class=\"supermap_tbbg\" onmouseover=\"this.className='supermap_tbbg1'\" onmouseout=\"this.className='supermap_tbbg'\">&nbsp;&nbsp;�յ㣺"
							+ RS.end_name + "</td></tr></table>");
			$('M_drivechooseDIV').style.display = "none";
			$('M_drive_list').style.display = "block";
			$('QN_driveresultList').innerHTML = route_content.join("");
			var select_r_m = document.getElementsByName("QN_routeModeSearch").length;
			for ( var i = 0; i < select_r_m; i++) {
				if (RS.route_way == i) {
					document.getElementsByName("QN_routeModeSearch")[i].checked = true;
				}
			}
			var poi_xy_r = data.coors;
			QN_driveLineSearch_drawline(poi_xy_r, RS.start_y, RS.start_x,
					RS.end_y, RS.end_x);// ���ݳ���·
		}
	} else {
		$('M_drivechooseDIV').style.display = "none";
		$('M_drive_list').style.display = "block";
		QN_resultError("QN_driveresultList");
	}
}
function QN_driveLineSearch_drawline(poi_xy_r, startY, startX, endY, endX) {// ���ݳ���·
	try {
		QN_removeAllOverlays();
		var stipc = tipcontent(RS.start_type, RS.start_address, RS.start_tel);
		var etipc = tipcontent(RS.end_type, RS.end_address, RS.end_tel);
		addSE("http://www.mapabc.com/qnmap/images/supermap/qd.png", "  ��㣺"
				+ RS.start_name, stipc, startX, startY, RS.start_pid,
				"0x00BD0A");
		addSE("http://www.mapabc.com/qnmap/images/supermap/zd.png", "  �յ㣺"
				+ RS.end_name, etipc, endX, endY, RS.end_pid, "0xff230b");

		var arrline = new Array();
		var linexy = poi_xy_r.split(',');
		var line_l = (linexy.length - 1) / 2;
		for ( var i = 0; i < line_l; i++) {
			arrline.push(new parent.map.MLngLat(linexy[2 * i], linexy[2 * i + 1]));
		}
		var lineS = new parent.map.MLineStyle();
		lineS.thickness = 3;
		lineS.color = 0xff230b;
		lineS.alpha = 1;
		var lineoption = new parent.map.MLineOptions();
		lineoption.canShowTip = false;
		lineoption.lineStyle = lineS;
		var line = new parent.map.MPolyline(arrline, lineoption);
		parent.map.mapObj.addOverlay(line, true);

		var lnglat = new parent.map.MLngLat(startX, startY);
		var markerOption = new parent.map.MMarkerOptions();
		markerOption.imageUrl = "http://211.137.182.233:8811/fmp/images/arrow.png";// ��ͨ����ͼ��
		markerOption.imageAlign = 5;
		var Mmarker1 = new parent.map.MMarker(lnglat, markerOption);
		Mmarker1.id = "bus1";
		parent.map.mapObj.addOverlay(Mmarker1);
		parent.map.mapObj.markerMoveAlong("bus1", arrline);
		parent.map.mapObj.startMoveAlong('bus1', true);
	} catch (e) {
	}
}
function QN_driveLineDrawFoldline(num, count) {// ����·����������б�.numΪ�ڼ�����·,countȫ����·��.
	try {
		var tr_id = "tr_" + num;
		for ( var i = 0; i < count; i++) {
			var id = "tr_" + i;
			$(id).style.backgroundColor = '#fff';
			$(id).onmouseout = function() {
				this.style.backgroundColor = '#fff';
			};
		}
		$(tr_id).style.backgroundColor = '#efefef';
		$(tr_id).onmouseout = function() {
			this.style.backgroundColor = '#efefef';
		};
		var arrline = new Array();
		var linexy = RS.route_segment[num].split(",");
		var line_l = (linexy.length - 1) / 2;
		for ( var i = 0; i < line_l; i++) {
			arrline.push(new parent.map.MLngLat(linexy[2 * i], linexy[2 * i + 1]));
		}
		var lineS = new parent.map.MLineStyle();
		lineS.thickness = 3;
		lineS.color = 0x00FF00;
		lineS.alpha = 1;
		var lineoption = new parent.map.MLineOptions();
		lineoption.canShowTip = false;
		lineoption.lineStyle = lineS;
		var line = new parent.map.MPolyline(arrline, lineoption);
		line.id = "1002";
		parent.map.mapObj.addOverlay(line, true);
	} catch (e) {
	}
}
var points = new Array();
function QN_driveLine_addpoi(addS, pid, x, y, data_citycode, type, address,
		tel, name, listid, listlength, search_type, poiStaticPath) {
	var tipc = tipcontent(type, address, tel);
	QN_setChooseStyle(listid, listlength, search_type);
	if ('undefined' != typeof parent.map.mapObj) {
		if (search_type == "rs") {
			parent.map.mapObj.removeOverlayById(RS.start_pid + "1000");
			parent.map.mapObj.removeOverlayById(RS.start_pid + "2000");

			RS.start_x = x;
			RS.start_y = y;
			RS.start_name = name;
			RS.start_type = type;
			RS.start_address = address;
			RS.start_tel = tel;
			RS.start_pid = pid;
			RS.start_citycode = data_citycode;
			RS.start_detailLink = poiStaticPath;

			var markerOption = new parent.map.MMarkerOptions();
			markerOption.imageUrl = "http://www.mapabc.com/images/qd.png";
			markerOption.isDraggable = false;// �Ƿ�����϶�

			var tipOption = new parent.map.MTipOptions();
			tipOption.title = "  ��㣺" + name;
			tipOption.content = "<br>" + tipc;// tip����
			tipOption.hasShadow = true;
			tipOption.borderStyle.thickness = 2;
			tipOption.borderStyle.color = 0x00BD0A;
			tipOption.borderStyle.alpha = 1;
			tipOption.titleFontStyle.name = "Arial";
			tipOption.titleFontStyle.size = 12;
			tipOption.titleFontStyle.color = 0xFFFFFF;
			tipOption.titleFontStyle.bold = true;
			tipOption.contentFontStyle.name = "Arial";
			tipOption.contentFontStyle.size = 13;
			tipOption.contentFontStyle.color = 0x000000;
			tipOption.contentFontStyle.bold = false;
			tipOption.fillStyle.color = 0xFFFFFF; // ���ɫ
			tipOption.fillStyle.alpha = 1;
			tipOption.titleFillStyle.color = 0x00BD0A;
			tipOption.titleFillStyle.alpha = 1;

			markerOption.tipOption = tipOption;
			markerOption.canShowTip = true;
			markerOption.hasShadow = true;
			var ll = new parent.map.MLngLat(x, y);
			Mmarker = new parent.map.MMarker(ll, markerOption);
			Mmarker.id = pid + "1000";

			var labelOptions = new parent.map.MLabelOptions();
			labelOptions.backgroundColor = "0x00BD0A";
			labelOptions.borderColor = "0xFFFFFF";
			labelOptions.content = name;
			var fontstyle = new parent.map.MFontStyle();
			fontstyle.name = "Arial";
			fontstyle.size = 12;
			fontstyle.color = 0x000000;
			fontstyle.bold = true;
			labelOptions.fontStyle = fontstyle;
			var labe = new parent.map.MLabel(new parent.map.MLngLat(x, y), labelOptions);
			labe.id = pid + "2000";
			var arr = new Array();
			if (addS == "0") {
				arr.push(Mmarker);
				arr.push(labe);
				//parent.map.mapObj.addOverlays(arr, true);
				for(var i=1;i<arr.length;i++){
					parent.map.mapObj.addOverlay(arr[i], false);
				}
				parent.map.mapObj.addOverlay(arr[0], true);
			} else {
				points.push(Mmarker);
				points.push(labe);
			}
		}
		if (search_type == "re") {
			parent.map.mapObj.removeOverlayById(RS.end_pid + "1000");
			parent.map.mapObj.removeOverlayById(RS.end_pid + "2000");
			RS.end_x = x;
			RS.end_y = y;
			RS.end_name = name;
			RS.end_type = type;
			RS.end_address = address;
			RS.end_tel = tel;
			RS.end_pid = pid;
			RS.end_citycode = data_citycode;
			RS.end_detailLink = poiStaticPath;
			var markerOption = new parent.map.MMarkerOptions();
			markerOption.imageUrl = "http://www.mapabc.com/images/zd.png";
			markerOption.isDraggable = false;// �Ƿ�����϶�

			var tipOption = new parent.map.MTipOptions();
			tipOption.title = "  �յ㣺" + name;
			tipOption.content = "<br>" + tipc;// tip����
			tipOption.hasShadow = true;
			tipOption.borderStyle.thickness = 2;
			tipOption.borderStyle.color = 0xff230b;
			tipOption.borderStyle.alpha = 1;
			tipOption.titleFontStyle.name = "Arial";
			tipOption.titleFontStyle.size = 12;
			tipOption.titleFontStyle.color = 0xFFFFFF;
			tipOption.titleFontStyle.bold = true;
			tipOption.contentFontStyle.name = "Arial";
			tipOption.contentFontStyle.size = 13;
			tipOption.contentFontStyle.color = 0x000000;
			tipOption.contentFontStyle.bold = false;
			tipOption.fillStyle.color = 0xFFFFFF; // ���ɫ
			tipOption.fillStyle.alpha = 1;
			tipOption.titleFillStyle.color = 0xff230b;
			tipOption.titleFillStyle.alpha = 1;

			markerOption.tipOption = tipOption;
			markerOption.canShowTip = true;
			markerOption.hasShadow = true;
			Mmarker = new parent.map.MMarker(new parent.map.MLngLat(x, y), markerOption);
			Mmarker.id = pid + "1000";

			var labelOptions = new parent.map.MLabelOptions();
			labelOptions.backgroundColor = "0xff230b";
			labelOptions.borderColor = "0xFFFFFF";
			labelOptions.content = name;
			var fontstyle = new parent.map.MFontStyle();
			fontstyle.name = "Arial";
			fontstyle.size = 12;
			fontstyle.color = 0x000000;
			fontstyle.bold = true;
			labelOptions.fontStyle = fontstyle;
			var labe = new parent.map.MLabel(new parent.map.MLngLat(x, y), labelOptions);
			labe.id = pid + "2000";
			var arr = new Array();
			if (addS == "0") {
				arr.push(Mmarker);
				arr.push(labe);
				//parent.map.mapObj.addOverlays(arr, true);
				for(var i=1;i<arr.length;i++){
					parent.map.mapObj.addOverlay(arr[i], false);
				}
				parent.map.mapObj.addOverlay(arr[0], true);
			} else {
				points.push(Mmarker);
				points.push(labe);
			}
		}
	}
}
function tipcontent(type, address, tel) {
	if (type == "" || type == "undefined" || type == null
			|| type == " undefined") {
		type = "����";
	}
	if (address == "" || address == "undefined" || address == null
			|| address == " undefined") {
		address = "����";
	}
	if (tel == "" || tel == "undefined" || tel == null || tel == " undefined") {
		tel = "����";
	}

	var str = "";
	if (!type == "" && type != null) {
		str += "  <font color='#000000'>���ͣ�" + type + "</font><br/>";
	}
	if (!address == "" && address != null) {
		str += "  <font color='#000000'>��ַ��" + address + "</font><br/>";
	}
	if (!tel == "" && tel != null) {
		str += "  <font color='#000000'>�绰��" + tel + "</font><br/>";
	}
	return str;
}
function addSE(imageurl, name, tipc, x, y, pid, color) {
	var markerOption = new parent.map.MMarkerOptions();
	markerOption.imageUrl = imageurl;
	markerOption.isDraggable = false;// �Ƿ�����϶�
	var tipOption = new parent.map.MTipOptions();
	tipOption.title = name;
	tipOption.content = "<br>" + tipc;// tip����
	tipOption.hasShadow = true;
	tipOption.borderStyle.thickness = 2;
	tipOption.borderStyle.color = color;
	tipOption.borderStyle.alpha = 1;
	tipOption.titleFontStyle.name = "Arial";
	tipOption.titleFontStyle.size = 12;
	tipOption.titleFontStyle.color = 0xFFFFFF;
	tipOption.titleFontStyle.bold = true;
	tipOption.contentFontStyle.name = "Arial";
	tipOption.contentFontStyle.size = 13;
	tipOption.contentFontStyle.color = 0x000000;
	tipOption.contentFontStyle.bold = false;
	tipOption.fillStyle.color = 0xFFFFFF; // ���ɫ
	tipOption.fillStyle.alpha = 1;
	tipOption.titleFillStyle.color = color;
	tipOption.titleFillStyle.alpha = 1;
	markerOption.tipOption = tipOption;
	markerOption.canShowTip = true;
	markerOption.hasShadow = true;
	var ll = new parent.map.MLngLat(x, y);
	Mmarker = new parent.map.MMarker(ll, markerOption);
	Mmarker.id = pid + "1000";

	var labelOptions = new parent.map.MLabelOptions();
	labelOptions.backgroundColor = color;
	labelOptions.borderColor = "0xFFFFFF";
	labelOptions.content = name;
	var fontstyle = new parent.map.MFontStyle();
	fontstyle.name = "Arial";
	fontstyle.size = 12;
	fontstyle.color = 0x000000;
	fontstyle.bold = true;
	labelOptions.fontStyle = fontstyle;
	var labe = new parent.map.MLabel(new parent.map.MLngLat(x, y), labelOptions);
	labe.id = pid + "2000";
	var arr = new Array();
	arr.push(Mmarker);
	arr.push(labe);
	//parent.map.mapObj.addOverlays(arr, true);
	for(var i=1;i<arr.length;i++){
		parent.map.mapObj.addOverlay(arr[i], false);
	}
	parent.map.mapObj.addOverlay(arr[0], true);
}
function QN_selectMode_route(mode) {
	// RS.route_way = mode;
	// qnroute_sis.setRouteType(RS.route_way);
	// qnroute_sis.setType("drive");
	// sis.searchBusAndDrive(qnroute_sis);
	RS.route_way = mode;
	qnroute_sis.routeType = RS.route_way;
	sis.routSearchByStartXYAndEndXY("drive", startPoint, endPoint, "all",
			qnroute_sis);
}
function QN_setChooseStyle(listid, listlength, search_type) {
	for ( var i = 0; i < listlength; i++) {
		$("" + search_type + "_" + i).className = "";
	}
	$(listid).className = "supermap_itemarea";
}
function QN_getdistance(le) {// �õ�
	if (le <= 1000) {
		var s = le;
		return s + "��";
	} else {
		var s = Math.round(le / 1000);
		return "Լ" + s + "����";
	}
}
function QN_resultError(divId) {
	$(divId).innerHTML = "<div class=\"supermap_warnpic\"></div><div class=\"supermap_warntip\"><strong>�������쳣,�����³���!</strong></div>";
}
function QN_zoom(y, x) {// TIP�е�"��λ�Ŵ�"
	parent.map.mapObj.setCenterByLatLng(y, x);
	parent.map.mapObj.setZoomLevel(15);
}
function Trim(s) {
	s = s.replace(/(^\s+)|(\s+$)/ig, "");
	s = s.replace(/\u3000+/g, " ");
	s = s.replace(/\s+/g, " ");
	return s;
}
function QN_removeAllOverlays() {
	if ('undefined' != typeof parent.map.mapObj) {
		parent.map.mapObj.removeAllOverlays();
	}
}