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
		//部件列表
		$(function(){
			setTimeout(function(){},100);
		});
		function $successfunc(response){
			var result = eval("(" + response.responseText	+ ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function click(cellvalue, options, rowObject){	
			return "<div style='text-align:center;'><a href='${mfgctx}/base-info/position-code/list.htm?componentId="+rowObject.id+"'>添加部位代码</a>&nbsp;"+"|"+"&nbsp;"
	              +"<a href='javascript:void(0)' onClick='deleteSubs("+rowObject.id+")'>删除部位代码</a></div>";
		}
		function delMyRow(rowId) {
			if(editing){
				alert("请先完成编辑！");
				return;
			}
			var ids = jQuery("#componentList").getGridParam('selarrrow');
			if(ids.length < 1){
				alert("请选中需要删除的记录！");
				return;
			}
			if(ids.length > 1){
				alert("记录可能含有部位代码，请逐条删除！");
				return;
			}			
			if(confirm("确定要删除所选中的记录？")){
				var ret = jQuery("#componentList").jqGrid('getRowData',ids);
				$.post("${mfgctx}/base-info/position-code/search-subs.htm", {componentId : ret.id}, function(result){
					if(result == "have"){
						alert("还有部位代码不能删除，请先删除其下部位代码！");
						return;
					}else{
						$.post("${mfgctx}/base-info/position-code/component-delete.htm", {
		 					deleteIds : ids.join(',')
		 				}, function(data) {
		 					//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
		 					while (ids.length>0) {
		 						jQuery("#componentList").jqGrid('delRowData', ids[0]);
		 					}
		 				});
					}
				});
			}
			
		}
		//删除某部件下的部位代码
		function deleteSubs(rowId){
			var ret = jQuery("#componentList").jqGrid('getRowData',rowId); 
			if(confirm("确定要删除"+ret.componentName+"下所有的部位代码吗？")){
				$.post("${mfgctx}/base-info/position-code/delete-subs.htm", {componentId : ret.id}, function(){});
			};
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="base-info-position-code-component-save">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		
		//导入
		function imports(){
			var url = '${mfgctx}/base-info/position-code/imports.htm';
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入",
				onClosed:function(){
					$("#componentList").trigger("reloadGrid");
				}
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="defectionCode";
		var treeMenu="position";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="base-info-position-code-component-save">
					<button class='btn' onclick="addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="base-info-position-code-component-delete">
					<button class='btn' onclick="delMyRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' onclick="showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="base-info-position-code-component-exportCompoent">
					<button class='btn' onclick="export_Data('${mfgctx}/base-info/position-code/exportCompoent.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>	
					</security:authorize>
					<security:authorize ifAnyGranted="base-info-position-code-imports">
					<button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>	
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >						
						<grid:jqGrid gridId="componentList" url="${mfgctx}/base-info/position-code/component-list-datas.htm" code="MFG_COMPONENT"></grid:jqGrid>
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