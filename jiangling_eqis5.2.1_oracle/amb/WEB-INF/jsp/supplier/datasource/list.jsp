<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.supplier.entity.Supplier"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	var params = {};
	//添加/修改数据来源
	function editInfo(isEdit){
		var id = "";
		if(isEdit){
			id = $("#dataSources").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请先选择数据来源!");
				return;
			}
		}
		$.colorbox({
			href:'${supplierctx}/datasource/input.htm?id='+id,
			iframe:true, 
			width:$(window).width()<900?$(window).width()-100:900, 
			height:$(window).height()<780?$(window).height()-100:780,
			overlayClose:false,
			title:"数据来源维护",
			onClosed:function(){
				$("#dataSources").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="_data_source";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-base-info-menu.jsp" %>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="data-source-input">
					<button class='btn' onclick="editInfo();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="data-source-input">
					<button class='btn' onclick="editInfo(true);"><span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="data-source-delete">
				<button class='btn' onclick="javascript:iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="data-source-datas">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				</security:authorize>
			</div>
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="dataSources"
						url="${supplierctx}/datasource/list-datas.htm" code="SUPPLIER_EVALUATE_DATA_SOURCE"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>