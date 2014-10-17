function getLineChart(url_, chartName_, nauditedSpanName){
	$.ajax({
    	type: "POST",//访问WebService使用Post方式请求
    	contentType: "application/json",
    	url: url_,//调用WebService的地址和方法名称组合 ---- WsURL/方法名
    	data: "{}",//这里是要传递的参数，格式为 data: "{paraName:paraValue}",下面将会看到
    	dataType: 'json',//WebService 会返回Json类型
    	dataFilter: function(result) {//回调函数，result，返回值
    		//字符串转成JSON数据
    		result = eval('('+result+')');
    		var chartDatas_ = result.chartDatas;
    		count_ = chartDatas_.length;
    		var widthMultiplier = 80;
    		if(targetTemplateType == '周'){
    			widthMultiplier = 100;
    		}else if(targetTemplateType == '旬'){
    			widthMultiplier = 120;
    		}
    		var tooltip_ = '￥';
    		var chartObject_ = null;
    		if(chartName_ == 'signBillChartDiv'){
    			chartObject_ = signBillChart;
    			var width_ = count_ * widthMultiplier;
    			document.getElementById("signBillChartTable").style.width = width_;
    			var startTime_ = result.startTime;
    			var endTime_ = result.endTime;
    			parent.signBillStartTimeReportChart = funConvertUTCToNormalDateTime(startTime_);
    			parent.signBillEndTimeReportChart = funConvertUTCToNormalDateTime(endTime_);
    		}else if(chartName_ == 'cashChartDiv'){
    			chartObject_ = cashChart;
    			var width_ = count_ * widthMultiplier;
    			document.getElementById("cashChartTable").style.width = width_;
    			var startTime_ = result.startTime;
    			var endTime_ = result.endTime;
    			parent.cashStartTimeReportChart = funConvertUTCToNormalDateTime(startTime_);
    			parent.cashEndTimeReportChart = funConvertUTCToNormalDateTime(endTime_);
    		}/*else if(chartName_ == 'costChartDiv'){
    			chartObject_ = costChart;
    			var startTime_ = result.startTime;
    			var endTime_ = result.endTime;
    			parent.costStartTimeReportChart = funConvertUTCToNormalDateTime(startTime_);
    			parent.costEndTimeReportChart = funConvertUTCToNormalDateTime(endTime_);
    		}*/else if(chartName_ == 'visitChartDiv'){
    			chartObject_ = visitChart;
    			tooltip_ = '';
    		}else if(chartName_ == 'cusVisitChartDiv'){
    			chartObject_ = cusVisitChart;
    			tooltip_ = '';
    		}
    		//横坐标,日期
    		var xAxis_ = [];
    		//纵坐标,值
    		var data_ = [];
    		//趋势图
    		var chartTitle_ = result.chartTitle;
        	for(var i=0;i<chartDatas_.length;i++){
        		var chartData_ = chartDatas_[i];
        		xAxis_.push(chartData_.date);
        		data_.push(Number(chartData_.value));
        	}
        	if(chartObject_){
        		chartObject_.xAxis[0].setCategories(xAxis_);
        		chartObject_.series[0].setData(data_);
        		chartObject_.setTitle({ text: chartTitle_ });
        	}else{
        		chartObject_ = createChart(chartName_, xAxis_, data_, chartTitle_, tooltip_);
        	}
        	//未来审核值
    		var unaudited_ = result.unaudited;
    		if(unaudited_ >= 0 || unaudited_.length > 0){
        		$(nauditedSpanName).text("包含未审核金额:"+unaudited_+"元");
    		}else{
    			$(nauditedSpanName).text("");
    		}
        	
    		if(chartName_ == 'signBillChartDiv'){
        		signBillChart = chartObject_;
    		}else if(chartName_ == 'cashChartDiv'){
    			cashChart = chartObject_;
    		}/*else if(chartName_ == 'costChartDiv'){
    			costChart = chartObject_;
    		}*/else if(chartName_ == 'visitChartDiv'){
    			visitChart = chartObject_;
    		}else if(chartName_ == 'cusVisitChartDiv'){
    			cusVisitChart = chartObject_;
    		}
    	}
    });
}

