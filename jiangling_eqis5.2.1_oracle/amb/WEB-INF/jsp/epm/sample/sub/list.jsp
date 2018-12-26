<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<c:set var="actionBaseCtx" value="${epmctx}/sample/sub" />
<script type="text/javascript">
	function createqualiy() {
		window.location = "${epmctx}/sample/input.htm";
	}
	
	function click(cellvalue, options, rowObject) {
		return "<a href='${epmctx}/sample/input.htm?sunId=" + rowObject.id + "'>"
				+ cellvalue + "</a>";
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "sample";
		var thirdMenu = "sampleSubList";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/epm-sec-menu.jsp"%>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/epm-sample-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="iMatrix.showSearchDIV(this);"
						type="button">
						<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
					</button>
					<security:authorize ifAnyGranted="epm_sample_export">
						<button class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export.htm');"
							type="button">
							<span><span><b class="btn-icons btn-icons-export"></b>导出</span></span>
						</button>
					</security:authorize>
					
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${actionBaseCtx}/list-datas.htm"
							code="EPM_SAMPLE_SUBLIST" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>