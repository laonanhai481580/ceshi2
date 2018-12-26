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
	//导出
	function exportSuppliers(){
		var state = '<%=Supplier.STATE_QUALIFIED%>';
		$("#contentForm").attr("action","${supplierctx}/archives/exports.htm?state="+state);
		$("#contentForm").submit();
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
	//导入
	function imports(){
		var url = '${supplierctx}/archives/import-supplier-form.htm';
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入供应商",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
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
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">

<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="archives-qualified-add">
				   <button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>				
<!-- 				<security:authorize ifAnyGranted="archives-qualified-delete"> -->
<!-- 				    <button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>  -->
<!-- 				</security:authorize>				 -->
				<security:authorize ifAnyGranted="archives-qualified-list-datas">
				    <button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				</security:authorize>				
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliersProducts" url="${supplierctx}/archives/products/list-product-datas.htm?parentId=${id }" code="SUPPLIER_SUPPLY_PRODUCT" ></grid:jqGrid>
				</form>
			</div>
	</div>
	</div>

</body>
</html>