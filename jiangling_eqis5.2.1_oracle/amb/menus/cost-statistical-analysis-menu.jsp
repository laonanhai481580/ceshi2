<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="cost_statistical_cost_total">
<div id="_composing_cost_total" class="west-notree" url="${costctx}/statistical-analysis/cost-total.htm" onclick="changeMenu(this);"><span>质量成本报表</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_statistical_cost_all">
<div id="_cost_all" class="west-notree" url="${costctx}/statistical-analysis/cost-all.htm" onclick="changeMenu(this);"><span>质量成本统计表</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_statistical_cost_analysis">
<div id="_cost_analysis" class="west-notree" url="${costctx}/statistical-analysis/cost-analysis.htm" onclick="changeMenu(this);"><span>质量成本推移图</span></div>
</security:authorize>
<security:authorize ifAnyGranted="">
<div id="_composing_cost_info" class="west-notree" url="${costctx}/loadinfo/list.htm" onclick="changeMenu(this);"><span>质量成本金额明细</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_anayse_xx">
<div id="_cost_anayse" class="west-notree" url="${costctx}/statistical-analysis/interior-loss-rate-trend-list.htm" onclick="changeMenu(this);"><span>质量成本金额推移图</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_cost_record_list">
<div id="_composing_cost_record" class="west-notree" url="${costctx}/cost-record/list.htm" onclick="changeMenu(this);"><span>手工维护成本数据</span></div>
</security:authorize>
<security:authorize ifAnyGranted="cost_cost_record_collection_list">
<div id="_composing_cost_record_collection" class="west-notree"  url="${costctx}/cost-record/collection-list.htm" onclick="changeMenu(this);"><span>集成成本数据查询</span></div>
</security:authorize>
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>