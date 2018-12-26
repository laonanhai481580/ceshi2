<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		//部位代码列表
		<%
			String componentId = request.getParameter("componentId");
			if(StringUtils.isNotEmpty(componentId)){
		%>
		function $addGridOption(jqGridOption){
			jqGridOption.postData.componentId=<%=componentId%>;
		}
		<%}%>
		var name='${component.componentName}';
		function click(cellvalue, options, rowObject){	
			return "<div style='text-align:center;'>"+name+"</div>";
		}
		function $processRowData(data){
			data.componentId = '<%=componentId%>';
			return data;
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="base-info-position-code-save">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="defectionCode";
		var treeMenu="position";
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
				<security:authorize ifAnyGranted="base-info-position-code-save">
				<button class='btn' onclick="addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="base-info-position-code-delete">
				<button class='btn' onclick="delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button  class='btn' onclick="showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="base-info-position-code-exportCode">
				<button  class='btn' type="button" onclick="export_Data('${mfgctx}/base-info/position-code/exportCode.htm?componentId='+<%=componentId%>);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<button  class='btn' type="button" onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >						
						<grid:jqGrid gridId="positionCodeList" url="${mfgctx}/base-info/position-code/list-datas.htm" code="MFG_POSITION_CODE"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>