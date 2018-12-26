<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar);
	calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	String startDateStr = DateUtil.formateDateStr(calendar);
    //物料大类
    ActionContext.getContext().put("materialTypes",ApiFactory.getSettingService().getOptionsByGroupCode("IQC_MATERIAL_TYPE"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript">
		var startTime = 0;
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
					x:0 //center
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
					maxPadding : 0,
					gridLineWidth : 1,
					gridLineDashStyle : 'ShortDashDot',
					min: 0,
					max:result.max
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
			$.post("${iqcctx}/inspection-statistics/check-type-chart-datas.htm",params,function(result){
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
				var jObj = $(obj);
				if(jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			$("#search select").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()&&jObj.val()!=""){
					params[obj.name] = jObj.val();
				}
			});
			params['params.myType']='date';
			return params;
		}
		
		//创建表格
		var detailTable = null;
		function createDetailTable(result){
			clearTable();
			var colModel = result.colModel,datas = result.tabledata;
			var width1 = $(".opt-btn").width();
			detailTable = $("#detail_table").jqGrid({
				datatype: "local",
				localReader : {
					id : 'custom_name'
				},
				data: datas,
				rownumbers: true,
				width: width1,
				height: 90,
				colModel: colModel,
			    multiselect: false,
			   	autowidth: true,
				forceFit: true,
				shrinkToFit: false,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete: function(){}
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
		
		//选择供应商
		function selectSupplier(){
			$.colorbox({
				href:"${supplierctx}/archives/select-supplier.htm",
				iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		
		function setSupplierValue(objs){
			var obj = objs[0];
			$("#params\\.supplierName_equals").val(obj.name);
			$("#params\\.supplierCode_equals").val(obj.code);
		}
		
		//选择零部件
		function selectBom(){
			var url = '${carmfgctx}';
			$.colorbox({href:url+"/common/product-bom-select.htm",iframe:true, 
				width:$(window).width()<900?$(window).width()-100:900, 
				innerHeight:$(window).height()<500?$(window).height()-100:500,
	 			overlayClose:false,
	 			title:"选择零部件"
	 		});
	 	}
		
		function setBomValue(datas){
			$("#params\\.checkBomName_equals").val(datas[0].value);
			$("#params\\.checkBomCode_equals").val(datas[0].key);
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
		function formReset(){
			$("#params\\.supplierCode_equals").val("");
			$("#params\\.checkBomCode_equals").val("");
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="statAnalyse";
		var thirdMenu="myCheckItemChart";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-statistics-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow:auto;">
		 	<div class="opt-btn"><span style="margin-left:4px;color:red;" id="message"></span></div>	
			<div id="search" style="display:block;padding: 2px;">
				<form action="" onsubmit="return false;">
					<table class="form-table-outside-border" style="width:100%;">
						<tr>
							<td>
								<span style="float:left;width:70px;text-align:right;padding-right:2px;">检验日期</span>
								<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>
							        至<input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:70px" class="line" value="<%=endDateStr%>"/>
						    </td>
						    <td>
						    	<span style="float:left;width:70px;text-align:right;padding-right:2px;">供应商</span>
								<input type="text" name="params.supplierName_equals" id="params.supplierName_equals" style="width:170px;" readonly="readonly" onclick="selectSupplier();"/>
						 		<input type="hidden" name="params.supplierCode_equals" id="params.supplierCode_equals"/>
							</td>
							<td>
								<span style="float:left;width:70px;text-align:right;padding-right:2px;">零部件</span>
								<input type="text" name="params.checkBomName_equals" id="params.checkBomName_equals" style="width:170px;" readonly="readonly" onclick="selectBom();"/>
						 		<input type="hidden" name="params.checkBomCode_equals" id="params.checkBomCode_equals"/>
							</td>
						</tr>
						<tr>
						    <td>
								<span style="float:left;width:70px;text-align:right;padding-right:2px;">零件类别</span>
								<s:select list="materialTypeOptions" id="materialType"
										  theme="simple"
										  cssStyle="width:172px;"
										  listKey="value" 
										  listValue="value" 
										  name="params.checkBomMaterialType_equals"
										  labelSeparator=""
										  emptyOption="true">
								</s:select>
							</td>
							<td>
								<span style="float:left;width:70px;text-align:right;padding-right:2px;">车型</span>
								<s:select id="modelSpecification"
									  list="modelSpecifications" 
									  cssStyle="width:172px;"
									  emptyOption="true"
									  name="params.modelSpecification_equals"
									  theme="simple"
									  listKey="value" 
									  listValue="value">
								</s:select>
							</td>
							<td style="text-align: right;">
								<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
								<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							 	<button class='btn' type="button" onclick="reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
							 	<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_CHECK-TYPE-CHART_EMAIL">
							 		<button class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>&nbsp;
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
</html>