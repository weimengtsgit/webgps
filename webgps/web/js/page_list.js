//hxy add 06/01/10
var AppPath;
var hostname = document.location.host;
AppPath="http://"+hostname+"/"+"der";
//hxy end
function doCancelForm()
{
    frmDataList.target='_blank';
    frmDataList.action="../baobiao/CancelForm.jsp";
	frmDataList.submit();
}
function doDeleteForm()
{   frmDataList.target='_blank';
    frmDataList.action="../baobiao/DeleteForm.jsp";
	frmDataList.submit();
}
function doAfterPrint()
{
   Form_PageList.print.value='false';
}
function doPageListSubmit()
{
	if (Form_PageList.StartYear == null) {
        Form_PageList.submit();
        return;
    }
	sStartDate = Form_PageList.StartYear.value + "-" +
    Form_PageList.StartMonth.value + "-" +
    Form_PageList.StartDay.value;
    if (!isValidDate(parseInt(Form_PageList.StartYear.value),
                     parseInt(Form_PageList.StartMonth.value),
                     parseInt(Form_PageList.StartDay.value))) {
        return;
    }

    if (!isValidDate(parseInt(Form_PageList.EndYear.value),
                     parseInt(Form_PageList.EndMonth.value),
                     parseInt(Form_PageList.EndDay.value))) {
        return;
    }

	sEndDate =   Form_PageList.EndYear.value + "-" +
				 Form_PageList.EndMonth.value + "-" +
				 Form_PageList.EndDay.value;
	Form_PageList.StartDate.value = sStartDate;
	Form_PageList.EndDate.value = sEndDate;

	Form_PageList.submit();
}

function setPageIDOrderBy(sPageID, sOrderBy)
{
	Form_PageList.PageID.value = sPageID;
	Form_PageList.OrderBy.value = sOrderBy;
}

function setPageID(sPageID)
{
	Form_PageList.PageID.value= sPageID;
}

function doPopWinView(sPreUrl, sWinTitle)
{
	cbox=frmDataList.chbox;
    i=0;
	for(j=0;j<cbox.length;j++)
	{
		if(cbox[j].checked)
		{
			i=j;
			break;
		}
	}
	var b=cbox[i].value;
	url = sPreUrl + b;
	openwindow(url, sWinTitle, 800, 600);
}

function deleteAll(myform, forward, clazz){
	//alert('in getIds()');
	//alert(myform);

	var cbxs = myform.ids;
	if(cbxs==null){
		alert('没有数据');
		return;
	}
	var size = cbxs.length;
	var result = "";

	if(size == null){
		if(cbxs.checked){
			result += cbxs.value + ",";
		}
	}else{
		for(var i = 0; i < size; i++){
			if(cbxs[i].checked){
				result += cbxs[i].value + ",";
			}
		}
	}

	if(result.length>0){
		result = result.substring(0,result.length-1);
	}else{
		alert('请选择要删除的条目！');
		return;
	}
        if(!confirm("确定删除这些记录吗？")) return;
        
	myform.clazz.value=result;
	myform.action=forward;
    
	myform.submit();
}

function openwindow( url, winName, width, height) {
	xposition=0;
	yposition=0;

	if ((parseInt(navigator.appVersion) >= 4 )){
		xposition = (screen.width - width) / 2;
		yposition = (screen.height - height) / 2;
	}

	theproperty= "width=" + width + ","
				+ "height=" + height + ","
				+ "location=0,"
				+ "menubar=0,"
				+ "resizable=1,"
				+ "scrollbars=1,"
				+ "status=0,"
				+ "titlebar=0,"
				+ "toolbar=0,"
				+ "hotkeys=0,"
				+ "screenx=" + xposition + "," //仅适用于Netscape
				+ "screeny=" + yposition + "," //仅适用于Netscape
				+ "left=" + xposition + "," //IE
				+ "top=" + yposition; //IE

	window.open( url,winName,theproperty );
}

function openDefaultWindow(url){
	openwindow(url,'_blank',800,510);
}

function _selected(o)
{

	window.returnValue = o;
	//Window.alert(ret);
        window.close();

}