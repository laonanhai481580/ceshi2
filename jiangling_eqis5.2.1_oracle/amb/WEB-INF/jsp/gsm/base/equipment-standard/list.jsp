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
		<%
			String standardId = request.getParameter("standardId");
			if(StringUtils.isNotEmpty(standardId)){
		%>
		function $addGridOption(jqGridOption){
			jqGridOption.postData.standardId=<%=standardId%>;
		}
		<%}%>
		function $processRowData(data){
			data.standardId = '<%=standardId%>';
			return data;
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="GSM_EQUIPMENT_STANDARD_ITEM_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		//后台返回错误信息
		function $successfunc(response){
			var jsonData = eval("(" + response.responseText+ ")");
			if(jsonData.error){
				alert(jsonData.message);
			}else{
				return true;
			}
		} 		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="standard";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-code-rules-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="GSM_EQUIPMENT_STANDARD_ITEM_SAVE">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="GSM_EQUIPMENT_STANDARD_ITEM_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="GSM_EQUIPMENT_STANDARD_ITEM_EXPORT">
					<button  class='btn' type="button" onclick="iMatrix.export_Data('${gsmctx}/base/equipment-standard/exportCode2.htm?standardId='+<%=standardId%>);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="defectionCodeList" url="${gsmctx}/base/equipment-standard/list-datas.htm" code="GSM_EQUIPMENT_STANDARD_ITEM"></grid:jqGrid>		
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>