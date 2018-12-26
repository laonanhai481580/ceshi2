<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
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
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
	
<style>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl .li{
		float:left;
		width:260px;
		height:24px;
		list-style:none;
	}
	#searchUl .liSpan{
		float:left;
		width:60px;
		text-align:right;
		padding-right:2px;
	}
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
-->
</style>
<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<script type="text/javascript">
	function contentResize(){
		if(cacheResult != null){
			createReport(cacheResult);
		}
	}
	
	var chart = null,cacheResult = null;
	$(document).ready(function(){
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		setFormWidth();
		search();
	});
	//确定的查询方法
	function search(){
		var params = getParams();
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("日期前后选择有误,请重新设置!");
			$("#datepicker1").focus();
		}else{
			reportByParams(params);
		}
	}
	
	function contentResize(){
		setFormWidth();
	}
	
	function setFormWidth(){
		var width = _getTableWidth()-16;
		var total = parseInt(width/260);
		var addWidth = parseInt(width/total);
		addWidth = addWidth < 260?260:addWidth;
		$("#searchUl .li").width(addWidth);
	}
	//根据参数获取数据
	function reportByParams(params){
		$("#reportDiv").html("图表生成中,请稍后....");
		$.post("${spcctx}/statistics-analysis/sign-cpk-trend-datas.htm",params,function(result){
				createReport(result);
				cacheResult = result;
					//window.location = "#typeTable";
		},'json');
	}
	
	function createReport(result){
		if(chart != null){
			chart.destroy();
			chart = null;
		}
		var width = $("#btnDiv").width()-10;
		chart = new Highcharts.Chart({
			colors: ["#E0B56C", "#DF5353", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
			 		"#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
			exporting : {
				enabled : false
			},
			chart: {
				renderTo: "reportDiv",
				width : width,
				height:500,
				type : 'line'
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
				x:0//center
			},
			xAxis: {
				categories: result.categories,
				gridLineDashStyle : 'ShortDashDot',
				gridLineWidth: 1
			},
			yAxis: [{
				title: {
					text: ''
				},
		        gridLineWidth : 0,
			        //tickInterval : 10,
				labels : {
					/* formatter : function(){
						if(this.value>100){
							return "";
						}else{
							return this.value + '%';
						}
					} */
				},
				 min: 0
				//max:100 
			}],
			legend: {
				 layout : 'vertical',
		         align: 'right',
		         verticalAlign : 'middle',
		         backgroundColor: '#FFFFFF',
		         x : 6,
		         y : 0
		    },
			plotOptions: {
		        line : {
		            shadow : false,
		            lineWidth : 2,
		            dataLabels: {
		               enabled: true,
		               formatter : function(){
		            	   return this.y.toFixed(2);
		               } 
		            }
		        }
		    },
			tooltip: {
				formatter: function() {
					return "<b>" + this.series.name + ":</b>" + this.y.toFixed(2);
				}
			},
			series: result.series
		});
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#searchTable").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&obj.type=='select-one'){
				if(obj.id&&!obj.disabled){
					var values = $("#" + obj.id).multiselect("getChecked").map(function(){
					   return this.value;	
					}).get();
					if(values){
						var val = values.toString();
						if(val){
							params[obj.name] = val;
						}
					}else{
						if(jObj.val()){
							params[obj.name] = jObj.val();
						}
					}
				}
			}else if(obj.name&&jObj.val()&&jObj.val()!=""){
					params[obj.name] = jObj.val();
			}
		});
		
		$(":input","#queryCondition").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&obj.type=='select-one'){
				if(obj.id&&!obj.disabled){
					var values = $("#" + obj.id).multiselect("getChecked").map(function(){
					   return this.value;	
					}).get();
					if(values){
						var val = values.toString();
						if(val){
							params[obj.name] = val;
						}
					}else{
						if(jObj.val()){
							params[obj.name] = jObj.val();
						}
					}
				}
			}else if(obj.type=='radio'){
				if(obj.checked){
					params[obj.name] = jObj.val();
				}
			}else if(obj.name&&jObj.val()&&jObj.val()!=""){
					params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	
	function changQueryCondition(){
		var url = '${spcctx }/statistics-analysis/query-condition.htm?featureId='+$("#_qualityFeature").val() + "&nowtime=" + (new Date()).getTime();
		$("#queryCondition").load(url);
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
	//选择质量特性
	function selectFeature(obj){
		$.colorbox({href:"${spcctx}/common/feature-bom-select.htm",
			/* href:"${spcctx}/common/feature-bom-multi-select.htm", */
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择质量特性"
		});
	}
	
	function setFeatureValue(datas){
		$("#featureName").val(datas[0].value);
		$("#_qualityFeature").val(datas[0].id);
		changQueryCondition();
		/* var ids="",values="";
		for ( var data in datas) {
			if(datas.length > 1){
				values="当前已选择"+(parseInt(data)+1)+"项质量特性!";
			}else{
				values += datas[data].value;
			}
			if(data == (datas.length-1)){
				ids += datas[data].id;
				//values += datas[data].value;
				break;
			}
			ids += datas[data].id+",";
			//values += datas[data].value+",";
		}
		$("#featureName").val(values);
		$("#_qualityFeature").val(ids);  */
	}
</script>

</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_sign_cpk_trend";
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
	
	<div class="ui-layout-center" style="overflow: auto;">
		<div class="opt-body" style="overflow: auto;">
			<aa:zone name = "main">
				<div class="opt-btn" id="btnDiv"></div>
				<div id="searchDiv" style="display:block;padding:4px;">
				<form id="contentForm" name="contentForm" method="post" action="">
				<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
					<tr>
						<td style="padding-left:2px;padding-bottom:4px;">
							<ul id="searchUl">
						 		<li class="li">
						 			<span class="liSpan">日期</span>
						 			<input id="datepicker1" type="text" readonly="readonly" style="width:78px" name="params.startDate_ge_date" value="<%=startDateStr%>"/>
						 			至
						 			<input id="datepicker2" type="text" readonly="readonly" style="width:78px" name="params.endDate_le_date" value="<%=endDateStr%>"/>
						 			<%-- <input id="datepicker1" type="text" readonly="readonly" style="width:72px" name="params.startDate_ge_date" value="<%=startDateStr%>"/>&nbsp;至&nbsp;<input id="datepicker2" type="text" readonly="readonly" style="width:72px" name="params.endDate_le_date" value="<%=endDateStr%>"/> --%>
						 		</li>
						 		<li class="li">
						 			<span class="liSpan">质量特性</span>
						 			<%-- <s:select id="_qualityFeature" list="qualityFeatures"
											  theme="simple"
											  listKey="value" 
											  listValue="name" 
											  name="params.qualityFeatures"
											  onchange="changQueryCondition();">
									</s:select> --%>
									<input name="featureName" id="featureName" value="${qualityFeatures[0].name}" style="width:100px" onclick="selectFeature(this);" readonly="readonly"/>
									<input name="params.qualityFeatures" id="_qualityFeature" value="${qualityFeatures[0].value}" class="targerSelect" type="hidden"/>
						 		</li>
						 		<li class="li">
						 			<span class="liSpan"><button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button></span>
						 		</li>
						 	</ul>
						 	<ul id="searchUl2">
						 	   <div id="queryCondition">
									<%@ include file="query-condition.jsp" %>
								</div>
						 	</ul>
						</td>
				    </tr>
			 		<tr>
			 			<td style="text-align: right;">
					 		<!-- <span>
					 			<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					 		</span> -->
			 			</td>
			 		</tr>
		 		</table>

		   <%-- <table class="form-table-outside-border"  style="width:100%;padding:4px;">
				<tr>
					<td style="width:70px;text-align:right;">日&nbsp;&nbsp;&nbsp;&nbsp;期</td>
					<td colspan="4">
						<input id="datepicker1" type="text" readonly="readonly" style="width:72px" name="params.startDate_ge_date" value="<%=startDateStr%>"/>至
					    <input id="datepicker2" type="text" readonly="readonly" style="width:72px" name="params.endDate_le_date" value="<%=endDateStr%>"/>
					</td>
					<td>
						质量特性
						<s:select id="_qualityFeature" list="qualityFeatures"
							  theme="simple"
							  listKey="value" 
							  listValue="name" 
							  cssClass="targerSelect"
							  name="params.qualityFeatures">
						</s:select>
					</td>
					<td>
					<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					</td>
				</tr>
			</table> --%>
			</form>
			</div>
			<div style="margin-left:2px;">
				<table style="width:98.5%;">
					<tr>
						<td id="reportDiv_parent">
							<div id='reportDiv'></div>
						</td>
					</tr>
				</table>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>