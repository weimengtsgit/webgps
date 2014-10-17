
$(function () {
    $(document).ready(function() {
		$.ajax({
	    	type: "POST",//访问WebService使用Post方式请求
	    	contentType: "application/json",
	    	url: "../target/target.do?method=getV21Main",//调用WebService的地址和方法名称组合 ---- WsURL/方法名
	    	data: "{}",//这里是要传递的参数，格式为 data: "{paraName:paraValue}",下面将会看到
	    	dataType: 'json',//WebService 会返回Json类型
	    	dataFilter: function(result) {//回调函数，result，返回值
	    		//字符串转成JSON数据
	    		result = eval('('+result+')');
	    		//目标维护值
	    		var curTarTemplates_ = result.curTarTemplates;
	    		var billAmount_ = curTarTemplates_.billAmount;
	    		var cashAmount_ = curTarTemplates_.cashAmount;
	    		var costAmount_ = curTarTemplates_.costAmount;
	    		var visitAmount_ = curTarTemplates_.visitAmount;
	    		var cusVisitAmount_ = curTarTemplates_.cusVisitAmount;
	    		//实际签单额
	    		var signBillCounts_ = result.signBillCounts;
	    		//签单仪表盘指针值
	    		var signBillNeedle = 0;
	    		if(Number(billAmount_ > 0)){
	    			signBillNeedle = Number(signBillCounts_)/Number(billAmount_) * 100;
	    		}
	    		$('#signBillMeterSpan').text("(本"+targetTemplateType+"累计签单额"+signBillCounts_+"元/目标值"+billAmount_+"元)");
	    		//实际回款额
	    		var cashCounts_ = result.cashCounts;
	    		//回款仪表盘指针值
	    		var cashNeedle = 0;
	    		if(Number(cashAmount_ > 0)){
	    			cashNeedle = Number(cashCounts_)/Number(cashAmount_) * 100;
	    		}
	    		$('#cashMeterSpan').text("(本"+targetTemplateType+"累计回款额"+cashCounts_+"元/目标值"+cashAmount_+"元)");
	    		
	    		//实际费用额
	    		var costCounts_ = result.costCounts;
	    		//费用仪表盘指针值
	    		var costNeedle = 0;
	    		if(Number(costAmount_ > 0)){
	    			costNeedle = Number(costCounts_)/Number(costAmount_) * 100;
	    		}
	    		$('#costMeterSpan').text("(本"+targetTemplateType+"累计费用额"+costCounts_+"元/目标值"+costAmount_+"元)");
	    		
	    		//实际员工出访
	    		var visitCounts_ = result.visitCounts;
	    		//员工出访仪表盘指针值
	    		var visitNeedle = 0;
	    		if(Number(visitAmount_ > 0)){
	    			visitNeedle = Number(visitCounts_)/Number(visitAmount_) * 100;
	    		}
	    		$('#visitMeterSpan').text("(本"+targetTemplateType+"累计拜访客户数"+visitCounts_+"个/目标值"+visitAmount_+"个)");
	    		
	    		//实际客户拜访覆盖
	    		var cusVisitCounts_ = result.cusVisitCounts;
	    		//客户拜访覆盖率仪表盘指针值
	    		var cusVisitNeedle = 0;
	    		if(Number(cusVisitAmount_ > 0)){
	    			cusVisitNeedle = Number(cusVisitCounts_)/Number(cusVisitAmount_) * 100;
	    		}
	    		$('#cusVisitMeterSpan').text("(本"+targetTemplateType+"累计拜访客户数"+cusVisitCounts_+"个/客户总数目"+cusVisitAmount_+"个)");
	    		
	    		
	    		var meterInterval_ = result.meterInterval;
	    		var signBillInterval_ = meterInterval_.signBillInterval;
	    		var cashInterval_ = meterInterval_.cashInterval;
	    		var costInterval_ = meterInterval_.costInterval;
	    		var visitInterval_ = meterInterval_.visitInterval;
	    		var cusVisitInterval_ = meterInterval_.cusVisitInterval;
	    		//签单仪表盘
	    		signBillGauge = createMeter('signBillCanvas', signBillInterval_, signBillNeedle);
	    		//回款仪表盘
	    		cashGauge = createMeter('cashCanvas', cashInterval_, cashNeedle);
	    		//费用仪表盘
	    		costGauge = createMeter('costCanvas', costInterval_, costNeedle);
	    		//员工出访仪表盘
	    		visitGauge = createMeter('visitCanvas', visitInterval_, visitNeedle);
	    		//客户拜访覆盖率仪表盘
	    		cusVisitGauge = createMeter('cusVisitCanvas', cusVisitInterval_, cusVisitNeedle);
	            
	    		//横坐标,日期
	    		var xAxis_ = [];
	    		//纵坐标,值
	    		var data_ = [];
	    		//签单趋势图
	    		var signBills_ = result.signBills;
	    		for(var i=0;i<signBills_.length;i++){
	    			var signBill_ = signBills_[i];
	    			xAxis_.push(signBill_.date);
	    			data_.push(signBill_.value);
	    		}
	    		signBillChart = createChart('signBillChart', xAxis_, data_);
	    		var xAxis_ = [];var data_ = [];
	    		//回款趋势图
	    		var cashs_ = result.cashs;
	    		for(var i=0;i<cashs_.length;i++){
	    			var cash_ = cashs_[i];
	    			xAxis_.push(cash_.date);
	    			data_.push(cash_.value);
	    		}
	    		cashGauge = createChart('cashChart', xAxis_, data_);
	    		var xAxis_ = [];var data_ = [];
	    		//费用趋势图
	    		var costs_ = result.costs;
	    		for(var i=0;i<costs_.length;i++){
	    			var cost_ = costs_[i];
	    			xAxis_.push(cost_.date);
	    			data_.push(cost_.value);
	    		}
	    		costGauge = createChart('costChart', xAxis_, data_);
	    		var xAxis_ = [];var data_ = [];
	    		//员工出访趋势图
	    		var visits_ = result.visits;
	    		for(var i=0;i<visits_.length;i++){
	    			var visit_ = visits_[i];
	    			xAxis_.push(visit_.date);
	    			data_.push(visit_.value);
	    		}
	    		visitGauge = createChart('visitChart', xAxis_, data_);
	    		var xAxis_ = [];var data_ = [];
	    		//费用趋势图
	    		var cusVisits_ = result.cusVisits;
	    		for(var i=0;i<cusVisits_.length;i++){
	    			var cusVisit_ = cusVisits_[i];
	    			xAxis_.push(cusVisit_.date);
	    			data_.push(cusVisit_.value);
	    		}
	    		cusVisitGauge = createChart('cusVisitChart', xAxis_, data_);
	    	}
	    });
		
    });
    
});
var signBillGauge;
var cashGauge;
var costGauge;
var visitGauge;
var cusVisitGauge;
//创建仪表盘
function createMeter(gaugeRenderName_, meterInterval_, needle_){
	var red_ = Number(meterInterval_.red);
	var yellow_ = Number(meterInterval_.yellow);
	var green_ = Number(meterInterval_.green);
	var gauge_ = new RGraph.Gauge(gaugeRenderName_, 0, green_, needle_);
	gauge_.Set('chart.scale.decimals', 0);
	gauge_.Set('chart.tickmarks.small', 50);
	gauge_.Set('chart.tickmarks.big',5);
    //gauge1.Set('chart.title.top', '');
	gauge_.Set('chart.title.top.size', 24);
	gauge_.Set('chart.units.post', '%');
	gauge_.Set('chart.title.bottom', needle_+'%');
    //gauge1.Set('chart.title.bottom.color', '#aaa');
	gauge_.Set('chart.colors.ranges', [[0, red_, 'red'], [red_, yellow_, 'yellow'], [yellow_, green_, 'green']]);
	gauge_.Draw();
	return gauge_;
}

