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
		if(response.responseText=="false"){
			alert("检验数不等于合格数加不良数！");
			return false;
		}else if(response.responseText=="false1"){
			alert("检验数必须小于来料数！");
			return false;
		}else if(response.responseText=="false2"){
			alert("不良数不等于不良细项中的不良数总和！");
			return false;
		}else if(response.responseText=="false3"){
			alert("不合格品处理单号已经存在！");
			return false;
		}else{
			return true;
			}
			
		}
	var materialNameId = null;
	function materialCodeClick(obj){
		materialNameId = obj.currentInputId;
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	function materialNameClick(obj){
		materialNameId = obj.currentInputId;
		var url = '${mfgctx}';
		$.colorbox({href:url+"/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	function setBomValue(datas){
		var id=materialNameId.split("_");
		$("#" + materialNameId).val(datas[0].value);
		$("#" + id[0]+"_materialCode").val(datas[0].key);
		$("#" + id[0]+"_materialName").val(datas[0].value);
 	}
	
	var dutySupplierId = null;
	function dutySupplierClick(obj){
		dutySupplierId = obj.currentInputId;
		var url='${supplierctx}'
		//var url='http://localhost:8082/supplier/evaluate/supplier-select.htm';
		$.colorbox({href:url+"/archives/select-supplier.htm",iframe:true, innerWidth:1000, innerHeight:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#" + dutySupplierId).val(obj.name);
	}
	//导入
	function imports(){
		var url = '${iqcctx}/defective-goods/imports.htm';
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入",
			onClosed:function(){
			}
		});
	}
	//重写(给单元格绑定事件)
	function $oneditfunc(rowid){
		var val = $("#" +rowid + "_unqualityNo").val();
		val = val.replace(/<[^<>]*>/g,'');
		if(val=='undefined'){
			val="";
		}
		$("#" +rowid + "_unqualityNo").val(val);
		jQuery('#'+rowid+'_inspectionAmount','#dynamicDefectiveGood').change(function(){
			caclute(rowid);
		});
		jQuery('#'+rowid+'_unqualifiedAmount','#dynamicDefectiveGood').change(function(){
			caclute(rowid);
		});
	}
	//系统计算合格率和不良值
	function caclute(rowid){
		var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#dynamicDefectiveGood').val();
		var unqualifiedAmount = jQuery('#'+rowid+'_unqualifiedAmount','#dynamicDefectiveGood').val();
		var qualifiedAmount = inspectionAmount-unqualifiedAmount;
		jQuery('#'+rowid+'_qualifiedAmount','#dynamicDefectiveGood').val(qualifiedAmount);
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='${iqcctx}/defective-goods/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	function showInfo(id){
		window.location="${iqcctx}/defective-goods/input.htm?id="+id;
	}
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="defective-save">
			isRight =  true;
		</security:authorize>
		return isRight;
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="defectiveReport";
	var thirdMenu="myDefectiveReport";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-defective-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="defective-save">
						<button class='btn' onclick="addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="defective-delete">
						<button class='btn' onclick="delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' onclick="showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="defective-export">
						<button class='btn' onclick="export_Data('${iqcctx}/defective-goods/export.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
<!-- 						<button class="sexybutton" ><span class="print">打印</span></button> -->
<!-- 						<button class="sexybutton" ><span class="reload">刷新</span></button> -->
<!-- 						<button class="sexybutton" ><span class="stat">统计</span></button> -->
<!-- 						<button class="sexybutton" ><span class="undo">重置</span></button> -->
<!-- 						<button class="sexybutton" ><span class="upload">上传</span></button> -->
<!-- 						<button class="sexybutton" ><span class="find">FIND</span></button> -->
<!-- 						<button class="sexybutton" ><span class="ok">提交</span></button> -->
<!-- 						<button class="sexybutton" ><span class="import">导入</span></button> -->
<!-- 						<button class="sexybutton" ><span class="copy">复制</span></button> -->
<!-- 						<button class="sexybutton" ><span class="edit">编辑</span></button> -->
<!-- 						<button class="sexybutton" ><span class="cancel">关闭</span></button> -->
<!-- 						<button class="sexybutton" ><span class="save">保存</span></button> -->
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicDefectiveGood" url="${iqcctx}/defective-goods/list-datas.htm" code="IQC_DGR" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
						<script type="text/javascript">
						$(document).ready(function(){
							$("#dynamicDefectiveGood").jqGrid('setGroupHeaders', {
								useColSpanStyle: true, 
								groupHeaders:[{startColumnName:'params.bug1', numberOfColumns: 9, titleText: '不良细项'},{startColumnName:'composingItems.name0', numberOfColumns:15, titleText: '不良成本'}]
							});	
						});
						</script>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>