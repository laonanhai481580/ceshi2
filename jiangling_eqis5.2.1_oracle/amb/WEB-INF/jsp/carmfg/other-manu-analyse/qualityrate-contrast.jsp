<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<%@include file="qualityrate-contrast-script.jsp" %>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="otherstatAnalyse";
		var thirdMenu="_qualityrate_contrast";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-other-manu-analyse-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<%@include file="qualityrate-contrast-center.jsp" %>
	</div>
</body>
</html>