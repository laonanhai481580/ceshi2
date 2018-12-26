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
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		<%
			String ortIndicatorId = request.getParameter("ortIndicatorId");
			if(StringUtils.isNotEmpty(ortIndicatorId)){
		%>
		function $addGridOption(jqGridOption){
			jqGridOption.postData.ortIndicatorId=<%=ortIndicatorId%>;
		}
		<%}%>
		function $processRowData(data){
			data.ortIndicatorId = '<%=ortIndicatorId%>';
			return data;
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="EPM_ORT_ITEM_SAVE">
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
		//导入台账数据
		function importDatas(){
			var url = encodeURI('${epmctx}/base-info/ort-item/ortItem-import.htm?ortIndicatorId=${ortIndicatorId}');
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#ortItemList").trigger("reloadGrid");
				}
			});
		}
		function downloadTemplate(){
			window.location = '${epmctx}/base-info/ort-item/download-template.htm';
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base";
		var thirdMenu="ortItem";
		var treeMenu="ortItem";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/epm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/epm-base-info-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="EPM_ORT_ITEM_SAVE">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="EPM_ORT_ITEM_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="EPM_ORT_ITEM_EXPORT">
					<button  class='btn' type="button" onclick="iMatrix.export_Data('${epmctx}/base-info/ort-item/export.htm?ortIndicatorId='+<%=ortIndicatorId%>);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					<security:authorize ifAnyGranted="EPM_ORT_ITEM_IMPORT">
						<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
						<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.Esc可退出编辑</span></span>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="ortItemList" url="${epmctx}/base-info/ort-item/list-datas.htm" code="EPM_ORT_ITEM"></grid:jqGrid>		
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>