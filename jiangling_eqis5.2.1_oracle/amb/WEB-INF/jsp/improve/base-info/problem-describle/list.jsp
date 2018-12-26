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
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		//不良现象列表
		var defectionType = '${problemDescrible.defectionType}';
		<%
			String problemDescribleId = request.getParameter("problemDescribleId");
			if(StringUtils.isNotEmpty(problemDescribleId)){
		%>
		function $addGridOption(jqGridOption){
			jqGridOption.postData.problemDescribleId=<%=problemDescribleId%>;
		}
		<%}%>
		function click(cellvalue, options, rowObject){	
			return "<div style='text-align:center;'>"+defectionType+"</div>";
		}
		function $processRowData(data){
			data.problemDescribleId = '<%=problemDescribleId%>';
			return data;
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="IMP_DEFECTION_PHENOMENON_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base_info";
		var thirdMenu="problem_describle";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/improve-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/improve-baseinfo-third-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="IMP_DEFECTION_PHENOMENON_SAVE">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="IMP_DEFECTION_PHENOMENON_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="IMP_DEFECTION_PHENOMENON_EXPORT">
					<button  class='btn' type="button" onclick="iMatrix.export_Data('${improvectx}/base-info/problem-describle/exportCode2.htm?problemDescribleId='+<%=problemDescribleId%>);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="defectionCodeList" url="${improvectx}/base-info/problem-describle/list-datas.htm" code="IMP_DEFECTION_PHENOMENON"></grid:jqGrid>		
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>