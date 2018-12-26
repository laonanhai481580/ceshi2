<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	function $getExtraParams(){
		return {searchStrs:'${params}'};
	}
	function contentResize(){
		$("#inprocessInspectionList").jqGrid("setGridHeight",$(window).height()-95);
	}
	function inspectionNoFormatter(value,o,rowObj){
		return '<a href="javascript:void(0);openViewInfo(\''+value+'\')">'+value+'</a>';
	}
	function openViewInfo(inspectionNo){
		window.location.href = '${mfgctx}/inspection/first-inspection/view-info.htm?inspectionNo='+inspectionNo;
	}
	</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div id="opt-content">
				<grid:jqGrid gridId="inprocessInspectionList"  url="${mfgctx}/manu-analyse/check-regular-right-detail-datas.htm" code="MFG_INSPECTION_RECORDS_IMW" pageName="checkPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
			</div>
		</div>
	</div>
</body>
</html>