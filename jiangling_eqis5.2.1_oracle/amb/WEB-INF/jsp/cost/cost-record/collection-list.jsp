<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.cost.entity.CostRecord"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		$(function(){
		});
		function $beforeEditRow(rowId,iRow,iCol,e){
			return false;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="statistical_analysis";
		var thirdMenu="_composing_cost_record_collection";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	 <div id="secNav">
		<%@ include file="/menus/cost-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/cost-statistical-analysis-menu.jsp" %>
	</div> 
	
	<div class="ui-layout-center">
		<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="middle" style="padding: 0px;width:50%; margin: 0px;" id="btnTd">
							<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
							<security:authorize ifAnyGranted="cost_cost_record_collection_export">
							<button class="btn" onclick="iMatrix.export_Data('${costctx}/cost-record/export-collection.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							</security:authorize>
						</td>
					</tr>
				</table>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<%
						ActionContext.getContext().put("sourceType",CostRecord.SOURCE_TYPE_COLLECTION);
					%>
					<grid:jqGrid gridId="list" url="${costctx}/cost-record/list-datas.htm?sourceType=${sourceType}" code="COST_COST_RECORD"></grid:jqGrid>
				</form>
			</div>
		</aa:zone>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>