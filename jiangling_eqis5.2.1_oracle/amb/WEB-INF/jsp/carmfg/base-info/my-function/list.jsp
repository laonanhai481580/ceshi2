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
		function contentResize(){
			var id = $('table.ui-jqgrid-btable').attr('id');
			if(id != undefined){
				var tableHeight = $('.ui-layout-center').height()-80;
				jQuery("#"+id).jqGrid('setGridHeight',tableHeight);
				jQuery("#"+id).jqGrid('setGridWidth',_getTableWidth());
			}
		}
		var postData = {};
		$(document).ready(function(){
			jQuery("#functionList").jqGrid({
				url:'${mfgctx}/base-info/my-function/list-datas.htm',
				postData : postData,
				treedatatype: "json",
				mtype: "POST",
			   	colNames:["id","名称","链接地址"],
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{name:'name',index:'name', width:200},
			   		{name:'address',index:'address', width:300}
			   	],
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    viewrecords: true,
				ExpandColumn : 'name',
				ExpandColClick : true,
				shrinkToFit : true,
				loadComplete : function(){
					if(scroolTop != null){
						$($("#functionList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
						scroolTop = null;
					}
				},
				gridComplete : function(){
					contentResize(); 
				}
			});
			contentResize();
		});
		/**
		* 刷新功能
		*/
		var scroolTop = null;
		function refreshFunction(){
			var data = $("#functionList")[0].p.data;
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
			scroolTop = $($("#functionList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#functionList").clearGridData();
			$("#functionList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#functionList").jqGrid("setGridParam",{postData:{expandIds:''}});
		}
		//创建功能
		function createFunction(){
			openFunction();
		}
		//编辑功能
		function editFunction(){
			var id = $("#functionList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择需要编辑的功能!");
				return;
			}
			openFunction(id);
		}
		//打开功能
		function openFunction(id){
			var parentId = null;
			var url='${mfgctx}/base-info/my-function/input.htm?1=1';
			if(id){
				url += '&id='+id;
			}else{
				parentId = $("#functionList").jqGrid("getGridParam","selrow");
				if(parentId){
					url += "&parentId=" + parentId;
				}
			}
			$.colorbox({href:url,iframe:true, innerWidth:600, innerHeight:290,
				overlayClose:false,
				onClosed:function(){
					refreshFunction();
				},
				title:(id?"编辑":"添加") + "功能"
			});
		}
		//删除功能
		function deleteFunction(){
			var id = $("#functionList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择要删除的功能!");
				return;
			}
			var rowData = $("#functionList").jqGrid("getRowData",id);
			if(!rowData){
				alert("数据不存在!");
				refreshFunction();
				return;
			}
			if(confirm("确定要删除选择的功能吗?")){
				$.post('${mfgctx}/base-info/my-function/delete.htm',{deleteIds:id},function(result){
					if(result.error){
						alert(result.message);
					}else{
						refreshFunction();
					}
				},'json');
			}
		}
		
		//导入
		function imports(){
			var id = $("#functionList").jqGrid("getGridParam","selrow");
			var url = '${mfgctx}/base-info/my-function/import-bom-form.htm';
			if(id){
				url += "?parentId=" + id;
			}
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入功能",
				onClosed:function(){
					refreshFunction();
				}
			});
		}
		//导入
		function exports(){
			if(!selStructureId){
				alert("请先选择产品型号!");
				return;
			}
			var id = $("#functionList").jqGrid("getGridParam","selrow");
			var url = '${mfgctx}/base-info/my-function/exports.htm?structureId=' + selStructureId;
			if(id){
				url += "&parentId=" + id;
			}
			window.location.href = url;
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="linkAddress";
		var treeMenu = 'address';
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
					<security:authorize ifAnyGranted="base-info-my-function-input">
					<button class='btn' onclick="createFunction();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="base-info-my-function-delete">
					<button class='btn' onclick="deleteFunction();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="base-info-my-function-input">
					<button  class='btn' type="button" onclick="editFunction(this);"><span><span><b class="btn-icons btn-icons-edit"></b>编辑</span></span></button>
					</security:authorize>
<!-- 					<button class="sexybutton" onclick="imports();" ><span class="import">导入</span></button>
 -->					</div>
					<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post"  action="">
							<table id="functionList"></table>
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