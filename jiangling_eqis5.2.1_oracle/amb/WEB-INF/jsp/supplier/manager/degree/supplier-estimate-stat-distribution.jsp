<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.3/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript" src="${ctx}/js/chart-view1.0.js"></script>
	<script type="text/javascript" src="${ctx}/js/chart-view-search-format1.0.js"></script>	
	<script type="text/javascript">
	var chart = null,searchParams = null,cacheResult = null;
	var multiselectIds = ['businessUnit'];
	window.chartView.initInputEditType = function(){
// 		$(":input[isDate]").each(function(index,obj){
// 			var formatStr = $(obj).attr("dateFormat");
// 			if(!formatStr){
// 				formatStr = "yy-mm-dd";
// 			}
// 			$(obj).datepicker({changeYear:true,changeMonth:true,showButtonPanel: true,dateFormat:formatStr});
// 		});
		$(".label").each(function(index,obj){
			var labelName = obj.innerHTML;
			if("查询人"==labelName){
				$(obj.nextElementSibling).attr("readonly",'readonly');
				$(obj.nextElementSibling).val('<%=ContextUtils.getUserName()%>');
			}
		});
	};
	function addSelect(obj){
		$(".label").each(function(index,obj2){
				var labelName2 = obj2.innerHTML;
				var processName = obj.processName;
				var processArr = processName.split(",");
				if("制程名称"==labelName2){
					var selectProcessName = obj2.nextElementSibling;
					var opp1 = new Option("","");
					selectProcessName.add(opp1);
	 				var names = processArr.split(',');
	 				for(var i=0;i<names.length;i++){
	 					var opp = new Option(names[i],names[i]);
	 					selectProcessName.add(opp);
	 				}
				}  
			});
	}
	</script>
</head>
<!-- 制程良率推移图 -->
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<%
			request.setAttribute("selLevel",1);
		%>
	<chart:view 
		chartDefinitionCode="supplier_evaluate"
		secMenu="evaluate"
		secMenuJspPath="/menus/supplier-sec-menu.jsp"
		thirdMenu="mySupplierEstimateStatDistribution"
		thirdMenuJspPath="../../evaluate/quarter/left.jsp"/>
</body>
</html>	

