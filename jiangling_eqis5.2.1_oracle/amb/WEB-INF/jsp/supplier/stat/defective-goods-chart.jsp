<%@ page contentType="text/html;charset=UTF-8" import="java.util.Date"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page import="java.io.File"%>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
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
			$('#datepicker1').datepicker({changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker({changeMonth:true,changeYear:true});
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
						}
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
		//$("#tableDiv").load("${ctx}/defective-goods/defective-goods-table.htm",params);
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
		//alert(result.series1.data);
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
	
		var w = $('.ui-layout-center').width()-20;
		if($.browser.msie){
			var obj=document.getElementById("ui-layout-center"); 
			if(obj!=null&&typeof(obj)!='undefined'){
				if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
					w = $('.ui-layout-center').width()-45;
				} 
			}
		}
		var width = w;
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader : {
				id : 'custom_name'
			},
			data: datas,
			rownumbers : true,
			width : width,
			height: 25,
			colModel:colModel,
			multiselect: false,
			autowidth: false,
			forceFit : true,
			shrinkToFit: false,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){}
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
	
	//查看明细的方法
	function showDetailByDate(searchname){
		var params = getParams();
		params['params.searchname'] = searchname;
		var url = '${iqcctx}/inspection-report/inspection-report-detail.htm?1=1';
		for(var pro in params){
			url += "&" + pro + "=" + params[pro];
		}
		//alert(url);
		$.colorbox({href:encodeURI(url),iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
 			overlayClose:false,
 			title:"台帐明细"
 		});
	}
	function goToNewLocationById(id){
		var url='${iqcctx}';
		$.colorbox({href:url+"/inspection-report/view-info.htm?id="+id,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
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
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="stat";
		var thirdMenu="myDefectiveChart";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-stat-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center" style="overflow:auto;">
		<%
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			String startDateStr = sdf.format(calendar.getTime());

			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, -1);
			String endDateStr = sdf.format(calendar.getTime());
		%>
		<form onsubmit="return false;">
		<div class="opt-body" >
			<div class="opt-btn" id="btn" style="line-height:33px;">
			<span style="margin-left:4px;color:red;" id="message"></span>
			</div>	
			<div id="search" style="display:block;padding:4px;">
				<input type="hidden" name="params.myType" value="date"/>
				<table class="form-table-outside-border" style="width:100%">
					<tr>
						<td style="text-align:right;width:70px;">检验日期</td>
						<td>
							<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>至
							<input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
						</td>
						<td>
							<input type="radio" name="params.type" value="materiel" checked="checked"></input>不合格物料
							<input type="radio" name="params.type" value="supplier"></input>供应商
							<input type="radio" name="params.type" value="inspector"></input>检验员
							<input type="radio" name="params.type" value="result"></input>处理结果
						</td>
					</tr>
					<tr>
						<td colspan="3" style="text-align:right">
							<security:authorize ifAnyGranted="stat-defective-chart">
							<button class="btn" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
							</security:authorize>
							<button class="btn" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							<button class="btn" type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>&nbsp;
						</td>
					</tr>
				</table>
			</div>
			<div>
				<table style="width:100%;">
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