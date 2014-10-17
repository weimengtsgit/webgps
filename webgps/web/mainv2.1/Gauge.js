/**
 * 
 */
var signBillGauge;
var cashGauge;
var costGauge;
var visitGauge;
var cusVisitGauge;
function getGauge(url, titleSpan, title, canvasName) {
	$.ajax({
		type : "POST",// 访问WebService使用Post方式请求
		contentType : "application/json",
		url : url,// 调用WebService的地址和方法名称组合 ---- WsURL/方法名
		data : "{}",// 这里是要传递的参数，格式为 data: "{paraName:paraValue}",下面将会看到
		dataType : 'json',// WebService 会返回Json类型
		dataFilter : function(result) {// 回调函数，result，返回值
			// 字符串转成JSON数据
			result = eval('(' + result + ')');
			// 仪表盘区间值
			var gaugeKpi_ = result.gaugeKpi;
			// 目标维护值
			var targetTemplates_ = result.targetTemplates;
			// 实际签单额
			var totals_ = result.totals;
			// 签单仪表盘指针值
			var needle_ = result.needle;
			/*
			 * var titleDesc = "("+title+totals_+"元/目标值"+targetTemplates_+"元)";
			 * if(canvasName == 'cusVisitCanvas'){ titleDesc =
			 * "("+title+totals_+"个/客户总数目"+targetTemplates_+"个)"; }else
			 * if(canvasName == 'visitCanvas'){ titleDesc =
			 * "("+title+totals_+"个/目标值"+targetTemplates_+"个)"; }
			 */
			if ("signBillCanvas" == canvasName) {
				$("#signNeedle").text("签单额达成率" + " " + needle_ + "%");
				$("#bill").text(totals_ / 10000 + " 万元");
				$("#targetBill").text(targetTemplates_ / 10000 + " 万元");
			} else if ("cashCanvas" == canvasName) {
				$("#cashNeedle").text("回款额达成率" + " " + needle_ + "%");
				$("#cash").text(totals_ / 10000 + " 万元");
				$("#targetCash").text(targetTemplates_ / 10000 + " 万元");
			} else if ("costCanvas" == canvasName) {
				$("#costNeedle").text("费用额使用率" + " " + needle_ + "%");
				$("#cost").text(totals_ / 10000 + " 万元");
				$("#targetCost").text(targetTemplates_ / 10000 + " 万元");
			} else if ("visitCanvas" == canvasName) {
				$("#visitNeedle").text("业务员出访达成率" + " " + needle_ + "%");
				$("#visit").text(totals_ + " 个");
				$("#targetVisit").text(targetTemplates_ + " 个");
			} else if ("cusVisitCanvas" == canvasName) {
				$("#cusVisitNeedle").text("客户被拜访达成率" + " " + needle_ + "%");
				$("#cusVisit").text(totals_ + " 个");
				$("#targetCusVisit").text(targetTemplates_ + " 个");
			}

			// 签单仪表盘
			createMeter(canvasName, gaugeKpi_, needle_);

		}
	});
}
// 创建仪表盘
function createMeter(gaugeRenderName_, gaugeInterval_, needle_) {
	var red_ = Number(gaugeInterval_.red);
	var yellow_ = Number(gaugeInterval_.yellow);
	var green_ = Number(gaugeInterval_.green);
	var colors = [ '#e7172f', '#ffff00', '#06bb4e' ];
	if (gaugeRenderName_ == 'costCanvas') {
		var tmp = red_;
		red_ = green_;
		green_ = tmp;
		colors = [ '#06bb4e', '#ffff00', '#e7172f' ];
	}
	var gauge_ = new RGraph.Gauge(gaugeRenderName_, 0, green_, needle_);
	gauge_.Set('chart.scale.decimals', 0);
	gauge_.Set('chart.tickmarks.small', 50);
	gauge_.Set('chart.tickmarks.big', 5);
	gauge_.Set('chart.units.post', '%');
	gauge_.Set('chart.text.size', 10);
	gauge_.Set('chart.text.font', '宋体');
	gauge_.Set('chart.colors.ranges', [ [ 0, red_, colors[0] ],
			[ red_, yellow_, colors[1] ], [ yellow_, green_, colors[2] ] ]);
	gauge_.Draw();
	return gauge_;
}
