<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function featureFormatter(value,options,rowObj){
			return '<a href="javascript:gotoSpc(\''+rowObj.featureId+'\',\''+rowObj.featureName+'\')">'+value+'</a>';
		}
		function gotoSpc(featureId,featureName){
			window.location = encodeURI('${spcctx}/statistics-analysis/data-analysis.htm?featureId='+featureId+'&featureName=' + featureName);
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="statAnalyse";
		var thirdMenu="_spc_control";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-manu-analyse-menu.jsp" %>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
			<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			<button class='btn' onclick="iMatrix.export_Data('${mfgctx}/spc-control/exports.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>	
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="MFG_ITEM_INDICATOR" url="${mfgctx}/spc-control/list-datas.htm" code="MFG_ITEM_INDICATOR"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>