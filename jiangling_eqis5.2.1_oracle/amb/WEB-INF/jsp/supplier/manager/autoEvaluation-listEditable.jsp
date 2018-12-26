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
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	function $ondblClickRow(rowId,iRow,iCol,e){
		$("#" + rowId + "_supplierCode").attr("readonly","readonly");
		$("#" + rowId + "_supplierName").attr("readonly","readonly");
		$("#" + rowId + "_supplierCode").click(function(){
			selectSupplier(rowId);
		});
		$("#" + rowId + "_supplierName").click(function(){
			selectSupplier(rowId);
		});
	}
	
	function thisAddRow(byEnter) {
		if (byEnter == undefined) {
			byEnter = false;
		}
		if ((!editing && !byEnter) || byEnter) {
//			取消对其他行的选中，新增行会自动被选中
			editableGrid.resetSelection();
			editableGrid.jqGrid('addRow', {
				rowID : "0",
				position : "last",
				addRowParams : editParams
			});
			//新增一行时，当前选中行的id为0
			lastsel=0;
			makeEditable(false);
			selRowId = 0;
			$("#0"  + "_supplierCode").attr("readonly","readonly");
			$("#0"  + "_supplierName").attr("readonly","readonly");
			$("#0"  + "_supplierCode").click(function(){
				selectSupplier(0);
			});
			$("#0"  + "_supplierName").click(function(){
				selectSupplier(0);
			});
		}else{
			alert("请先完成编辑！");
		}
	}
	
	var selRowId;
	//选择责任供应商
	function selectSupplier(rowId){
		selRowId = rowId;
		$.colorbox({href:"${supplierctx}/archives/select-supplier.htm?multiselect=false",iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<700?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(datas){
		$("#" + selRowId + "_supplierName").val(datas[0].name);
		$("#" + selRowId + "_supplierCode").val(datas[0].code);
	}
	
	function import_Data(){
 		var url = '${supplierctx}/manager/import-form.htm';
		$.colorbox({
			href:url,
			iframe:true, 
			innerWidth:350, 
			innerHeight:200,
			overlayClose:false,
			title:"导入自动评价供应商清单",
			onClosed:function(){
				$("#supplierList").trigger("reloadGrid");
			}
		});
 	}
 	function autoCaculate(){
 		var ids = $("#supplierList").jqGrid("getGridParam","selarrrow");
 		if(ids.length==0){
 			alert("请选择数据!");
 			return;
 		}
 		if(!confirm("确定要继续吗?")){
 			return;
 		}
 		$.showMessage("正在自动计算,请稍候... ...","custom");
 		var url = '${supplierctx}/evaluate/quarter/auto-caculate.htm';
 		$.post(url,{deleteIds:ids.join(",")},function(result){
 			$.clearMessage();
 			if(result.error){
 				alert(result.message);
 			}else{
 				alert("成功!");
 			}
 		},'json');
 	}
	</script>
</head>

<body>
	<script type="text/javascript">
		var secMenu="estimate";
		var thirdMenu="_auto_evaluation";
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
						<button class='btn' onclick="thisAddRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
						<button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
						<button class='btn' onclick="import_Data();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
						<button class='btn' type="button" onclick="iMatrix.export_Data('${supplierctx}/manager/autoEvaluation-export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						<button class='btn' onclick="autoCaculate();" type="button"><span><span><b class="btn-icons btn-icons-auto"></b>自动计算</span></span></button>
						<span id="message" style="color:red;margin-left:8px;"></span>
					</div>
					
					<div id="opt-content">
					<form id="sForm" name="sForm" method="post"  action=""></form>
					<div id="message"><s:actionmessage theme="mytheme" /></div>	
					<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
						<form id="contentForm"  method="post"  action="">
							<grid:jqGrid gridId="supplierList" url="${supplierctx}/manager/autoEvaluation-listDatas.htm" submitForm="sForm"  code="SUPPLIER_AUTO_EVALUATION"></grid:jqGrid>
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