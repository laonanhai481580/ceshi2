<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.supplier.entity.Supplier"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
//确定
function realSelect(id){
	var ids = [];
	if(id){
		ids.push(id);
	}else{
		var id = $("#suppliers").jqGrid("getGridParam","selrow");
		if(id){
			ids.push(id);
		}
	}
	if(ids.length == 0){
		alert("请选择报检物料!");
		return;
	}
	 if($.isFunction(window.parent.setMaterialTypeValue)){//获取简洁的值
		var objs = [];
		for(var i=0;i<ids.length;i++){
			var data = $("#suppliers").jqGrid('getRowData',ids[i]);
			if(data){
				objs.push({materialType:data.materialType});
			}
		}
		if(objs.length>0){
			window.parent.setMaterialTypeValue(objs);
			window.parent.$.colorbox.close();
		}else{
			alert("选择的值不存在!");
		}
	}else{
		alert("页面还没有 setMaterialTypeValue 方法!");
	};
}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			</div>
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
						url="${supplierctx}/base-info/material-type-goal/list-datas.htm" code="SUPPLIER_MATERIAL_TYPE_GOAL"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>