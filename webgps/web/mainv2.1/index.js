
$(function () {
    $(document).ready(function() {
    	getGauge('../signBill/signBill.do?method=getGaugeByTargetType', '#signBillGaugeSpan', '本'+targetTemplateType+'累计签单额', 'signBillCanvas');
    	getLineChart('../signBill/signBill.do?method=getSignBillsByTime', 'signBillChartDiv', '#signBillUnauditedSpan');
    	getGauge('../cash/cash.do?method=getGaugeByTargetType', '#cashGaugeSpan', '本'+targetTemplateType+'累计回款额', 'cashCanvas');
    	getLineChart('../cash/cash.do?method=getCashsByTime', 'cashChartDiv', '#cashUnauditedSpan');
    	getGauge('../cost/cost.do?method=getGaugeByTargetType', '#costGaugeSpan', '本'+targetTemplateType+'累计费用额', 'costCanvas');
    	getLineChart('../cost/cost.do?method=getCostsByTime', 'costChartDiv', '#costUnauditedSpan');
    	getGauge('../visit/visit.do?method=getGaugeByTargetType', '#visitGaugeSpan', '本'+targetTemplateType+'累计拜访客户数', 'visitCanvas');
    	getLineChart('../visit/visit.do?method=getVisitsByTime', 'visitChartDiv', '#visitUnauditedSpan');
    	getGauge('../visit/visit.do?method=getGaugeByTargetTypeCus', '#cusVisitGaugeSpan', '本'+targetTemplateType+'累计拜访客户数', 'cusVisitCanvas');
    	getLineChart('../visit/visit.do?method=getCusVisitsByTime', 'cusVisitChartDiv', '#cusVisitUnauditedSpan');
    
    });
});
//add by wangzhen 2012-09-19 定位跳转
function jumpToPoiPlat() {
	parent.clickLocButton();
}
