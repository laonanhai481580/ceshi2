<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.supplier.entity.Supplier"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp" %>
	<c:set var="actionBaseCtx" value="${supplierctx}/audit/year"/>
	<script type="text/javascript">
	function createForm(){
		window.location="${actionBaseCtx}/input.htm";	
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='${actionBaseCtx}/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}

	function stageFormatter(value,options, rowObject){
		var launchState = rowObject.launchState; 
		var changeWorkFlowColor = rowObject.changeWorkFlowColor;
		if(changeWorkFlowColor=='red'){
			return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/red.gif'/></div>";
		}else{
			if(launchState){
				var colName = options.colModel.name;
				if(launchState.indexOf(colName)>-1){
					var reg = new RegExp(colName + "$");
					if(reg.test(launchState)){
						return "<div style='text-align:center;margin-left:-10px;color:green;'>办理中...</div>";
					}else{
						return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/green.gif'/></div>";					
					}
				}else{
					return '';
				}
			}else{
				return "";
			}
		}
	}
	

	function viewProcessInfo(value,o,obj){
		var strs = "";
		strs = "<div style='width:100%;text-align:center;' title='查看流转历史' ><a class=\"small-button-bg\"  onclick=\"_viewProcessInfo("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
		return strs;
	}
	function _viewProcessInfo(formId){
		$.colorbox({href:'${actionBaseCtx}/view-info.htm?id='+formId,iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"表单"
		});
	}
function click(cellvalue, options, rowObject){	
	return "<a href='${actionBaseCtx}/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
}
function initProductDate(){
	$(":input[name=year][datatype=INTEGER]").val(year);
	if(!window.initFormProductDate){
		window.initFormProductDate = true;
		var myDate = new Date();
		var year = myDate.getFullYear();
	}
}
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
		return data;
	}
	//修改供应商信息
	function editInfo(id){
		$.colorbox({
			href:'${supplierctx}/archives/input.htm?id='+id,
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
		var state = '';
// 		$("#contentForm").attr("action","${supplierctx}/archives/exports.htm?state="+state);
// 		$("#contentForm").submit();
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
	//导入
	function imports(){
		var url = '${supplierctx}/archives/import-supplier-form.htm';
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
	//一次性导入供应商准入物料
	function initAdmittanceMaterial(){
		var url = '${supplierctx}/archives/import-supplier-material-form.htm';
		$.colorbox({href:url,iframe:true, 
			innerWidth:350, innerHeight:240,
			overlayClose:false,
			title:"导入供应商准入物料",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	
 	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="archives-input">
			isRight =  true;
		</security:authorize>
		return isRight;
	}
 	
	function updateSupplierNewCode(){
		var url = '${supplierctx}/archives/import-supplier-new-code-form.htm';
		$.colorbox({href:url,iframe:true, 
			innerWidth:350, innerHeight:240,
			overlayClose:false,
			title:"更新供应商编码",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	function addNew(){
		var ids = $("#suppliers").jqGrid("getGridParam","selarrrow");
		var url = '${supplierctx}/base-info/level-score/input.htm';
		var title = "新建";
		if(ids.length>1){
			alert("只能选择一条进行编辑!");
			return;
		}else if(ids.length==1){
			title = "编辑";
			url += "?id="+ ids;
		}
		$.colorbox({href:url,iframe:true, 
			innerWidth:650, innerHeight:240,
			overlayClose:false,
			title:"新建/编辑",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
			}
		});
	}
	function formateNum(cellValue,option,rowObj){
		if(cellValue!=null&&cellValue!=''&&cellValue!='undefined'&&cellValue!='&nbsp;'){
			return cellValue * 100 +"%";
		}else{
			return "";
		}
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
		if(row.supplierId=="undefined"||row.supplierId.length==0||row.supplierId=="&nbsp;"){
			alert("该供应商不是合格供应商");
			return;
		}
		url += "?supplierId="+ row.supplierId;
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
	//上传附件
	function formateMessageFile(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="audit";
		var thirdMenu="audtiHistoryList";
	</script>


	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-audit-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
			   <security:authorize ifAnyGranted="supplier-year-check-delete">
				    <button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
			</div>
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
						url="${supplierctx}/audit/year/history-list-datas.htm" code="SUPPLIER_YEAR_CHECK"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>