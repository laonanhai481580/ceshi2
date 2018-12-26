<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>原因措施经验库</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript" src="${ctx}/js/search.js"></script>
	<script type="text/javascript">
	function exportProduct(){
		$("#contentForm").attr("action","${mfgctx}/data-acquisition/reason-measure-lib/export.htm");
		$("#contentForm").submit();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="dataAcquisition";
	var thirdMenu="reasonAndMearsureLib";
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
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
				<div class="opt-btn">
				<a class='btn' onclick="addRow();"><span><span>新建</span></span></a>
						<a class='btn' onclick="delRow();"><span><span>删除</span></span></a>
						<a class='btn' onclick="exportProduct();"><span><span>导出</span></span></a>
						<a  class='btn' onclick="showSearchDIV(this);"><span><span>查询</span></span></a>
					</div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post"  action="">
							<grid:jqGrid gridId="reasonMeasureLib" url="${mfgctx}/data-acquisition/reason-measure-lib/list-datas.htm" code="MFG_REASON_AND_MEASURE_LIB" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>