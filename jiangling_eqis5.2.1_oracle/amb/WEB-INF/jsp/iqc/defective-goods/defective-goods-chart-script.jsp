<%@ page contentType="text/html;charset=UTF-8" import="java.util.Date"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript">
		function contentResize(){
			if(cacheResult != null){
				clearTable();
				createReport(cacheResult);
				createDetailTable(cacheResult);
			}
		}
		
		var cacheResult = null;
		$(document).ready(function(){
			contentResize();
			$('#datepicker1').datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			search();
		});
		
		var chart = null;
		function createReport(result){
			if(chart != null){
				chart.destroy();
				chart = null;
			}
			var reportDiv = "reportDiv";
			var width = $("#" + reportDiv).width();
			chart = new Highcharts.Chart({
				exporting : {
					enabled : false
				},
				colors : ['#95B3D7','#B7DEE8','#89A54E'],
				chart: {
					renderTo: reportDiv,
					spacingRight : 63
					
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
					x: 0 //center
				},
				xAxis: {
					categories: result.categories,
					//gridLineDashStyle : 'ShortDashDot',
					//gridLineWidth: 1,
					labels : {
						style : {
							"color": 'black'
						},
						rotation:-15
					}
				},
				yAxis: [ {
					gridLineWidth : 1,
					gridLineDashStyle : 'ShortDashDot',
					title: {
						text: result.yAxisTitle1,
						rotation : 0,
						style : {
				               'font-weight':'bold',
				               fontSize:15,
				               color : 'black'
							}
					},
			plotLines: [{
						value: 12,
						color: 'black'
					}],
					labels : {
						align : 'right'
					}
				}],
				legend: {
			         align: 'right',
			         verticalAlign : 'top',
			         floating: true,
			         backgroundColor: '#FFFFFF',
			         x : -90,
			         y : 0
			    },
			    plotOptions: {
					column: {
			            dataLabels: {
			               enabled: true,
			               formatter : function(){
			            	   if(this.y<=0){
			            		   return '';
			            	   }else{
			            		   return '<font style="color:black;">' + this.y + '</font>';
			            	   }
			               }      
			            },
			            shadow : true,
// 			            pointInterval : 0,
			            borderWidth:0,
			            pointPadding:0,
			             cursor : 'pointer',
			               events : {
				            	click : function(obj){
// 				            		alert(obj.point.searchname);
				            		showDetailByDate(obj.point.searchname);
				            	}
				            } 
			        } 
			    },
				tooltip: {
					formatter: function() {
						var s;
						if (this.point.name) { // the pie chart
							s = ''+
								this.point.name +': '+ this.y +' fruits';
						} else {
							s = ''+
								this.x  +': '+ this.y;
						}
						return s;
					}
				},
				series: [{
					type: 'column',
					name: result.series1.name,
					data: result.series1.data
				}]
			});
		}
		
		//确定的查询方法
		function search(){
			var date1 = $("#datepicker1").val(); 
			var date2 = $("#datepicker2").val(); 
			if(date1>date2){
				alert("日期前后有误，请重新设置 !"); 
				$("#datepicker2").focus();
			}else{
				reportByParams(getParams());
			}
		}	
		
		//根据参数获取数据
		function reportByParams(params){
	 		$.post("${iqcctx}/defective-goods/defective-goods-chart-datas.htm",params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					clearTable();
					createReport(result);
					createDetailTable(result);
					cacheResult = result;
				}
			},'json'); 
		}
		
		//获取表单的值
		function getParams(){
			var params = {};
			$("#search input[type=text]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			$("#search select").each(function(index,obj){
				var jObj=$(obj);
				if(obj.name&&jObj.val()&&jObj.val()!=""){
					params[obj.name]=jObj.val();
				}
			});
			$("#search input[type=radio]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&obj.checked&&jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			$("#search input[type=hidden]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			return params;
		}
			
		//创建表格
		var detailTable = null;
		function createDetailTable(result){
			clearTable();
			var colModel=[{name:'custom_name',width:80,index:'custom_name',align:'center',label:result.series1.name}],datas = [];
			var tableHeaderList = result.tableHeaderList;
			for(var i=0;i<tableHeaderList.length;i++){
				colModel.push({name:'date'+i,index:'date' + i,width:120,align:'center',label : tableHeaderList[i]});
			}
			var dataCheckList = result.series1.data;
			var p = {id:1,custom_name:"不合格批数"};
			var checks = [];
			for(var i=0;i<dataCheckList.length;i++){
				checks.push(dataCheckList[i].y);
			}
			for(var i=0;i<dataCheckList.length;i++){
				p['date' + i] = checks[i];
			}
			datas.push(p);
		
			detailTable = $("#detail_table").jqGrid({
				datatype: "local",
				localReader : {
					id : 'custom_name'
				},
				data: datas,
				rownumbers : true,
				colModel:colModel,
				multiselect: false,
				autowidth: false,
				forceFit : true,
				shrinkToFit: false,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete : function(){}
			});
			$("#detail_table").jqGrid("setGridWidth",$("#search").width());
			$("#detail_table").jqGrid("setGridHeight",50);
		}
			
		//清除表格
		function clearTable(){
			if(detailTable != null){
				detailTable.GridDestroy();
				detailTable = null;
			}
			$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table>");
		}
		
		//查看明细的方法
		function showDetailByDate(searchname){
			var params = getParams();
			params['params.searchname'] = searchname;
			var url = '${iqcctx}/defective-goods/defective-goods-detail.htm?1=1';
			for(var pro in params){
				url += "&" + pro + "=" + params[pro];
			}
			//alert(url);
			$.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"台帐明细"
	 		});
		}
		function goToNewLocationById(id){
			var url='${iqcctx}';
			$.colorbox({href:url+"/inspection-report/view-info.htm?id="+id,iframe:true, 
				innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"页面详情"
			});
		}
			
		function exportChart(){
			$.exportChart({
	    		chart:chart,
	    		grid:$("#detail_table"),
	    		message:$("#message")
	    	});
		}
		//发送邮件
		function sendChart(){
			$.sendChart({
				chart:chart,
				grid:$("#detail_table"),
				message:$("#message"),
				width:$("#reportDiv").width(),
				height:$("#reportDiv").height()
			});
		}
	</script>
</head>
