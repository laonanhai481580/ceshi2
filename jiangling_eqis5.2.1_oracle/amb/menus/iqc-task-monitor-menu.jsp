<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<%-- 	<security:authorize ifAnyGranted="taskmonitor-task-list">
		<div id="_task_list" class="west-notree" url="${iqcctx}/taskmonitor/task-list.htm" onclick="changeMenu(this);"><span>检验任务邮件通知维护</span></div>
	</security:authorize> --%>
	<security:authorize ifAnyGranted="taskmonitor-overdue-list">
		<div id="_overdue_list" class="west-notree" url="${iqcctx }/taskmonitor/overdue-list.htm" onclick="changeMenu(this);"><span>报检超期邮件通知维护</span></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>