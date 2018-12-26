<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	function saveDefectionCode(){
		window.location='${mfgctx}/base-info/procedure-defection/input.htm';
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="inspectionPoint";
		var treeMenu="code";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
					<security:authorize ifAnyGranted="MFG_BASE-INFO_PROCEDURE-DEFECTION_INPUT">
					<button class='btn' onclick="saveDefectionCode();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_BASE-INFO_PROCEDURE-DEFECTION_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_BASE-INFO_PROCEDURE-DEFECTION_EXPORT">
					<button class='btn' onclick="iMatrix.export_Data('${mfgctx}/base-info/procedure-defection/export.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>	
					</security:authorize></div>
					<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post"  action="">
							<grid:jqGrid gridId="procedureDefectionList" url="${mfgctx}/base-info/procedure-defection/list-datas.htm" code="MFG_PDD"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>