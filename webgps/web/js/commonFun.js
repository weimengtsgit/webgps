//ȥ����β�ո�
function trim(inputString) {

              if (typeof inputString != "string") { return inputString; }
              var retValue = inputString;
              var ch = retValue.substring(0, 1);
              while (ch == " ") {
           //����ַ�����ʼ���ֵĿո�
                  retValue = retValue.substring(1, retValue.length);
                  ch = retValue.substring(0, 1);
              }
              ch = retValue.substring(retValue.length-1, retValue.length);
              while (ch == " ") {
                 //����ַ����������ֵĿո�
                 retValue = retValue.substring(0, retValue.length-1);
                 ch = retValue.substring(retValue.length-1, retValue.length);
              }
              while (retValue.indexOf("  ") != -1) {
          //�������м��������Ŀո��Ϊһ���ո�
                 retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1, retValue.length);
              }
              return retValue;
           }
/**ȥ���ַ��ո�*/
function TrimString(str){
  var i,j;
  if(str == "") return "";
  if (str==null) return "";
  for(i=0;i<str.length;i++)
    if(str.charAt(i) != ' ') break;
  if(i >= str.length) return "";

  for(j=str.length-1;j>=0;j--)
    if(str.charAt(j) != ' ') break;

  return str.substring(i,j+1);
}

//����Ļ���Ĵ��´���
function openWinToCenter(winName,w,h){
	var W = screen.width;
	var H = screen.height;
	var myleft = parseInt((W-w)/2);
	var mytop =parseInt((H-h)/2);
	var content='height='+h+', width='+w+',left='+myleft+', top='+mytop+', toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no, status=no';
	window.showModalDialog(winName,window,content);
}

/**
* ���ַ�ת��������
* �����ַ���ʽ����Ϊ��2006-12-30 21:12:22
*/
function setDate(pDate){
	var y=pDate.substring(0,4);
	var m=pDate.substring(5,7);
	var d=pDate.substring(8,10);
	var h=pDate.substring(11,13);
	var mi=pDate.substring(14,16);
	var s=pDate.substring(17,19);
	var tmpDate = new Date();
	tmpDate.setYear(y);
	tmpDate.setMonth(m-1);
	tmpDate.setDate(d);
	tmpDate.setHours(h);
	tmpDate.setMinutes(mi);
	tmpDate.setSeconds(s);
	return tmpDate;
}

/**
* �����ַ���� 2006-2-3 21:2:2  ת��  2006-02-03 21:02:02
*
*/
function formatdata(pDate){
var t=pDate;
  var dt=t.split(" ");
  var conData="";
  if(dt.length==2){
    var data=dt[0].split("-");
    conData+=data[0]+"-";
    if(data[1].length==1){
      conData+="0"+data[1]+"-";
    }else{
      conData+=data[1]+"-";
    }
    if(data[2].length==1){
      conData+="0"+data[2];
    }else{
      conData+=data[2];
    }
    conData+=" ";
    var time=dt[1].split(":");
    if(time[0].length==1){
      conData+="0"+time[0]+":";
    }else{
      conData+=time[0]+":";
    }
    if(time[1].length==1){
      conData+="0"+time[1]+":";
    }else{
      conData+=time[1]+":";
    }
    if(time[2].length==1){
      conData+="0"+time[2];
    }else{
      conData+=time[2];
    }
    return conData;
  }
}

String.prototype.isTime = function()
{
  var r = this.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
  if(r==null)return false; var d = new Date(r[1], r[3]-1,r[4],r[5],r[6],r[7]);
  return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]&&d.getHours()==r[5]&&d.getMinutes()==r[6]&&d.getSeconds()==r[7]);
}
String.prototype.isDate = function()
{
   var r = this.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
   if(r==null)return false; var d = new Date(r[1], r[3]-1, r[4]);
   return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
}

