<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="QSM_MANAGE_REVIEW_INPUT">
		<div id="_manage_review_input" class="west-notree"  ><a href="${qsmctx}/manage-review/input.htm">管理评审表</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_MANAGE_REVIEW_LIST">
		<div id="_manage_review_list" class="west-notree"  ><a href="${qsmctx}/manage-review/list.htm">管理评审表台账</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>