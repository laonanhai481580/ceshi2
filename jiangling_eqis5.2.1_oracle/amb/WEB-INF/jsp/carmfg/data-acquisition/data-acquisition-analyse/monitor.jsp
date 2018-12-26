<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<style>
	<!--
		#searchUl{
			margin:0px;
			padding:0px;
		}
		#searchUl li{
			float:left;
			height:24px;
			line-height:24px;
			list-style:none;
		}
		#searchUl li .span{
			float:left;
			width:70px;
			text-align:right;
			padding-right:2px;
		}
	-->
	</style>
	<script type="text/javascript">
		function contentResize(){
			if(cacheChartResult != null){
				createReport(cacheChartResult);
			}
		}
		var chart = null,searchParams = null,cacheChartResult = null,cacheTableResult;
		var historyCount = 1;
		$(document).ready(function(){
			$("#datepicker1").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#datepicker2").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			reportByParams(getParams());
		});
		function createReport(result){
			if(chart != null){
				try {
					chart.destroy();
					chart = null;
				} catch (e) {
					chart = null;
				}
			}
			$("#reportDiv").html("图表生成中,请稍候... ...");
			setTimeout(function(){
				chart = new Highcharts.Chart({
					colors: ["green"],
					exporting : {
						enabled : false
					},
					chart: {
						renderTo: 'reportDiv'
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
						column: {
				            dataLabels: {
				               enabled: true,
					            formatter : function(){
									return '<font style="color:black;">' + this.y + '</font>';
								}
				            },
				            shadow : false,
// 				            pointWidth : size == 0 ? 1 : (width-160)/size,
				            cursor : 'pointer',
				            events : {
				            	click : function(obj){
				            		showDetailByPoint(obj.point);
				            	}
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
						min : result.min,
						max : result.max,
						labels : {
							style : {
								"color": 'black'
							}
						}
					}],
					tooltip: {
						formatter: function() {
							return '<b>' + this.x + ':</b>' + this.y;
						}
					},
					legend: {
						 align: 'right',
				         verticalAlign : 'top',
				         floating: true,
				         backgroundColor: '#FFFFFF',
				         x : -16,
				         y : 16
					},
					series: [{
						type : 'column',
						name: result.series1.name,
						data: result.series1.data
					}]
				});
			},10);
		}
		//确定的查询方法
		function search(){
			reportByParams(getParams());
		}
		//根据参数获取数据
		function reportByParams(params){
			$.post("${mfgctx}/data-acquisition/data-acquisition-analyse/monitor-datas.htm",params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					searchParams = params;
					try {
						createReport(result);
						cacheChartResult = result;
					} catch (e) {
						alert(e.message);
					}
				}
			},'json');
		}
		//获取表单的值
		function getParams(){
			var params = {};
			$(":input","#customSearchDiv").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()){
					if(obj.type=="radio"){
						if(obj.checked){
							params[obj.name] = jObj.val();
							if(obj.name=='params.group'&&jObj.attr("title")){
								params['params.groupName'] = jObj.attr("title");
							}
						}
					}else if(obj.type == 'select-one'){
						if(!obj.disabled){
							params[obj.name] = jObj.val();
						}
					}else{
						params[obj.name] = jObj.val();
					}
				}
			});
			return params;
		}
		
		function setParams(initParams){
			$(":input","#customSearchDiv").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&initParams[obj.name]){
					if(obj.type=="radio"){
						if(jObj.val() == initParams[obj.name]){
							obj.checked = true;
							typeSelect(obj,jObj.val() + '_select');
						}
					}else if(obj.type == 'select-one'){
						jObj.val(initParams[obj.name]);
					}else{
						jObj.val(initParams[obj.name]);
					}
				}
			});
		}
		//返回
		function goToBack(){
			window.history.back();
		}
		
		function showDetailByPoint(point){
			var params = searchParams;
			params['params.processBusNode_equals'] = point.processBusNode;
			var url = '${mfgctx}/data-acquisition/data-acquisition-analyse/monitor-detail.htm?' + $.param(params);
			$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:600,
	 			overlayClose:false,
	 			title:"计划监控台帐明细"
	 		});
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="dataAcquisition";
		var thirdMenu="monitor";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-data-acquisition-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<%
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH,1);
			String startDateStr = sdf.format(calendar.getTime());
			
			calendar.add(Calendar.MONTH,1);
			calendar.add(Calendar.DATE,-1);
			String endDateStr = sdf.format(calendar.getTime());
		%>
		<div class="opt-body" style="overflow-y:auto;">
			<form onsubmit="return false;">
			<div class="opt-btn" id="btnDiv">
				<button type="button"  class='btn' onclick="reset();formReset();"><span><span>重置查询</span></span></button>
			</div>
			<div id="customSearchDiv" style="display:block;padding:4px;">
				<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
					<tr>
						<td>
							<ul id="searchUl">
						 		<li>
						 			<span class="span">日&nbsp;&nbsp;期</span>
						 			<input id="datepicker1" type="text" readonly="readonly" style="width:70px;" class="line" name="params.startDate" value="<%=startDateStr%>"/>至
						   	 		<input id="datepicker2"type="text" readonly="readonly" style="width:70px;" class="line" name="params.endDate" value="<%=endDateStr%>"/>
						 		</li>
						 		<!-- <li>
						 			<span class="span">制令号</span>
						 			<input type="text"  name="params.planNo_equals"/>
						 		</li>
						 		<li>
						 			<span class="span">订单号</span>
						 			<input type="text"  name="params.orderNo"/>
						 		</li> -->
						 		<li style="margin-left:20px;">
						 			<button style="padding:0px;margin:0px;width:70px;" class='btn' onclick="search()"><span><span>统计</span></span></button>&nbsp;&nbsp;
						 		</li>
						 	</ul>
						</td>
					</tr>
				</table>
			</div>
			</form>	
			<div id="opt-content">
				<table style="width:100%;">
					<tr>
						<td id="reportDiv_parent" width="100%">
							<div id='reportDiv'></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>