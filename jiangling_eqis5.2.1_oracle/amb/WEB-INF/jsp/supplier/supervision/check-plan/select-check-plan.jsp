<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
///////////////页面使用方法////////////////////////////////////
//	//选择供应商
// 	function selectSupplier(){
// 		var url='${supplierctx}/archives/select-supplier.htm';
// 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
// 			overlayClose:false,
// 			title:"选择供应商"
// 		});
// 	}
	
//  //选择之后的方法 data格式{key:'a',value:'a'}
// 	function setSupplierValue(data){
// 		alert("select is key:" + data.key + ",value:" + data.value);
// 	}
///////////////页面使用方法结束////////////////////////////////////	
		var secMenu="baseInfo";
		var thirdMenu="bom";
		var topMenu = 'aaa';
		$(document).ready(function(){
			$("#fixedSearchZoon").width($(window).width()-32);
		});
		//确定
		function realSelect(){
			var ids = $("#checkPlans").jqGrid("getGridParam","selarrrow");
			if(ids.length == 0){
				alert("请稽查计划!");
				return;
			}
			if(ids.length > 1){
				alert("只能选择一条稽查计划!");
				return;
			}
			if($.isFunction(window.parent.setSupplierValue)){
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#checkPlans").jqGrid('getRowData',ids[i]);
					if(data){
						objs.push(data);
					}
				}
				if(objs.length>0){
					window.parent.setCheckPlanValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else{
				alert("页面还没有 setCheckPlanValue 方法!");
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
				<button class='btn' onclick="javascript:iMatrix.showSearchDIV(this);">
					<span><span><b class="btn-icons btn-icons-search"></b>显示查询</span></span>
				</button>
			</div>
			<div style="line-height: 4px;">&nbsp;</div>
			<div style="padding-left:8px;padding-top: 15px;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="checkPlans"
						url="${supplierctx}/supervision/check-plan/list-datas.htm" code="SUPPLIER_CHECK_PLAN" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>