<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager" %>
<%@ page import="com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao" %>
<%@ include file="/common/taglibs.jsp"%>
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr1 = DateUtil.formateDateStr(calendar);
	calendar = Calendar.getInstance();
	calendar.add(Calendar.DATE,-10);
	String startDateStr1 = DateUtil.formateDateStr(calendar);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	
	calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
	calendar.add(Calendar.MONTH,-6);
	String startDateStr = DateUtil.formateDateStr(calendar.getTime(),"yyyy-MM");
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
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
	</object> 
	<script type="text/javascript">
	var LODOP; //声明打印的全局变量 
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
		$('#datepicker1').datepicker({"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
		$('#datepicker2').datepicker({"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
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
			},{
				title: {
					text: result.yAxisTitle2,
					style: {
			               color: '#000000',
			               'font-weight':'bold',
			               fontSize:18
			            },
					x : 52,
					rotation : 0
				},
				plotLines: [{
					width: 1,
					color: 'black'
				}],
		        gridLineWidth : 0,
		        tickInterval : 10,
				labels : {
					style: {
						color : 'black'
		            },
					align : 'right',
					x : width - 45,
					y : 1,
					formatter : function(){
						if(result.max==0){
							return '';
						}else if(this.value == result.max){
							return '100%';
						}else if(this.value < result.max){
							return ((this.value / result.max *  100).toFixed(0)) + '%';
						}else{
							return '';
						}
					}
				},
				min: 0
				
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
		            borderWidth:0,
		            pointPadding:0,
		            groupPadding:0.0,
		    		cursor : 'pointer',
	              	events : {
		            	click : function(obj){
		            		showDetailByDate(obj.point.name,obj.point.date);
		            	}
		            }
		        },
		        spline: {
		            lineWidth : 1,
		            shadow : true,
		            dataLabels: {
		               enabled: true,
		               formatter : function(){
		            	   return ((this.y / result.max *  100).toFixed(2)) + '%';
		               }
		            }
		        }
		    },
			tooltip: {
				formatter: function() {
					var s;
					if (this.series.type=="spline") { 
						s = this.x +'的值' + ': ' + this.y.toFixed(2) + '%';
					}else{
						return "<b>" + this.series.name + ":</b>" + this.y + "<br/><span style='font-size:12px;color:blue;'>单击查看详细</span>";
					}
					return s;
				}
			},
			series: [{
				type: 'column',
				name: result.series1.name,
				data: result.series1.data
			} ,
			{
				type: 'column',
				name: result.series2.name,
				data: result.series2.data
			},{
				type: 'spline',
				name: result.series3.name,
				data: result.series3.data,
				yAxis : 1
			}]
		});
	}
	
	//确定的查询方法
	function search(){
		var date1 = $("#datepicker1").val(); 
		var date2 = $("#datepicker2").val(); 
		if(date1>date2){
			alert("时间前后选择有误，请重新设置 !"); 
			$("#datepicker2").focus();
		}else{
			reportByParams(getParams());
		}
	}	
	
	//根据参数获取数据
	function reportByParams(params){
		$("#message").html("图表查询中,请稍候...");
		$.post("${iqcctx}/inspection-report/inspection-chart-datas.htm",params,function(result){
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
		$("#search input[type=text]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		$("#search input[type=hidden]").each(function(index,obj){
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
		$("#search select").each(function(index,obj){
			var jObj=$(obj);
			if(obj.name&&jObj.val()&&jObj.val()!=""){
				params[obj.name]=jObj.val();
			}
		});
		return params;
	}
	
	//创建表格
	var detailTable = null;
	function createDetailTable(result){
		clearTable();
		var colModel=[{name:'custom_name',width:70,index:'custom_name',align:'center',label:result.firstColName}],datas = [];
		var tableHeaderList = result.tableHeaderList;
		for(var i=0;i<tableHeaderList.length;i++){
			colModel.push({name:'date'+i,index:'date' + i,width:60,align:'center',label : tableHeaderList[i]});
		}
		var dataCheckList = result.series1.data;
		var checks1 = [];
		for(var i=0;i<dataCheckList.length;i++){
			checks1.push(dataCheckList[i].y);
		}
		var p = {id:1,custom_name:"检验批数"};
		for(var i=0;i<dataCheckList.length;i++){
			p['date' + i] = checks1[i];
		}
		datas.push(p);
		
		var dataRegularList = result.series2.data;
		var checks2 = [];
		for(var i=0;i<dataRegularList.length;i++){
			checks2.push(dataRegularList[i].y);
		}
		p = {id:1,custom_name:"合格批数"};
		for(var i=0;i<dataRegularList.length;i++){
			p['date' + i] = checks2[i];
		}
		datas.push(p);
		
		var dataRateList = result.series3.data;
		p = {id:1,custom_name:"合格率"};
		for(var i=0;i<dataRateList.length;i++){
			p['date' + i] = parseFloat(dataRateList[i]).toFixed(2)+"%";
		}
		datas.push(p);
	
		var width = $("#btnDiv").width()-30;
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader: {
				id : 'custom_name'
			},
			data: datas,
			rownumbers: true,
			width: width,
			height: 80,
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
	function showDetailByDate(inspectionPoint,inspectionDate){
		var params = getParams();
		if(inspectionPoint == '合格批数'){
			params['params.inspectionConclusion_equals'] = 'OK';			
		}
		params['params.startDate_ge_date'] = inspectionDate;
		params['params.endDate_le_date'] = inspectionDate;
		params['params.state'] = 'all';
		var url = '${iqcctx}/inspection-report/inspection-report-detail.htm?a=1';
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
	
	function materialNameClick(obj){
		var url = '${mfgctx}';
		$.colorbox({href:url+"/common/product-bom-select.htm",iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	function setBomValue(datas){
		$("#params\\.itemdutyPart_equals").val(datas[0].value);
		$("#params\\.itemdutyPartCode_equals").val(datas[0].key);
 	}
	function dutySupplierClick(obj){
		var url='${supplierctx}';
		//var url='http://localhost:8082/supplier/evaluate/supplier-select.htm';
		$.colorbox({href:url+"/archives/select-supplier.htm",iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#params\\.dutySupplier").val(obj.name);
		$("#params\\.dutySupplierCode").val(obj.code);
	}
	function goToNewLocationById(id){
		var url='${iqcctx}';
		$.colorbox({href:url+"/inspection-report/view-info.htm?id="+id,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"页面详情"
		});
		/* window.location.href = "${iqcctx}/iqc/inspection-report/input.htm?id="+id; */
	}
	function typeSelect(obj,selectId){
		$(".typeSelect").attr("disabled",true);
		$("#" + selectId).attr("disabled",false);
		if(obj.value=='month'){
			$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm',changeMonth:true,changeYear:true});
			$('#datepicker1').val("<%=startDateStr%>");
			$('#datepicker2').val("<%=endDateStr%>");
		}else{
			$('#datepicker1').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker('option',{"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			$('#datepicker1').val("<%=startDateStr1%>");
			$('#datepicker2').val("<%=endDateStr1%>");
		}
	}
	function chartprint(){
		$.chartprint({
			chart:chart
		});
	}
	function prn1_preview() {	
		CreateOneFormPage();	
	}
	function CreateOneFormPage(){
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
		LODOP.PRINT_INIT("进货检验表单打印");
		var strBodyStyle="<style>table { border: 1 solid #000000;border-collapse:collapse }</style>";
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("print").innerHTML+"</body>";
		/* var svg=chart.getSVG();;
		$.post('/amb/hight-chart/chart-image-export.htm',svg,function(){
		},'json'); */
		LODOP.SET_PRINT_STYLE("FontSize",12);
		LODOP.SET_PRINT_STYLE("Bold",1);
		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",strFormHtml);
		LODOP.ADD_PRINT_IMAGE(0,0,"100%","100%","c:/text.jpg");
		LODOP.PREVIEW();
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
	function clearAll(){
		$("#params\\.itemdutyPartCode_equals").val("");
		$("#params\\.dutySupplierCode").val("");
		document.print.reset();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="Chart";
		var thirdMenu="myInspectionChart";
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
	
	<div class="ui-layout-center" style="overflow:auto;">
		<form onsubmit="return false;" name="print" id="print">
		<div class="opt-body">
			<div class="opt-btn"></div>	
			<div id="search" style="display:block;padding-right:2px;">
				<table class="form-table-outside-border" style="width:100%;">
					<tr>
						<td>
							<input type="radio" name="params.myType" id="month" onclick="typeSelect(this,'month');" value="month" checked="checked"  style="float:left;"/><label for="month"  style="float:left;">月份</label>
							<input type="radio" id="date" name="params.myType" onclick="typeSelect(this,'date');"  style="float:left;" value="date"/><label  style="float:left;" for="date">日期</label>
							<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px;float:left;margin-left:4px;" class="line" value="<%=startDateStr%>"/>
							<span style="float:left;margin-left:4px;">至</span>
						    <input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px;float:left;margin-left:4px;" class="line" value="<%=endDateStr%>"/>
						    <span style="margin-left:4px;float:left;">事业部</span>
						    <s:select list="businessUnits" 
											  theme="simple"
											  listKey="businessUnitCode" 
											  listValue="businessUnitName" 
											  name="params.businessUnitCode_equals"
											  labelSeparator=""
											  cssStyle="float:left;margin-left:4px;"
											  emptyOption="true"></s:select>
							<span style="margin-left:4px;float:left;">单据类型</span>
						    <select name="params.formType_equals" style="float:left;margin-left:4px;">
												<option value=""></option>
												<option value="入库单">入库单</option>
												<option value="退货单">退货单</option>
											</select>
							<span style="margin-left:4px;float:left;">物料大类</span>
							<input id="checkBomMaterialLevels" name="checkBomMaterialLevels" onclick="showAllMateriel();"/>
							<input type="hidden" id="params.checkBomMaterialLevels_in" name="params.checkBomMaterialLevels_in"/>
						   <%--  <s:select list="materialTypes" 
											  theme="simple"
											  listKey="value" 
											  listValue="name"
											  cssStyle="float:left;margin-left:4px;" 
											  name="params.checkBomMaterialType_equals"
											  labelSeparator=""
											  emptyOption="true"></s:select> --%>
							<span style="margin-left:4px;float:left;">供应商</span>
							<input type="text" onclick="dutySupplierClick();" id="params.dutySupplier" name="params.supplierName_like" style="width:170px;float:left;margin-left:4px;"/>
							<input type="hidden" id="params.dutySupplierCode" name="params.supplierCode_equals"/>
							<a class="small-button-bg" style="margin-left:2px;float:left;margin-top:2px;" onclick="dutySupplierClick(this)" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">
							<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
							<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						 	<button class='btn' type="button" onclick="clearAll();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
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
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>