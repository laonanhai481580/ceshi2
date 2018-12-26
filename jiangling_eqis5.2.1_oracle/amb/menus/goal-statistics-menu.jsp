<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="GOAL_GOAL-STATISTICS_KPI-MONITOR_INPUT">
<%-- <%
	List<MonitorPermissions> monitorPermissionss = (List<MonitorPermissions>)request.getAttribute("monitorPermissionss");
	if(monitorPermissionss.size()>0){
		for(MonitorPermissions monitorPermissions : monitorPermissionss){
			%>
				<div id="monitorPermissions_<%=monitorPermissions.getId() %>" class="west-notree" url="${goalctx}/goal-statistics/kpi-monitor.htm?monitorPermissionsId=<%=monitorPermissions.getId() %>&attachUrl=<%=monitorPermissions.getAttachUrl() %>"  onclick="changeMenu(this);"><span><%=monitorPermissions.getName() %></span></div>
			<%
		}
	}
%> --%>
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>