<%@page import="com.ambition.carmfg.bom.service.ProductBomManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
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

<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="bomList"  url="${mfgctx}/base-info/bom-cost/list-price-datas.htm?parentId=${parentId}&businessUnitName=${businessUnitName}" code="MFG_MATERIAL_COST_PRICE" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
