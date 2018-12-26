<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
function contentResize(){
	var id = $('table.ui-jqgrid-btable').attr('id');
	if(id != undefined){
		jQuery("#"+id).jqGrid('setGridHeight',$(".ui-layout-center").height()-80);
		jQuery("#"+id).jqGrid('setGridWidth',_getTableWidth());
	}
}
</script>
<script type="text/javascript">
		var postData = {};
		$(document).ready(function(){
			jQuery("#estimateModelList").jqGrid({
				url:'${supplierctx}/estimate/model/quarter/list-datas.htm',
				postData : postData,
				treedatatype: "json",
				mtype: "POST",
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{name:'isLeaf',index:'isLeaf', width:1,hidden:true},
			   		{label:"评价模型",name:'name',index:'name', width:300},
			   		{label:"评价周期",name:'cycle',index:'cycle', width:40},
			   		{label:"开始月份",name:'startMonth',index:'startMonth', width:40},
			   		{label:"总分",name:'totalPoints',index:'totalPoints', width:40},
			   		{label:"备注",name:'remark',index:'remark', width:200},
			   		{label:"评价指标",name:'act',index:'act', width:40}	
			   	],
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    viewrecords: true,
				ExpandColumn : 'name',
// 				ExpandColClick : true,
				shrinkToFit : true,
				gridComplete : function(){
					var ids = jQuery("#estimateModelList").jqGrid('getDataIDs');
					for(var i=0;i < ids.length;i++){ 
						var cl = ids[i];
						var rowData = $("#estimateModelList").jqGrid("getRowData",cl);
						if(rowData&&rowData.isLeaf=='true'){
							var canEdit = false;
							<security:authorize ifAllGranted="estimate-model-edit,estimate-model-save-indicator">
							canEdit = true;
							</security:authorize>
							var operations = "";
							if(canEdit){
								operations = "<div style='text-align:center;cursor:pointer;' title=\"编辑指标\"><a class=\"small-button-bg\" onclick=\"editModelIndicator("+cl+");\" href=\"#\"><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
							}
							jQuery("#estimateModelList").jqGrid('setRowData',ids[i],{act:operations});
						}
					}
				},
				loadComplete : function(){
					if(scroolTop != null){
						$($("#estimateModelList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
						scroolTop = null;
					}
				}
			});
			contentResize(); 
		});
		/**
		* 刷新评价模型
		*/
		var scroolTop = null;
		function refreshEstimateModel(){
			var data = $("#estimateModelList")[0].p.data;
			var expandIds = [];
			for(var i=0;i<data.length;i++){
				var d = data[i];
				if(d.expanded){
					expandIds.push(d.id);
				}
			}
			var postData = {
				nodeid:'',
				expandIds : expandIds.join(",")
			};
			scroolTop = $($("#estimateModelList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#estimateModelList").clearGridData();
			$("#estimateModelList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#estimateModelList").jqGrid("setGridParam",{postData:{expandIds:''}});
		}
		//创建评价模型
		function createEstimateModel(){
			openEstimateModel();
		}
		//编辑评价模型
		function editEstimateModel(){
			var id = $("#estimateModelList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择需要编辑的评价模型!");
				return;
			}
			openEstimateModel(id);
		}
		//打开评价模型
		function openEstimateModel(id){
			var parentId = null;
			var url='${supplierctx}/estimate/model/quarter/input.htm?1=1';
			var isLeaf = true;
			if(id){
				url += '&id='+id;
				var rowData = $("#estimateModelList").jqGrid("getRowData",id);
				isLeaf = rowData.isLeaf;
				isLeaf = isLeaf == 'true'?true:false;
			}else{
				parentId = $("#estimateModelList").jqGrid("getGridParam","selrow");
				if(parentId){
					url += "&parentId=" + parentId;
				}
			}
			$.colorbox({href:url,iframe:true, innerWidth:400, innerHeight:isLeaf?280:230,
				overlayClose:false,
				onClosed:function(){
					refreshEstimateModel();
				},
				title:(id?"编辑":"添加") + "评价模型"
			});
		}
		//删除评价模型
		function deleteEstimateModel(){
			var id = $("#estimateModelList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择要删除的评价模型!");
				return;
			}
			var rowData = $("#estimateModelList").jqGrid("getRowData",id);
			if(!rowData){
				alert("数据不存在!");
				refreshEstimateModel();
				return;
			}
			if(!rowData.isLeaf||rowData.isLeaf=='false'){
				alert("还有子节点不能删除，请先删除子节点!");
				return;
			}
			if(confirm("确定要删除选择的评价模型吗?")){
				$.post('${supplierctx}/estimate/model/quarter/delete.htm',{deleteIds:id},function(result){
					if(result.error){
						alert(result.message);
					}else{
						refreshEstimateModel();
					}
				},'json');
			}
		}
		//编辑模型指标
		function editModelIndicator(id){
			var url='${supplierctx}/estimate/model/quarter/edit-indicator.htm?estimateModelId=' + id;
			$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
				overlayClose:false,
				onClosed:function(){
					refreshEstimateModel();
				},
				title:"编辑评价指标"
			});
		}
		//复制模型
		function copyModel(){
			var id = $("#estimateModelList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择要复制的评价模型!");
				return;
			}
			var html = '<div class="opt-body" id="copyModelBody"><div class="opt-btn" style="width:300px;line-height:30px;"><button class="btn" onclick="saveCopyModel();"><span><span>确定</span></span></button><span id="copyMessage" style="color:red;padding-left:4px;"></span></div>';
			html += '<form id="copyModelForm"><div style="padding:6px;padding-top:12px;">新模型名称:<input type="text" name="params.name" class="{required:true}"/><input type="hidden" name="params.sourceId" value="'+id+'"/></div></form>';
			html += '</div>';
			$.colorbox({
				html:html,
				height:200
			});
			$("#copyModelForm").validate();
		}
		//保存复制的模型
		function saveCopyModel(){
			if($("#copyModelForm").valid()){
				var params = {};
				$(":input","#copyModelForm").each(function(index,obj){
					if(obj.name){
						params[obj.name] = obj.value;
					}
				});
				$("#copyMessage").html("正在保存,请稍候......");
				$("#copyModelBody").attr("disabled","disabled");
				$.post("${supplierctx}/estimate/model/quarter/save-copy-model.htm",params,function(result){
					if(result.error){
						$("#copyModelBody").attr("disabled","");
						$("#copyMessage").html(result.message);
						setTimeout(function(){
							$("#copyMessage").html("");
						},3000);
					}else{
						$.colorbox.close();
						refreshEstimateModel();
					}
				},'json');
			}
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="_estimate_model";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-base-info-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" style="line-height:30px;">
					<security:authorize ifAllGranted="estimate-model-input,estimate-model-save">
					<button class="btn" onclick="createEstimateModel();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="estimate-model-delete">
					<button class="btn" onclick="deleteEstimateModel();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAllGranted="estimate-model-input,estimate-model-save">
					<button class="btn" onclick="editEstimateModel();"><span><span><b class="btn-icons btn-icons-edit"></b>编辑</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="estimate-model-copy"> 
					<button class="btn" onclick="copyModel();"><span><span><b class="btn-icons btn-icons-copy"></b>复制模型</span></span></button>
					</security:authorize>
					<span id="message" style="color:red;"></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<table id="estimateModelList"></table>
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