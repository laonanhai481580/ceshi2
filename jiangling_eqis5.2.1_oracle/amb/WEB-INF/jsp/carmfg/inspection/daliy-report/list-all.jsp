<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_list';
		var thirdMenu="daliyPoduceReportList";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inspection-list-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn" id="btnDiv">
				<security:authorize ifAnyGranted="patrol-inspection-input">
				</security:authorize>
				<!-- <button class='btn' onclick="window.location='${mfgctx}/patrol-inspection/input.htm?workshop=${workshop}&inspectionPointId=${inspectionPointId}';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button> -->
				<security:authorize ifAnyGranted="MFG_INSPECTION_DALIY-REPORT_LIST-ALL_DELETE">
				<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="MFG_INSPECTION_DALIY-REPORT_LIST-ALL_EXPORT">
				<button  class='btn' onclick="iMatrix.export_Data('${mfgctx}/inspection/daliy-report/export-list-all.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
					<grid:jqGrid gridId="inprocessInspectionList"  url="${mfgctx}/inspection/daliy-report/list-all-datas.htm" code="MFG_INSPECTION_RECORDS_IMW_ALL" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					<script type="text/javascript">
						$(document).ready(function(){
							var colCodes = $("#colCode").val().split(',');
							var firstCol = colCodes[0];
							var colNumbers = colCodes.length-1;
							$("#inprocessInspectionList").jqGrid('setGroupHeaders', {
								  useColSpanStyle: true, 
								  groupHeaders:[
									{startColumnName:firstCol , numberOfColumns: colNumbers, titleText: '不良明细'}
								  ]
								});
							$("#inprocessInspectionList").jqGrid('setGridParam',{gridComplete:contentResize});
						});
					</script>
				</form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>