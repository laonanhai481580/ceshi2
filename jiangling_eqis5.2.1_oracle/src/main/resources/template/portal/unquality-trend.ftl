<div id="report-unquality-trend-div" style="margin:0px;padding:0px;width:100%;height:100%;">
</div>
<script type="text/javascript">
	var result = ${result};
	$("#report-unquality-trend-div").ready(function(){
		if (typeof(Highcharts) == 'undefined') {
		     $.getScript("${ctx}/js/highcharts.js",function(){
				createUnqualityReport(result);
			});
		}else{
			createUnqualityReport(result);
		}  
	});
	function createUnqualityReport(result){
		var chart = new Highcharts.Chart({
			colors: ["red", "#E0B56C", "#DF5353", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
			 		"#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
			exporting : {
				enabled : false
			},
			chart: {
				renderTo: 'report-unquality-trend-div',
				height : $("#report-unquality-trend-div").width()/2
			},
			credits: {
		         enabled: false
			},
			title: {
				text: "本周" + result.title,
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
		            	   if(result.group == 'rate'){
		            		    return this.y + "%";
						   }else{
								return this.y;
						   }
		               },
		               color:'black'
		            }
		        }
		    },
			xAxis: [{
				categories: result.categories,
				labels : {
					style : {
						"color": 'black'
					}
				}
			}],
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
				min : -0.1,
				max : result.max,
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
					if(this.point.date){
						return '<b>' + this.x + ' 不良发生率:</b>' + this.y + "%";
					}else{
						return '';
					}
				}
			},
			legend: {
				 enabled:$(window).width()>1024?true:false,
				 align: 'right',
		         verticalAlign : 'top',
		         floating: true,
		         backgroundColor: '#FFFFFF',
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
