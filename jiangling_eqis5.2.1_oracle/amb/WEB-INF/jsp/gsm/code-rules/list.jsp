<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/CodeCombobox.js"></script>
	<script type="text/javascript" src="${ctx}/js/CodeMultiCombobox.js"></script>
	<script type="text/javascript">	
		//不良代码列表
		$(function(){
			$("#customCombobox").CodeCombobox({
				value : {id:1,code:'asd',name:'add'}
			});
			$("#customCombobox1").CodeMultiCombobox({
				value : [{id:1,code:'asd',name:'add'}]
			});
		});
		//回调信息
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		} 
		//格式化操作
		function click(cellValue, options, rowObject){	
			return "<div style='text-align:center;'><a href='${gsmctx}/code-sec-rules/list.htm?gsmCodeRulesId="+rowObject.id+"'>添加二级类别代码</a>&nbsp;"+"|"+"&nbsp;"
	              +"<a href='javascript:void(0)' onclick='deleteSubs("+rowObject.id+")'>删除二级类别代码</a></div>";
		}
		//自定义删除
		function delMyRow(rowId) {
			if(editing){
				alert("请先完成编辑！");
				return;
			}
			var ids = jQuery("#dynamicCodeSecRuler").getGridParam('selarrrow');
			if(ids.length==0){
				alert("请选择一条数据");
				return;
			}
			if(ids.length > 1){
				alert("请选中需要删除的记录！");
				return;
			}
			if(confirm("确定要删除所选中的记录？")){
				var ret = $("#dynamicCodeSecRuler").jqGrid('getRowData',ids);
				$.post("${gsmctx}/code-sec-rules/search-subs.htm",{gsmCodeRulesId : ret.id},function(result){
					if(result == "have"){
						alert("还有不良代码不能删除，请先删除其下不良代码！");
						return;
					}else{
						$.post("${gsmctx}/code-rules/delete.htm", {
							deleteIds : ids.join(',')
						}, function(data) {
							//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
							while (ids.length>0) {
								jQuery("#dynamicCodeSecRuler").jqGrid('delRowData', ids[0]);
							}
						});
					}
				});
			}			
		}
		//删除子级
		function deleteSubs(rowId){
			var ret = jQuery("#dynamicCodeSecRuler").jqGrid('getRowData',rowId);
			if(confirm("确定要删除"+ret.measurementType+"下所有的不良代码吗？")){
				$.post("${gsmctx}/code-sec-rules/delete-subs.htm?gsmCodeRulesId="+rowId, null, function(result){
					if(result.error){
						alert(result.message);
					}else{
						$("#message").html(result.message);
						setTimeout(function() {
							$("#message").html("");
						}, 3000);
					}
				},'json');
			};
		}
		//编辑行前
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="gsm_equipment_code-sec-rules_save">
				isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="MyCodeRules";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-code-rules-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gsm_equipment_code-rules_save">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
	 				</security:authorize>
					<security:authorize ifAnyGranted="gsm_equipment_code-rules_delete">
						<button class="btn" onclick="delMyRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>  
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
					<security:authorize ifAnyGranted="gsm_equipment_code-rules_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/code-rules/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<span style="color:red;" id="message"><s:actionmessage theme="simple" cssStyle="color:red;" /></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="dynamicCodeSecRuler" url="${gsmctx}/code-rules/list-datas.htm" code="MEASUREMENT_CODE_RULES" pageName="page" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>