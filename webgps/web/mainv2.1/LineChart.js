var signBillChart;
var cashChart;
var costChart;
var visitChart;
var cusVisitChart;

// 首页报表链接跳转
var signBillStartTimeChart;
var signBillEndTimeChart;
var costStartTimeChart;
var costEndTimeChart;
var cashStartTimeChart;
var cashEndTimeChart;
var visitStartTimeChart;
var visithEndTimeChart;
var cusVisitStartTimeChart;
var cusVisitEndTimeChart;

function getLineChart(url_, chartName_, nauditedSpanName) {
	$
			.ajax({
				type : "POST",// 访问WebService使用Post方式请求
				contentType : "application/json",
				url : url_,// 调用WebService的地址和方法名称组合 ---- WsURL/方法名
				data : "{}",// 这里是要传递的参数，格式为 data: "{paraName:paraValue}",下面将会看到
				dataType : 'json',// WebService 会返回Json类型
				dataFilter : function(result) {// 回调函数，result，返回值
					// 字符串转成JSON数据
					result = eval('(' + result + ')');
					var tooltip_ = '￥';
					var chartObject_ = null;
					if (chartName_ == 'signBillChartDiv') {
						chartObject_ = signBillChart;
						signBillStartTimeChart = funConvertUTCToNormalDateTime(result.startTime);
						signBillEndTimeChart = funConvertUTCToNormalDateTime(result.endTime);
					} else if (chartName_ == 'cashChartDiv') {
						chartObject_ = cashChart;
						cashStartTimeChart = funConvertUTCToNormalDateTime(result.startTime);
						cashEndTimeChart = funConvertUTCToNormalDateTime(result.endTime);
					} else if (chartName_ == 'costChartDiv') {
						chartObject_ = costChart;
						costStartTimeChart = funConvertUTCToNormalDateTime(result.startTime);
						costEndTimeChart = funConvertUTCToNormalDateTime(result.endTime);
					} else if (chartName_ == 'visitChartDiv') {
						chartObject_ = visitChart;
						tooltip_ = '';
						visitStartTimeChart = funConvertUTCToNormalDateTime(result.startTime);
						visithEndTimeChart = funConvertUTCToNormalDateTime(result.endTime);
					} else if (chartName_ == 'cusVisitChartDiv') {
						chartObject_ = cusVisitChart;
						tooltip_ = '';
						cusVisitStartTimeChart = funConvertUTCToNormalDateTime(result.startTime);
						cusVisitEndTimeChart = funConvertUTCToNormalDateTime(result.endTime);
					}
					// 横坐标,日期
					var xAxis_ = [];
					// 纵坐标,值
					var data_ = [];
					// 趋势图
					var chartDatas_ = result.chartDatas;
					var chartTitle_ = result.chartTitle;
					// 未来审核值
					var unaudited_ = result.unaudited;
					// 子主题
					var subTitle = "";
					if (parseInt(unaudited_) > 0 || unaudited_.length > 0) {
						subTitle = "包含未审核金额:" + unaudited_ / 10000 + "万元";
					}

					// 最大值，纵坐标间隔
					var maxVal, yInterval;
					if (chartName_ == 'signBillChartDiv'
							|| chartName_ == 'cashChartDiv'
							|| chartName_ == 'costChartDiv') {
						for ( var i = 0; i < chartDatas_.length; i++) {
							var chartData_ = chartDatas_[i];
							xAxis_.push(chartData_.date);
							// 转换成万元
							data_.push(Number(chartData_.value) / 10000);
						}
						maxVal = Math.max.apply(0, data_);
						yInterval = maxVal / 4;
					} else {
						for ( var i = 0; i < chartDatas_.length; i++) {
							var chartData_ = chartDatas_[i];
							xAxis_.push(chartData_.date);
							data_.push(Number(chartData_.value));
						}
						maxVal = Math.max.apply(0, data_);
						yInterval = maxVal / 4;
					}

					/*
					 * if (chartObject_) {
					 * chartObject_.xAxis[0].setCategories(xAxis_);
					 * chartObject_.series[0].setData(data_);
					 * chartObject_.setTitle({ text : chartTitle_ }); } else {
					 * chartObject_ = createChart(chartName_, xAxis_, data_,
					 * chartTitle_, tooltip_, subTitle, maxVal, yInterval); }
					 */

					var subTitWidth = 250 / 1600 * screen.width;
					chartObject_ = createChart(chartName_, xAxis_, data_,
							chartTitle_, tooltip_, subTitle, maxVal, yInterval,
							subTitWidth);

					if (chartName_ == 'signBillChartDiv') {
						signBillChart = chartObject_;
					} else if (chartName_ == 'cashChartDiv') {
						cashChart = chartObject_;
					} else if (chartName_ == 'costChartDiv') {
						costChart = chartObject_;
					} else if (chartName_ == 'visitChartDiv') {
						visitChart = chartObject_;
					} else if (chartName_ == 'cusVisitChartDiv') {
						cusVisitChart = chartObject_;
					}
				}
			});
}

