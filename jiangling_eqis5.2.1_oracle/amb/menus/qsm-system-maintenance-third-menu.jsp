<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="QSM_SYSTEM_MAINTENANCE_LIST">
		<div id="_system_maintenance" class="west-notree"  ><a href="${qsmctx}/system-maintenance/list.htm">体系维护台账</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>