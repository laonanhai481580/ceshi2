<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
<script type="text/javascript">
<%-- 		<% --%>
// 		String sid = request.getParameter("sid");
// 		if(StringUtils.isNotEmpty(sid)){
<%-- 		%> --%>
// 		function $addGridOption(jqGridOption){
<%-- 			jqGridOption.postData.sid=<%=sid%>; --%>
// 		}
<%-- 		<%}%> --%>
		function $processRowData(data){
			data.sid = '${sid}';
			$("#" + rowId + "_percentage").change(function(){
				setPercentage(rowId);
			});
			return data;
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
		function $beforeSaveCell(){
			setPercentage();
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="maintain_save">
				isRight =  true;
			</security:authorize>
			return isRight;
		}	
		var selRowId=null;
		//行数据编辑事件
		function $oneditfunc(rowId){
			selRowId = rowId;
			$("#" + rowId + "_percentage").keyup(setPercentage);
// 			$("#" + rowId + "_percentage").change(function(){
// 				setPercentage(rowId);
// 			});
			
		}
		//重写保存
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
		function setPercentage(){
			var obj = $("#list").jqGrid("getRowData");
			var ret = 0;
			var id ="";
			for(var i=0;obj.length>i;i++){
				id = obj[i].id;
				if(isNaN(obj[i].percentage)||obj[i].percentage==""){
					obj[i].percentage=0;
				}
				if(id==selRowId){
					if(isNaN($("#"+id+"_percentage").val())){
						$("#"+id+"_percentage").val(0);
						alert("请输入数字");
					}
					obj[i].percentage=$("#"+id+"_percentage").val();
					
				}
				if(id==""){
					if(isNaN($("#"+selRowId+"_percentage").val())){
						$("#"+selRowId+"_percentage").val(0);
						alert("请输入数字");
					}
					obj[i].percentage=$("#"+selRowId+"_percentage").val();
					
				}
				if(i==0){
					ret = parseInt(obj[i].percentage);
				}else{
					ret += parseInt(obj[i].percentage);
				}
			}
			$("#ratio").val(parseInt(ret)+"%");
		} 
		//数据加载完事件
		$.extend($.jgrid.defaults,{
			loadComplete : function(obj){
				setPercentage();
			}
		});
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<div class="opt-btn">
				<security:authorize ifAnyGranted="averageMaterial_save">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
						<button class="btn" onclick="customSave('list');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="averageMaterial_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="averageMaterial_export">
						<button class="btn" onclick="iMatrix.export_Data('${supplierctx}/gpmaterial/sub/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
					<input type="hidden" name="sid" id="sid" value="${sid }"/>
					<span style="float:right;">总比率:
					<input style="float:right;" name="ratio" id="ratio" value="${ratio }"/></span>
					
			</div>
			<div id="message">
				<s:actionmessage theme="mytheme" />
			</div>
<!-- 			<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script> -->
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="list" url="${gpctx}/averageMaterial/sub/list-datas.htm?sid=${sid}"
						submitForm="defaultForm" code="GP_SUBSTANCE"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>