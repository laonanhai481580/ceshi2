<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="GOAL_GOAL-STATISTICS_KPI-EXAMINATION_INPUT">
		<div id="_table" class="west-notree" url="${goalctx}/goal-statistics/kpi-examination.htm" onclick="changeMenu(this);"><span>KPI考核表</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="goal_kpi_input">
		<div id="_kpi_input" class="west-notree" url="${goalctx}/goal-statistics/kpi-input.htm" onclick="changeMenu(this);"><span>KPI手工数据维护</span></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>