<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="GOAL_GOAL-TREND_AMOUNT_INPUT,GOAL_GOAL-TREND_OUT-COST_INPUT,GOAL_GOAL-TREND_COMPLAIN-AMOUNT_INPUT,GOAL_GOAL-TREND_PLAN_INPUT,GOAL_GOAL-TREND_IQC-QUALITY-RATE_INPUT">
		<li id="trend">
			<span><span>
				<a href='<grid:authorize code="GOAL_GOAL-TREND_AMOUNT_INPUT,GOAL_GOAL-TREND_OUT-COST_INPUT,GOAL_GOAL-TREND_COMPLAIN-AMOUNT_INPUT,GOAL_GOAL-TREND_PLAN_INPUT,GOAL_GOAL-TREND_IQC-QUALITY-RATE_INPUT" systemCode="goal"></grid:authorize>'>企业管理看板</a>
			</span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="GOAL_GOAL-STATISTICS_KPI-MONITOR_INPUT">
		<li id="targetmonitor"><span><span><a href="<grid:authorize code="GOAL_GOAL-STATISTICS_KPI-MONITOR_INPUT" systemCode="goal"></grid:authorize>">目标监控</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="GOAL_GOAL-STATISTICS_KPI-EXAMINATION_INPUT">
		<li id="kpitable"><span><span><a href="<grid:authorize code="GOAL_GOAL-STATISTICS_KPI-EXAMINATION_INPUT" systemCode="goal"></grid:authorize>">KPI考核表</a></span></span></li>
	</security:authorize>
	<security:authorize ifAnyGranted="GOAL-BASE-INFO-DATABASESETTING-LIST,GOAL-BASE-INFO-DATASOURCE-LIST,GOAL-BASE-INFO-METADATA-LIST,GOAL-BASE-INFO-INDEXDATA-LIST,GOAL_GOAL-ITEM_LIST_LIST,GOAL_GOAL-ITEM-SECURITY_LIST_LIST,GOAL_GOAL-STATISTICS_MONITOR-LIST_LIST">
		<li id="basic"><span><span><a href="<grid:authorize code="GOAL-BASE-INFO-DATABASESETTING-LIST,GOAL-BASE-INFO-DATASOURCE-LIST,GOAL-BASE-INFO-METADATA-LIST,GOAL-BASE-INFO-INDEXDATA-LIST,GOAL_GOAL-ITEM_LIST_LIST,GOAL_GOAL-ITEM-SECURITY_LIST_LIST,GOAL_GOAL-STATISTICS_MONITOR-LIST_LIST" systemCode="goal"></grid:authorize>">基础维护</a></span></span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
    var topMenu='sysPlan';
	$('#'+secMenu).addClass('sec-selected');
</script>