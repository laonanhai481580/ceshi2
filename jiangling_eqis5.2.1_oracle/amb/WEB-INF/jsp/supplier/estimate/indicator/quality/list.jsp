<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
		function contentResize(){
			jQuery("#evaluatingIndicatorList").jqGrid('setGridHeight',$(".ui-layout-center").height()-80);
			jQuery("#evaluatingIndicatorList").jqGrid('setGridWidth',_getTableWidth());
		}
		var postData = {};
		$(document).ready(function(){
			jQuery("#evaluatingIndicatorList").jqGrid({
				url:'${supplierctx}/estimate/indicator/quality/list-datas.htm',
				postData : postData,
				treedatatype: "json",
				mtype: "POST",
			   	colNames:["id","评价指标", "备注"],
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{name:'name',index:'name', width:300},
			   		{name:'remark',index:'remark', width:200}	
			   	],
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    viewrecords: true,
				ExpandColumn : 'name',
// 				ExpandColClick : true,
				shrinkToFit : true,
				loadComplete : function(){
					if(scroolTop != null){
						$($("#evaluatingIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
						scroolTop = null;
					}
				},
				gridComplete:function(){}
			});
			contentResize(); 
		});
		/**
		* 刷新评价指标
		*/
		var scroolTop = null;
		function refreshEvaluatingIndicator(){
			var data = $("#evaluatingIndicatorList")[0].p.data;
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
			scroolTop = $($("#evaluatingIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#evaluatingIndicatorList").clearGridData();
			$("#evaluatingIndicatorList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#evaluatingIndicatorList").jqGrid("setGridParam",{postData:{expandIds:''}});
		}
		//创建评价指标
		function createEvaluatingIndicator(){
			openEvaluatingIndicator();
		}
		//编辑评价指标
		function editEvaluatingIndicator(){
			var id = $("#evaluatingIndicatorList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择需要编辑的评价指标!");
				return;
			}
			openEvaluatingIndicator(id);
		}
		//打开评价指标
		function openEvaluatingIndicator(id){
			var parentId = null;
			var url='${supplierctx}/estimate/indicator/quality/input.htm?1=1';
			if(id){
				url += '&id='+id;
			}else{
				parentId = $("#evaluatingIndicatorList").jqGrid("getGridParam","selrow");
				if(parentId){
					url += "&parentId=" + parentId;
				}
			}
			$.colorbox({href:url,iframe:true, innerWidth:400, innerHeight:230,
				overlayClose:false,
				onClosed:function(){
					refreshEvaluatingIndicator();
				},
				title:(id?"编辑":"添加") + "评价指标"
			});
		}
		//删除评价指标
		function deleteEvaluatingIndicator(){
			var id = $("#evaluatingIndicatorList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择要删除的评价指标!");
				return;
			}
			var rowData = $("#evaluatingIndicatorList").jqGrid("getRowData",id);
			if(!rowData){
				alert("数据不存在!");
				refreshEvaluatingIndicator();
				return;
			}
			if(!rowData.isLeaf||rowData.isLeaf=='false'){
				alert("还有子节点不能删除，请先删除子节点!");
				return;
			}
			if(confirm("确定要删除选择的评价指标吗?")){
				$.post('${supplierctx}/estimate/indicator/quality/delete.htm',{deleteIds:id},function(result){
					if(result.error){
						alert(result.message);
					}else{
						refreshEvaluatingIndicator();
					}
				},'json');
			}
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="estimate";
		var thirdMenu="_estimate_quality";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-estimate-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="createEvaluatingIndicator();">
						<span><span><b class="btn-icons btn-icons-save"></b><s:text name='common.save'/></span>
						</span>
					</button>
					<button class='btn' onclick="deleteEvaluatingIndicator();">
						<span><span><b class="btn-icons btn-icons-delete"></b><s:text name='common.delete'/></span>
						</span>
					</button>
					<button class='btn' onclick="editEvaluatingIndicator();">
						<span><span><b class="btn-icons btn-icons-edit"></b><s:text name='common.edit'/></span>
						</span>
					</button>
				</div>
				<div style="display: none;" id="message">
					<font class=onSuccess>
					<nobr>删除成功</nobr></font>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<table id="evaluatingIndicatorList"></table>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>