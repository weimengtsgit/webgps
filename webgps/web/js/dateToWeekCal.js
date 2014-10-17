

/**
 * 根据年月获取当前年月的最大天数
 */
function getDaysInMonth(year,month){
      month = parseInt(month,10)+1;
      var temp = new Date(year+"/"+month+"/0");
      return temp.getDate();
}

function dateToWeekCal(ymd){
	
	 var week = '';
	
     var dtw = dateToWeekData.split(',');
	 
     for(var i=0; i < dtw.length; i++){
    	 if(dtw[i].split('_')[0] == ymd){
    		 week = dtw[i].split('_')[1];
    	 }
     }
     
     return week;
 }
