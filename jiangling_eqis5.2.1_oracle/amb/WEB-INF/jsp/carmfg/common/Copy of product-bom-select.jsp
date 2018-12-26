<%@page import="com.ambition.carmfg.bom.service.ProductBomManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	Map<String,Integer> structureKeyMap = ProductBomManager.getStructureKeyMap();
	Integer level = structureKeyMap.get("产品型号");
	level = (level == null?1:level);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<style>
		<!--
			#searchUl{
				margin:0px;
				padding:0px;
			}
			#searchUl li{
				float:left;
				width:220px;
				height:28px;
				line-height:28px;
				list-style:none;
			}
			#searchUl li select{
				width:140px;
			}
			#searchUl li input{
				width:140px;
			}
			.field-label{
				float:left;
				width:70px;
				text-align:right;
				padding-right:2px;
			}
			.searchInputClass{
				width:220px;
			}
		-->
	</style>
	<script type="text/javascript">
///////////////页面使用方法////////////////////////////////////
//	//选择物料BOM
// 	function selectBomValue(){
// 		var url = '${mfgctx}/common/product-bom-select.htm';
// 		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:400,
// 			overlayClose:false,
// 			title:"选择物料BOM"
// 		});
// 	}
	
//  //选择之后的方法 data格式{key:'a',value:'a'}
// 	function setBomValue(data){
// 		alert("select is key:" + data.key + ",value:" + data.value);
// 	}
///////////////页面使用方法结束////////////////////////////////////	
// 		function contentResize(){
// 			var searchDivHeight = 0;
// 			if($("#customerSearchDiv").css("display")=='block'){
// 				searchDivHeight = $("#customerSearchDiv").height();
// 			}
// 			$("#bomList").jqGrid("setGridWidth",$("#btnDiv").width());
// 			$("#bomList").jqGrid('setGridHeight',$(document).height() - $("#btnDiv").height() - 45 - searchDivHeight);
// 		}
		$.extend($.jgrid.defaults,{
			loadComplete : function(){
				var p = $("#bomList")[0].p.postData;
				if(p&&p.searchParameters!=undefined){
					window.selParentId = null;
				}
			}
		});
		var secMenu="baseInfo";
		var thirdMenu="bom";
		var topMenu = 'aaa';
		$(document).ready(function(){
			createStructureTree();
// 			createBom();
// 			contentResize(); 
			$("#opt-content").height($(window).height()-55);
			$("#product-structure-tree").height($(window).height()-45);

		});
		//创建树
		function createStructureTree(){
			$("#product-structure-tree").jstree({
				json_data : {
					data : [
						{ 
							"data" : "产品结构", 
							"state" : "closed",
							attr:{
								id:'root',
								level : 0,
								rel:'drive'
							}
						}
					],
					ajax : { 
						"url" : "${mfgctx}/base-info/bom/list-structure.htm",
						data : function(n){
							var id = n.attr("id");
							return {date:(new Date()).getTime(),selParentId:id=='root'?'':id}	
						}
					}
				},
				plugins : ["themes","json_data","ui","crrm","contextmenu",'types'],
				core : { "initially_open" : ["root"] },
				types : {
					valid_children:'drive',
					types:{
						drive:{
							icon:{
								image:'${mfgctx}/images/_drive.png'
							}
						}
					}
				},
				"ui" : {
//	 				"initially_select" : [ "root"]
				}
			}).bind("select_node.jstree",function(e,data){
				var modelSpecificationLevel = <%=level%>;
				var id = data.rslt.obj.attr("id");
				id = id == 'root'?'':id;
				loadProductBomByStructureAndParent(id);
			});
		}
		
		//加载产品BOM 
		function loadProductBomByStructureAndParent(parentId){
			if(window.selParentId == parentId){
				return;
			}
			window.selParentId = parentId || '';
			$("#bomList")[0].p.postData = {selParentId:parentId,"_list_code":"MFG_PRODUCT_BOM"};
			$("#bomList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
		}
		function selectFormatter(value,o,obj){
			return "<a class=\"small-button-bg\" href=\"#\" onclick=\"realSelect("+value+")\" style='float:left;margin-left:10px;' title='选择【"+obj.name+"】'><span class=\"ui-icon ui-icon-circle-check\"></span></a>";
		}
		function operateFormatter(value,o,obj){
			if(value){
				return "<a class=\"small-button-bg\" onclick=\"loadProductBomByStructureAndParent("+value+");\" href=\"#\" style='float:left;margin-left:20px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-newwin\" style='cursor:pointer;'></span></a>";
			}else{
				return '';
			}
		}
		function nameFormatter(value,o,obj){
			if(obj.hasChild==true||obj.hasChild=='true'){
				return "<a class=\"small-button-bg\" href=\"#\" onclick=\"loadProductBomByStructureAndParent("+obj.id+")\" style='float:left;margin-right:2px;' title='转到【"+obj.name+"】'><span class=\"ui-icon ui-icon-folder-collapsed\"></span></a>" + value;
			}else if(value){
				return value;
			}else{
				return '';
			}
		}
		//确定
		function realSelect(id){
			var ids = [];
			if(id){
				ids.push(id);
			}else{
				var id = $("#bomList").jqGrid("getGridParam","selrow");
				if(id){
					ids.push(id);
				}
			}
			if(ids.length == 0){
				alert("请选择物料BOM!");
				return;
			}
			if($.isFunction(window.parent.setFullBomValue)){//获取全部的值
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#bomList").jqGrid('getRowData',ids[i]);
					if(data){
						if(data.name){
							data.name = data.name.replace(/<[^<]*>/g,'');
						}
						objs.push(data);
					}
				}
				if(objs.length>0){
					window.parent.setFullBomValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else if($.isFunction(window.parent.setBomValue)){//获取简洁的值
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#bomList").jqGrid('getRowData',ids[i]);
					if(data){
						objs.push({key:data.code,value:data.name?data.name.replace(/<[^<]*>/g,''):"",model:data.model?data.model.replace(/<[^<]*>/g,''):data.model,remadePrice:data.remadePrice,dumpingPrice:data.dumpingPrice});
					}
				}
				if(objs.length>0){
					window.parent.setBomValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else{
				alert("页面还没有 setBomValue 方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-west">
		<div class="opt-body">
			<div class="opt-btn" style="padding-left:6px;line-height:30px;">
				产品结构
			</div>
			<div style="padding:4px;overflow:auto;" id="product-structure-tree">
			</div>
		</div>
	</div>
	<div class="ui-layout-center">
		<aa:zone name = "main">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="bomList"  url="${mfgctx}/base-info/bom/list-datas.htm?parentId=${parentId}" code="MFG_PRODUCT_BOM_SINGLE_SELECT" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</div>
		</aa:zone>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			var divWidth = $("#opt-content").width() - 10;
			$("#fixedSearchZoon").width(divWidth);
			//var tdWidth = $("#fixedSearchZoon").find(".searchInputClass").closest("td").width();
			$("#searchZoon").width(divWidth);
		});
	</script>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>