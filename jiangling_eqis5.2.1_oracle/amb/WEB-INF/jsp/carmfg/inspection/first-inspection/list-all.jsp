<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	var batchNo = "<%=request.getParameter("batchNo")==null?"":request.getParameter("batchNo")%>";
	jQuery.extend($.jgrid.defaults,{
		beforeRequest : function(){
			if(batchNo){
				return false;
			}
			return true;
		}
	});
	
	function click(cellvalue, options, rowObject){	
		return "<a href='${iqcctx}/inspection-report/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	var dutySupplierId = null;
	function supplierNameClick(obj){
		dutySupplierId = obj.currentInputId;
		var url='${supplierctx}';
		$.colorbox({href:url+"/archives/select-supplier.htm",
			iframe:true, 
			width:$(window).width()<1000?$(window).width()-100:1000, 
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#" + dutySupplierId).val(obj.name);
		params.supplierId = obj.id;
	}
	
	var materialNameId = null;
	function checkBomCodeClick(obj){
		var url = '${mfgctx}';
		materialNameId = obj.currentInputId;
		$.colorbox({href:url+"/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	
	function checkBomNameClick(obj){
		var url = '${mfgctx}';
		materialNameId = obj.currentInputId;
		$.colorbox({href:url+"/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	
	function setBomValue(datas){
		var rowId = materialNameId.split("_")[0];
		$("#" + rowId + "_checkBomCode").val(datas[0].key);
		$("#" + rowId + "_checkBomName").val(datas[0].value);
	}
	
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		data.isLedger = true;
		return data;
	}
	//业务判断
	function checkAmount(value,colname){
		var curId = $("#dynamicInspection").jqGrid('getGridParam','selrow');
		var inspectionAmount = $('#'+curId+'_inspectionAmount','#dynamicInspection').val();
		var qualifiedAmount = $('#'+curId+'_qualifiedAmount','#dynamicInspection').val();
		var unqualifiedAmount = $('#'+curId+'_unqualifiedAmount','#dynamicInspection').val();
		if(parseInt(inspectionAmount)!=parseInt(qualifiedAmount)+parseInt(unqualifiedAmount)){
			return [false,"不正确，检验数量不等于合格数和不良数之和！"];
		}else{
			return [true,""];
		}
	}
	
	//业务判断
	function checkStockAmount(value,colname){
		var curId = $("#dynamicInspection").jqGrid('getGridParam','selrow');
		var inspectionAmount = $('#'+curId+'_inspectionAmount','#dynamicInspection').val();
		var stockAmount = $('#'+curId+'_stockAmount','#dynamicInspection').val();
		if(parseInt(inspectionAmount)>parseInt(stockAmount)){
			return [false,"不正确，来料数不能小于检验数！"];
		}else{
			return [true,""];
		}
	}
	
	function inspectorClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.rowid+"_inspector",
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	} 
	//导入
	function imports(){
		var url = '${iqcctx}/inspection-report/imports.htm';
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入",
			onClosed:function(){
			}
		});
	}

	function $afterrestorefunc(rowId){
		if(rowId > 0){
			$("#" + rowId + " .upload").hide();
			var rowData = $("#dynamicInspection").jqGrid("getRowData",rowId);
			var rate = rowData.qualifiedAmount/(rowData.inspectionAmount*1.0);
			$("#dynamicInspection").jqGrid("setRowData",rowId,{qualifiedRate:rate,inspectionConclusion:rate<100?"NG":"OK"});
		}
	}
	//重写(给单元格绑定事件)
	var params = {};
	function $oneditfunc(rowid){
		params = {
			hisAttachmentFiles : $("#" + rowid + "_hiddenAttachmentFiles").val()
		};
		$("#" + rowid + " .upload").show();

		jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').change(function(){
			var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#dynamicInspection').val();
			var val = this.value;
			if(inspectionAmount&&!isNaN(inspectionAmount)&&val&&!isNaN(val)){
				if(val>inspectionAmount){
					val = inspectionAmount;
					this.value = val;
				}
				jQuery('#'+rowid+'_unqualifiedAmount','#dynamicInspection').val(inspectionAmount - val);
				caclute(rowid);
			}
		});
		jQuery('#'+rowid+'_unqualifiedAmount','#dynamicInspection').change(function(){
			var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#dynamicInspection').val();
			var val = this.value;
			if(inspectionAmount&&!isNaN(inspectionAmount)&&val&&!isNaN(val)){
				if(val>inspectionAmount){
					val = inspectionAmount;
					this.value = val;
				}
				jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val(inspectionAmount - val);
				caclute(rowid);
			}
		});
		jQuery('#'+rowid+'_inspectionAmount','#dynamicInspection').change(function(){
			var unqualifiedAmount = jQuery('#'+rowid+'_unqualifiedAmount','#dynamicInspection').val();
			var qualifiedAmount = jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val();
			var val = this.value;
			if(unqualifiedAmount&&!isNaN(unqualifiedAmount)&&val&&!isNaN(val)&&qualifiedAmount&&!isNaN(qualifiedAmount)){
				if(val<unqualifiedAmount){
					unqualifiedAmount = val;
					jQuery('#'+rowid+'_unqualifiedAmount','#dynamicInspection').val(qualifiedAmount);
				}
				jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val(val - unqualifiedAmount);
				caclute(rowid);
			}
		});
		//获取编号
		if(rowid == 0){
			$("#dynamicInspection").jqGrid("setRowData",rowid,{inspectionNo:'正在生成编号...'});
			$.post("${iqcctx}/inspection-report/get-inspection-no.htm",{},function(result){
				$("#dynamicInspection").jqGrid("setRowData",rowid,{inspectionNo:result});
			});
		}
	}
	
	//系统计算合格率和不良值
	function caclute(rowid){
		var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#dynamicInspection').val();
		var qualifiedAmount = jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val();
		var qualifiedRate = (qualifiedAmount/inspectionAmount)*100;
		jQuery('#'+rowid+'_qualifiedRate','#dynamicInspection').val(qualifiedRate.toFixed(1));
		$("#" + rowid + " .rate").html(qualifiedRate.toFixed(1) + "%");
		if(qualifiedRate<100){
			$("#dynamicInspection").jqGrid("setRowData",rowid,{inspectionConclusion:"NG"});
			params.inspectionConclusion = "NG";
		}else{
			$("#dynamicInspection").jqGrid("setRowData",rowid,{inspectionConclusion:"OK"});
			params.inspectionConclusion = "OK";
		}
	}
	
	function formatRate(cellvalue, options, obj){
		if(cellvalue){
			var rate = cellvalue*100;
			if(obj.inspectionAmount != undefined && obj.qualifiedAmount != undefined){
				rate = obj.qualifiedAmount*100.0/obj.inspectionAmount;
			}
			if(rate.toFixed){
				rate = rate.toFixed(2);
			}
			var operations = "<div style='text-align:left;' class='rate'>"+rate+"%</div>";
			return operations;
		}else{
			return "<div style='text-align:left;' class='rate'></div>";
		}
	}
	function formateAttachmentFiles(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	
	function beginUpload(rowId){
		$.upload({
			showInputId : rowId + "_showAttachmentFiles",
			hiddenInputId : rowId + "_hiddenAttachmentFiles",
			callback : function(files){
				params.attachmentFiles = $("#" + rowId + "_hiddenAttachmentFiles").val();
			}
		});
	}
	function inspectionNoFormatter(value,o,rowObj){
		return '<a href="${mfgctx}/inspection/first-inspection/input.htm?id='+rowObj.id+'">'+value+'</a>';
	}
	//比率格式化
	function rateFormatter(value){
		if(isNaN(value)){
			return "";
		}else{
			return (parseFloat(value)*100).toFixed(2) + "%";
		}
	}
	<jsp:include page="re-check.jsp" />
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_list';
		var thirdMenu="firstInspectionRecord";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inspection-list-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="">
					<!--<button class='btn' onclick="window.location='${mfgctx}/inspection/first-inspection/input.htm'" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button> -->
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_INSPECTION_FIRST-INSPECTION_LIST-ALL_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_INSPECTION_AUDIT">
					<button class='btn' onclick="reCheckList();" type="button"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_INSPECTION_FIRST-INSPECTION_LIST-ALL_EXPORT">
					<button  class='btn' onclick="iMatrix.export_Data('${mfgctx}/inspection/export/export-first-inspection-list-all.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					</div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post" action="">
							<grid:jqGrid gridId="dynamicInspection"  url="${mfgctx}/inspection/first-inspection/list-datas.htm" code="MFG_FIRST_INSPECTION_ALL" pageName="page"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>