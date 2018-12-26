<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="GOAL_GOAL-TREND_AMOUNT_INPUT">
		<div id="_amount" class="west-notree" url="${goalctx}/goal-trend/amount.htm" onclick="changeMenu(this);"><span>内部损失推移图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="GOAL_GOAL-TREND_OUT-COST_INPUT">
	<div id="_out_cost" class="west-notree" url="${goalctx}/goal-trend/out-cost.htm" onclick="changeMenu(this);"><span>外部损失推移图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="GOAL_GOAL-TREND_COMPLAIN-AMOUNT_INPUT">
<%-- 		<div id="_complain_amount" class="west-notree" url="${goalctx}/goal-trend/complain-amount.htm" onclick="changeMenu(this);"><span>投诉退货数统计</span></div> --%>
	</security:authorize>
	<security:authorize ifAnyGranted="GOAL_GOAL-TREND_PLAN_INPUT">
<%-- 		<div id="_complete_rate" class="west-notree" url="${goalctx}/goal-trend/plan.htm" onclick="changeMenu(this);"><span>生产计划达成率</span></div> --%>
	</security:authorize>
	<%-- <div id="_qualify_rate" class="west-notree" url="${goalctx}/goal-trend/trend.htm" onclick="changeMenu(this);"><span>制造检验合格率</span></div> --%>
	<security:authorize ifAnyGranted="GOAL_GOAL-TREND_IQC-QUALITY-RATE_INPUT">
		<div id="_iqc_quality_rate" class="west-notree" url="${goalctx}/goal-trend/iqc-quality-rate.htm" onclick="changeMenu(this);"><span>进货检验合格率</span></div>
	</security:authorize>
	<%-- <div id="_efficiency_rate" class="west-notree" url="${goalctx}/goal-trend/trend.htm" onclick="changeMenu(this);"><span>效率</span></div> --%>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>