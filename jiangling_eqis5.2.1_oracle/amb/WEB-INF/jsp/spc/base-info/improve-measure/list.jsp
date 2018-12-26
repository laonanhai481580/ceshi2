<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
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
	
	function exportMeasures(){
		$("#contentForm").attr("action","${spcctx}/base-info/improve-measure/exports.htm");
		$("#contentForm").submit();
	}
	
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="SPC_BASE-INFO_IMPROVE-MEASURE_SAVE">
		  isRight =  true;
		</security:authorize>
		return isRight;
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base_info";
		var thirdMenu="_measuresList";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-base-info-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="spc_base-info_improve-measure_save">
						<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="spc_base-info_improve-measure_delete">
						<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="spc_base-info_improve-measure_exports">
						<!-- <button class='btn' onclick="exportMeasures();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button> -->
						<button class='btn' onclick="iMatrix.export_Data('${spcctx}/base-info/improve-measure/exports.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
				</div>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="measures" url="${spcctx}/base-info/improve-measure/list-datas.htm" code="SPC_IMPROVEMENT_MEASURE" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>