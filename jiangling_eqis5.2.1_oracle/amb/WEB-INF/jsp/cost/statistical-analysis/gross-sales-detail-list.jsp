<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function contentResize(){
			$("#grid").jqGrid("setGridHeight",$(window).height()-74);
			$("#grid").jqGrid("setGridWidth",$(window).width()-25);
		}
	</script>
</head>
<%
	JSONObject params = (JSONObject)ActionContext.getContext().getValueStack().findValue("params");
	String url = "startDate=" + params.getInt("inspectionDate") + "&endDate=" + params.getInt("inspectionDate");
	if(params.containsKey("businessUnitCode")){
		url += "&businessUnitCode=" + params.getString("businessUnitCode");
	}
	ActionContext.getContext().put("searchUrl",url);
%>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
			</div>
			<div id="opt-content">
				<grid:jqGrid gridId="grid"
					url="${costctx}/statistical-analysis/gross-sales-detail-list-datas.htm?${searchUrl}"
					code="ERP_SALES_RECORD" pageName="erpSalesRecordPage"></grid:jqGrid>
			</div>
		</div>
	</div>
</body>
</html>