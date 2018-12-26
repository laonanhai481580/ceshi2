<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript">
	var params = {},hisParams = {};
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
	var selRowId = null;
	function $oneditfunc(rowId){
		selRowId = rowId;
		var rowData = $("#checkPlans").jqGrid("getRowData",rowId);
		rowData = rowData?rowData:{};
		hisParams = $.extend(true,{},rowData);
		params = {};
		if(rowId == 0){
			$.post("${supplierctx}/supervision/check-plan/generate-check-plan-code.htm",{},function(result){
				$("#checkPlans").jqGrid("setRowData",rowId,{planCode:result});
			});
		}
	}
	//重写取消方法
	function $afterrestorefunc(rowId){
		if(rowId > 0){
			for(var pro in params){
				params[pro] = hisParams[pro];
			}	
			$("#checkPlans").jqGrid("setRowData",rowId,params);
		}
	}
	//重写序列化数据
	function $processRowData(data){
		$.extend(data,params);
		return data;
	}
	//选择供应商供应的产品
 	function checkBomNameClick(obj){
		var rowId = selRowId;
		var rowData = $("#checkPlans").jqGrid("getRowData",rowId);
		var supplierId = rowData.supplierId;
		if(!supplierId){
			alert("请先选择供应商!");
			return;
		}
 		var url='${supplierctx}/archives/select-supply-products.htm?multiselect=true&id=' + supplierId;
 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
 			overlayClose:false,
 			title:"选择供应的产品"
 		});
 	}
	
 	//选择之后的方法 data格式{key:'a',value:'a'}
 	function setSupplyProductValue(objs){
 		var names = [],ids = [],codes=[];
 		for(var i=0;i<objs.length;i++){
 			names.push(objs[i].name);
 			codes.push(objs[i].code);
 			ids.push(objs[i].id);
 		}
 		$("#"+selRowId+"_checkBomName").val(names.join(","));
 		params.checkBomCode = codes.join(",");
 		params.checkBomName = names.join(",");
		$("#checkPlans").jqGrid("setRowData",rowId,{
			checkBomCode : params.checkBomCode,
			checkBomName : params.checkBomName
		});
 	}
	
	/////////////////供应商选择//////////////////////////////////
	var selObjId = '';
	function supplierNameClick(obj){
		selObjId = obj.currentInputId;
		selectSupplier();
	}
	function selectSupplier(){
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
				params.supplyProducts = obj.supplyProductStr;
				params.supplierImportance = obj.importance;
				$("#" + selObjId).val(obj.name);
				$("#" + rowId + "_checkBomName").val("");
				$("#checkPlans").jqGrid("setRowData",rowId,{
					supplierId : params.supplierId,
					supplyProducts : params.supplyProducts,
					supplierImportance : params.supplierImportance
				});
			}
			$("#" + selObjId).focus();
		}
	}
	/////////////////供应商选择结束//////////////////////////////////
	
	////////////////选择审核组长////////////////////////////////////
	function auditGroupLeaderClick(obj){
			var currentInputId = obj.currentInputId;
			var acsSystemUrl = "${ctx}";
			popTree({ 
				title :"选择审核组长",
				innerWidth:'400',
				treeType:'MAN_DEPARTMENT_GROUP_TREE',
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:obj.currentInputId,
				showInputId:obj.currentInputId,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){
					var obj = document.getElementById(currentInputId);
					if(obj&&obj.focus){
						obj.focus();
					}
				}
			});
	}
	////////////////选择审核组长结束////////////////////////////////////
	
	//报告编号格式化///////////////////////////////////////////////////
	function checkReportCodeFormatter(value,options,rowData){
		if(rowData.id>0){
			if(value&&value!='&nbsp;'){
				return "<div style='width:100%;text-align:center;'><a href='javascript:editCheckReport("+value+")' title='修改监察报告'>" + rowData["$checkReport.code"] + "</a></div>";
			}else{
				return "<div style='text-align:center;'><a title='填写监察报告'  class='small-button-bg' onclick='editCheckReport(\"\","+rowData.id+")'><span class='ui-icon ui-icon-note' style='cursor:pointer;'></span></a></div>";
			}
		}else{
			return '';
		}
	}
	
	function editCheckReport(id,checkPlanId){
		if(!id&&!checkPlanId){
			return;
		}
		if(id){
			alert("编辑报告");
		}else{
			alert("添加报告:" + checkPlanId)
		}
	}
	function showInfo(id){
		window.location="${supplierctx}/supervision/check-report/input.htm?checkPlanId="+id;
	}
	//报告编号格式化结束///////////////////////////////////////////////////
	function exportPlans(){
		$("#contentForm").attr("action","${supplierctx}/supervision/check-plan/exports.htm");
		$("#contentForm").submit();
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="supervision";
		var thirdMenu="_plan_all";
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
				<security:authorize ifAnyGranted="supervision-save">
				<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="supervision-delete">
				<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button> 
				</security:authorize>				
				<security:authorize ifAnyGranted="supervision-list-datas">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);contentResize();"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="supervision-export">
				<button class="btn" onclick="exportPlans();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>				
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="checkPlans" url="${supplierctx}/supervision/check-plan/list-datas.htm" code="SUPPLIER_CHECK_PLAN" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					<script type="text/javascript">
						$(function(){
							var colModels = $("#checkPlans").jqGrid("getGridParam","colModel");
							var start = null,count=0;
							$.each(colModels,function(i,o){
								if(o.name.indexOf("checkReport") == 0 && !o.hidden){
									if(start == null){
										start = o.name;
									}
									count++;
								}else if(o.name.indexOf("checkReport") > 0 && start != null){
									return false;
								}
							});
							if(start != null&&count>1){
								$("#checkPlans").jqGrid('setGroupHeaders', {
									  useColSpanStyle: true, 
									  groupHeaders:[{startColumnName: start, numberOfColumns: 3, titleText: '执行情况'}]
								});
							}
							contentResize();
						});
					</script>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>