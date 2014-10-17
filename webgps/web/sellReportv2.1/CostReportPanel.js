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
        	var startTime_ = result.startTime;
    		var endTime_ = result.endTime;
    		parent.costStartTimeReportChart = funConvertUTCToNormalDateTime(startTime_);
    		parent.costEndTimeReportChart = funConvertUTCToNormalDateTime(endTime_);
    		
    		var chartDatas_ = result.chartDatas;
    		count_ = chartDatas_.length;
    		var widthMultiplier = 80;
			var width_ = count_ * widthMultiplier;
    		document.getElementById("costChartTable").style.width = width_;
    		var tooltip_ = '￥';
    		//横坐标,日期
    		var xAxis_ = [];
    		//纵坐标,值
    		var data_ = [];
    		//趋势图
    		var chartTitle_ = result.chartTitle;
    		var meals_ = [];var transportations_ = [];var accommodations_ = [];
    		var communications_ = [];var gifts_ = [];var others_ = [];
        	for(var i=0;i<chartDatas_.length;i++){
        		var chartData_ = chartDatas_[i];
        		var value_ = chartData_.value.split(',');
        		if(value_.length == 6){
        			meals_.push(Number(value_[0]));
        			transportations_.push(Number(value_[1]));
        			accommodations_.push(Number(value_[2]));
        			communications_.push(Number(value_[3]));
        			gifts_.push(Number(value_[4]));
        			others_.push(Number(value_[5]));
        		}else{
        			meals_.push(0);
        			transportations_.push(0);
        			accommodations_.push(0);
        			communications_.push(0);
        			gifts_.push(0);
        			others_.push(0);
        		}
        		xAxis_.push(chartData_.date);
        	}
        	//未来审核值
    		var unaudited_ = result.unaudited;
    		if(unaudited_ >= 0 || unaudited_.length > 0){
        		$(nauditedSpanName).text("包含未审核金额:"+unaudited_+"元");
    		}else{
    			$(nauditedSpanName).text("");
    		}
        	//if(costChart){
        	//	costChart.xAxis[0].setCategories(xAxis_);
        	//	costChart.series[0].setData(data_);
        	//	costChart.setTitle({ text: chartTitle_ });
        	//}else{
        		costChart = createChart(chartName_, xAxis_, meals_, transportations_, accommodations_, communications_, gifts_, others_, chartTitle_, tooltip_);
        	//}
    		
        	
    	}
    });
}

function createChart(chartRenderName_, xAxis_, data1_, data2_, data3_, data4_, data5_, data6_, chartTitle_, tooltip_){
	var chart = new Highcharts.Chart({
		chart: {
			renderTo: chartRenderName_,
			type: 'column'
		},
		title: {
			text: chartTitle_
		},
		xAxis: {
			categories: xAxis_
		},
		yAxis: {
			min: 0,
			title: {
				text: ''
			},
			stackLabels: {
				enabled: true,
				style: {
					fontWeight: 'bold',
					color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
				}
			}
		},
		legend: {
			align: 'right',
			x: -100,
			verticalAlign: 'top',
			y: 20,
			floating: true,
			backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
			borderColor: '#CCC',
			borderWidth: 1,
			shadow: false
		},
		tooltip: {
			formatter: function() {
				
				return '<b>'+this.x+'</b><br/>'+tooltip_+this.y;
			}
		},
		plotOptions: {
			column: {
				stacking: 'normal',
				dataLabels: {
					enabled: true,
					color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
				}
			}
		},
		series: [{name: '餐费',data: data1_},
		         {name: '交通费',data: data2_},
		         {name: '住宿费',data: data3_},
		         {name: '通信费',data: data4_},
		         {name: '礼品费',data: data5_},
		         {name: '其他',data: data6_}
		]
	});
	//chart_.xAxis[0].setCategories(xAxis_);
	return chart_;
}

function jumpToDetail(){
	if(parent.costStartTimeReportChart == undefined || parent.costStartTimeReportChart.length <= 0){
		return;
	}
	parent.costDetailStartTime = parent.costStartTimeReportChart;
	parent.costDetailEndTime = parent.costEndTimeReportChart;
	parent.costDetailGpsDeviceids = parent.costGpsDeviceidsReportChart;
	parent.costDetailApproved = -1;
	parent.searchCostDetailGrid();
	var tabPanel_ = parent.Ext.getCmp('SellReportTabPanel');
	tabPanel_.activate('CostDetailPanel');
	var startTimeArr_ = parent.costDetailStartTime.split(' ');
	var endTimeArr_ = parent.costDetailEndTime.split(' ');
	if(startTimeArr_.length == 2 && endTimeArr_.length == 2){
		var startTime_ = parent.Ext.getCmp('costDetailsdf');
		startTime_.format = 'Y-m-d';
		startTime_.setValue(startTimeArr_[0]);
		var endTime_ = parent.Ext.getCmp('costDetailedf');
		endTime_.format = 'Y-m-d';
		endTime_.setValue(endTimeArr_[0]);
	}
	parent.Ext.getCmp('costDetailApproved').setValue(-1);
}
