<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="maintain_save">
				isRight =  true;
			</security:authorize>
			return isRight;
		}	
		//生成校验计划
		function createMan() {
			var rowIds = $("#list").getGridParam("selarrrow");
			if (rowIds.length == 0) {
				alert("<s:text name='请选择数据！！'/>");
				return;
			}
			var flag = false;
			for (var i = 0; i < rowIds.length; i++) {
				var rowData = $("#list").jqGrid("getRowData",rowIds[i]);
				if(!("在用" == rowData.checkState || "在库" == rowData.checkState)){
					flag = true;
				}
			}
			if(flag){
				alert("<s:text name='状态不是【在用】或【在库】不能生成校验计划'/>");
				return false;
			}
			$.post("${gsmctx}/maintain/create-man-plan.htm?ids="+ rowIds, null,function(data) {
				if (data.error) {
					alert(data.message);
					return false;
				}
				alert(data.message);
				$("#list").trigger("reloadGrid");
			}, "json");
		}
		function customSave(gridId) {
			if (lastsel == undefined || lastsel == null) {
				alert("当前没有可编辑的行!");
				return;
			}
			var $grid = $("#" + gridId);
			var o = getGridSaveParams(gridId);
			if ($.isFunction(gridBeforeSaveFunc)) {
				gridBeforeSaveFunc.call($grid);
			}
			$grid.jqGrid("saveRow", lastsel, o);
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="nonconformity";
		var thirdMenu="maintainList";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-nonconformity-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="maintain_save">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
						<button class="btn" onclick="customSave('list');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="maintain_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="maintain_create-man-plan">
						<button class="btn" onclick="createMan()"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name="生成校验计划" /></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="maintain_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/maintain/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${gsmctx}/maintain/list-datas.htm" submitForm="defaultForm" code="GSM_MAINTAIN" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>