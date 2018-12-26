<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="supplier-audit-year-list">
	<div id="auditYearList" class="west-notree" url="${supplierctx }/audit/year-view/list.htm"
		onclick="changeMenu(this);">
		<span>供应商年度稽核计划</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-audit-year-history-list">
	<div id="audtiHistoryList" class="west-notree" url="${supplierctx }/audit/year/list.htm"
		onclick="changeMenu(this);">
		<span>供应商历史稽核计划台帐</span>
	</div>
</security:authorize>
<%-- <security:authorize ifAnyGranted="supplier-audit-month-list">
<div id="auditMonthList" class="west-notree" url="${supplierctx }/audit/month/list.htm"
	onclick="changeMenu(this);">
	<span>供应商月度稽核计划</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-audit-improve-list">
<div id="auditImproveList" class="west-notree" url="${supplierctx }/audit/improve/list.htm"
	onclick="changeMenu(this);">
	<span>问题改善报告台账</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-audit-improve-input">
<div id="auditImproveInput" class="west-notree" url="${supplierctx }/audit/improve/input.htm"
	onclick="changeMenu(this);">
	<span>问题改善报告</span>
</div>
</security:authorize> --%>
<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>