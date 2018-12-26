<%@page import="com.ambition.supplier.entity.EvaluatingIndicator"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">	
	function createEstimateDegree(){
		openEstimateDegree();
	}
	function editEstimateDegree(){
		var ids=jQuery("#estimateDegreeList").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择需要编辑的记录！");
			return;
		}else if(ids.length>1){
			alert("请不要选择多条记录！");
			return;
		}
		openEstimateDegree(ids[0]);
	}
	function deleteEstimateDegree(){
		var ids=jQuery("#estimateDegreeList").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择需要删除的记录！");
			return;
		}			
		if(confirm("确定要删除所选中的记录？")){
			$.post("${supplierctx}/manager/degree/delete.htm", 
					{deleteIds : ids.join(',')}, 
					function(data) {
						if(data){
							alert(data);
						}else{
							//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
							while (ids.length>0) {
								jQuery("#estimateDegreeList").jqGrid('delRowData', ids[0]);
							}
							jQuery("#estimateDegreeList").trigger("reloadGrid");
						}
					}
			);
		}
	}
	function openEstimateDegree(id){
		var url='${supplierctx}/manager/degree/degree-input.htm';
		if(id!=undefined){
			url=url+'?id='+id;
		}
		$.colorbox({href:url,iframe:true, innerWidth:400, innerHeight:300,
			overlayClose:false,
			onClosed:function(){
				jQuery("#estimateDegreeList").trigger("reloadGrid");
			},
			title:"等级及红黄牌规则"
		});
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
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="estimate";
		var thirdMenu="_estimate_degree";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-estimate-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="manager-degree-input">
					<button class="btn" onclick="createEstimateDegree();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button> 
					</security:authorize>
					<security:authorize ifAnyGranted="manager-degree-input">
					<button class="btn" onclick="editEstimateDegree();"><span><span><b class="btn-icons btn-icons-edit"></b>编辑</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="manager-degree-delete">
					<button class="btn" onclick="deleteEstimateDegree();"><span><span><b class="btn-icons btn-icons-delete"></b>删除 </span></span></button>	
					</security:authorize>
				</div>
				<div style="display: none;" id="message">
					<font class=onSuccess>
					<nobr>删除成功</nobr></font>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="estimateDegreeList" url="${supplierctx}/manager/degree/list-datas.htm" code="SUPPLIER_WARN_SIGN"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>

</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>