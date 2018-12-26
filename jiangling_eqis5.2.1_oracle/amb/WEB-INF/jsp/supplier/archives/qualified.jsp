<%@page import="com.ambition.supplier.entity.Supplier"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
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

	var params = {};
	function $oneditfunc(rowId){
		params = {};
		var obj = $("#" + rowId + "_qualityModelId");
		if(obj.length>0){
			params.qualityModelName = obj.bind("change",function(){
				params.qualityModelName = $(this).find("option:selected").html();
			}).find("option:selected").html();
		}
		obj = $("#" + rowId + "_estimateModelId");
		if(obj.length>0){
			params.estimateModelName = obj.bind("change",function(){
				params.estimateModelName = $(this).find("option:selected").html();
			}).find("option:selected").html();
		}
	}
	
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		data.state = '<%=Supplier.STATE_QUALIFIED%>';
		return data;
	}
	
	//修改供应商信息
	function editInfo(id){
		$.colorbox({
			href:'${supplierctx}/archives/input.htm?state=<%=Supplier.STATE_QUALIFIED%>&id='+id,
			iframe:true, 
			width:$(window).width()<900?$(window).width()-100:900, 
			height:$(window).height()<680?$(window).height()-100:680,
			overlayClose:false,
			title:"供应商信息",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	
	function editData(){
		var ids = $("#suppliers").jqGrid("getGridParam","selarrrow");
		if(ids.length>1){
			alert("只能选择一条进行编辑!");
			return;
		}
		var url = '${supplierctx}/audit/year/input.htm';
		var title = "编辑";
		var row = $("#suppliers").jqGrid('getRowData',ids[0]);
		if(row.id=="undefined"||row.id.length==0||row.id=="&nbsp;"){
			alert("该供应商不是合格供应商");
			return;
		}
		url += "?id="+ row.Id;
		$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<1100?$(window).width()-100:1100, 
			innerHeight:$(window).height()-80,
			overlayClose:false,
			title:title,
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
			}
		});
	};
	//导出
	function exportSuppliers(){
		var state = '<%=Supplier.STATE_QUALIFIED%>';
		iMatrix.export_Data("${supplierctx}/archives/exports.htm?state="+state);
	}
	//格式化
	function operateFormater(cellValue,options,rowObj){
		if(rowObj.id){
			var operations = "<div style='text-align:center;' title='修改详细信息'><a class=\"small-button-bg\" onclick=\"editInfo("+cellValue+");\" href=\"#\"><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
			return operations;
		}else{
			return '';
		}
	}
	
	//停用
	function stopSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要停用的供应商！");
			return;
		}else if(ids.length>0){
			if(confirm("确定要停用所选的供应商吗？")){
				$.post("${supplierctx}/archives/update-stop.htm?deleteIds="+ids.join(","), {}, function(result) {
					if(result.error){
						alert(result.message);
					}else{
						jQuery("#suppliers").clearGridData();
						jQuery("#suppliers").trigger("reloadGrid");
					}
				});
			}
		}
		
	}
	//禁用
	function disableSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要禁用的供应商！");
			return;
		}else if(ids.length>0){
			if(confirm("确定要禁用所选的供应商吗？")){
				$.post("${supplierctx}/archives/update-disable.htm?deleteIds="+ids.join(","), {}, function(result) {
					if(result.error){
						alert(result.message);
					}else{
						jQuery("#suppliers").clearGridData();
						jQuery("#suppliers").trigger("reloadGrid");
					}
				});
			}
		}
	}
	//恢复
	function restoreSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要恢复的供应商！");
			return;
		}else if(ids.length>0){
			if(confirm("确定要恢复所选的供应商吗？")){
				$.post("${supplierctx}/archives/update-restore.htm?deleteIds="+ids.join(","), {}, function(result) {
					if(result.error){
						alert(result.message);
					}else{
						jQuery("#suppliers").clearGridData();
						jQuery("#suppliers").trigger("reloadGrid");
					}
				});
			}
		}
	}
	//淘汰供应商
	function washOutSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要淘汰的供应商！");
			return;
		}else if(ids.length>0){
			$.post("${supplierctx}/archives/wash-out-supplier.htm?deleteIds="+ids.join(","), {}, function(result) {
				if(result.error){
					alert(result.message);
				}else{
					jQuery("#suppliers").trigger("reloadGrid");
				}
			});
		}
	}
	function editInfo(id){
		$.colorbox({
			href:'${supplierctx}/archives/input.htm?id='+id,
			iframe:true, 
			width:$(window).width()<1500?$(window).width()-100:1500, 
			height:$(window).height()<1380?$(window).height()-100:1380,
			overlayClose:false,
			title:"供应商信息",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	//导入
	function imports(){
		var url = '${supplierctx}/archives/import-form.htm';
		$.colorbox({href:url,iframe:true, 
			innerWidth:350, innerHeight:240,
			overlayClose:false,
			title:"导入供应商",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	//导出模板
	function downloadExcel(){
		$("#iframe").bind("readystatechange",function(){
			$("#iframe").unbind("readystatechange");
		}).attr("src","${supplierctx}/archives/download-excel.htm");
	}
	function sendCalcleSuppliers(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要取消的供应商！");
			return;
		}else if(ids.length>1){
			alert("只能选一条！");
		}else{
			window.open("${supplierctx}/archives/supplier-cancle/input.htm?supplierId=" + ids[0]);	
		}
	}
	function sendEvaluateSuppliers(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		var row = $("#suppliers").jqGrid('getRowData',ids[0]);
		var estimateModelId = row.estimateModelId;
		var materialType = row.materialType;
		if(estimateModelId.length==0){
			alert("请先设置评价模型");
			return;
		}
		if(ids.length<=0){
			alert("请选择要评价的供应商！");
			return;
		}else if(ids.length>1){
			alert("只能选一条！");
		}else{
			window.open("${supplierctx}/evaluate/quarter/add.htm?supplierId=" + ids[0]+"&estimateModelId="+estimateModelId+"&materialType="+materialType);	
		}
	}
	var rowId=null;
	function supplyMaterialClick(obj){
		rowId=obj.rowid;
		$.colorbox({href:"${iqcctx}/inspection-base/inspection-bom/bom-inspection-mulit-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
	}
	function setInspectionBomValue(datas){
		var materielName = "";
		for(var i=0;i<datas.length;i++){
			if(materielName.length==0){
				materielName = datas[i].materielName;
			}else{
				materielName += ";" + datas[i].materielName;
			}
		}
		$("#"+rowId+"_supplyMaterial").val(materielName);
 	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="archives";
		var thirdMenu="_qualified";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-supplier-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="archives-qualified-add">
				<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="archives-qualified-delete">
				<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button> 
				</security:authorize>				
				<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				<security:authorize ifAnyGranted="archives-qualified-exports">
				<button class="btn" onclick="exportSuppliers();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>				
			    <security:authorize ifAnyGranted="archives-qualified-imports">
					<button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导入</span></span></button>
					<button class='btn' onclick="downloadExcel();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>下载模板</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="archives-qualified-send-cancle">
				    <button class="btn" onclick="sendCalcleSuppliers();"><span><span><b class="btn-icons btn-icons-audit"></b>合格供应商取消申请</span></span></button>
				</security:authorize>
<%-- 				<security:authorize ifAnyGranted="archives-qualified-evaluate-add"> --%>
				    <button class="btn" onclick="sendEvaluateSuppliers();"><span><span><b class="btn-icons btn-icons-audit"></b>发起评价</span></span></button>
<%-- 				</security:authorize> --%>
<%-- 				<security:authorize ifAnyGranted="archives-qualified-send-create"> --%>
<!-- 				    <button class="btn" onclick="editData();"><span><span><b class="btn-icons btn-icons-audit"></b>生成年度稽核计划</span></span></button> -->
<%-- 				</security:authorize> --%>
				<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
						url="${supplierctx}/archives/qualified-list-datas.htm" code="SUPPLIER_SUPPLIER_QUALIFIED"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>

</body>
</html>