<%@page import="com.ambition.carmfg.entity.ProductBom"%>
<%@page import="com.ambition.supplier.entity.SupplyProduct"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
///////////////页面使用方法////////////////////////////////////
//	//选择供应商供应的产品
// 	function selectSupplyProducts(){
// 		var url='${supplierctx}/archives/select-supply-products.htm?id=1';
// 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
// 			overlayClose:false,
// 			title:"选择供应商"
// 		});
// 	}
	
//  //选择之后的方法 data格式{key:'a',value:'a'}
// 	function setSupplyProductValue(data){
// 		alert("select is key:" + data.key + ",value:" + data.value);
// 	}
///////////////页面使用方法结束////////////////////////////////////	
		var secMenu="baseInfo";
		var thirdMenu="bom";
		var topMenu = 'aaa';
		var multiselect = ${multiselect};
		$(document).ready(function(){
			$("#fixedSearchZoon").width($(window).width()-32);
		});
		//确定
		function realSelect(){
			var currentNode = "<%=request.getParameter("currentNode")%>";
			var ids = $("#supplyProducts").jqGrid("getGridParam","selarrrow");
			if(ids.length == 0){
				alert("请选择供应的产品!");
				return;
			}
			if(!multiselect&&ids.length > 1){
				alert("只能选择一条信息!");
				return;
			}
			if($.isFunction(window.parent.setSupplyProductValue)){
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#supplyProducts").jqGrid('getRowData',ids[i]);
					if(data){
						/**
						if(currentNode=='inspection-report'){//考察报告
							var canSelectState = ",<%=SupplyProduct.APPLYSTATE_DEFAULT%>,<%=SupplyProduct.APPLYSTATE_INSPECTFAIL%>,<%=SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEFAIL%>,<%=SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSFAIL%>,";
							if(data.applyState&&canSelectState.indexOf("," + data.applyState+",")==-1){
								alert(data.name + data.applyState + ",不能选择为考察对象!");
								return;
							}
						}else if(currentNode=='sample-appraisal'){//样件鉴定
							var canSelectState = ",<%=SupplyProduct.APPLYSTATE_DEFAULT%>,<%=SupplyProduct.APPLYSTATE_INSPECT%>,<%=SupplyProduct.APPLYSTATE_INSPECTPASS%>,<%=SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEFAIL%>,<%=SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSFAIL%>,";
							if(data.importance=='<%=ProductBom.IMPORTANCE_A%>'){
								canSelectState = ",<%=SupplyProduct.APPLYSTATE_INSPECTPASS%>,<%=SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEFAIL%>,<%=SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSFAIL%>,";
							}
							if(data.applyState&&canSelectState.indexOf("," + data.applyState+",")==-1){
								alert(data.name + data.applyState + ",不能选择为样件鉴定对象!");
								return;
							}
						}else if(currentNode=='sublots-appraisal'){//小批鉴定
							var canSelectState = ",<%=SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEPASS%>,<%=SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSFAIL%>,<%=SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSPASS%>,";
							if(data.applyState&&canSelectState.indexOf("," + data.applyState+",")==-1){
								alert(data.name + data.applyState + ",不能选择为小批鉴定对象!");
								return;
							}
						}
						*/
						if(currentNode=='check-plan'){
							objs.push(data);
						}else{
							if(data.errorMessage){
								alert(data.name + data.errorMessage + ",不能选择!");
								return;
							}
							objs.push(data);
						}
					}
				}
				if(objs.length>0){
					window.parent.setSupplyProductValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else{
				alert("页面还没有 setSupplyProductValue 方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' onclick="realSelect();">
					<span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span>
				</button>
				<button class='btn' onclick="cancel();">
					<span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span>
				</button>
			</div>
			<div style="line-height: 4px;">&nbsp;</div>
			<div style="padding-left:8px;">
				<form id="contentForm" name="contentForm" method="post" action=""
				onsubmit="return false;">
					<grid:jqGrid gridId="supplyProducts" url="${supplierctx}/archives/select-supply-products-datas.htm?currentNode=${currentNode}&id=${id}"
						code="SUPPLIER_SUPPLY_PRODUCTS_SELECT" dynamicColumn="${dynamicColumnDefinitions}"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
<script type="text/javascript">
	function contentResize(){
		$("#supplyProducts").jqGrid("setGridHeight",$(window).height()-$("#btnDiv").height()-40);
		$("#supplyProducts").jqGrid("setGridWidth",$(window).width()-18);
	};
</script>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>