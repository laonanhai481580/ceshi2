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
</head>
<!-- OBA不良趋势图-->
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<chart:view 
		chartDefinitionCode="afs-oba-bad-rate"
		secMenu="analysis"
		secMenuJspPath="/menus/aftersales-sec-menu.jsp"
		thirdMenu="oba_bad_rate"
		thirdMenuJspPath="/menus/aftersales-analysis-third-menu.jsp"/>
</body>
</html>	

