<%@ page contentType="text/html;charset=utf-8"%>
<%
String path = request.getContextPath();
%>
<!--<!DOCTYPE html >-->
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<TITLE></TITLE>

    <script src="<%=path%>/js/RGraph/libraries/RGraph.common.core.js" ></script>
    <script src="<%=path%>/js/RGraph/libraries/RGraph.gauge.js" ></script>
    <!--[if lt IE 9]><![endif]-->
	<script src="<%=path%>/js/RGraph/excanvas/excanvas.js"></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
	<script src="<%=path%>/js/Highcharts-2.2.5/js/highcharts.js"></script>
	<script src="<%=path%>/js/Highcharts-2.2.5/js/modules/exporting.js"></script>
    <script>
        window.onload = function ()
        {
            var gauge1 = new RGraph.Gauge('cvs1', 0, 160, 100);
            gauge1.Set('chart.scale.decimals', 0);
            gauge1.Set('chart.tickmarks.small', 50);
            gauge1.Set('chart.tickmarks.big',5);
            //gauge1.Set('chart.title.top', '');
            gauge1.Set('chart.title.top.size', 24);
            gauge1.Set('chart.units.post', '%');
            gauge1.Set('chart.title.bottom', '100%');
            gauge1.Set('chart.title.bottom.color', '#aaa');
			gauge1.Set('chart.colors.ranges', [[0, 60, 'red'], [60, 90, 'yellow'], [90, 160, 'green']]);
            gauge1.Draw();

			var gauge2 = new RGraph.Gauge('cvs2', 0, 160, 100);
            gauge2.Set('chart.scale.decimals', 0);
            gauge2.Set('chart.tickmarks.small', 50);
            gauge2.Set('chart.tickmarks.big',5);
            //gauge1.Set('chart.title.top', '');
            gauge2.Set('chart.title.top.size', 24);
            gauge2.Set('chart.units.post', '%');
            gauge2.Set('chart.title.bottom', '100%');
            gauge2.Set('chart.title.bottom.color', '#aaa');
			gauge2.Set('chart.colors.ranges', [[0, 60, 'red'], [60, 90, 'yellow'], [90, 160, 'green']]);
            gauge2.Draw();
			
			var gauge3 = new RGraph.Gauge('cvs3', 0, 160, 100);
            gauge3.Set('chart.scale.decimals', 0);
            gauge3.Set('chart.tickmarks.small', 50);
            gauge3.Set('chart.tickmarks.big',5);
            //gauge1.Set('chart.title.top', '');
            gauge3.Set('chart.title.top.size', 24);
            gauge3.Set('chart.units.post', '%');
            gauge3.Set('chart.title.bottom', '100%');
            gauge3.Set('chart.title.bottom.color', '#aaa');
			gauge3.Set('chart.colors.ranges', [[0, 60, 'red'], [60, 90, 'yellow'], [90, 160, 'green']]);
            gauge3.Draw();
			
			var gauge4 = new RGraph.Gauge('cvs4', 0, 160, 100);
            gauge4.Set('chart.scale.decimals', 0);
            gauge4.Set('chart.tickmarks.small', 50);
            gauge4.Set('chart.tickmarks.big',5);
            //gauge1.Set('chart.title.top', '');
            gauge4.Set('chart.title.top.size', 24);
            gauge4.Set('chart.units.post', '%');
            gauge4.Set('chart.title.bottom', '100%');
            gauge4.Set('chart.title.bottom.color', '#aaa');
			gauge4.Set('chart.colors.ranges', [[0, 60, 'red'], [60, 90, 'yellow'], [90, 160, 'green']]);
            gauge4.Draw();
			
			var gauge5 = new RGraph.Gauge('cvs5', 0, 160, 100);
            gauge5.Set('chart.scale.decimals', 0);
            gauge5.Set('chart.tickmarks.small', 50);
            gauge5.Set('chart.tickmarks.big',5);
            //gauge1.Set('chart.title.top', '');
            gauge5.Set('chart.title.top.size', 24);
            gauge5.Set('chart.units.post', '%');
            gauge5.Set('chart.title.bottom', '100%');
            gauge5.Set('chart.title.bottom.color', '#aaa');
			gauge5.Set('chart.colors.ranges', [[0, 60, 'red'], [60, 90, 'yellow'], [90, 160, 'green']]);
            gauge5.Draw();
			
        }
    </script>
	
		<script type="text/javascript">
