<div id="inspection-one-chart-div" style="margin:0px;padding:0px;width:100%;height:100%;">
</div>
<script type="text/javascript">
	var result = ${result};
	$("#inspection-one-chart-div").ready(function(){
		if (typeof(Highcharts) == 'undefined') {
		     $.getScript("${ctx}/js/highcharts.js",function(){
				createInspectionOneChartReport(result);
			});
		}else{
			createInspectionOneChartReport(result);
		}  
	});
	function createInspectionOneChartReport(result){
		var chart = new Highcharts.Chart({
			exporting : {
				enabled : false
			},
			colors : ['green','#B7DEE8','#89A54E'],
			chart: {
				renderTo: "inspection-one-chart-div",
				height : $("#inspection-one-chart-div").width()/2
			},
			credits: {
		         enabled: false
			},
			title: {
				style : {
					"font-weight":'bold',
					"color": '#3E576F',
					"font-size": '20px'
				},
				text: result.title
			},
			subtitle: {
				text: result.subtitle,
				y:33,
				x: -20 //center
			},
			xAxis: {
				categories: result.categories,
				gridLineDashStyle : 'ShortDashDot',
				gridLineWidth: 1,
				labels : {
					style : {
						"color": 'black'
					}
				}
			},
			yAxis: [{
				title: {
					text: null,
					style: {
			               color: '#000000',
			               'font-weight':'bold',
			               fontSize:18
			            },
					rotation : 0
				},
				plotLines: [{
					width: 1,
					color: 'black'
				}],
		        gridLineWidth : 0,
		        tickInterval : 10,
				labels : {
					style: {
						color : 'black'
		            },
					formatter : function(){
						if(result.max==0){
							return '';
						}else if(this.value == result.max){
							return '100%';
						}else if(this.value < result.max){
							return this.value + '%';
						}else{
							return '';
						}
					}
				}
			}],
			legend: {
				 enabled : false,
		         align: 'right',
		         verticalAlign : 'top',
		         floating: true,
		         backgroundColor: '#FFFFFF',
		         x : 45,
		         y : 0
		    },
		    plotOptions: {
		        line : {
		            lineWidth : 1,
		            shadow : false,
		            dataLabels: {
		               enabled: true,
		               formatter : function(){
		            	   return this.y + '%';
		               }
		            }
		        }
		    },
			tooltip: {
				formatter: function() {
					var s;
					s = 
						this.x+'月'  +': '+ this.y+'%';
				return s;
			}
		},
		series: [{
			type: 'line',
			name: result.series3.name,
			data: result.series3.data
		}]
	});
}
</script>
