<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><s:text name='main.title'/></title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	var isUsingComonLayout=false;
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	function $processRowData(data){
		data.indicatorId = ${indicatorId};
		return data;
	}
	$(function(){
		setTimeout(function(){
			$("#gradeRules").jqGrid('setGridHeight',$(window).height()-65);
			$("#gradeRules").jqGrid('setGridWidth',$(window).width());				
		},300);
	});
</script>
</head>

<body>
	<div class="opt-body">
		<div class="opt-btn" style="line-height:30px;">
			<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
			<security:authorize ifAnyGranted="supplier-delete-rule">
			<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
			</security:authorize>
			<span id="message" style="margin-left:4px;color:red;"></span> 
			<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
		</div>
		<form id="contentForm" name="contentForm" method="post" action="">
			<grid:jqGrid gridId="gradeRules"
				url="${supplierctx}/estimate/indicator/quarter/list-rule-datas.htm?indicatorId=${indicatorId}" code="SUPPLIER_EVALUATING_GRADE_RULE"></grid:jqGrid>
		</form>
	</div>
</body>
</html>