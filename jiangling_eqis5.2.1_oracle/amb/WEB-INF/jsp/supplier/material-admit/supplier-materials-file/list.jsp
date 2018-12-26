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
			<security:authorize ifAnyGranted="supplier-materials_save">
				isRight =  true;
			</security:authorize>
			return isRight;
		}	
		
		function showPicture(value,options,obj){
			var strs = "";
			strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
			return strs;
		}
		function attachmentFilesClick(rowId){
			//上传附件 
			$.upload({   
				showInputId : rowId + "_showFiles",
				hiddenInputId : rowId + "_hiddenFiles",
				title:"上传附件",
				callback:function(files){
					params.materialsFile = $("#" + rowId + "_hiddenFiles").val();
				}
			}); 
		}
		function addNew(){
			iMatrix.addRow();
			$("#undefined_uploadBtn").show();
		}
		var params = {};
		function $oneditfunc(rowId){
			selRowId = rowId;
			params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
			params.hisAttachmentFiles = params.attachmentFiles;
			$(".uploadBtn").hide();
			$("#undefined_uploadBtn").show();
			$("#" + rowId + "_uploadBtn").show();
			
		}
		function $afterrestorefunc(rowId){
			$("#" + rowId + "_uploadBtn").hide();
		}
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			return data;
		}
		//自定义保存方法
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
		var secMenu="admit";
		var thirdMenu="_supplier_materials";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-material-admit-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="supplier-materials-input">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
<!-- 						<button class="btn" onclick="customSave('list');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button> -->
					</security:authorize>
					<security:authorize ifAnyGranted="supplier-materials-delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${supplierctx}/material-admit/supplier-materials-file/list-datas.htm" submitForm="defaultForm" code="SUPPLIER_MATERIALS_FILE" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>