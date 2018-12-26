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
	var params={};
	$().ready(function(){
		var paramsstr = '${params}';
		var paramsObj = eval("("+paramsstr+")");
		for(var index in paramsObj){
			params["params."+index]=paramsObj[index];
			$("#"+index).html(paramsObj[index]);
		}
	});
	
    function $addGridOption(jqGridOption){ 		
	   jqGridOption.postData=params;
	   jqGridOption.postData._list_code="SUPPLIER_MONTH_LEVEL_DETAIL";
	}
    
	//导出
	function exportSuppliers(){
		var searchParams = "";
		var paramsstr = '${params}';
		var paramsObj = eval("("+paramsstr+")");
		for(var index in paramsObj){
			searchParams += "params."+index+"="+paramsObj[index]+"&";
		}
		iMatrix.export_Data("${supplierctx}/report/monthCartypeReport-export.htm?"+searchParams.substring(0,searchParams.length-1));
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="manager";
		var thirdMenu="_warning_sign";
	</script>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post"  action=""></form>
			<aa:zone name="main">
				<div class="opt-btn" style="text-align: center;font-size: 20px;font-weight: bolder;">
				<button class="btn" onclick="exportSuppliers();" style="float:left;"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					<span id="year"></span>年<span id="month"></span>月
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="monthCartypeReportGridId" url="${supplierctx}/report/monthCartypeReport-monthlevle-listDatas.htm" submitForm="defaultForm" code="SUPPLIER_MONTH_LEVEL_DETAIL" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>