//创建趋势图
function createChart(chartRenderName_, xAxis_, data_, chartTitle_, tooltip_){
	var chart_ = new Highcharts.Chart({
        chart: {
            renderTo: chartRenderName_,
            type: 'line',
            marginRight: 130,
            marginBottom: 25
        },
        title: {
            text: chartTitle_,
            x: -20 //center
        },
        subtitle: {
            text: '',
            x: -20
        },
        xAxis: {
            categories: []
        },
        yAxis: {
            title: {
                text: ''
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            formatter: function() {
                    //return '<b>2012年7月'+this.x+'日</b><br/>'+
            	return '<b>'+this.x+'</b><br/>'+tooltip_+this.y;
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -10,
            y: 100,
            borderWidth: 0
        },
        series: [{
            //name: '',
            data: []
        }]
    });
	chart_.xAxis[0].setCategories(xAxis_);
	chart_.series[0].setData(data_);
	return chart_;
}

function jumpToDetail(reportName_){
	if(reportName_ == 'signBillChartDiv'){
		if(parent.signBillStartTimeReportChart.length <= 0 || parent.signBillEndTimeReportChart.length <= 0){
			return;
		}
		parent.signBillDetailStartTime = parent.signBillStartTimeReportChart;
		parent.signBillDetailEndTime = parent.signBillEndTimeReportChart;
		parent.signBillDetailGpsDeviceids = parent.signBillGpsDeviceidsReportChart;
		parent.signBillDetailPoiName = parent.signBillPoiNameReportChart;
		parent.signBillDetailApproved = -1;
		parent.searchSignBillDetailGrid();
		var tabPanel_ = parent.Ext.getCmp('SellReportTabPanel');
		tabPanel_.activate('SignBillDetailPanel');
		var startTimeArr_ = parent.signBillDetailStartTime.split(' ');
		var endTimeArr_ = parent.signBillDetailEndTime.split(' ');
		if(startTimeArr_.length == 2 && endTimeArr_.length == 2){
			var startTime_ = parent.Ext.getCmp('signBillDetailsdf');
			startTime_.format = 'Y-m-d';
			startTime_.setValue(startTimeArr_[0]);
			var endTime_ = parent.Ext.getCmp('signBillDetailedf');
			endTime_.format = 'Y-m-d';
			endTime_.setValue(endTimeArr_[0]);
		}
		parent.Ext.getCmp('signBillDetailApproved').setValue(-1);
	}else if(reportName_ == 'cashChartDiv'){
		if(parent.cashStartTimeReportChart.length <= 0 || parent.cashEndTimeReportChart.length <= 0){
			return;
		}
		parent.cashDetailStartTime = parent.cashStartTimeReportChart;
		parent.cashDetailEndTime = parent.cashEndTimeReportChart;
		parent.cashDetailGpsDeviceids = parent.cashGpsDeviceidsReportChart;
		parent.cashDetailPoiName = parent.cashPoiNameReportChart;
		parent.cashDetailApproved = -1;
		parent.searchCashDetailGrid();
		var tabPanel_ = parent.Ext.getCmp('SellReportTabPanel');
		tabPanel_.activate('CashDetailPanel');
		var startTimeArr_ = parent.cashDetailStartTime.split(' ');
		var endTimeArr_ = parent.cashDetailEndTime.split(' ');
		if(startTimeArr_.length == 2 && endTimeArr_.length == 2){
			var startTime_ = parent.Ext.getCmp('cashDetailsdf');
			startTime_.format = 'Y-m-d';
			startTime_.setValue(startTimeArr_[0]);
			var endTime_ = parent.Ext.getCmp('cashDetailedf');
			endTime_.format = 'Y-m-d';
			endTime_.setValue(endTimeArr_[0]);
		}
		parent.Ext.getCmp('cashDetailApproved').setValue(-1);
	}
}
