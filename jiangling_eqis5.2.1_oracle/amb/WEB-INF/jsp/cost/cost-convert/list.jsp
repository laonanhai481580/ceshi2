<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.cost.entity.CostRecord"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
    <script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js"></script>
	<script type="text/javascript">
		$(function(){
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
		var params = {};
		function $oneditfunc(rowid){
			params = {};
			enterKeyToNext("list",rowid,function(){
			});
			$("#" + rowid + "_levelTwoCode").change(function(){
				var selHtml = $(this).find("option[selected]").html();
				params['levelTwoName'] = selHtml;
			});
		}
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			return data;
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="cost_cost_convert_save">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="composing_detail";
		var thirdMenu="cost_convert_list";
		var treeMenu = 'cost_convert_list_list';
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
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="middle" style="padding: 0px;width:50%; margin: 0px;" id="btnTd">
							<security:authorize ifAnyGranted="cost_cost_convert_save">
							<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
							<button class='btn' onclick="customSave('list');" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
							</security:authorize>
							<security:authorize ifAnyGranted="cost_cost_convert_delete">
							<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
							</security:authorize>
							<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
							<security:authorize ifAnyGranted="cost_cost_convert_export">
							<button class="btn" onclick="iMatrix.export_Data('${costctx}/cost-convert/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							</security:authorize>
						</td>
					</tr>
				</table>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="list" url="${costctx}/cost-convert/list-datas.htm" code="COST_COST_CONVERT"></grid:jqGrid>
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