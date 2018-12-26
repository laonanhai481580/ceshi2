<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="QSM_YEAR_AUDIT_INPUT">
		<div id="_year_audit_input" class="west-notree"  ><a href="${qsmctx}/inner-audit/year-audit/input.htm">年度审核计划</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_YEAR_AUDIT_LIST">
		<div id="_year_audit_list" class="west-notree"  ><a href="${qsmctx}/inner-audit/year-audit/list.htm">年度审核计划台账</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_AUDIT_PLAN_INPUT">
		<div id="_audit_plan_input" class="west-notree"  ><a href="${qsmctx}/inner-audit/audit-plan/input.htm">内审计划与实施</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_AUDIT_PLAN_LIST">
		<div id="_audit_plan_list" class="west-notree"  ><a href="${qsmctx}/inner-audit/audit-plan/list.htm">内审计划与实施台账</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_PROBLEMS_INPUT">
		<div id="_problems_input" class="west-notree"  ><a href="${qsmctx}/inner-audit/problems/input.htm">内审问题点改善报告</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_PROBLEMS_LIST">
		<div id="_problems_list" class="west-notree"  ><a href="${qsmctx}/inner-audit/problems/list.htm">内审问题点改善台账</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_AUDITOR_LIBRARY_LIST">
		<div id="_auditor_library_list" class="west-notree"  ><a href="${qsmctx}/inner-audit/auditor-library/list.htm">内审员备选库</a></div>
	</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
</script>