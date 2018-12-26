<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.util.List"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%>
<%@page import="com.ambition.spc.entity.FeatureLayer"%>
<%@page import="com.ambition.spc.entity.LayerType"%>
<%@page import="com.ambition.spc.entity.LayerDetail"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<style>
	#searchUl2{
		margin:0px;
		padding:0px;
	}
	#searchUl2 .li{
		float:left;
		width:260px;
		height:24px;
		list-style:none;
	}
	#searchUl2 .liSpan{
		float:left;
		width:80px;
		text-align:right;
		padding-right:2px;
	}
	</style>
<%@include file="/common/meta.jsp"%>
<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
<script type="text/javascript">
	function contentResize(){
		if(cacheResult != null){
			createReport(cacheResult);
			createDetailTable(cacheResult);
		}
	}
	
	var chart = null,cacheResult = null,searchParams = null;
	$(document).ready(function(){
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		searchParams = getParams();
		reportByParams(searchParams);
	});
	//确定的查询方法
	function search(){
		searchParams = getParams();
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("日期前后选择有误,请重新设置!");
			$("#datepicker1").focus();
		}else{
			reportByParams(searchParams);
		}
	}
	//根据参数获取数据
	function reportByParams(params){
		$("#reportDiv").html("图表生成中,请稍后....");
		params = params || searchParams;
		var tagValue = "";
		$(":input","#opt-content").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&obj.type=='select-one'){
				if(obj.id&&!obj.disabled){
					var values = $("#" + obj.id).multiselect("getChecked").map(function(){
					   return this.value;	
					}).get();
					if(values){
						var val = values.toString();
						if(val){
							tagValue = val;
						}
					}
				}
			}
		});
		var url = "${spcctx}/statistics-analysis/spc-application-datas.htm";
		if(tagValue.length>0){
			url += "?tagValue="+tagValue;
		}
		$.post(url,params,function(result){
				try{
					if(result.error){
						alert(result.message);
					}
				}catch (e) {
				}
				createReport(result);
				createDetailTable(result);
				cacheResult = result;
				$("#featureCount").val(result.featureCount);
				$("#subGroupCount").val(result.subGroupCount);
//				window.location = "#typeTable";
		},'json');
	}
	//创建表格
	var detailTable = null;
	function createDetailTable(result){
		if(detailTable != null){
			detailTable.GridDestroy();
			detailTable = null;
		}
		$("#detail_table_parent").html("<table id=\"detail_table\"></table><div id=\"page\"></div>");
		$("#detail_table").jqGrid({
			datatype: "local",
			data: result.myTableData,
			height : $("#reportDiv").height()-45,
			rownumbers : true,
			colNames:['','CPK范围','质量特性数量','比率'], 
			colModel:[ 
				{name:'id',index:'id',width:1,hidden:true}, 
				{name:'name',index:'name',width:80}, 
				{name:'total',index:'total',width:60,align:'center'}, 
				{name:'bi1',index:'bi1',width:90,align:'center'}
	        ],
		    multiselect: false,
		   	autowidth: true,
			forceFit : true,
		   	shrinkToFit: true,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){}
		});
		$("#detail_table").jqGrid('setGroupHeaders', {
			useColSpanStyle: true, 
			groupHeaders:[
				{startColumnName: 'total', numberOfColumns: 2, titleText: 'CPK水平分布'}
			]
		});
	}
	//创建饼图
	function createReport(result){
		if(chart != null){
			try {
				chart.destroy();
				chart = null;
			} catch (e) {
				chart = null;
			}
		}
		setTimeout(function(){
			var myData = [];
			if(result.categories){
				for(var i=0;i<result.tableData.length;i++){
					var sourceData = result.series.data[i];
					var d = {};
					d.name = result.categories[i];
					d.y = sourceData.y;
					d.arg = sourceData.arg;
					if(i == 0){
						d.sliced = 'true';
						d.selected = 'true';
					}
					myData.push(d);
				}
// 				alert($.param(myData));
			}
			var total = null,count = null,rate = null;
			total = result.total;
			count = result.featureCount;
			if(total && count && count != 0){
				rate = total/count;
			}else{
				rate = 0;
			}
			chart = new Highcharts.Chart({
				chart: {
					renderTo: "reportDiv",
					plotBackgroundColor: null,
					plotBorderWidth: null,
					plotShadow: false,
					height:360
				},
				title: {
					text: result.title
				},
				tooltip: {
					formatter: function() {
						return '<b>'+ this.point.name +'</b>: '+ (this.percentage*rate).toFixed(2) +'%';
					}
				},
				exporting : {
					enabled : false
				},
				credits: {
			         enabled: false
				},
				plotOptions: {
					pie: 
					{
						cursor: 'pointer',
						dataLabels: {
							enabled: true,
							color: '#000000',
							connectorColor: '#000000',
							formatter: function() {
								return '<b>'+ this.point.name +'</b>: '+ (this.percentage*rate).toFixed(2) +'%';
							}
						},
						showInLegend: true,
						events : {
			            	click : function(obj){
			            		showDetailByArg(obj.point.name);
			            	}
			            }
					}
				},
				series: [{
					type: 'pie',
					name: 'Browser share',
					data: myData
				}]
			});
			$("#detail_table").jqGrid("setGridHeight",$("#reportDiv").height()-45);
		},10);
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#opt-content").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	
	function selectProcess(obj){
		$.colorbox({href:"${spcctx}/common/process-tree-select.htm",
			iframe:true, 
			innerWidth:350, 
			innerHeight:400,
			overlayClose:false,
			title:"选择过程范围"
		});
	}
	
	function setProcessRange(datas){
		var range = "";
		var rangeIds = "";
		for(var i=0;i<datas.length;i++){
			var obj = datas[i];
			range += obj.processName+',';
			rangeIds += obj.id+',';
		}
		range = range.substring(0, range.length-1);
		rangeIds = rangeIds.substring(0, rangeIds.length-1);
		$("#processRange").val(range);
		$("#processId").val(rangeIds);
// 		$("#title").val(datas[0].processName+"-应用状况报表");
	}
	
	function showDetailByArg(obj){
		var params = getParams();
		params['params.range'] = obj;
		var url = '${spcctx}/statistics-analysis/spc-application-detail.htm?a=1';
		for(var pro in params){
			url += "&" + pro + "=" + params[pro];
		}
		$.colorbox({href:encodeURI(url),iframe:true, 
			innerWidth:$(window).width()<1200?$(window).width()-50:1200, 
			innerHeight:$(window).height()<580?$(window).height()-50:$(window).height()-50,
 			overlayClose:false,
 			title:"SPC应用状况明细"
 		});
	}
	//统计改变的方法
	function targetSelect(obj,selectId){
		var aa=$(".targerSelect");
		$(".targerSelect").multiselect("disable");
		$(".targerSelect").attr("disabled","disabled");
		var a=$("#" + selectId);
		var type=a.attr("type");
		if(type=='select-one'){
			$("#" + selectId).multiselect("enable");
		}else{
			a.attr("disabled","");
		}
		
	}
	function setFormWidth(){
		var width = _getTableWidth()-16;
		var total = parseInt(width/260);
		var addWidth = parseInt(width/total);
		addWidth = addWidth < 260?260:addWidth;
		$("#searchUl .li").width(addWidth);
	}
</script>

</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_spc_process";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-statistics-analysis-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<form id="contentForm" method="post" onsubmit="return false;">
					<div class="opt-btn" id="btnDiv" style="line-height:33px;">
						<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						<button class="btn" onclick="reset();$('#processId').val('');"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
<%-- 						<span style="color:red;" id="message"><s:actionmessage theme="mytheme"/></span> --%>
					</div>
					<div id="opt-content">
						<table class="form-table-outside-border" style="width:100%;height: 30px;">
							<tr>
								<td style="padding: 0px;width:75px; margin: 0px;" >
									<span>
										日&nbsp;&nbsp;期
										<input id="datepicker1" type="text" readonly="readonly" style="width:80px;border: none;" name="params.startDate_ge_date" value="<%=startDateStr%>"/>&nbsp;至&nbsp;<input id="datepicker2" type="text" readonly="readonly" style="width:80px;border: none;" name="params.endDate_le_date" value="<%=endDateStr%>"/>
									</span>
								</td>
								<td style="padding: 0px;margin: 0px;" >
									<span>
										过程范围<input style="border: none;" id="processRange" name="params.processRange" readonly="readonly" onclick="selectProcess(this);"/>
										<input id="processId" name="params.processId" readonly="readonly" type="hidden"/>
									</span>
								</td>
								<!-- <td style="padding-right: 4px;text-align:right;" valign="middle">
									<span>
										<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
										<button class="btn" onclick="reset();$('#processId').val('');"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
									</span>
								</td> -->
							</tr>
							<tr>
							        <%
										ValueStack valueStack1=ActionContext.getContext().getValueStack();
										List<LayerType> layerTypes=(List)valueStack1.findValue("layerTypes");
										int k=1;%>
										<td colspan='2' style="padding: 0px;width:100%; margin: 0px;">
									<%if(layerTypes!=null){
									for(int i=0;i<layerTypes.size();i++){ 
										LayerType layerType=(LayerType)layerTypes.get(i);
										if("0".equals(layerType.getSampleMethod())){
									%>
										<span class="liSpan"><input name="params.group" type="radio" value="<%=layerType.getTypeCode() %>" onclick="targetSelect(this,'select_<%=layerType.getId()%>')" /><%=layerType.getTypeName()%></span>
										<%-- <input type="hidden" name="tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/> --%>
										<input type="text" name="params.tag_value" id="select_<%=layerType.getId()%>" class="targerSelect" disabled="disabled"/>
									<%}else if("2".equals(layerType.getSampleMethod())||"1".equals(layerType.getSampleMethod())){%>
										<%-- <input type="hidden" name="params.tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/> --%>
										<span class="liSpan"><input name="params.group" type="radio" value="<%=layerType.getTypeCode() %>" onclick="targetSelect(this,'select_<%=layerType.getId()%>')" /><%=layerType.getTypeName()%></span>
										<select name="params.tag_value" id="select_<%=layerType.getId()%>" class="targerSelect" disabled="disabled">
										<%List<LayerDetail> layerDetails=layerType.getLayerDetails();
										for(int j=0;j<layerDetails.size();j++){
											LayerDetail layerDetail=layerDetails.get(j);%>
										<option value="<%=layerDetail.getDetailCode()%>"><%=layerDetail.getDetailName()%></option>
										<%}%>
										</select>
										<script type="text/javascript">
										$(document).ready(function(){
											setFormWidth();
												$("#select_"+<%=layerType.getId()%>).multiselect({
													selectedList:1
												});	
										});
										</script>
										<%}}}%>
										</td>
							</tr>
						</table></br>
						<table cellpadding="0" cellspacing="0" style="width:100%;">
							<caption><input id="title" style="border:2px;background-color:Transparent;font-size: 22px;" value="SPC应用状况报表"/></caption>
							<tr>
								<td colspan="4"><h4>报表结果：</h4></td>
							</tr>
							<tr style="height:32px;background:#99CCFF;font-weight:bold;">
								<td align='right'>质量特性总数：</td>
								<td align='left'><input id="featureCount" style="border:0px;background-color:Transparent;"/></td>
								<td align='right'>子组数：</td>
								<td align='left'><input id="subGroupCount" style="border:0px;background-color:Transparent;"/></td>
							</tr>
						</table>
						<table style="width:100%;">
							<tr>
								<td width="50%" valign="top" id="detail_table_parent">
									<table id="detail_table"></table>
								</td>
								<td width="50%" valign="top">
									<div id="reportDiv_parent">
										<div id='reportDiv'></div>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</form>
			</aa:zone>
		</div>
	</div>
</body>
</html>