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
		var postData = {};
		$(document).ready(function(){
			jQuery("#composingList").jqGrid({
				url:'${costctx}/composing-detail/list-datas.htm',
				postData : postData,
				treedatatype: "json",
				mtype: "POST",
			   	colNames:["id",'level',"科目","编码", "核算部门", "配合部门","备注"],
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{name:'level',index:'level', width:1,hidden:true},
			   		{name:'name',index:'name', width:160},
			   		{name:'code',index:'code', width:70},
			   		{name:'checkDepartment',index:'checkDepartment', width:70,hidden:true},		
			   		{name:'cooperateDepartment',index:'cooperateDepartment', width:70,hidden:true},		
			   		{name:'remark',index:'remark',width:300}	
			   	],
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    viewrecords: true,
				ExpandColumn : 'name',
				ExpandColClick : true,
				shrinkToFit : true,
				loadComplete : function(){
					if(scroolTop != null){
						$($("#composingList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
						scroolTop = null;
					}
				},
				gridComplete:function(){}
			});
			jQuery("#composingList").jqGrid("setGridHeight",$("#opt-content").height()-35);
		});
		/**
		* 刷新质量成本
		*/
		var scroolTop = null;
		function refreshComposing(){
			var data = $("#composingList")[0].p.data;
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
			scroolTop = $($("#composingList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#composingList").clearGridData();
			$("#composingList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#composingList").jqGrid("setGridParam",{postData:{expandIds:''}});
		}
		//创建质量成本
		function createComposing(){
			var id = $("#composingList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择二级科目!");
				return;
			}
			openComposing();
		}
		//编辑质量成本
		function editComposing(){
			var id = $("#composingList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择需要编辑的质量成本!");
				return;
			}else{
				var data = $("#composingList").jqGrid("getRowData",id);
				if(!data){
					alert("编辑的科目不存在!");
					refreshComposing();
					return;
				}else{
					if(data.level + 1 == 1){
						alert("不能编辑二级科目!");
						return;
					}
				}
				openComposing(id);
			}
		}
		//打开质量成本
		function openComposing(id){
			var parentId = null;
			var url='${costctx}/composing-detail/input.htm?a=1';
			var level = 0;
			if(id){
				var data = $("#composingList").jqGrid("getRowData",parentId);
				if(!data){
					alert("编辑的科目不存在!");
					refreshComposing();
				}else{
					url += '&id='+id;
					level = data.level;
				}
			}else{
				parentId = $("#composingList").jqGrid("getGridParam","selrow");
				if(parentId){
					var data = $("#composingList").jqGrid("getRowData",parentId);
					if(parseInt(data.level)+1>1){
						alert("只能添加三级科目!");
						return;
					}
					url += "&parentId=" + parentId;
					level = data.level + 1;
				}
			}
			level = parseInt(level) + 1;
			$.colorbox({href:url,iframe:true, innerWidth:460, innerHeight:level < 3?320:350,
				overlayClose:false,
				onClosed:function(){
					refreshComposing();
				},
				title:(id?"编辑":"添加") + "质量成本"
			});
		}
		//删除质量成本
		function deleteComposing(){
			var id = $("#composingList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择要删除的质量成本!");
				return;
			}
			var rowData = $("#composingList").jqGrid("getRowData",id);
			if(!rowData){
				alert("数据不存在!");
				refreshComposing();
				return;
			}
// 			if(!rowData.isLeaf||rowData.isLeaf=='false'){
// 				alert("还有子节点不能删除，请先删除子节点!");
// 				return;
// 			}
			if(!rowData.parent||rowData.parent=='&nbsp;'){
				alert("不能删除二级科目!");
				return;
			}
			if(confirm("确定要删除选择的质量成本吗?")){
				$.post('${costctx}/composing-detail/delete.htm',{deleteIds:id},function(result){
					if(result.error){
						alert(result.message);
					}else{
						refreshComposing();
					}
				},'json');
			}
		}
		//选择质量成本
	 	function selectComposing(){
	 		var url = '${costctx}/common/composing-select.htm?multiselect=true';
	 		$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择质量成本"
	 		});
	 	}
		
	//  //选择之后的方法 data格式{key:'a',value:'a'}
	 	function setFullComposingValue(datas){
	 		alert("select is key:" + datas);
	 	}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="composing_detail";
		var thirdMenu="cost_composing_list";
		var treeMenu = '_cost_composing_list_list';
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/cost-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/cost-composing-detail-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
					<security:authorize ifAnyGranted="cost_detail_input_m">
						<button class="btn" onclick="createComposing();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
						</security:authorize>
						<security:authorize ifAnyGranted="cost_detail_input_m">
						<button class="btn" onclick="editComposing();"><span><span><b class="btn-icons btn-icons-edit"></b>编辑</span></span></button>
						</security:authorize>
						<security:authorize ifAnyGranted="cost_delete_data">
						<button class="btn" onclick="deleteComposing();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
						</security:authorize>
					</div>
					<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post"  action="">
							<table id="composingList"></table>
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