// 创建趋势图
function createChart(chartRenderName_, xAxis_, data_, chartTitle_, tooltip_,
		subTitle, maxVal, yInterval, subTitWidth) {
	var chart_ = new Highcharts.Chart({
		chart : {
			renderTo : chartRenderName_,
			type : 'line',
			marginRight : 130,
			marginBottom : 25
		},
		exporting : {
			enabled : false
		},
		title : {
			text : chartTitle_,
			style : {
				color : '#0668b3',
				fontSize : '18px',
				fontWeight : 'bold',
				fontFamily : '宋体'
			},
			align : "center"
		},
		subtitle : {
			text : subTitle,
			x : subTitWidth,
			style : {
				color : 'black'
			}
		},
		xAxis : {
			categories : [],
			labels : {
				formatter : function() {
					var val = this.value;
					if (-1 == val.indexOf("~")) {
						if(-1 != val.indexOf("年") && -1 != val.indexOf("月") && -1 != val.indexOf("日"))
						{
							val = val.substring(val.indexOf("月") + 1, val.indexOf("日"));
						}
						else if(-1 != val.indexOf("年") && -1 != val.indexOf("月"))
						{
							val = val.substring(val.indexOf("年") + 1, val.indexOf("月") + 1);
						}
						return val;
					} else {
						return val.substring(val.indexOf("~") - 2, val
								.indexOf("~"))
								+ "~" + val.substring(val.length - 2);
					}
				}
			}
		},
		yAxis : {
			title : {
				text : ''
			},
			min : 0,
			max : maxVal,
			tickInterval : yInterval,
			gridLineWidth : 3,
			labels : {
				formatter : function() {
					return this.value;
				}
			}
		},
		tooltip : {
			formatter : function() {
				return '<b>' + this.x + '</b><br/>' + tooltip_ + this.y;
			}
		},
		legend : {
			enabled : false
		},
		series : [ {
			data : []
		} ]
	});
	chart_.xAxis[0].setCategories(xAxis_);
	chart_.series[0].setData(data_);
	return chart_;
}

