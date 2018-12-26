<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.iqc.entity.IncomingInspectionActionsReport" %>
<%@ page import="com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager" %>
<%@ page import="com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao" %>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
	<script type="text/javascript">
	var startTime = 0;
	function contentResize(){
		if(cacheResult != null){
			clearTable();
			createReport(cacheResult);
			createDetailTable(cacheResult);
		}
	}
	function formReset(){
		$("#checkBomMaterialType").multiselect("uncheckAll");
		$("#businessUnitCode").multiselect("uncheckAll");
	}
	var cacheResult = null;
		$(document).ready(function(){
			contentResize();
			$('#datepicker1').datepicker({changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker({changeMonth:true,changeYear:true});
			$("#checkBomMaterialType").multiselect({selectedList:1});
			$("#checkBomMaterialType").multiselect("uncheckAll");
			$("#businessUnitCode").multiselect({selectedList:1});
			$("#businessUnitCode").multiselect("uncheckAll");
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
					x:0 //center
				},
				xAxis: {
					categories: result.categories,
					gridLineDashStyle : 'ShortDashDot',
					gridLineWidth: 1,
					labels : {
						style : {
							"color": 'black'
						},
						rotation:-15
					}
				},
				yAxis: [{
					title: {
						text: result.yAxisTitle,
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
					//maxPadding : 0,
					//gridLineWidth : 1,
					gridLineDashStyle : 'ShortDashDot'
					//min: 0
				}],
				legend: {
			         align: 'right',
			         verticalAlign : 'top',
			         floating: true,
			         backgroundColor: '#FFFFFF',
			         x : 45,
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
			            lineWidth : 0,
			            shadow : true,
// 			            pointInterval : 0,
			            borderWidth:0,
			            pointPadding:0,
			            groupPadding:0.0,
			          	cursor : 'pointer',
		               	events : {
			            	click : function(obj){
		            			showDetailByDate(obj.point.type,obj.point.count);
			            	}
			            }
				        },
				        spline: {
				            lineWidth : 1,
				            shadow : true,
				        }
			    },
				tooltip: {
					formatter: function() {
						return "<b>" + this.series.name + ":</b>" + this.y + "<br/><span style='font-size:12px;color:blue;'>单击查看详细</span>";
					}
				},
				series: [{
					type: 'column',
					name: result.series.name,
					data: result.series.data
				}]
			},function(){
				var endTime = (new Date()).getTime();
//                 alert ('rendered:' + (endTime - startTime));
			});
		}
		
		//确定的查询方法
		function search(){
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			if(date1>date2){
				alert("日期时间前后设置有误，请重新选择！");
				$("#datepicker1").focus();
			}else{
				reportByParams(getParams());
			}
		}	
		
		//根据参数获取数据
		function reportByParams(params){
			$("#message").html("图表查询中,请稍候...");
			startTime = (new Date()).getTime();
			$.post("${iqcctx}/inspection-report/check-type-chart-datas.htm",params,function(result){
				$("#message").html("");
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
			$("#search :input[name]").each(function(index,obj){
				if(obj.name.indexOf("multiselect_")==0){
					return;
				}
				var jObj = $(obj);
				var value = jObj.val();
				if(obj.type=='radio'){
					if(obj.checked&&value){
						params[obj.name] = value;
					}
				}else if(obj.type == 'select-one'){
					var multiselect = jObj.attr("multiselect");
					//多选框
					if(multiselect == 'true'){
						value = "";
						$(":input[name=multiselect_"+obj.id+"]:checked").each(function(index,obj){
							if(value){
								value += ",";
							}
							value += obj.value;
						});
					}
					if(value){
						params[obj.name] = value;
					}
				}else if(value){
					params[obj.name] = value;
				}
			});
// 			params['params.myType']='date';
			return params;
		}
		
		//创建表格
		var detailTable = null;
		function createDetailTable(result){
			clearTable();
			var colModel = result.colModel,datas = result.tabledata;
			var width = $(".ui-layout-center").width()-25;
			detailTable = $("#detail_table").jqGrid({
				datatype: "local",
				localReader : {
					id : 'custom_name'
				},
				data: datas,
				rownumbers: true,
				width: width,
				height: 90,
				colModel: colModel,
			    multiselect: false,
			   	autowidth: true,
				forceFit: true,
				shrinkToFit: false,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete: function(){}
			}).jqGrid("setGridWidth",width);
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
		function showDetailByDate(typeName,type){
			var params = getParams();
			params['params.iteminspectionType_equals'] = typeName;
			params['params.itemconclusion_equals'] = 'NG';
			var url = '${iqcctx}/inspection-report/inspection-report-detail.htm?a=1';
			for(var pro in params){
				url += "&" + pro + "=" + params[pro];
			}
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
	    		message:$("#message"),
	    		width:$("#reportDiv").width(),
	    		height:$("#reportDiv").height()
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
		
		function showAllMateriel(){
			var url="${iqcctx}/inspection-report/materiel-node-load.htm";
			$.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:$(window).width()<350?$(window).width()-50:350, 
				innerHeight:$(window).height()<400?$(window).height()-50:400,
	 			overlayClose:false,
	 			title:"物料级别"
	 		});
		}
		
		function showCheckedMateriel(materielName,checkedId,information){
			var html='<span style="text-align: left;">物料级别信息:'+information+"</span>";
			var materielNameStr=materielName.substring(0,materielName.length-1);
			var checkedIdStr=checkedId.substring(0,checkedId.length-1);
			$("#materialLevels").val(materielNameStr);
			$("#materialLevels").next().val(checkedIdStr);
			$("#materialTypeInformation").html(information);
		}
	</script>
</head>
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar);
	calendar = Calendar.getInstance();
	calendar.add(Calendar.DATE,-10);
	String startDateStr = DateUtil.formateDateStr(calendar);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	
	
	calendar = Calendar.getInstance();
	String endDateStr1 = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
	calendar.add(Calendar.MONTH,-1);
	String startDateStr1 = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
    //物料大类
    ActionContext.getContext().put("materialTypes",ApiFactory.getSettingService().getOptionsByGroupCode("IQC_MATERIAL_TYPE"));
%>
<script type="text/javascript">
	function typeSelect(obj,selectId){
		$(".typeSelect").attr("disabled",true);
		$("#" + selectId).attr("disabled",false);
		if(obj.value=='month'){
			$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$('#datepicker1').val("<%=startDateStr1%>");
			$('#datepicker2').val("<%=endDateStr1%>");
		}else{
			$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			$('#datepicker1').val("<%=startDateStr%>");
			$('#datepicker2').val("<%=endDateStr%>");
		}
	}
</script>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="Chart";
		var thirdMenu="myCheckTypeRateChart";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-chart-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow:auto;">
		 	<div class="opt-btn"><span style="margin-left:4px;color:red;" id="message"></span></div>	
			<div id="search">
				<form action="" onsubmit="return false;">
				<table class="form-table-outside-border" style="width:100%;">
					<tr>
						<td style="width:40%;">
							<div style="float:left;margin-left:4px;">
								<span style="margin-left:4px;">检验</span>
<%-- 								<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>至 --%>
<%-- 							    <input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/> --%>
								<input type="radio" id="date" name="params.myType" onclick="typeSelect(this,'date');" value="date" checked="checked"/><label for="date">日期</label>
								<input type="radio" name="params.myType" id="month" onclick="typeSelect(this,'month');" value="month"  /><label for="month">月份</label>
								<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>至
							    <input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
								
							</div>
						</td>
						<td style="width:30%;">
							<div style="float:left;margin-left:4px;">
								<span style="margin-left:4px;">事业部</span>
						    	<s:select list="businessUnits" 
											  theme="simple"
											  listKey="businessUnitCode" 
											  listValue="businessUnitName" 
											  name="params.businessUnitCode_in"
											  id="businessUnitCode"
											  multiselect="true"></s:select>
							</div>
						</td>
						<td style="width:30%;">
							<div style="float:left;margin-left:4px;">
								<span style="margin-left:4px;">单据类型</span>
						    	<select name="params.formType_equals">
									<option value=""></option>
									<option value="入库单">入库单</option>
									<option value="退货单">退货单</option>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div style="float:left;margin-left:4px;">
								<span style="margin-left:4px;">物料级别</span>
								<input id="materialLevels" name="materialLevels" onclick="showAllMateriel();"/>
								<input type="hidden" id="params.checkBomMaterialLevels" name="params.checkBomMaterialLevels"/>
							</div>
						</td>
						<td>
							<div style="float:left;margin-left:4px;">
								<span style="margin-left:4px;">物料类别</span>
						    	<s:select list="materialTypes" 
									  theme="simple"
									  listKey="value" 
									  listValue="name" 
									  name="params.checkBomMaterialType_in"
									  id="checkBomMaterialType"
									  multiselect="true"></s:select>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="4" style="text-align: right;" valign="bottom">
							<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_CHECK-TYPE-CHART_STATA">
								<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
							</security:authorize>
								<button class='btn' type="button" onclick="javascript:this.form.reset();formReset()"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
							<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_CHECK-TYPE-CHART_EXPORT">
								<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							</security:authorize>
							<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_CHECK-TYPE-CHART_EMAIL">
							 	<button  class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>&nbsp;
							</security:authorize>
						</td>
					</tr>
				</table>
				</form>
			</div>
			<div>
				<table style="width:98.5%;">
					<tr>
						<td id="reportDiv_parent">
							<div id='reportDiv'></div>
						</td>
					</tr>
					<tr>
						<td style="text-align: left;" id="materialTypeInformation" >
							
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
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>
