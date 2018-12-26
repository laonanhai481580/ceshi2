<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager" %>
<%@ page import="com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao" %>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/calendar/WdatePicker.js"></script>
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
			$('#datepicker1').click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$('#datepicker2').click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
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
						text: result.yAxisTitle1,
						  stackLabels: {
					            enabled: true,
					            style: {
					               fontWeight: 'bold',
					               color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
					            }
					         },
						style : {
				               'font-weight':'bold',
				               fontSize:15,
				               color : 'black'
							},
						rotation : 0
					},
					plotLines: [{
						value: 12,
						color: 'black'
					}],
					labels : {
						align : 'right'
					},
					maxPadding : 0,
					gridLineWidth : 1,
					gridLineDashStyle : 'ShortDashDot',
					min: 0
				}],
				legend: {
			         align: 'left',
			         verticalAlign : 'top',
			         floating: true,
			         backgroundColor: '#FFFFFF',
			         x : 45,
			         y : 0
			    },
			    plotOptions: {
			        spline: {
			            lineWidth : 1,
			            shadow : true,
			            dataLabels: {
			               enabled: true,
			               formatter : function(){
			            	   return ((this.y / result.max *  100).toFixed(0)) + '%';
			               }
			            }
			        }
			    },
				tooltip: {
					formatter: function() {
						var s;
						if (this.series.type=="spline") { 
							s = '第'+
							this.x+'天'  +': '+ this.y+'%';
						}else{
							return "<b>" + this.series.name + ":</b>" + this.y + "<br/><span style='font-size:12px;color:blue;'>单击查看详细</span>";
						}
						return s;
					}
				},
				series: [{
					type: 'spline',
					name: result.series3.name,
					data: result.series3.data,
					yAxis : 0
				}]
			});
		}
		
		//确定的查询方法
		function search(){
			reportByParams(getParams());
		}	
		
		//根据参数获取数据
		function reportByParams(params){
			//$("#tableDiv").load("${ctx}/inspection-report/inspection-chart-table.htm",params);
			$.post("${mfgctx}/efficient-management/ontime-manager/efficient-chart-datas.htm",params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					createDetailTable(result);
					createReport(result);
					cacheResult = result;
				}
			},'json');
		}
		
		//获取表单的值
	function getParams(){
	var params = {};
	$("#search  input[type=text]").each(function(index,obj){
		var jObj = $(obj);
		if(obj.name&&jObj.val()){
		params[obj.name] = jObj.val();
				}
			});
	$("#search  input[type=radio]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&obj.checked&&jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			
	$("#search select").each(function(index,obj){
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
		//alert(result.series1.data);
		clearTable();
		var colModel=[{name:'custom_name',width:70,index:'custom_name',align:'center',label:'日期'}],datas = [];
		var tableHeaderList = result.tableHeaderList;
		for(var i=0;i<tableHeaderList.length;i++){
			colModel.push({name:'date'+i,index:'date' + i,width:45,align:'center',label : tableHeaderList[i]});
		}
		
		var dataRateList = result.series3.data;
		p = {id:1,custom_name:"效率"};
		for(var i=0;i<dataRateList.length;i++){
			p['date' + i] = parseFloat(dataRateList[i]).toFixed(1)+"%";
		}
		datas.push(p);
	
		var width = $("#btnDiv").width()-30;
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader : {
				id : 'custom_name'
			},
			data: datas,
			rownumbers : true,
			width : width,
			colModel:colModel,
		    multiselect: false,
		   	autowidth: true,
			forceFit : true,
			shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){
				
			}
		});
	}
		
	//清除表格
	function clearTable(){
		if(detailTable != null){
			detailTable.GridDestroy();
			detailTable = null;
		}
		$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table>");
	}
	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="efficientManagement";
	var thirdMenu="ontimeManager";
	var treeMenu="efficientChartThreeMenu";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-efficient-management-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center" style="overflow:auto;">
		<form action="">
		<div class="opt-body">
		<div class="opt-btn"></div>	
			<div id="search" style="display:block;"  >
				<table style="width:100%;display:block;">
					<tr>&nbsp;&nbsp;</tr>
					<tr>
						<td>检验日期:</td>
						<td>
							<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:80px" class="line" value="2012-04-01"/>至
						    <input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:80px" class="line" value="2012-04-30"/>
					   	</td>
						<td>生产线:</td>
						<td>
						<s:select list="productionLines" 
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="params.productionLine"
								  labelSeparator=""
								  emptyOption="true"
								  headerValue="焊装"
								  headerKey="焊装" ></s:select>
						</td>
						<td style="text-align: right;">
							<button class='btn' type="button" onclick="search();"><span><span>统计</span></span></button>
							<button class='btn' type="reset"><span><span>取消</span></span></button>
						</td>
					</tr>
				</table>
			</div>
			<div>
				<table style="width:98%;">
					<tr>
						<td id="reportDiv_parent">
							<div id='reportDiv'></div>
						</td>
					</tr>
					<tr>
						<td id="detailTableDiv_parent">
							<table id="detailTable"></table>
						</td>
					</tr>
				</table>
			</div>
		</div>
		</form>
	</div>
	
</body>
</html>