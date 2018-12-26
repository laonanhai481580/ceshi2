<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
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
// 			return "<a href='${iqcctx}/inspection-report/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
			return "<a href=\"#\" onclick=\"window.open('${iqcctx}/inspection-report/input.htm?id="+rowObject.id+"','检验报告');\">"+cellvalue+"</a>";
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
			var url = '${carmfgctx}';
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
			var url = '${carmfgctx}';
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
				return [false,"不正确，检验数不能大于来料数！"];
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
			enterKeyToNext("dynamicInspection",rowId,function(){
			},true);
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
		
		function formatInspectionConclusion(cellvalue, options, obj){
			if(obj.inspectionState=='待检验'){
				return "";
			}else{
				var showName = "";
				if(cellvalue=='OK'){
					showName= '合格';
				}else{
					showName= '不合格';
				}
				return showName;
			}
		}
		
		function formateAttachmentFiles(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
		
		function  icheckStatusFormate(value,o,obj){
			return "<span style='color:red;'>"+value+"</span>";
		}
		
		function  inspectionTypeFormate(value,o,obj){
			//K:'K-库存零件',S:'S-送检',KS:'KS-重新送检'
			var str = "";
			if(value=="K"){
				str= "<span style='color:red;'>K-库存零件</span>";
			}else if(value=="S"){
				str= "<span'>S-送检</span>";
			}else{
				str= "<span style='color:red;'>KS-重新送检</span>";
			}
			return str;
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
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="iqc-incomingInspectionActionsReport-listEditable">
				isRight =  true;
			</security:authorize>
			return isRight;
		}
		
		function createReport(){
			window.location='${iqcctx}/inspection-report/input.htm';
		}
		
	 	//发起不合格品处理流程
		function beginDefectiveGoodsProcessForm(){
	 		var ids = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
	 		if(ids.length>1){
	 			alert("只能选择一项！");
	 			return ;
	 		}else if(ids.length==0){
	 			alert("请选择一项！");
	 			return ;
	 		}
	 		var rowData = $("#dynamicInspection").jqGrid("getRowData",ids[0]);
	 		var unqualifiedSourceNo =$(rowData.inspectionNo).html();
	 		if(rowData.inspectionState=='已完成'&&rowData.inspectionConclusion=='NG'){
	 			$.post("${iqcctx}/inspection-report/re-check-unqualified.htm",{id:ids[0]},function(result){
					if(result.error){
						$("#message").html("<b style='color:red;'>"+result.message+"</b>");
						alert(result.message);				
					}else{
						if(confirm("您确定要发起不合格品处理吗？")){
				 			window.location.href = '${iqcctx}/defective-goods/input.htm?unqualifiedSourceNo='+unqualifiedSourceNo+'&unqualifiedSource=iqc';
				 		}else{
				 			return;
				 		}
					}
				},'json');
	 		}else{
	 			alert("检验状态为'已完成'且为'不合格'才能发起不合格！");
	 			return;
	 		}
			
		} 	
	 	//导出入库检验不良
		function exportChart(){
			var postData = $("#dynamicInspection").jqGrid("getGridParam","postData");
			var url = '${iqcctx}/inspection-report/export.htm?_list_code=IQC_IIAR&exportFlag=unList';
			if(postData.searchParameters){
				url += '&searchParameters=' + postData.searchParameters;
			}
			$("#contentForm1").attr("action",url);
			$("#contentForm1").submit();
		}
		<jsp:include page="audit-method.jsp" />
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionReport";
		var thirdMenu="unmyInspectionReport";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="iqc-incomingInspectionActionsReport-input">
<!-- 						<button class='btn' type="button" onclick="createReport();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button> -->
					</security:authorize>
					<security:authorize ifAnyGranted="iqc-incomingInspectionActionsReport-audit">
						<button class='btn' type="button" onclick="auditIqc();"><span><span><b class="btn-icons btn-icons-audit"></b>审核</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc-incomingInspectionActionsReport-recheck">
						<button class='btn' type="button" onclick="reCheck();"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc-incomingInspectionActionsReport-delete">
						<button class='btn' type="button" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>失效</span></span></button>
					</security:authorize>
					<button class='btn' type="button" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="iqc-incomingInspectionActionsReport-unHandling">
						<button class="btn" onclick="beginDefectiveGoodsProcessForm()"><span><span><b class="btn-icons btn-icons-alarm"></b>发起不合格品处理</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc-incomingInspectionActionsReport-export">
<%-- 						<button class='btn' type="button" onclick="iMatrix.export_Data('${iqcctx}/inspection-report/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button> --%>
					<button class='btn' type="button" onclick="exportChart();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>					
				</div>
				<div id="opt-content">
					<form id="contentForm1" name="contentForm1" method="post" action=""></form>
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="dynamicInspection"  url="${iqcctx}/inspection-report/no-list-datas.htm" code="IQC_IIAR" pageName="page"></grid:jqGrid>
					</form>
					<script type="text/javascript">
						$(document).ready(function(){
							if(batchNo){
								$(":input[name=batchNo]","#parameter_Table").each(function(index,obj){
									if(obj.name=='batchNo'){
										$(obj).val(batchNo);
										return false;
									}
								});
								batchNo = false;
								//showSearchDIV($("#searchBtn")[0]);
								fixedSearchSubmit('dynamicInspection', '${iqcctx}/inspection-report/no-list-datas.htm');
							}
						});
					</script>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>

</html>