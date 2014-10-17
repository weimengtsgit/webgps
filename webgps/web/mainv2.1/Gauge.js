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
		type : "POST",// ����WebServiceʹ��Post��ʽ����
		contentType : "application/json",
		url : url,// ����WebService�ĵ�ַ�ͷ���������� ---- WsURL/������
		data : "{}",// ������Ҫ���ݵĲ�������ʽΪ data: "{paraName:paraValue}",���潫�ῴ��
		dataType : 'json',// WebService �᷵��Json����
		dataFilter : function(result) {// �ص�������result������ֵ
			// �ַ���ת��JSON����
			result = eval('(' + result + ')');
			// �Ǳ�������ֵ
			var gaugeKpi_ = result.gaugeKpi;
			// Ŀ��ά��ֵ
			var targetTemplates_ = result.targetTemplates;
			// ʵ��ǩ����
			var totals_ = result.totals;
			// ǩ���Ǳ���ָ��ֵ
			var needle_ = result.needle;
			/*
			 * var titleDesc = "("+title+totals_+"Ԫ/Ŀ��ֵ"+targetTemplates_+"Ԫ)";
			 * if(canvasName == 'cusVisitCanvas'){ titleDesc =
			 * "("+title+totals_+"��/�ͻ�����Ŀ"+targetTemplates_+"��)"; }else
			 * if(canvasName == 'visitCanvas'){ titleDesc =
			 * "("+title+totals_+"��/Ŀ��ֵ"+targetTemplates_+"��)"; }
			 */
			if ("signBillCanvas" == canvasName) {
				$("#signNeedle").text("ǩ��������" + " " + needle_ + "%");
				$("#bill").text(totals_ / 10000 + " ��Ԫ");
				$("#targetBill").text(targetTemplates_ / 10000 + " ��Ԫ");
			} else if ("cashCanvas" == canvasName) {
				$("#cashNeedle").text("�ؿ������" + " " + needle_ + "%");
				$("#cash").text(totals_ / 10000 + " ��Ԫ");
				$("#targetCash").text(targetTemplates_ / 10000 + " ��Ԫ");
			} else if ("costCanvas" == canvasName) {
				$("#costNeedle").text("���ö�ʹ����" + " " + needle_ + "%");
				$("#cost").text(totals_ / 10000 + " ��Ԫ");
				$("#targetCost").text(targetTemplates_ / 10000 + " ��Ԫ");
			} else if ("visitCanvas" == canvasName) {
				$("#visitNeedle").text("ҵ��Ա���ô����" + " " + needle_ + "%");
				$("#visit").text(totals_ + " ��");
				$("#targetVisit").text(targetTemplates_ + " ��");
			} else if ("cusVisitCanvas" == canvasName) {
				$("#cusVisitNeedle").text("�ͻ����ݷô����" + " " + needle_ + "%");
				$("#cusVisit").text(totals_ + " ��");
				$("#targetCusVisit").text(targetTemplates_ + " ��");
			}

			// ǩ���Ǳ���
			createMeter(canvasName, gaugeKpi_, needle_);

		}
	});
}
// �����Ǳ���
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
	gauge_.Set('chart.text.font', '����');
	gauge_.Set('chart.colors.ranges', [ [ 0, red_, colors[0] ],
			[ red_, yellow_, colors[1] ], [ yellow_, green_, colors[2] ] ]);
	gauge_.Draw();
	return gauge_;
}
