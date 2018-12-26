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
<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>

<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:178px;
	}
	.input{
		width:170px;
	}
	.label{
		float:left;
		width:70px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:120px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:260px;
		margin-bottom:2px;
		text-align:right;
	}
-->
</style>
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
		//$("#_qualityFeature").multiselect({selectedList:2});
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
	//根据参数获取数据
	function reportByParams(params){
		$("#reportDiv").html("图表生成中,请稍后....");
		$.post("${spcctx}/statistics-analysis/cpk-trend-datas.htm",params,function(result){
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
//			        tickInterval : 10,
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
		$(":input","#customerSearchDiv").each(function(index,obj){
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
			}else if(obj.type == 'radio'){
				if(obj.checked){
					params[obj.name] = jObj.val();
				}
			}else if(obj.name&&jObj.val()&&jObj.val()!=""){
					params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	//选择质量特性
	function selectFeature(obj){
		$.colorbox({/* href:"${spcctx}/common/feature-bom-select.htm", */
			href:"${spcctx}/common/feature-bom-multi-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择质量特性"
		});
	}
	
	function setFeatureValue(datas){
		/* $("#featureName").val(datas[0].value);
		$("#_qualityFeature").val(datas[0].id); */
		var ids="",values="";
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
		$("#_qualityFeature").val(ids);
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_cpk_trend";
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
				<form id="contentForm" name="contentForm" method="post" action="">
					<div class="opt-btn" id="btnDiv">
						<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					</div>
					<div id="customerSearchDiv" style="display:block;padding:4px;">
						<table class="form-table-outside-border"  style="width:100%;padding:4px;">
							<tr>
								<td style="width:60px;text-align:right;">日&nbsp;&nbsp;期</td>
								<td colspan="4" style="width: 24.5%;">
									<input id="datepicker1" type="text" readonly="readonly" style="width:80px;margin-left: 5px;border:1px solid gray;" name="params.startDate_ge_date" value="<%=startDateStr%>"/>&nbsp;至&nbsp;
									<input id="datepicker2" type="text" readonly="readonly" style="width:80px;border:1px solid gray;" name="params.endDate_le_date" value="<%=endDateStr%>"/>
								</td>
								<td>
									质量特性
									<%-- <s:select id="_qualityFeature" list="qualityFeatures"
										  theme="simple"
										  listKey="value" 
										  listValue="name" 
										  cssClass="targerSelect"
										  name="params.qualityFeatures">
									</s:select> --%>
									<input style="border:1px solid blue;color: blue;font-weight:bold" name="featureName" id="featureName" value="${qualityFeatures[0].name}" onclick="selectFeature(this);" readonly="readonly"/>
									<input name="params.qualityFeatures" id="_qualityFeature" value="${qualityFeatures[0].value}" class="targerSelect" type="hidden"/>
								</td>
							</tr>
						</table>
						<table class="form-table-outside-border" style="width:100%;">
							<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计分组</caption>
							<tr>
								<td style="padding:0px;margin:0px;padding-bottom:2px;">
									<ul id="groupUl">
										<li>
											<input type="radio" name="params.group" checked="checked" value="CPK" title="CPK"/>CPK
										</li>
										<li>
											<input type="radio" name="params.group" value="MEAN" title="MEAN" id="MEAN"/>MEAN
										</li>
										<li>
											<input type="radio" name="params.group" value="StdDve" title="StdDve" id="StdDve"/>StdDve
										</li>
										<li>
											<input type="radio" name="params.group" value="SIGMA" title="SIGMA"/>SIGMA
										</li>
										<li>
											<input type="radio" name="params.group" value="Fpu(Perf)" title="Fpu(Perf)"/>Fpu(Perf)
										</li>
									</ul>
								</td>
								<!-- <td style="text-align:right;">
									<span>
										<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
									</span>
								</td> -->
							</tr>
							<!-- <tr>
								<td style="text-align:right;">
									<span>
									<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
									</span>
								</td>
							</tr> -->
						</table>
					</div>
				</form>
				<div style="margin-left:2px;" >
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