var signBillChart;
var cashChart;
var costChart;
var visitChart;
var cusVisitChart;
function createChart(chartRenderName_, xAxis_, data_){
	var chart_ = new Highcharts.Chart({
        chart: {
            renderTo: chartRenderName_,
            type: 'line',
            marginRight: 130,
            marginBottom: 25
        },
        title: {
            text: '',
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
            	return '<b>'+this.x+'</b><br/>'+'￥'+this.y;
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

function setData(chart_, xAxis_, data_){
	chart_.xAxis[0].setCategories(xAxis_);
	chart_.series[0].setData(data_);
}

function parseJSON(data) {
	if (typeof data !== "string" || !data) {
		return null;
	}
	// Make sure leading/trailing whitespace is removed (IE can't handle it)
	data = jQuery.trim(data);
	// Attempt to parse using the native JSON parser first
	if (window.JSON && window.JSON.parse) {
		return window.JSON.parse(data);
	}
	// Make sure the incoming data is actual JSON
	// Logic borrowed from http://json.org/json2.js
	if (rvalidchars.test(data.replace(rvalidescape, "@").replace(rvalidtokens,
			"]").replace(rvalidbraces, ""))) {
		return (new Function("return " + data))();
	}
	jQuery.error("Invalid JSON: " + data);
}

function changeChart(url_, tableName_){
	$.ajax({
    	type: "POST",
    	contentType: "application/json",
    	url: url_,
    	data: "{}",
    	dataType: 'json',
    	dataFilter: function(result) {
    		//字符串转成JSON数据
    		result = eval('('+result+')');
    		//横坐标,日期
    		var xAxis_ = [];
    		//纵坐标,值
    		var data_ = [];
    		//趋势图
    		if(tableName_ == 'signBill'){
    			var signBills_ = result.signBills;
        		for(var i=0;i<signBills_.length;i++){
        			var signBill_ = signBills_[i];
        			xAxis_.push(signBill_.date);
        			data_.push(signBill_.value);
        		}
        		signBillChart = createChart('signBillChart', xAxis_, data_);
    		}else if(tableName_ == 'cash'){
    			var cashs_ = result.cashs;
        		for(var i=0;i<cashs_.length;i++){
        			var cash_ = cashs_[i];
        			xAxis_.push(cash_.date);
        			data_.push(cash_.value);
        		}
        		cashChart = createChart('cashChart', xAxis_, data_);
    		}else if(tableName_ == 'cost'){
    			var costs_ = result.costs;
        		for(var i=0;i<costs_.length;i++){
        			var cost_ = costs_[i];
        			xAxis_.push(cost_.date);
        			data_.push(cost_.value);
        		}
        		costChart = createChart('costChart', xAxis_, data_);
    		}else if(tableName_ == 'visit'){
    			var visits_ = result.visits;
        		for(var i=0;i<visits_.length;i++){
        			var visit_ = visits_[i];
        			xAxis_.push(visit_.date);
        			data_.push(visit_.value);
        		}
        		visitChart = createChart('visitChart', xAxis_, data_);
    		}else if(tableName_ == 'cusVisit'){
    			var cusVisits_ = result.cusVisits;
        		for(var i=0;i<cusVisits_.length;i++){
        			var cusVisit_ = cusVisits_[i];
        			xAxis_.push(cusVisit_.date);
        			data_.push(cusVisit_.value);
        		}
        		cusVisitChart = createChart('cusVisitChart', xAxis_, data_);
    		}
    	}
    });
}