function dateToString(date){
  var result = date.getFullYear()+"-";
  if(date.getMonth()+1<10)
  result += "0";
  result += (date.getMonth()+1)+"-";
  if(date.getDate()<10)
  result += "0";
  result += date.getDate()+" ";
  if(date.getHours()<10)
  result += "0";
  result += date.getHours()+":";
  if(date.getMinutes()<10)
  result += "0";
  result += date.getMinutes()+":";
  if(date.getSeconds()<10)
  result += "0";
  result += date.getSeconds();
  return result;
}
/**�õ���ɫ*/
function getColor(curIndex){
var tmp =curIndex%10;
var tmpColor="";
switch(tmp){
  case 0:
  tmpColor="FF0000";
  break;
  case 1:
  tmpColor="00B2C9";
  break;
  case 2:
  tmpColor="00FF00";
  break;
  case 3:
  tmpColor="00FFFF";
  break;
  case 4:
  tmpColor="0000FF";
  break;
  case 5:
  tmpColor="FF00FF";
  break;
  case 6:
  tmpColor="EC008C";
  break;
  case 7:
  tmpColor="2E3192";
  break;
  case 8:
  tmpColor="00AEEF";
  break;
  case 9:
  tmpColor="00A651";
  break;
}
return tmpColor;
}

/**�ؼ�ֻ����������ֵ
* ����ڿؼ���onKeyPress�¼���
*/
function inputNumberOnly(){
if ( !(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))
|| (window.event.keyCode == 13) || (window.event.keyCode == 46)
|| (window.event.keyCode == 45))){
	window.event.keyCode = 0 ;
}
}

/**�õ�ĳһCOOKIE��ֵ*/
function getCookie(cookieName) {
//alert(document.cookie);
//alert("1cookname=:"+cookieName +"\n" +"value=");
  var cookieString =unescape( document.cookie);
  var start = cookieString.indexOf(cookieName + '=');
//alert("2cookname=:"+cookieName +"\n" +"value=");

  if (start == -1) return "";
  start += cookieName.length + 1;
  var end = cookieString.indexOf(';', start);
//alert("3cookname=:"+cookieName +"\n" +"value="+unescape(cookieString.substring(start)));
  if (end == -1) return unescape(cookieString.substring(start));
//alert("4cookname=:"+cookieName +"\n" +"value="+unescape(cookieString.substring(start, end)));
  return unescape(cookieString.substring(start, end));
}
//pType:0��ʽ��Сʱ 1����ʽ�����Ӻ���
function formatTime(pValue,pType){
var tmpValue =TrimString( pValue);
if (tmpValue.length==0)return "00";
if (tmpValue.length==1)
{
	tmpValue = "0"+tmpValue;
	return tmpValue;
}
if (tmpValue.length==2)
{
	if (tmpValue.substring(0,1)=="0") return tmpValue;
	var tmp = parseInt(tmpValue);
	if (pType==0)
	{
		if (tmp<0|| tmp>23) return -1;
	}else{
		if (tmp<0|| tmp>59) return -1;
	}
	return tmpValue;

}
return -1;
}

/**
* �����ַ���ʽ����Ϊ��2006-12-30 21:12:22
*/


/**
* ��ʽ��ʱ�����
*/
function formatOutputDate(date){
	var tmpSecond=date.getSeconds();
	var tmpMi=date.getMinutes();
	var tmpH=date.getHours();
	var tmpD=date.getDate();
	var tmpM=date.getMonth()+1;
	if (tmpSecond<10)tmpSecond="0"+tmpSecond;
	if (tmpMi<10)tmpMi="0"+tmpMi;
	if (tmpH<10)tmpH="0"+tmpH;
	if (tmpD<10)tmpD="0"+tmpD;
	if (tmpM<10)tmpM="0"+tmpM;
	return date.getYear() +'-' +  (tmpM) + '-' +tmpD+ ' '+ tmpH+':'+tmpMi+':'+tmpSecond;
}

/**
* ��ʽ�����ϵͳʱ��
*/
function formatOutputSystemDate(date){
	var tmpSecond=date.getSeconds();
	var tmpMi=date.getMinutes();
	var tmpH=date.getHours();
	var tmpD=date.getDate();
	var tmpM=date.getMonth()+1;
	if (tmpSecond<10)tmpSecond="0"+tmpSecond;
	if (tmpMi<10)tmpMi="0"+tmpMi;
	if (tmpH<10)tmpH="0"+tmpH;
	if (tmpD<10)tmpD="0"+tmpD;
	if (tmpM<10)tmpM="0"+tmpM;
	return date.getYear() +'-' +  (tmpM) + '-' +tmpD+ ' '+ tmpH+':'+tmpMi+':'+tmpSecond;
}

function addZero2LeftStr(str){
		if(1==str.length){
			return "0" + str;
		}
		return str;
	}

function changeImg(obj,img){
		obj.src=img;
	}
function changecolor(obj,color){
	var tmpArr=obj.cells;
	for(var i=0;i<tmpArr.length;i++){
		tmpArr[i].style.backgroundColor=color;
	}
}