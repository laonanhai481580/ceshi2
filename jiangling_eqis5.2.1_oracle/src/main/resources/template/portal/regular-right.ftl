<div id="regular-right-div" style="margin:0px;padding:0px;width:100%;height:100%;">
</div>
<script type="text/javascript">
	var result = ${result};
	$("#regular-right-div").ready(function(){
		if (typeof(Highcharts) == 'undefined') {
		     $.getScript("${ctx}/js/highcharts.js",function(){
				createRegularRightReport(result);
			 });
		}else{
			createRegularRightReport(result);
		}  
	});
	function createRegularRightReport(result){
		var chart = new Highcharts.Chart({
			colors: ["green"],
			exporting : {
				enabled : false
			},
			chart: {
				renderTo: 'regular-right-div',
				height : $("#regular-right-div").width()/2
			},
			credits: {
		         enabled: false
			},
			title: {
				text: result.title,
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
				min : 0,
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
					return this.x + "月:" + this.y + "%";
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
				name: result.seriesRate.name,
				data: result.seriesRate.data
			}]
		});
	}
</script>
