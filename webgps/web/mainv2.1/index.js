
$(function () {
    $(document).ready(function() {
    	getGauge('../signBill/signBill.do?method=getGaugeByTargetType', '#signBillGaugeSpan', '��'+targetTemplateType+'�ۼ�ǩ����', 'signBillCanvas');
    	getLineChart('../signBill/signBill.do?method=getSignBillsByTime', 'signBillChartDiv', '#signBillUnauditedSpan');
    	getGauge('../cash/cash.do?method=getGaugeByTargetType', '#cashGaugeSpan', '��'+targetTemplateType+'�ۼƻؿ��', 'cashCanvas');
    	getLineChart('../cash/cash.do?method=getCashsByTime', 'cashChartDiv', '#cashUnauditedSpan');
    	getGauge('../cost/cost.do?method=getGaugeByTargetType', '#costGaugeSpan', '��'+targetTemplateType+'�ۼƷ��ö�', 'costCanvas');
    	getLineChart('../cost/cost.do?method=getCostsByTime', 'costChartDiv', '#costUnauditedSpan');
    	getGauge('../visit/visit.do?method=getGaugeByTargetType', '#visitGaugeSpan', '��'+targetTemplateType+'�ۼưݷÿͻ���', 'visitCanvas');
    	getLineChart('../visit/visit.do?method=getVisitsByTime', 'visitChartDiv', '#visitUnauditedSpan');
    	getGauge('../visit/visit.do?method=getGaugeByTargetTypeCus', '#cusVisitGaugeSpan', '��'+targetTemplateType+'�ۼưݷÿͻ���', 'cusVisitCanvas');
    	getLineChart('../visit/visit.do?method=getCusVisitsByTime', 'cusVisitChartDiv', '#cusVisitUnauditedSpan');
    
    });
});
//add by wangzhen 2012-09-19 ��λ��ת
function jumpToPoiPlat() {
	parent.clickLocButton();
}
