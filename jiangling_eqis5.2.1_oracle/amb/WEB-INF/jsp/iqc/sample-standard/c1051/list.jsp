<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page contentType="text/html;charset=UTF-8" import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String baseType = SampleCodeLetter.MIL1051_TYPE;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		//样本量字码台帐			
		$(function(){
			setTimeout(function(){},100);
		});
		function $successfunc(response){
			try {
				var result = eval("(" + response.responseText	+ ")");
				if(result.error){
					alert(result.message);
					return false;
				}else{
					return true;
				}
			} catch (e) {
				alert(response.responseText);
				return false;
			}
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			isRight =  true;
			return isRight;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="standard";
		var thirdMenu="_code_letter";
		var treeMenu="c1051";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-sample-standard-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${iqcctx}/sample-standard/c1051/list-datas.htm" code="IQC_SAMPLE_C1051_CODE_LETTER" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#list").jqGrid('setGroupHeaders', {
				useColSpanStyle: true, 
				groupHeaders:${groupHeaders}
			});
		});
	</script>
</body>
</html>