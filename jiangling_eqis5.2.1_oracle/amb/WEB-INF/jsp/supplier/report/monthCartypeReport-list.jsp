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
	//格式化
	function operateFormater(cellValue,options,rowObj){
		if(rowObj.degree=='C'){
			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
		}else if(rowObj.degree=='D'){
			return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
		}else{
			return '';
		}
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="_month_cartype_report";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west" id="west-ui">
		<%
			request.setAttribute("selLevel",0);
		%>
		<%@include file="../evaluate/quarter/left.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post"  action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<button  class='btn' onclick="iMatrix.export_Data('${supplierctx}/report/monthCartypeReport-export-all.htm?maxValue=${maxValue}');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="monthCartypeReportGridId" url="${supplierctx}/report/monthCartypeReport-listDatas.htm?maxValue=${maxValue}" submitForm="defaultForm" code="SUPPLIER_MONTH_CARTYPE_REPORT" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>