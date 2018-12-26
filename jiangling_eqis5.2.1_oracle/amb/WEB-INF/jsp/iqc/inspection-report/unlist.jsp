<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>进货检验报告台帐</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	function click(cellvalue, options, rowObject){	
		return "<a href='${iqcctx}/inspection-report/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	function formatRate(cellvalue, options, rowObject){
		if(cellvalue){
			var rate = cellvalue*100;
			var operations = "<div style='text-align:left;'>"+rate+".00%</div>";
			return operations;
		}else{
			return '';
		}
	}
	
	function isLaunchedFormat(cellvalue, options, rowObject){
		return cellvalue;
	}
	
	function formateAttachmentFiles(cellValue){
		return cellValue;
	}
	function defectiveFormFormatter(cellvalue, options, rowObject){
		if(cellvalue&&cellvalue!='&nbsp;'){
			return "<a href='#' onclick='callList(\""+cellvalue+"\")'>"+cellvalue+"</a>";
		}else{
			return "";
		}
	}
	function callList(defectiveFormFormNo){
		$.colorbox({href:'${mfgctx}/defective-goods/ledger/view-info.htm?formNo='+defectiveFormFormNo,iframe:true,
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"不合格品处理单详情"
		});
	}
	function addImprove(){
 		var id = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
 		if(id.length>1){
 			alert("只能选择一项！");
 			return ;
 		}else if(id.length==0){
 			alert("请选择一项！");
 			return ;
 		}
		window.open('${supplierctx}/improve/input.htm?formId='+id);
	}		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionReport";
		var thirdMenu="myUnInspectionReport";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>					
					<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_UNLIST_EXPORT">
					<button class='btn' onclick="iMatrix.export_Data('${iqcctx}/inspection-report/unexport.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="supplier-improve-input">
						<button class='btn' type="button" onclick="addImprove();"><span><span><b class="btn-icons btn-icons-add"></b>发起改进</span></span></button>	
					</security:authorize>							
				</div>			
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicInspection" url="${iqcctx}/inspection-report/unlist-datas.htm" code="IQC_UNIIR" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					</form>
					<script type="text/javascript">
					$(document).ready(function(){
						$("#dynamicInspection").jqGrid('setGroupHeaders', {
							useColSpanStyle: true, 
							groupHeaders:[{startColumnName:'params.bug1', numberOfColumns:9, titleText: '不良细项'}]
						});	
					});
					</script>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>