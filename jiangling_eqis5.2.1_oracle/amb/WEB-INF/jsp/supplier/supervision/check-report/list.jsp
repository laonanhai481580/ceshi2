<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
	<script src="${ctx}/widgets/validation/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	var params = {},hisParams = {};
	/* function contentResize(){
		var tableWidth=_getTableWidth();
		jQuery("#checkReports").jqGrid('setGridWidth',tableWidth);
		var tableHeight=$('.ui-layout-center').height()-122-$("#searchDiv").height();
		jQuery("#checkReports").jqGrid('setGridHeight',tableHeight);
	} */
	$(document).ready(function(){
		setTimeout(function(){
			$("#checkReports").jqGrid("setGridParam",{
				gridComplete : function(){
					contentResize();
				}
			});
			contentResize();
		},200);
	});
	//重写保存后的方法
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	//重写编辑的方法
	function $oneditfunc(rowId){
		var rowData = $("#checkReports").jqGrid("getRowData",rowId);
		rowData = rowData?rowData:{};
		hisParams = $.extend(true,{},rowData);
		params = {};
		if(rowId == 0){
			$.post("${supplierctx}/supervision/check-report/generate-check-report-code.htm",{},function(result){
				$("#checkReports").jqGrid("setRowData",rowId,{code:result});
			});
		}
	}
	//重写取消方法
	function $afterrestorefunc(rowId){
		if(rowId > 0){
			for(var pro in params){
				params[pro] = hisParams[pro];
			}	
			$("#checkReports").jqGrid("setRowData",rowId,params);
		}
	}
	//重写序列化数据
	function $processRowData(data){
		$.extend(data,params);
		return data;
	}
	/////////////////供应商选择//////////////////////////////////
	var selObjId = '';
	function supplierNameClick(obj){
		selObjId = obj.currentInputId;
		var url='${supplierctx}/archives/select-supplier.htm';
		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:480,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		if(selObjId){
			var rowId = selObjId.split("_")[0];
			var hisValue = $("#" + selObjId).val();
			if(obj.name != hisValue){
				params.supplierId = obj.id;
				params.supplierCode = obj.code;
				$("#" + selObjId).val(obj.name);
			}
			$("#" + selObjId).focus();
		}
	}
	/////////////////供应商选择结束//////////////////////////////////
	
	////////////////选择物料BOM////////////////////////////////////
	function checkBomNameClick(obj){
		selObjId = obj.currentInputId;
		var url = '${mfgctx}/common/product-bom-select.htm';
		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:400,
			overlayClose:false,
			title:"选择物料BOM"
		});
	}
	function setBomValue(objs){
		var obj = objs[0];
		if(selObjId){
			var rowId = selObjId.split("_")[0];
			var hisValue = $("#" + selObjId).val();
			if(obj.value != hisValue){
				params.checkBomCode = obj.key;
				$("#checkReports").jqGrid("setRowData",rowId,{checkBomCode:obj.key});
				$("#" + selObjId).val(obj.value);
			}
			$("#" + selObjId).focus();
		}
	}
	////////////////选择物料BOM结束////////////////////////////////////
	
	//操作格式化///////////////////////////////////////////////////
	function operateFormatter(value,options,rowData){
		if(value>0){
			return "<div style='text-align:center;'><a title='查看监察报告'  class='small-button-bg' onclick='openViewInfo("+value+");'><span class='ui-icon ui-icon-info' style='cursor:pointer;'></span></a></div>";
		}else{
			return '';
		}
	}
	function openViewInfo(value){
		var url = '${supplierctx}/supervision/check-report/view-info.htm?id='+value;
		$.colorbox({href:url,iframe:true, innerWidth:900, innerHeight:550,
			overlayClose:false,
			title:"监察报告"
		});
	}
	//查看评分表
	function goToNewLocationById(id){
		window.location="${supplierctx}/supervision/check-report/input.htm?id="+id;

	}
	//操作格式化结束///////////////////////////////////////////////////
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="supervision";
		var thirdMenu="_check_report";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-supervision-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="supervision-check-delete">
				<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button> 
				</security:authorize>				
				<security:authorize ifAnyGranted="supervision-check-list-datas">
				<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				</security:authorize>				
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="checkReports"
						url="${supplierctx}/supervision/check-report/list-datas.htm" code="SUPPLIER_CHECK_REPORT"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>