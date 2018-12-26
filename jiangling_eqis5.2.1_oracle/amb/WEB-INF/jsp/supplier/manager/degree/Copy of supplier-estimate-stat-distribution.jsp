<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.sql.Date"%>
<%@page import="java.io.File"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript">
		var myLayout;
		$(document).ready(function(){
			myLayout = $('body').layout({
				north__paneSelector : '#header',
				north__size : 66,
				west__size : 250,
				north__spacing_open : 31,
				north__spacing_closed : 31,
				west__spacing_open : 6,
				west__spacing_closed : 6,
				center__minSize : 400,
				resizable : false,
				paneClass : 'ui-layout-pane',
				north__resizerClass : 'ui-layout-resizer',
				west__onresize : $.layout.callbacks.resizePaneAccordions,
				center__onresize : contentResize
			});
		});
	</script>
	<script type="text/javascript">
	function exportChart(){
		$.exportChart({
    		chart:chart,
    		grid:$("#detail_table"),
    		message:$("#message")
    	});
	}
	
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
			var evaluateYear = '${evaluateYear }';
			$("#params\\.evaluateYear option[value="+evaluateYear+"]").attr('selected','selected');
			$('#datepicker1').datepicker({changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker({changeMonth:true,changeYear:true});
			$("#detail_table").jqGrid('setGroupHeaders', {
				useColSpanStyle: true, 
				groupHeaders:[{startColumnName:'A级', numberOfColumns: 4, titleText: '供应商等级'}]
			});
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
						"font-size": '16px'
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
				            		//alert(obj.point.searchname);
				            		showDetailByDate(obj.point.importance);
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
								this.x  +': '+ this.y
								+ "<br/><span style='font-size:12px;color:blue;'>单击查看详细</span>";
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
			reportByParams(getParams());
		}	
		
		//根据参数获取数据
		function reportByParams(params){
		params.type = '${type}';
	 	$.post("${supplierctx}/manager/degree/supplier-estimate-stat-distribution-datas.htm",params,function(result){
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
		function getParams() {
			var params = {};
			$("#opt-btn select").each(function(index, obj) {
				var jObj = $(obj);
				if (obj.name && jObj.val()) {
					params[obj.name] = jObj.val();
				}
			});
			return params;
		}

		//创建表格
		var detailTable = null;
		function createDetailTable(result) {
			//alert(result.series1.data);
			clearTable();
			var colModel = result.colModel, datas = result.tabledata;
			// 		var tableHeaderList = result.tableHeaderList;
			// 		for(var i=0;i<tableHeaderList.length;i++){
			// 			colModel.push({name:'date'+i,index:'date' + i,width:120,align:'center',label : tableHeaderList[i]});
			// 		}
			detailTable = $("#detail_table").jqGrid({
				datatype : "local",
				localReader : {
					id : 'supplierType'
				},
				data : datas,
				rownumbers : true,
				height : 150,
				colModel : colModel,
				multiselect : false,
				autowidth : true,
				forceFit : true,
				shrinkToFit : true,
				viewrecords : true,
				sortorder : "desc",
				gridComplete : function() {
					$("#detail_table").jqGrid('setGroupHeaders', {
						useColSpanStyle : true,
						groupHeaders : [ {
							startColumnName : 'A级',
							numberOfColumns : 4,
							titleText : '供应商等级'
						} ]
					});
				}
			});
		}

		$("#detail_table").jqGrid('setGroupHeaders', {
			useColSpanStyle : true,
			groupHeaders : [ {
				startColumnName : 'A级',
				numberOfColumns : 4,
				titleText : '供应商等级'
			} ]
		});

		//清除表格
		function clearTable() {
			if (detailTable != null) {
				detailTable.GridDestroy();
				detailTable = null;
			}
			$("#detailTableDiv_parent").html(
					"<table id=\"detail_table\"></table>");
		}

		//查看明细的方法
		function showDetailByDate(importance) {
			var params = getParams();
			params['params.importance'] = importance;
			var url = '${supplierctx}/manager/warning-sign-view.htm?type=${type}';
			for ( var pro in params) {
				url += "&" + pro + "=" + params[pro];
			}
			//alert(url);
			$.colorbox({
				href : encodeURI(url),
				iframe : true,
				innerWidth : 800,
				innerHeight : 500,
				overlayClose : false,
				title : "供应商详情"
			});
		}
		function yearChange(obj) {
			reportByParams(getParams());
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="mySupplierEstimateStatDistribution";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west" id="west-ui">
		<%
			request.setAttribute("selLevel",1);
		%>
		<%@include file="../../evaluate/quarter/left.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow:auto;">
			 	<div class="opt-btn" id="opt-btn">
			 	<table cellpadding="0" cellspacing="0" width="100%" height="100%">
					<tr>
						<td valign="middle" style="padding: 0px; margin: 0px; padding-left: 8px;">
							评价年份&nbsp;
							<select name="params.evaluateYear" id="params.evaluateYear"  onchange="yearChange(this)" style="width:60px;">
								<option value="2014">2014</option>
								<option value="2015">2015</option>
								<option value="2016">2016</option>
								<option value="2017">2017</option>
								<option value="2018">2018</option>
								<option value="2019">2019</option>
								<option value="2020">2020</option>
								<option value="2021">2021</option>
								<option value="2022">2022</option>
								<option value="2023">2023</option>
								<option value="2024">2024</option>
							</select>
							评价月份&nbsp;
							<select name="params.evaluateMonth" id="params.evaluateMonth"  onchange="yearChange(this)" style="width:60px;">
								<option value="0"></option>
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
								<option value="8">8</option>
								<option value="9">9</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
							</select>
						</td>
						<td><span style="margin-left:4px;color:red;" id="message"></span></td>
						<td style="text-align:right;">
							<security:authorize ifAnyGranted="manager-product-exploitation-datas">
							<button class="btn"  onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>&nbsp;
							</security:authorize>
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
			
		</div>
	
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>