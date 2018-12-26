<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.supplier.entity.Supplier"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
	<script type="text/javascript">
//重写保存后的方法
function $successfunc(response){
	var result = eval("(" + response.responseText + ")");
	if(result.error){
		alert(result.message);
		return false;
	}else{
		return true;
	}
}
function $oneditfunc(rowId){
	var obj = $("#" + rowId + "_materialType");
	obj.css("width","100%");
}
function $processRowData(data){
	data._list_code = "SUPPLIER_ADMI_FLOW_CONF";
	return data;
}
function limitFormatter(value){
	if(value==undefined||value==''||value=='&nbsp;'){
		return "无限制";
	}else{
		return value;
	}
}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="estimate";
		var thirdMenu="_flow_configure";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-estimate-menu.jsp" %>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
					<button class='btn' onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				<button class='btn' onclick="javascript:iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
			</div>
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="flowConfigures"
						url="${supplierctx}/admittance/flow-configure/list-datas.htm" code="SUPPLIER_ADMI_FLOW_CONF"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>