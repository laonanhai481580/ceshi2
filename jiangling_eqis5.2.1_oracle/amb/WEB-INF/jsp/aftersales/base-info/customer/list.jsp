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
			String customerListId = request.getParameter("customerListId");
			if(StringUtils.isNotEmpty(customerListId)){
		%>
		function $addGridOption(jqGridOption){
			jqGridOption.postData.customerListId=<%=customerListId%>;
		}
		<%}%>
		function $processRowData(data){
			data.customerListId = '<%=customerListId%>';
			return data;
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="AFS_OFILMMODEL_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		//导入
		function imports(){
			var url = '${aftersalesctx}/base-info/customer/imports.htm?customerListId=${customerListId}';
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入",
				onClosed:function(){
					$("#defectionItemList").trigger("reloadGrid");
				}
			});
		}	
		function downloadTemplate(){
			window.location = '${aftersalesctx}/base-info/customer/download-template.htm';
		}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="customer";
		var treeMenu="_customer";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-baseinfo-third-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="AFS_OFILMMODEL_SAVE">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="AFS_OFILMMODEL_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="AFS_OFILMMODEL_EXPORT">
					<button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
					<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
					<button  class='btn' type="button" onclick="iMatrix.export_Data('${aftersalesctx}/base-info/defection-item/exportCode2.htm?customerListId='+<%=customerListId%>);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="defectionItemList" url="${aftersalesctx}/base-info/customer/list-datas.htm" code="AFS_OFILM_MODEL"></grid:jqGrid>		
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>