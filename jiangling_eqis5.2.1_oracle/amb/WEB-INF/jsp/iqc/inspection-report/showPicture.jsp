<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> 
<%@ include file="/common/taglibs.jsp"%>
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<script type="text/javascript" src="${ctx}/js/jquery-1.5.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/jqzoom/js/jquery.jqzoom-core.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/widgets/jqzoom/css/jquery.jqzoom.css"/> 
	<script type="text/javascript">
	$(document).ready(function(){
	 var options={
		zoomWidth:600,
		zoomHeight:500
	 }
	$(".MYCLASS").jqzoom(options);
	})
	</script>
	 </head>
<body> 
<a href="${mfgctx}/common/download.htm?id=${path}" class="MYCLASS" title="${name}">  
<img id="monitorImage" width="530" height="420" src="${mfgctx}/common/download.htm?id=${path}"  alt="${name}" />
</a></body>   
</html>