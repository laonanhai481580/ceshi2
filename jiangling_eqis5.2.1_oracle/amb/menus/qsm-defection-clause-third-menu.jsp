<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="QSM_DEFECTION_CLAUSE_LIST">
		<div id="_defection_clause" class="west-notree"  ><a href="${qsmctx}/defection-clause/list.htm">不符合条款维护</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>