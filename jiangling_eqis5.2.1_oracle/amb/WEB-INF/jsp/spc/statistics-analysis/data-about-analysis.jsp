<%@page import="java.text.SimpleDateFormat"%>
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
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
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
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	
	function setImgUrl(){
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		var type=$('input:radio[name="type"]:checked').val();
		if(type=='no'){
			var beginNo = $("#beginNo").val();
			var endNo = $("#endNo").val();
			if(!beginNo||!endNo){
				alert("请输入子组号");
				return;
			}
		}
		if(date1>date2){
			alert("日期前后选择有误,请重新设置!");
			$("#datepicker1").focus();
		}else{
			$.showMessage("正在统计,请稍候... ...");
			//添加质量特性
			var url = '${spcctx }/statistics-analysis/distribute-draw.htm?xqualityFeature='+$("#xqualityFeature").val() +"&yqualityFeature="+$("#yqualityFeature").val() + "&nowtime=" + (new Date()).getTime();
			//添加日期
			url += "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
			//添加统计分组
			url += "&group=" + $('input:radio[name="group"]:checked').val();
			//添加类型
			url += "&type=" + $('input:radio[name="type"]:checked').val();
			url += "&beginNo=" + $("#beginNo").val()+"&endNo="+$("#endNo").val();
			$("#imgID").attr("src",encodeURI(url));
		}
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_aboutAnalysis";
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
						<button class='btn' onclick="setImgUrl();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
						<span id="message" style="color:red;"></span>
					</div>
					<div id="customerSearchDiv" style="display:block;padding:4px;">
						<table class="form-table-outside-border"  style="width:100%;padding:4px;">
							<tr>
								<td style="width:90px;"><input type="radio" name="type" id="type" value="date" checked="checked"/>日&nbsp;&nbsp;期</td>
								<td >
									<input id="datepicker1" type="text" readonly="readonly" style="width:80px;border: none;" name="params.startDate_ge_date" value="<%=startDateStr%>"/>&nbsp;至&nbsp;<input id="datepicker2" type="text" readonly="readonly" style="width:80px;border: none;" name="params.endDate_le_date" value="<%=endDateStr%>"/>
								</td>
								<td style="width:80px;text-align:right;"><input type="radio" name="type" id="type" value="no"/>子组号:</td>
								<td >
									<input  type="text"  style="width:30px;border: none;" name="beginNo" id="beginNo" />&nbsp;到&nbsp;<input  type="text"  style="width:30px;border: none;" name="endNo" id="endNo"/>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									自变量(X):
									<s:select cssStyle="border: none;" id="xqualityFeature" list="qualityFeatures"
										  theme="simple"
										  listKey="value" 
										  listValue="name" 
										  cssClass="targerSelect"
										  name="xqualityFeature">
									</s:select>
								</td>
								<td colspan="2">
									自变量(Y):
									<s:select cssStyle="border: none;" id="yqualityFeature" list="qualityFeatures"
										  theme="simple"
										  listKey="value" 
										  listValue="name" 
										  cssClass="targerSelect"
										  name="yqualityFeature">
									</s:select>
								</td>
							</tr>
						</table>
						<table class="form-table-outside-border" style="width:100%;">
							<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;">统计分组</caption>
							<tr>
								<td style="padding:4px;margin:0px;padding-bottom:2px;">
									<ul id="groupUl">
										<li>
											<input type="radio" name="group"  checked="checked" value="data" title="原始数据"/>原始数据
										</li>
										<li>
											<input type="radio" name="group"  value="mean" title="平均值" id="mean"/>平均值
										</li>
										<li>
											<input type="radio" name="group"  value="max" title="最大值" id="max"/>最大值
										</li>
										<li>
											<input type="radio" name="group"  value="min" title="最小值"/>最小值
										</li>
										<li>
											<input type="radio" name="group"  value="range" title="极差"/>极差
										</li>
										<li>
											<input type="radio" name="group"  value="stdev" title="标准偏差"/>标准偏差
										</li>
									</ul>
								</td>
								<!-- <td style="text-align:right;">
									<span>
										<button class='btn' onclick="setImgUrl();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
										<button class='btn' type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
									</span>
								</td> -->
							</tr>
				<!-- 			<tr> -->
				<!-- 				<td style="text-align:right;"> -->
				<!-- 					<span> -->
				<!-- 					<button class='btn' onclick="setImgUrl();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button> -->
				<!-- 					</span> -->
				<!-- 				</td> -->
				<!-- 			</tr> -->
						</table>
					</div>
				</form>
				<div style="margin-left: 10px;">
					<table>
						<tr>
							<td>
								<img id="imgID" src="${ctx}/images/stat/controlpic.png" width="100%;" height="100%;"/>
							</td>
						</tr>
					</table>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>