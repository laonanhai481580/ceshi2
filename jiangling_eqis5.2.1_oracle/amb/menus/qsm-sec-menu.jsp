<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- <div id="secNav"> -->
<a class="scroll-left-btn" onclick="_scrollLeft();">&lt;&lt;</a>
<div class="fix-menu">
<ul class="scroll-menu">	
	<security:authorize ifAnyGranted="QSM_CUSTOMER_AUDIT_LIST,QSM_CUSTOMER_AUDIT_ISSUES_LIST">
		<li  id="customer_audit" class="last"><span><span><a href="<grid:authorize code="QSM_CUSTOMER_AUDIT_LIST,QSM_CUSTOMER_ISSUES_LIST" systemCode="qsm"></grid:authorize>">客户审核</a></span></span></li>
	</security:authorize>	
	<security:authorize ifAnyGranted="QSM_YEAR_AUDIT_LIST">
		<li  id="inner_audit" class="last"><span><span><a href="<grid:authorize code="QSM_YEAR_AUDIT_LIST" systemCode="qsm"></grid:authorize>">内审</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_SYSTEM_CERTIFICATE_LIST">
		<li  id="system_certificate" class="last"><span><span><a href="<grid:authorize code="QSM_SYSTEM_CERTIFICATE_LIST" systemCode="qsm"></grid:authorize>">体系认证</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_SYSTEM_MAINTENANCE_LIST">
		<li  id="system_maintenance" class="last"><span><span><a href="<grid:authorize code="QSM_SYSTEM_MAINTENANCE_LIST" systemCode="qsm"></grid:authorize>">体系维护</a></span></span></li>
	</security:authorize>	
	<security:authorize ifAnyGranted="QSM_DEFECTION_CLAUSE_LIST">
		<li  id="defection_clause" class="last"><span><span><a href="<grid:authorize code="QSM_DEFECTION_CLAUSE_LIST" systemCode="qsm"></grid:authorize>">基础维护</a></span></span></li>
	</security:authorize>	
	<security:authorize ifAnyGranted="QSM_MANAGE_REVIEW_LIST,QSM_MANAGE_REVIEW_INPUT">
		<li  id="manage_review" class="last"><span><span><a href="<grid:authorize code="QSM_MANAGE_REVIEW_LIST,QSM_MANAGE_REVIEW_INPUT" systemCode="qsm"></grid:authorize>">管理评审</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_ANALYSIS,QSM_LOSS_ITEM_LIST,QSM_AUDIT_PASS_LIST,QSM_AUDIT_BING_LIST,QSM_AUDIT_CLOSE_LIST">
		<li  id="analysis" class="last"><span><span><a href="<grid:authorize code="QSM_ANALYSIS,QSM_LOSS_ITEM_LIST,QSM_AUDIT_PASS_LIST,QSM_AUDIT_BING_LIST,QSM_AUDIT_CLOSE_LIST" systemCode="qsm"></grid:authorize>">统计分析</a></span></span></li>
	</security:authorize>	
</ul>
</div>
<a class="scroll-right-btn" onclick="_scrollRight();">&gt;&gt;</a>
<div class="hid-header" onclick="headerChange(this);" title="隐藏"></div>
<script>
	var topMenu;
	$("#" + secMenu).addClass('sec-selected');
</script>
