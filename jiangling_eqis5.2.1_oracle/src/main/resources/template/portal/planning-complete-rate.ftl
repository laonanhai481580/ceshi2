<div id="planning-complete-rate-div" style="margin:0px;padding:0px;width:100%;height:100%;">
</div>
<script type="text/javascript">
	var result = ${result};
	$("#planning-complete-rate-div").ready(function(){
		if (typeof(Highcharts) == 'undefined') {
		     $.getScript("${ctx}/js/highcharts.js",function(){
				createPlanningReport(result);
			 });
		}else{
			createPlanningReport(result);
		}  
	});
	function createPlanningReport(result){
		var chart = new Highcharts.Chart({
			colors: ["red", "#E0B56C", "#DF5353", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
					 		"#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
			exporting : {
				enabled : false
			},
			chart: {
				renderTo: 'planning-complete-rate-div',
				height : $("#planning-complete-rate-div").width()/2
			},
			credits: {
		         enabled: false
			},
			title: {
				text: '本周' + result.title,
				style : {
					"font-weight":'bold',
					"color": 'black',
					"font-size": '20px'
				}
			},
			subtitle: {
				text: result.subtitle,
				y:33
			},
			plotOptions: {
				line: {
		            shadow : false,
		            lineWidth : 1,
		            dataLabels: {
		               enabled: true,
		               formatter : function(){
		            	   if(this.point.message){
		            		   return '';
		            	   }else{
		            		   return this.y + "%";
		            	   }
		               },
		               color:'black'
		            }
		        }
		    },
			xAxis: {
				categories: result.categories,
				labels : {
					style : {
						"color": 'black'
					}
				}
			},
			yAxis: [{
				title: {
					text: ""
				},
				gridLineWidth : 1,
				gridLineDashStyle : 'ShortDashDot',
				plotLines: [{
					value: 0,
					width: 1
				}],
				min : result.minRate,
				max : result.maxRate,
				labels : {
					formatter : function(){
						if(this.value>100){
							return '';
						}if(this.value<0){
							return '';
						}else{
							return this.value.toFixed(1) + "%";
						}
					},
					style : {
						"color": 'black'
					}
				}
			}],
			tooltip: {
				formatter: function() {
					if(this.point.message){
						return '<b>' + this.point.date + this.point.message + '</b>';
					}else{
						return '<b>' + this.point.date + ' </b><br/>'
						+ '计　划:' + this.point.planningAmount + '<br/>'
						+ '实　绩:' + this.point.completeAmount + '<br/>'
						+ '完成率:' + this.y + "%";
					}
				}
			},
			legend: {
				 enabled:$(window).width()>1024?true:false,
				 align: 'right',
		         verticalAlign : 'top',
		         floating: true,
		         backgroundColor: '#FFFFFF',
		         x : -16,
		         y : 16
			},
			series: [{
				type : 'line',
				name: result.series1.name,
				data: result.series1.data
			}]
		});
	}
</script>
