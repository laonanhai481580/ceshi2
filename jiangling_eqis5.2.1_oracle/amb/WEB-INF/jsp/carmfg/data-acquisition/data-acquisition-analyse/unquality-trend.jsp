<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<%@include file="unquality-trend-script.jsp" %>
	
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<input type="hidden" id="workshop"  value="${workshop}"/>
	<c:set var="accordionMenu" value="input"/>
	<script type="text/javascript">
		var secMenu="dataAcquisition";
		var thirdMenu="unqualityRateTrend";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-data-acquisition-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center" style="overflow-y:auto">
			<%@include file="unquality-trend-center.jsp" %>
	</div>
	
</body>
</html>