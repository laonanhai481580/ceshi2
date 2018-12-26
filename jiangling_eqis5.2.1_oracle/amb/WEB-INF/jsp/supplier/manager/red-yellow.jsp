<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	var myLayout;
	$(document).ready(function(){
		myLayout = $('body').layout({
			north__paneSelector : '#header',
			north__size : 66,
			west__size : 250,
			north__spacing_open : 31,
			north__spacing_closed : 31,
			west__spacing_open : 6,
			west__spacing_closed : 6,
			center__minSize : 400,
			resizable : false,
			paneClass : 'ui-layout-panel',
			north__resizerClass : 'ui-layout-resizer',
			west__onresize : $.layout.callbacks.resizePaneAccordions,
			center__onresize : contentResize
		});
	});
</script>
<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
<script type="text/javascript" src="${ctx}/js/search.js"></script>
<script type="text/javascript">
	var topMenu='';
	jQuery.extend($.jgrid.defaults,{
		prmNames:{
			rows:'signPage.pageSize',
			page:'signPage.pageNo',
			sort:'signPage.orderBy',
			order:'signPage.order'
		}
	});
	$(document).ready(function(){
		$(":input[name=evaluateYear]").attr("dataType","INTEGER");
	});
	//导出
	function exportSupplier(){
		$("#contentForm").attr("action","${supplierctx}/manager/sign-exports.htm");
		$("#contentForm").submit();
	}
	//格式化
	function operateFormater(cellValue,options,rowObj){
		if(cellValue=='yellow'){
			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
		}else if(cellValue=='red'){
			return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
		}else{
			return '';
		}
	}
	//制定监察计划
	function importSomeInspectionPlan() {
		var supplierId = new Array();
		var ids = jQuery("#suppliers").getGridParam('selarrrow');
		for(var i=0;i<ids.length;i++){
			var rowData = $("#suppliers").getRowData(ids[i]); 
			supplierId[i]=rowData['supplier.id'];
		}
		if(ids.length<1){
			alert("请选中需要导入的记录！");
			return;
		}
		$.post("${supplierctx}/supervision/check-plan/import-check-plan.htm", {
			deleteIds : supplierId.join(',')
		}, function(data) {
			window.location="${supplierctx}/supervision/check-plan/list.htm";
	});

	}
</script>

</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="manager";
		var thirdMenu="_red_yellow";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-manager-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" style="line-height:30px;">
				<security:authorize ifAnyGranted="manager-degree-red-yellow-datas">
				<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				</security:authorize>
				<security:authorize ifAnyGranted="manager-red-yellow-export">
				<button class="btn" onclick="exportSupplier();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="manager-import-plan">
				<button class="btn" onclick="importSomeInspectionPlan();"><span><span><b class="btn-icons btn-icons-add"></b>制定监察计划</span></span></button>
				</security:authorize>
				<span style="display:block;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
						url="${supplierctx}/manager/red-yellow-datas.htm" code="SUPPLIER_SUPPLIER_GOAL"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>