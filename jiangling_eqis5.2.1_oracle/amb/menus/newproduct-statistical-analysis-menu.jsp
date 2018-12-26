<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="new_statistical_analysis">
	<div id="_week_delay_query" class="west-notree" 
		url="${newproductctx}/statistical-analysis/week-delay.htm"
		onclick="changeMenu(this);"> 
		<span>问题统计汇总表</span>
	</div>
</security:authorize>

<security:authorize ifAnyGranted="new_statistical_analysis_weekOverdue">
	<div id="_week_overdue_query" class="west-notree" 
		url="${newproductctx}/statistical-analysis/week-overdue.htm" 
		onclick="changeMenu(this);">
		<span>周延迟数据查询</span>
	</div>
</security:authorize>

<security:authorize ifAnyGranted="new_statistical_analysis_nextExpected">
	<div id="_next_expected_query" class="west-notree" 
		url="${newproductctx }/statistical-analysis/next-expected.htm" 
		onclick="changeMenu(this);">
		<span>预计完成项目</span>
	</div>
</security:authorize>

<security:authorize ifAnyGranted="new_statistical_analysis_problemCompletion">
	<div id="_problem_completion_query" class="west-notree" 
		url="${newproductctx }/statistical-analysis/problem-completion.htm" 
		onclick="changeMenu(this);">
		<span>未按时完成项</span></div>
</security:authorize>

<security:authorize ifAnyGranted="new_statistical_analysis_problemType">
	<div id="_problem_type_query" class="west-notree" 
		url="${newproductctx }/statistical-analysis/problem-type.htm" 
		onclick="changeMenu(this);">
		<span>问题类别数据查询</span></div>
</security:authorize>

<security:authorize ifAnyGranted="new_statistical_analysis_problemLevel">
	<div id="_problem_level_query" class="west-notree" 
		url="${newproductctx }/statistical-analysis/problem-level.htm" 
		onclick="changeMenu(this);">
		<span>问题级别数据查询</span></div>
</security:authorize>

<security:authorize ifAnyGranted="new_statistical_analysis_problemState">
	<div id="_problem_state_query" class="west-notree" 
		url="${newproductctx }/statistical-analysis/problem-state.htm" 
		onclick="changeMenu(this);">
		<span>问题整改状态数据查询</span></div>
</security:authorize>

<security:authorize ifAnyGranted="new_statistical_analysis_carType">
	<div id="_car_type_query" class="west-notree" 
		url="${newproductctx }/statistical-analysis/car-type.htm" 
		onclick="changeMenu(this);">
		<span>车型配置数据查询</span></div>
</security:authorize>
<security:authorize ifAnyGranted="new_statistical_analysis_close_rate">
	<div id="_problem_close_rate" class="west-notree" 
		url="${newproductctx }/statistical-analysis/problem-close-rate.htm" 
		onclick="changeMenu(this);">
		<span>质量问题关闭率趋势</span></div>
</security:authorize>
<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>