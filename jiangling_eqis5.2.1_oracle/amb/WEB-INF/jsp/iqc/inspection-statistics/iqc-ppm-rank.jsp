<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.util.common.DateUtil"%>
<%@ include file="/common/taglibs.jsp"%>
<style>
	<!--
		.spanLabel{
			text-align:right;
			padding-right:2px;
			float:left;
			width:75px;
		}
	-->
</style>
<%
	Calendar calendar = Calendar.getInstance();
	String endDateStr = DateUtil.formateDateStr(calendar);
	calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	String startDateStr = DateUtil.formateDateStr(calendar);
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
		$(document).ready(function(){
			$('#datepicker1').datepicker({changeMonth:true,changeYear:true});
			$('#datepicker2').datepicker({changeMonth:true,changeYear:true});
			createTable(getParams());
		});
		function createTable(params){
			$("#opt-content").html("数据加载中,请稍候... ...");
			var url = "${iqcctx}/inspection-statistics/iqc-ppm-rank-table.htm";
			$("#opt-content").load(url,params);
		}
		//确定的查询方法
		function search(){
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			if(date1>date2){
				alert("日期时间前后设置有误，请重新选择！");
				$("#datepicker1").focus();
			}else{
				createTable(getParams());
			}
		}	
		//获取表单的值
		function getParams(){
			var params = {};
			$("#search :input[name]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()){
					if(obj.type=="radio"){
						if(obj.checked){
							params[obj.name] = jObj.val();
						}
					}else{
						params[obj.name] = jObj.val();
					}
				}
			});
// 			alert($.param(params))
			return params;
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
			$("#supplierName").val(obj.name);
			$("#supplierCode").val(obj.code);
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
			$("#materialName").val(datas[0].value);
			$("#materialCode").val(datas[0].key);
	 	}
		function formReset(){
			$("#supplierCode").val("");
			$("#materialCode").val("");
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
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="statAnalyse";
		var thirdMenu="myIqcPpmRank";
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
		 	<div class="opt-btn"></div>	
			<div id="search" style="display:block;padding: 2px;">
				<form action="" onsubmit="return false;">
					<table class="form-table-outside-border" style="width:100%;">
						<tr>
							<td>
								<span class="spanLabel">检验日期</span>
								<input id="datepicker1" type="text" name="params.startDate_ge_date" readonly="readonly" style="width:72px" class="line" value="<%=startDateStr%>"/>
							        至<input id="datepicker2" type="text" name="params.endDate_le_date" readonly="readonly" style="width:72px" class="line" value="<%=endDateStr%>"/>
						    </td>
						    <td>
						    	<span class="spanLabel"><input type="radio" name="params.statType" value="0" checked="checked"/>供应商</span>
						    	<input type="text" name="supplierName" id="supplierName" readonly="readonly" onclick="selectSupplier();"/>
						    	<input type="hidden" name="params.supplierCode" id="supplierCode"/>
						    </td>
						    <td>
						    	<span class="spanLabel"><input type="radio" name="params.statType" value="1"/>零件</span>
						    	<input type="text" name="materialName" id="materialName" readonly="readonly" onclick="selectBom();"/>
						    	<input type="hidden" name="params.materialCode" id="materialCode"/>
						    </td>
						</tr>
						<tr>
							<td>
								<span class="spanLabel">车型</span>
						    	<s:select id="modelSpecification"
									  list="modelSpecifications" 
									  cssStyle="width:170px;"
									  emptyOption="true"
									  name="params.modelSpecification"
									  theme="simple"
									  listKey="value" 
									  listValue="value">
								</s:select>
							</td>
							<td colspan="2" style="text-align: right;">
								<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
<!-- 								<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button> -->
							 	<button class='btn' type="button" onclick="reset();formReset();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
							 	<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_CHECK-TYPE-CHART_EMAIL">
							 		<button class='btn' type="button" onclick="sendChart();"><span><span><b class="btn-icons btn-icons-email"></b>发送邮件</span></span></button>&nbsp;
								</security:authorize>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div id="opt-content" style="display:block;padding: 2px;"></div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>