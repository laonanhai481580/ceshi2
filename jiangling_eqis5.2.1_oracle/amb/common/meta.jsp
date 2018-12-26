<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
       
	<script lang="javascript" type="text/javascript">
		var webRoot="${ctx}";
		var resourceRoot="${resourcesCtx}";
		var imatrixRoot="${imatrixCtx}";
		var topMenu="";
		var versionType="${versionType}";
	</script>
	
	<script type="text/javascript" src="${resourcesCtx}/js/jquery-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/common-layout.js"></script>	
	<script type="text/javascript" src="${resourcesCtx}/js/jquery.timers-1.2.js"></script>
	<link   type="text/css" rel="stylesheet" href="${resourcesCtx}/css/<%=ContextUtils.getTheme()%>/jquery-ui-1.8.16.custom.css" id="_style"/>
	<script type="text/javascript" src="${resourcesCtx}/widgets/jqgrid/jqgrid-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/jqgrid/jqGrid.custom.js"></script>
	<link   type="text/css" rel="stylesheet" href="${resourcesCtx}/widgets/jqgrid/ui.jqgrid.css" />
	<script type="text/javascript" src="${resourcesCtx}/widgets/jstree/jquery.jstree.js"></script>
	
	
	<script type="text/javascript" src="${resourcesCtx}/js/aa.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/public.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/js/form.js"></script>
	<script type="text/javascript" src="${ctx}/js/demo.js"></script>
		<script type="text/javascript" src="${ctx}/js/commons.js"></script>
	<link type="text/css" rel="stylesheet" href="${ctx}/widgets/uploadify/Huploadify.css" />
	<script type="text/javascript" src="${ctx}/widgets/uploadify/jquery.Huploadify.js">	
	<script type="text/javascript" src="${resourcesCtx}/templateJs/ztree-tag.js"></script>
	<link rel="stylesheet" href="${resourcesCtx}/widgets/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${resourcesCtx}/widgets/ztree/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/ztree/js/jquery.ztree.excheck-3.5.js"></script>
	<script src="${resourcesCtx}/js/z-tree.js" type="text/javascript"></script>
	
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
	<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="${ctx}/widgets/highstock-5.0.11/highstock.js"></script>
	<%-- <script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/handlers.js"></script>
	<link rel="stylesheet" href="${ctx}/widgets/swfupload/css/button.css" type="text/css"
	<script type="text/javascript" src="${ctx}/js/highcharts.js"></script> />
	<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="${ctx}/widgets/multiselect/jquery.multiselect.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script> --%>
	