<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<jsp:include page="supplier-rate-chart-script.jsp"/>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="Chart";
		var thirdMenu="mySupplierRateChart";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-chart-report-menu.jsp" %>
	</div>
	<jsp:include page="supplier-rate-chart-center.jsp"/>
</body>
</html>