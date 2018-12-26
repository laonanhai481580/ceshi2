<%@page import="com.norteksoft.product.web.struts2.Struts2Utils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%String _source = Struts2Utils.getParameter("_source");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	//格式化表单编号(业绩评价)
	function operateFormater(cellValue,options,rowObj){
		var operations = "<div style='text-align:center;' title=\"请选择评价!\"><a class=\"small-button-bg\" onclick=\"editInfo("+cellValue+");\" href=\"#\"><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
		return operations;
	}
	function editInfo(id){
		$(document).mask();
		window.location = '${supplierctx}/evaluate/quarter/add.htm?id=' + id;
	}
	//格式化表单编号(NCR)
	function formNoFormateToinput(cellValue, option, rowObject) {
		return "<a title=\"查看详情\"  href='${supplierctx}/improve/ncr/input.htm?id="
				+ rowObject.id + "'>" + cellValue + "</a>";
	}
	//格式化表单编号(8D)
	function formNoFormate(cellValue,option,rowObject){
		return "<a title=\"查看详情\"  href='${supplierctx}/improve/8d/view-info.htm?id="+rowObject.id+"'>"+cellValue+"</a>";
	}
	//重置大小
	function contentResize(){
		$("#dynamicInspection").jqGrid("setGridHeight",$(window).height()-75);
	}
	//获取扩展参数
	function $getExtraParams(){
		return {searchStrs:'${params}'};
	}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
				${title}
			</div>
			<div id="opt-content">
				<%if(_source.contains("NCR") || _source.contains("投诉") || _source.contains("进货") || _source.contains("制程") || _source.contains("客户")){%>
					<grid:jqGrid gridId="dynamicInspection"  url="${supplierctx}/evaluate/quarter/evaluate-detail-datas.htm" code="SUPPLIER_NCREPORT" pageName="nCReportPage"></grid:jqGrid>
				<%}else if(_source.contains("8D")){%>
					<grid:jqGrid gridId="dynamicInspection"  url="${supplierctx}/evaluate/quarter/evaluate-detail-datas.htm" code="SUPPLIER_IMPROVE_REPORT" pageName="improve8dReportPage"></grid:jqGrid>
				<%}else{%>
					<grid:jqGrid gridId="dynamicInspection"  url="${supplierctx}/evaluate/quarter/evaluate-detail-datas.htm" code="SUPPLIER_EVALUATE" pageName="page"></grid:jqGrid>
				<%}%>
			</div>
		</div>
	</div>
</body>
</html>