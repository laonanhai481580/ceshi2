<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="process_monitor";
		var thirdMenu="_monitor";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-process-monitor-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn">
					<button class='btn' onclick="addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<button class='btn' onclick="delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					<button class='btn' onclick="showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<button class='btn' onclick="exportMonitor();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</div>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="bsRules"  url="${spcctx}/bs-rules/list-datas.htm" code="SPC_BS_RULES" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>