function mainJumpToDetail(reportName_) {
	parent.clickLocButton();
	if (reportName_ == 'signBillChartDiv') {
		parent.sell_report_btn_click(false);
		var tabPanel_ = parent.Ext.getCmp('SellReportTabPanel');
		tabPanel_.activate('SignBillDetailPanel');
		var startTimeArr_ = signBillStartTimeChart.split(' ');
		var endTimeArr_ = signBillEndTimeChart.split(' ');
		if (startTimeArr_.length == 2 && endTimeArr_.length == 2) {
			var startTime_ = parent.Ext.getCmp('signBillDetailsdf');
			startTime_.format = 'Y-m-d';
			startTime_.setValue(startTimeArr_[0]);
			var endTime_ = parent.Ext.getCmp('signBillDetailedf');
			endTime_.format = 'Y-m-d';
			endTime_.setValue(endTimeArr_[0]);
		}
		parent.signBillDetailStartTime = signBillStartTimeChart;
		parent.signBillDetailEndTime = signBillEndTimeChart;
		parent.signBillDetailGpsDeviceids = '-1';
		parent.signBillDetailPoiName = '';
		parent.signBillDetailApproved = -1;
		parent.searchSignBillDetailGrid();
		parent.Ext.getCmp('signBillDetailApproved').setValue(-1);

	} else if (reportName_ == 'cashChartDiv') {
		parent.sell_report_btn_click(false);
		var tabPanel_ = parent.Ext.getCmp('SellReportTabPanel');
		tabPanel_.activate('CashDetailPanel');
		var startTimeArr_ = cashStartTimeChart.split(' ');
		var endTimeArr_ = cashEndTimeChart.split(' ');
		if (startTimeArr_.length == 2 && endTimeArr_.length == 2) {
			var startTime_ = parent.Ext.getCmp('cashDetailsdf');
			startTime_.format = 'Y-m-d';
			startTime_.setValue(startTimeArr_[0]);
			var endTime_ = parent.Ext.getCmp('cashDetailedf');
			endTime_.format = 'Y-m-d';
			endTime_.setValue(endTimeArr_[0]);
		}
		parent.cashDetailStartTime = cashStartTimeChart;
		parent.cashDetailEndTime = cashEndTimeChart;
		parent.cashDetailGpsDeviceids = '-1';
		parent.cashDetailPoiName = '';
		parent.cashDetailApproved = -1;
		parent.searchCashDetailGrid();
		parent.Ext.getCmp('cashDetailApproved').setValue(-1);
	} else if (reportName_ == 'costChartDiv') {
		parent.sell_report_btn_click(false);
		var tabPanel_ = parent.Ext.getCmp('SellReportTabPanel');
		tabPanel_.activate('CostDetailPanel');
		var startTimeArr_ = costStartTimeChart.split(' ');
		var endTimeArr_ = costEndTimeChart.split(' ');
		if (startTimeArr_.length == 2 && endTimeArr_.length == 2) {
			var startTime_ = parent.Ext.getCmp('costDetailsdf');
			startTime_.format = 'Y-m-d';
			startTime_.setValue(startTimeArr_[0]);
			var endTime_ = parent.Ext.getCmp('costDetailedf');
			endTime_.format = 'Y-m-d';
			endTime_.setValue(endTimeArr_[0]);
		}
		parent.costDetailStartTime = costStartTimeChart;
		parent.costDetailEndTime = costEndTimeChart;
		parent.costDetailGpsDeviceids = '-1';
		parent.costDetailApproved = -1;
		parent.searchCostDetailGrid();
		parent.Ext.getCmp('costDetailApproved').setValue(-1);
	} else if (reportName_ == 'visitChartDiv') {
		parent.report_btn_click(false);
		var tabPanel_ = parent.Ext.getCmp('ReportTabPanel');
		if (parent.visit_stat_report != ''
				&& parent.visit_stat_report.length > 0) {
			tabPanel_.activate('VisitStatPanel');
			var startTimeArr_ = visitStartTimeChart.split(' ');
			var endTimeArr_ = visithEndTimeChart.split(' ');
			if (startTimeArr_.length == 2 && endTimeArr_.length == 2) {
				var startTime_ = parent.Ext.getCmp('visitStatsdf');
				startTime_.format = 'Y-m-d';
				startTime_.setValue(startTimeArr_[0]);
				var endTime_ = parent.Ext.getCmp('visitStatedf');
				endTime_.format = 'Y-m-d';
				endTime_.setValue(endTimeArr_[0]);
			}
			parent.visitStatStartTime = visitStartTimeChart;
			parent.visitStatEndTime = visithEndTimeChart;
			parent.visitStatGpsDeviceids = '-1';
			parent.visitStatSearchValue = '';
			parent.searchVisitStatGrid();
		} else {
			tabPanel_.activate('VisitStatReportPanel');
			var startTimeArr_ = visitStartTimeChart.split(' ');
			var endTimeArr_ = visithEndTimeChart.split(' ');
			if (startTimeArr_.length == 2 && endTimeArr_.length == 2) {
				var startTime_ = parent.Ext.getCmp('visitStatReportsdf');
				startTime_.format = 'Y-m-d';
				startTime_.setValue(startTimeArr_[0]);
				var endTime_ = parent.Ext.getCmp('visitStatReportedf');
				endTime_.format = 'Y-m-d';
				endTime_.setValue(endTimeArr_[0]);
			}
			parent.visitStatReportStartTime = visitStartTimeChart;
			parent.visitStatReportEndTime = visithEndTimeChart;
			parent.visitStatReportGpsDeviceids = '-1';
			parent.visitStatReportSearchValue = '';
			parent.searchVisitStatReportGrid();
		}

	} else if (reportName_ == 'cusVisitChartDiv') {
		parent.report_btn_click(false);
		var tabPanel_ = parent.Ext.getCmp('ReportTabPanel');
		if (parent.cusvisit_stat_report != ''
				&& parent.cusvisit_stat_report.length > 0) {
			tabPanel_.activate('CusVisitStatPanel');
			var startTimeArr_ = cusVisitStartTimeChart.split(' ');
			var endTimeArr_ = cusVisitEndTimeChart.split(' ');
			if (startTimeArr_.length == 2 && endTimeArr_.length == 2) {
				var startTime_ = parent.Ext.getCmp('cusVisitStatsdf');
				startTime_.format = 'Y-m-d';
				startTime_.setValue(startTimeArr_[0]);
				var endTime_ = parent.Ext.getCmp('cusVisitStatedf');
				endTime_.format = 'Y-m-d';
				endTime_.setValue(endTimeArr_[0]);
			}
			parent.cusVisitStatStartTime = cusVisitStartTimeChart;
			parent.cusVisitStatEndTime = cusVisitEndTimeChart;
			parent.cusVisitStatGpsDeviceids = '-1';
			parent.cusVisitStatSearchValue = '';
			parent.searchCusVisitStatGrid();
		} else {
			tabPanel_.activate('CusVisitReportPanel');
			var startTimeArr_ = cusVisitStartTimeChart.split(' ');
			var endTimeArr_ = cusVisitEndTimeChart.split(' ');
			if (startTimeArr_.length == 2 && endTimeArr_.length == 2) {
				var startTime_ = parent.Ext.getCmp('cusVisitReportsdf');
				startTime_.format = 'Y-m-d';
				startTime_.setValue(startTimeArr_[0]);
				var endTime_ = parent.Ext.getCmp('cusVisitReportedf');
				endTime_.format = 'Y-m-d';
				endTime_.setValue(endTimeArr_[0]);
			}
			parent.cusVisitReportStartTime = cusVisitStartTimeChart;
			parent.cusVisitReportEndTime = cusVisitEndTimeChart;
			parent.cusVisitReportGpsDeviceids = '-1';
			parent.cusVisitReportSearchValue = '';
			parent.searchCusVisitReportGrid();
		}
	}
}

function funConvertUTCToNormalDateTime(utc) {
	var date = new Date(utc);
	date.setHours(date.getHours() + 8);
	var ndt;
	var year_ = date.getUTCFullYear();
	var month_ = (date.getUTCMonth() + 1);
	var date_ = date.getUTCDate();
	month_ = month_ >= 10 ? month_ : '0' + month_;
	date_ = date_ >= 10 ? date_ : '0' + date_;
	ndt = year_ + "-" + month_ + "-" + date_ + " " + (date.getUTCHours()) + ":"
			+ date.getUTCMinutes() + ":" + date.getUTCSeconds();
	return ndt;
}