$(function () {
    $(document).ready(function() {
        var chart1 = new Highcharts.Chart({
            chart: {
                renderTo: 'container1',
                type: 'line',
                marginRight: 130,
                marginBottom: 25
            },
            title: {
                text: 'Monthly Average Temperature',
                x: -20 //center
            },
            subtitle: {
                text: 'Source: WorldClimate.com',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Temperature (��C)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y +'��C';
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
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]
        });
		
		var chart2 = new Highcharts.Chart({
            chart: {
                renderTo: 'container2',
                type: 'line',
                marginRight: 130,
                marginBottom: 25
            },
            title: {
                text: 'Monthly Average Temperature',
                x: -20 //center
            },
            subtitle: {
                text: 'Source: WorldClimate.com',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Temperature (��C)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y +'��C';
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
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]
        });
		
		var chart3 = new Highcharts.Chart({
            chart: {
                renderTo: 'container3',
                type: 'line',
                marginRight: 130,
                marginBottom: 25
            },
            title: {
                text: 'Monthly Average Temperature',
                x: -20 //center
            },
            subtitle: {
                text: 'Source: WorldClimate.com',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Temperature (��C)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y +'��C';
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
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]
        });
		
		var chart4 = new Highcharts.Chart({
            chart: {
                renderTo: 'container4',
                type: 'line',
                marginRight: 130,
                marginBottom: 25
            },
            title: {
                text: 'Monthly Average Temperature',
                x: -20 //center
            },
            subtitle: {
                text: 'Source: WorldClimate.com',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Temperature (��C)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y +'��C';
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
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]
        });
		
		var chart5 = new Highcharts.Chart({
            chart: {
                renderTo: 'container5',
                type: 'line',
                marginRight: 130,
                marginBottom: 25
            },
            title: {
                text: 'Monthly Average Temperature',
                x: -20 //center
            },
            subtitle: {
                text: 'Source: WorldClimate.com',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Temperature (��C)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y +'��C';
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
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]
        });
		
    });
    
});
</script>
<style type="text/css"> 
/* CSS Document */ 
.STYLE1 {
	font-family: "宋体";
	font-weight: bold;
	font-size: 36px;
}
-->
body { 
    color: #4f6b72; 
    background: #E6EAE9; 
}

td { 
    border-right: 1px solid #C1DAD7; 
    border-bottom: 1px solid #C1DAD7; 
    background: #fff; 
    font-size:11px; 
    padding: 0px 0px 0px 0px; 
    color: #4f6b72; 
}


/*---------for IE 5.x bug*/ 
html>body td{ font-size:11px;} 
</style> 
</HEAD>

<BODY leftmargin="0" topmargin="0">
<table width="100%" border="0">
  <tr>
    <td width="3%" rowspan="3"><p class="STYLE1">销</p>
    <p class="STYLE1">售</p>
    <p class="STYLE1">达</p>
    <p class="STYLE1">成</p></td>
    <td width="11%">
		<div style="text-align: center">
			<canvas id="cvs1" width="230" height="230">[No canvas support]</canvas>
		</div>
	    <p>签单达成率</p>
    <p>(当月累计签单额/指标值)</p></td>
    <td width="86%"><div id="container1" style="min-width: 500px; height: 300px; margin: 0 auto"></div></td>
  </tr>
  <tr>
    <td>
		<div style="text-align: center">
			<canvas id="cvs2" width="230" height="230">[No canvas support]</canvas>
		</div>
		<p>回款达成率</p>
		<p>(当月累计回款额/指标值)</p></td>
    <td><div id="container2" style="min-width: 500px; height: 300px; margin: 0 auto"></div></td>
  </tr>
  <tr>
    <td>
		<div style="text-align: center">
			<canvas id="cvs3" width="230" height="230">[No canvas support]</canvas>
		</div>
		<p>费用使用率</p>
		<p>(当月累计费用额/指标值)</p></td>
    <td><div id="container3" style="min-width: 500px; height: 300px; margin: 0 auto"></div></td>
  </tr>
  <tr>
    <td rowspan="2"><p class="STYLE1">拜</p>
    <p class="STYLE1">访</p>
    <p class="STYLE1">统</p>
    <p class="STYLE1">计</p></td>
    <td>
		<div style="text-align: center">
			<canvas id="cvs4" width="230" height="230">[No canvas support]</canvas>
		</div>
		<p>员工出访达成率</p>
		<p>(当月累计客户拜访数/指标值)</p></td>
    <td><div id="container4" style="min-width: 500px; height: 300px; margin: 0 auto"></div></td>
  </tr>
  <tr>
    <td>
		<div style="text-align: center">
			<canvas id="cvs5" width="230" height="230">[No canvas support]</canvas>
		</div>
		<p>客户拜访覆盖率</p>
		<p>(当月被拜访客户数/客户总数)</p></td>
    <td><div id="container5" style="min-width: 500px; height: 300px; margin: 0 auto"></div></td>
  </tr>
  <tr>
    <td><p class="STYLE1">定</p>
    <p class="STYLE1">位</p>
    <p class="STYLE1">信</p>
    <p class="STYLE1">息</p></td>
    <td colspan="2">&nbsp;</td>
  </tr>
</table>

</BODY>
</HTML>
