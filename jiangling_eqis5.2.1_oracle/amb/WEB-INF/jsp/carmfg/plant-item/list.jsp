<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%
	String baseUrl = request.getContextPath();
%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$.extend($.jgrid.defaults,{
		loadComplete : function(){
			var p = $("#itemList")[0].p.postData;
			if(p&&p.searchParameters!=undefined){
				$("#path").html("查询结果");
				window.selParentId = null;
			}
		}
	});
		function operateFormatter(value,o,obj){
			if(obj.id){
				return "<a class=\"small-button-bg\" onclick=\"loadInspectingItemByStructureAndParent("+obj.id+");\" href=\"#\" style='float:left;margin-left:10px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-newwin\" style='cursor:pointer;'></span></a>";
			}else{
				return '';
			}
		}
		function nameFormatter(value,o,obj){
			if(obj.hasChild==true||obj.hasChild=='true'){
				return "<a class=\"small-button-bg\" href=\"#\" style='float:left;margin-right:2px;'><span class=\"ui-icon ui-icon-folder-collapsed\"></span></a>" + obj.name;
			}else if(obj.name){
				return obj.name;
			}else{
				return '';
			}
		}
		function $oneditfunc(rowId){
			var val = $("#" + rowId + "_name").val();
			if(val){
				val = val.replace(/<[^>]*>/g,'');
				$("#" + rowId + "_name").val(val);
			}
		}
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		
		function $processRowData(data){
			var postData = $("#itemList").jqGrid("getGridParam","postData");
			data.selParentId = postData?postData.selParentId:'';
			return data;
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_PLANT_ITEM_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="plant_parameter";
		var treeMenu = "_plant_item";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="MFG_PLANT_ITEM_SAVE">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_PLANT_ITEM_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="itemList"  url="${mfgctx}/plant-item/list-datas.htm" code="MFG_PLANT_ITEM" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>