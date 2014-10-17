var visitRankHighChart;

function loadVisitRankChart(store_){
	var count_ = store_.getTotalCount();
	var width_ = count_ * 100;
	document.getElementById("visitRankChartTable").style.width = width_;
	var xAxis_ = [];var data_ = [];
	for(var i = 0;i < count_; i++){
		var record_ = store_.getAt(i);
		var vehicleNumber_ = record_.get('vehicleNumber');
		var visitCounts_ = record_.get('visitCounts');
		xAxis_.push(vehicleNumber_);
		data_.push(Number(visitCounts_));
	}
	
	visitRankHighChart = new Highcharts.Chart({
		chart : {
			renderTo : 'visitRankChartDiv',
			type : 'column',
			margin : [ 50, 50, 100, 80 ]
		},
		title : {
			text : '客户拜访数排名'
		},
		xAxis : {
			labels : {
				align : 'center',
				style : {
					//font : 'normal 13px Verdana, sans-serif'
				}
			}
		},
		yAxis : {
			min : 0,
			title : {
				text : '客户拜访个数'
			}
		},
		legend : {
			enabled : false
		},
		tooltip : {
			formatter : function() {
				return '<b>' + this.x + '</b><br/>'+'<b>客户拜访个数 ' + this.y + '</b>';
			}
		},
		series : [ {
			name : 'Population',
			dataLabels : {
				enabled : true,
				//rotation : -90,
				color : '#FFFFFF',
				align : 'right',
				x : -3,
				y : 10,
				formatter : function() {
					return this.y;
				},
				style : {
					//font : 'normal 13px Verdana, sans-serif'
				}
			}
		} ]
	});
	
	visitRankHighChart.xAxis[0].setCategories(xAxis_);
	visitRankHighChart.series[0].setData(data_);
}

