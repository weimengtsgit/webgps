
function clickOnfolder1(n){
	var obj=document.all.item("treesutr"+n);
	var imgobj1=document.all.item("treel"+n);
	var imgobj2=document.all.item("plusl"+n);

	if(obj.style.display=="none"){
		obj.style.display="";
		imgobj1.src="../images/line_rd.gif";
		imgobj2.src="../images/folderopen.gif"

	}else{
		obj.style.display="none";
		imgobj1.src="../images/line_pulus.gif";
		imgobj2.src="../images/folderclose.gif"

	}
}
function clickOnfolder2(n){
	var imgobj1=document.all.item("treel"+n);
	var imgobj2=document.all.item("plusl"+n);
	var obj=document.all.item("treesutr"+n);
	if(obj.style.display=="none"){
		obj.style.display="";
		imgobj1.src="../images/line_rd_last.gif";
		imgobj2.src="../images/folderopen.gif"
	}else{
		obj.style.display="none";
		imgobj1.src="../images/line_pulus_last.gif";
		imgobj2.src="../images/folderclose.gif"
	}
}


