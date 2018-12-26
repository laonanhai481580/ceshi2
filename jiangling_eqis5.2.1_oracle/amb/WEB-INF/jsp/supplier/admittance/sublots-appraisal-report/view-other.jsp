<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.supplier.entity.AppraisalReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	ActionContext.getContext().put("id",request.getParameter("id"));
	ActionContext.getContext().put("supplierId",request.getParameter("supplierId"));
	ActionContext.getContext().put("bomCode",request.getParameter("bomCode"));
%>
<div class="opt-content" style="padding:10px;">
	<grid:jqGrid gridId="appraisalReportList" url="${supplierctx}/admittance/sublots-appraisal-report/view-other-datas.htm?id=${id}&supplierId=${supplierId}&bomCode=${bomCode}" code="SUBLOTS_APPRAISAL_REPORT"></grid:jqGrid>
</div>
<script type="text/javascript">
	$("#appraisalReportList").jqGrid("setGridWidth",$(window).width()-25);
	$("#appraisalReportList").jqGrid("setGridHeight",$(window).height()-112);
	$("#appraisalReportList").jqGrid("setGridParam",{
		ondblClickRow : function(rowId){
			viewDetailInfo(rowId);
		}
	});
</script>